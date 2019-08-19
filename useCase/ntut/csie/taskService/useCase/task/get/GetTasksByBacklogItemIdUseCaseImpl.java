package ntut.csie.taskService.useCase.task.get;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.useCase.task.ConvertTaskToDTO;
import ntut.csie.taskService.useCase.task.TaskModel;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class GetTasksByBacklogItemIdUseCaseImpl implements GetTasksByBacklogItemIdUseCase, GetTasksByBacklogItemIdInput{
	private TaskRepository taskRepository;
	
	private String backlogItemId;
	
	public GetTasksByBacklogItemIdUseCaseImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	@Override
	public void execute(GetTasksByBacklogItemIdInput input, GetTasksByBacklogItemIdOutput output) {
		List<TaskModel> taskList = new ArrayList<>();
		for(Task task : taskRepository.getTasksByBacklogItemId(input.getBacklogItemId())) {
			taskList.add(ConvertTaskToDTO.transform(task));
		}
		output.setTaskList(taskList);
	}

	@Override
	public String getBacklogItemId() {
		return backlogItemId;
	}

	@Override
	public void setBacklogItemId(String backlogItemId) {
		this.backlogItemId = backlogItemId;
	}
}
