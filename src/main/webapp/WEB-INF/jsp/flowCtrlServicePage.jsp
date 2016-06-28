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
        <link href="${pageContext.request.contextPath}/resources/css/service-ui.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/stepform.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/epoch.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="https://d3js.org/d3.v3.min.js" charset="utf-8"></script>
        <script src="${pageContext.request.contextPath}/resources/js/epoch.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/ctrl.service.js"></script>

        <script type="text/javascript">
            $(function() {

                var dialog, form;
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
                                    '</h3><div class="ui-accordion-content ui-helper-reset \n\
                                    ui-widget-content ui-corner-bottom"><div class="form-style-ctrl-stat">\n\
                                    <div class="form-style-3-heading">Controller Stats</div>\n\
                                     <table id="' + system + 'Tbl"> \n\
                                     <thead>\n\
                                    <tr><th>Resource Name</th><th>Controller Status</th> \n\
                                     <th>Measurement Target</th><th>Ref. Value</th> <th>Scheduling</th> \n\
                                     <th>Backoff No</th> <th>Actions</th>\n\
                                    <th></th></tr></thead><tbody> </tbody></table>\n\
                                      </div><div class="form-style-resource-share">\n\
                                    <div class="form-style-3-heading">Resource Share</div>\n\
                                   </div><div class="form-style-ctrl-diag">\n\
                                    <div class="form-style-3-heading">Controller Performance Monitoring</div>\n\
                                     </div></div></div></div>');
                }

                //Technique 1: Consuming json using ajax and parsing using each function
                var ctrlStatus;
                var ctrlMap = {};
                $.get("../getCtrls/" + $flowId, function(ctrls) {
                    $.each(ctrls, function(i, ctrl) {
                        ctrlStatus = getCtrlStatus($flowId, ctrl.ctrlName, ctrl.resourceName, ctrl.measurementTarget);
                        ctrlMap[ctrl.ctrlName] = ctrl.ctrlName;
                        $('#' + ctrl.ctrlName + 'Tbl tr:last')
                                .after('<tr><td>' + ctrl.resourceName + '</td>\n\
                                                    <td>' + ctrlStatus + '</td>\n\
                                                    <td>' + ctrl.measurementTarget + '</td>\n\
                                                    <td>' + ctrl.refValue + '</td> \n\
                                                    <td>' + ctrl.monitoringPeriod + '</td>\n\
                                                    <td>' + ctrl.backoffNo + '</td>\n\
                                                    <td><div class="play ' + getCtrlBtnCls(ctrlStatus) + '" style="text-shadow:none">' + getCtrlBtnName(ctrlStatus) + '</div>\n\
                                                    <div class="setting" style="text-shadow:none">Settings</div></td>\n\
                                                    <td><input type="radio" name="' + ctrl.ctrlName + '" value="' + ctrl.resourceName + '"></td></tr>');
                    });

                });

