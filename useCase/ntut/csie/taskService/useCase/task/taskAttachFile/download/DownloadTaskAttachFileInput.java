package ntut.csie.taskService.useCase.task.taskAttachFile.download;

import ntut.csie.taskService.useCase.Input;

public interface DownloadTaskAttachFileInput extends Input {
	public String getTaskAttachFileId();
	
	public void setTaskAttachFileId(String taskAttachFileId);
	
	public String getTaskId();
	
	public void setTaskId(String taskId);
}
