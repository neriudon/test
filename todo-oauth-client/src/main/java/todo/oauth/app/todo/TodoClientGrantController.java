package todo.oauth.app.todo;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.groups.Default;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import todo.oauth.app.todo.TodoForm.TodoCreate;
import todo.oauth.app.todo.TodoForm.TodoDelete;
import todo.oauth.app.todo.TodoForm.TodoFinish;
import todo.oauth.domain.model.Todo;
import todo.oauth.domain.service.todo.TodoService;
import todo.oauth.domain.service.token.RevokeTokenClientService;
/**
 * クライアントクレデンシャルグラントで認可したとき用のコントローラ<br/>
 * わかりやすくするためtodo/clientにマッピングする
 */
@Controller
@RequestMapping("todo/client")
public class TodoClientGrantController {
	// クライアントクレデンシャルグラント用のサービス
	@Inject
	@Named("clientGrantTodoServiceImpl")
	TodoService todoService;
	// トークンを取り消すサービス
	@Inject
	RevokeTokenClientService revokeTokenClientService;

	@Inject
	Mapper beanMapper;


	@ModelAttribute
	public TodoForm setUpForm() {
		TodoForm form = new TodoForm();
		return form;
	}

	/**
	 * Todoを一覧表示
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String list(Model model) {
		Collection<Todo> todos = todoService.findAll(getUserName());
		model.addAttribute("todos", todos);
		return "todo/client/list";
	}

	/**
	 * Todoを新規に作成
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Validated({ Default.class, TodoCreate.class }) TodoForm todoForm, BindingResult bindingResult,
			Model model, RedirectAttributes attributes) {
		// 入力チェック
		if (bindingResult.hasErrors()) {
			return list(model);
		}

		Todo todo = beanMapper.map(todoForm, Todo.class);
		todo.setOwner(getUserName());
		try {
			todoService.create(todo);
		} catch (BusinessException e) {
			model.addAttribute(e.getResultMessages());
			return list(model);
		}
		attributes.addFlashAttribute(ResultMessages.success().add(ResultMessage.fromText("Created successfully!")));
		return "redirect:/todo/client/list";
	}

	/**
	 * Todoの終了フラグをたてる
	 */
	@RequestMapping(value = "finish", method = RequestMethod.POST)
	public String finish(@Validated({ Default.class, TodoFinish.class }) TodoForm form, BindingResult bindingResult,
			Model model, RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			return list(model);
		}

		try {
			todoService.finish(form.getTodoId());
		} catch (BusinessException e) {
			model.addAttribute(e.getResultMessages());
			return list(model);
		}

		attributes.addFlashAttribute(ResultMessages.success().add(ResultMessage.fromText("Finished successfully!")));
		return "redirect:/todo/client/list";
	}

	/**
	 * Todoを削除する
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public String delete(@Validated({ Default.class, TodoDelete.class }) TodoForm form, BindingResult bindingResult,
			Model model, RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			return list(model);
		}

		try {
			todoService.delete(form.getTodoId());
		} catch (BusinessException e) {
			model.addAttribute(e.getResultMessages());
			return list(model);
		}

		attributes.addFlashAttribute(ResultMessages.success().add(ResultMessage.fromText("Deleted successfully!")));
		return "redirect:/todo/client/list";
	}

	/**
	 * トークンを取り消す
	 */
	@RequestMapping(value = "/revoke", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public String revokeTokenbyClientGrant(RedirectAttributes attributes) {
		revokeTokenClientService.revokeToken();
		attributes.addFlashAttribute(ResultMessages.success().add(ResultMessage.fromText("Revoked successfully!")));
		return "todo/revoked";
	}
	
	private String getUserName() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getUsername();
	}
}
