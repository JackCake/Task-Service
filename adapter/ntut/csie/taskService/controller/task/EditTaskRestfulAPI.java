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
import ntut.csie.taskService.useCase.task.edit.EditTaskInput;
import ntut.csie.taskService.useCase.task.edit.EditTaskOutput;
import ntut.csie.taskService.useCase.task.edit.EditTaskUseCase;

@Path("/tasks")
@Singleton
public class EditTaskRestfulAPI implements EditTaskOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private EditTaskUseCase editTaskUseCase = applicationContext.newEditTaskUseCase();
	
	private boolean editSuccess;
	private String errorMessage;
	
	@PUT
	@Path("/{task_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized EditTaskOutput editTask(
			@PathParam("task_id") String taskId, 
			String taskInfo) {
		String description = "";
		int estimate = 0;
		int remains = 0;
		String notes = "";
		
		EditTaskOutput output = this;
		
		try {
			JSONObject taskJSON = new JSONObject(taskInfo);
			description = taskJSON.getString("description");
			estimate = taskJSON.getInt("estimate");
			remains = taskJSON.getInt("remains");
			notes = taskJSON.getString("notes");
		} catch (JSONException e) {
			e.printStackTrace();
			output.setEditSuccess(false);
			output.setErrorMessage("Sorry, there is the service problem when edit the task. Please contact to the system administrator!");
			return output;
		}
		
		EditTaskInput input = (EditTaskInput) editTaskUseCase;
		input.setTaskId(taskId);
		input.setDescription(description);
		input.setEstimate(estimate);
		input.setRemains(remains);
		input.setNotes(notes);
		
		editTaskUseCase.execute(input, output);
		
		return output;
	}
	
	@Override
	public boolean isEditSuccess() {
		return editSuccess;
	}

	@Override
	public void setEditSuccess(boolean editSuccess) {
		this.editSuccess = editSuccess;
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
