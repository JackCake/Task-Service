package ntut.csie.taskService.useCase.task.delete;

import ntut.csie.taskService.useCase.Output;

public interface DeleteTaskOutput extends Output{
	public boolean isDeleteSuccess();
	
	public void setDeleteSuccess(boolean deleteSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
