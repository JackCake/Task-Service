package ntut.csie.taskService.controller.task;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ntut.csie.taskService.ApplicationContext;
import ntut.csie.taskService.useCase.task.TaskModel;
import ntut.csie.taskService.useCase.task.get.GetTasksByBacklogItemIdInput;
import ntut.csie.taskService.useCase.task.get.GetTasksByBacklogItemIdOutput;
import ntut.csie.taskService.useCase.task.get.GetTasksByBacklogItemIdUseCase;

@Path("/backlog_items/{backlog_item_id}/tasks")
@Singleton
public class GetTasksByBacklogItemIdRestfulAPI implements GetTasksByBacklogItemIdOutput{
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private GetTasksByBacklogItemIdUseCase getTasksByBacklogItemIdUseCase = applicationContext.newGetTasksByBacklogItemIdUseCase();
	
	private List<TaskModel> taskList;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized GetTasksByBacklogItemIdOutput getTasksByBacklogItemId(@PathParam("backlog_item_id") String backlogItemId) {
		GetTasksByBacklogItemIdOutput output = this;
		
		GetTasksByBacklogItemIdInput input = (GetTasksByBacklogItemIdInput) getTasksByBacklogItemIdUseCase;
		input.setBacklogItemId(backlogItemId);
		
		getTasksByBacklogItemIdUseCase.execute(input, output);
		
		return output;
	}
	
	@Override
	public List<TaskModel> getTaskList() {
		return taskList;
	}

	@Override
	public void setTaskList(List<TaskModel> taskList) {
		this.taskList = taskList;
	}
}
