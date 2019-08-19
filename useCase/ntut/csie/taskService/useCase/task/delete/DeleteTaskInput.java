package ntut.csie.taskService.useCase.task.delete;

import ntut.csie.taskService.useCase.Input;

public interface DeleteTaskInput extends Input{
	public String getTaskId();
	
	public void setTaskId(String taskId);
}
