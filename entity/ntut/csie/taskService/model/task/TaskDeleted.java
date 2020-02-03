package ntut.csie.taskService.model.task;

import java.util.Date;
import java.util.List;

import ntut.csie.taskService.model.DateProvider;
import ntut.csie.taskService.model.DomainEvent;

public class TaskDeleted implements DomainEvent {
	private Date occurredOn;
	private String taskId;
	private List<TaskAttachFile> taskAttachFiles;
	private String backlogItemId;

	public TaskDeleted(String taskId, List<TaskAttachFile> taskAttachFiles, String backlogItemId) {
		this.occurredOn = DateProvider.now();
		this.taskId = taskId;
		this.taskAttachFiles = taskAttachFiles;
		this.backlogItemId = backlogItemId;
	}
	
	@Override
	public Date occurredOn() {
		return occurredOn;
	}
	
	public String taskId() {
		return taskId;
	}
	
	public List<TaskAttachFile> taskAttachFiles(){
		return taskAttachFiles;
	}
	
	public String backlogItemId() {
		return backlogItemId;
	}
}
