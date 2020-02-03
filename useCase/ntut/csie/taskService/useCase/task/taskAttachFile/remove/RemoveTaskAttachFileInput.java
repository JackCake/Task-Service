package ntut.csie.taskService.useCase.task.taskAttachFile.remove;

import ntut.csie.taskService.useCase.Input;

public interface RemoveTaskAttachFileInput extends Input {
	public String getTaskAttachFileId();
	
	public void setTaskAttachFileId(String taskAttachFileId);
	
	public String getTaskId();
	
	public void setTaskId(String taskId);
}
