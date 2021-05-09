package com.sebastian_daschner.asciiblog.source.entity;

import java.util.stream.Stream;

public class ChangeSetBuilder {

    private final ChangeSet changeSet;

    private ChangeSetBuilder() {
        changeSet = new ChangeSet();
    }

    public ChangeSetBuilder andChangedFile(final String name, final String content) {
        changeSet.getChangedFiles().put(name, content);
        return this;
    }

    public ChangeSet build() {
        return changeSet;
    }

    public static ChangeSetBuilder withRemovedFiles(final String... names) {
        final ChangeSetBuilder builder = new ChangeSetBuilder();
        Stream.of(names).forEach(builder.changeSet.getRemovedFiles()::add);
        return builder;
    }

}
