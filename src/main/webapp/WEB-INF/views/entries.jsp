<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <title>All blog entries - Sebastian Daschner</title>
    <link rel="stylesheet" href="${homeUri}/css/style.css"/>
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
                    <a href="${homeUri}">Home</a>
                </li>
                <li>
                    <a href="/">Blog</a>
                </li>
                <li>
                    <a href="${homeUri}/contact">Contact</a>
                </li>
            </ul>
        </header>
        <main>
            <h2 class="bottom1_5em">All blog entries</h2>
            <p>
            <c:forEach var="entry" items="${entries}">
                <a class="no_highlight" href="/entries/${entry.link}">${entry.title}</a><br>
            </c:forEach>
            </p>
        </main>
    </div>
    <footer>
        <span>&copy; Sebastian Daschner, CC BY-NC-SA 4.0</span>
    </footer>
</body>
</html>
