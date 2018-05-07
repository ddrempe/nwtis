<%-- 
    Document   : index
    Created on : Apr 25, 2018, 4:16:58 PM
    Author     : grupa_2
--%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dodavanje parkiralista</title>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.css"/>
    </head>
    <body>
        <div class="container-fluid">
            <h1>Dodavanje parkiralista</h1>

            <% String geolokacija = (String) request.getAttribute("geolokacija");%>
            <% String naziv = (String) request.getAttribute("naziv");%>
            <% String adresa = (String) request.getAttribute("adresa");%>
            <form method="POST" 
                  action="${pageContext.servletContext.contextPath}/DodajParkiraliste">
                <table class="table">
                    <tr>
                        <td>
                            Naziv i adresa: 
                        </td>
                        <td>
                            <input name="naziv" 
                                   placeholder="Upisi naziv"
                                   class="form-control"
                                   required="true"
                                   value="<%
                                       if (naziv != null) {
                                           out.println(naziv);
                                       } else {
                                           out.println("");
                                       }
                                   %>"/> 
                        </td>
                        <td>
                            <input name="adresa" 
                                   placeholder="Upisi adresu"
                                   class="form-control"
                                   required="true"
                                   value="<%
                                       if (adresa != null) {
                                           out.println(adresa);
                                       } else {
                                           out.println("");
                                       }
                                   %>"/> 
                        </td>
                        <td>
                            <input type="submit" 
                                   name="geolokacija" 
                                   value="Geo lokacija"
                                   class="btn btn-block"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Geo lokacija:
                        </td>
                        <td>
                            <input name="lokacija" 
                                   readonly="readonly"
                                   class="form-control"
                                   disabled="true"
                                   value="<%
                                       if (geolokacija != null) {
                                           out.println(geolokacija);
                                       } else {
                                           out.println("");
                                       }
                                   %>"/> 
                        </td>
                        <td></td>
                        <td>
                            <input type="submit" 
                                   name="spremi" 
                                   value="Spremi"
                                   class="btn btn-block btn-primary"/>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td><input type="submit" 
                                   name="meteo" 
                                   value="Meteo podaci"
                                   class="btn btn-block"/></td>
                    </tr>
                </table>

            </form>
            <div class="alert alert-info">
                <% String obavijest = (String) request.getAttribute("obavijest");%>
                <%
                    if (obavijest != null) {
                        out.println(obavijest);
                    } else {
                        out.println("");
                    }
                %>
            </div>

            <div class="alert alert-success">
                <% String meteo = (String) request.getAttribute("meteo");%>
                <%
                    if (meteo != null) {
                        out.println(meteo);
                    } else {
                        out.println("");
                    }
                %>
            </div>
        </div>
    </body>
</html>

