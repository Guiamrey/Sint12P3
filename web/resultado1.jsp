<%--
  Created by IntelliJ IDEA.
  User: ruth_
  Date: 28/12/2015
  Time: 13:12
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
    <h2>Lista de canciones</h2>
    <h3>Cantante: <%= session.getAttribute("interprete")%><br>Álbum: <%= session.getAttribute("album1")%>
    </h3>
    <h4>Resultado de su consulta:</h4><br>

    <c:choose>
        <c:when test="${empty resultBean.data}">
            Su consulta no ha generado ningún resultado
            <h4>:(</h4>
        </c:when>
        <c:otherwise>
            <ul>
                <c:forEach items="${resultBean.data}" var="datos">
                    <li><c:out value="${datos}"></c:out></li>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>
    <br>
    <form method='POST' action='?etapa_31'>
        <input type='hidden' name='etapa' value='031'>
        <p>
            <input type='submit' onclick='form.etapa.value=121' value='Atrás'>
            <input type='submit' onclick='form.etapa.value=0' value='Inicio'>
        </p>
    </form>
</div>
<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 3</footer>
</body>
</html>