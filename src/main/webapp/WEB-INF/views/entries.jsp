<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8"/>
  <meta name="description" content=""/>
  <meta name="keywords" content=""/>
  <title>All blog entries - Sebastian Daschner</title>
  <link rel="stylesheet" href="/static/css/style.css"/>
</head>
<body>
<header>
  <h1><a href="http://blog.sebastian-daschner.com">SD blog</a></h1>
</header>
<main>
  <p>
    <c:forEach var="entry" items="${entries}">
    <a href="/entries/${entry.link}">${entry.title}</a><br>
    </c:forEach>
  </p>
</main>
<footer>
  <hr/>
  <ul>
    <li><a href="http://www.sebastian-daschner.com">Home</a></li>
    <li><a href="http://blog.sebastian-daschner.com">Blog</a></li>
    <li><a href="https://twitter.com/DaschnerS">@DaschnerS</a></li>
  </ul>
  <span>&copy; Sebastian Daschner, CC BY-NC-SA 4.0</span>
</footer>
</body>
</html>
