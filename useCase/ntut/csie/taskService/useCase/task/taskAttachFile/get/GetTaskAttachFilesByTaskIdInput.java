package ntut.csie.taskService.useCase.task.taskAttachFile.get;

import ntut.csie.taskService.useCase.Input;

public interface GetTaskAttachFilesByTaskIdInput extends Input {
	public String getTaskId();
	
	public void setTaskId(String taskId);
}
