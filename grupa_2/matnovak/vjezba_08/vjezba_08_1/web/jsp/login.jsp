<%-- 
    Document   : login
    Created on : Apr 10, 2018, 5:22:47 PM
    Author     : grupa_2
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Prijava korisnika</title>
    </head>
    <body>
        <h1>Prijava korisnika</h1>
        <form action="${pageContext.servletContext.contextPath}/ProvjeraKorisnika" method="POST">
            <label for="korime">Korisničko ime</label>
            <input type="text" name="korime" id="korime" 
                   placeholder="Korisničko ime"
                   maxlength="15" size="15" /><br/>
            <label for="lozinka">Lozinka</label>
            <input type="password" name="lozinka" id="lozinka"
                   maxlength="15" size="10"/><br/>
            <input type="submit" value="Prijavi se" />
        </form>
    </body>
</html>
