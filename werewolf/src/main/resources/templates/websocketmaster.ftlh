<#macro master>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is  -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <title>Werewolf</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/werewolf.css" rel="stylesheet">

    <!-- Websocket Support -->
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    <script src="/webjars/jquery/jquery.min.js"></script>

    <script src="/js/bootstrap.min.js"></script>
    <script src="/js/app.js"></script>
</head>
<body class="container">
<noscript><h2 style="color: #ff0000">Javascript is required to view this webpage, please enable Javascript and refresh the page</h2></noscript>
    <#nested/>
</body>
</html>
</#macro>