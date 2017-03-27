package todo.oauth.app;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * クライアントのトップページを表示するためのビューコントローラ
 */
@Controller
public class HomeController {

	@RequestMapping(value = "/")
	public String home(Locale locale, Model model) {
		return "home/home";
	}

}
