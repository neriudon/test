package todo.oauth.domain.repository.todo;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import todo.oauth.domain.model.Todo;

public interface TodoRepository {
	Todo findOne(@Param("todoId") String todoId, @Param("owner") String owner);

	Collection<Todo> findAll(String owner);

	void create(Todo todo);

	boolean update(Todo todo);

	void delete(Todo todo);

	long countByFinished(@Param("finished") boolean finished, @Param("owner") String owner);

}
