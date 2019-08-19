package ntut.csie.taskService.useCase.task.move;

import ntut.csie.taskService.useCase.Input;

public interface MoveTaskInput extends Input{
	public String getTaskId();
	
	public void setTaskId(String taskId);
	
	public String getStatus();
	
	public void setStatus(String status);
}
