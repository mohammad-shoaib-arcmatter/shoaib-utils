"""Convert Markdown files in a directory to DOCX using pypandoc."""

import argparse
from pathlib import Path

import pypandoc


def convert_directory(input_dir: Path, output_dir: Path) -> None:
	input_dir = input_dir.expanduser().resolve()
	output_dir = output_dir.expanduser().resolve()
	if not input_dir.is_dir():
		raise NotADirectoryError(f"Input directory does not exist: {input_dir}")

	output_dir.mkdir(parents=True, exist_ok=True)
	for markdown_file in sorted(input_dir.glob("*.md")):
		output_file = output_dir / f"{markdown_file.stem}.docx"
		pypandoc.convert_file(
			str(markdown_file),
			to="docx",
			format="md",
			outputfile=str(output_file),
		)
		print(f"Converted {markdown_file.name} -> {output_file}")


def main() -> None:
	parser = argparse.ArgumentParser(
		description="Convert Markdown files in a directory to DOCX."
	)
	parser.add_argument("input_dir", type=Path, help="Directory containing .md files")
	parser.add_argument(
		"-o",
		"--output-dir",
		type=Path,
		help="Output directory (defaults to the input directory)",
	)
	args = parser.parse_args()
	convert_directory(args.input_dir, args.output_dir or args.input_dir)


if __name__ == "__main__":
	main()
