<%-- 
    Document   : stepform
    Created on : 17/02/2016, 2:23:23 PM
    Author     : kho01f
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html><head>
        <!--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-2.0.0.min.js"></script>-->
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

        <link href="${pageContext.request.contextPath}/resources/css/smart_wizard.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.smartWizard.js"></script>
        <style>
            #sortable { list-style-type: none; margin: 0; float: left; margin-right: 194px; background: #fbfbfb; padding: 10px 0px 10px 20px; width: 257px}
            #sortable li { font-family: Helvetica, Arial, sans-serif; margin: 5px; padding: 5px 5px 15px 10px; font-size: 14px; width: 150px;color: #262626}
            .ui-state-default-start {
                /*border: 1px solid #449d44;*/
                background: #99cc00;
                font-weight: bold;
                text-align: center;
            }

            .ui-state-default-end {
                /*border: 1px solid #a94442;*/
                background: #ce8483;
                font-weight: bold;
                color: #fbfbfb;
                text-align: center;
            }
            img.icon{
                width:20px; height:20px;
            }

            #sortable > li > .image{
                display:block;
                float:left;
                margin:0px;
            }
            div.first-div{
                padding-top:0px;
                float: left
            }
            div.second-div{
                padding-top:0px;
                padding-left: 25px;
            }
            div.third-div{
                position: relative

            }

            .ui-state-default-extention {
                border: 1px solid #d3d3d3;
                background: #e6e6e6 50% 50% repeat-x;
                font-weight: normal;
                color: #555555;
                height: 32px;
                width: 205px;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {

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

                    var $wizardRef = $('#wizard ul');
                    $wizardRef.append('<li><a href=\"#step-' + i + '\"><span class=\"stepDesc\">\n\
                        ' + (i + 1) + '. ' + systems[i] + '</span></a></li>');



                    // the order is wrong!
                    $wizardRef.after('<div id=\"step-' + i + '\"></div>');
                }
                // Initialize Smart Wizard
                $('#wizard').smartWizard();
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
                    <li class="ui-state-default-start" style="color:#fbfbfb;padding: 3px 6.5px 5px 8px">Analytics Flow</li>

                    <!--<li class="ui-state-default-end" style="color:white;padding: 3px 6.5px 5px 8px">End</li>-->
                </ul>
            </div>
            <div class="container">

                <div class="row">

                    <div id="wizard" class="swMain">
                        <ul>

                        </ul>

                    </div>
                </div>            
            </div>
        </div>
        <div class="jumbotron2">
            <div class="container">
                <div class="row" style="text-align: center;">
                    <input type="button" class="btn btn-action" value="Cancel"/>
                    <input class="btn btn-action" type="submit" form="flow-general-setting" value="Next: Launch Service"/>
                </div>
            </div>

        </div>

        <tiles:insertDefinition name="defaultfooter" />

    </body>
</html>