package ntut.csie.taskService.useCase.task.add;

import ntut.csie.taskService.useCase.Output;

public interface AddTaskOutput extends Output {
	public boolean isAddSuccess();
	
	public void setAddSuccess(boolean addSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
