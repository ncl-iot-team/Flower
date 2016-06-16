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
                z-index:10
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
                background:#adadad;
                margin-right: 1px;
                margin-bottom: 1px;
                border-radius:2px;
            }

            #primary_nav_wrap ul li:hover
            {
                background:#337AB7
            }

            #primary_nav_wrap ul ul
            {
                display:none;
                border-radius:2px;
                margin-top: 2px;
                position:absolute;
                top:100%;
                left:0;
                background:#adadad;
                padding:0
            }

            #primary_nav_wrap ul ul li
            {
                float:none;
                width:126px;

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
                var user = "ali";
                $('.leftMenu').on('click', function() {
                    var action = $(this).text().replace(' ', '');
                    $('table').remove();

                    $('#primary_nav_wrap').append('<ul>\n\
                                <li class="current-menu-item"><a href="#">Launch Flow</a></li>\n\
                                <li class="current-menu-item"><a href="#">Launch Service</a>\n\
                                    <ul><li><a href="#">Elasticity</a></li>\n\
                                        <li><a href="#">Monitoring</a></li>\n\
                                        <li><a href="#">Recommender</a></li>\n\
                                    </ul></li></ul>');

                    $('#stepDiv').append('<table id="' + action + 'Tbl"> \n\
                                     <thead><tr><th></th><th>Flow Name</th><th>Status</th> \n\
                                     <th>Flow Platforms</th><th>Launch Time</th>\n\
                                    </tr></thead><tbody> </tbody></table>');
                    $.get('getFlowList/' + user, function(flows) {
                        $.each(flows, function(i, flow) {
                            $('#' + action + 'Tbl tr:last').after('<tr><td><input type="radio" name="' + action + '" value="' + flow.flowId + '"></td>\n\
                                                            <td>' + flow.flowName + '</td>\n\
                                                            <td>Active</td>\n\
                                                            <td>' + getNicePlatformList(flow.platforms) + '</td>\n\
                                                            <td>' + getFormatedDate(flow.creationDate) + '</td></tr>');
                        });
                    });
                });

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
            <div style="float:left" >
                <ul id="sortable">
                    <li class="ui-state-default-extention" style="margin-bottom: 5px">
                        <div class="first-div">
                            <img class="icon" src="/Flower/resources/img/conf.png"/>
                        </div>
                        <div class="second-div"><a class="leftMenu" href="#">Flow Dashboard</a></div>
                    </li>
                    <li class="ui-state-default-extention">
                        <div class="first-div">
                            <img class="icon" src="/Flower/resources/img/Graph (2).png"/>
                        </div>
                        <div class="second-div"><a class="leftMenu" href="#">Elasticity Manager</a></div>
                    </li>
                    <li class="ui-state-default-extention">
                        <div class="first-div">
                            <img class="icon" src="/Flower/resources/img/Down1.png"/>
                        </div>
                        <div class="second-div"><a class="leftMenu" href="#">Monitoring</a></div>
                    </li>

                    <li class="ui-state-default-extention">
                        <div class="first-div">
                            <img class="icon" src="/Flower/resources/img/Cloud.png"/>
                        </div>
                        <div class="second-div"><a class="leftMenu" href="#">Cloud Recommender</a></div>
                    </li>

                </ul>
            </div>
            <div class="container">

                <div class="row" >

                    <div id="wizard" class="swMain">

                        <nav id="primary_nav_wrap">

                        </nav>
                        <div class="stepContainer" style="height: 500px;z-index: 1;">
                            <div id="stepDiv" class="content" style="display: block">

                            </div>

                        </div>
                    </div>            
                </div>
            </div>
        </div>

        <div class="jumbotron2">
            <div id="buttonContainer" class="container">
                <div class="row" style="text-align: center">
                    <input type="button" class="btn btn-action" value="Cancel">
                    <input class="btn btn-action" type="button" value="Create a New Flow">

                </div>
            </div>

        </div>

        <tiles:insertDefinition name="defaultfooter" />

    </body>


</html>
