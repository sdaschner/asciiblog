package com.sebastian_daschner.asciiblog.entries.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class Entry implements Serializable {

    private final String link;
    private final String title;
    private final LocalDateTime date;
    private final String abstractContent;
    private final String content;
    private final Set<String> tags;

    public Entry(String link, String title, LocalDateTime date, String abstractContent, String content, Set<String> tags) {
        Objects.requireNonNull(link);
        Objects.requireNonNull(title);
        Objects.requireNonNull(date);
        Objects.requireNonNull(abstractContent);
        Objects.requireNonNull(content);
        Objects.requireNonNull(tags);

        this.link = link;
        this.title = title;
        this.date = date;
        this.abstractContent = abstractContent;
        this.content = content;
        this.tags = tags;
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

    public Set<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return link.equals(entry.link)
                && title.equals(entry.title)
                && date.equals(entry.date)
                && abstractContent.equals(entry.abstractContent)
                && content.equals(entry.content)
                && tags.equals(entry.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, title, date, abstractContent, content, tags);
    }

    @Override
    public String toString() {
        return "Entry{" +
                "link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", abstractContent='" + abstractContent + '\'' +
                ", content='" + content + '\'' +
                ", tags=" + tags +
                '}';
    }
}
