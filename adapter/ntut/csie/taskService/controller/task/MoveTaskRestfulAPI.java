package ntut.csie.taskService.controller.task;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import ntut.csie.taskService.ApplicationContext;
import ntut.csie.taskService.useCase.task.move.MoveTaskInput;
import ntut.csie.taskService.useCase.task.move.MoveTaskOutput;
import ntut.csie.taskService.useCase.task.move.MoveTaskUseCase;

@Path("/task_statuses")
@Singleton
public class MoveTaskRestfulAPI implements MoveTaskOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private MoveTaskUseCase moveTaskUseCase = applicationContext.newMoveTaskUseCase();
	
	private boolean moveSuccess;
	private String errorMessage;
	
	@PUT
	@Path("/{task_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized MoveTaskOutput moveTask(
			@PathParam("task_id") String taskId, 
			String taskInfo) {
		String status = "";
		
		MoveTaskOutput output = this;
		
		try {
			JSONObject taskJSON = new JSONObject(taskInfo);
			status = taskJSON.getString("status");
		} catch (JSONException e) {
			e.printStackTrace();
			output.setMoveSuccess(false);
			output.setErrorMessage("Sorry, there is the service problem when move the task. Please contact to the system administrator!");
			return output;
		}
		
		MoveTaskInput input = (MoveTaskInput) moveTaskUseCase;
		input.setTaskId(taskId);
		input.setStatus(status);
		
		moveTaskUseCase.execute(input, output);
		
		return output;
	}

	@Override
	public boolean isMoveSuccess() {
		return moveSuccess;
	}

	@Override
	public void setMoveSuccess(boolean moveSuccess) {
		this.moveSuccess = moveSuccess;
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
