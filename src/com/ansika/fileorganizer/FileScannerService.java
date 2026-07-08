package com.ansika.fileorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileScannerService {
    public List<FileRecord> scan(Path folder) throws IOException {
        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            throw new IllegalArgumentException("Invalid folder path: " + folder);
        }

        List<FileRecord> records = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String extension = getExtension(path.getFileName().toString());
                            FileCategory category = FileCategory.fromExtension(extension);
                            long size = Files.size(path);
                            Instant modified = Files.getLastModifiedTime(path).toInstant();
                            records.add(new FileRecord(path, extension, size, category, modified));
                        } catch (IOException e) {
                            System.out.println("Skipping file: " + path + " (" + e.getMessage() + ")");
                        }
                    });
        }
        return records;
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(dotIndex + 1).toLowerCase();
    }
}
