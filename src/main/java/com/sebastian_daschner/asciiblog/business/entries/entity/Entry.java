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

package com.sebastian_daschner.asciiblog.business.entries.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Entry implements Serializable {

    private final String link;
    private final String title;
    private final LocalDateTime date;
    private final String abstractContent;
    private final String content;

    public Entry(final String link, final String title, final LocalDateTime date, final String abstractContent, final String content) {
        Objects.requireNonNull(link);
        Objects.requireNonNull(title);
        Objects.requireNonNull(date);
        Objects.requireNonNull(abstractContent);
        Objects.requireNonNull(content);

        this.link = link;
        this.title = title;
        this.date = date;
        this.abstractContent = abstractContent;
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getAbstractContent() {
        return abstractContent;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Entry entry = (Entry) o;

        if (!link.equals(entry.link)) return false;
        if (!title.equals(entry.title)) return false;
        if (!date.equals(entry.date)) return false;
        if (!abstractContent.equals(entry.abstractContent)) return false;

        return content.equals(entry.content);
    }

    @Override
    public int hashCode() {
        int result = link.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + abstractContent.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", abstractContent='" + abstractContent + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
