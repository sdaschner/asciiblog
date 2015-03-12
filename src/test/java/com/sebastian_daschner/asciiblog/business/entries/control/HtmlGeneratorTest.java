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

package com.sebastian_daschner.asciiblog.business.entries.control;

import com.sebastian_daschner.asciiblog.business.entries.entity.Entry;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HtmlGeneratorTest {

    private HtmlGenerator cut;

    @Before
    public void setUp() throws Exception {
        cut = new HtmlGenerator();

        final TemplateExposer templateExposer = new TemplateExposer();
        templateExposer.initFactory();

        cut.indexMustache = templateExposer.indexTemplate();
        cut.entryMustache = templateExposer.entryTemplate();
        cut.entriesMustache = templateExposer.entriesTemplate();
    }

    @Test
    public void testGenerateIndex() throws Exception {
        final String expectedOutput = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <meta name=\"description\" content=\"\"/>\n" +
                "    <meta name=\"keywords\" content=\"\"/>\n" +
                "    <title>SD blog - Sebastian Daschner</title>\n" +
                "    <link rel=\"stylesheet\" href=\"/static/css/style.css\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<header>\n" +
                "    <h1><a href=\"http://blog.sebastian-daschner.com\">SD blog</a></h1>\n" +
                "</header>\n" +
                "<main>\n" +
                "        <div>\n" +
                "            <span class=\"note\">Published on 2015-01-03</span>\n" +
                "            <h2><a href=\"/entries/test\">Test</a></h2>\n" +
                "            <p>Hello World <a class=\"more\" href=\"/entries/test\">more</a></p>\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <span class=\"note\">Published on 2015-01-02</span>\n" +
                "            <h2><a href=\"/entries/another_test\">Test</a></h2>\n" +
                "            <p>Hi World <a class=\"more\" href=\"/entries/another_test\">more</a></p>\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <span class=\"note\">Published on 2015-01-01</span>\n" +
                "            <h2><a href=\"/entries/third-test\">Test</a></h2>\n" +
                "            <p>Goodbye World <a class=\"more\" href=\"/entries/third-test\">more</a></p>\n" +
                "        </div>\n" +
                "</main>\n" +
                "<footer>\n" +
                "    <hr/>\n" +
                "    <ul>\n" +
                "        <li><a href=\"http://www.sebastian-daschner.com\">Home</a></li>\n" +
                "        <li><a href=\"http://blog.sebastian-daschner.com\">Blog</a></li>\n" +
                "        <li><a href=\"https://twitter.com/DaschnerS\">@DaschnerS</a></li>\n" +
                "    </ul>\n" +
                "    <span>&copy; Sebastian Daschner, CC BY-NC-SA 4.0</span>\n" +
                "</footer>\n" +
                "</body>\n" +
                "</html>\n";

        final List<Entry> entries = getTestEntries();
        final String actualOutput = cut.generateIndex(entries);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGenerateEntries() throws Exception {
        final String expectedOutput = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <meta name=\"description\" content=\"\"/>\n" +
                "    <meta name=\"keywords\" content=\"\"/>\n" +
                "    <title>All blog entries - Sebastian Daschner</title>\n" +
                "    <link rel=\"stylesheet\" href=\"/static/css/style.css\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<header>\n" +
                "    <h1><a href=\"http://blog.sebastian-daschner.com\">SD blog</a></h1>\n" +
                "</header>\n" +
                "<main>\n" +
                "    <p>\n" +
                "        <a href=\"/entries/test\">Test</a><br>\n" +
                "        <a href=\"/entries/another_test\">Test</a><br>\n" +
                "        <a href=\"/entries/third-test\">Test</a><br>\n" +
                "    </p>\n" +
                "</main>\n" +
                "<footer>\n" +
                "    <hr/>\n" +
                "    <ul>\n" +
                "        <li><a href=\"http://www.sebastian-daschner.com\">Home</a></li>\n" +
                "        <li><a href=\"http://blog.sebastian-daschner.com\">Blog</a></li>\n" +
                "        <li><a href=\"https://twitter.com/DaschnerS\">@DaschnerS</a></li>\n" +
                "    </ul>\n" +
                "    <span>&copy; Sebastian Daschner, CC BY-NC-SA 4.0</span>\n" +
                "</footer>\n" +
                "</body>\n" +
                "</html>\n";

        final String actualOutput = cut.generateEntries(getTestEntries());

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGenerateEntry() throws Exception {
        final String expectedOutput = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <meta name=\"description\" content=\"\"/>\n" +
                "    <meta name=\"keywords\" content=\"\"/>\n" +
                "    <title>Test - Sebastian Daschner</title>\n" +
                "    <link rel=\"stylesheet\" href=\"/static/css/style.css\"/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<header>\n" +
                "    <h1><a href=\"http://blog.sebastian-daschner.com\">SD blog</a></h1>\n" +
                "</header>\n" +
                "<main>\n" +
                "    <h2 class=\"highlight\">Test</h2>\n" +
                "    <span class=\"note\">Published on 2015-01-03</span>\n" +
                "    <p>Hello World</p><p><strong>Hello</strong> World!</p>\n" +
                "</main>\n" +
                "<footer>\n" +
                "    <hr/>\n" +
                "    <ul>\n" +
                "        <li><a href=\"http://www.sebastian-daschner.com\">Home</a></li>\n" +
                "        <li><a href=\"http://blog.sebastian-daschner.com\">Blog</a></li>\n" +
                "        <li><a href=\"https://twitter.com/DaschnerS\">@DaschnerS</a></li>\n" +
                "    </ul>\n" +
                "    <span>&copy; Sebastian Daschner, CC BY-NC-SA 4.0</span>\n" +
                "</footer>\n" +
                "</body>\n" +
                "</html>\n";

        final String actualOutput = cut.generateEntry(getTestEntries().get(0));

        assertEquals(expectedOutput, actualOutput);
    }

    private static List<Entry> getTestEntries() {
        final Entry firstEntry = new Entry("test", "Test", LocalDate.of(2015, 1, 3), "Hello World", "<p>Hello World</p><p><strong>Hello</strong> World!</p>");
        final Entry secondEntry = new Entry("another_test", "Test", LocalDate.of(2015, 1, 2), "Hi World", "<p>Hello World</p><p><strong>Hi</strong> World!</p>");
        final Entry thirdEntry = new Entry("third-test", "Test", LocalDate.of(2015, 1, 1), "Goodbye World", "<p>Hello World</p><p><strong>Goodbye</strong> World!</p>");

        return Arrays.asList(firstEntry, secondEntry, thirdEntry);
    }

}