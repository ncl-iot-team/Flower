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
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
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
            }
            img.icon{
                width:30px; height:30px;
            }

        </style>
        <style>

            div.play {
                color: #ddd;
                width: 100px;
                height: 40px;
                text-align: center;
                line-height: 38px;
                font-size: 14px;
                cursor: pointer;
                position: relative;
                background-color: #3498db;  
                -webkit-border-radius: 50%;
                -moz-border-radius: 50%;
                border-radius: 5%;
                -webkit-box-shadow: inset 0 0 0 1px #ddd, inset 0 0 0 3px #fff,inset 0 0 0 4px #ddd;
                box-shadow: inset 0 0 0 1px #ddd, inset 0 0 0 3px #fff,inset 0 0 0 4px #ddd;
                -webkit-transition: all .2s ease;
                transition: all .2s ease;
                /*margin: 0 auto;*/ 

            }

            div.play:hover,
            div.play.active {
                -webkit-box-shadow: inset 0 0 0 0 #CD391F, inset 0 0 0 0 #fff,inset 0 0 0 0px #cd3920;
                -moz-box-shadow: inset 0 0 0 0 #CD391F, inset 0 0 0 0 #fff,inset 0 0 0 0px #cd3920;
                box-shadow: inset 0 0 0 0 #CD391F, inset 0 0 0 0 #fff,inset 0 0 0 0px #cd3920;
                color: #fff;
                background-color: #CD391F;
            }



            .form-style-ctrl-control{
                width: 200px;
                padding: 20px 12px 10px 20px;
                font: 13px Arial, Helvetica, sans-serif;
                position: relative;
                float: left;
            }
            .form-style-ctrl-stat{
                width: 600px;
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
            .form-style-2-heading{
                font-weight: bold;
                /*font-style: italic;*/
                border-bottom: 1px solid #ddd;
                margin-bottom: 10px;
                font-size: 13px;
                padding-bottom: 3px;
            }
            .form-style-2 label{
                display: block;
                margin: 0px 0px 15px 0px;
            }
            .form-style-2 label > span{
                /*width: 110px;*/
                width: 133px;
                font-weight: normal;
                float: left;
                padding-top: 8px;
                padding-right: 5px;
            }
        </style>
        <script type="text/javascript">
            $(function() {


                var flow = '${flow.platforms}';
                var systems = flow.split(",");
                for (var i = 0; i < systems.length; i++) {
                    $("#accordion").children().last()
                            .after('<h3 class="accordion-header ui-accordion-header \n\
                                    ui-helper-reset ui-state-default ui-accordion-icons \n\
                                    ui-corner-all"><span class="ui-accordion-header-icon ui-icon \n\
                                    ui-icon-triangle-1-e"></span><img class="icon" \n\
                                    src="${pageContext.request.contextPath}/resources/img/'
                                    + systems[i] + '.png" /> ' + systems[i] +
                                    '</h3><div class="ui-accordion-content ui-helper-reset \n\
                                    ui-widget-content ui-corner-bottom"><div class="form-style-ctrl-control">\n\
                                    <div class="form-style-2-heading">Controller Control!</div>\n\
                                    <div class="play" id="btn1">start</div></div><div class="form-style-ctrl-stat">\n\
                                    <div class="form-style-2-heading">Controller Control!</div>\n\
                                    <div class="play" id="btn1">start</div></div><div class="form-style-resource-share">\n\
                                    <div class="form-style-2-heading">Controller Control!</div>\n\
                                    <div class="play" id="btn1">start</div></div><div class="form-style-ctrl-stat">\n\
                                    <div class="form-style-2-heading">Controller Control!</div>\n\
                                    <div class="play" id="btn1">start</div></div></div></div>');
                }

                $('.play').click(function() {
                    var $this = $(this);
                    var id = $this.attr('id').replace(/btn/, '');
                    $this.toggleClass('active');
                    if ($this.hasClass('active')) {
                        $this.text('pause');
                        $('audio[id^="sound"]')[id - 1].play();
                    } else {
                        $this.text('start');
                        $('audio[id^="sound"]')[id - 1].pause();
                    }
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
                                   <h3><strong style="color: #555"></strong>Flow Configuration</h3>
                                   <hr>
                                   <p id="ssman">Say something!
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