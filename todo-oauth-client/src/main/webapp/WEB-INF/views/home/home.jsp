<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>クライアント</title>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/resources/app/css/styles.css">
</head>
<body>
    <div id="wrapper">
        <h1>クライアントトップページ</h1>
    </div>
    <div id="wrapper">
        <h2>認可グラントを選択</h2>
        <p>
            <a href="./todo/authcode/list">認可コードグラント</a>
        </p>
        <p>
            <a href="./todo/implicit/list">インプリシットグラント</a>
        </p>
        <p>
            <a href="./todo/password/list">リソースオーナクレデンシャルグラント</a>
        </p>
        <p>
            <a href="./todo/client/list">クライアントグラント</a>
        </p>
    </div>
    <div id="wrapper">
        <p>
            <form:form action="${pageContext.request.contextPath}/logout">
                <button type="submit">Logout</button>
            </form:form>
        </p>
    </div>
</body>
</html>
