package ntut.csie.taskService.useCase.task.move;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class MoveTaskUseCaseImpl implements MoveTaskUseCase, MoveTaskInput{
	private TaskRepository taskRepository;
	
	private String taskId;
	private String status;
	
	public MoveTaskUseCaseImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	@Override
	public void execute(MoveTaskInput input, MoveTaskOutput output) {
		Task task = taskRepository.getTaskById(input.getTaskId());
		if(task == null) {
			output.setMoveSuccess(false);
			output.setErrorMessage("Sorry, the task is not exist!");
			return;
		}
		task.changeStatus(input.getStatus());
		try {
			taskRepository.save(task);
		} catch (Exception e) {
			output.setMoveSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setMoveSuccess(true);
	}

	@Override
	public String getTaskId() {
		return taskId;
	}

	@Override
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}
}
