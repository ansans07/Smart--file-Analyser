package com.ansika.fileorganizer;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsService {
    public Map<FileCategory, Long> countByCategory(List<FileRecord> records) {
        Map<FileCategory, Long> result = new EnumMap<>(FileCategory.class);
        for (FileCategory category : FileCategory.values()) {
            result.put(category, 0L);
        }
        for (FileRecord record : records) {
            result.put(record.getCategory(), result.get(record.getCategory()) + 1);
        }
        return result;
    }

    public long totalSize(List<FileRecord> records) {
        return records.stream().mapToLong(FileRecord::getSizeBytes).sum();
    }


    public long totalSizeByCategory(List<FileRecord> records, FileCategory category) {
        return records.stream()
                .filter(record -> record.getCategory() == category)
                .mapToLong(FileRecord::getSizeBytes)
                .sum();
    }

    public List<FileRecord> largestFiles(List<FileRecord> records, int limit) {
        return records.stream()
                .sorted(Comparator.comparingLong(FileRecord::getSizeBytes).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public String readableSize(long bytes) {
        double size = bytes;
        String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = 0;
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        return String.format("%.2f %s", size, units[unitIndex]);
    }
}
