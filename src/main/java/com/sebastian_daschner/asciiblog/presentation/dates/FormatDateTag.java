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

package com.sebastian_daschner.asciiblog.presentation.dates;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Formats a {@link LocalDate} to a String representation.
 *
 * @author Sebastian Daschner
 */
public class FormatDateTag extends SimpleTagSupport {

    private final DateTimeFormatter rfc1123Formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
    private final DateTimeFormatter readableFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.ENGLISH);

    private LocalDateTime value;
    private boolean rfc1123;

    @Override
    public void doTag() throws IOException {
        final String format = rfc1123 ?
                rfc1123Formatter.format(value) :
                readableFormatter.format(value).toLowerCase();

        getJspContext().getOut().print(format);
    }

    public void setValue(final LocalDateTime value) {
        this.value = value;
    }

    public void setRfc1123(final boolean rfc1123) {
        this.rfc1123 = rfc1123;
    }

}
