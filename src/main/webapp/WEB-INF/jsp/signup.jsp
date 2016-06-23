<%-- 
    Document   : signup
    Created on : 23/06/2016, 3:51:33 PM
    Author     : kho01f
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <link href="${pageContext.request.contextPath}/resources/css/stepform.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/smart_wizard.css" rel="stylesheet" type="text/css">

        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    </head>
    <script>

    </script>
    <body>
        <tiles:insertDefinition name="defaultbar" />

        <div class="col-xs-12">
            <h3><strong style="color: #555">Create a new account</strong></h3>
            <hr>
            <p>Use the form below to create login credentials that can be used for using Flower services.</p>
            <br>
        </div>


        <div  class="jumbotron_body">
            <div class="container">
                <div class="row">   
                    <div id="wizard" class="swMain">
                        <div class="stepContainer" style="height: 300px;">
                            <div class="content" style="display: block;height: 320px">

                                <form id="signupForm" action="signupForm" method="post">
                                    <fieldset>
                                        <div class="form-style-2"> 
                                            <div class="form-style-2-heading">Login Credentials</div>

                                            <label>
                                                <span>Name <span class="required">*</span></span>
                                                <input type="text" class="input-field" name="name"/></label>
                                            <label><span>E-mail address <span class="required">*</span></span>
                                                <input type="text" class="input-field" name="email"/>
                                            </label><label><span>New password <span class="required">*</span></span>
                                                <input type="password" class="input-field" name="password" />
                                            </label><label></label>
                                        </div>
                                        <input class="btn btn-action" type="submit" value="Create account" style="font-size:12px;margin-left: 153px;">
                                    </fieldset>
                                </form></div>
                        </div></div>
                </div>
            </div>            
        </div>

        <div class="jumbotron2">
            <div class="container">
                <div class="row" style="text-align: center">

                </div>
            </div>

        </div>


        <tiles:insertDefinition name="defaultfooter" />
    </body>
</html>
