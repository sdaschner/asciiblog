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

package com.sebastian_daschner.asciiblog.presentation.document;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Formats a document title for the html {@code <head>} representation.
 *
 * @author Sebastian Daschner
 */
public class FormatHeadTag extends SimpleTagSupport {

    private static final List<String> replacements = Arrays.asList("<code>", "</code>");

    private String title;

    @Override
    public void doTag() throws JspException, IOException {
        final String title = replacements.stream().reduce(this.title, (t, r) -> t.replace(r, ""));

        JspWriter out = getJspContext().getOut();
        out.print(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
