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
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class EntryCompilerTest {

    private EntryCompiler cut;

    @Before
    public void setUp() {
        cut = new EntryCompiler();
    }

    @Test
    public void test() throws URISyntaxException {
        final Entry expectedEntry = new Entry("test", "Test entry", LocalDate.of(2015, 1, 1),
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
                        "</div>");

        final String content = "= Test entry\n" +
                "Sebastian Daschner\n" +
                "2015-01-01\n" +
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

        assertEquals(expectedEntry, actualEntry);
    }

}