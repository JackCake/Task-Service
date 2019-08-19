package ntut.csie.taskService.model.task;

import java.util.Date;

import ntut.csie.taskService.model.DateProvider;
import ntut.csie.taskService.model.DomainEvent;

public class TaskNotesEdited implements DomainEvent {
	private Date occurredOn;
	private String taskId;
	private String originalNotes;
	private String newNotes;

	public TaskNotesEdited(String taskId, String originalNotes, String newNotes) {
		this.occurredOn = DateProvider.now();
		this.taskId = taskId;
		this.originalNotes = originalNotes;
		this.newNotes = newNotes;
	}
	
	@Override
	public Date occurredOn() {
		return occurredOn;
	}
	
	public String taskId() {
		return taskId;
	}
	
	public String originalNotes() {
		return originalNotes;
	}

	public String newNotes() {
		return newNotes;
	}
}
