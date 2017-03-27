package todo.oauth.app.oauth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 認可画面を表示するためのビューコントローラ
 */
@Controller
public class OAuth2ApprovalController {

	/**
	 * 認可画面のカスタマイズをするために <br/>
	 * 認可画面の表示を行うコントローラのURLにマッピングする
	 */
	@RequestMapping("/oauth/confirm_access")
	public String confirmApproval() {
		return "approval/oauthConfirm";
	}

}
