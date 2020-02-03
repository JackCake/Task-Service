package ntut.csie.taskService.useCase.task.taskAttachFile.remove;

import java.io.File;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.model.task.TaskAttachFile;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class RemoveTaskAttachFileUseCaseImpl implements RemoveTaskAttachFileUseCase, RemoveTaskAttachFileInput {
	private TaskRepository taskRepository;
	
	private String taskAttachFileId;
	private String taskId;
	
	public RemoveTaskAttachFileUseCaseImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	@Override
	public void execute(RemoveTaskAttachFileInput input, RemoveTaskAttachFileOutput output) {
		Task task = taskRepository.getTaskById(input.getTaskId());
		if(task == null) {
			output.setRemoveSuccess(false);
			output.setErrorMessage("Sorry, the task is not exist!");
			return;
		}
		
		try {
			String taskAttachFileId = input.getTaskAttachFileId();
			TaskAttachFile taskAttachFile = task.getTaskAttachFile(taskAttachFileId);
			File attachFile = new File(taskAttachFile.getPath());
			if(!attachFile.delete()) {
				output.setRemoveSuccess(false);
				output.setErrorMessage("Sorry, there is the problem when delete the attach file of the task. Please contact to the system administrator!");
				return;
			}
			File folder = attachFile.getParentFile();
			if(folder.list().length == 0) {
				folder.delete();
			}
			task.removeTaskAttachFile(taskAttachFileId);
			taskRepository.save(task);
		} catch (Exception e) {
			output.setRemoveSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setRemoveSuccess(true);
	}

	@Override
	public String getTaskAttachFileId() {
		return taskAttachFileId;
	}

	@Override
	public void setTaskAttachFileId(String backlogItemAttachFileId) {
		this.taskAttachFileId = backlogItemAttachFileId;
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
