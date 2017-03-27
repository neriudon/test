package todo.oauth.domain.service.token;

/**
 * トークンの取り消しを行うサービスクラスのインタフェース
 */
public interface RevokeTokenClientService {
	/**
	 * トークンを取り消す
	 */
	String revokeToken();
}
