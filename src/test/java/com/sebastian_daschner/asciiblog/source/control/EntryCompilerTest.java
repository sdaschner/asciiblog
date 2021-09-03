package com.sebastian_daschner.asciiblog.source.control;

import com.sebastian_daschner.asciiblog.entries.entity.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class EntryCompilerTest {

    private EntryCompiler cut;

    @BeforeEach
    void setUp() {
        cut = new EntryCompiler();
    }

    @Test
    void testCompile() {
        final Entry expectedEntry = new Entry("test", "Test entry", LocalDateTime.of(2015, 1, 1, 0, 0),
                "Lorem ipsum <code>dolor</code> sit amet.<br>\n" +
                "Lorem ipsum",
                "<div id=\"abstract\" class=\"paragraph\">\n" +
                "<p>Lorem ipsum <code>dolor</code> sit amet.<br>\n" +
                "Lorem ipsum</p>\n" +
                "</div>\n" +
                "<div class=\"paragraph\">\n" +
                "<p>Lorem ipsum dolor <strong>sit</strong> amet <em>adipiscing</em> elit.</p>\n" +
                "</div>\n" +
                "<div class=\"listingblock\">\n" +
                "<div class=\"content\">\n" +
                "<pre>\"Hello\" &amp;world{} &lt;test&gt;</pre>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"ulist\">\n" +
                "<ul>\n" +
                "<li>\n" +
                "<p>First</p>\n" +
                "</li>\n" +
                "<li>\n" +
                "<p>Second</p>\n" +
                "</li>\n" +
                "<li>\n" +
                "<p>Third</p>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "<div class=\"paragraph\">\n" +
                "<p><a href=\"https://github.com/sdaschner/jaxrs-analyzer\">JAX-RS Analyzer</a></p>\n" +
                "</div>",
                Set.of(), false);

        final String content = "= Test entry\n" +
                               "Sebastian Daschner\n" +
                               ":docdatetime: 2015-01-01T00:00\n" +
                               "\n" +
                               "[[abstract]]\n" +
                               "Lorem ipsum `dolor` sit amet. +\n" +
                               "Lorem ipsum\n" +
                               "\n" +
                               "Lorem ipsum dolor *sit* amet _adipiscing_ elit.\n" +
                               "\n" +
                               "----\n" +
                               "\"Hello\" &world{} <test>\n" +
                               "----\n" +
                               "\n" +
                               "- First\n" +
                               "- Second\n" +
                               "- Third\n" +
                               "\n" +
                               "https://github.com/sdaschner/jaxrs-analyzer[JAX-RS Analyzer]";

        final Entry actualEntry = cut.compile("test", content);

        assertThat(expectedEntry).isEqualTo(actualEntry);
    }

    @Test
    void testDateTimeSeconds() {
        final Entry expectedEntry = new Entry("test", "Test entry", LocalDateTime.of(2015, 1, 1, 1, 20, 20),
                "Lorem ipsum",
                "<div id=\"abstract\" class=\"paragraph\">\n" +
                "<p>Lorem ipsum</p>\n" +
                "</div>\n" +
                "<div class=\"paragraph\">\n" +
                "<p>Lorem ipsum dolor sit amet.</p>\n" +
                "</div>",
                Set.of(), false);

        final String content = "= Test entry\n" +
                               "Sebastian Daschner\n" +
                               ":docdatetime: 2015-01-01T01:20:20\n" +
                               "\n" +
                               "[[abstract]]\n" +
                               "Lorem ipsum\n" +
                               "\n" +
                               "Lorem ipsum dolor sit amet.";

        final Entry actualEntry = cut.compile("test", content);

        assertThat(expectedEntry).isEqualTo(actualEntry);
    }

    @Test
    void testReadTags() {
        final Entry expectedEntry = new Entry("test", "Test entry", LocalDateTime.of(2015, 1, 1, 1, 20, 20),
                "Lorem ipsum",
                "<div id=\"abstract\" class=\"paragraph\">\n" +
                "<p>Lorem ipsum</p>\n" +
                "</div>\n" +
                "<div class=\"paragraph\">\n" +
                "<p>Lorem ipsum dolor sit amet.</p>\n" +
                "</div>",
                Set.of("productivity", "jakartaee"), false);

        final String content = "= Test entry\n" +
                               "Sebastian Daschner\n" +
                               ":docdatetime: 2015-01-01T01:20:20\n" +
                               ":tags: productivity,jakartaee\n" +
                               "\n" +
                               "[[abstract]]\n" +
                               "Lorem ipsum\n" +
                               "\n" +
                               "Lorem ipsum dolor sit amet.";

        final Entry actualEntry = cut.compile("test", content);

        assertThat(expectedEntry).isEqualTo(actualEntry);
    }

    @Test
    void testReadEmptyTags() {
        final Entry expectedEntry = new Entry("test", "Test entry", LocalDateTime.of(2015, 1, 1, 1, 20, 20),
                "Lorem ipsum",
                "<div id=\"abstract\" class=\"paragraph\">\n" +
                "<p>Lorem ipsum</p>\n" +
                "</div>\n" +
                "<div class=\"paragraph\">\n" +
                "<p>Lorem ipsum dolor sit amet.</p>\n" +
                "</div>",
                Set.of(), false);

        final String content = "= Test entry\n" +
                               "Sebastian Daschner\n" +
                               ":docdatetime: 2015-01-01T01:20:20\n" +
                               ":tags: \n" +
                               "\n" +
                               "[[abstract]]\n" +
                               "Lorem ipsum\n" +
                               "\n" +
                               "Lorem ipsum dolor sit amet.";

        final Entry actualEntry = cut.compile("test", content);

        assertThat(actualEntry.getTags()).isEmpty();
        assertThat(expectedEntry).isEqualTo(actualEntry);
    }

    @Test
    void testReadSuppressNewsTeaser() {
        final Entry expectedEntry = new Entry("test", "Test entry", LocalDateTime.of(2015, 1, 1, 1, 20, 20),
                "Lorem ipsum",
                "<div id=\"abstract\" class=\"paragraph\">\n" +
                "<p>Lorem ipsum</p>\n" +
                "</div>\n" +
                "<div class=\"paragraph\">\n" +
                "<p>Lorem ipsum dolor sit amet.</p>\n" +
                "</div>",
                Set.of(), true);

        final String content = "= Test entry\n" +
                               "Sebastian Daschner\n" +
                               ":docdatetime: 2015-01-01T01:20:20\n" +
                               ":suppressnewsteaser: true\n" +
                               "\n" +
                               "[[abstract]]\n" +
                               "Lorem ipsum\n" +
                               "\n" +
                               "Lorem ipsum dolor sit amet.";

        final Entry actualEntry = cut.compile("test", content);

        assertThat(expectedEntry).isEqualTo(actualEntry);
    }

    @Test
    void testReadDontSuppressNewsTeaser() {
        final Entry expectedEntry = new Entry("test", "Test entry", LocalDateTime.of(2015, 1, 1, 1, 20, 20),
                "Lorem ipsum",
                "<div id=\"abstract\" class=\"paragraph\">\n" +
                "<p>Lorem ipsum</p>\n" +
                "</div>\n" +
                "<div class=\"paragraph\">\n" +
                "<p>Lorem ipsum dolor sit amet.</p>\n" +
                "</div>",
                Set.of(), false);

        final String content = "= Test entry\n" +
                               "Sebastian Daschner\n" +
                               ":docdatetime: 2015-01-01T01:20:20\n" +
                               ":suppressnewsteaser: false\n" +
                               "\n" +
                               "[[abstract]]\n" +
                               "Lorem ipsum\n" +
                               "\n" +
                               "Lorem ipsum dolor sit amet.";

        final Entry actualEntry = cut.compile("test", content);

        assertThat(expectedEntry).isEqualTo(actualEntry);
    }

}