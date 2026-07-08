# Interview Notes - Smart File Organizer

## One-minute project explanation

Smart File Organizer is a Core Java console application that scans a folder recursively and helps users manage files. It categorizes files by extension, searches files by name, shows storage statistics, detects duplicate files using SHA-256 hashing, exports a CSV report, and safely moves duplicate files to a recovery folder while keeping one original copy.

## Why I built it

I wanted one practical Java project that demonstrates OOP, Collections, File Handling, Exception Handling, and clean class separation without using web frameworks. The project solves a real problem: messy folders and duplicate files.

## Main classes

### Main
Handles the console menu and calls service classes based on the user's choice.

### FileRecord
Represents one scanned file. It stores file path, name, extension, size, category, and last modified time.

### FileCategory
An enum that maps file extensions to categories like Documents, Images, Audio, Videos, Spreadsheets, Archives, Code, and Others.

### FileScannerService
Scans a folder recursively using `Files.walk()` and creates a list of `FileRecord` objects.

### SearchService
Searches scanned files by checking whether the filename contains the user's keyword.

### StatsService
Calculates total size, category-wise count, category-wise storage, and largest files.

### DuplicateFinder
Computes SHA-256 hashes for files and groups files with the same hash using a `HashMap`.

### DuplicateRemovalService
Keeps one file from each duplicate group and moves remaining duplicate files to a recovery folder.

### OrganizerService
Copies files into category folders based on their file category.

### ReportExporter
Exports scanned file details to a CSV report.

### HashUtil
Generates SHA-256 hashes from file contents using `MessageDigest`.

## Important data structures

### ArrayList
Used to store all scanned file records.

### HashMap
Used to group duplicate files by hash.

```java
Map<String, List<FileRecord>>
```

Key: SHA-256 hash  
Value: files having that hash

### EnumMap
Useful for category-wise statistics because keys are enum values.

## How duplicate detection works

1. Read each file as bytes.
2. Generate SHA-256 hash using `MessageDigest`.
3. Store the hash in a map.
4. If multiple files have the same hash, they are duplicates.

This works even if duplicate files have different names.

## Why SHA-256 instead of filename or size?

- Same filename does not guarantee same content.
- Same size does not guarantee same content.
- SHA-256 is based on actual file contents, so it is much more reliable.

## Why move duplicates instead of deleting?

Deleting files permanently is risky. Moving them to a recovery folder is safer because the user can restore them if needed.

## Time complexity

Let `n` be the number of files and `S` be the total file size.

- Scanning files: `O(n)` plus directory traversal cost
- Searching by filename: `O(n)`
- Category statistics: `O(n)`
- Largest files: `O(n log n)` due to sorting
- Duplicate detection: `O(S)` because file contents must be read to compute hashes

## Possible interview questions

### Why did you use OOP here?
To separate responsibilities. Scanning, searching, statistics, organizing, report exporting, and duplicate detection are different responsibilities, so they are handled by different classes.

### Why did you use HashMap for duplicates?
A HashMap gives fast lookup. The SHA-256 hash is the key, and all files with the same hash are stored in a list.

### How would you improve this project?
I would add undo functionality, filters by size/date/type, a JavaFX GUI, and scheduled automatic scans.

### What happens if the folder has very large files?
Hashing large files takes more time because the program must read the file contents. The code reads files in chunks using a buffer, so it does not load the entire file into memory at once.

### What did you learn?
I learned practical file handling, recursive folder scanning, hashing, Java collections, service-based class design, and safe file operations.
