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
