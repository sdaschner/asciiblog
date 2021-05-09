package com.sebastian_daschner.asciiblog.source.boundary;

import com.sebastian_daschner.asciiblog.entries.control.EntryCache;
import com.sebastian_daschner.asciiblog.entries.entity.Entry;
import com.sebastian_daschner.asciiblog.source.control.EntryCompiler;
import com.sebastian_daschner.asciiblog.source.control.GitExtractor;
import com.sebastian_daschner.asciiblog.source.entity.ChangeSet;
import io.quarkus.runtime.Startup;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@Startup
public class SourceInvoker {

    @Inject
    GitExtractor gitExtractor;

    @Inject
    EntryCompiler entryCompiler;

    @Inject
    EntryCache cache;

    @PostConstruct
    public void checkNewEntries() {
        final ChangeSet changes = gitExtractor.getChanges();

        compileChangedFiles(changes.getChangedFiles());

        removeFiles(changes.getRemovedFiles());
    }

    private void compileChangedFiles(Map<String, String> changedFiles) {
        List<Entry> entries = changedFiles.entrySet().stream()
                .map(e -> entryCompiler.compile(e.getKey(), e.getValue())).filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!entries.isEmpty())
            cache.store(entries);
        entries.forEach(e -> System.out.println("Added entry " + e.getLink()));
    }

    private void removeFiles(Set<String> removedFiles) {
        if (!removedFiles.isEmpty())
            cache.remove(removedFiles);
        removedFiles.forEach(e -> System.out.println("Removed entry " + e));
    }

}
