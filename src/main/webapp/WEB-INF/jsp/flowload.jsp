<%-- 
    Document   : flowload
    Created on : 14/02/2016, 4:30:36 PM
    Author     : kho01f
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<html><body>
        <tiles:insertDefinition name="defaultbar" />
        <div class="col-xs-12">
            <h3><strong style="color: #555">Load the Analytics Flow</strong></h3>
            <hr>
            <p>Select the template that describes the data analytics flow that you want to create. A flow describes a group of related platforms and resources that you manage as a single unit.
            </p>
            <br>
        </div>
        <div  class="jumbotron1">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12 col-md-9 col-lg-5">
                        <input type="file" id="fileinput" />
                    </div>
                    <div class=" text-center col-sm-6 col-sm-offset-3 col-md-3 col-xs-offset-4 col-xs-5 col-lg-offset-0 col-lg-2">
                        <a class="btn btn-action" href="#" title="">Load Template</a> 
                    </div>
                </div>
            </div>
        </div>
        
        <div  class="jumbotron_body">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12 col-md-9 col-lg-5">

                    </div>
                </div>
            </div>
        </div>

        <div class="jumbotron2">
            <div class="container">
                <div class="row">
                    <div class="col-xs-12 col-md-9 col-lg-5">
                    </div>
                    <div class=" text-center col-sm-6 col-sm-offset-3 col-md-3 col-xs-offset-4 col-xs-5 col-lg-offset-0 col-lg-2">
                        <a class="btn btn-action" href="#" title="">Cancel</a> 

                        <a class="btn btn-action" href="#" title="">Next</a> 

                    </div>

                </div>
            </div>

        </div>

        <tiles:insertDefinition name="defaultfooter" />
    </body>
</html>
