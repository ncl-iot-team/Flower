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
        <link href="${pageContext.request.contextPath}/resources/css/service-ui.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/epoch.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="https://d3js.org/d3.v3.min.js" charset="utf-8"></script>
        <script src="${pageContext.request.contextPath}/resources/js/epoch.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/ctrl.service.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/storm.stats.monitor.js"></script>

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
                                    ui-icon-triangle-1-e"></span><img class="accordion-icon" \n\
                                    src="${pageContext.request.contextPath}/resources/img/'
                                    + systems[i] + '.png" /> ' + systems[i] +
                                    '</h3><div id=' + system + ' class="ui-accordion-content ui-helper-reset \n\
                                    ui-widget-content ui-corner-bottom">\n\
                                    </div>');

                    if (system === 'ApacheStorm') {
                        $('#ApacheStorm').append('<div class="tabs-min" style="z-index: 200;min-height: 349px;font-size: 12px;width: 100%;">\n\
                                <ul><li><a href="#clusterTab">Cluster</a></li>\n\
                                    <li><a href="#supervisorTab">Supervisor</a></li>\n\
                                    <li><a href="#topologyTab">Topology</a></li></ul>\n\
                                <div id="clusterTab">\n\
                                    <div class="form-style-large-box">\n\
                                    <div class="form-style-3-heading">Cluster Summary</div>\n\
                                     <table id="clusterTbl"> \n\
                                     <thead>\n\
                                     <tr><th>Storm Version</th><th>Nimbus Uptime</th> \n\
                                     <th>Supervisors</th><th>Total Slots</th> <th>Used Slots</th> \n\
                                     <th>Free Slots</th> <th>Total Executors</th>\n\
                                     <th>Total Tasks</th></tr></thead><tbody> </tbody></table>\n\
                                     </div>\n\
                                     <div id = "cluster-CPUUtilization" class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">CPU Utilization</div>\n\
                                     </div><div id = "cluster-MemoryUtilization" class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Memory Utilization</div>\n\
                                     </div><div id = "cluster-NetworkUtilization" class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Network Utilization</div></div>\n\
                                </div>\n\
                                <div id="supervisorTab">\n\
                                <div class="form-style-large-box">\n\
                                    <div class="form-style-3-heading">Supervisors List</div>\n\
                                     <table id="supervisorTbl"> \n\
                                     <thead>\n\
                                     <tr><th>id</th><th>Host</th> \n\
                                     <th>Uptime</th><th>Total Slots</th> <th>Used Slots</th> \n\
                                     <th>Total Memory</th><th>Total CPU</th><th>Used Memory</th><th>Used CPU</th>\n\
                                     <th></th></tr></thead><tbody> </tbody></table>\n\
                                     </div>\n\
                                     <div id = "supervisor-CPUUtilization" class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">CPU Utilization</div>\n\
                                     </div><div id = "supervisor-MemoryUtilization" class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Memory Utilization</div>\n\
                                     </div><div id = "supervisor-NetworkUtilization" class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">Network Utilization</div></div>\n\
                                </div>\n\
                                <div id="topologyTab">\n\
                                <div class="form-style-large-box" style="width:1010px">\n\
                                    <div class="form-style-3-heading">Topologies List</div>\n\
                                     <table id="topologyTbl"> \n\
                                     <thead>\n\
                                     <tr><th>id</th><th>Name</th> \n\
                                     <th>Status</th><th>Uptime</th> <th>Total Tasks</th> \n\
                                     <th>Total Workers</th> <th>Total Executors</th>\n\
                                     <th></th></tr></thead><tbody> </tbody></table>\n\
                                     </div><div class="form-style-large-box" style="width:1099px">\n\
                                     <div class="tabs-min"  style="width:1099px">\n\
                                    <ul><li><a href="#topologyStats">Topology Stats</a></li>\n\
                                    <li><a href="#spoutsStats">Spouts</a></li>\n\
                                    <li><a href="#boltsStats">Bolts</a></li>\n\
                                    </ul><div id="topologyStats"></div>\n\
                                    <div id="spoutsStats"></div>\n\
                                    <div id="boltsStats"></div>\n\
                                    </div></div>\n\
                                </div>\n\
                            </div>');

                        $.getJSON("../loadClusterSummary/" + $flowId, function(data) {
                            $('#clusterTbl tr:last').after('<tr><td>' + data.stormVersion + '</td>\n\
                                                                <td>' + data.nimbusUptime + '</td>\n\
                                                                <td>' + data.supervisors + '</td>\n\
                                                                <td>' + data.slotsTotal + '</td>\n\
                                                                <td>' + data.slotsUsed + '</td>\n\
                                                                <td>' + data.slotsFree + '</td>\n\\n\
                                                                <td>' + data.executorsTotal + '</td>\n\
                                                                <td>' + data.tasksTotal + '</td></tr>');
                        });
                        $.getJSON("../loadSupervisorList/" + $flowId, function(list) {
                            $.each(list.supervisors, function(i, supervisor) {
                                $('#supervisorTbl tr:last').after('<tr><td>' + supervisor.id + '</td>\n\
                                                                <td>' + supervisor.host + '</td>\n\
                                                                <td>' + supervisor.uptime + '</td>\n\
                                                                <td>' + supervisor.slotsTotal + '</td>\n\
                                                                <td>' + supervisor.slotsUsed + '</td>\n\
                                                                <td>' + supervisor.totalMem + '</td>\n\
                                                                <td>' + supervisor.totalCpu + '</td>\n\
                                                                <td>' + supervisor.usedMem + '</td>\n\
                                                                <td>' + supervisor.usedCPU + '</td>\n\
                                                                <td><input type="radio" name="supervisorList" value="' + supervisor.id + '"></tr>');
                            });
                        });
                        $.getJSON("../loadTopologyList/" + $flowId, function(list) {
                            $.each(list.topologies, function(i, topology) {
                                $('#topologyTbl tr:last').after('<tr><td>' + topology.id + '</td>\n\
                                                                <td>' + topology.name + '</td>\n\
                                                                <td>' + topology.status + '</td>\n\
                                                                <td>' + topology.uptime + '</td>\n\
                                                                <td>' + topology.tasksTotal + '</td>\n\\n\
                                                                <td>' + topology.workersTotal + '</td>\n\
                                                                <td>' + topology.executorsTotal + '</td>\n\
                                                                <td><input type="radio" name="topologyList" value="' + topology.id + '"></tr>');
                            });
                        });

                        launchClusterDiagrams($flowId);
                    }
                    if (system === 'DynamoDB') {

                        var dynamoMetricMap = {};
                        dynamoMetricMap["ConsumedReadCapacityUnits"] = 'ConsumedReadCapacityUnits';
                        dynamoMetricMap["ConsumedWriteCapacityUnits"] = 'ConsumedWriteCapacityUnits';
                        dynamoMetricMap["WriteThrottleEvents"] = 'WriteThrottleEvents';
                        dynamoMetricMap["ReadThrottleEvents"] = 'ReadThrottleEvents';

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
                    }
                    if (system === 'AmazonKinesis') {

                        var kinesisMetricMap = {};
                        kinesisMetricMap["IncomingRecords"] = 'IncomingRecords';
                        kinesisMetricMap["WriteThroughput"] = 'IncomingBytes';
                        kinesisMetricMap["ReadThroughput"] = 'GetRecordsBytes';

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
                    }
                }

                $(".tabs-min").tabs();
