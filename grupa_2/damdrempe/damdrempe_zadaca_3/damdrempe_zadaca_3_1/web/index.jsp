<%-- 
    Document   : index
    Created on : Apr 25, 2018, 4:16:58 PM
    Author     : grupa_2
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dodavanje parkirališta</title>
    </head>
    <body>
        <h1>Dodavanje parkirališta</h1>
    </body>

    <form action="${pageContext.servletContext.contextPath}/DodajParkiraliste" method="post">
<!--        TODO prepisati-->
        Naziv i adresa:<br>
        <input type="text" name="naziv"><br>
        <input type="text" name="adresa"><br>
        Geo lokacija:<br>
        <input type="text" name="geolokacija"><br><br>
        <button type="button">Geo lokacija</button> 
        <input type="submit" value="Spremi">
        <button type="button">Meteo podaci</button> 
            
        Temp: <label></label>
        Vlaga: <label></label>
        Tlak: <label></label>
    </form> 
</html>
