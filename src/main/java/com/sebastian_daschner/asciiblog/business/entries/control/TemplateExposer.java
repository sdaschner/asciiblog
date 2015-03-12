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

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.sebastian_daschner.asciiblog.business.entries.entity.Template;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class TemplateExposer {

    private DefaultMustacheFactory factory;

    @PostConstruct
    public void initFactory() {
        factory = new DefaultMustacheFactory("templates");
    }

    @Template(Template.Page.INDEX)
    @Produces
    public Mustache indexTemplate() {
        return factory.compile("index.mustache");
    }

    @Template(Template.Page.ENTRY)
    @Produces
    public Mustache entryTemplate() {
        return factory.compile("entry.mustache");
    }

    @Template(Template.Page.ENTRIES)
    @Produces
    public Mustache entriesTemplate() {
        return factory.compile("entries.mustache");
    }

}
