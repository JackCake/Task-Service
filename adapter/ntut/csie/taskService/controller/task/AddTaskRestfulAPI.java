package ntut.csie.taskService.controller.task;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import ntut.csie.taskService.ApplicationContext;
import ntut.csie.taskService.useCase.task.add.AddTaskInput;
import ntut.csie.taskService.useCase.task.add.AddTaskOutput;
import ntut.csie.taskService.useCase.task.add.AddTaskUseCase;

@Path("/backlog_items/{backlog_item_id}/tasks")
@Singleton
public class AddTaskRestfulAPI implements AddTaskOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private AddTaskUseCase addTaskUseCase = applicationContext.newAddTaskUseCase();
	
	private boolean addSuccess;
	private String errorMessage;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized AddTaskOutput addTask(
			@PathParam("backlog_item_id") String backlogItemId, 
			String taskInfo) {
		String description = "";
		int estimate = 0;
		String notes = "";
		
		AddTaskOutput output = this;
		
		try {
			JSONObject taskJSON = new JSONObject(taskInfo);
			description = taskJSON.getString("description");
			estimate = taskJSON.getInt("estimate");
			notes = taskJSON.getString("notes");
		} catch (JSONException e) {
			e.printStackTrace();
			output.setAddSuccess(false);
			output.setErrorMessage("Sorry, there is the service problem when add the task. Please contact to the system administrator!");
			return output;
		}
		
		AddTaskInput input = (AddTaskInput) addTaskUseCase;
		input.setDescription(description);
		input.setEstimate(estimate);
		input.setNotes(notes);
		input.setBacklogItemId(backlogItemId);
		
		addTaskUseCase.execute(input, output);
		
		return output;
	}

	@Override
	public boolean isAddSuccess() {
		return addSuccess;
	}

	@Override
	public void setAddSuccess(boolean addSuccess) {
		this.addSuccess = addSuccess;
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
