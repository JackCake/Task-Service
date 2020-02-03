package ntut.csie.taskService.useCase.task.taskAttachFile.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class UploadTaskAttachFileUseCaseImpl implements UploadTaskAttachFileUseCase, UploadTaskAttachFileInput {
	private TaskRepository taskRepository;
	
	private byte[] attachFileContents;
	private String name;
	private String taskId;
	
	public UploadTaskAttachFileUseCaseImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	@Override
	public void execute(UploadTaskAttachFileInput input, UploadTaskAttachFileOutput output) {
		byte[] attachFileContents = input.getAttachFileContents();
		if(attachFileContents == null || attachFileContents.length == 0) {
			output.setUploadSuccess(false);
			output.setErrorMessage("Please upload the file!");
			return;
		}
		int maxAttachFileSize = 2097152; // (1MB = 1024 KB = 1048576 bytes)
		if(attachFileContents.length > maxAttachFileSize) {
			output.setUploadSuccess(false);
			output.setErrorMessage("The size of the file is too large! Please upload the smaller file!");
			return;
		}
		String taskId = input.getTaskId();
		String name = input.getName();
		String folderPath = "taskAttachFiles" + File.separator + taskId;
		String attachFilePath = folderPath + File.separator + UUID.randomUUID().toString() + name; //加上UUID是為了確保檔案是不重複的，以免重複上傳檔案造成檔案被覆寫的現象
		Task task = taskRepository.getTaskById(taskId);
		if(task == null) {
			output.setUploadSuccess(false);
			output.setErrorMessage("Sorry, the task is not exist!");
			return;
		}
		try {
			task.uploadTaskAttachFile(name, attachFilePath);
			File folder = new File(folderPath);
			if(!folder.exists()) {
				folder.mkdirs();
			}
			OutputStream uploadedAttachFileOutputStream = new FileOutputStream(new File(attachFilePath));
			uploadedAttachFileOutputStream.write(attachFileContents);
			uploadedAttachFileOutputStream.flush();
			uploadedAttachFileOutputStream.close();
			taskRepository.save(task);
		} catch (Exception e) {
			output.setUploadSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setUploadSuccess(true);
	}
	
	@Override
	public byte[] getAttachFileContents() {
		return attachFileContents;
	}

	@Override
	public void setAttachFileContents(byte[] attachFileContents) {
		this.attachFileContents = attachFileContents;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getTaskId() {
		return taskId;
	}

	@Override
	public void setTaskId(String backlogItemId) {
		this.taskId = backlogItemId;
	}
}
