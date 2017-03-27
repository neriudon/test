package todo.oauth.app.oauth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 認可時のエラー画面を表示するビューコントローラ
 */
@Controller
public class OAuth2ErrorController {

	/**
	 * 認可エラーの場合OAuth2Exceptionがthrowされ、リクエストは(コンテキストパス)/oauth/errorにフォワードされるので<br/>
	 * 例外をハンドリングをするために/oauth/errorにマッピングする
	 */
	@RequestMapping("/oauth/error")
	public String handleError() {
		return "common/error/oauthError";
	}

}
