<%-- 
    Document   : flowCtrlServicePage
    Created on : 05/04/2016, 5:41:54 PM
    Author     : kho01f
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html>
    <head>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

        <link href="${pageContext.request.contextPath}/resources/css/stepform.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/epoch.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="https://d3js.org/d3.v3.min.js" charset="utf-8"></script>
        <script src="${pageContext.request.contextPath}/resources/js/epoch.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/ctrl.service.js"></script>

        <style>
            .ui-accordion .ui-accordion-header {
                font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
                color:#23527c;
                display: block;
                cursor: pointer;
                position: relative;
                margin: 2px 0 0 0;
                padding: .5em .5em .5em .7em;
                min-height: 0; 
            }
            .ui-accordion .ui-accordion-icons {
                padding-left: 2.2em;
            }
            .ui-accordion .ui-accordion-icons .ui-accordion-icons {
                padding-left: 2.2em;
            }
            .ui-accordion .ui-accordion-header .ui-accordion-header-icon {
                position: absolute;
                left: .5em;
                top: 50%;
                margin-top: -8px;
            }
            .ui-accordion .ui-accordion-content {
                padding: 0px;//1em 2.2em;
                border: 0;
                overflow: auto;
                border: 1px solid #ddd;
            }
            img.icon{
                width:30px; height:30px;
            }
        </style>
        <style>
            .tabs-min { 
                background: transparent; 
                border: none; 
            } 
            .tabs-min .ui-widget-header { 
                background: transparent; 
                border: none; 
                border-bottom: 1px solid #c0c0c0; 
                -moz-border-radius: 0px; 
                -webkit-border-radius: 0px; 
                border-radius: 0px; 
            } 
            .tabs-min .ui-tabs-nav .ui-state-default { 
                background: transparent; 
                border: none; 
            } 
            .tabs-min .ui-tabs-nav .ui-state-active { 
                background: transparent no-repeat bottom center; 
                border: none; 
            } 
            .tabs-min .ui-tabs-nav .ui-state-default a { 
                color: #c0c0c0; 
            } 
            .tabs-min .ui-tabs-nav .ui-state-active a { 
                color: #459e00; 
            }
        </style>
        <style>

            .form-style-large-box{
                width: 950px;
                padding: 20px 12px 10px 20px;
                font: 13px Arial, Helvetica, sans-serif;
                position: relative;
                float: left;
            }
            .form-style-medium-box{
                width: 855px;
                padding: 20px 12px 10px 20px;
                font: 13px Arial, Helvetica, sans-serif;
                position: relative;
                float: left;
            }

            .form-style-xmedium-box{
                width: 500px;
                padding: 20px 12px 10px 20px;
                font: 13px Arial, Helvetica, sans-serif;
                position: relative;
                float: left;
            }
            .form-style-small-box{
                width: 350px;
                padding: 20px 12px 10px 20px;
                font: 13px Arial, Helvetica, sans-serif;
                position: relative;
                float: left;
            }

            .form-style-tiny-box{
                width: 200px;
                padding: 20px 12px 10px 20px;
                font: 13px Arial, Helvetica, sans-serif;
                position: relative;
                float: left;
            }

            .legend { list-style: none; font-size: 12px;}
            .legend li { float: left; margin-right: 10px; }
            .legend span { border: 1px solid #ccc; float: left; width: 12px; height: 12px; margin: 2px; }
            .legend .allocated { background-color: #ff7f0e; }
            .legend .used { background-color: #3182bd; }
            .legend .pending { background-color: #ff7f0e; }
            .legend .sharelimit { background-color: #3182bd; }
            .legend .usedshare { background-color: #31a354; }

            #wrapper   { overflow: auto }
            #firstDiv  {  float: left;}
            #secondDiv {  float: left;}

        </style>
        <script type="text/javascript">
            $(function() {

                var flow = '${flow.platforms}';
                var $flowId = '${flow.flowId}';
                var systems = flow.split(",");
                var system;
                for (var i = 0; i < systems.length; i++) {
                    system = systems[i].replace(' ', '');
                    $("#accordion").children().last()
                            .after('<h3 class="accordion-header ui-accordion-header \n\
                                    ui-helper-reset ui-state-default ui-accordion-icons \n\
                                    ui-corner-all"><span class="ui-accordion-header-icon ui-icon \n\
                                    ui-icon-triangle-1-e"></span><img class="icon" \n\
                                    src="${pageContext.request.contextPath}/resources/img/'
                                    + systems[i] + '.png" /> ' + systems[i] +
                                    '</h3><div id=' + system + ' class="ui-accordion-content ui-helper-reset \n\
                                    ui-widget-content ui-corner-bottom">\n\
                                    </div>');
                }

                $('#ApacheStorm').append('<div class="tabs-min" style="z-index: 200;min-height: 349px;font-size: 12px;width: 100%;">\n\
                                <ul><li><a href="#clusterTab">Cluster</a></li>\n\
                                    <li><a href="#supervisorTab">Supervisor</a></li>\n\
                                    <li><a href="#topologyTab">Topology</a></li></ul>\n\
                                <div id="clusterTab">\n\
                                    <div class="form-style-medium-box">\n\
                                    <div class="form-style-3-heading">Cluster Summary</div>\n\
                                     <table id="clusterTbl"> \n\
                                     <thead>\n\
                                     <tr><th>Resource Name</th><th>Controller Status</th> \n\
                                     <th>Measurement Target</th><th>Ref. Value</th> <th>Scheduling</th> \n\
                                     <th>Backoff No</th> <th>Actions</th>\n\
                                     <th></th></tr></thead><tbody> </tbody></table>\n\
                                     </div><div class="form-style-tiny-box">\n\
                                     <div class="form-style-3-heading">Cluster Health Check</div>\n\
                                     </div>\n\
                                     <div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">CPU</div>\n\
                                     </div><div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Memory</div>\n\
                                     </div><div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Network</div></div>\n\
                                </div>\n\
                                <div id="supervisorTab">\n\
                                <div class="form-style-medium-box">\n\
                                    <div class="form-style-3-heading">Supervisors List</div>\n\
                                     <table id="supervisorTbl"> \n\
                                     <thead>\n\
                                     <tr><th>Resource Name</th><th>Controller Status</th> \n\
                                     <th>Measurement Target</th><th>Ref. Value</th> <th>Scheduling</th> \n\
                                     <th>Backoff No</th> <th>Actions</th>\n\
                                     <th></th></tr></thead><tbody> </tbody></table>\n\
                                     </div><div class="form-style-tiny-box">\n\
                                     <div class="form-style-3-heading">Supervisor Health Check</div>\n\
                                     </div>\n\
                                     <div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">CPU</div>\n\
                                     </div><div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Memory</div>\n\
                                     </div><div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Network</div></div>\n\
                                </div>\n\
                                <div id="topologyTab">\n\
                                <div class="form-style-medium-box">\n\
                                    <div class="form-style-3-heading">Topologies List</div>\n\
                                     <table id="topologyTbl"> \n\
                                     <thead>\n\
                                     <tr><th>Resource Name</th><th>Controller Status</th> \n\
                                     <th>Measurement Target</th><th>Ref. Value</th> <th>Scheduling</th> \n\
                                     <th>Backoff No</th> <th>Actions</th>\n\
                                     <th></th></tr></thead><tbody> </tbody></table>\n\
                                     </div><div class="form-style-tiny-box">\n\
                                     <div class="form-style-3-heading">Topology Health Check</div>\n\
                                     </div>\n\
                                     <div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">CPU</div>\n\
                                     </div><div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Memory</div>\n\
                                     </div><div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Network</div></div>\n\
                                </div>\n\
                            </div>');

                $('#DynamoDB').append('<div class="form-style-medium-box">\n\
                                    <div class="form-style-3-heading">Tables List</div>\n\
                                     <table id="dynamoTbl"> \n\
                                     <thead>\n\
                                      <tr><th>Table Name</th>\n\
                                     <th>Status</th><th>Total Read Capaqcity</th><th>Total Write Capacity</th><th>Actions</th>\n\
                                     <th></th></tr></thead><tbody> </tbody></table>\n\
                                     </div><div id="ConsumedReadCapacityUnits" class="form-style-xmedium-box">\n\
                                     <div class="form-style-3-heading">ConsumedReadCapacityUnits</div>\n\
                                     </div><div id="ConsumedWriteCapacityUnits" class="form-style-xmedium-box">\n\
                                     <div class="form-style-3-heading">ConsumedWriteCapacityUnits</div>\n\
                                     </div><div id="ReadThrottleEvents" class="form-style-xmedium-box">\n\
                                     <div class="form-style-3-heading">ReadThrottleEvents</div></div>\n\
                                     <div id="WriteThrottleEvents" class="form-style-xmedium-box">\n\
                                     <div class="form-style-3-heading">WriteThrottleEvents</div></div>');
                $('#AmazonKinesis').append('<div class="form-style-medium-box">\n\
                                    <div class="form-style-3-heading">Stream List</div>\n\
                                     <table id="kinesisTbl"> \n\
                                     <thead>\n\
                                     <tr><th>Stream Name</th>\n\
                                     <th>Status</th><th>Open Shards</th><th>Closed Shards</th><th>Actions</th>\n\
                                     <th></th></tr></thead><tbody> </tbody></table>\n\
                                     </div><div id="IncomingRecords" class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Total Incoming Records</div>\n\
                                     </div><div id="GetRecordsBytes" class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Read Throughput</div>\n\
                                     </div><div id="IncomingBytes" class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Write Throughput</div></div>');

                $(".tabs-min").tabs();


                $.get("../loadKinesisStreams/" + $flowId, function(strs) {
                    $.each(strs, function(i, str) {
                        $('#kinesisTbl tr:last')
                                .after('<tr><td>' + str + '</td>\n\
                                                    <td>ACTIVE</td>\n\
                                                    <td>1</td><td>0</td>\n\
                                                    <td><img class="refresh" /> </td>\n\
                                                    <td><input type="radio" name="AmazonKinesis" value="' + str + '">\n\
                                                    <input type="radio" name="AmazonKinesis" style="display:none""></td>\n\
                                                    </tr>');
                    });

                });

                $.get("../loadDynamoDBTbls/" + $flowId, function(tbls) {
                    $.each(tbls, function(i, tbl) {
                        $('#dynamoTbl tr:last')
                                .after('<tr><td>' + tbl + '</td>\n\
                                                    <td>ACTIVE</td>\n\
                                                    <td>2</td><td>2</td>\n\
                                                    <td><img class="refresh" /> </td>\n\
                                                    <td><input type="radio" name="DynamoDB" value="' + tbl + '">\n\
                                                    <input type="radio" name="DynamoDB" style="display:none""></td>\n\
                                                    </tr>');
                    });

                });




                var timeoutMap = {};
                function setupDeselectEvent() {
                    var selected = {};
                    $(document).on('click', 'input[type="radio"]', function() {
                        if (this.name in selected && this != selected[this.name])
                            $(selected[this.name]).trigger("deselect");
                        selected[this.name] = this;
                    }).filter(':checked').each(function() {
                        selected[this.name] = this;
                    });
                }

                var kinesisMetricMap = {};
                kinesisMetricMap["IncomingRecords"] = 'IncomingRecords';
                kinesisMetricMap["WriteThroughput"] = 'IncomingBytes';
                kinesisMetricMap["ReadThroughput"] = 'GetRecordsBytes';

                var dynamoMetricMap = {};
                dynamoMetricMap["ConsumedReadCapacityUnits"] = 'ConsumedReadCapacityUnits';
                dynamoMetricMap["ConsumedWriteCapacityUnits"] = 'ConsumedWriteCapacityUnits';
                dynamoMetricMap["WriteThrottleEvents"] = 'WriteThrottleEvents';
                dynamoMetricMap["ReadThrottleEvents"] = 'ReadThrottleEvents';

                function getMetricMap(system) {
                    if (system === "AmazonKinesis") {
                        return kinesisMetricMap;
                    } else if (system === "DynamoDB") {
                        return dynamoMetricMap;
                    }
                }

                setupDeselectEvent(true);
//                var resourceMap = {};
//                resourceMap["DynamoDB"] = 'WriteCapacityUnits';
//                resourceMap["AmazonKinesis"] = 'No. of Shards';
//                resourceMap["ApacheStorm"] = 'No. of VMs';
                $(document).on('deselect', 'input:radio', function() {
                    var $this = $(this);
                    var $platform = $this.attr('name');
                    var metricMap = getMetricMap($platform);
                    for (var key in metricMap) {
                        clearTimeout(timeoutMap[metricMap[key]]);
                        delete timeoutMap[metricMap[key]];
                        $('#' + metricMap[key] + 'LineChart').remove();
                    }
//                    $('#' + $resource + 'BarChart').parent('.wrapper').remove();
                }).on('change', 'input:radio', function() {
                    var $this = $(this);
                    var $resource = $this.val();
                    var $platform = $this.attr('name');
                    var metricMap = getMetricMap($platform);
                    for (var key in metricMap) {
                        var lineChartDiv = '#' + metricMap[key] + 'LineChart';
//                        var barChartDiv = '#' + kinesisMetricMap[key] + '-' + $resource + 'BarChart';
                        var $timeInterval = 1 * 60000;
                        $('#' + metricMap[key])
                                .append('<div id="' + metricMap[key] + 'LineChart" class="epoch category3" style="width: 100%; height: 200px">\n\
                             <div style="z-index: 1"><ul class="legend"><li><span class="used">\n\
                            </span>' + metricMap[key] + '</li></ul></div></div>');
                        var lineChart = setupLineChart(lineChartDiv);
//                        $this.parents('div[class="form-style-ctrl-stat"]')
//                                .siblings('div[class="form-style-resource-share"]').append(
//                                '<div class="wrapper"><div id="' + $platform + '-' + $resource + 'BarChart" class="epoch category3" style="width: 350px; height: 200px"></div>\n\
//                            <ul class="legend">\n\
//                            <li><span class="usedshare"></span>Allocated ' + resourceMap[$ctrlName] + '</li>\n\
//                            <li><span class="pending"></span>Pending Resizing</li>\n\
//                            <li><span class="sharelimit"></span>' + resourceMap[$ctrlName] + ' Share</li>\n\
//                            </ul></div>');
//                        var barChart = setupBarChart(barChartDiv);
                        drawer(lineChart, $platform, $resource, $flowId, metricMap[key], $timeInterval);
                    }
                });

                function setupLineChart(chartDiv) {
                    var graph = $(chartDiv).epoch({
                        type: 'time.line',
                        data: [{label: "metric", values: [{time: getTimeStampSec((new Date()).getTime()), y: 0}]}],
                        axes: ['bottom', 'left', 'right']
                    });
                    return graph;
                }

                function getTimeStampSec(timeStampMill) {
                    return parseInt(timeStampMill / 1000);
                }

                function drawer(lineChart, $platform, $resource, $flowId, $metric, $timeInterval) {
                    $.get('../getCloudWatchStats',
                            {platform: $platform, resource: $resource, flowId: $flowId, metric: $metric, timeStamp: 1000 * 60 * 2},
                    function(data) {
                        if (!data) {
                            console.log(data);
                        } else {
//                            $.each(statRecords, function(i, statRecord) {
                            lineChart.push([{time: getTimeStampSec((new Date()).getTime()), y: data}]);
//                                    {time: getTimeStampSec(statRecord.timeStamp), y: statRecord.allocatedResource}]);
//                                barChart.push([
//                                    {time: getTimeStampSec(ctrlStatRecord.timeStamp), y: Math.random() * 100},
//                                    {time: getTimeStampSec(ctrlStatRecord.timeStamp), y: ctrlStatRecord.nextCtrlDecisionValue},
//                                    {time: getTimeStampSec(ctrlStatRecord.timeStamp), y: ctrlStatRecord.allocatedResource}]);
//                            });
                        }
                        timeoutMap[$metric] = setTimeout(function() {
                            drawer(lineChart, $platform, $resource, $flowId, $metric, $timeInterval);
                        }, $timeInterval);
                    });
                }

                function setupBarChart(chartDiv) {
                    var bar = $(chartDiv).epoch({
                        type: 'time.bar',
                        data: [{values: [{time: getTimeStampSec((new Date()).getTime()), y: 0}]},
                            {values: [{time: getTimeStampSec((new Date()).getTime()), y: 0}]},
                            {values: [{time: getTimeStampSec((new Date()).getTime()), y: 0}]}],
                        axes: ['bottom', 'left', 'right'],
                        windowSize: 10
                    });
//                            var layer_num = 1;
//                            var catname = "category4";
//                            bar.getVisibleLayers()[layer_num].className = "layer " + catname;
                    return bar;
                }

//                function setupPieChart(chartDiv) {
//                    var i = parseInt(Math.random() * 100);
//                    var pie = $(chartDiv).epoch({
//                        type: 'pie',
//                        data: [{label: i.toString(), value: i}, {label: (100 - i).toString(), value: 100 - i}],
//                        inner: 40,
//                        width: 150,
//                        height: 150
//                    });
//                    return pie;
//                }


                var headers = $('#accordion .accordion-header');
                var contentAreas = $('#accordion .ui-accordion-content ').hide();
                var expandLink = $('.accordion-expand-all');
                // add the accordion functionality
                headers.click(function() {
                    var panel = $(this).next();
                    var isOpen = panel.is(':visible');
                    // open or close as necessary
                    panel[isOpen ? 'slideUp' : 'slideDown']()
                            // trigger the correct custom event
                            .trigger(isOpen ? 'hide' : 'show');
                    // stop the link from causing a pagescroll
                    return false;
                });
                // hook up the expand/collapse all
                expandLink.click(function() {
                    var isAllOpen = $(this).data('isAllOpen');
                    contentAreas[isAllOpen ? 'hide' : 'show']()
                            .trigger(isAllOpen ? 'hide' : 'show');
                });
                // when panels open or close, check to see if they're all open
                contentAreas.on({
                    // whenever we open a panel, check to see if they're all open
                    // if all open, swap the button to collapser
                    show: function() {
                        var isAllOpen = !contentAreas.is(':hidden');
                        if (isAllOpen) {
                            expandLink.text('Collapse All')
                                    .data('isAllOpen', true);
                        }
                    },
                    // whenever we close a panel, check to see if they're all open
                    // if not all open, swap the button to expander
                    hide: function() {
                        var isAllOpen = !contentAreas.is(':hidden');
                        if (!isAllOpen) {
                            expandLink.text('Expand all')
                                    .data('isAllOpen', false);
                        }
                    }
                });
            });
        </script>
    </head>
    <%--
    Technique 1:
    $('#ApacheStormTbl tr:last')
        .after('<tr><td>' + '${flowSetting.stormCtrl.targetTopology}' + '</td>\n\
        <td>Active</td>\n\
        <td>' + '${flowSetting.stormCtrl.measurementTarget}' + '</td>\n\
        <td>' + '${flowSetting.stormCtrl.refValue}' + '</td> \n\
        <td>' + '${flowSetting.stormCtrl.monitoringPeriod}' + '</td>\n\
        <td>' + '${flowSetting.stormCtrl.backoffNo}' + '</td>\n\
        <td>7.03</td>\n\
        <td><div id="' + '${flowSetting.stormCtrl.targetTopology}' + '" class="play active" style="text-shadow:none">pause</div></td>\n\
        <td><input type="radio" name="ApacheStormRadio" value="" checked="checked" ></td></tr>');
    
    Technique 2: Iterate through model object using jsp tags
    <c:forEach items="${flowSetting.kinesisCtrls}" var="kCtrl">
    $('#AmazonKinesisTbl tr:last')
    .after('<tr><td>' + '${kCtrl.streamName}' + '</td>\n\
    <td>Running</td>\n\
    <td>' + '${kCtrl.measurementTarget}' + '</td>\n\
    <td>' + '${kCtrl.refValue}' + '</td> \n\
    <td>' + '${kCtrl.monitoringPeriod}' + '</td>\n\
    <td>' + '${kCtrl.backoffNo}' + '</td>\n\
    <td>7.03</td>\n\
    <td><div id="' + '${kCtrl.streamName}' + '" class="play active" style="text-shadow:none">stop</div></td>\n\
    <td><input type="radio" name="' + system + 'Radio" value="' + '${kCtrl.streamName}' + '" \n\
    checked="checked" ></td></tr>');
    </c:forEach>
    --%>
    <body>
        <tiles:insertDefinition name="defaultbar" />

        <div class="col-xs-12">
            <h3><strong style="color: #555">Elasticity Management of <font color="#67B168">${flow.flowName}</font> Flow</strong></h3>
            <hr>
            <!--            <p id="sman">
            
                        </p>-->
            <br>
        </div>

        <div id="settingForm">
            <form id="CtrlInternalSettingsForm" action="../updateCtrlSettings" method="post">

            </form>
        </div>

        <div  class="jumbotron_body">

            <div class="container">

                <div class="row" >

                    <div class="container">

                        <p class="accordion-expand-holder">
                            <a class="accordion-expand-all" href="#">Expand all</a>
                        </p>
                        <div id="accordion" class="ui-accordion ui-widget ui-helper-reset">
                            <span></span>
                        </div>

                    </div>




                </div>            
            </div>
        </div>
        <div class="jumbotron2">
            <div id="buttonContainer" class="container">
            </div>

        </div>

        <tiles:insertDefinition name="defaultfooter" />

    </body>
</html>