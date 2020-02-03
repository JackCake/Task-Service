package ntut.csie.taskService.useCase.task.taskAttachFile.upload;

import ntut.csie.taskService.useCase.Input;

public interface UploadTaskAttachFileInput extends Input {
	public byte[] getAttachFileContents();
	
	public void setAttachFileContents(byte[] attachFileContents);
	
	public String getName();
	
	public void setName(String name);
	
	public String getTaskId();
	
	public void setTaskId(String taskId);
}
