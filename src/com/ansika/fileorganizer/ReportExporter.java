package com.ansika.fileorganizer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReportExporter {
    public void exportCsv(List<FileRecord> records, Path outputFile) throws IOException {
        if (outputFile.getParent() != null) {
            Files.createDirectories(outputFile.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            writer.write("Name,Path,Extension,Category,SizeBytes,ReadableSize,LastModified");
            writer.newLine();
            for (FileRecord record : records) {
                writer.write(csv(record.getName()) + "," +
                        csv(record.getPath().toString()) + "," +
                        csv(record.getExtension()) + "," +
                        record.getCategory() + "," +
                        record.getSizeBytes() + "," +
                        csv(record.getReadableSize()) + "," +
                        record.getLastModified());
                writer.newLine();
            }
        }
    }

    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
