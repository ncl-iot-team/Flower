<%-- 
    Document   : flowCtrlServicePage
    Created on : 05/04/2016, 5:41:54 PM
    Author     : kho01f
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html>
    <head>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

        <link href="${pageContext.request.contextPath}/resources/css/stepform.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/epoch.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="https://d3js.org/d3.v3.min.js" charset="utf-8"></script>
        <script src="${pageContext.request.contextPath}/resources/js/epoch.min.js"></script>

        <style>
            .ui-accordion .ui-accordion-header {
                /*text-align: center;*/
                font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
                color:#23527c;
                display: block;
                cursor: pointer;
                position: relative;
                margin: 2px 0 0 0;
                padding: .5em .5em .5em .7em;
                min-height: 0; /* support: IE7 */
                /*                border: 1px #c1e2b3 solid;*/

            }
            .ui-accordion .ui-accordion-icons {
                padding-left: 2.2em;
            }
            .ui-accordion .ui-accordion-icons .ui-accordion-icons {
                padding-left: 2.2em;
            }
            .ui-accordion .ui-accordion-header .ui-accordion-header-icon {
                position: absolute;
                left: .5em;
                top: 50%;
                margin-top: -8px;
            }
            .ui-accordion .ui-accordion-content {
                padding: 1em 2.2em;
                border: 0;
                overflow: auto;
                border: 1px solid #ddd;
            }
            img.icon{
                width:30px; height:30px;
            }

        </style>
        <style>
            div.kill {
                color: #ffffff;
                width: 70px;
                height: 30px;
                text-align: center;
                line-height: 30px;
                font-size: 14px;
                cursor: pointer;
                position: relative;
                background-color: #67B168;  
                -webkit-border-radius: 50%;
                -moz-border-radius: 50%;
                border-radius: 5%;
                -webkit-box-shadow: inset 0 0 0 1px #ddd, inset 0 0 0 3px #fff,inset 0 0 0 4px #ddd;
                box-shadow: inset 0 0 0 1px #ddd, inset 0 0 0 3px #fff,inset 0 0 0 4px #ddd;
                -webkit-transition: all .2s ease;
                transition: all .2s ease;
                margin-right: 5px; 
                float: left;
            }
            div.kill:hover,
            div.kill.active {
                -webkit-box-shadow: inset 0 0 0 0 #CD391F, inset 0 0 0 0 #fff,inset 0 0 0 0px #cd3920;
                -moz-box-shadow: inset 0 0 0 0 #CD391F, inset 0 0 0 0 #fff,inset 0 0 0 0px #cd3920;
                box-shadow: inset 0 0 0 0 #CD391F, inset 0 0 0 0 #fff,inset 0 0 0 0px #cd3920;
                color: #fff;
                background-color: #CD391F;
            }
            div.play {
                color: #ffffff;
                width: 70px;
                height: 30px;
                text-align: center;
                line-height: 30px;
                font-size: 14px;
                cursor: pointer;
                position: relative;
                background-color: #67B168;  
                -webkit-border-radius: 50%;
                -moz-border-radius: 50%;
                border-radius: 5%;
                -webkit-box-shadow: inset 0 0 0 1px #ddd, inset 0 0 0 3px #fff,inset 0 0 0 4px #ddd;
                box-shadow: inset 0 0 0 1px #ddd, inset 0 0 0 3px #fff,inset 0 0 0 4px #ddd;
                -webkit-transition: all .2s ease;
                transition: all .2s ease;
                margin-right: 5px; 
                float: left;
            }

            div.play:hover,
            div.play.active {
                -webkit-box-shadow: inset 0 0 0 0 #CD391F, inset 0 0 0 0 #fff,inset 0 0 0 0px #cd3920;
                -moz-box-shadow: inset 0 0 0 0 #CD391F, inset 0 0 0 0 #fff,inset 0 0 0 0px #cd3920;
                box-shadow: inset 0 0 0 0 #CD391F, inset 0 0 0 0 #fff,inset 0 0 0 0px #cd3920;
                color: #fff;
                background-color: #CD391F;
            }

            .form-style-ctrl-stat{
                width: 980px;
                padding: 20px 12px 10px 20px;
                font: 13px Arial, Helvetica, sans-serif;
                position: relative;
                /*float: left;*/
            }
            .form-style-ctrl-diag{
                width: 780px;
                padding: 20px 12px 10px 20px;
                font: 13px Arial, Helvetica, sans-serif;
                position: relative;
                float: left;
            }
            .form-style-resource-share{
                width: 200px;
                padding: 20px 12px 10px 20px;
                font: 13px Arial, Helvetica, sans-serif;
                position: relative;
                float: left;
            }

        </style>
        <script type="text/javascript">
            $(function() {

                var flow = '${flow.platforms}';
                var systems = flow.split(",");
                for (var i = 0; i < systems.length; i++) {
                    var system = systems[i].replace(' ', '');
                    $("#accordion").children().last()
                            .after('<h3 class="accordion-header ui-accordion-header \n\
                                    ui-helper-reset ui-state-default ui-accordion-icons \n\
                                    ui-corner-all"><span class="ui-accordion-header-icon ui-icon \n\
                                    ui-icon-triangle-1-e"></span><img class="icon" \n\
                                    src="${pageContext.request.contextPath}/resources/img/'
                                    + systems[i] + '.png" /> ' + systems[i] +
                                    '</h3><div class="ui-accordion-content ui-helper-reset \n\
                                    ui-widget-content ui-corner-bottom"><div class="form-style-ctrl-stat">\n\
                                    <div class="form-style-3-heading">Controller Stats</div>\n\
                                     <table id="' + system + 'Tbl"><thead>\n\
                                    <tr><th>Resource Name</th><th>Controller Status</th> \n\
                                     <th>Measurement Target</th><th>Ref. Value</th> <th>Scheduling</th> <th>Backoff No</th> <th>Last update</th>\n\
                                    <th></th> <th></th></tr></thead> \n\
                                        <tbody><tr><td></td> <td></td><td>\n\
                                        </td><td>45</td> \n\
                                        <td></td><td></td>\n\
                                        <td></td><td><div id="' + system + 'Ctrl" class="play" style="text-shadow:none">start</div></td>\n\
                                        <td><input type="radio" name="'+system+'Radio" value=""></td></tr>\n\
                                      </tbody></table>\n\
                                      </div><div class="form-style-resource-share">\n\
                                    <div class="form-style-3-heading">Resource Share</div>\n\
                                   <div id="' + system + 'Pie" class="epoch category20c" style="width: 180px; height: 180px"></div></div>\n\
                                    <div class="form-style-ctrl-diag">\n\
                                    <div class="form-style-3-heading">System Performance Monitoring</div>\n\
                                     <div id="' + system + 'LineChart" class="epoch category30" style="width: 700px; height: 200px"></div>\n\
                                        </div></div></div>');
                }

                $('.play').click(function() {
                    var $this = $(this);
                    $this.toggleClass('active');
                    if ($this.hasClass('active')) {
                        $this.text('pause');
                        var $id = $this.attr('id');
                        $.ajax({
                            type: 'POST',
                            url: runControllerService,
                            data: {ctrlName: $id}
                        });
                    } else {
                        $this.text('start');
                    }
                });
                
                
                $('.kill').click(function() {
                    var $this = $(this);
                    $this.toggleClass('active');
                    if ($this.hasClass('active')) {
                        $this.text('stop');
                        $('.play').off('click');
                    } else {
                        $this.toggleClass('active');
                    }
                });

                function getTime() {
                    return parseInt((new Date).getTime() / 1000);
                }
                var mygraph = $('#ApacheStormLineChart').epoch({
                    type: 'time.line',
                    data: [{label: "S1", values: [{time: getTime(), y: 10}]},
                        {label: "S2", values: [{time: getTime(), y: 10}]}],
                    axes: ['bottom', 'left']
                });
                (function worker() {
                    var times = getTime();
                    var rnd = parseInt(Math.random() * 1000);
                    mygraph.push([{time: times, y: 100 + rnd}, {time: times, y: 350 + rnd}]);
                    setTimeout(worker, 60000);
                })();

                var pieData = [
                    {label: 'Slice 1', value: 10},
                    {label: 'Slice 2', value: 20}
                ];

                $('#ApacheStormPie').epoch({
                    type: 'pie',
                    data: pieData
                });




                var headers = $('#accordion .accordion-header');
                var contentAreas = $('#accordion .ui-accordion-content ').hide();
                var expandLink = $('.accordion-expand-all');
                // add the accordion functionality
                headers.click(function() {
                    var panel = $(this).next();
                    var isOpen = panel.is(':visible');
                    // open or close as necessary
                    panel[isOpen ? 'slideUp' : 'slideDown']()
                            // trigger the correct custom event
                            .trigger(isOpen ? 'hide' : 'show');
                    // stop the link from causing a pagescroll
                    return false;
                });
                // hook up the expand/collapse all
                expandLink.click(function() {
                    var isAllOpen = $(this).data('isAllOpen');
                    contentAreas[isAllOpen ? 'hide' : 'show']()
                            .trigger(isAllOpen ? 'hide' : 'show');
                });
                // when panels open or close, check to see if they're all open
                contentAreas.on({
                    // whenever we open a panel, check to see if they're all open
                    // if all open, swap the button to collapser
                    show: function() {
                        var isAllOpen = !contentAreas.is(':hidden');
                        if (isAllOpen) {
                            expandLink.text('Collapse All')
                                    .data('isAllOpen', true);
                        }
                    },
                    // whenever we close a panel, check to see if they're all open
                    // if not all open, swap the button to expander
                    hide: function() {
                        var isAllOpen = !contentAreas.is(':hidden');
                        if (!isAllOpen) {
                            expandLink.text('Expand all')
                                    .data('isAllOpen', false);
                        }
                    }
                });
            });
        </script>
    </head>

    <body>
        <tiles:insertDefinition name="defaultbar" />

        <div class="col-xs-12">
            <h3><strong style="color: #555">Elasticity Management of <font color="#67B168">${flow.flowName}</font> Flow</strong></h3>
            <hr>
            <p id="ssman">Placeholder
            </p>
            <br>
        </div>


        <div  class="jumbotron_body">
            <!--            <div style="float:left" >
                            <ul id="sortable">
                                <li class="ui-state-default-start" style="color:#fbfbfb; padding: 3px 6.5px 5px 8px">Analytics Flow</li>
            
                            </ul>
                        </div>-->
            <div class="container">

                <div class="row" >

                    <div class="container">

                        <p class="accordion-expand-holder">
                            <a class="accordion-expand-all" href="#">Expand all</a>
                        </p>
                        <div id="accordion" class="ui-accordion ui-widget ui-helper-reset">
                            <span></span>
                        </div>

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