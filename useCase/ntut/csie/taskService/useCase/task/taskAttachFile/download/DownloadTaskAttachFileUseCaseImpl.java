package ntut.csie.taskService.useCase.task.taskAttachFile.download;

import java.io.File;
import java.nio.file.Files;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.model.task.TaskAttachFile;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class DownloadTaskAttachFileUseCaseImpl implements DownloadTaskAttachFileUseCase, DownloadTaskAttachFileInput {
	private TaskRepository taskRepository;
	
	private String taskAttachFileId;
	private String taskId;
	
	public DownloadTaskAttachFileUseCaseImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	@Override
	public void execute(DownloadTaskAttachFileInput input, DownloadTaskAttachFileOutput output) {
		Task task = taskRepository.getTaskById(input.getTaskId());
		if(task == null) {
			output.setDownloadSuccess(false);
			output.setErrorMessage("Sorry, the task is not exist!");
			return;
		}
		
		byte[] attachFileContents = null;
		try {
			TaskAttachFile taskAttachFile = task.getTaskAttachFile(input.getTaskAttachFileId());
			File attachFile = new File(taskAttachFile.getPath());
			attachFileContents = Files.readAllBytes(attachFile.toPath());
		} catch (Exception e) {
			output.setDownloadSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setDownloadSuccess(true);
		output.setAttachFileContents(attachFileContents);
	}

	@Override
	public String getTaskAttachFileId() {
		return taskAttachFileId;
	}

	@Override
	public void setTaskAttachFileId(String taskAttachFileId) {
		this.taskAttachFileId = taskAttachFileId;
	}

	@Override
	public String getTaskId() {
		return taskId;
	}

	@Override
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
