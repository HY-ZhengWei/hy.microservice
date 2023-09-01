<!DOCTYPE html>
<html>
<head>
	<title>Hello world</title>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    
    <script type='text/javascript' charset='utf-8' src='../jquery/jquery.min.js' ></script>
    <script type='text/javascript' charset='utf-8' src='../jquery/jquery-ui.min.js' ></script>
    
    <link rel="stylesheet" href="../jquery/jquery-ui.min.css" />
</head>
<body>
Hello world（<a id="deomAjax" href="#" class='ui-button ui-widget ui-corner-all'>demo04_Ajax</a>）

<script type='text/javascript' charset='utf-8'>

$("#deomAjax").click(function()
{
	$.ajax({
        type: 'get',
        url: 'test04_Ajax?userName=${data.userName}',
        success: function(data) 
        {
            alert(data.datas);
        }
    });
});

</script>
</body>
</html>