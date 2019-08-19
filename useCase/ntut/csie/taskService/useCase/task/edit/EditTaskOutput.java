package ntut.csie.taskService.useCase.task.edit;

import ntut.csie.taskService.useCase.Output;

public interface EditTaskOutput extends Output{
	public boolean isEditSuccess();
	
	public void setEditSuccess(boolean editSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
