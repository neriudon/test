package todo.oauth.converter;

import java.util.Map;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

	@Override
	public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {

		@SuppressWarnings("unchecked")
		Map<String, Object> response = (Map<String, Object>) super.convertAccessToken(token, authentication);
		// リソースサーバに引き渡す情報を独自項目client_additional_keyとして定義
		response.put("client_additional_key", "client_additional_value");
		// チェックトークンエンドポイントのトークン検証時にレスポンスBODYとしてJSON形式でリソースサーバへ返却
		return response;
	}
}