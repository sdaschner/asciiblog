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

import java.time.LocalDateTime;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class EntryCompilerSourceHighlightTest {

    private EntryCompiler cut;

    @Before
    public void setUp() {
        cut = new EntryCompiler();
        cut.logger = Logger.getLogger(EntryCompilerSourceHighlightTest.class.getName());
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
                        "</div>");

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

        assertEquals(expectedEntry, actualEntry);
    }

}