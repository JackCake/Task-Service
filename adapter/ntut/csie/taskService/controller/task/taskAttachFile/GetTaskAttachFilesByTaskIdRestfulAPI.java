package ntut.csie.taskService.controller.task.taskAttachFile;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ntut.csie.taskService.ApplicationContext;
import ntut.csie.taskService.useCase.task.taskAttachFile.TaskAttachFileModel;
import ntut.csie.taskService.useCase.task.taskAttachFile.get.GetTaskAttachFilesByTaskIdInput;
import ntut.csie.taskService.useCase.task.taskAttachFile.get.GetTaskAttachFilesByTaskIdOutput;
import ntut.csie.taskService.useCase.task.taskAttachFile.get.GetTaskAttachFilesByTaskIdUseCase;

@Path("/tasks/{task_id}/task_attach_files")
@Singleton
public class GetTaskAttachFilesByTaskIdRestfulAPI implements GetTaskAttachFilesByTaskIdOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private GetTaskAttachFilesByTaskIdUseCase getTaskAttachFilesByTaskIdUseCase = applicationContext.newGetTaskAttachFilesByTaskIdUseCase();
	
	private List<TaskAttachFileModel> taskAttachFileList;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized GetTaskAttachFilesByTaskIdOutput getTaskAttachFilesByTaskId(@PathParam("task_id") String taskId) {
		GetTaskAttachFilesByTaskIdOutput output = this;
		
		GetTaskAttachFilesByTaskIdInput input = (GetTaskAttachFilesByTaskIdInput) getTaskAttachFilesByTaskIdUseCase;
		input.setTaskId(taskId);
		
		getTaskAttachFilesByTaskIdUseCase.execute(input, output);
		
		return output;
	}

	@Override
	public List<TaskAttachFileModel> getTaskAttachFileList() {
		return taskAttachFileList;
	}

	@Override
	public void setTaskAttachFileList(List<TaskAttachFileModel> taskAttachFileList) {
		this.taskAttachFileList = taskAttachFileList;
	}
}
