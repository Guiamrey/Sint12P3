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
    <h3>Cantante: <%= session.getAttribute("interprete")%>
    </h3>
    <h4>Seleccione el álbum deseado:</h4>
    <form method='POST' action='?etapa_21&consultainicial_Cantantes&Cantante_<%= session.getAttribute("interprete")%>'>
        <input type='hidden' name='etapa' value='021'>
        <c:choose>
        <c:when test="${empty resultBean.data}">
            Su consulta no ha generado ningún resultado <br>(El artista seleccionado no tiene ningún álbum)
            <h4>:(</h4>
        </c:when>
        <c:otherwise>
        <c:forEach items="${resultBean.data}" var="datos">
            <input type='radio' value='<c:out value="${datos}"></c:out>' name='album1'><c:out value="${datos}"></c:out>
            <br>
        </c:forEach>
        <input type='radio' value='Todos' checked='' name='album1'>Todos<br>
        <p><input type='submit' value='Enviar'>
            </c:otherwise>
            </c:choose>

            <br><input type='submit' onclick='form.etapa.value=111' value='Atrás'>
            <input type='submit' onclick='form.etapa.value=0' value='Inicio'>
        </p>
    </form>
</div>
<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 3</footer>
</body>
</html>
