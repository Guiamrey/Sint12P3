<%--
  Created by IntelliJ IDEA.
  User: ruth_
  Date: 28/12/2015
  Time: 17:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <h2>Número de canciones</h2>
    <h3>Año: <%= session.getAttribute("anhio")%>
        <br>Álbum: <%= session.getAttribute("album2")%>
        <br>Estilo: <%= session.getAttribute("estilo")%>
    </h3>
    <h4>Resultado de su consulta:</h4>
    <c:set value="${resultBean.data}" var="datos"></c:set>
    <br>El número de canciones es: <c:out value="${fn:length(datos)}"></c:out>
    <form method='POST' action='?etapa_42'>
        <input type='hidden' name='etapa' value='042'>
        <p><input type='submit' onclick='form.etapa.value=132' value='Atrás'>
            <input type='submit' onclick='form.etapa.value=0' value='Inicio'>
        </p>
    </form>
</div>
<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 3</footer>
</body>
</html>