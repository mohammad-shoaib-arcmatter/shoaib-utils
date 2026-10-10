"""Convert a .docx file to Markdown. Requires python-docx."""

from __future__ import annotations

import argparse
import re
from pathlib import Path
from typing import Optional, Union

from docx import Document
from docx.oxml.ns import qn
from docx.table import Table
from docx.text.paragraph import Paragraph
from docx.text.run import Run


def _blocks(document):
	"""Yield top-level paragraphs and tables in document order."""
	for element in document.element.body.iterchildren():
		if element.tag == qn("w:p"):
			yield Paragraph(element, document)
		elif element.tag == qn("w:tbl"):
			yield Table(element, document)


def _format_run(run: Run) -> str:
	text = run.text
	if not text:
		return ""
	if run.font.strike:
		text = f"~~{text}~~"
	if run.bold:
		text = f"**{text}**"
	if run.italic:
		text = f"*{text}*"
	if run.font.subscript:
		text = f"~{text}~"
	elif run.font.superscript:
		text = f"^{text}^"
	if run.style and "code" in run.style.name.lower():
		escaped = text.replace("`", "\\`")
		text = f"`{escaped}`"
	return text


def _paragraph_text(paragraph: Paragraph) -> str:
	"""Render paragraph runs and hyperlinks, preserving inline formatting."""
	pieces = []
	for element in paragraph._p.iterchildren():
		if element.tag == qn("w:r"):
			pieces.append(_format_run(Run(element, paragraph)))
		elif element.tag == qn("w:hyperlink"):
			label = "".join(
				_format_run(Run(run, paragraph))
				for run in element.findall(qn("w:r"))
			)
			rel_id = element.get(qn("r:id"))
			relationship = paragraph.part.rels.get(rel_id) if rel_id else None
			target = relationship.target_ref if relationship else None
			pieces.append(f"[{label}]({target})" if target else label)
	return "".join(pieces).strip()


def _list_marker(paragraph: Paragraph) -> Optional[str]:
	style = paragraph.style.name.lower() if paragraph.style else ""
	if "bullet" in style:
		return "- "
	if "number" in style:
		return "1. "
	return None


def _table_markdown(table: Table) -> str:
	rows = []
	for row in table.rows:
		cells = []
		for cell in row.cells:
			value = " ".join(_paragraph_text(p) for p in cell.paragraphs).strip()
			cells.append(value.replace("|", r"\|").replace("\n", "<br>"))
		rows.append("| " + " | ".join(cells) + " |")
	if not rows:
		return ""
	divider = "| " + " | ".join("---" for _ in table.rows[0].cells) + " |"
	return "\n".join([rows[0], divider, *rows[1:]])


def docx_to_markdown(
	docx_path: Union[str, Path],
	markdown_path: Optional[Union[str, Path]] = None,
) -> str:
	"""Convert a Word document to Markdown, optionally writing the result."""
	document = Document(str(docx_path))
	output = []
	for block in _blocks(document):
		if isinstance(block, Table):
			rendered = _table_markdown(block)
		else:
			text = _paragraph_text(block)
			if not text:
				continue
			style = block.style.name.lower() if block.style else ""
			heading = re.fullmatch(r"heading\s+(\d+)", style)
			if heading:
				rendered = f"{'#' * min(int(heading.group(1)), 6)} {text}"
			elif style == "title":
				rendered = f"# {text}"
			elif style == "subtitle":
				rendered = f"## {text}"
			elif style.startswith("quote") or "blockquote" in style:
				rendered = "> " + text.replace("\n", "\n> ")
			else:
				marker = _list_marker(block)
				rendered = marker + text if marker else text
		if rendered:
			output.append(rendered)

	markdown = "\n\n".join(output).rstrip() + "\n"
	if markdown_path is not None:
		Path(markdown_path).write_text(markdown, encoding="utf-8")
	return markdown


def main() -> None:
	parser = argparse.ArgumentParser(description="Convert DOCX to Markdown.")
	parser.add_argument("docx", help="Input .docx file")
	parser.add_argument("markdown", nargs="?", help="Output .md file")
	args = parser.parse_args()
	output = args.markdown or str(Path(args.docx).with_suffix(".md"))
	docx_to_markdown(args.docx, output)


if __name__ == "__main__":
	main()
