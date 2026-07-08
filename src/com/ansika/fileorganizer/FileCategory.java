package com.ansika.fileorganizer;

import java.util.Set;

public enum FileCategory {
    IMAGES(Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp", "svg")),
    DOCUMENTS(Set.of("pdf", "doc", "docx", "txt", "rtf", "odt")),
    SPREADSHEETS(Set.of("xls", "xlsx", "csv")),
    PRESENTATIONS(Set.of("ppt", "pptx", "key")),
    VIDEOS(Set.of("mp4", "mkv", "mov", "avi", "webm")),
    AUDIO(Set.of("mp3", "wav", "aac", "flac")),
    ARCHIVES(Set.of("zip", "rar", "7z", "tar", "gz")),
    CODE(Set.of("java", "py", "cpp", "c", "js", "html", "css", "sql", "json", "xml")),
    OTHERS(Set.of());

    private final Set<String> extensions;

    FileCategory(Set<String> extensions) {
        this.extensions = extensions;
    }

    public static FileCategory fromExtension(String extension) {
        String ext = extension.toLowerCase();
        for (FileCategory category : values()) {
            if (category.extensions.contains(ext)) {
                return category;
            }
        }
        return OTHERS;
    }
}