//                 $(".tabs-nohdr").tabs();

                function launchClusterDiagrams($flowId) {
                    var stormClusterMetricMap = {};
                    stormClusterMetricMap["cluster-CPUUtilization"] = 'cluster-CPUUtilization';
                    stormClusterMetricMap["cluster-MemoryUtilization"] = 'cluster-MemoryUtilization';
                    stormClusterMetricMap["cluster-NetworkUtilization"] = 'cluster-NetworkUtilization';
                    
                    stormClusterMetricMap["supervisor-CPUUtilization"] = 'supervisor-CPUUtilization';
                    stormClusterMetricMap["supervisor-MemoryUtilization"] = 'supervisor-MemoryUtilization';
                    stormClusterMetricMap["supervisor-NetworkUtilization"] = 'supervisor-NetworkUtilization';

                    var $timeInterval = 1 * 5000;
                    for (var key in stormClusterMetricMap) {
                        var lineChartDiv = '#' + stormClusterMetricMap[key] + 'LineChart';
                        $('#' + stormClusterMetricMap[key])
                                .append('<div id="' + stormClusterMetricMap[key] + 'LineChart" class="epoch category3" style="width: 100%; height: 200px">\n\
                             <div style="z-index: 1"><ul class="legend"><li><span class="used">\n\
                            </span>' + stormClusterMetricMap[key] + '</li></ul></div></div>');
                        var lineChart = setupLineChart(lineChartDiv);
                        stormDigaramDrawer(lineChart, stormClusterMetricMap[key], $timeInterval);
                    }
                }

                function stormDigaramDrawer(lineChart, $metric, $timeInterval) {
//                    $.get('../getStormClusterStats',
//                            {flowId: $flowId, metric: $metric, timeStamp: 1000 * 60 * 2},
//                    function(data) {
                    lineChart.push([{time: getTimeStampSec((new Date()).getTime()), y: Math.random() * 100}]);
                    setTimeout(function() {
                        stormDigaramDrawer(lineChart, $metric, $timeInterval);
                    }, $timeInterval);
//                    });
                }

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

                function getMetricMap(mapName) {
                    switch (mapName) {
                        case "AmazonKinesis":
                            return kinesisMetricMap;
                        case "DynamoDB":
                            return dynamoMetricMap;
                        case "spouts":
                            return spoutMetricMap;
                        case "bolts":
                            return boltMetricMap;
                    }
                }

                setupDeselectEvent(true);
                var timeoutMap = {};
                $(document).on('deselect', 'input[name=AmazonKinesis],input[name=DynamoDB]', function() {
                    var $this = $(this);
                    var $platform = $this.attr('name');
                    var metricMap = getMetricMap($platform);
                    for (var key in metricMap) {
                        clearTimeout(timeoutMap[metricMap[key]]);
                        delete timeoutMap[metricMap[key]];
                        $('#' + metricMap[key] + 'LineChart').remove();
                    }
                }).on('change', 'input[name=AmazonKinesis],input[name=DynamoDB]', function() {
                    var $this = $(this);
                    var $resource = $this.val();
                    var $platform = $this.attr('name');
                    var metricMap = getMetricMap($platform);
                    var $timeInterval = 1 * 60000;
                    for (var key in metricMap) {
                        var lineChartDiv = '#' + metricMap[key] + 'LineChart';
                        $('#' + metricMap[key]).append('<div id="' + metricMap[key] + 'LineChart" class="epoch category3" style="width: 100%; height: 200px">\n\
                             <div style="z-index: 1"><ul class="legend"><li><span class="used">\n\
                            </span>' + metricMap[key] + '</li></ul></div></div>');
                        var lineChart = setupLineChart(lineChartDiv);
                        drawer(lineChart, $platform, $resource, $flowId, metricMap[key], $timeInterval);
                    }
                });

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
                            lineChart.push([{time: getTimeStampSec((new Date()).getTime()), y: data}]);
                        }
                        timeoutMap[$metric] = setTimeout(function() {
                            drawer(lineChart, $platform, $resource, $flowId, $metric, $timeInterval);
                        }, $timeInterval);
                    });
                }

                var topologyMetricMap = {};
                topologyMetricMap["topologyStats-emitted"] = 'topologyStats-emitted';
                topologyMetricMap["topologyStats-trasferred"] = 'topologyStats-trasferred';
                topologyMetricMap["topologyStats-completeLatency"] = 'topologyStats-completeLatency';
                topologyMetricMap["topologyStats-acked"] = 'topologyStats-acked';
                topologyMetricMap["topologyStats-failed"] = 'topologyStats-failed';
                var topologyTimeoutMap = {};
                $(document).on('deselect', 'input[name=topologyList]', function() {
                    for (var key in topologyMetricMap) {
                        clearTimeout(topologyTimeoutMap[topologyMetricMap[key]]);
                        delete timeoutMap[topologyMetricMap[key]];
//                        $('#' + topologyMetricMap[key] + 'LineChart').remove();
                        $('#topologyStats').children().remove();
                        $('#spoutsStats').children().remove();
                        $('#boltsStats').children().remove();

                    }
                }).on('change', 'input[name=topologyList]', function() {
                    var $topologyId = $(this).val();
                    var $timeInterval = 1 * 60000;
                    for (var key in topologyMetricMap) {
                        var lineChartDiv = '#' + topologyMetricMap[key] + 'LineChart';
                        $('#topologyStats').append('<div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">' + topologyMetricMap[key] + '</div>\n\
                                     <div id="' + topologyMetricMap[key] + 'LineChart" class="epoch category3" style="width: 100%; height: 200px">\n\
                                     <div style="z-index: 1"><ul class="legend"><li><span class="used">\n\
                                     </span>' + topologyMetricMap[key] + '</li></ul></div></div></div>');
                        var lineChart = setupLineChart(lineChartDiv);
                        topologyStatDrawer(lineChart, $flowId, $topologyId, topologyMetricMap[key], $timeInterval);
                    }

                    $('#spoutsStats').append('<div class="form-style-large-box">\n\
                                    <table id="spoutTbl"> \n\
                                     <thead>\n\
                                     <tr><th>Spout id</th><th>Executors</th> \n\
                                     <th>Tasks</th><th>Last Error</th><th>Elapsed Sec. Since Error</th><th>Error Log Link</th>\n\
                                     <th></th></tr></thead><tbody> </tbody></table></div>');
                    $('#boltsStats').append('<div class="form-style-large-box">\n\
                                     <table id="boltTbl"> \n\
                                     <thead>\n\
                                     <tr><th>Bolt id</th><th>Executors</th> \n\
                                     <th>Tasks</th><th>Last Error</th><th>Elapsed Sec. Since Error</th><th>Error Log Link</th>\n\
                                     <th></th></tr></thead><tbody> </tbody></table></div>');

                    $.getJSON('../getTopologyStats?flowId=' + $flowId + '&topologyId=' + $topologyId, function(data) {
                        $.each(data.spouts, function(i, spout) {
                            $('#spoutTbl tr:last').after('<tr><td>' + spout.spoutId + '</td>\n\
                                      <td>' + spout.executors + '</td>\n\
                                       <td>' + spout.tasks + '</td>\n\
                                       <td>' + spout.lastError + '</td>\n\
                                       <td>' + spout.errorLapsedSecs + '</td>\n\
                                       <td>' + spout.errorWorkerLogLink + '</td>\n\
                                        <td><input type="radio" name="spouts" value="' + spout.spoutId + '">\n\
                                         </td>\n\
                                          </tr>');
                        });
                        $.each(data.bolts, function(i, bolt) {
                            $('#boltTbl tr:last').after('<tr><td>' + bolt.boltId + '</td>\n\
                                      <td>' + bolt.executors + '</td>\n\
                                       <td>' + bolt.tasks + '</td>\n\
                                       <td>' + bolt.lastError + '</td>\n\
                                       <td>' + bolt.errorLapsedSecs + '</td>\n\
                                       <td>' + bolt.errorWorkerLogLink + '</td>\n\
                                        <td><input type="radio" name="bolts" value="' + bolt.boltId + '">\n\
                                         </td>\n\
                                          </tr>');
                        });
                    });
                });

                function topologyStatDrawer(lineChart, $flowId, $topologyId, $metric, $timeInterval) {
                    $.getJSON('../getTopologyStats?flowId=' + $flowId + '&topologyId=' + $topologyId, function(data) {
                        var metricValue = data[$metric.split('-')[0]][0][$metric.split('-')[1]];
                        lineChart.push([{time: getTimeStampSec((new Date()).getTime()), y: metricValue}]);
                        topologyTimeoutMap[$metric] = setTimeout(function() {
                            topologyStatDrawer(lineChart, $flowId, $topologyId, $metric, $timeInterval);
                        }, $timeInterval);
                    });
                }

                var spoutMetricMap = {};
                spoutMetricMap["spoutSummary-emitted"] = 'spoutSummary-emitted';
                spoutMetricMap["spoutSummary-trasferred"] = 'spoutSummary-trasferred';
                spoutMetricMap["spoutSummary-completeLatency"] = 'spoutSummary-completeLatency';
                spoutMetricMap["spoutSummary-acked"] = 'spoutSummary-acked';
                spoutMetricMap["spoutSummary-failed"] = 'spoutSummary-failed';

                var boltMetricMap = {};
                boltMetricMap["boltStats-emitted"] = 'boltStats-emitted';
                boltMetricMap["boltStats-trasferred"] = 'boltStats-trasferred';
                boltMetricMap["boltStats-processLatency"] = 'boltStats-processLatency';
                boltMetricMap["boltStats-acked"] = 'boltStats-acked';
                spoutMetricMap["boltStats-failed"] = 'boltStats-failed';

                var spoutTimeoutMap = {};
                $(document).on('deselect', 'input[name=spouts],[name=bolts]', function() {
                    var componentType = $(this).attr('name');
                    var metricMap = getMetricMap(componentType);
                    for (var key in metricMap) {
                        clearTimeout(spoutTimeoutMap[metricMap[key]]);
                        delete timeoutMap[metricMap[key]];
                        $('#' + componentType + 'Stats').find('.form-style-small-box').remove();
                    }
                }).on('change', 'input[name=spouts],[name=bolts]', function() {
                    var componentType = $(this).attr('name');
                    var metricMap = getMetricMap(componentType);
                    var $topologyId = $('input[name=topologyList]:checked').val();
                    var $spoutId = $(this).val();
                    var $timeInterval = 1 * 60000;

                    for (var key in metricMap) {
                        var lineChartDiv = '#' + metricMap[key] + 'LineChart';
                        $('#' + componentType + 'Stats').append('<div class="form-style-small-box">\n\
                                     <div class="form-style-3-heading">' + metricMap[key] + '</div>\n\
                                     <div id="' + metricMap[key] + 'LineChart" class="epoch category3" style="width: 100%; height: 200px">\n\
                                     <div style="z-index: 1"><ul class="legend"><li><span class="used">\n\
                                     </span>' + metricMap[key] + '</li></ul></div></div></div>');
                        var lineChart = setupLineChart(lineChartDiv);
                        spoutStatDrawer(lineChart, $flowId, $topologyId, $spoutId, metricMap[key], $timeInterval);
                    }

                });
                function spoutStatDrawer(lineChart, $flowId, $topologyId, $spoutId, $metric, $timeInterval) {
                    $.getJSON('../getComponentStats?flowId=' + $flowId + '&topologyId=' + $topologyId + '&componentId=' + $spoutId, function(data) {
                        var metricValue = data[$metric.split('-')[0]][0][$metric.split('-')[1]];
                        lineChart.push([{time: getTimeStampSec((new Date()).getTime()), y: metricValue}]);
                        spoutTimeoutMap[$metric] = setTimeout(function() {
                            spoutStatDrawer(lineChart, $flowId, $topologyId, $spoutId, $metric, $timeInterval);
                        }, $timeInterval);
                    });
                }


                function setupLineChart(chartDiv) {
                    var graph = $(chartDiv).epoch({
                        type: 'time.line',
                        data: [{label: "metric", values: [{time: getTimeStampSec((new Date()).getTime()), y: 0}]}],
                        axes: ['bottom', 'left', 'right']
                    });
                    return graph;
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
    <body>
        <tiles:insertDefinition name="defaultbar" />

        <div class="col-xs-12">
            <h3><strong style="color: #555">Holistic Monitoring of <font color="#67B168">${flow.flowName}</font> Flow</strong></h3>
            <hr>
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