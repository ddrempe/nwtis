<%-- 
    Document   : ispisAktvnihKorisnika
    Created on : Apr 11, 2018, 5:11:02 PM
    Author     : grupa_2
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ispis aktivnih korisnika</title>
    </head>
    <body>
        <h1>Ispis aktivnih korisnika</h1>
        <c:forEach items="${applicationScope.PRIJAVLJENI_KORISNICI}" var="k">
           ID: ${k.id} 
           Korisničko ime:${k.korime} 
           Ime: ${k.ime} 
           Prezime ${k.prezime}  
           <br/>
        </c:forEach>
    </body>
</html>
