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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Formats a {@link LocalDate} to a String representation.
 *
 * @author Sebastian Daschner
 */
public class FormatDateTag extends SimpleTagSupport {

    private LocalDate value;
    private boolean rfc1123;

    @Override
    public void doTag() throws JspException, IOException {
        final String format;
        if (rfc1123)
            format = DateTimeFormatter.RFC_1123_DATE_TIME.format(value.atStartOfDay(ZoneOffset.UTC));
        else
            format = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.ENGLISH).format(value).toLowerCase();

        JspWriter out = getJspContext().getOut();
        out.print(format);
    }

    public void setValue(final LocalDate value) {
        this.value = value;
    }

    public void setRfc1123(final boolean rfc1123) {
        this.rfc1123 = rfc1123;
    }

}
