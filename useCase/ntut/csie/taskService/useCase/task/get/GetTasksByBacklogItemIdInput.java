package ntut.csie.taskService.useCase.task.get;

import ntut.csie.taskService.useCase.Input;

public interface GetTasksByBacklogItemIdInput extends Input{
	public String getBacklogItemId();
	
	public void setBacklogItemId(String backlogItemId);
}
