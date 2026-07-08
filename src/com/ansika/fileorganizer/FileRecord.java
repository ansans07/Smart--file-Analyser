package com.ansika.fileorganizer;

import java.nio.file.Path;
import java.time.Instant;

public class FileRecord {
    private final Path path;
    private final String name;
    private final String extension;
    private final long sizeBytes;
    private final FileCategory category;
    private final Instant lastModified;

    public FileRecord(Path path, String extension, long sizeBytes, FileCategory category, Instant lastModified) {
        this.path = path;
        this.name = path.getFileName().toString();
        this.extension = extension;
        this.sizeBytes = sizeBytes;
        this.category = category;
        this.lastModified = lastModified;
    }

    public Path getPath() { return path; }
    public String getName() { return name; }
    public String getExtension() { return extension; }
    public long getSizeBytes() { return sizeBytes; }
    public FileCategory getCategory() { return category; }
    public Instant getLastModified() { return lastModified; }

    public String getReadableSize() {
        double size = sizeBytes;
        String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = 0;
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        return String.format("%.2f %s", size, units[unitIndex]);
    }
}
