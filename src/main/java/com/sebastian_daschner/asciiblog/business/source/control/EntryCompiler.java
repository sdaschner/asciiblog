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

package com.sebastian_daschner.asciiblog.business.source.control;

import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.ast.StructuredDocument;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntryCompiler {

    private static final String ABSTRACT_CONTENT_ID = "abstract";
    private static final String DOC_DATE_TIME_ATTR = "docdatetime";

    private final Asciidoctor asciidoctor = Asciidoctor.Factory.create();
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Inject
    Logger logger;

    public Entry compile(final String name, final String fileContent) {
        try {
            final DocumentHeader documentHeader = asciidoctor.readDocumentHeader(fileContent);
            final String headline = documentHeader.getDocumentTitle().getMain();
            String docDateTime = documentHeader.getAttributes().get(DOC_DATE_TIME_ATTR).toString();
            final LocalDateTime date = formatter.parse(docDateTime, LocalDateTime::from);

            final StructuredDocument structuredDocument = asciidoctor.readDocumentStructure(fileContent, new HashMap<>());
            final String abstractContent = structuredDocument.getPartById(ABSTRACT_CONTENT_ID).getContent();

            final String content = asciidoctor.convert(fileContent, OptionsBuilder.options().toFile(false));

            return new Entry(name, headline, date, abstractContent, content);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not compile entry " + name, e);
            return null;
        }
    }

}
