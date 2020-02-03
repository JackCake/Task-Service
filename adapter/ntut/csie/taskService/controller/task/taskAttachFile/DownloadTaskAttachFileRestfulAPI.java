package ntut.csie.taskService.controller.task.taskAttachFile;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ntut.csie.taskService.ApplicationContext;
import ntut.csie.taskService.useCase.task.taskAttachFile.download.DownloadTaskAttachFileInput;
import ntut.csie.taskService.useCase.task.taskAttachFile.download.DownloadTaskAttachFileOutput;
import ntut.csie.taskService.useCase.task.taskAttachFile.download.DownloadTaskAttachFileUseCase;

@Path("/tasks/{task_id}/task_attach_files")
@Singleton
public class DownloadTaskAttachFileRestfulAPI implements DownloadTaskAttachFileOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private DownloadTaskAttachFileUseCase downloadBacklogItemAttachFileUseCase = applicationContext.newDownloadTaskAttachFileUseCase();
	
	private boolean downloadSuccess;
	private String errorMessage;
	private byte[] attachFileContents;
	
	@GET
	@Path("/{task_attach_file_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized DownloadTaskAttachFileOutput downloadTaskAttachFile(
			@PathParam("task_id") String taskId, 
			@PathParam("task_attach_file_id") String taskAttachFileId) {
		DownloadTaskAttachFileOutput output = this;
		
		DownloadTaskAttachFileInput input = (DownloadTaskAttachFileInput) downloadBacklogItemAttachFileUseCase;
		input.setTaskAttachFileId(taskAttachFileId);
		input.setTaskId(taskId);
		
		downloadBacklogItemAttachFileUseCase.execute(input, output);
		
		return output;
	}

	@Override
	public boolean isDownloadSuccess() {
		return downloadSuccess;
	}

	@Override
	public void setDownloadSuccess(boolean downloadSuccess) {
		this.downloadSuccess = downloadSuccess;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public byte[] getAttachFileContents() {
		return attachFileContents;
	}

	@Override
	public void setAttachFileContents(byte[] attachFileContents) {
		this.attachFileContents = attachFileContents;
	}
}
