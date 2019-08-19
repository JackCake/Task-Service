package ntut.csie.taskService.useCase.task;

import ntut.csie.taskService.model.task.Task;

public class ConvertTaskToDTO {
	public static TaskModel transform(Task task) {
		TaskModel dto = new TaskModel();
		dto.setTaskId(task.getTaskId());
		dto.setOrderId(task.getOrderId());
		dto.setDescription(task.getDescription());
		dto.setHandlerId(task.getHandlerId());
		dto.setStatus(task.getStatus());
		dto.setEstimate(task.getEstimate());
		dto.setRemains(task.getRemains());
		dto.setNotes(task.getNotes());
		dto.setBacklogItemId(task.getBacklogItemId());
		return dto;
	}
}
