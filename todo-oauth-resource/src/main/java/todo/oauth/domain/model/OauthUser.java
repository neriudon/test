package todo.oauth.domain.model;

import java.io.Serializable;

/**
 * アノテーション@AuthenticationPrincipalで取得する情報を保持するモデル
 */
public class OauthUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;

	private String userAdditionalValue;

	private String clientAdditionalValue;

	private String clientId;

	public OauthUser(String username, String userAdditionalValue, String clientAdditionalValue, String clientId) {
		this.username = username;
		this.userAdditionalValue = userAdditionalValue;
		this.clientAdditionalValue = clientAdditionalValue;
		this.clientId = clientId;
	}

	public String getUsername() {
		return username;
	}

	public String getUserAdditionalValue() {
		return userAdditionalValue;
	}

	public String getClientAdditionalValue() {
		return clientAdditionalValue;
	}

	public String getClientId() {
		return clientId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUserAdditionalValue(String userAdditionalValue) {
		this.userAdditionalValue = userAdditionalValue;
	}

	public void setClientAdditionalValue(String clientAdditionalValue) {
		this.clientAdditionalValue = clientAdditionalValue;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
