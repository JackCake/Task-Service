package ntut.csie.taskService.model.task;

import java.util.Date;

import ntut.csie.taskService.model.DateProvider;
import ntut.csie.taskService.model.DomainEvent;

public class TaskRemainsChanged implements DomainEvent {
	private Date occurredOn;
	private String taskId;
	private int originalRemains;
	private int newRemains;

	public TaskRemainsChanged(String taskId, int originalRemains, int newRemains) {
		this.occurredOn = DateProvider.now();
		this.taskId = taskId;
		this.originalRemains = originalRemains;
		this.newRemains = newRemains;
	}
	
	@Override
	public Date occurredOn() {
		return occurredOn;
	}
	
	public String taskId() {
		return taskId;
	}
	
	public int originalRemains() {
		return originalRemains;
	}

	public int newRemains() {
		return newRemains;
	}
}
