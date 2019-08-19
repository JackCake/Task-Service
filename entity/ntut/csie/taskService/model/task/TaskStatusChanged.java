package ntut.csie.taskService.model.task;

import java.util.Date;

import ntut.csie.taskService.model.DateProvider;
import ntut.csie.taskService.model.DomainEvent;

public class TaskStatusChanged implements DomainEvent {
	private Date occurredOn;
	private String taskId;
	private String originalStatus;
	private String newStatus;

	public TaskStatusChanged(String taskId, String originalStatus, String newStatus) {
		this.occurredOn = DateProvider.now();
		this.taskId = taskId;
		this.originalStatus = originalStatus;
		this.newStatus = newStatus;
	}
	
	@Override
	public Date occurredOn() {
		return occurredOn;
	}
	
	public String taskId() {
		return taskId;
	}
	
	public String originalStatus() {
		return originalStatus;
	}

	public String newStatus() {
		return newStatus;
	}
}
