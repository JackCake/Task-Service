package ntut.csie.taskService.model.task;

import java.util.Date;

import ntut.csie.taskService.model.DateProvider;
import ntut.csie.taskService.model.DomainEvent;

public class TaskDeleted implements DomainEvent {
	private Date occurredOn;
	private String taskId;
	private String backlogItemId;

	public TaskDeleted(String taskId, String backlogItemId) {
		this.occurredOn = DateProvider.now();
		this.taskId = taskId;
		this.backlogItemId = backlogItemId;
	}
	
	@Override
	public Date occurredOn() {
		return occurredOn;
	}
	
	public String taskId() {
		return taskId;
	}
	
	public String backlogItemId() {
		return backlogItemId;
	}
}
