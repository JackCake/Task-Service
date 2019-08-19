package ntut.csie.taskService.useCase.history.get;

import ntut.csie.taskService.useCase.Input;

public interface GetHistoriesByTaskIdInput extends Input{
	public String getTaskId();
	
	public void setTaskId(String taskId);
}
