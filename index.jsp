<%
    String path = request.getContextPath();
%>
<html>
<head>
    <script>
        function redirect() {
            window.location.href = '<%=path%>/login';
        }
    </script>
</head>
<body onload="redirect()">
</body>
</html>
