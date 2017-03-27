package todo.oauth.converter;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import todo.oauth.domain.model.OauthUser;

public class CustomUserTokenConverter extends DefaultUserAuthenticationConverter {

	private Collection<? extends GrantedAuthority> defaultAuthorities;

	// getAuthoritiesメソッドで使用されるdefaultAuthoritiesとsetAuthoritiesメソッドを実装
	public void setDefaultAuthorities(String[] defaultAuthorities) {
		this.defaultAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(StringUtils.arrayToCommaDelimitedString(defaultAuthorities));
	}

	/**
	 * 認可サーバから連携された情報から認証情報を抽出するメソッド
	 */
	@Override
	public Authentication extractAuthentication(Map<String, ?> map) {
		if (map.containsKey(USERNAME)) {
			Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
			// 認可サーバから連携された情報をOauthUserクラスに設定
			OauthUser user = new OauthUser((String) map.get(USERNAME), (String) map.get("user_additional_key"),
					(String) map.get("client_additional_key"), (String) map.get("client_id"));

			// 認可サーバから連携された情報をアノテーション@AuthenticationPrincipalで取得できるようにする
			return new UsernamePasswordAuthenticationToken(user, "N/A", authorities);
		}
		return null;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
		if (!map.containsKey(AUTHORITIES)) {
			return defaultAuthorities;
		}
		Object authorities = map.get(AUTHORITIES);
		if (authorities instanceof String) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
		}
		if (authorities instanceof Collection) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList(
					StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
		}
		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
	}
}
