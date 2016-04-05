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

        $(function() {

            var dialog, form;
            initDialog("storm");

            function updateTips(t, platform) {
                var tips = document.getElementById(platform + "-validateTips");
                tips.textContent = t;
                tips.setAttribute('class', "ui-state-highlight");
                setTimeout(function() {
                    tips.removeClass("ui-state-highlight", 1500);
                }, 500);
            }

            function saveData(platform) {
                var valid = true;
                var access = document.getElementById(platform + "-accesskey").value;
                var secret = document.getElementById(platform + "-secretkey").value;
                var category = document.getElementById(platform + "-categories").value;

                if (category == "") {
                    updateTips("Please select the Cloud provider.", platform);
                    return false;
                }
                if (access == "" || access == null) {
                    updateTips("Access Key is empty.", platform);
                    return false;
                }

                if (secret == "" || secret == null) {
                    updateTips("Secret Key is empty.", platform);
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
                divTag.setAttribute('id', platform + "-dialog-form");
                divTag.setAttribute('title', "Platform config information");

                var pTag = document.createElement("p");
                pTag.setAttribute('id', platform + "-validateTips");
                pTag.textContent = "All form fields are required.";

                var formTag = document.createElement("form");
                var fieldsetTag = document.createElement("fieldset");

                var cloudLabelTag = document.createElement("label");
                cloudLabelTag.textContent = "Cloud Provider";

                var catSelectTag = document.createElement("select");
                catSelectTag.setAttribute('id', platform + "-categories");
                catSelectTag.setAttribute('class', "text ui-widget-content ui-corner-all");
                catSelectTag.setAttribute('style', "margin-bottom:12px");
                catSelectTag.options.add(new Option("--Select--", "", true, true));
                catSelectTag.options.add(new Option("Amazon Cloud Services", "Amazon"));
                catSelectTag.options.add(new Option("Google Cloud Platform", "Google"));
                catSelectTag.options.add(new Option("Microsoft Azure", "Microsoft"));

                var regionLabelTag = document.createElement("label");
                regionLabelTag.textContent = "Region";

                var subSelectTag = document.createElement("select");
                subSelectTag.setAttribute('id', platform + "-subcats");
                subSelectTag.setAttribute('class', "text ui-widget-content ui-corner-all");
                subSelectTag.setAttribute('style', "margin-bottom:12px;width:165px");

                var accessKeyLabelTag = document.createElement("label");
                accessKeyLabelTag.textContent = "Access Key";

                var accessInputTag = document.createElement("input");
                accessInputTag.setAttribute('type', "text");
                accessInputTag.setAttribute('name', "accesskey");
                accessInputTag.setAttribute('id', platform + "-accesskey");
                accessInputTag.setAttribute('class', "text ui-widget-content ui-corner-all");

                var secretKeyLabelTag = document.createElement("label");
                secretKeyLabelTag.textContent = "Secret Key";

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

            form = dialog.find("form").on("save", function(event) {
                event.preventDefault();
            });

            $("#stormConf").button().on("click", function() {
                var platform = "storm";
                var element = document.getElementById(platform + "-dialog-form");
                if (typeof (element) == 'undefined' || element == null)
                {
                    createDialogForm(platform);
                }
                dialog.dialog("open");
            });

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

        });
    </script>
</head>
<div id="storm" title="Platform config information">
</div>
<input type="image" src="${pageContext.request.contextPath}/resources/img/setting1.png" id="stormConf" />