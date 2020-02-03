package ntut.csie.taskService.controller.task.taskAttachFile;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import ntut.csie.taskService.ApplicationContext;
import ntut.csie.taskService.useCase.task.taskAttachFile.upload.UploadTaskAttachFileInput;
import ntut.csie.taskService.useCase.task.taskAttachFile.upload.UploadTaskAttachFileOutput;
import ntut.csie.taskService.useCase.task.taskAttachFile.upload.UploadTaskAttachFileUseCase;

@Path("/tasks/{task_id}/task_attach_files")
@Singleton
public class UploadTaskAttachFileRestfulAPI implements UploadTaskAttachFileOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private UploadTaskAttachFileUseCase uploadTaskAttachFileUseCase = applicationContext.newUploadTaskAttachFileUseCase();
	
	private boolean uploadSuccess;
	private String errorMessage;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized UploadTaskAttachFileOutput uploadTaskAttachFile(
			@PathParam("task_id") String taskId, 
			String taskAttachFileInfo) {
		byte[] attachFileContents = null;
		String name = "";
		
		UploadTaskAttachFileOutput output = this;
		
		try {
			JSONObject taskAttachFileJSON = new JSONObject(taskAttachFileInfo);
			attachFileContents = Base64.decode(taskAttachFileJSON.getString("attachFileContents"));
			name = taskAttachFileJSON.getString("name");
		} catch (JSONException e) {
			e.printStackTrace();
			output.setUploadSuccess(false);
			output.setErrorMessage("Sorry, there is the service problem when upload the attach file of the task. Please contact to the system administrator!");
			return output;
		}
		
		UploadTaskAttachFileInput input = (UploadTaskAttachFileInput) uploadTaskAttachFileUseCase;
		input.setAttachFileContents(attachFileContents);
		input.setName(name);
		input.setTaskId(taskId);
		
		uploadTaskAttachFileUseCase.execute(input, output);
		
		return output;
	}

	@Override
	public boolean isUploadSuccess() {
		return uploadSuccess;
	}

	@Override
	public void setUploadSuccess(boolean addSuccess) {
		this.uploadSuccess = addSuccess;
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
