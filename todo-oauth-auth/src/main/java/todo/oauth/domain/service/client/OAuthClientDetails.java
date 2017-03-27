package todo.oauth.domain.service.client;

import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import todo.oauth.domain.model.Client;
/**
 * BaseClientDetailsはOAuth 2.0を利用する上での基本的なパラメータを提供しており<br/>
 * 拡張することで独自のパラメータを追加することも可能<br/>
 * ここではBaseClientDetailsを拡張子clientNameを持つClientをパラメータに持つ
 */
public class OAuthClientDetails extends BaseClientDetails {

	private static final long serialVersionUID = 1L;
	
	private Client client;

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
