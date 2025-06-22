<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    
<jsp:include page="components/header.jsp">
    <jsp:param name="pageTitle" value="Accueil"/>
</jsp:include>

<body>
    <jsp:include page="components/navbar.jsp"></jsp:include>
    <div id="page-content">
        <div class="container">
            Bienvenue sur HugoseCar, la meilleure plateforme de covoiturage au monde !
        </div>
    </div>
</body>
</html>
