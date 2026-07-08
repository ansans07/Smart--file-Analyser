# Smart File Organizer

A Core Java console application that scans a folder, categorizes files, searches files, detects duplicate files using SHA-256 hashing, safely moves duplicates to a recovery folder, organizes files by category, and exports a CSV report.

## Why this project?

This project demonstrates practical Java skills without requiring web development or frameworks. It focuses on OOP, Collections, File Handling, Exception Handling, Recursion, Sorting, and Hashing.

## Features

- Scan any folder recursively
- Categorize files into Documents, Images, Videos, Audio, Spreadsheets, Archives, Code, and Others
- Show category-wise statistics and total storage used
- Search files by filename keyword
- Detect duplicate files using SHA-256 content hashing
- Move duplicate files safely to a recovery folder instead of permanently deleting them
- Organize files into category folders without deleting originals
- Export a CSV report containing file name, path, extension, category, size, and last modified time
- Show the largest files in a scanned folder

## Tech Stack

- Java 17+
- Core Java
- Java Collections Framework
- Java NIO File API
- SHA-256 hashing using `MessageDigest`

## Project Structure

```text
SmartFileOrganizer/
├── src/com/ansika/fileorganizer/
│   ├── Main.java
│   ├── FileRecord.java
│   ├── FileCategory.java
│   ├── FileScannerService.java
│   ├── SearchService.java
│   ├── StatsService.java
│   ├── DuplicateFinder.java
│   ├── DuplicateRemovalService.java
│   ├── OrganizerService.java
│   ├── ReportExporter.java
│   └── HashUtil.java
├── sample_workspace/
├── docs/
│   └── INTERVIEW_NOTES.md
├── reports/
│   └── sample_report.csv
├── .gitignore
└── README.md
```

## How to Run in IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Click **Open**.
3. Select the full `SmartFileOrganizer` folder, not just `src`.
4. Open `Main.java`.
5. Right-click inside the file and choose **Run 'Main.main()'**.

## How to Run from Terminal

From inside the project folder:

```bash
javac -d out src/com/ansika/fileorganizer/*.java
java -cp out com.ansika.fileorganizer.Main
```

## Menu Options

```text
1. Scan folder
2. Show category statistics
3. Search files by name
4. Detect duplicate files
5. Organize files into category folders
6. Export CSV report
7. Show largest files
8. Move duplicate files to recovery folder
0. Exit
```

## Suggested Test Flow

Use the included `sample_workspace` folder first.

Example folder path on Mac:

```text
/Users/yourname/Downloads/SmartFileOrganizer/sample_workspace
```

Recommended order:

1. Choose option `1` and scan `sample_workspace`.
2. Choose option `2` to see category statistics.
3. Choose option `3` and search for `notes`.
4. Choose option `4` to detect duplicates.
5. Choose option `7` and enter `5` to show largest files.
6. Choose option `6` and export a CSV report.
7. Choose option `5` and copy organized files into a separate output folder.
8. Choose option `8` and move duplicate files into a recovery folder.

## Duplicate Detection Logic

The project uses SHA-256 hashing to compare file contents.

Two files are considered duplicates if their SHA-256 hashes are the same, even if their names are different.

Example:

```text
notes.txt       -> same SHA-256 hash
notes_copy.txt  -> same SHA-256 hash
```

This is more reliable than comparing only filenames or file sizes.

## Safety Design

The project avoids destructive operations:

- Organizing files copies them into category folders.
- Duplicate removal moves extra copies to a recovery folder.
- One copy from each duplicate group is always kept.
- The recovery folder is required to be outside the scanned folder.

## Java Concepts Used

- OOP and class design
- Encapsulation
- Java Collections: `List`, `ArrayList`, `Map`, `HashMap`, `EnumMap`
- File handling with `Path`, `Files`, and `FileVisitor`-style traversal through `Files.walk`
- Exception handling
- Recursion-like directory traversal
- Sorting using Comparators
- SHA-256 hashing using `MessageDigest`
- CSV export
- Console-based menu programming

## Resume Description

**Smart File Organizer | Java, OOP, Collections, File Handling**  
Built a Core Java console application to scan folders recursively, categorize files, search by filename, detect duplicate files using SHA-256 hashing, organize files into category folders, export CSV reports, and safely move duplicate files to a recovery folder.

## Future Improvements

- Add undo for duplicate recovery and organization
- Add filters by file size/date/type
- Add GUI using JavaFX
- Add scheduled automatic scans
- Add file preview for text files
