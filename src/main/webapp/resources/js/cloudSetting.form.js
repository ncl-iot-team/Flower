/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function createCloudSettingForm(divLoc) {
    var divContainer = $(divLoc);
    divContainer.html('<div class="form-style-2"> \n\
        <div class="form-style-2-heading">Provide your information</div> \n\
        <label for="field1">\n\
        <span>Cloud Provider <span class="required">*</span></span></label>\n\
        <label for="field2"><select id="-categories" class="select-field">\n\
        <option value=""></option> \n\
        <option value="Amazon">Amazon Cloud Services</option>\n\
        <option value="Google">Google Cloud Platform</option>\n\
        <option value="Microsoft">Microsoft Azure</option></select>\n\
        </label><label for="field3">\n\
        <span>Region <span class="required">*</span></span>\n\
        <select id="-subcats" class="select-field"/></label>\n\
        </select>\n\
        <label for="field2"><span>Access Key <span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="accessKey" required/>\n\
        </label><label><span>Secret Key<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="secretKey" value=""/>\n\
        </label><label></label></div>');

//    var divTag = document.createElement("div");
//        divTag.setAttribute('id', platform + "-dialog-form");
//        divTag.setAttribute('title', "Platform config information");

//        var pTag = document.createElement("p");
//        pTag.setAttribute('id', platform + "-validateTips");
//        pTag.textContent = "All form fields are required.";

//    var formTag = document.createElement("form");
//    var fieldsetTag = document.createElement("fieldset");
//
//    var cloudLabelTag = document.createElement("label");
//    cloudLabelTag.textContent = "Cloud Provider";
//
//    var catSelectTag = document.createElement("select");
//    catSelectTag.setAttribute('id', "-categories");
////    catSelectTag.setAttribute('class', "text ui-widget-content ui-corner-all");
//    catSelectTag.setAttribute('style', "margin-bottom:12px");
//    catSelectTag.options.add(new Option("--Select--", "", true, true));
//    catSelectTag.options.add(new Option("Amazon Cloud Services", "Amazon"));
//    catSelectTag.options.add(new Option("Google Cloud Platform", "Google"));
//    catSelectTag.options.add(new Option("Microsoft Azure", "Microsoft"));
//
//    var regionLabelTag = document.createElement("label");
//    regionLabelTag.textContent = "Region";
//
//    var subSelectTag = document.createElement("select");
//    subSelectTag.setAttribute('id', "-subcats");
////    subSelectTag.setAttribute('class', "text ui-widget-content ui-corner-all");
//    subSelectTag.setAttribute('style', "margin-bottom:12px;width:165px");
//
//    var accessKeyLabelTag = document.createElement("label");
//    accessKeyLabelTag.textContent = "Access Key";
//
//    var accessInputTag = document.createElement("input");
//    accessInputTag.setAttribute('type', "text");
//    accessInputTag.setAttribute('name', "accesskey");
//    accessInputTag.setAttribute('id', "-accesskey");
////    accessInputTag.setAttribute('class', "text ui-widget-content ui-corner-all");
//
//    var secretKeyLabelTag = document.createElement("label");
//    secretKeyLabelTag.textContent = "Secret Key";
//
//    var secretInputTag = document.createElement("input");
//    secretInputTag.setAttribute('type', "text");
//    secretInputTag.setAttribute('name', "secretkey");
//    secretInputTag.setAttribute('id', "-secretkey");
////    secretInputTag.setAttribute('class', "text ui-widget-content ui-corner-all");
//
//    fieldsetTag.appendChild(cloudLabelTag);
//    fieldsetTag.appendChild(catSelectTag);
//    fieldsetTag.appendChild(regionLabelTag);
//    fieldsetTag.appendChild(subSelectTag);
//    fieldsetTag.appendChild(accessKeyLabelTag);
//    fieldsetTag.appendChild(accessInputTag);
//    fieldsetTag.appendChild(secretKeyLabelTag);
//    fieldsetTag.appendChild(secretInputTag);
//
//    formTag.appendChild(fieldsetTag);
////        divTag.appendChild(pTag);
////    divTag.appendChild(formTag);
//    divContainer.appendChild(formTag);

    setLinkedSubCategory();
}
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
    $("#-categories").change(function() {
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
                $("#-subcats").html('');
                break;
        }
    });
    function list(array_list)
    {
        $("#-subcats").html(""); //reset child options
        $(array_list).each(function(i) { //populate child options
            $("#-subcats").append("<option value=\"" + array_list[i].value + "\">" + array_list[i].display + "</option>");
        });
    }
}
