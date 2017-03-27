package todo.oauth.converter;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

public class CustomUserTokenConverter extends DefaultUserAuthenticationConverter {
	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put(USERNAME, authentication.getName());

		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
		}
		// リソースサーバに引き渡す情報を独自項目user_additional_keyとして定義
		response.put("user_additional_key", "user_additional_value");
		// チェックトークンエンドポイントのトークン検証時にレスポンスBODYとしてJSON形式でリソースサーバへ返却
		return response;
	}
}