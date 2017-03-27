<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>OAuth Error!</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
	<div id="wrapper">
		<h1>OAuth Error!</h1>
		<p>${f:h(error.OAuth2ErrorCode)}</p>
		<p>${f:h(error.message)}</p>
		<br>
	</div>
</body>
</html>