/*
 * Copyright (C) 2015 Sebastian Daschner, sebastian-daschner.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sebastian_daschner.asciiblog.business.source.boundary;

import com.sebastian_daschner.asciiblog.business.entries.control.EntryCache;
import com.sebastian_daschner.asciiblog.business.source.control.EntryCompiler;
import com.sebastian_daschner.asciiblog.business.source.control.GitExtractor;
import com.sebastian_daschner.asciiblog.business.source.entity.ChangeSet;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Objects;
import java.util.logging.Logger;

@Singleton
@Startup
public class SourceInvoker {

    @Inject
    GitExtractor gitExtractor;

    @Inject
    EntryCompiler entryCompiler;

    @Inject
    EntryCache cache;

    @Inject
    Logger logger;

    @PostConstruct
    @Schedule(minute = "*", hour = "*")
    // TODO add REST resource to invoke this method
    public void checkNewEntries() {
        final ChangeSet changes = gitExtractor.getChanges();

        changes.getChangedFiles().entrySet().stream()
                .map(e -> entryCompiler.compile(e.getKey(), e.getValue())).filter(Objects::nonNull)
                .forEach(e -> {
                    cache.store(e);
                    logger.info("Added entry " + e.getLink());
                });

        changes.getRemovedFiles().forEach(e -> {
            cache.remove(e);
            logger.info("Removed entry " + e);
        });
    }

}
