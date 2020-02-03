package ntut.csie.taskService.useCase.task.taskAttachFile.get;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.model.task.TaskAttachFile;
import ntut.csie.taskService.useCase.task.TaskRepository;
import ntut.csie.taskService.useCase.task.taskAttachFile.ConvertTaskAttachFileToDTO;
import ntut.csie.taskService.useCase.task.taskAttachFile.TaskAttachFileModel;

public class GetTaskAttachFilesByTaskIdUseCaseImpl implements GetTaskAttachFilesByTaskIdUseCase, GetTaskAttachFilesByTaskIdInput {
	private TaskRepository taskRepository;
	
	private String taskId;
	
	public GetTaskAttachFilesByTaskIdUseCaseImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	@Override
	public void execute(GetTaskAttachFilesByTaskIdInput input,
			GetTaskAttachFilesByTaskIdOutput output) {
		List<TaskAttachFileModel> taskAttachFileList = new ArrayList<>();
		Task task = taskRepository.getTaskById(input.getTaskId());
		for(TaskAttachFile taskAttachFile : task.getTaskAttachFiles()) {
			taskAttachFileList.add(ConvertTaskAttachFileToDTO.transform(taskAttachFile));
		}
		output.setTaskAttachFileList(taskAttachFileList);
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
