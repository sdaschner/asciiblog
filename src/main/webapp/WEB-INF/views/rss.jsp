<%@ page contentType="application/rss+xml;charset=UTF-8" language="java" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="blog" uri="/WEB-INF/blog.tld" %><?xml version="1.0" encoding="UTF-8" ?>
<?xml-stylesheet type="text/css" href="${homeUri}/css/rss.css" ?>
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
    <channel>
        <title>Blog - Sebastian Daschner</title>
        <description>Sebastian Daschner's Java / programming / computer science blog</description>
        <link>https://blog.sebastian-daschner.com</link>
        <language>en-us</language>
        <copyright>Copyright (C) ${latestDate.year} Sebastian Daschner, sebastian-daschner.com</copyright>
        <lastBuildDate><blog:formatDate rfc1123="true" value="${latestDate}"/></lastBuildDate>
        <pubDate><blog:formatDate rfc1123="true" value="${latestDate}"/></pubDate>
        <ttl>1440</ttl>
        <atom:link href="https://blog.sebastian-daschner.com/feeds/rss" rel="self" type="application/rss+xml" />
        <c:forEach var="entry" items="${entries}">
            <item>
                <title>${entry.title}</title>
                <description><c:out value="${entry.abstractContent}" /></description>
                <link>https://blog.sebastian-daschner.com/entries/${entry.link}</link>
                <guid isPermaLink="true">https://blog.sebastian-daschner.com/entries/${entry.link}</guid>
                <pubDate><blog:formatDate rfc1123="true" value="${entry.date}"/></pubDate>
            </item>
        </c:forEach>
    </channel>
</rss>
