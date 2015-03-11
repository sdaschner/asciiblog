package com.sebastian_daschner.asciiblog.business.source.boundary;

import com.sebastian_daschner.asciiblog.business.entries.control.EntriesCache;
import com.sebastian_daschner.asciiblog.business.source.control.EntryCompiler;
import com.sebastian_daschner.asciiblog.business.source.control.GitExtractor;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Singleton
@Startup
public class SourceInvoker {

    @Inject
    GitExtractor gitExtractor;

    @Inject
    EntryCompiler entryCompiler;

    @Inject
    EntriesCache cache;

    @PostConstruct
    @Schedule(second = "0", minute = "*", hour = "*")
    public void checkNewEntries() {
        System.out.println("invoking entry check");
        final Map<String, String> changedFiles = gitExtractor.getChangedFiles();
        System.out.println("changedFiles = " + changedFiles);
        changedFiles.entrySet().stream()
                .map(e -> entryCompiler.compile(e.getKey(), e.getValue())).filter(Objects::nonNull)
                .forEach(cache::store);
    }

}
