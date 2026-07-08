package com.ansika.fileorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final FileScannerService scannerService = new FileScannerService();
    private static final OrganizerService organizerService = new OrganizerService();
    private static final DuplicateFinder duplicateFinder = new DuplicateFinder();
    private static final SearchService searchService = new SearchService();
    private static final StatsService statsService = new StatsService();
    private static final ReportExporter reportExporter = new ReportExporter();
    private static final DuplicateRemovalService duplicateRemovalService = new DuplicateRemovalService();

    private static List<FileRecord> currentRecords = new ArrayList<>();
    private static Path currentFolder;

    public static void main(String[] args) {
        System.out.println("==== Smart File Organizer ====");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> scanFolder();
                    case "2" -> showStats();
                    case "3" -> searchFiles();
                    case "4" -> showDuplicates();
                    case "5" -> organizeFiles();
                    case "6" -> exportReport();
                    case "7" -> showLargestFiles();
                    case "8" -> moveDuplicateFiles();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid choice. Please choose a number from the menu.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\n1. Scan folder");
        System.out.println("2. Show category statistics");
        System.out.println("3. Search files by name");
        System.out.println("4. Detect duplicate files");
        System.out.println("5. Organize files into category folders");
        System.out.println("6. Export CSV report");
        System.out.println("7. Show largest files");
        System.out.println("8. Move duplicate files to recovery folder");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }

    private static void scanFolder() throws IOException {
        Path folder = readPath("Enter folder path: ");
        currentFolder = folder;
        currentRecords = scannerService.scan(currentFolder);
        System.out.println("Scanned " + currentRecords.size() + " files from " + currentFolder.toAbsolutePath());
    }

    private static void showStats() {
        ensureScanned();
        System.out.println("\nTotal files: " + currentRecords.size());
        System.out.println("Total size: " + statsService.readableSize(statsService.totalSize(currentRecords)));
        System.out.println("\nFiles by category:");
        for (Map.Entry<FileCategory, Long> entry : statsService.countByCategory(currentRecords).entrySet()) {
            if (entry.getValue() > 0) {
                long categorySize = statsService.totalSizeByCategory(currentRecords, entry.getKey());
                System.out.println(entry.getKey() + ": " + entry.getValue() + " files, " + statsService.readableSize(categorySize));
            }
        }
    }

    private static void searchFiles() {
        ensureScanned();
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        List<FileRecord> results = searchService.searchByName(currentRecords, keyword);
        if (results.isEmpty()) {
            System.out.println("No matching files found.");
            return;
        }
        for (FileRecord record : results) {
            System.out.println(record.getName() + " | " + record.getCategory() + " | " + record.getReadableSize() + " | " + record.getPath());
        }
    }

    private static void showDuplicates() {
        ensureScanned();
        Map<String, List<FileRecord>> duplicates = duplicateFinder.findDuplicates(currentRecords);
        if (duplicates.isEmpty()) {
            System.out.println("No duplicate files found.");
            return;
        }
        int group = 1;
        for (List<FileRecord> files : duplicates.values()) {
            System.out.println("\nDuplicate Group " + group++ + ":");
            for (FileRecord record : files) {
                System.out.println("- " + record.getPath() + " (" + record.getReadableSize() + ")");
            }
        }
    }

    private static void organizeFiles() throws IOException {
        ensureScanned();
        Path output = readPath("Enter output folder path: ");
        int copied = organizerService.organizeByCategory(currentRecords, output);
        System.out.println("Copied " + copied + " files into category folders at: " + output.toAbsolutePath());
        System.out.println("Original files were not deleted.");
    }

    private static void exportReport() throws IOException {
        ensureScanned();
        Path output = readPath("Enter output CSV path, example report.csv: ");
        reportExporter.exportCsv(currentRecords, output);
        System.out.println("Report exported to: " + output.toAbsolutePath());
    }

    private static void showLargestFiles() {
        ensureScanned();
        int limit = readPositiveInt("How many files to show? ");
        List<FileRecord> largest = statsService.largestFiles(currentRecords, limit);
        for (FileRecord record : largest) {
            System.out.println(record.getReadableSize() + " | " + record.getName() + " | " + record.getPath());
        }
    }

    private static void moveDuplicateFiles() throws IOException {
        ensureScanned();
        Map<String, List<FileRecord>> duplicates = duplicateFinder.findDuplicates(currentRecords);
        if (duplicates.isEmpty()) {
            System.out.println("No duplicate files found.");
            return;
        }

        System.out.println("\nDuplicate files found:");
        int group = 1;
        long recoverableBytes = 0;
        int duplicateFileCount = 0;

        for (List<FileRecord> files : duplicates.values()) {
            System.out.println("\nDuplicate Group " + group++ + ":");
            System.out.println("Keeping: " + files.get(0).getPath());
            for (int i = 1; i < files.size(); i++) {
                FileRecord duplicate = files.get(i);
                System.out.println("Moving:  " + duplicate.getPath() + " (" + duplicate.getReadableSize() + ")");
                recoverableBytes += duplicate.getSizeBytes();
                duplicateFileCount++;
            }
        }

        System.out.println("\nDuplicate files to move: " + duplicateFileCount);
        System.out.println("Recoverable space: " + statsService.readableSize(recoverableBytes));
        Path recoveryFolder = readPath("Enter recovery folder path: ");

        if (isSubPath(recoveryFolder, currentFolder)) {
            System.out.println("For safety, choose a recovery folder outside the scanned folder.");
            return;
        }

        System.out.print("Move duplicates to this folder? Type YES to confirm: ");
        String confirmation = scanner.nextLine().trim();

        if (!confirmation.equals("YES")) {
            System.out.println("Cancelled. No files were moved.");
            return;
        }

        int moved = duplicateRemovalService.moveDuplicatesToRecoveryFolder(duplicates, recoveryFolder);
        System.out.println("Moved " + moved + " duplicate files to: " + recoveryFolder.toAbsolutePath());
        System.out.println("One copy from each duplicate group was kept in the original folder.");
        if (currentFolder != null) {
            currentRecords = scannerService.scan(currentFolder);
            System.out.println("Folder re-scanned. Current file count: " + currentRecords.size());
        }
    }

    private static Path readPath(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be empty.");
        }
        return Path.of(input);
    }

    private static int readPositiveInt(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            int value = Integer.parseInt(input);
            if (value <= 0) {
                throw new IllegalArgumentException("Number must be greater than zero.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }

    private static boolean isSubPath(Path child, Path parent) throws IOException {
        if (child == null || parent == null || !Files.exists(parent)) {
            return false;
        }
        Path normalizedChild = child.toAbsolutePath().normalize();
        Path normalizedParent = parent.toRealPath().normalize();
        return normalizedChild.startsWith(normalizedParent);
    }

    private static void ensureScanned() {
        if (currentRecords == null || currentRecords.isEmpty()) {
            throw new IllegalStateException("Scan a folder first using option 1.");
        }
    }
}
