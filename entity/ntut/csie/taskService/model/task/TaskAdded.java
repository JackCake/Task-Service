package ntut.csie.taskService.model.task;

import java.util.Date;

import ntut.csie.taskService.model.DateProvider;
import ntut.csie.taskService.model.DomainEvent;

public class TaskAdded implements DomainEvent {
	private Date occurredOn;
	private String taskId;

	public TaskAdded(String taskId) {
		this.occurredOn = DateProvider.now();
		this.taskId = taskId;
	}
	
	@Override
	public Date occurredOn() {
		return occurredOn;
	}
	
	public String taskId() {
		return taskId;
	}
}
