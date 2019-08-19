package ntut.csie.taskService.controller.task;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import ntut.csie.taskService.ApplicationContext;
import ntut.csie.taskService.useCase.task.delete.DeleteTaskInput;
import ntut.csie.taskService.useCase.task.delete.DeleteTaskOutput;
import ntut.csie.taskService.useCase.task.delete.DeleteTaskUseCase;

@Path("/tasks")
@Singleton
public class DeleteTaskRestfulAPI implements DeleteTaskOutput{
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private DeleteTaskUseCase deleteTaskUseCase = applicationContext.newDeleteTaskUseCase();
	
	private boolean deleteSuccess;
	private String errorMessage;
	
	@DELETE
	@Path("/{task_id}")
	public synchronized DeleteTaskOutput deleteTask(@PathParam("task_id") String taskId) {
		DeleteTaskOutput output = this;
		
		DeleteTaskInput input = (DeleteTaskInput) deleteTaskUseCase;
		input.setTaskId(taskId);
		
		deleteTaskUseCase.execute(input, output);
		
		return output;
	}
	
	@Override
	public boolean isDeleteSuccess() {
		return deleteSuccess;
	}
	
	@Override
	public void setDeleteSuccess(boolean deleteSuccess) {
		this.deleteSuccess = deleteSuccess;
	}
	
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
	
	@Override
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
