<%-- 
    Document   : index
    Created on : Apr 25, 2018, 4:16:58 PM
    Author     : grupa_2
--%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dodavanje parkiralista</title>
    </head>
    <body>
        <h1>Dodavanje parkiralista</h1>

        <% String geolokacija = (String) request.getAttribute("geolokacija");%>
        <% String naziv = (String) request.getAttribute("naziv");%>
        <% String adresa = (String) request.getAttribute("adresa");%>
        <form method="POST" 
              action="${pageContext.servletContext.contextPath}/DodajParkiraliste">
            <table>
                <tr>
                    <td>Naziv i adresa: 
                        <input name="naziv" placeholder="Upisi naziv"
                               value="<%
                                   if (naziv != null) {
                                       out.println(naziv);
                                   } else {
                                       out.println("");
                                   }
                               %>"/> 
                    </td>
                    <td><input name="adresa" placeholder="Upisi adresu"
                               value="<%
                                   if (adresa != null) {
                                       out.println(adresa);
                                   } else {
                                       out.println("");
                                   }
                               %>"/> 
                    </td>
                    <td><input type="submit" name="geolokacija" value="Geo lokacija"/></td>
                </tr>
                <tr>
                    <td colspan="2">Geo lokacija
                        <input name="lokacija" 
                               readonly="readonly" 
                               size="60"
                               value="<%
                                   if (geolokacija != null) {
                                       out.println(geolokacija);
                                   } else {
                                       out.println("");
                                   }
                               %>"/> 
                    </td>
                    <td><input type="submit" name="spremi" value="Spremi"/></td>
                </tr>
                <tr>
                    <td colspan="2"></td>
                    <td><input type="submit" name="meteo" value="Meteo podaci"/></td>
                </tr>
            </table>
            <div>
                <% String meteo = (String) request.getAttribute("meteo");%>
                <%
                    if (meteo != null) {
                        out.println(meteo);
                    } else {
                        out.println("");
                    }
                %>
            </div>


            <% String obavijest = (String) request.getAttribute("obavijest");%>
            <%
                if (obavijest != null) {
                    out.println(obavijest);
                } else {
                    out.println("");
                }
            %>
        </form>
    </body>
</html>

