<%--
  Created by IntelliJ IDEA.
  User: ruth_
  Date: 28/12/2015
  Time: 17:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Consulta musical</title>
    <link rel='stylesheet' href='iml.css'>
    <link href='https://fonts.googleapis.com/css?family=Open+Sans+Condensed:300' rel='stylesheet' type='text/css'>
</head>
<body>
<header>
    <h1>Servicio web de consulta músical</h1>
</header>
<div>
    <h3>Lista de canciones por estilo</h3>
    <h4>Seleccione el año deseado:</h4>
    <form method='POST' action='?etapa_12&consultainicial_Canciones'>
        <input type='hidden' name='etapa' value='012'>
        <c:choose>
        <c:when test="${empty resultBean.data}">
            Su consulta no ha generado ningún resultado
            <h4>:(</h4>
        </c:when>
        <c:otherwise>
        <c:forEach items="${resultBean.data}" var="datos">
            <input type='radio' value='<c:out value="${datos}"></c:out>' name='anhio'><c:out value="${datos}"></c:out>
            <br>
        </c:forEach>
        <input type='radio' value='Todos' checked='' name='anhio'>Todos<br>
        <p><input type='submit' value='Enviar'>

            </c:otherwise>
            </c:choose>
            <br><input type='submit' onclick='form.etapa.value=0' value='Atrás'>
            <input type='submit' onclick='form.etapa.value=0' value='Inicio'>
        </p>
    </form>
</div>
<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 3</footer>
</body>
</html>

