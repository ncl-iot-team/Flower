/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function createCtrlSettingForm($flowId, $ctrlName, $resource, $measurementTarget) {
    var dialogId = '#' + $ctrlName + '-' + $resource + '-' + $measurementTarget + '-form';
    $.get('getCtrl',
            {ctrlName: $ctrlName, resource: $resource, flowId: $flowId, measurementTarget: $measurementTarget},
    function(ctrl) {
        $('#settingForm').html('<div id="' + dialogId + '"><form action="saveCtrlSettings" method="post" ><div class="form-style-2"> \n\
     <div class="form-style-2-heading">Controller specific settings</div> \n\
        <label for="field0"><span>Platform <span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.ctrlName" value="' + ctrl.ctrlName + '"/></label>\n\
        <label for="field7"><span>Resource Name<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.resourceName" value="' + ctrl.resourceName + '"/></label>\n\
        <label for="field0"><span>Measurement Target <span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.measurementTarget" value="' + ctrl.measurementTarget + '"/></label>\n\
        <label for="field0"><span>Monitoring Period <span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.monitoringPeriod" value="' + ctrl.monitoringPeriod + '"/ style="width:50px"></label>\n\
        <label for="field0"><span>Reference Value<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.refValue" value="' + ctrl.refValue + '"/ style="width:50px"></label>\n\
        <label for="field0"><span>Backoff Number<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.backoffNo" value="' + ctrl.backoffNo + '"/ style="width:50px"></label>\n\
        <div class="form-style-2-heading">Controller internal settings</div> \n\
        <label for="field0"><span>upperK0<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.upperK0" value="' + ctrl.upperK0 + '"/ style="width:50px"></label>\n\
        <label for="field0"><span>upInitK0<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.upInitK0" value="' + ctrl.upInitK0 + '"/ style="width:50px"></label>\n\
        <label for="field0"><span>lowInitK0<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.lowInitK0" value="' + ctrl.lowInitK0 + '"/ style="width:50px"></label>\n\
        <label for="field0"><span>lowerK0<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.lowerK0" value="' + ctrl.lowerK0 + '"/ style="width:50px"></label>\n\
        <label for="field0"><span>k_init<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.k_init" value="' + ctrl.k_init + '"/ style="width:50px"></label>\n\
        <label for="field0"><span>gamma<span class="required">*</span></span>\n\
        <input type="text" class="input-field" name="ctrl.gamma" value="' + ctrl.gamma + '"/ style="width:50px"></label>\n\
        </div></form></div>');
    });
    return dialogId;
}
