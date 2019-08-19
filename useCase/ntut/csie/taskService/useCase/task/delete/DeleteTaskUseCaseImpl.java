package ntut.csie.taskService.useCase.task.delete;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class DeleteTaskUseCaseImpl implements DeleteTaskUseCase, DeleteTaskInput {
	private TaskRepository taskRepository;
	
	private String taskId;	
	
	public DeleteTaskUseCaseImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	@Override
	public void execute(DeleteTaskInput input, DeleteTaskOutput output) {
		String taskId = input.getTaskId();
		Task task = taskRepository.getTaskById(taskId);
		if(task == null) {
			output.setDeleteSuccess(false);
			output.setErrorMessage("Sorry, the task is not exist.");
			return;
		}
		try {
			taskRepository.remove(task);
		} catch (Exception e) {
			output.setDeleteSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		task.markAsRemoved();
		output.setDeleteSuccess(true);
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
