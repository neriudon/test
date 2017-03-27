package todo.oauth.api.todo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import todo.oauth.domain.model.OauthUser;
import todo.oauth.domain.model.Todo;
import todo.oauth.domain.service.todo.TodoService;

@RestController
@RequestMapping("todos")
public class TodoRestController {
	@Inject
	TodoService todoService;
	@Inject
	Mapper beanMapper;

	private static final Logger logger = LoggerFactory.getLogger(TodoRestController.class);

//	/**
//	 * 引数 user にリソースオーナの認証情報が格納される。 レビュー時には使っていない機能
//	 */
//	@RequestMapping(value = "todos", method = RequestMethod.GET)
//	@ResponseStatus(HttpStatus.OK)
//	public Collection<Todo> list(@AuthenticationPrincipal UserDetails user) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("called OAuth Resource/POST. username={}, password={}", user.getUsername(),
//					user.getPassword());
//		}
//		return null;
//	}
//
//	/**
//	 * 引数 user にリソースオーナの認証情報が格納される。 レビュー時には使っていない機能
//	 */
//	@RequestMapping(value = "todos", method = RequestMethod.GET)
//	@ResponseStatus(HttpStatus.OK)
//	public Collection<Todo> list(@AuthenticationPrincipal OauthUser user) {
//		if (logger.isDebugEnabled()) {
//			logger.debug(
//					"called OAuth Resource/POST. username={}, useradditionalvalue={}, client={}, clientadditionalvalue={}",
//					user.getUsername(), user.getUserAdditionalValue(), user.getClientId(),
//					user.getClientAdditionalValue());
//		}
//		return null;
//	}
//
//	/**
//	 * 引数 authentication にリソースオーナ、クライアントの認証情報が格納される。 レビュー時には使っていない機能
//	 */
//	@RequestMapping(value = "todos_auth", method = RequestMethod.GET)
//	@ResponseStatus(HttpStatus.OK)
//	public Collection<Todo> list(OAuth2Authentication authentication) {
//		// authentication よりリソースオーナ名を取得する。
//		String username = authentication.getUserAuthentication().getName();
//		// authentication よりクライアントIDを取得する。
//		String clientId = authentication.getOAuth2Request().getClientId();
//		return null;
//	}

	/**
	 * 全てのTodoを取得する
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<TodoResource> getTodos(@AuthenticationPrincipal OauthUser user) {
		Collection<Todo> todos = todoService.findAll(user.getUsername());
		List<TodoResource> todoResources = new ArrayList<>();
		for (Todo todo : todos) {
			todoResources.add(beanMapper.map(todo, TodoResource.class));
		}
		return todoResources;
	}

	/**
	 * Todoを作成する
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public TodoResource postTodos(@RequestBody @Validated TodoResource todoResource) {
		Todo createdTodo = todoService.create(beanMapper.map(todoResource, Todo.class));
		TodoResource createdTodoResponse = beanMapper.map(createdTodo, TodoResource.class);
		return createdTodoResponse;
	}

	/**
	 * Todoを1件取得する
	 */
	@RequestMapping(value = "{todoId}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public TodoResource getTodo(@PathVariable("todoId") String todoId, @AuthenticationPrincipal OauthUser user) {
		Todo todo = todoService.findOne(todoId, user.getUsername());
		TodoResource todoResource = beanMapper.map(todo, TodoResource.class);
		return todoResource;
	}

	/**
	 * Todoを更新する
	 */
	@RequestMapping(value = "{todoId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public TodoResource putTodo(@PathVariable("todoId") String todoId, @AuthenticationPrincipal OauthUser user) {
		Todo finishedTodo = todoService.finish(todoId, user.getUsername());
		TodoResource finishedTodoResource = beanMapper.map(finishedTodo, TodoResource.class);
		return finishedTodoResource;
	}

	/**
	 * Todoを削除する
	 */
	@RequestMapping(value = "{todoId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTodo(@PathVariable("todoId") String todoId, @AuthenticationPrincipal OauthUser user) {
		todoService.delete(todoId, user.getUsername());
	}
}
