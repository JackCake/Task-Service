package ntut.csie.taskService.model.task;

import java.util.UUID;

public class TaskBuilder {
	private String taskId;
	private int orderId;
	private String description;
	private int estimate;
	private String notes;
	private String backlogItemId;
	
	public static TaskBuilder newInstance() {
		return new TaskBuilder();
	}
	
	public TaskBuilder orderId(int orderId) {
		this.orderId = orderId;
		return this;
	}
	
	public TaskBuilder description(String description) {
		this.description = description;
		return this;
	}
	
	public TaskBuilder estimate(int estimate) {
		this.estimate = estimate;
		return this;
	}
	
	public TaskBuilder notes(String notes) {
		this.notes = notes;
		return this;
	}
	
	public TaskBuilder backlogItemId(String backlogItemId) {
		this.backlogItemId = backlogItemId;
		return this;
	}
	
	public Task build() throws Exception {
		String exceptionMessage = "";
		if(description == null || description.isEmpty()) {
			exceptionMessage += "The description of the task should be required!\n";
		}
		if(backlogItemId == null || backlogItemId.isEmpty()) {
			exceptionMessage += "The backlog item id of the task should be required!\n";
		}
		if(!exceptionMessage.isEmpty()) {
			throw new Exception(exceptionMessage);
		}
		
		taskId = UUID.randomUUID().toString();
		Task task = new Task(taskId, description, backlogItemId);
		task.setOrderId(orderId);
		task.setStatus(TaskStatus.toDo);
		task.setEstimate(estimate);
		task.setRemains(estimate);
		task.setNotes(notes);
		return task;
	}
}
