<%--
  Created by IntelliJ IDEA.
  User: ruth_
  Date: 28/12/2015
  Time: 17:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    </h3>
    <h4>Seleccione el álbum deseado:</h4>
    <form method='POST' action='?etapa_32&consultainicial_Canciones&anio_<%= session.getAttribute("anhio")%>album_<%= session.getAttribute("album2")%>' >
        <input type='hidden' name='etapa' value='032'>

        <c:choose>
            <c:when test="${empty resultBean.data}">
                Su consulta no ha generado ningún resultado
                <h4>:(</h4>
            </c:when>
            <c:otherwise>
                <c:forEach items="${resultBean.data}" var="datos">
                    <input type='radio' value='<c:out value="${datos}"></c:out>' name='estilo'><c:out value="${datos}"></c:out>
                    <br>
                </c:forEach>
                <input type='radio' value='Todos' checked='' name='estilo'>Todos<br>
            </c:otherwise>
        </c:choose>
        <p><input type='submit' value='Enviar'><br>
            <input type='submit' onclick='form.etapa.value=122' value='Atrás'>
            <input type='submit' onclick='form.etapa.value=0' value='Inicio'>
        </p>
    </form>
</div>
<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 3</footer>
</body>
</html>


