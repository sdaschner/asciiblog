package com.sebastian_daschner.asciiblog.templating;

import io.quarkus.qute.TemplateExtension;

import java.util.List;
import java.util.Map;

@TemplateExtension(namespace = "blog")
public class BlogTemplateExtension {

    private static final List<String> headReplacements = List.of("<code>", "</code>");
    private static final Map<String, String> rssReplacements = Map.of("&mdash;", "&#8211;");

    static String formatHead(String title) {
        return headReplacements.stream().reduce(title, (t, r) -> t.replace(r, ""));
    }

    static String formatRss(String title) {
        return rssReplacements.entrySet().stream().reduce(title, (t, e) -> t.replace(e.getKey(), e.getValue()), String::concat);
    }

}
