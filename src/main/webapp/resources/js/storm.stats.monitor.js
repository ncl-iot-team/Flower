///* 
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//
//
//
//
//$(function() {
//    var topologyMetricMap = {};
//    topologyMetricMap["topologyStats-emitted"] = 'topologyStats-emitted';
//    topologyMetricMap["topologyStats-trasferred"] = 'topologyStats-trasferred';
//    topologyMetricMap["topologyStats-completeLatency"] = 'topologyStats-completeLatency';
//    topologyMetricMap["topologyStats-acked"] = 'topologyStats-acked';
//    topologyMetricMap["topologyStats-failed"] = 'topologyStats-failed';
//    var topologyTimeoutMap = {};
//    $(document).on('deselect', 'input[name=topologyList]', function() {
//        for (var key in topologyMetricMap) {
//            clearTimeout(topologyTimeoutMap[topologyMetricMap[key]]);
//            delete timeoutMap[topologyMetricMap[key]];
////                        $('#' + topologyMetricMap[key] + 'LineChart').remove();
//            $('#topologyStats').children().remove();
//        }
//    }).on('change', 'input[name=topologyList]', function() {
//        var $topologyId = $(this).val();
//        var $timeInterval = 1 * 5000;
//        $.getJSON('../getTopologyStats?flowId=' + $flowId + '&topologyId=' + $topologyId, function(data) {
//            for (var key in topologyMetricMap) {
//                var lineChartDiv = '#' + topologyMetricMap[key] + 'LineChart';
//                $('#topologyStats').append('<div class="form-style-small-box">\n\
//                                     <div class="form-style-3-heading">' + topologyMetricMap[key] + '</div>\n\
//                                     <div id="' + topologyMetricMap[key] + 'LineChart" class="epoch category3" style="width: 100%; height: 200px">\n\
//                                     <div style="z-index: 1"><ul class="legend"><li><span class="used">\n\
//                                     </span>' + topologyMetricMap[key] + '</li></ul></div></div></div>');
//                var lineChart = setupLineChart(lineChartDiv);
//                topologyStatDrawer(lineChart, $flowId, $topologyId, topologyMetricMap[key], $timeInterval);
//            }
//            $('#spouts').append('<div class="form-style-large-box" style="width:700px">\n\
//                                    <table id="spoutTbl"> \n\
//                                     <thead>\n\
//                                     <tr><th>Spout id</th><th>Executors</th> \n\
//                                     <th>Tasks</th>\n\
//                                     <th></th></tr></thead><tbody> </tbody></table></div>');
//            $('#bolts').append('<div class="form-style-large-box" style="width:700px">\n\
//                                     <table id="boltTbl"> \n\
//                                     <thead>\n\
//                                     <tr><th>Bolt id</th><th>Executors</th> \n\
//                                     <th>Tasks</th>\n\
//                                     <th></th></tr></thead><tbody> </tbody></table></div>');
//            $.each(data.spouts, function(i, spout) {
//                $('#spoutTbl tr:last').after('<tr><td>' + spout.spoutId + '</td>\n\
//                                      <td>' + spout.executors + '</td>\n\
//                                       <td>' + spout.tasks + '</td>\n\
//                                        <td><input type="radio" name="spouts" value="' + spout.spoutId + '">\n\
//                                         <input type="radio" name="spouts" style="display:none""></td>\n\
//                                          </tr>');
//            });
//            $.each(data.bolts, function(i, bolt) {
//                $('#boltTbl tr:last').after('<tr><td>' + bolt.boltId + '</td>\n\
//                                      <td>' + bolt.executors + '</td>\n\
//                                       <td>' + bolt.tasks + '</td>\n\
//                                        <td><input type="radio" name="bolts" value="' + bolt.boltId + '">\n\
//                                         <input type="radio" name="bolts" style="display:none""></td>\n\
//                                          </tr>');
//            });
//        });
//    });
//
//    function setupLineChart(chartDiv) {
//        var graph = $(chartDiv).epoch({
//            type: 'time.line',
//            data: [{label: "metric", values: [{time: getTimeStampSec((new Date()).getTime()), y: 0}]}],
//            axes: ['bottom', 'left', 'right']
//        });
//        return graph;
//    }
//});