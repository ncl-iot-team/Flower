<%-- 
    Document   : navBar
    Created on : 14/02/2016, 7:11:35 PM
    Author     : kho01f
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
    $(function() {
        $("#logoutLink").on('click', function() {
            $('#logoutForm').submit();
        });
    });
</script>
<nav class="navbar navbar-default">
    <div class="container-fluid"> 
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false"> <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
            <img src="${pageContext.request.contextPath}/resources/img/flower.png" width="45" height="45"></div>
        <div class="navbar-header"> <a href="/Flower"> 
                <img src="${pageContext.request.contextPath}/resources/img/flower_icon.png" width="110" height="40"></a></div>


        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="active"> </li>
            </ul>
            <!--      <form class="navbar-form navbar-right" role="search">
                    <div class="form-group">
                      <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                  </form>-->
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <c:if test="${pageContext.request.remoteUser != null}">
                        <a id="logoutLink" href="#">Logout</a>
                    </c:if>
                    <c:if test="${pageContext.request.userPrincipal.name == null}">
                        <a href="${pageContext.request.contextPath}/user/login"> Sign In</a>
                    </c:if>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse --> 
    </div>
    <!-- /.container-fluid --> 
</nav>

<form action="${pageContext.request.contextPath}/j_spring_security_logout" method="post" id="logoutForm">
</form>