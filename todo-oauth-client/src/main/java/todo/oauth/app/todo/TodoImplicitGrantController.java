package todo.oauth.app.todo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * インプリシットグラントで認可したとき用のコントローラ<br/>
 * わかりやすくするためtodo/implicitにマッピングする
 */
@Controller
@RequestMapping("todo/implicit")
public class TodoImplicitGrantController {
	
    @Value("${auth.serverUrl}")
    String authServerUrl;

    @Value("${client.serverUrl}")
    String clientServerUrl;

    @Value("${resource.serverUrl}")
    String resourceServerUrl;

    @Value("${api.auth.clientId}")
    String clientId;

	/**
	 * Todoを一覧表示
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String list(Model model) {
        model.addAttribute("authServerUrl", authServerUrl);
        model.addAttribute("clientServerUrl", clientServerUrl);
        model.addAttribute("resourceServerUrl", resourceServerUrl);
        model.addAttribute("clientId", clientId);
		return "todo/implicit/list";
	}
}
