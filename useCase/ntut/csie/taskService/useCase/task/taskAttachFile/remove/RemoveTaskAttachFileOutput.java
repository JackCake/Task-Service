package ntut.csie.taskService.useCase.task.taskAttachFile.remove;

import ntut.csie.taskService.useCase.Output;

public interface RemoveTaskAttachFileOutput extends Output {
	public boolean isRemoveSuccess();
	
	public void setRemoveSuccess(boolean removeSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
