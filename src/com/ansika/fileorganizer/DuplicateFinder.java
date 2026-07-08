package com.ansika.fileorganizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateFinder {
    public Map<String, List<FileRecord>> findDuplicates(List<FileRecord> records) {
        Map<Long, List<FileRecord>> sizeGroups = new HashMap<>();
        for (FileRecord record : records) {
            sizeGroups.computeIfAbsent(record.getSizeBytes(), k -> new ArrayList<>()).add(record);
        }

        Map<String, List<FileRecord>> hashGroups = new HashMap<>();
        for (List<FileRecord> sameSizeFiles : sizeGroups.values()) {
            if (sameSizeFiles.size() > 1) {
                for (FileRecord record : sameSizeFiles) {
                    String hash = HashUtil.sha256(record.getPath());
                    hashGroups.computeIfAbsent(hash, k -> new ArrayList<>()).add(record);
                }
            }
        }

        hashGroups.entrySet().removeIf(entry -> entry.getValue().size() < 2);
        for (List<FileRecord> group : hashGroups.values()) {
            group.sort(Comparator.comparing(record -> record.getPath().toString()));
        }
        return hashGroups;
    }
}
