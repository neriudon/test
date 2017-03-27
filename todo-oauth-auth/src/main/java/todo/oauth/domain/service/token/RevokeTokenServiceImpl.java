package todo.oauth.domain.service.token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * トークンを取り消すサービスクラス
 */
@Service
@Transactional
public class RevokeTokenServiceImpl implements RevokeTokenService {

	// アクセストークンの取り消しを行うインタフェース ConsumerTokenService の実装クラスン
	@Inject
	ConsumerTokenServices consumerService;

	// アクセストークン発行時の認証情報を取得するために使用する TokenStore の実装クラスン
	@Inject
	TokenStore tokenStore;

	// アクセストークンの発行時の認可情報を取得するために使用する ApprovalStore の実装クラス
	// アクセストークンの取り消し時に認可情報を削除しない場合は不要
	@Inject
	ApprovalStore approvalStore;

	/**
	 * トークンの取り消しを行う
	 * @param tokenValue 取り消しをおこなうトークンの値
	 * @param clientId クライアントチェックで使用するId
	 */
	public String revokeToken(String tokenValue, String clientId) {
		// アクセストークンを発行した際の認証情報を取得
		OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
		// 認証情報が正常に取得できた場合のみ、トークンの削除処理
		if (authentication != null) {
			// アクセストークン発行時に使用したクライアントIDを取得し、リクエストパラメータのクライアントIDと一致するかを確認
			if (clientId.equals(authentication.getOAuth2Request().getClientId())) {
				// アクセストークン発行時のリソースオーナの認証情報を取得
				// クライアントクレデンシャルグラントを使用・アクセストークンの取り消し時に認可情報を削除しない場合、この処理は不要
				Authentication user = authentication.getUserAuthentication();
				// リソースオーナの認証情報が取得できた場合
				if (user != null) {
					Collection<Approval> approvals = new ArrayList<Approval>();
					for (String scope : authentication.getOAuth2Request().getScope()) {
						approvals.add(
								new Approval(user.getName(), clientId, scope, new Date(), ApprovalStatus.APPROVED));
					}
					// 認可情報の削除
					approvalStore.revokeApprovals(approvals);
				}
				// アクセストークンとアクセストークンに紐付くリフレッシュトークンの削除
				consumerService.revokeToken(tokenValue);
				return "success";

			} else {
				return "invalid client : " + clientId;
			}
		} else {
			return "invalid token : " + tokenValue;
		}
	}
}
