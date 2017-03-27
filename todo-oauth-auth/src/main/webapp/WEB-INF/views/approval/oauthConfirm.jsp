<!DOCTYPE html>
<html>
<head>
<title>認可画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
	<div id="wrapper">
		<h1>認可するスコープを選択</h1>
		<p>Do you authorize '${f:h(authorizationRequest.clientId)}' to
			access your protected resources?</p>
		<form id='confirmationForm' name='confirmationForm'
			action='${pageContext.request.contextPath}/oauth/authorize'
			method='post'>
			<c:forEach var="scope" items="${scopes}" varStatus="status">
				<li>${f:h(scope.key)}
					<input type='radio' name="${f:h(scope.key)}" value='true' />Approve
					<input type='radio' name="${f:h(scope.key)}" value='false' />Deny
				</li>
			</c:forEach>
			<input name='user_oauth_approval' value='true' type='hidden' />
			<sec:csrfInput />
			<label> <input name='authorize' value='Authorize'
				type='submit' />
			</label>
		</form>
	</div>
	<div id="wrapper">
		<p>
			認可するユーザを切り替える場合はこちら
			<form:form action="${pageContext.request.contextPath}/logout">
				<button type="submit">Logout</button>
			</form:form>
		</p>
	</div>
</body>
</html>