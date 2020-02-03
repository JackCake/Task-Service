package ntut.csie.taskService.unitTest.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class FakeTaskRepository implements TaskRepository{
	private Map<String, Task> tasks;

	public FakeTaskRepository() {
		tasks = Collections.synchronizedMap(new LinkedHashMap<String, Task>());
	}
	
	@Override
	public void save(Task task) {
		tasks.put(task.getTaskId(), task);
	}

	@Override
	public void remove(Task task) {
		tasks.remove(task.getTaskId());
	}

	@Override
	public Task getTaskById(String taskId) {
		return tasks.get(taskId);
	}

	@Override
	public Collection<Task> getTasksByBacklogItemId(String backlogItemId) {
		List<Task> taskList = new ArrayList<>();
		for(Task task : tasks.values()) {
			if(task.getBacklogItemId().equals(backlogItemId)) {
				taskList.add(task);
			}
		}
		return taskList;
	}
	
	public void clearAll() {
		tasks.clear();
	}
}