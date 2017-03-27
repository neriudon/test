var oauth2Func = (function(exp, $) {
    "use strict";

    var
        config = {},
        DEFAULT_LIFETIME = 3600;

    var uuid = function() {
        return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function(c) {
            var r = Math.random()*16|0, v = c == "x" ? r : (r&0x3|0x8);
            return v.toString(16);
        });
    };

    var encodeURL= function(url, params) {
        var res = url;
        var k, i = 0;
        for(k in params) {
            res += (i++ === 0 ? "?" : "&") + encodeURIComponent(k) + "=" + encodeURIComponent(params[k]);
        }
        return res;
    };

    var epoch = function() {
        return Math.round(new Date().getTime()/1000.0);
    };

    var parseQueryString = function (qs) {
        var e,
            a = /\+/g,
            r = /([^&;=]+)=?([^&;]*)/g,
            d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
            q = qs,
            urlParams = {};

        while (e = r.exec(q)) {
           urlParams[d(e[1])] = d(e[2]);
        }

        return urlParams;
    };

    var saveState = function(state, obj) {
        localStorage.setItem("state-" + state, JSON.stringify(obj));
    };

    var getState = function(state) {
        var obj = JSON.parse(localStorage.getItem("state-" + state));
        localStorage.removeItem("state-" + state);
        return obj;
    };

    var hasScope = function(token, scope) {
        if (!token.scopes) return false;
        var i;
        for(i = 0; i < token.scopes.length; i++) {
            if (token.scopes[i] === scope) return true;
        }
        return false;
    };
    // アクセストークンの有効期限とスコープの可否をチェックする関数
    // クライントが使用可能なアクセストークンを返却
    var filterTokens = function(tokens, scopes) {
        if (!scopes) scopes = [];

        var i, j,
        result = [],
        now = epoch(),
        usethis;
        for(i = 0; i < tokens.length; i++) {
            usethis = true;

            if (tokens[i].expires && tokens[i].expires < (now+1)) usethis = false;

            for(j = 0; j < scopes.length; j++) {
                if (!hasScope(tokens[i], scopes[j])) usethis = false;
            }

            if (usethis) result.push(tokens[i]);
        }
        return result;
    };

    var saveTokens = function(provider, tokens) {
        localStorage.setItem("tokens-" + provider, JSON.stringify(tokens));
    };

    var getTokens = function(provider) {
        var tokens = JSON.parse(localStorage.getItem("tokens-" + provider));
        if (!tokens) tokens = [];

        return tokens;
    };

    var wipeTokens = function(provider) {
        localStorage.removeItem("tokens-" + provider);
    };

    var saveToken = function(provider, token) {
        var tokens = getTokens(provider);
        tokens = filterTokens(tokens);
        tokens.push(token);
        saveTokens(provider, tokens);
    };

    var getToken = function(provider, scopes) {
        var tokens = getTokens(provider);
        tokens = filterTokens(tokens, scopes);
        if (tokens.length < 1) return null;
        return tokens[0];
    };
    // 認可サーバに対して認可を要求する関数 
    // コンフィギュレーション情報より必要パラメータを取得し、リクエストを作成
    var sendAuthRequest = function(providerId, scopes) {
        if (!config[providerId]) throw "Could not find configuration for provider " + providerId;
        var co = config[providerId];

        var state = uuid();
        var request = {
            "response_type": "token"
        };
        request.state = state;

        if (co["redirectUrl"]) {
            request["redirect_uri"] = co["redirectUrl"];
        }
        if (co["clientId"]) {
            request["client_id"] = co["clientId"];
        }
        if (scopes) {
            request["scope"] = scopes.join(" ");
        }

        var authurl = encodeURL(co.authorization, request);

        if (window.location.hash) {
            request["restoreHash"] = window.location.hash;
        }
        request["providerId"] = providerId;
        if (scopes) {
            request["scopes"] = scopes;
        }

        saveState(state, request);
        redirect(authurl);

    };

    // 認可の応答よりアクセストークンを取得する関数
    // アクセストークンが取得できた場合、ローカルストレージに情報を格納
    var checkForToken = function(providerId) {
        var h = window.location.hash;

        if (h.length < 2) return true;

        // 認可の結果、エラーが返却された場合エラー処理としてhandleErrorを呼び出す
        if (h.indexOf("error") > 0) {
            h = h.substring(1);
            var errorinfo = parseQueryString(h);
            handleError(providerId, errorinfo);
            return false;
        }

        if (h.indexOf("access_token") === -1) {
            return true;
        }
        h = h.substring(1);
        var atoken = parseQueryString(h);

        if (!atoken.state) {
            return true;
        }

        var state = getState(atoken.state);
        if (!state) throw "Could not retrieve state";
        if (!state.providerId) throw "Could not get providerId from state";
        if (!config[state.providerId]) throw "Could not retrieve config for this provider.";

        var now = epoch();
        if (atoken["expires_in"]) {
            atoken["expires"] = now + parseInt(atoken["expires_in"]);
        } else {
            atoken["expires"] = now + DEFAULT_LIFETIME;
        }

        if (atoken["scope"]) {
            atoken["scopes"] = atoken["scope"].split(" ");
        } else if (state["scopes"]) {
            atoken["scopes"] = state["scopes"];
        }

        saveToken(state.providerId, atoken);

        if (state.restoreHash) {
            window.location.hash = state.restoreHash;
        } else {
            window.location.hash = "";
        }
        return true;
    };
    // 認可時のエラーを処理する関数
    var handleError = function(providerId, cause) {
        if (!config[providerId]) throw "Could not retrieve config for this provider.";

        var co = config[providerId];
        var errorDetail = cause["error"];

        // コンフィギュレーション情報にて指定されている エラー時のリダイレクト先URLへのリダイレクト
        if(co["errRedirectUrl"]) {
            redirect(co["errRedirectUrl"] + "/" + errorDetail);
        } else {
            alert("Access Error. cause: " + errorDetail);
        }
    };


    var redirect = function(url) {
        window.location = url;
    };

    var initialize = function(c) {
        config = c;
        try {
            var key, providerId;
            for(key in c) {
                    providerId = key;
            }
            return checkForToken(providerId);
        } catch(e) {
            console.log("Error when retrieving token from hash: " + e);
            window.location.hash = "";
            return false;
        }
    };

    var clearTokens = function() {
        var key;
        for(key in config) {
            wipeTokens(key);
        }
    };

    // リソースサーバに対してリソースへのアクセスを要求する関数
    var oajax = function(settings) {
        var providerId = settings.providerId;
        var scopes = settings.scopes;
        // ローカルストレージよりアクセストークンを取得
        var token = getToken(providerId, scopes);

        if (!token) {
            sendAuthRequest(providerId, scopes);
            return;
        }

        if (!settings.headers) settings.headers = {};
        settings.headers["Authorization"] = "Bearer " + token["access_token"];
        // jQueryのajax関数を用いてリソースサーバへリクエスト
        $.ajax(settings);
    };

    return {
        initialize: function(config) {
            return initialize(config);
        },
        clearTokens: function() {
            return clearTokens();
        },
        oajax: function(settings) {
            return oajax(settings);
        }
    };

})(window, jQuery);