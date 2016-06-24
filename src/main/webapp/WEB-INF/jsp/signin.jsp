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
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.validate.min.js"></script>
    </head>
    <style>
        .success-msg-box {
            font-weight: bold;
            width: 367px;
            /*border: 1px solid;*/
            margin: 23px 0px;
            padding:10px 10px 10px 50px;
            background-repeat: no-repeat;
            background-position: 10px center;
            color: #4F8A10;
            background-color: #DFF2BF;
            background-image:url("${pageContext.request.contextPath}/resources/img/done.png");
            background-size: 25px 25px;

        }
    </style>
    <script>
        $(function() {
            jQuery.extend(jQuery.validator.messages, {
                required: ""
            });

            $("#signinForm").validate({
                errorPlacement: function(error, element) {
                    error.addClass('arrow');
                    error.insertAfter(element);
                },
                rules: {
                    "userEmail": "required",
                    "password": "required"
                }});
            var str = window.location.pathname;

            if (str.indexOf('success') > -1) {
                $("#msgBox").addClass('success-msg-box').text('Your account created successfully.');
            }

            $("#signinForm").on('submit', function(event) {
                if (!$("#signinForm").valid()) {
                    return false;
                }
                var $frm = $("#signinForm");
                $.ajax({
                    async: false,
                    data: $frm.serializeArray(),
                    type: $frm.attr('method'),
                    url: $frm.attr('action'),
                    success: function(data) {
                        if (data === true) {
                            window.location.replace("/Flower/flowList");
                        } else {
                            $(".failed-request").text("Either e-mail or password is incorrect!");
                        }
                    }
                });
                event.preventDefault();
            });
        });
    </script>
    <body>
        <tiles:insertDefinition name="defaultbar" />

        <div class="col-xs-12">
            <h3 id="h3"><strong style="color: #555">Sign In</strong></h3>
            <hr>
            <p>Use the form below to login to Flower.</p>
            <br>
        </div>


        <div  class="jumbotron_body">
            <div class="container">
                <div class="row" style="margin-left:200px;">   
                    <div id="wizard" class="swMain">
                        <div class="stepContainer" style="height: 300px;">
                            <div class="content" style="display: block;height: 320px">

                                <form id="signinForm" action="signinForm" method="post">
                                    <fieldset>
                                        <div class="form-style-2"> 
                                            <div class="form-style-2-heading">Login Credentials</div>
                                            <p class="failed-request"></p>
                                            <div id="msgBox"></div>

                                            <label><span>E-mail address <span class="required">*</span></span>
                                                <input type="text" class="input-field" name="userEmail"/>
                                            </label><label><span>Password <span class="required">*</span></span>
                                                <input type="password" class="input-field" name="password" />
                                            </label><label></label>
                                        </div>
                                        <input class="btn btn-action" type="submit" value="Sign in" style="font-size:12px;margin-left: 153px;">
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
