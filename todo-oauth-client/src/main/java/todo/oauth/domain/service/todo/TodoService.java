package todo.oauth.domain.service.todo;

import java.util.Collection;

import todo.oauth.domain.model.Todo;

public interface TodoService {

	Collection<Todo> findAll(String owner);

	void create(Todo todo);

	void finish(String todoId);

	void delete(String todoId);
}
