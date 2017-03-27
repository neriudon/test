package todo.oauth.domain.service.token;
/**
 * トークンを取り消すサービスインターフェイス
 */
public interface RevokeTokenService {
	String revokeToken(String tokenValue, String clientId);
}
