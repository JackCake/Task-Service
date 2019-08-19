package ntut.csie.taskService.model.task;

import java.util.Date;

import ntut.csie.taskService.model.DateProvider;
import ntut.csie.taskService.model.DomainEvent;

public class TaskDescriptionEdited implements DomainEvent {
	private Date occurredOn;
	private String taskId;
	private String originalDescription;
	private String newDescription;

	public TaskDescriptionEdited(String taskId, String originalDescription, String newDescription) {
		this.occurredOn = DateProvider.now();
		this.taskId = taskId;
		this.originalDescription = originalDescription;
		this.newDescription = newDescription;
	}
	
	@Override
	public Date occurredOn() {
		return occurredOn;
	}
	
	public String taskId() {
		return taskId;
	}
	
	public String origianlDescription() {
		return originalDescription;
	}

	public String newDescription() {
		return newDescription;
	}
}
