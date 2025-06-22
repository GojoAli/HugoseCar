<%
    String pageTitle = request.getParameter("pageTitle");
    if (pageTitle == null) pageTitle = "Sans titre";
%>

<head>
    <meta charset="UTF-8">
    <title>HugoseCar - <%= pageTitle %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>