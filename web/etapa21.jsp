<%--
  Created by IntelliJ IDEA.
  User: ruth_
  Date: 28/12/2015
  Time: 13:00
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
    <h2>Lista de canciones</h2>
    <h3>Cantante: " + interprete + "</h3>
    <h4>Seleccione el álbum deseado:</h4>
    <form method='POST' action='?etapa_21&consultainicial_Cantantes&Cantante_" + interprete + "' >
        <input type='hidden' name='etapa' value='021'>

        <input type='radio' value='Todos' name='album1'>Todos<br>
        <p><input type='submit' value='Enviar'><br>
            <input type='submit' onclick='form.etapa.value=111' value='Atrás'>
            <input type='submit' onclick='form.etapa.value=0' value='Inicio'>
    </form>
</div>
<footer>Creado por Ruth Guimarey Docampo<br>Servicios de Internet. Práctica 2</footer>
</body>
</html>
