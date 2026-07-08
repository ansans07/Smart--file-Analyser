package com.ansika.fileorganizer;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchService {
    public List<FileRecord> searchByName(List<FileRecord> records, String keyword) {
        String key = keyword.toLowerCase(Locale.ROOT);
        return records.stream()
                .filter(record -> record.getName().toLowerCase(Locale.ROOT).contains(key))
                .collect(Collectors.toList());
    }
}
