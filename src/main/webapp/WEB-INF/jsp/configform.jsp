<%-- 
    Document   : configform
    Created on : 17/02/2016, 12:24:35 PM
    Author     : kho01f
--%>

<head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
    <link rel="stylesheet" href="resources/css/general_config_form.css">
    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

    <script>
//        $(document).ready(function() {
//            var t = document.getElementById('a');
//            t.textContent = document.con ("xxx").length;
//        });
        $(function() {

            var dialog, form;
//                    allFields = $([]).add(access).add(secret).add(category),


            function updateTips(t) {
                var tips = document.getElementById("validateTips");
                tips.textContent = t;
                tips.setAttribute('class', "ui-state-highlight");
                setTimeout(function() {
                    tips.removeClass("ui-state-highlight", 1500);
                }, 500);
            }

            function saveData() {
                var valid = true;
//                allFields.removeClass("ui-state-error");
                var access = document.getElementById("accesskey").value;
//                document.getElementById("pip").textContent = access;
                var secret = document.getElementById("secretkey").value;
                var category = document.getElementById("categories").value;

                if (category == "") {
                    updateTips("Please select the Cloud provider.");
                    return false;
                }
                if (access == "" || access == null) {
                    updateTips("Access Key is empty.");
                    return false;
                }

                if (secret == "" || secret == null) {
                    updateTips("Secret Key is empty.");
                    return false;
                }

                if (valid) {
//                    $("#users").append('<img class="warning" />');
                    dialog.dialog("close");
                }
                return valid;
            }

            function createDialogForm() {
                var divContainer = document.getElementById("container");

//                divContainer.innerHTML = "<div id=\"dialog-form\" ><p class=\"validateTips\">All form fields are required.</p>\n\
//                                            <form> <fieldset>\n\
//                                            <label for=\"CloudProvider\">Cloud Provider</label>\n\
//                                            <select id=\"categories\" class=\"text ui-widget-content ui-corner-all\" style=\"margin-bottom:12px\">\n\
//                                                <option value=\"\">--Select--</option>\n\
//                                                <option value=\"Amazon\">Amazon Cloud Services</option>\n\
//                                                <option value=\"Google\">Google Cloud Platform</option>\n\
//                                                <option value=\"Microsoft\">Microsoft Azure</option>\n\
//                                            </select>\n\
//                                            <label for=\"region\">Region</label>\n\
//<select id=\"subcats\" class=\"text ui-widget-content ui-corner-all\" style=\"margin-bottom:12px;width:165px\">\n\
//            </select>\n\
//            <label for=\"access\">Access Key</label>\n\
//            <input type=\"text\" name=\"accesskey\" id=\"accesskey\" class=\"text ui-widget-content ui-corner-all\">\n\
//            <label for=\"secret\">Secret Key</label>\n\
//            <input type=\"text\" name=\"secretkey\" id=\"secretkey\" class=\"text ui-widget-content ui-corner-all\">\n\
//        </fieldset>\n\
//    </form>\n\
//</div>";
                var divTag = document.createElement("div");
                divTag.setAttribute('id', "storm-dialog-form");
                divTag.setAttribute('title', "Platform config information");

                var pTag = document.createElement("p");
                pTag.setAttribute('id', "validateTips");
                pTag.textContent = "All form fields are required.";

                var formTag = document.createElement("form");
                var fieldsetTag = document.createElement("fieldset");

                var cloudLabelTag = document.createElement("label");
                cloudLabelTag.textContent = "Cloud Provider";

                var catSelectTag = document.createElement("select");
                catSelectTag.setAttribute('id', "categories");
                catSelectTag.setAttribute('class', "text ui-widget-content ui-corner-all");
                catSelectTag.setAttribute('style', "margin-bottom:12px");
                catSelectTag.options.add(new Option("--Select--", "", true, true));
                catSelectTag.options.add(new Option("Amazon Cloud Services", "Amazon"));
                catSelectTag.options.add(new Option("Google Cloud Platform", "Google"));
                catSelectTag.options.add(new Option("Microsoft Azure", "Microsoft"));

                var regionLabelTag = document.createElement("label");
                regionLabelTag.textContent = "Region";

                var subSelectTag = document.createElement("select");
                subSelectTag.setAttribute('id', "subcats");
                subSelectTag.setAttribute('class', "text ui-widget-content ui-corner-all");
                subSelectTag.setAttribute('style', "margin-bottom:12px;width:165px");

                var accessKeyLabelTag = document.createElement("label");
                accessKeyLabelTag.textContent = "Access Key";

                var accessInputTag = document.createElement("input");
                accessInputTag.setAttribute('type', "text");
                accessInputTag.setAttribute('name', "accesskey");
                accessInputTag.setAttribute('id', "accesskey");
                accessInputTag.setAttribute('class', "text ui-widget-content ui-corner-all");

                var secretKeyLabelTag = document.createElement("label");
                secretKeyLabelTag.textContent = "Secret Key";

                var secretInputTag = document.createElement("input");
                secretInputTag.setAttribute('type', "text");
                secretInputTag.setAttribute('name', "secretkey");
                secretInputTag.setAttribute('id', "secretkey");
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

                setLinkedSubCategory();
            }

            dialog = $("#container").dialog({
                autoOpen: false,
                height: 350,
                width: 350,
                modal: true,
                buttons: {
                    "Save": saveData,
                    Cancel: function() {
                        dialog.dialog("close");
                    }
                }
//                ,
//                close: function() {
//                    //form[ 0 ].reset();
//                    //allFields.removeClass("ui-state-error");
//                }
            });

            form = dialog.find("form").on("save", function(event) {
                event.preventDefault();
//                    addUser();
            });

            $("#stormConf").button().on("click", function() {
                var element = document.getElementById("storm-dialog-form");
                if (typeof(element) == 'undefined' || element == null)
                {
                    createDialogForm();
                }
                dialog.dialog("open");
            });

            function setLinkedSubCategory() {
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
                $("#categories").change(function() {
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
                            $("#subcats").html('');
                            break;
                    }
                });
                function list(array_list)
                {
                    $("#subcats").html(""); //reset child options
                    $(array_list).each(function(i) { //populate child options
                        $("#subcats").append("<option value=\"" + array_list[i].value + "\">" + array_list[i].display + "</option>");
                    });
                }
            }

        });
    </script>
</head>
<p id="a">awsdfrgthyju</p>
<div id="container" title="Platform config information">
</div>
<!--<div id="dialog-form" >-->
<!--    <p class="validateTips">All form fields are required.</p>
    <form>
        <fieldset>
            <label for="CloudProvider">Cloud Provider</label>
            <select id="categories" class="text ui-widget-content ui-corner-all" style="margin-bottom:12px">
                <option value="">--Select--</option>
                <option value="Amazon">Amazon Cloud Services</option>
                <option value="Google">Google Cloud Platform</option>
                <option value="Microsoft">Microsoft Azure</option>
            </select>

            <label for="region">Region</label>
            <select id="subcats" class="text ui-widget-content ui-corner-all" style="margin-bottom:12px;width:165px">
            </select>

            <label for="access">Access Key</label>
            <input type="text" name="accesskey" id="accesskey" class="text ui-widget-content ui-corner-all">
            <label for="secret">Secret Key</label>
            <input type="text" name="secretkey" id="secretkey" class="text ui-widget-content ui-corner-all">

        </fieldset>
    </form>
</div>-->
<input type="image" src="${pageContext.request.contextPath}/resources/img/setting1.png" name="stormConfForm" id="stormConf" />