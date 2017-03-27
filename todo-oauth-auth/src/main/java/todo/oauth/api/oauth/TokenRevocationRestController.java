package todo.oauth.api.oauth;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import todo.oauth.domain.service.token.RevokeTokenService;
/**
 * トークンの取り消しをするコントローラ
 */
@RestController
@RequestMapping("oauth")
public class TokenRevocationRestController {

	@Inject
	RevokeTokenService revokeTokenService;

	// /oauth/tokens/revoke" へのアクセスに対するメソッドとしてマッピング
	// ここで指定するパスはBasic認証の適用とCSRF対策機能の無効化をする
	@RequestMapping(value = "tokens/revoke", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public String revoke(@RequestParam("token") String token, @AuthenticationPrincipal UserDetails user) {
		// Basic認証で生成された認証情報からトークンの取り消し時のチェックで使用するクライアントIDを取得
		String clientId = user.getUsername();
		// RevokeTokenService を呼び出し、トークンを取り消す
		String result = revokeTokenService.revokeToken(token, clientId);
		return result;
	}
}