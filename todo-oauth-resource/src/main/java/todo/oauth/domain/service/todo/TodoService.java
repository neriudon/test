package todo.oauth.domain.service.todo;

import java.util.Collection;

import todo.oauth.domain.model.Todo;

public interface TodoService {

	Todo findOne(String todoId, String owner);

	Collection<Todo> findAll(String owner);

	Todo create(Todo todo);

	Todo finish(String todoId, String owner);

	void delete(String todoId, String owner);
}
