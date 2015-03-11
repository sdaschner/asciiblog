package com.sebastian_daschner.asciiblog.business.entries.entity;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Template {

    /**
     * The used page template.
     */
    Page value();

    enum Page {
        INDEX, ENTRY, ENTRIES
    }

}
