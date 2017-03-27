package todo.oauth.domain.service.token;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
/**
 * トークンの取り消しを行うサービスクラス
 */
@Service
public class RevokeTokenClientServiceImpl implements RevokeTokenClientService {

	// アクセストークンの取り消しを認可サーバに依頼する際に使用するURL
	@Value("${auth.serverUrl}/api/v1/oauth/tokens/revoke")
	String revokeTokenUrl;

	// 取り消しを行うアクセストークンを保持している OAuth2RestTemplate
	@Inject
	@Named("todoAuthCodeGrantResourceRestTemplate")
	OAuth2RestOperations oauth2RestOperations;

	// アクセストークンの取り消しを行う RestTemplate
	@Inject
	@Named("revokeRestTemplate")
	RestOperations revokeRestOperations;

	@Override
	public String revokeToken() {

		String token = getTokenValue(oauth2RestOperations);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> variables = new LinkedMultiValueMap<String, String>();
		variables.add("token", token);

		// 認可サーバにアクセストークンの取り消しを行うために、RESTでメソッドPOSTでアクセス
		String result = revokeRestOperations.postForObject(revokeTokenUrl,
				new HttpEntity<MultiValueMap<String, String>>(variables, headers), String.class);
		// 認可サーバの処理結果を判定し、正常の場合のみ OAuth2RestOperations で保持しているアクセストークンを削除
		if ("success".equals(result)) {
			initContextToken(oauth2RestOperations);
		}
		return result;
	}

	/**
	 * OAuth2RestOperations で保持しているアクセストークンを取得する 
	 */
	private String getTokenValue(OAuth2RestOperations oauth2RestOperations) {
		String tokenValue = "";
		OAuth2AccessToken token = oauth2RestOperations.getAccessToken();
		if (token != null) {
			tokenValue = token.getValue();
		}
		return tokenValue;
	}

	/**
	 *   OAuth2RestOperations で保持しているアクセストークンを削除(nullで上書き)する
	 */
	private void initContextToken(OAuth2RestOperations oauth2RestOperations) {
		oauth2RestOperations.getOAuth2ClientContext().setAccessToken(null);
	}
}