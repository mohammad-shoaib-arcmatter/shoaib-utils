import os
from pathlib import Path
from image_filename_processor import process_folder


DEFAULT_ROOT = Path(r"C:\Users\shoaib\Downloads\QuickShare")


def main(root: Path = DEFAULT_ROOT) -> None:
    if not root.is_dir():
        return

    for folder_name, _, filenames in os.walk(root):
        process_folder(Path(folder_name), filenames)


if __name__ == "__main__":
    main()
