package ntut.csie.taskService.controller.task.taskAttachFile;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import ntut.csie.taskService.ApplicationContext;
import ntut.csie.taskService.useCase.task.taskAttachFile.remove.RemoveTaskAttachFileInput;
import ntut.csie.taskService.useCase.task.taskAttachFile.remove.RemoveTaskAttachFileOutput;
import ntut.csie.taskService.useCase.task.taskAttachFile.remove.RemoveTaskAttachFileUseCase;

@Path("/tasks/{task_id}/task_attach_files")
@Singleton
public class RemoveTaskAttachFileRestfulAPI implements RemoveTaskAttachFileOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private RemoveTaskAttachFileUseCase removeTaskAttachFileUseCase = applicationContext.newRemoveTaskAttachFileUseCase();

	private boolean removeSuccess;
	private String errorMessage;
	
	@DELETE
	@Path("/{task_attach_file_id}")
	public synchronized RemoveTaskAttachFileOutput removeTaskAttachFile(
			@PathParam("task_id") String taskId, 
			@PathParam("task_attach_file_id") String taskAttachFileId) {
		RemoveTaskAttachFileOutput output = this;
		
		RemoveTaskAttachFileInput input = (RemoveTaskAttachFileInput) removeTaskAttachFileUseCase;
		input.setTaskAttachFileId(taskAttachFileId);
		input.setTaskId(taskId);
		
		removeTaskAttachFileUseCase.execute(input, output);
		
		return output;
	}

	@Override
	public boolean isRemoveSuccess() {
		return removeSuccess;
	}

	@Override
	public void setRemoveSuccess(boolean removeSuccess) {
		this.removeSuccess = removeSuccess;
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
