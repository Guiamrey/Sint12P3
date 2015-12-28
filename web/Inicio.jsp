<%--
  Created by IntelliJ IDEA.
  User: ruth_
  Date: 28/12/2015
  Time: 12:15
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
    <div class='error'>
        <b>Errores encontrados</b>: <br>


        <br><b>Ficheros erróneos</b>: <br>


    </div>
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
<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 2</footer>
</body>
</html>
