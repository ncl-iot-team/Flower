<%-- 
    Document   : stepform
    Created on : 17/02/2016, 2:23:23 PM
    Author     : kho01f
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html><head>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

        <link href="${pageContext.request.contextPath}/resources/css/smart_wizard.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/stepform.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.smartWizard.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.validate.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/cloudSetting.form.js"></script>

        <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.4.1/jsgrid.min.css" />
        <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.4.1/jsgrid-theme.min.css" />

        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.4.1/jsgrid.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {

                jQuery.extend(jQuery.validator.messages, {
                    required: "Required."
                });

                var flow = '${platforms}';
                var systems = flow.split(",");
                systems.unshift("Cloud Setting");
                for (var i = 0; i < systems.length; i++) {
                    if (i > 0) {
                        $('#sortable').children().last().after('<li class=\"ui-state-default-extention\"> \n\
                        <div class="first-div"> <img class="icon" \n\
                        src="${pageContext.request.contextPath}/resources/img/' + systems[i] + '.png" /></div> \n\
                        <div class="second-div">' + systems[i] + '</div></li>');
                    }
                    // j is sued for ordering divs
                    var j = (systems.length - 1) - i;
                    var $wizardRef = $('#wizard ul');
                    $wizardRef.append('<li><a href=\"#step-' + i + '\"><span class=\"stepDesc\">\n\
                        ' + (i + 1) + '. ' + systems[i] + '</span></a></li>');
                    $wizardRef.after('<div id=\"step-' + j + '\"></div>');
                }

                // Initialize Smart Wizard
                $('#wizard').smartWizard();
                $('.stepContainer').wrap('<form id="stepForms" action="" method="post"></form>');

//                createCloudSettingForm('#step-0');
                createKinesisCtrlForm('#step-0');


                $("#stepForms").validate({
                    errorPlacement: function(error, element) {
                        error.addClass('arrow');
                        error.insertAfter(element);
                    },
                    rules: {
                        accessKey: "required",
                        secretKey: "required",
                        cloudProviderCat: "required",
                        cloudProviderSubcatat: "required"
                    }});
            });


        </script>
    </head>

    <body>
        <tiles:insertDefinition name="defaultbar" />

        <div class="col-xs-12">
            <h3><strong style="color: #555"></strong>Flow Configuration</h3>
            <hr>
            <p>Drag the platforms that constitute your analytics flow from the left canvas and drop it to the right canvas.
                Next, configure each system as per its current deployment.
            </p>
            <br>
        </div>


        <div  class="jumbotron_body">
            <div style="float:left" >
                <ul id="sortable">
                    <li class="ui-state-default-start" style="color:#fbfbfb; padding: 3px 6.5px 5px 8px">Analytics Flow</li>

                    <!--<li class="ui-state-default-end" style="color:white;padding: 3px 6.5px 5px 8px">End</li>-->
                </ul>
            </div>
            <div class="container">

                <div class="row">

                    <div id="wizard" class="swMain">
                        <ul>

                        </ul>
                        <!--                        <div id="test" class="form-style-2">
                                                    <form id="forms" action="" method="post">
                                                        
                                                    </form>
                                                </div>-->
                    </div>
                </div>            
            </div>
        </div>
        <div class="jumbotron2">
            <div class="container">
                <div class="row" style="text-align: center;">
                    <input type="button" class="btn btn-action" value="Cancel"/>
                    <input class="btn btn-action" type="submit" id="submit-button" form="flow-general-setting" value="Next: Launch Service"/>
                </div>
            </div>

        </div>

        <tiles:insertDefinition name="defaultfooter" />

    </body>
</html>