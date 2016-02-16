<%-- 
    Document   : flowloadbody
    Created on : 15/02/2016, 5:29:52 PM
    Author     : kho01f
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

    <head>
        <meta charset="utf-8">

        <spring:url value="/resources/css/dragdrop.css" var="DdCss" />
        <link href="${DdCss}" rel="stylesheet" />
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


        <ul id="sortable1" class="droptrue">
            <li class="ui-state-default"> 
                <div class="first-div"> 
                    <img class="icon" src="${pageContext.request.contextPath}/resources/img/storm.png" />
                </div> 
                <div class="second-div">Apache Storm</div>
                <div style="position: relative;float: right">
                    <img class="setting" />
                </div>
            </li>
            <li class="ui-state-default"> 
                <div class="first-div"> 
                    <img  class="icon" src="${pageContext.request.contextPath}/resources/img/kafka.png" />
                </div> 
                <div class="second-div">Apache Kafka</div>
                <div style="position: relative;float: right">
                    <img class="setting" />
                </div>
            </li>
            <li class="ui-state-default"> 
                <div class="first-div"> 
                    <img class="icon" src="${pageContext.request.contextPath}/resources/img/spark.png" />
                </div> 
                <div class="second-div">Apache Spark</div>
                <div style="position: relative;float: right">
                    <img class="setting" />
                </div>
            </li>
            <li class="ui-state-default"> 
                <div class="first-div"> 
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
        </ul>

        <ul id="sortable2" class="dropfalse">
            <li class="ui-state-default-start disabled1" style="color:white;padding: 3px 6.5px 5px 8px" >Start</li>
            <li class="ui-state-default-end disabled2" style="color:white;padding: 3px 6.5px 5px 8px">End</li>
        </ul>