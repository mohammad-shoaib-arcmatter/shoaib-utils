import os
from collections import Counter, defaultdict
from datetime import datetime
from pathlib import Path
from typing import DefaultDict, List, Optional, Tuple
from uuid import uuid4

MONTH_DESC = {
    "01": "Jan",
    "02": "Feb",
    "03": "Mar",
    "04": "Apr",
    "05": "May",
    "06": "Jun",
    "07": "Jul",
    "08": "Aug",
    "09": "Sep",
    "10": "Oct",
    "11": "Nov",
    "12": "Dec",
}


def get_date_from_filename(filename: str) -> Optional[str]:
    if "-" in filename:
        date_text = filename[:4] + filename[5:7] + filename[11:13]
    else:
        date_text = filename[:8]

    if len(date_text) != 8 or not date_text.isdigit():
        return None

    try:
        datetime.strptime(date_text, "%Y%m%d")
    except ValueError:
        return None

    return date_text


def get_filename_without_sequence(filename: str) -> str:
    filename_without_extension = os.path.splitext(filename)[0]
    name_parts = filename_without_extension.split(" ")
    return " ".join(name_parts[:-1])


def get_folder_description(filename: str) -> str:
    filename_without_sequence = get_filename_without_sequence(filename)
    return filename_without_sequence.partition(" ")[2]


def format_full_filename(date_text: str, folder: Path) -> str:
    folder_description = folder.name.partition(" ")[2]
    date = datetime.strptime(date_text, "%Y%m%d")
    filename = (
        f"{date.year:04d}-{date.month:02d}{MONTH_DESC[date_text[4:6]]}"
        f"-{date.day:02d}"
    )
    if folder_description:
        filename += f" {folder_description}"
    return filename


def process_folder(folder: Path, filenames: Optional[List[str]] = None) -> None:
    if filenames is None:
        files = [path for path in folder.iterdir() if path.is_file()]
    else:
        files = [folder / filename for filename in filenames]
    files.sort(key=lambda path: path.name)
    dated_files: List[Tuple[Path, str]] = []
    for path in files:
        date_text = get_date_from_filename(path.name)
        if date_text is not None:
            dated_files.append((path, date_text))

    if not dated_files:
        return

    split_by_date = not bool(get_folder_description(dated_files[0][0].name))
    staged_files: List[Tuple[Path, str, str]] = []
    for path, date_text in dated_files:
        stem, extension = os.path.splitext(path.name)
        staged_name = f"{stem}_{extension}"
        staged_files.append((path, date_text, staged_name))

    staged_files.sort(key=lambda item: item[2])
    date_counts = Counter(date_text for _, date_text, _ in staged_files)
    date_sequences: DefaultDict[str, int] = defaultdict(int)
    overall_sequence = 0
    overall_width = max(2, len(str(len(staged_files))))
    rename_plan: List[Tuple[Path, str]] = []

    for path, date_text, staged_name in staged_files:
        if split_by_date:
            date_sequences[date_text] += 1
            sequence = date_sequences[date_text]
            sequence_width = max(2, len(str(date_counts[date_text])))
        else:
            overall_sequence += 1
            sequence = overall_sequence
            sequence_width = overall_width

        if "-" in staged_name:
            filename_without_sequence = get_filename_without_sequence(staged_name)
        else:
            filename_without_sequence = format_full_filename(date_text, folder)

        extension = os.path.splitext(staged_name)[1]
        new_name = (
            f"{filename_without_sequence} "
            f"{sequence:0{sequence_width}d}{extension}"
        )
        rename_plan.append((path, new_name))

    source_names = {path.name for path, _, _ in staged_files}
    destination_names = set()
    for _, destination_name in rename_plan:
        if destination_name in destination_names:
            raise FileExistsError(
                f"Multiple files in {folder} would be renamed to {destination_name}"
            )
        destination_names.add(destination_name)

        destination = folder / destination_name
        if destination.exists() and destination_name not in source_names:
            raise FileExistsError(f"Rename destination already exists: {destination}")

    temporary_files: List[Tuple[Path, Path]] = []
    for source, _ in rename_plan:
        temporary = folder / f".imagefilename-{uuid4().hex}.tmp"
        while temporary.exists():
            temporary = folder / f".imagefilename-{uuid4().hex}.tmp"
        source.rename(temporary)
        temporary_files.append((source, temporary))

    for (source, destination_name), (_, temporary) in zip(
        rename_plan, temporary_files
    ):
        temporary.rename(source.with_name(destination_name))
