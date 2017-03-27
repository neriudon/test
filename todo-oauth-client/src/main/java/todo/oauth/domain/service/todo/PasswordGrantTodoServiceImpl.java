package todo.oauth.domain.service.todo;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.terasoluna.gfw.common.exception.BusinessException;

import todo.oauth.domain.model.Todo;
/**
 * リソースオーナパスワードクレデンシャルグラント用のサービスクラス
 */
@Service
@Transactional
public class PasswordGrantTodoServiceImpl implements TodoService {

	/** 複数OAuth2RestTemplateクラスをビーン定義しているためNamedアノテーションを付ける */
	@Inject
	@Named("todoPasswordGrantResourceRestTemplate")
	RestOperations restOperations;
	// リソースサーバのAPIのエンドポイント
	@Value("${resource.serverUrl}/api/v1/todos")
	String uri;

	/**
	 * Todoを全件取得
	 */
	@Override
	public Collection<Todo> findAll(String owner) {
		// 指定したURLにRESTでメソッドGETでアクセスし結果をリストで受け取る
		Todo[] todoArray = null;
		try {
			todoArray = restOperations.getForObject(uri, Todo[].class);
		} catch (InsufficientScopeException e) {
			// 認可が無いときのをキャッチ
			// ボディを引数に渡し業務例外をスロー
			throw new BusinessException(e.getMessage());
		}
		return Arrays.asList(todoArray);
	}

	/**
	 * Todoを作成
	 */
	@Override
	public void create(Todo todo) {
		try {
			restOperations.postForObject(uri, todo, Todo.class);
		} catch (HttpClientErrorException e) {
			// 4xx系の例外をキャッチ
			// ボディを引数に渡し業務例外をスロー
			throw new BusinessException(e.getResponseBodyAsString());
		} catch (InsufficientScopeException e) {
			// 認可が無いときのをキャッチ
			// ボディを引数に渡し業務例外をスロー
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * Todoを更新
	 */
	@Override
	public void finish(String todoId) {
		try {
			restOperations.put(uri + "/{todoId}", null, todoId);
		} catch (HttpClientErrorException e) {
			// 4xx系の例外をキャッチ
			// ボディを引数に渡し業務例外をスロー
			throw new BusinessException(e.getResponseBodyAsString());
		} catch (InsufficientScopeException e) {
			// 認可が無いときのをキャッチ
			// ボディを引数に渡し業務例外をスロー
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * Todoを削除
	 */
	@Override
	public void delete(String todoId) {
		try {
			restOperations.delete(uri + "/{todoId}", todoId);
		} catch (HttpClientErrorException e) {
			// 4xx系の例外をキャッチ
			// ボディを引数に渡し業務例外をスロー
			throw new BusinessException(e.getResponseBodyAsString());
		} catch (InsufficientScopeException e) {
			// 認可が無いときのをキャッチ
			// ボディを引数に渡し業務例外をスロー
			throw new BusinessException(e.getMessage());
		}

	}
}