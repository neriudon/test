<!-- OAuth 2.0機能を独自に実装したjsファイル、jQueryをそれぞれ格納したパスを指定 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/vendor/jquery/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/app/js/oauth2.js"></script>
<script type="text/javascript">
"use strict";

$(document).ready(function() {
    var result = oauth2Func.initialize({ // 認可要求に使用するコンフィギュレーション情報を定義し、初期化
        "todo" : { // クライアント別にコンフィギュレーション情報を区別するための識別子として一意な値を指定。本項目をキーにコンフィギュレーション情報を管理・取得する
            clientId : "${clientId}", // クライアントを識別するID
            redirectUrl : "${clientServerUrl}/todo/implicit/list", // 認可サーバのリソースオーナ認証後にクライアントをリダイレクトさせるURL
            errRedirectUrl : "${clientServerUrl}/oauth/error", // 認可応答として認可サーバよりエラーを受信した場合にリダイレクトさせるURL
            authorization : "${authServerUrl}/oauth/authorize" // 認可サーバの認可エンドポイントURL
        }
    });

    if (result) {
        oauth2Func.oajax({ // リソースへのアクセス
            url : "${resourceServerUrl}/api/v1/todos", // リソースサーバのアクセス先URL 
            providerId : "todo",  // 参照するコンフィギュレーション情報の識別子 
            scopes : [ "READ" ],  // リソースへ要求するスコープ 
            dataType : "json",    // レスポンスの型
            type : "GET",  // メソッドGETでリソースサーバへアクセス
            success : function(data) { // 処理成功時に行う処理を指定
                $("#message").text(JSON.stringify(data));
            },
            error : function() {
                oauth2Func.clearTokens();
            }
        });
    } else {
        oauth2Func.clearTokens(); // アクセストークンをクリア 
    }


};

</script>
<div id="wrapper">
    <h1 id="title">implicit grant</h1>
    <p id="message"></p>
</div>