package com.sebastian_daschner.asciiblog.business.source.control;

import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.ast.StructuredDocument;

import java.time.LocalDate;
import java.util.HashMap;

public class EntryCompiler {

    /**
     * The AsciiDoc ID of the abstract content blog.
     */
    private static final String ABSTRACT_CONTENT_ID = "abstract";
    private static final String ASCIIDOC_SUFFIX = ".adoc";

    private final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

    public Entry compile(final String fileName, final String fileContent) {
        if (!fileName.endsWith(ASCIIDOC_SUFFIX))
            return null;

        final String name = fileName.substring(0, fileName.length() - ASCIIDOC_SUFFIX.length());

        final DocumentHeader documentHeader = asciidoctor.readDocumentHeader(fileContent);
        final String headline = documentHeader.getDocumentTitle().getMain();
        final LocalDate date = LocalDate.parse(documentHeader.getRevisionInfo().getDate());

        final StructuredDocument structuredDocument = asciidoctor.readDocumentStructure(fileContent, new HashMap<>());
        final String abstractContent = structuredDocument.getPartById(ABSTRACT_CONTENT_ID).getContent();

        final String content = asciidoctor.convert(fileContent, OptionsBuilder.options().toFile(false));

        return new Entry(name, headline, date, abstractContent, content);
    }

}
