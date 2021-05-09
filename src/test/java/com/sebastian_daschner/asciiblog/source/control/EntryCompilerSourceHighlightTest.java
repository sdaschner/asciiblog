package com.sebastian_daschner.asciiblog.source.control;

import com.sebastian_daschner.asciiblog.entries.entity.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EntryCompilerSourceHighlightTest {

    private EntryCompiler cut;

    @BeforeEach
    public void setUp() {
        cut = new EntryCompiler();
    }

    @Test
    public void test() {
        final Entry expectedEntry = new Entry("test", "Test entry", LocalDateTime.of(2015, 1, 1, 0, 0),
                "Lorem ipsum",
                "<div id=\"abstract\" class=\"paragraph\">\n" +
                        "<p>Lorem ipsum</p>\n" +
                        "</div>\n" +
                        "<div class=\"listingblock\">\n" +
                        "<div class=\"content\">\n" +
                        "<pre class=\"CodeRay highlight\"><code data-lang=\"java\">" +
                        "<span class=\"directive\">public</span> <span class=\"type\">class</span> <span class=\"class\">Hello</span> {\n" +
                        "    <span class=\"directive\">private</span> <span class=\"predefined-type\">String</span> hello;}</code></pre>\n" +
                        "</div>\n" +
                        "</div>",
                Set.of());

        final String content = "= Test entry\n" +
                "Sebastian Daschner\n" +
                ":docdatetime: 2015-01-01T00:00\n" +
                "\n" +
                "[[abstract]]\n" +
                "Lorem ipsum\n" +
                "\n" +
                "[source, java]\n" +
                "----\n" +
                "public class Hello {\n" +
                "    private String hello;" +
                "}\n" +
                "----\n";

        final Entry actualEntry = cut.compile("test", content);

        assertThat(expectedEntry).isEqualTo(actualEntry);
    }

}