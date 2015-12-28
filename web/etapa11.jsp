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
    <h4>Seleccione el inteprete deseado:</h4>
    <form method='POST' action='?etapa_11&consultainicial_Cantantes'>
        <input type='hidden' name='etapa' value='011'>

        <c:forEach items="${resultBean.data}" var="datos">
            <input type='radio' value='<c:out value="${datos}"></c:out>' name='interprete'><c:out
                value="${datos}"></c:out>
            <br>
        </c:forEach>
        <input type='radio' value='Todos' checked='' name='interprete'>Todos<br>

        <p>
            <input type='submit' value='Enviar'><br>
            <input type='submit' onclick='form.etapa.value=0' value='Atrás'>
            <input type='submit' onclick='form.etapa.value=0' value='Inicio'>
        </p>
    </form>
</div>
<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 3</footer>
</body>
</html>
