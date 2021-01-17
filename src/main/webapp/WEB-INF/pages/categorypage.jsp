<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <style>
        table {
            border-collapse: collapse;
            border: 1px solid black;
        }

        td {
            border: 1px solid black;
        }
    </style>
</head>
<body>
<div>Панель категорий</div>
<p>Список существующих категорий</p>
<c:url value="/" var="home"/>
<a href="${home}" >Перейти на домашнюю страницу</a>
<p></p>
<c:url value="/admin/add" var="admin"/>
<a href="${admin}">Перейтиив админ панель</a>
<p></p>
<table>
    <thead>
    <tr>
        <td>Название категории</td>
    </tr>

    </thead>
    <c:forEach items="${category}" var="category">
        <tr>
            <td>${category.categoryName}</td>
            <c:url value="/category/delete/${category.categoryId}" var="deleteCategory"/>
            <td><a href="${deleteCategory}">Удалить категорию</a></td>
        </tr>
    </c:forEach>
</table>
<c:url value="/category/addCategory" var="formCategory"/>
<form action="${formCategory}" method="post">
    <label for="categoryName">Введите название новой категории<br/>
        <input id="categoryName" name="categoryName" size="50" required/>
    </label>
    <input type="submit">
</form>
<c:if test="${CategoryPresent == 1}">
    <p style="color: red">Такая категория уже существует !!!</p>
</c:if>
</body>
</html>
