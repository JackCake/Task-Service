package ntut.csie.taskService.model.task;

import java.util.Date;

import ntut.csie.taskService.model.DateProvider;
import ntut.csie.taskService.model.DomainEvent;

public class TaskEstimateChanged implements DomainEvent {
	private Date occurredOn;
	private String taskId;
	private int originalEstimate;
	private int newEstimate;

	public TaskEstimateChanged(String taskId, int originalEstimate, int newEstimate) {
		this.occurredOn = DateProvider.now();
		this.taskId = taskId;
		this.originalEstimate = originalEstimate;
		this.newEstimate = newEstimate;
	}
	
	@Override
	public Date occurredOn() {
		return occurredOn;
	}
	
	public String taskId() {
		return taskId;
	}
	
	public int originalEstimate() {
		return originalEstimate;
	}

	public int newEstimate() {
		return newEstimate;
	}
}
