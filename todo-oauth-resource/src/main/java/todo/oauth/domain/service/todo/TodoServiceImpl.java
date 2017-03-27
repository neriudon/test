package todo.oauth.domain.service.todo;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import todo.oauth.domain.model.Todo;
import todo.oauth.domain.repository.todo.TodoRepository;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {

	private static final long MAX_UNFINISHED_COUNT = 5;

	@Inject
	TodoRepository todoRepository;

	@Transactional(readOnly = true)
	public Todo findOne(String todoId, String owner) {
		Todo todo = todoRepository.findOne(todoId, owner);
		if (todo == null) {

			ResultMessages messages = ResultMessages.error();
			messages.add("E404", todoId);
			throw new ResourceNotFoundException(messages);
		}
		return todo;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Todo> findAll(String owner) {
		return todoRepository.findAll(owner);
	}

	@Override
	public Todo create(Todo todo) {
		long unfinishedCount = todoRepository.countByFinished(false, todo.getOwner());
		if (unfinishedCount >= MAX_UNFINISHED_COUNT) {
			ResultMessages messages = ResultMessages.error();
			messages.add("E001", MAX_UNFINISHED_COUNT);
			throw new BusinessException(messages);
		}

		String todoId = UUID.randomUUID().toString();
		Date createdAt = new Date();

		todo.setTodoId(todoId);
		todo.setCreatedAt(createdAt);
		todo.setFinished(false);
		todoRepository.create(todo);
		return todo;
	}

	@Override
	public Todo finish(String todoId, String owner) {
		Todo todo = findOne(todoId, owner);
		if (todo.isFinished()) {
			ResultMessages messages = ResultMessages.error();
			messages.add("E002", todoId);
			throw new BusinessException(messages);
		}
		todo.setFinished(true);
		todoRepository.update(todo);
		return todo;
	}

	@Override
	public void delete(String todoId, String owner) {
		Todo todo = findOne(todoId, owner);
		todoRepository.delete(todo);
	}
}
