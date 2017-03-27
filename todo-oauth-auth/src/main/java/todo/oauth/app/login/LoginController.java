package todo.oauth.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * ログインフォームを表示するためのビューコントローラ
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	/**
	 * /login下のlogin.jspを表示
	 */
	@RequestMapping
	public String login() {
		return "login/login";
	}
}