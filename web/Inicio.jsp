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
    <div class='error'>
        <b>Errores encontrados</b>: <br>
        <c:forEach items="${errorBean.errores}" var="listaErrores">
            <c:out value="${listaErrores}"></c:out><br>
        </c:forEach>
        <c:if test="${empty errorBean.fichError}">
            No se han encontrado errores<br>
        </c:if>
        <br><b>Ficheros erróneos</b>: <br>
        <c:forEach items="${errorBean.fichError}" var="listaFichErrores">
            <c:out value="${listaFichErrores}"></c:out><br>
        </c:forEach>
        <c:if test="${empty errorBean.fichError}">
            No se han encontrado errores<br>
        </c:if>

    </div>
    <span class="caducado"><h4><%=session.getAttribute("Caducado")%></h4></span>
    <h3>Selecciona la consulta que desea hacer</h3>
    <form method='POST' action='?etapa_10'>
        <input type='hidden' name='etapa' value='10'>
        <input type='radio' value='1' checked='' name='consulta'>Lista de canciones<br>
        <input type='radio' value='2' name='consulta'>Número de canciones<br>
        <p>
            <input type='submit' value='Enviar'>
        </p>
    </form>
</div>
<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 3</footer>
</body>
</html>
