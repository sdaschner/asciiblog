package com.sebastian_daschner.asciiblog.source.control;

import com.sebastian_daschner.asciiblog.entries.entity.Entry;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.ast.StructuralNode;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.asciidoctor.AttributesBuilder.attributes;

@ApplicationScoped
public class EntryCompiler {

    private static final String ABSTRACT_CONTENT_ID = "abstract";
    private static final String DOC_DATE_TIME_ATTR = "docdatetime";
    private static final String DOC_TAGS_ATTR = "tags";

    private final Asciidoctor asciidoctor = Asciidoctor.Factory.create();
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Entry compile(final String name, final String fileContent) {
        try {
            final DocumentHeader documentHeader = asciidoctor.readDocumentHeader(fileContent);
            final String headline = documentHeader.getDocumentTitle().getMain();
            String docDateTime = documentHeader.getAttributes().get(DOC_DATE_TIME_ATTR).toString();
            Set<String> tags = parseTags(documentHeader.getAttributes());
            final LocalDateTime date = formatter.parse(docDateTime, LocalDateTime::from);

            Document document = asciidoctor.load(fileContent, new HashMap<>());
            String abstractContent = findAbstract(document);

            OptionsBuilder options = OptionsBuilder.options()
                    .attributes(attributes()
                            .sourceHighlighter("coderay")
                            .get())
                    .toFile(false);
            final String content = asciidoctor.convert(fileContent, options);

            return new Entry(name, headline, date, abstractContent, content, tags);
        } catch (Exception e) {
            System.err.println("Could not compile entry " + name);
            e.printStackTrace();
            return null;
        }
    }

    private Set<String> parseTags(Map<String, Object> attrs) {
        if (attrs.containsKey(DOC_TAGS_ATTR)) {
            String tags = attrs.get(DOC_TAGS_ATTR).toString();
            return Stream.of(tags.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    private String findAbstract(Document document) {
        return (String) document.getBlocks().stream()
                .filter(b -> ABSTRACT_CONTENT_ID.equals(b.getId()))
                .findAny().map(StructuralNode::getContent)
                .orElse("");
    }

}
