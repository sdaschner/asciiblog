package com.sebastian_daschner.asciiblog.templating;

import io.quarkus.qute.TemplateExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@TemplateExtension(namespace = "date")
public class DateTemplateExtension {

    private static final DateTimeFormatter rfc1123Formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
    private static final DateTimeFormatter readableFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.ENGLISH);

    static String format(LocalDateTime dateTime) {
        return readableFormatter.format(dateTime).toLowerCase();
    }

    static String formatRfc1123(LocalDateTime dateTime) {
        return rfc1123Formatter.format(dateTime.atOffset(ZoneOffset.UTC));
    }

}
