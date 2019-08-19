package ntut.csie.taskService.useCase.task.move;

import ntut.csie.taskService.useCase.Output;

public interface MoveTaskOutput extends Output{
	public boolean isMoveSuccess();
	
	public void setMoveSuccess(boolean moveSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
