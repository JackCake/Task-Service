package ntut.csie.taskService.useCase.task.get;

import java.util.List;

import ntut.csie.taskService.useCase.Output;
import ntut.csie.taskService.useCase.task.TaskModel;

public interface GetTasksByBacklogItemIdOutput extends Output{
	public List<TaskModel> getTaskList();
	
	public void setTaskList(List<TaskModel> taskList); 
}
