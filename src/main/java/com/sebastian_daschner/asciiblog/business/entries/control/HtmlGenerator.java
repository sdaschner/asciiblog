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
