<%-- 
    Document   : stepform
    Created on : 17/02/2016, 2:23:23 PM
    Author     : kho01f
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html><head>
        <script type='text/javascript' >
            var globalIndexMap = -1;
        </script>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

        <link href="${pageContext.request.contextPath}/resources/css/smart_wizard.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/stepform.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.smartWizard.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.validate.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/ctrl.step.forms.js"></script>

        <style>
            table td:last-child {
                width: 1px;  
            }

            table td:nth-child(5){
                width: 5%;
            }
            table td:nth-child(4){
                width: 15%;
            }
            table td:nth-child(3){
                width: 20%;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {

                jQuery.extend(jQuery.validator.messages, {
                    required: ""
                });

                $('#submit-button').prop('disabled', true).css("cursor", "default");

                var flow = '${flow.platforms}';
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

                var flowId = '${flow.flowId}';
                // Initialize Smart Wizard
                $('#wizard').smartWizard();
                $('.stepContainer').wrap('<form id="stepForms" action="../ctrls/submitFlowCtrlSettingForm/' + flowId + '" method="post"></form>');

                for (var i = 0; i < systems.length; i++) {
                    switch (systems[i]) {
                        case 'Cloud Setting':
                            createCloudSettingForm('#step-' + i);
                            break;
                        case 'Apache Storm':
                            createStormCtrlForm('#step-' + i);
                            break;
                        case 'Amazon Kinesis':
                            createKinesisCtrlForm('#step-' + i);
                            break;
                        case 'DynamoDB':
                            createDynamoCtrlForm('#step-' + i);
                            break;
                        default:
                            break;
                    }
                }

                $("#stepForms").validate({
                    errorPlacement: function(error, element) {
                        error.addClass('arrow');
                        error.insertAfter(element);
                    },
                    rules: {
                        "cloudSetting.accessKey": "required",
                        "cloudSetting.secretKey": "required",
                        "cloudSetting.cloudProvider": "required",
                        "cloudSetting.region": "required",
                        "stormCluster.nimbusIp": "required",
                        "stormCluster.supervisorPrefix": "required",
                        "stormCluster.uiIp": "required",
                        "stormCluster.uiPort": "required"
                    }});

                function addValidationRule() {
                    $('select[name*=measurementTarget],input[name*=refValue],input[name*=resourceName]').each(function() {
                        $(this).rules('add', {
                            required: true
                        });
                    });
                }
                addValidationRule();

                function populateCloudSettingObj() {
                    var cloudSetting = {};
                    cloudSetting["cloudProvider"] = $("#-categories").val();
                    cloudSetting["region"] = $("#-subcats").val();
                    cloudSetting["accessKey"] = $("#accessKey").val();
                    cloudSetting["secretKey"] = $("#secretKey").val();
                    return cloudSetting;
                }

                var faildReqMsg = '<p class="failed-request">Request failed! \n\
                Please check the provided Cloud hosting information again.</p>';

                var noTblMsg = '<p class="failed-request">Oops! No tables were found.</p>';

                var noStreamMsg = '<p class="failed-request">Oops! No Streams were found.</p>';

                $('#loadStreams').on('click', function() {
                    $.ajax({
                        type: 'POST',
                        contentType: 'application/json',
                        dataType: 'json',
                        url: "../ctrls/loadKinesisStreams",
                        data: JSON.stringify(populateCloudSettingObj()),
                        success: function(data) {
                            if (!data.length) {
                                ajaxReqMsg('#loadStreams', '.failed-request', noStreamMsg);
                            } else {
                                $.each(data, function(index, str) {
                                    if (!$('input[value=' + str + ']').length) {
                                        globalIndexMap++;
                                        $('#kinesisTbl tr:last').after('<tr> <td><input type="text" style="border: 0px;background:#fafafa;text-align:center" \n\
                                                                name="ctrls[' + globalIndexMap + '].resourceName" readonly=true value="' + str + '"></td><td>\n\
                                                                <select name="ctrls[' + globalIndexMap + '].measurementTarget" class="select-field">\n\
                                                                <option value=""></option><option value="IncomingRecords">Incoming Records</option>\n\
                                                                </td><td> <input type="text" class="input-field" name="ctrls[' + globalIndexMap + '].refValue" value=""/></td> \n\
                                                                <td><input type="text" class="input-field" name="ctrls[' + globalIndexMap + '].monitoringPeriod" value=""/>\n\
                                                                </td><td><input type="text" class="input-field" name="ctrls[' + globalIndexMap + '].backoffNo" value=""/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].ctrlName" value="AmazonKinesis"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].epsilon" value="0.0001"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].upperK0" value="0.1"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].upInitK0" value="0.08"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].lowInitK0" value="0.02"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].lowerK0" value="0"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].k_init" value="0.03"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].gamma" value="0.0003"/></td>\n\
                                                                <td><img class="bin"/></td></tr>');
                                    }
                                });
                                addValidationRule();
                            }
                        },
                        error: function(e) {
                            ajaxReqMsg('#loadStreams', '.failed-request', faildReqMsg);
                        }
                    });
                });

                $('#loadTbls').on('click', function() {
                    $.ajax({
                        type: 'POST',
                        contentType: 'application/json',
                        dataType: 'json',
                        url: "../ctrls/loadDynamoTables",
                        data: JSON.stringify(populateCloudSettingObj()),
                        success: function(data) {
//                            debugger;
                            if (!data.length) {
                                ajaxReqMsg('#loadTbls', '.failed-request', noTblMsg);
                            } else {
                                $.each(data, function(index, tbl) {
                                    if (!$('input[value=' + tbl + ']').length) {
                                        globalIndexMap++;
                                        $('#dynamoTbl tr:last').after('<tr><td><input type="text" style="border: 0px;background:#fafafa;text-align:center" \n\
                                                        name="ctrls[' + globalIndexMap + '].resourceName" readonly=true value="' + tbl + '"></td><td>\n\
                                                        <select name="ctrls[' + globalIndexMap + '].measurementTarget" class="select-field">\n\
                                                        <option value=""></option><option value="ConsumedWriteCapacityUnits">Write Capacity</option></td>\n\
                                                        <td><input type="text" class="input-field" name="ctrls[' + globalIndexMap + '].refValue"/></td>\n\
                                                        <td><input type="text" class="input-field" name="ctrls[' + globalIndexMap + '].monitoringPeriod"/></td>\n\
                                                        <td><input type="text" class="input-field" name="ctrls[' + globalIndexMap + '].backoffNo"/> \n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].ctrlName" value="DynamoDB"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].epsilon" value="0.0001"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].upperK0" value="0.1"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].upInitK0" value="0.08"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].lowInitK0" value="0.02"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].lowerK0" value="0"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].k_init" value="0.03"/>\n\
                                                                <input type="hidden" name="ctrls[' + globalIndexMap + '].gamma" value="0.0003"/></td>\n\
                                                        <td><img class="bin"/></td></tr>');
                                    }
                                });
                                addValidationRule();
                            }
                        },
                        error: function(e) {
                            ajaxReqMsg('#loadTbls', '.failed-request', faildReqMsg);
                        }
                    });
                });

                function ajaxReqMsg(position, selectorCls, msg) {
                    if (!$(selectorCls).length) {
                        $(position).after(msg);
                        setTimeout(function() {
                            $(selectorCls).remove();
                        }, 5000);
                    }
                }

                $('#dynamoTbl').on('click', '.bin', function(e) {
                    $(e.target).parents('tr').remove();
                });
                $('#kinesisTbl').on('click', '.bin', function(e) {
                    $(e.target).parents('tr').remove();
                });

            });


        </script>
    </head>

    <body>
        <tiles:insertDefinition name="defaultbar" />

        <div class="col-xs-12">
            <h3><strong style="color: #555"></strong>Flow Configuration</h3>
            <hr>
            <p id="ssman">Please complete the below step form. You can modify the default settings now or after the controller has been launched.  
            </p>
            <br>
        </div>


        <div  class="jumbotron_body">
            <div style="float:left" >
                <ul id="sortable">
                    <li class="ui-state-default-start" style="color:#fbfbfb; padding: 3px 6.5px 5px 8px">Analytics Flow</li>

                </ul>
            </div>
            <div class="container">

                <div class="row" >

                    <div id="wizard" class="swMain">
                        <ul>

                        </ul>

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