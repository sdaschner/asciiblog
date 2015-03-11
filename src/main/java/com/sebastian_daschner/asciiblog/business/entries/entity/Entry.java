package com.sebastian_daschner.asciiblog.business.entries.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Entry {

    private final String link;
    private final String title;
    private final LocalDate date;
    private final String abstractContent;
    private final String content;

    public Entry(final String link, final String title, final LocalDate date, final String abstractContent, final String content) {
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

    public LocalDate getDate() {
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
