<%-- 
    Document   : stepform
    Created on : 17/02/2016, 2:23:23 PM
    Author     : kho01f
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html><head>
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

        <link href="${pageContext.request.contextPath}/resources/css/smart_wizard.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/stepform.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.smartWizard.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.validate.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/cloudSetting.form.js"></script>

        <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.4.1/jsgrid.min.css" />
        <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.4.1/jsgrid-theme.min.css" />

        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.4.1/jsgrid.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {

                jQuery.extend(jQuery.validator.messages, {
                    required: ""
                });

                $('#submit-button').prop('disabled', true).css("cursor", "default");

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
                    // j is sued for ordering divs
                    var j = (systems.length - 1) - i;
                    var $wizardRef = $('#wizard ul');
                    $wizardRef.append('<li><a href=\"#step-' + i + '\"><span class=\"stepDesc\">\n\
                        ' + (i + 1) + '. ' + systems[i] + '</span></a></li>');
                    $wizardRef.after('<div id=\"step-' + j + '\"></div>');
                }

                // Initialize Smart Wizard
                $('#wizard').smartWizard();
                $('.stepContainer').wrap('<form id="stepForms" action="submitFlowCtrlSettingForm" method="post">');

                for (var i = 0; i < systems.length; i++) {
                    switch (systems[i]) {
                        case 'Cloud Setting':
                            createCloudSettingForm('#step-' + i);
                            $('#step-' + i).append('<input type="hidden" name="cloudSetting.flowIdFk" value="${flowId}"></form>');
                            $('#step-' + i).append('<input type="hidden" name="platforms" value="${platforms}"></form>');
                            break;
                        case 'Apache Storm':
                            createStormCtrlForm('#step-' + i);
                            $('#step-' + i).append('<input type="hidden" name="StormCluster.flowIdFk" value="${flowId}"></form>');
                            $('#step-' + i).append('<input type="hidden" name="StormCtrl.flowIdFk" value="${flowId}"></form>');
                            break;
                        case 'Amazon Kinesis':
                            createKinesisCtrlForm('#step-' + i);
                            $('#step-' + i).append('<input type="hidden" name="kinesisCtrl.flowIdFk" value="${flowId}"></form>');
                            break;
                        case 'DynamoDB':
                            createDynamoCtrlForm('#step-' + i);
                            $('#step-' + i).append('<input type="hidden" name="dynamoCtrl.flowIdFk" value="${flowId}"></form>');
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
                        tblMeasure: "required",
                        tblRef: "required",
                        strMeasure: "required",
                        strRef: "required",
                        stormMeasurment: "required",
                        stormRef: "required",
                        topologyList: "required",
                        nimbusIP: "required",
                        zookeeper: "required"
                    }});


                $(".bin").on('click', function(e) {
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
            <p>Placeholder
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