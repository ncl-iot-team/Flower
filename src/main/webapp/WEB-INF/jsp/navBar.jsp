<%-- 
    Document   : navBar
    Created on : 14/02/2016, 7:11:35 PM
    Author     : kho01f
--%>

<nav class="navbar navbar-default">
  <div class="container-fluid"> 
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false"> <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
      <img src="${pageContext.request.contextPath}/resources/img/flower.png" width="45" height="45"></div>
  <div class="navbar-header"> <img src="${pageContext.request.contextPath}/resources/img/flower_icon.png" width="110" height="40"></div>
    
  <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li class="active"> </li>
      </ul>
      <form class="navbar-form navbar-right" role="search">
        <div class="form-group">
          <input type="text" class="form-control" placeholder="Search">
        </div>
        <button type="submit" class="btn btn-default">Submit</button>
      </form>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#">Sign in</a> </li>
        <li class="dropdown"> <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false" aria-haspopup="true">My Account <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#">Console</a> </li>
            <li><a href="#">Account Settings</a> </li>
            <li><a href="#">Security Credentials</a> </li>


          </ul>
        </li>
      </ul>
    </div>
    <!-- /.navbar-collapse --> 
  </div>
  <!-- /.container-fluid --> 
</nav>
