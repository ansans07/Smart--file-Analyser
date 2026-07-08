package com.ansika.fileorganizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class DuplicateRemovalService {
    public int moveDuplicatesToRecoveryFolder(Map<String, List<FileRecord>> duplicateGroups, Path recoveryFolder) throws IOException {
        Files.createDirectories(recoveryFolder);
        int movedCount = 0;

        for (List<FileRecord> group : duplicateGroups.values()) {
            if (group.size() < 2) {
                continue;
            }

            // Keep the first file in each duplicate group.
            // Move all remaining duplicates to the recovery folder.
            for (int i = 1; i < group.size(); i++) {
                FileRecord duplicate = group.get(i);
                Path source = duplicate.getPath();
                Path target = createUniqueTargetPath(recoveryFolder, source.getFileName().toString());

                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                movedCount++;
            }
        }

        return movedCount;
    }

    private Path createUniqueTargetPath(Path folder, String fileName) {
        Path target = folder.resolve(fileName);
        if (!Files.exists(target)) {
            return target;
        }

        String baseName = fileName;
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        }

        int count = 1;
        while (Files.exists(target)) {
            target = folder.resolve(baseName + "_duplicate_" + count + extension);
            count++;
        }
        return target;
    }
}