//                for (c in ctrlMap) {
//                    $('#' + c + 'Tbl').children('input[type="radio"]').after('<input type="radio" name="' + c + '" checked>');
//                }

                function getCtrlBtnCls(ctrlStatus) {
                    var $ctrlBtnCls;
                    (ctrlStatus === 'Running') ? $ctrlBtnCls = "active" : $ctrlBtnCls = "";
                    return $ctrlBtnCls;
                }

                function getCtrlBtnName(ctrlStatus) {
                    var $ctrlBtnName;
                    (ctrlStatus === 'Running') ? $ctrlBtnName = "Stop" : $ctrlBtnName = "Start";
                    return $ctrlBtnName;
                }

                function getCtrlStatus(flowId, ctrlName, resourceName, measurementTarget) {
                    return $.ajax({
                        contentType: 'application/json',
                        type: 'GET',
                        url: '../getCtrlStatus',
                        async: false,
                        data: {flowId: flowId, ctrlName: ctrlName, resource: resourceName, measurementTarget: measurementTarget}
                    }).responseText;
                }

                $(document).on('click', '.play', function() {
                    var $this = $(this);
                    var $ctrlName = $this.parents('table').attr('id').replace('Tbl', '');
                    var $resource = $this.closest('tr').find('td:eq(0)').text();
                    var $measurementTarget = $this.closest('tr').find('td:eq(2)').text();
                    $this.toggleClass('active');
                    if (!$this.hasClass('active')) {
                        $this.text('Start');
                        $.post(
                                '../stopCtrl',
                                {ctrlName: $ctrlName, resource: $resource, flowId: $flowId, measurementTarget: $measurementTarget}
                        );
                        $this.closest('tr').find('td:eq(1)').text('Stopped');
                    } else {
                        $this.text('Stop');
                        $.post(
                                '../restartCtrl',
                                {ctrlName: $ctrlName, resource: $resource, flowId: $flowId, measurementTarget: $measurementTarget}
                        );
                        $this.closest('tr').find('td:eq(1)').text('Running');
                    }
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

                setupDeselectEvent(true);
                var resourceMap = {};
                resourceMap["DynamoDB"] = 'WriteCapacityUnits';
                resourceMap["AmazonKinesis"] = 'No. of Shards';
                resourceMap["ApacheStorm"] = 'No. of VMs';
                $(document).on('deselect', 'input:radio', function() {
                    var $resource = $(this).val();
                    clearTimeout(timeoutMap[$resource]);
                    delete timeoutMap[$resource];
                    $('#' + $resource + 'LineChart').remove();
                    $('#' + $resource + 'BarChart').parent('.wrapper').remove();
                }).on('change', 'input:radio', function() {
                    var $this = $(this);
                    var $ctrlName = $this.attr('name');
                    var $resource = $this.val();
                    var lineChartDiv = '#' + $resource + 'LineChart';
                    var barChartDiv = '#' + $resource + 'BarChart';
                    var $timeInterval = parseInt($(this).closest('tr').find('td:eq(4)').text()) * 60000;
                    var $measurementTarget = $(this).closest('tr').find('td:eq(2)').text();
                    $this.parents('div[class="form-style-ctrl-stat"]')
                            .siblings('div[class="form-style-ctrl-diag"]').append(
                            '<div id="' + $resource + 'LineChart" class="epoch category3" style="width: 670px; height: 200px">\n\
                             <div style="z-index: 1"><ul class="legend"><li><span class="used"></span>' + $measurementTarget + '</li>\n\
                            <li><span class="allocated"></span>Allocated Resource</li></ul></div></div>');
                    var lineChart = setupLineChart(lineChartDiv);
                    $this.parents('div[class="form-style-ctrl-stat"]')
                            .siblings('div[class="form-style-resource-share"]').append(
                            '<div class="wrapper"><div id="' + $resource + 'BarChart" class="epoch category3" style="width: 350px; height: 200px"></div>\n\
                            <ul class="legend">\n\
                            <li><span class="usedshare"></span>Allocated ' + resourceMap[$ctrlName] + '</li>\n\
                            <li><span class="pending"></span>Pending Resizing</li>\n\
                            <li><span class="sharelimit"></span>' + resourceMap[$ctrlName] + ' Share</li>\n\
                            </ul></div>');
                    var barChart = setupBarChart(barChartDiv);
                    drawer(lineChart, barChart, $ctrlName, $resource, $flowId, $measurementTarget, $timeInterval);
                });

                function setupLineChart(chartDiv) {
                    var graph = $(chartDiv).epoch({
                        type: 'time.line',
                        data: [{label: "used", values: [{time: getTimeStampSec((new Date()).getTime()), y: 0}]},
                            {label: "allocated", values: [{time: getTimeStampSec((new Date()).getTime()), y: 0}]}],
                        axes: ['bottom', 'left', 'right']
                    });
                    return graph;
                }

                function getTimeStampSec(timeStampMill) {
                    return parseInt(timeStampMill / 1000);
                }

                function drawer(lineChart, barChart, $ctrlName, $resource, $flowId, $measurementTarget, $timeInterval) {
                    $.get('../getCtrlStats',
                            {ctrlName: $ctrlName, resource: $resource, flowId: $flowId, measurementTarget: $measurementTarget, timeStamp: (new Date()).getTime() - $timeInterval},
                    function(ctrlStatRecords) {
                        if (!ctrlStatRecords.length) {
                            console.log(ctrlStatRecords.length);
                        } else {
                            $.each(ctrlStatRecords, function(i, ctrlStatRecord) {
                                lineChart.push([
                                    {time: getTimeStampSec(ctrlStatRecord.timeStamp), y: ctrlStatRecord.measurementTargetValue},
                                    {time: getTimeStampSec(ctrlStatRecord.timeStamp), y: ctrlStatRecord.allocatedResource}]);
                                barChart.push([
                                    {time: getTimeStampSec(ctrlStatRecord.timeStamp), y: Math.random() * 100},
                                    {time: getTimeStampSec(ctrlStatRecord.timeStamp), y: ctrlStatRecord.nextCtrlDecisionValue},
                                    {time: getTimeStampSec(ctrlStatRecord.timeStamp), y: ctrlStatRecord.allocatedResource}]);
                            });
                        }
                        timeoutMap[$resource] = setTimeout(function() {
                            drawer(lineChart, barChart, $ctrlName, $resource, $flowId, $measurementTarget, $timeInterval);
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

                function initDialog(dialogId) {
                    dialog = $(dialogId).dialog({
                        autoOpen: false,
                        height: 770,
                        width: 465,
                        modal: true,
                        open: function(event, ui) {
                            $('.ui-dialog').css('z-index', 1000);
                            $('.ui-dialog').css('line-height', 0.7);
                            $('.ui-dialog').css('font-size', 12);
                        }
                    });
                }

                $(document).on("click", 'input[value = "Cancel"]', function() {
                    closeDialog('#CtrlInternalSettingsForm');
                });

                function closeDialog(frm) {
                    dialog.dialog("close");
                    if ($(frm)) {
                        $(frm).children().remove();
                    }
                }

                $('#CtrlInternalSettingsForm').on('submit', function(event) {
                    var $frm = $('#CtrlInternalSettingsForm');
                    var formData = $frm.serializeArray();
                    var ctrlName = formData[1].value;
                    var resourceName = formData[2].value;
                    var refVal = formData[4].value;
                    var monitoringPeriod = formData[5].value;
                    var backoffNo = formData[6].value;

                    $.ajax({
                        data: formData,
                        type: $frm.attr('method'),
                        url: $frm.attr('action'),
                        success: function() {
                            var tblRow = $("#" + ctrlName + "Tbl tr td").filter(function() {
                                return $(this).text() === resourceName;
                            }).closest("tr");
                            tblRow.find('td:eq(3)').text(refVal);
                            tblRow.find('td:eq(4)').text(monitoringPeriod);
                            tblRow.find('td:eq(5)').text(backoffNo);
                        }
                    });

                    closeDialog('#CtrlInternalSettingsForm');
                    event.preventDefault();
                });

                $(document).on("click", '.setting', function() {
                    var $this = $(this);
                    var $ctrlName = $this.parents('table').attr('id').replace('Tbl', '');
                    var $resource = $this.closest('tr').find('td:eq(0)').text();
                    var $measurementTarget = $this.closest('tr').find('td:eq(2)').text();
                    createCtrlSettingForm($flowId, $ctrlName, $resource, $measurementTarget);
                    initDialog("#settingForm");
                    dialog.dialog("open");
                });



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