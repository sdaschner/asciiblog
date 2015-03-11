package com.sebastian_daschner.asciiblog.business.entries.control;

import com.github.mustachejava.Mustache;
import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;
import com.sebastian_daschner.asciiblog.business.entries.entity.Template;

import javax.inject.Inject;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlGenerator {

    @Inject
    @Template(Template.Page.INDEX)
    Mustache indexMustache;

    @Inject
    @Template(Template.Page.ENTRIES)
    Mustache entriesMustache;

    @Inject
    @Template(Template.Page.ENTRY)
    Mustache entryMustache;

    public String generateIndex(final List<Entry> entries) {
        final Map<String, Object> scope = new HashMap<>();
        scope.put("entries", entries);
        final Writer writer = indexMustache.execute(new StringWriter(), scope);
        return writer.toString();
    }

    public String generateEntries(final List<Entry> entries) {
        final Map<String, Object> scope = new HashMap<>();
        scope.put("entries", entries);
        final Writer writer = entriesMustache.execute(new StringWriter(), scope);
        return writer.toString();
    }

    public String generateEntry(final Entry entry) {
        final Writer writer = entryMustache.execute(new StringWriter(), entry);
        return writer.toString();
    }

}
