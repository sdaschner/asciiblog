<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="blog" uri="/WEB-INF/blog.tld" %><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <title>Blog - Sebastian Daschner</title>
    <link rel="stylesheet" href="${homeUri}/css/style.css"/>
    <link rel="alternate" type="application/rss+xml" title="Blog - Sebastian Daschner" href="/feeds/rss"/>
</head>
<body>
    <div id="content">
        <header>
            <h1>
                <a href="/"><strong>sebastiandaschner</strong> blog</a>
            </h1>
            <hr>
            <ul>
                <li>
                    <a href="${homeUri}/about">About</a>
                </li>
                <li>
                    <a href="${homeUri}/offerings">Offerings</a>
                </li>
                <li>
                    <a href="${homeUri}/contact">Contact</a>
                </li>
                <li>
                    <a href="/">Blog</a>
                </li>
                <li>
                    <a href="${homeUri}/news">News</a>
                </li>
            </ul>
        </header>
        <main>
            <c:forEach var="entry" items="${entries}">
            <div class="teaser">
                    <h2><a href="/entries/${entry.link}">${entry.title}</a></h2>
                    <span><blog:formatDate value="${entry.date}" /></span>

                    <p>${entry.abstractContent} <br><a class="more" href="/entries/${entry.link}">read more</a></p>
            </div>
            </c:forEach>
            <p><a href="/entries/">All blog entries</a></p>
        </main>
    </div>
    <footer>
        <span>&copy; Sebastian Daschner, CC BY-NC-SA 4.0</span>
    </footer>
</body>
</html>
