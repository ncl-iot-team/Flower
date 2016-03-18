<%-- 
    Document   : flowload
    Created on : 14/02/2016, 4:30:36 PM
    Author     : kho01f
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<html><head>
        <meta charset="utf-8">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <link rel="stylesheet" href="resources/css/flow_load.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script>

            $(function() {

                function updateTips(t, platform) {
                    var tips = document.getElementById(platform + "-validateTips");
                    tips.textContent = t;
                    tips.setAttribute('class', "ui-state-highlight");
                    tips.setAttribute('style', "font-size:12px");
//                    setTimeout(function() {
//                        tips.setAttribute('class', "conf-p");
//                    }, 1500);
                }


                function saveData(platform) {
                    var valid = true;
                    var accessElem = document.getElementById(platform + "-accesskey");
                    var secretElem = document.getElementById(platform + "-secretkey");
                    var categoryElem = document.getElementById(platform + "-categories");

                    if (categoryElem.value == "") {
                        updateTips("Please select the Cloud provider.", platform);
                        categoryElem.setAttribute('class', "ui-state-error text ui-widget-content ui-corner-all");
                        setTimeout(function() {
                            categoryElem.setAttribute('class', "text ui-widget-content ui-corner-all");
                        }, 1500);
                        return false;
                    }
                    if (accessElem.value == "" || accessElem.value == null) {
                        updateTips("Access Key is empty.", platform);
                        accessElem.setAttribute('class', "ui-state-error text ui-widget-content ui-corner-all");
                        setTimeout(function() {
                            accessElem.setAttribute('class', "text ui-widget-content ui-corner-all");
                        }, 1500);
                        return false;
                    }

                    if (secretElem.value == "" || secretElem.value == null) {
                        updateTips("Secret Key is empty.", platform);
                        secretElem.setAttribute('class', "ui-state-error text ui-widget-content ui-corner-all");
                        setTimeout(function() {
                            secretElem.setAttribute('class', "text ui-widget-content ui-corner-all");
                        }, 1500);
                        return false;
                    }

                    if (valid) {
//                    $("#users").append('<img class="warning" />');
                        dialog.dialog("close");
                    }
                    return valid;
                }

                function createDialogForm(platform) {
                    var divContainer = document.getElementById(platform);
                    divContainer.setAttribute("class", 'dialog-setting');
                    var divTag = document.createElement("div");
                    divTag.setAttribute('id', platform + "-dialog-form");
                    var pTag = document.createElement("p");
                    pTag.setAttribute('id', platform + "-validateTips");
                    pTag.textContent = "All form fields are required.";
                    pTag.setAttribute('class', "conf-p");
                    var formTag = document.createElement("form");
                    var fieldsetTag = document.createElement("fieldset");
                    var cloudLabelTag = document.createElement("label");
                    cloudLabelTag.textContent = "Cloud Provider";
                    cloudLabelTag.setAttribute('class', "conf");
                    var catSelectTag = document.createElement("select");
                    catSelectTag.setAttribute('id', platform + "-categories");
                    catSelectTag.setAttribute('class', "text ui-widget-content ui-corner-all");
                    catSelectTag.setAttribute('style', "margin-bottom:12px;height: 25px");
                    catSelectTag.options.add(new Option("--Select--", "", true, true));
                    catSelectTag.options.add(new Option("Amazon Cloud Services", "Amazon"));
                    catSelectTag.options.add(new Option("Google Cloud Platform", "Google"));
                    catSelectTag.options.add(new Option("Microsoft Azure", "Microsoft"));
                    var regionLabelTag = document.createElement("label");
                    regionLabelTag.textContent = "Region";
                    regionLabelTag.setAttribute('class', "conf");
                    var subSelectTag = document.createElement("select");
                    subSelectTag.setAttribute('id', platform + "-subcats");
                    subSelectTag.setAttribute('class', "text ui-widget-content ui-corner-all");
                    subSelectTag.setAttribute('style', "margin-bottom:12px;width:165px;height: 25px");
                    var accessKeyLabelTag = document.createElement("label");
                    accessKeyLabelTag.textContent = "Access Key";
                    accessKeyLabelTag.setAttribute('class', "conf");

                    var accessInputTag = document.createElement("input");
                    accessInputTag.setAttribute('type', "text");
                    accessInputTag.setAttribute('name', "accesskey");
                    accessInputTag.setAttribute('id', platform + "-accesskey");
                    accessInputTag.setAttribute('class', "text ui-widget-content ui-corner-all");
                    var secretKeyLabelTag = document.createElement("label");
                    secretKeyLabelTag.textContent = "Secret Key";
                    secretKeyLabelTag.setAttribute('class', "conf");

                    var secretInputTag = document.createElement("input");
                    secretInputTag.setAttribute('type', "text");
                    secretInputTag.setAttribute('name', "secretkey");
                    secretInputTag.setAttribute('id', platform + "-secretkey");
                    secretInputTag.setAttribute('class', "text ui-widget-content ui-corner-all");

                    fieldsetTag.appendChild(cloudLabelTag);
                    fieldsetTag.appendChild(catSelectTag);
                    fieldsetTag.appendChild(regionLabelTag);
                    fieldsetTag.appendChild(subSelectTag);
                    fieldsetTag.appendChild(accessKeyLabelTag);
                    fieldsetTag.appendChild(accessInputTag);
                    fieldsetTag.appendChild(secretKeyLabelTag);
                    fieldsetTag.appendChild(secretInputTag);
                    formTag.appendChild(fieldsetTag);
                    divTag.appendChild(pTag);
                    divTag.appendChild(formTag);
                    divContainer.appendChild(divTag);
                    setLinkedSubCategory(platform);
                }

                function initDialog(platform) {
                    dialog = $("#" + platform).dialog({
                        autoOpen: false,
                        height: 350,
                        width: 350,
                        modal: true,
                        buttons: {
                            "Save": function() {
                                saveData(platform);
                            },
                            Cancel: function() {
                                dialog.dialog("close");
                            }
                        }
                    });
                }

                function setLinkedSubCategory(platform) {
                    var AWS = [
                        {display: "US East (N. Virginia)", value: "us-east-1"},
                        {display: "US West (Oregon)", value: "us-west-2"},
                        {display: "EU (Ireland)", value: "eu-west-1"},
                        {display: "Asia Pacific (Tokyo)", value: "ap-northeast-1"},
                        {display: "Asia Pacific (Sydney)", value: "ap-southeast-2"}];
                    var Azure = [
                        {display: "R1", value: "R1"},
                        {display: "R2", value: "R2"},
                        {display: "R3", value: "R3"}];
                    $("#" + platform + "-categories").change(function() {
                        var parent = $(this).val();
                        switch (parent) {
                            case 'Amazon':
                                list(AWS);
                                break;
                            case 'Microsoft':
                                list(Azure);
                                break;
                            case 'Google':
                                list(Azure);
                                break;
                            default: //default child option is blank
                                $("#" + platform + "-subcats").html('');
                                break;
                        }
                    });
                    function list(array_list)
                    {
                        $("#" + platform + "-subcats").html(""); //reset child options
                        $(array_list).each(function(i) { //populate child options
                            $("#" + platform + "-subcats").append("<option value=\"" + array_list[i].value + "\">" + array_list[i].display + "</option>");
                        });
                    }
                }

                var platformList = new Array();

                $('#flow-general-setting').submit(function() {
                     $('#sortable').each(function(){
                         platformList.push($(this).text());
                     });
                     $('#hiddenListInput').val(platformList);
                    return true;
                });

                $('#sortable').sortable();
                $('#draggables').sortable();

                var confBtnElem = document.getElementById('sortable');
                confBtnElem.addEventListener('click', removeDraggableLi);
                confBtnElem.addEventListener('click', configPlt);

                $('#sortable').droppable({
                    drop: function(ev, ui) {
                        var title = ui.draggable.attr('title');
                        var platform;
                        switch (title) {
                            case 'Apache Storm':
                                platform = 'sto';
                                break;
                            case 'Amazon Kinesis':
                                platform = 'kin';
                                break;
                            case 'DynamoDB':
                                platform = 'dyn';
                                break;
                            default:
                                return false;
                        }
                        $(ui.draggable).html('<div class="first-div"> <img class="icon" \n\
                        src="${pageContext.request.contextPath}/resources/img/' + title + '.png" /></div> \n\
                        <div class="second-div">' + title + '</div> \n\
                        <div  class="third-div"> \n\
                        <img id="' + platform + 'Conf"  style="opacity: 1" class="setting" />\n\
                        <img id="' + platform + 'Btn" style="opacity: 1" class="bin" /> </div>');
                    }
                });

                function removeDraggableLi(e) {
                    var divDialogName;
                    var deleteEnabled = false;
                    e.stopPropagation();
                    switch (e.target.id) {
                        case 'stoBtn':
                            divDialogName = 'storm';
                            deleteEnabled = true;
                            break;
                        case 'kinBtn':
                            divDialogName = 'kinesis';
                            deleteEnabled = true;
                            break;
                        case 'dynBtn':
                            divDialogName = 'dynamo';
                            deleteEnabled = true;
                            break;
                        default:
                            deleteEnabled = false;
                    }
                    if (deleteEnabled) {
                        $(e.target).parents('li').remove();
                        $("#" + divDialogName).empty();
                    }
                }

                function configPlt(e) {
                    var divDialogName;
                    switch (e.target.id) {
                        case 'stoConf':
                            divDialogName = 'storm';
                            break;
                        case 'kinConf':
                            divDialogName = 'kinesis';
                            break;
                        case 'dynConf':
                            divDialogName = 'dynamo';
                            break;
                        default:
                            return false;
                    }
                    $('body').append('<div id="' + divDialogName + '" title="Platform config information"></div>');
                    initDialog(divDialogName);
                    var element = document.getElementById(divDialogName + "-dialog-form");
                    if (typeof (element) == 'undefined' || element == null)
                    {
                        createDialogForm(divDialogName);
                    }
                    dialog.dialog("open");
                }

                $('#saveBtn').on('click', function() {
                    var analyticsName = $('#flow-name').val();
                    if (analyticsName) {

                    }
                });

                $('#draggables li').draggable({
                    connectToSortable: '#sortable',
                    helper: 'clone',
                    revert: 'invalid',
                    cursor: 'move'
                });
                $("#sortable").sortable({
                    items: "li:not(.disabled1)"
                });
                $("#sortable").sortable({
                    cancel: ".disabled2"
                });
            });
        </script>
    </head>
    <body>
        <tiles:insertDefinition name="defaultbar" />
        <!--        <div class="col-xs-12">
                    <h3><strong style="color: #555">Create Your Analytics Flow</strong></h3>
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
        
        
                                <label class="conf">Analytics Flow Name</label>
                                <input type="text" name="flowName" id="storm-accesskey" class="text ui-widget-content ui-corner-all"/>
        
        
                            </div>
                            <div class=" text-center col-sm-6 col-sm-offset-3 col-md-3 col-xs-offset-4 col-xs-5 col-lg-offset-0 col-lg-2">
                                <a class="btn btn-action" href="#" title="">Load Template</a> 
                            </div>
                        </div>
                    </div>
                </div>-->

        <div class="col-xs-12">
            <h3><strong style="color: #555">Create Your Analytics Flow</strong></h3>
            <hr>
            <p>Drag the platforms that constitute your analytics flow from the left canvas and drop it to the right canvas.
            </p>
            <br>
        </div>


        <div  class="jumbotron_body">
            <div class="container">
                <div class="row">   
                    <h5><strong style="color: #555">Analytics Flow settings</strong></h5>
                    <hr style="width:85%;" align="left">
                    <form id="flow-general-setting" action="submitFlowFormSetting" method="post">
                        <fieldset>
                            <div style="float:left"> <label class="conf">Analytics Flow Name*</label>
                                <input type="text" name="flowName" id="flow-name" class="text ui-widget-content ui-corner-all"/></div>
                            <div style="float:left; margin-left: 50px"><label class="conf">Flow Owner*</label>
                                <select name="owner" class="text ui-widget-content ui-corner-all" style="width: 150px;height: 25px">
                                    <option value=""></option>
                                    <option value="currentUser">Current User</option>
                                </select>
                            </div>
                            <input type="text" name="list[]" id="hiddenListInput" />
                        </fieldset>
                    </form>

                </div>
                <div class="row">
                    <h5><strong style="color: #555">Drag and Drop!</strong></h5>
                    <hr style="width:85%;" align="left">
                    <p style="color: #555555">Systems and Resources List</p>
                    <ul id="draggables" class="droptrue" >

                        <li class="ui-state-default-extention" title='Apache Storm'> 
                            <div class="first-div"> 
                                <img class="icon" src="${pageContext.request.contextPath}/resources/img/Apache Storm.png" />
                            </div> 
                            <div class="second-div">Apache Storm</div>
                            <div  class="third-div">
                                <img class="setting" />
                                <img class="bin" />
                                <!--<img class="warning" />-->
                            </div>
                        </li>
                        <li id="2" class="ui-state-default-extention" title='Amazon Kinesis'> 
                            <div class="image first-div"> 
                                <img class="icon" src="${pageContext.request.contextPath}/resources/img/Amazon Kinesis.png" />
                            </div> 
                            <div class="second-div">Amazon Kinesis</div>
                            <div style="position: relative;float: right">
                                <img class="setting" />
                                <img class="bin" />
                            </div>
                        </li>
                        <li id="3" class="ui-state-default-extention" title='DynamoDB'> 
                            <div class="first-div"> 
                                <img class="icon" src="${pageContext.request.contextPath}/resources/img/DynamoDB.png" />
                            </div> 
                            <div class="second-div">DynamoDB</div>
                            <div style="position: relative;float: right">
                                <img class="setting" />
                                <img class="bin" />
                            </div>
                        </li>
                        <li id="4" class="ui-state-default-extention"  title='Apache Kafka'> 
                            <div class="image first-div"> 
                                <img class="icon" src="${pageContext.request.contextPath}/resources/img/Apache Kafka.png" />
                            </div> 
                            <div class="second-div">Apache Kafka</div>
                            <div style="position: relative;float: right">
                                <img class="setting" />
                                <img class="bin" />
                            </div>
                        </li>
                        <li id="5" class="ui-state-default-extention" title='Apache Spark'> 
                            <div class="image first-div"> 
                                <img class="icon" src="${pageContext.request.contextPath}/resources/img/Apache Spark.png" />
                            </div> 
                            <div class="second-div">Apache Spark</div>
                            <div style="position: relative;float: right">
                                <img class="setting" />
                                <img class="bin" />
                            </div>
                        </li>
                        <li id="6" class="ui-state-default-extention" title = 'Apache Cassandra'> 
                            <div class="image first-div"> 
                                <img class="icon" src="${pageContext.request.contextPath}/resources/img/Apache Cassandra.png" />
                            </div> 
                            <div class="second-div">Apache Cassandra</div>
                            <div style="position: relative;float: right">
                                <img class="setting" />
                                <img class="bin" />
                            </div>
                        </li>
                    </ul>

                    <ul id="sortable" class="dropfalse">
                        <li class="ui-state-default-start disabled2 disabled1" style="color:white;padding: 3px 6.5px 5px 8px" >Start</li>
                        <li class="ui-state-default-end ui-disabled2" style="color:white;padding: 3px 6.5px 5px 8px">End</li>
                    </ul>
                </div>
            </div>            
        </div>

        <div class="jumbotron2">
            <div class="container">
                <div class="row" style="text-align: center">
                    <!--                    <div class="col-xs-12 col-md-9 col-lg-5">
                                        </div>-->
                    <!--<div class="text-center col-sm-6 col-sm-offset-3 col-md-3 col-xs-offset-4 col-xs-5 col-lg-offset-0 col-lg-2" style="width:300px;text-align: right">-->
                    <input type="button" class="btn btn-action" value="Cancel">
                    <input class="btn btn-action" type="submit" form="flow-general-setting" value="Next: Config Systems">
                    <!--</div>-->

                </div>
            </div>

        </div>


        <tiles:insertDefinition name="defaultfooter" />
    </body>
</html>
