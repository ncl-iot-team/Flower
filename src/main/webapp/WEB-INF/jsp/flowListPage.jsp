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
        <link href="${pageContext.request.contextPath}/resources/css/stepform.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/smart_wizard.css" rel="stylesheet" type="text/css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <style>
            #primary_nav_wrap
            {
                margin-top:15px;
            }

            #primary_nav_wrap ul
            {
                list-style:none;
                position:relative;
                float:left;
                margin:0;
                padding:0;
                z-index:300
            }

            #primary_nav_wrap ul a
            {
                display:block;
                color:#fbfbfb;
                text-decoration:none;
                font-weight:600;
                font-size:12px;
                line-height:32px;
                padding:0 15px;
                font-family:"HelveticaNeue","Helvetica Neue",Helvetica,Arial,sans-serif
            }

            #primary_nav_wrap ul li
            {
                position:relative;
                float:left;
                margin:0;
                padding:0
            }

            #primary_nav_wrap ul li.current-menu-item
            {
                background:#67B168;//#337AB7;
                margin-right: 2px;
                margin-bottom: 1px;
                border-radius:2px;
            }

            #primary_nav_wrap ul li:hover
            {
                background:#adadad
            }

            #primary_nav_wrap ul ul
            {
                display:none;
                border-radius:2px;
                margin-top: 2px;
                position:absolute;
                top:100%;
                left:0;
                background:#67B168;//#337AB7;
                padding:0
            }

            #primary_nav_wrap ul ul li
            {
                float:none;
                width:135px;

            }

            #primary_nav_wrap ul ul a
            {
                line-height:120%;
                padding:10px 15px
            }

            #primary_nav_wrap ul ul ul
            {
                top:0;
                left:100%
            }

            #primary_nav_wrap ul li:hover > ul
            {
                display:block
            }
        </style>
        <script  type="text/javascript">
            $(function() {
                $("#tabs").tabs();
                var user = "ali";
                loadFlowDashboard();
                $('#flowDashboard').on('click', function() {
                    loadFlowDashboard();
                });

//                $('#elelasticityServiceDashboard').on('click', function() {
//                    loadElasticityDashboard();
//                });

                function cleanTab() {
                    $('#elasticityService').children().remove();
                    $('#monitoringService').children().remove();
                    $('#recommenderService').children().remove();
                }

                $('#flowDashboardTbl').on('change', 'input:radio', function() {
                    cleanTab();
                    var flowId = $(this).closest('tr').find('td:eq(1)').text();

//                    $('#primary_nav_wrap').append('<ul>\n\
//                                <li class="current-menu-item"><a href="#">Service Actions</a>\n\
//                                    <ul><li><a href="#">Start</a></li>\n\
//                                        <li><a href="#">Stop</a></li>\n\
//                                        <li><a href="#">Terminate</a></li>\n\
//                                        <li><a href="#">Monitor</a></li>\n\
//                                    </ul></li></ul>');

                    $('#elasticityService').append('<table id="elasticityDashboardTbl" style="margin-top:10px"> \n\
                                     <thead><tr><th></th><th>Flow Id</th><th>Platform</th> \n\
                                     <th>Resource Name</th><th>Measurement Target</th><th>Service Status</th>\n\
                                    <th>Launch Time</th></tr></thead><tbody> </tbody></table>');
                    $.get("ctrls/getCtrls/" + flowId, function(ctrls) {
                        $.each(ctrls, function(i, ctrl) {
                            $('#elasticityDashboardTbl tr:last').after('<tr><td><input type="radio" name="elasticityDashboard" value="' + ctrl.flowIdFk + '"></td>\n\
                                                            <td>' + flowId + '</td>\n\
                                                            <td>' + ctrl.ctrlName + '</td>\n\
                                                            <td>' + ctrl.resourceName + '</td>\n\
                                                            <td>' + ctrl.measurementTarget + '</td>\n\
                                                            <td>' + getCtrlStatus(flowId, ctrl.ctrlName, ctrl.resourceName, ctrl.measurementTarget) + '</td>\n\
                                                            <td>' + getFormatedDate(ctrl.creationDate) + '</td></tr>');
                        });
                    });
                });

                $('a').on('click', function() {
                    var action = $(this).text();
                    var flowId = $('input[type=radio][name=flowDashboardRadio]:checked').val();
                    if (action === 'Elasticity') {
                        window.open('/Flower/ctrls/flowCtrlServicePage/' + flowId);
                    }
                    if (action === 'Elasticity Service') {
                        window.open('/Flower/redirectToStepForm/' + flowId);
                    }
                });

                function getCtrlStatus(flowId, ctrlName, resourceName, measurementTarget) {
                    return $.ajax({
                        contentType: 'application/json',
                        type: 'GET',
                        url: 'ctrls/getCtrlStatus',
                        async: false,
                        data: {flowId: flowId, ctrlName: ctrlName, resource: resourceName, measurementTarget: measurementTarget}
                    }).responseText;
                }

                function loadFlowDashboard() {
                    $('#flowDashboardTbl').remove();

                    $('#primary_nav_wrap').append('<ul>\n\
                                <li class="current-menu-item"><a href="/Flower/FlowCreationFom">Launch Flow</a></li>\n\
                                <li class="current-menu-item"><a href="#">Monitor Service</a>\n\
                                    <ul><li><a href="#">Elasticity</a></li>\n\
                                        <li><a href="#">Monitoring</a></li>\n\
                                        <li><a href="#">Recommender</a></li>\n\
                                    </ul></li>\n\
                                <li class="current-menu-item"><a href="#">Launch Service</a>\n\
                                    <ul><li><a href="#">Elasticity Service</a></li>\n\
                                        <li><a href="#">Monitoring</a></li>\n\
                                        <li><a href="#">Recommender</a></li>\n\
                                    </ul></li></ul>');

                    $('#stepDiv').append('<table id="flowDashboardTbl"> \n\
                                     <thead><tr><th></th><th>Flow Id</th><th>Flow Name</th> \n\
                                     <th>Flow Platforms</th><th>Launch Time</th>\n\
                                    </tr></thead><tbody> </tbody></table>');
                    $.get('getFlowList/' + user, function(flows) {
                        $.each(flows, function(i, flow) {
                            $('#flowDashboardTbl tr:last').after('<tr><td><input type="radio" name="flowDashboardRadio" value="' + flow.flowId + '"></td>\n\
                                                            <td>' + flow.flowId + '</td>\n\
                                                            <td>' + flow.flowName + '</td>\n\
                                                            <td>' + getNicePlatformList(flow.platforms) + '</td>\n\
                                                            <td>' + getFormatedDate(flow.creationDate) + '</td></tr>');
                        });
                    });
                }

                $(document).on('change', 'input:radio', function() {
                    var $this = $(this);
//                    if($this).se
                });
                function getFormatedDate(timeStamp) {
                    var newDate = new Date();
                    newDate.setTime(timeStamp);
                    return newDate.toUTCString();
                }

                function getNicePlatformList(platforms) {
                    return platforms.replace(',', ' -> ');
                }
            });
        </script>
    </head>

    <body>
        <tiles:insertDefinition name="defaultbar" />

        <div class="col-xs-12">
            <h3><strong style="color: #555"></strong>Flow Management Dashboard</h3>
            <hr>
            <p id="ssman">
            </p>
            <br>
        </div>

        <div  class="jumbotron_body">
            <div style="float:left;width:250px" >
                <ul id="sortable">
                    <li class="ui-state-default-extention" style="margin-bottom: 5px">
                        <div class="first-div">
                            <img class="icon" src="/Flower/resources/img/conf.png"/>
                        </div>
                        <div class="second-div"><a id="flowDashboard" href="#">Flow Dashboard</a></div>
                    </li>
                    <li class="ui-state-default-extention">
                        <div class="first-div">
                            <img class="icon" src="/Flower/resources/img/Graph (2).png"/>
                        </div>
                        <div class="second-div"><a id="elelasticityServiceDashboard" href="#">Elasticity Manager</a></div>
                    </li>
                    <li class="ui-state-default-extention">
                        <div class="first-div">
                            <img class="icon" src="/Flower/resources/img/Down1.png"/>
                        </div>
                        <div class="second-div"><a id="monitoringServiceDashboard" href="#">Monitoring</a></div>
                    </li>

                    <li class="ui-state-default-extention">
                        <div class="first-div">
                            <img class="icon" src="/Flower/resources/img/Cloud.png"/>
                        </div>
                        <div class="second-div"><a class="recommenderServiceDashboard" href="#">Cloud Recommender</a></div>
                    </li>

                </ul>
            </div>
            <div class="container">

                <div class="row" >

                    <div id="wizard" class="swMain" style=position:relative;height:70%;" >

                        <nav id="primary_nav_wrap">

                        </nav>
                        <div class="stepContainer" style="min-height: 630px;">
                            <div id="stepDiv" class="content" style="position: absolute;top: 0;min-height: 630px;width: 1000px">

                            </div>
                            <div id="tabs" style="z-index: 200;position: absolute;bottom:10;min-height: 349px;font-size: 12px;min-width: 950px;">
                                <ul>
                                    <li><a href="#elasticityService">Elasticity Service</a></li>
                                    <li><a href="#monitoringService">Monitoring Service</a></li>
                                    <li><a href="#recommenderService">Deployment Recommender Service</a></li>
                                </ul>
                                <div id="elasticityService">
                                </div>
                                <div id="monitoringService">
                                </div>
                                <div id="recommenderService">
                                </div>
                            </div>



                        </div>
                    </div>            
                </div>
            </div>
        </div>

        <div class="jumbotron2">
            <!--            <div id="buttonContainer" class="container">
                            <div class="row" style="text-align: center">
                                <input type="button" class="btn btn-action" value="Cancel">
                                <input class="btn btn-action" type="button" value="Create a New Flow">
            
                            </div>
                        </div>-->

        </div>

        <tiles:insertDefinition name="defaultfooter" />

    </body>


</html>
