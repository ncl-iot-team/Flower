<%-- 
    Document   : flowListPage
    Created on : 08/06/2016, 3:51:43 PM
    Author     : kho01f
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    </head>
    <body>
        <tiles:insertDefinition name="defaultbar" />

        <div class="col-xs-12">
            <h3><strong style="color: #555">Create Your Analytics Flow</strong></h3>
            <hr>
            <p>Drag the platforms that constitute your analytics flow from the left canvas and drop it to the right canvas.
            </p>
            <br>
        </div>


        <div  class="jumbotron_body">
            <div class="container">
                <div class="row">   
                    <h5><strong style="color: #555">Analytics Flow settings</strong></h5>
                    <hr style="width:85%;" align="left">


                </div>

            </div>            
        </div>

        <div class="jumbotron2">
            <div class="container">
                <div class="row" style="text-align: center">
                    <input type="button" class="btn btn-action" value="Cancel">
                    <input class="btn btn-action" type="button" value="Create a New Flow">

                </div>
            </div>

        </div>


        <tiles:insertDefinition name="defaultfooter" />
    </body>
</html>
