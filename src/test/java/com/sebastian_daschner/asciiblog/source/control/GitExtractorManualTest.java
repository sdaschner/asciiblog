package com.sebastian_daschner.asciiblog.source.control;

import com.sebastian_daschner.asciiblog.entries.entity.Entry;
import com.sebastian_daschner.asciiblog.source.entity.ChangeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Disabled
public class GitExtractorManualTest {

    private GitExtractor extractor;
    private EntryCompiler compiler;
    private Map<String, String> replacements;

    @BeforeEach
    public void setUp() {
        extractor = new GitExtractor();
        extractor.gitUri = "file:///tmp/blog";
        extractor.sshPath = "/tmp/id_rsa";
        compiler = new EntryCompiler();

        replacements = new HashMap<>();
        replacements.put("<code>", "");
        replacements.put("</code>", "");
        replacements.put("(Video)", "");
        replacements.put("&amp;", "&");
    }

    @Test
    public void test() {
        List<Entry> entries = extractEntries();

        System.out.println("\n\nArticles\n");
        System.out.println(entries.stream()
                .filter(e -> !e.getTitle().contains("(Video)"))
                .map(this::toLine)
                .collect(Collectors.joining("\n")));

        System.out.println("\n\nVideos\n");
        System.out.println(entries.stream()
                .filter(e -> e.getTitle().contains("(Video)"))
                .map(this::toLine)
                .collect(Collectors.joining("\n")));
    }

    private String toLine(Entry entry) {
        String title = entry.getTitle();
        for (Map.Entry<String, String> replacement : replacements.entrySet()) {
            title = title.replace(replacement.getKey(), replacement.getValue());
        }
        return "- " + entry.getDate().toLocalDate() + " " + title;
    }

    private List<Entry> extractEntries() {
        try {
            extractor.openGit();
            ChangeSet changes = extractor.getChanges();
            return compileChangedFiles(changes.getChangedFiles());
        } finally {
            extractor.closeGit();
        }
    }

    private List<Entry> compileChangedFiles(final Map<String, String> changedFiles) {
        return changedFiles.entrySet().stream()
                .map(e -> compiler.compile(e.getKey(), e.getValue())).filter(Objects::nonNull)
                .filter(e -> e.getDate().isAfter(LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.MIN)))
                .sorted(Comparator.comparing(Entry::getDate))
                .collect(Collectors.toList());
    }

}