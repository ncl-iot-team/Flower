<%-- 
    Document   : defaultFooter.jsp
    Created on : 14/02/2016, 4:57:29 PM
    Author     : kho01f
--%>
<script>
    $(function(){
    var date = new Date();
    var year = date.getFullYear();
    $('#year').html(year);
    });
</script>
<footer class="text-center">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <p>Copyright © <span id="year"></span> Data61, CSIRO.</p>
            </div>
        </div>
    </div>
</footer>