/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function createDynamoCtrlForm(divLoc) {
    var divContainer = $(divLoc);
    divContainer.html('<div class="form-style-2"> \n\
        <div class="form-style-2-heading">DynamoDB controller setting</div> \n\
        <input type="button" value="Load Tables" class="btn btn-default" style="margin-bottom:10px; height:30px; font-size:12px;"/>\n\
        <table><thead>\n\
        <tr><th>Table Name</th><th>Measurement Target <span class="required">*</span></th> <th>Reference Value <span class="required">*</span></th><th>Monitoring Period</th> <th>Backoff</th> <th></th></tr></thead> <tbody>\n\
         <tr> <td><input type="text" style="border: 0px;background:#fafafa;text-align:center" name="dynamoCtrl.tableName" readonly=true value="Sample"></td><td>\n\
        <select id="dynamoCat" name="dynamoCtrl.measurementTarget" class="select-field">\n\
        <option value=""></option> \n\
        <option value="Write">Write Capacity</option>\n\
        </td><td> <input type="text" class="input-field" name="dynamoCtrl.refValue" value=""/></td> \n\
            <td><input type="text" class="input-field" name="dynamoCtrl.monitoringPeriod" value=""/>\n\
            </td><td><input type="text" class="input-field" name="dynamoCtrl.backoffNo" value=""/></td><td><img class="bin"/></td></tr>\n\
        </tbody></table>');
}

function createStormCtrlForm(divLoc) {
    var divContainer = $(divLoc);
    divContainer.html('<div class="form-style-2"> \n\
     <div class="form-style-2-heading">Storm cluster settings</div> \n\
        <label for="field0"><span>Nimbus IP <span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="stormCluster.nimbusIp"/>\n\
        </label><label for="field7"><span>Zookeeper Endpoint<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="stormCluster.zookeeperEndpoint" value=""/> </label>\n\
        <div class="form-style-2-heading">Storm cntroller settings</div> \n\
        <label for="field1">\n\
        <span>Measurement Target <span class="required">*</span></span></label>\n\
        <label for="field2"><select id="stormCat" name="stormCtrl.measurementTarget" class="select-field">\n\
        <option value=""></option> \n\
        <option value="CPU">CPU</option>\n\
        <option value="Memory">Memory</option>\n\</select>\n\
        </label><label for="field3">\n\
        <span>Target Topology <span class="required">*</span></span>\n\
        <input type="text" id="topology" class="select-field" name="stormCtrl.targetTopology" \>\n\
        </label>\n\
        <label for="field4"><span>Reference Value <span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="stormCtrl.refValue" style="width:50px"/>\n\
        </label><label for="field5"><span>Monitoring Period</span>\n\
        <input type="text" class="input-field" name="stormCtrl.monitoringPeriod" value="" style="width:50px"/>\n\
        </label><label for="field6"><span>Backoff No.</span>\n\
        <input type="text" class="input-field" name="stormCtrl.backoffNo" value="" style="width:50px"/>\n\
        </label><label></label></div>');
}

function createKinesisCtrlForm(divLoc) {
    var divContainer = $(divLoc);
    divContainer.html('<div class="form-style-2"> \n\
        <div class="form-style-2-heading">Kinesis controller setting</div> \n\
        <input type="button" value="Load Streams" class="btn btn-default" style="margin-bottom:10px; height:30px; font-size:12px;"/>\n\
        <br/><table><thead>\n\
        <tr><th>Stream Name</th><th>Measurement Target <span class="required">*</span></th> <th>Reference Value <span class="required">*</span></th><th>Monitoring Period</th> <th>Backoff</th> <th></th></tr></thead> <tbody>\n\
         <tr> <td><input type="text" style="border: 0px;background:#fafafa;text-align:center" name="kinesisCtrl.streamName" readonly=true value="Sample"></td><td>\n\
        <select id="kinesisCat" name="kinesisCtrl.measurementTarget" class="select-field">\n\
        <option value=""></option> \n\
        <option value="IncRecord">Incoming Records</option>\n\
        </td><td> <input type="text" class="input-field" name="kinesisCtrl.refValue" value=""/></td> \n\
            <td><input type="text" class="input-field" name="kinesisCtrl.monitoringPeriod" value=""/>\n\
            </td><td><input type="text" class="input-field" name="kinesisCtrl.backoffNo" value=""/></td><td><img class="bin"/></td></tr>\n\
        </tbody></table>');
}


function createCloudSettingForm(divLoc) {
    var divContainer = $(divLoc);
    divContainer.html('<div class="form-style-2"> \n\
        <div class="form-style-2-heading">Cloud hosting information</div> \n\
        <label for="field1">\n\
        <span>Cloud Provider <span class="required">*</span></span></label>\n\
        <label for="field2"><select id="-categories" name="cloudSetting.cloudProvider" class="select-field">\n\
        <option value=""></option> \n\
        <option value="Amazon">Amazon Cloud Services</option>\n\
        <option value="Google">Google Cloud Platform</option>\n\
        <option value="Microsoft">Microsoft Azure</option></select>\n\
        </label><label for="field3">\n\
        <span>Region <span class="required">*</span></span>\n\
        <select id="-subcats" class="select-field" name="cloudSetting.region"> </select></label>\n\
        <label for="field4"><span>Access Key <span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="cloudSetting.accessKey"/>\n\
        </label><label for="field5"><span>Secret Key<span class="required">*</span></span>\n\
        <input type="password" class="input-field" name="cloudSetting.secretKey" value=""/>\n\
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
