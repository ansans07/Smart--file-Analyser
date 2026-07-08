package com.ansika.fileorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class OrganizerService {
    public int organizeByCategory(List<FileRecord> records, Path outputFolder) throws IOException {
        Files.createDirectories(outputFolder);
        int copiedCount = 0;
        for (FileRecord record : records) {
            Path categoryFolder = outputFolder.resolve(record.getCategory().name());
            Files.createDirectories(categoryFolder);
            Path target = getNonConflictingPath(categoryFolder.resolve(record.getName()));
            Files.copy(record.getPath(), target, StandardCopyOption.COPY_ATTRIBUTES);
            copiedCount++;
        }
        return copiedCount;
    }

    private Path getNonConflictingPath(Path target) {
        if (!Files.exists(target)) return target;

        String filename = target.getFileName().toString();
        String baseName = filename;
        String extension = "";
        int dot = filename.lastIndexOf('.');
        if (dot != -1) {
            baseName = filename.substring(0, dot);
            extension = filename.substring(dot);
        }

        int counter = 1;
        Path parent = target.getParent();
        Path candidate;
        do {
            candidate = parent.resolve(baseName + "_copy" + counter + extension);
            counter++;
        } while (Files.exists(candidate));
        return candidate;
    }
}
