package ntut.csie.taskService.useCase.task;

import java.util.Collection;

import ntut.csie.taskService.model.task.Task;

public interface TaskRepository {
	public void save(Task task) throws Exception;
	
	public void remove(Task task) throws Exception;
	
	public Task getTaskById(String taskId);
	
	public Collection<Task> getTasksByBacklogItemId(String backlogItemId);
}
