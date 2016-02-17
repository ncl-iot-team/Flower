<%-- 
    Document   : flowload
    Created on : 14/02/2016, 4:30:36 PM
    Author     : kho01f
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<html><head>
        <meta charset="utf-8">
        <style>
            img.setting{
                position: absolute;
                right: 0px;
                bottom: 0px;
                top: -10px;
                /*left:5px;*/
                content:url("${pageContext.request.contextPath}/resources/img/setting1.png");
                width:25px; 
                height:23px;
            }
            div.first-div{
                padding-top:3px;
                float: left
            }
            div.second-div{
                padding-top:7px;
                padding-left: 35px
            }
            div.third-div{
                position: relative

            }
            #sortable1, #sortable2 { list-style-type: none; margin: 0; float: left; margin-right: 194px; background: #fbfbfb; padding: 10px 0px 10px 20px; width: 257px; border: 1px solid #cccccc}
            #sortable1 li, #sortable2 li { font-family: Helvetica, Arial, sans-serif; margin: 5px; padding: 5px 5px 15px 10px; font-size: 14px; width: 205px;color: #262626}
            #sortable1 > li > .image{
                display:block;
                float:left;
                margin:0px;
            }
            img.icon{
                width:30px; height:30px;
            }

            #sortable2 > li > .image{
                display:block;
                float:left;
                margin:0px;
            }

            .ui-state-default,
            .ui-widget-content .ui-state-default,
            .ui-widget-header .ui-state-default {
                border: 1px solid #d3d3d3;
                background: #e6e6e6 50% 50% repeat-x;
                font-weight: normal;
                color: #555555;
            }

            .ui-state-default-start {
                border: 1px solid #449d44;
                background: #99cc00;
                font-weight: bold;
                color: white;
                text-align: center;
            }

            .ui-state-default-end {
                border: 1px solid #a94442;
                background: #ce8483;
                font-weight: bold;
                color: white;
                text-align: center;

            }</style>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script>
            $(function() {
                $("ul.droptrue").sortable({
                    connectWith: "ul"
                });

                $("ul.dropfalse").sortable({
                    connectWith: "ul",
                    dropOnEmpty: false
                });

                $("#sortable2").sortable({
                    items: "li:not(.disabled1)"
                });

                $("#sortable2").sortable({
                    cancel: ".disabled2"
                });

                $("#sortable1, #sortable2").disableSelection();
            });
        </script>
    </head><body>
        <tiles:insertDefinition name="defaultbar" />
        <div class="col-xs-12">
            <h3><strong style="color: #555">Load the Analytics Flow</strong></h3>
            <hr>
            <p>Select the template that describes the data analytics flow that you want to create. A flow describes a group of related platforms and resources that you manage as a single unit.
            </p>
            <br>
        </div>
        <div  class="jumbotron1">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12 col-md-9 col-lg-5">
                        <input type="file" id="fileinput" />
                    </div>
                    <div class=" text-center col-sm-6 col-sm-offset-3 col-md-3 col-xs-offset-4 col-xs-5 col-lg-offset-0 col-lg-2">
                        <a class="btn btn-action" href="#" title="">Load Template</a> 
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xs-12">
            <h3><strong style="color: #555">Or Drag and Drop!</strong></h3>
            <hr>
            <p>Drag the platforms that constitute your analytics flow from the left canvas and drop it to the right canvas.
                Next, configure each system as per its current deployment.
            </p>
            <br>
        </div>

        <div  class="jumbotron_body">
            <div class="container">
                <div class="row">
                    <p style="font-weight: bold;color: #555555">Systems and Resources List</p>
                    <ul id="sortable1" class="droptrue">
                        
                        <li class="ui-state-default"> 
                            <div class="first-div"> 
                                <img class="icon" src="${pageContext.request.contextPath}/resources/img/storm.png" />
                            </div> 
                            <div class="second-div">Apache Storm</div>
                            <div  class="third-div">
                                <img class="setting" />
                            </div>
                        </li>
                        <li class="ui-state-default"> 
                            <div class="image first-div"> 
                                <img class="icon" src="${pageContext.request.contextPath}/resources/img/kinesis.png" />
                            </div> 
                            <div class="second-div">Amazon Kinesis</div>
                            <div style="position: relative;float: right">
                                <img class="setting" />
                            </div>
                        </li>
                        <li class="ui-state-default"> 
                            <div class="first-div"> 
                                <img class="icon" src="${pageContext.request.contextPath}/resources/img/dynamo.png" />
                            </div> 
                            <div class="second-div">DynamoDB</div>
                            <div style="position: relative;float: right">
                                <img class="setting" />
                            </div>
                        </li>
                        <li class="ui-state-default"> 
                            <div class="image first-div"> 
                                <img class="icon" style="width: 25px" src="${pageContext.request.contextPath}/resources/img/kafka.png" />
                            </div> 
                            <div class="second-div">Apache Kafka</div>
                            <div style="position: relative;float: right">
                                <img class="setting" />
                            </div>
                        </li>
                        <li class="ui-state-default"> 
                            <div class="image first-div"> 
                                <img class="icon" style="height: 27px;width: 33px" src="${pageContext.request.contextPath}/resources/img/spark.png" />
                            </div> 
                            <div class="second-div">Apache Spark</div>
                            <div style="position: relative;float: right">
                                <img class="setting" />
                            </div>
                        </li>
                        <li class="ui-state-default"> 
                            <div class="image first-div"> 
                                <img class="icon" style="height: 25px" src="${pageContext.request.contextPath}/resources/img/Cassandra.png" />
                            </div> 
                            <div class="second-div">Apache Cassandra</div>
                            <div style="position: relative;float: right">
                                <img class="setting" />
                            </div>
                        </li>
                    </ul>
                    <!--<p style="font-weight: bold;color: #555555">Data Analytics Flow</p>-->
                    <ul id="sortable2" class="dropfalse">

                        <li class="ui-state-default-start disabled2 disabled1" style="color:white;padding: 3px 6.5px 5px 8px" >Start</li>
                        <li class="ui-state-default-end disabled2" style="color:white;padding: 3px 6.5px 5px 8px">End</li>
                    </ul>

                </div>
            </div>
        </div>

        <div class="jumbotron2">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12 col-md-9 col-lg-5">
                    </div>
                    <div class=" text-center col-sm-6 col-sm-offset-3 col-md-3 col-xs-offset-4 col-xs-5 col-lg-offset-0 col-lg-2">
                        <a class="btn btn-action" href="#" title="">Cancel</a> 

                        <a class="btn btn-action" href="#" title="">Next</a> 

                    </div>

                </div>
            </div>

        </div>

        <tiles:insertDefinition name="defaultfooter" />
    </body>
</html>
