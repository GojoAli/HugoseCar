<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    
<jsp:include page="components/header.jsp">
    <jsp:param name="pageTitle" value="Accueil"/>
</jsp:include>

<body>
    <jsp:include page="components/navbar.jsp"></jsp:include>
    <div>

    </div>
    <h1>HugoseCar</h1>
    <p><a href="login.jsp">Se connecter</a></p>
    <p><a href="sessionPage.jsp">Accéder à une page sécurisée</a></p>
    <p><a href="register.jsp">S'inscrire sur le site</a></p>
</body>
</html>
