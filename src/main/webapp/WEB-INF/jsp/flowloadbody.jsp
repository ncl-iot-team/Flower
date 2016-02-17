<%-- 
    Document   : flowloadbody
    Created on : 15/02/2016, 5:29:52 PM
    Author     : kho01f
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<head>
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
    </head>

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