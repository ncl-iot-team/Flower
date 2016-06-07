/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function createCtrlSettingForm($flowId, $ctrlName, $resource, $measurementTarget) {
//    var dialogId = '#' + $ctrlName + '-' + $resource + '-' + $measurementTarget + '-form';
    $.get('getCtrl',
            {ctrlName: $ctrlName, resource: $resource, flowId: $flowId, measurementTarget: $measurementTarget},
    function(ctrl) {
        $('#CtrlInternalSettingsForm').html('<div class="form-style-2"> \n\
         <div class="form-style-2-heading">Controller specific settings</div> \n\
         <label><span>Platform </span>\n\
        <input type="text" class="input-field" name="ctrl.ctrlName" value="' + ctrl.ctrlName + '" style="width:50%" readonly/></label>\n\
        <label><span>Resource Name</span>\n\
        <input type="text" class="input-field" name="ctrl.resourceName" value="' + ctrl.resourceName + '" style="width:50%" readonly/></label>\n\
         <label><span>Measurement Target</span>\n\
        <input type="text" class="input-field" name="ctrl.measurementTarget" value="' + ctrl.measurementTarget + '" style="width:60%" readonly/></label>\n\
         <label><span>Reference Value</span>\n\
        <input type="text" class="input-field" name="ctrl.refValue" value="' + ctrl.refValue + '"/ style="width:65px"></label>\n\
         <label><span>Monitoring Period</span>\n\
        <input type="text" class="input-field" name="ctrl.monitoringPeriod" value="' + ctrl.monitoringPeriod + '"/ style="width:65px"></label>\n\
         <label><span>Backoff Number</span>\n\
        <input type="text" class="input-field" name="ctrl.backoffNo" value="' + ctrl.backoffNo + '"/ style="width:65px"></label>\n\
        <div class="form-style-2-heading">Controller internal settings</div> \n\
         <label><span>upperK0</span>\n\
        <input type="text" class="input-field" name="ctrl.upperK0" value="' + ctrl.upperK0 + '"/ style="width:65px"></label>\n\
         <label><span>upInitK0</span>\n\
        <input type="text" class="input-field" name="ctrl.upInitK0" value="' + ctrl.upInitK0 + '"/ style="width:65px"></label>\n\
         <label><span>lowInitK0</span>\n\
        <input type="text" class="input-field" name="ctrl.lowInitK0" value="' + ctrl.lowInitK0 + '"/ style="width:65px"></label>\n\
         <label><span>lowerK0</span>\n\
        <input type="text" class="input-field" name="ctrl.lowerK0" value="' + ctrl.lowerK0 + '"/ style="width:65px"></label>\n\
         <label><span>k_init</span>\n\
        <input type="text" class="input-field" name="ctrl.k_init" value="' + ctrl.k_init + '"/ style="width:65px"></label>\n\
         <label><span>gamma</span>\n\
        <input type="text" class="input-field" name="ctrl.gamma" value="' + ctrl.gamma + '"/ style="width:65px"></label>\n\
        </div><div style="border-top:1px solid #ccc;padding-top:5px;text-align:right;">\n\
        <input type="submit" class="btn btn-action" value="Save" />\n\
        <input type="button" class="btn btn-action" value="Cancel" /></div>');
    });
}
