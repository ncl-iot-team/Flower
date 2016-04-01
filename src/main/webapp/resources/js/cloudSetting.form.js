/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function createKinesisCtrlForm(divLoc) {
    var divContainer = $(divLoc);
    divContainer.html('<div class="form-style-2"> \n\
        <div class="form-style-2-heading">Kinesis controller setting</div> \n\
        <table><thead>\n\
        <tr><th>Table Name</th><th>Measurement Target</th> <th>Reference Value</th><th>Monitoring Period</th> <th>Backoff No.</th> <th>56</th></tr></thead> <tbody>\n\
         <tr> <td>Test Table</td><td>\n\
        <select id="-categories" name="cloudProviderCat" class="select-field">\n\
        <option value=""></option> \n\
        <option value="tbl-measure">Write Capacity</option>\n\
        </td><td> <input type="text" class="input-field" name="tbl-ref" value=""/></td> \n\
            <td><input type="text" class="input-field" name="tbl-monitoring" value=""/>\n\
            </td><td><input type="text" class="input-field" name="tbl-backoff" value=""/></td><td>55</td></tr></tbody></table>');

}

function createCloudSettingForm(divLoc) {
    var divContainer = $(divLoc);
    divContainer.html('<div class="form-style-2"> \n\
        <div class="form-style-2-heading">Cloud hosting information</div> \n\
        <label for="field1">\n\
        <span>Cloud Provider <span class="required">*</span></span></label>\n\
        <label for="field2"><select id="-categories" name="cloudProviderCat" class="select-field">\n\
        <option value=""></option> \n\
        <option value="Amazon">Amazon Cloud Services</option>\n\
        <option value="Google">Google Cloud Platform</option>\n\
        <option value="Microsoft">Microsoft Azure</option></select>\n\
        </label><label for="field3">\n\
        <span>Region <span class="required">*</span></span>\n\
        <select id="-subcats" class="select-field" name="cloudProviderSubcatat"/></label>\n\
        </select>\n\
        <label for="field4"><span>Access Key <span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="accessKey"/>\n\
        </label><label for="field5"><span>Secret Key<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="secretKey" value=""/>\n\
        </label><label></label></div>');

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
