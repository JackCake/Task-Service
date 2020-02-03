package ntut.csie.taskService.useCase.task.taskAttachFile.upload;

import ntut.csie.taskService.useCase.Output;

public interface UploadTaskAttachFileOutput extends Output {
	public boolean isUploadSuccess();
	
	public void setUploadSuccess(boolean uploadSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
