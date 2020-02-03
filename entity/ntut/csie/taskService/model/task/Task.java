package ntut.csie.taskService.model.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ntut.csie.taskService.model.DomainEventPublisher;

public class Task {
	private String taskId;
	private int orderId;
	private String description;
	private String handlerId;
	private String status;
	private int estimate;
	private int remains;
	private String notes;
	private String backlogItemId;
	private List<TaskAttachFile> taskAttachFiles;
	
	public Task() {
		taskAttachFiles = new ArrayList<>();
	}
	
	public Task(String taskId, String description, String backlogItemId) {
		this.setTaskId(taskId);
		this.setDescription(description);
		this.setBacklogItemId(backlogItemId);
		taskAttachFiles = new ArrayList<>();
		DomainEventPublisher.getInstance().publish(new TaskAdded(taskId));
	}
	
	public void editDescription(String description) {
		if(this.description.equals(description)) {
			return;
		}
		DomainEventPublisher.getInstance().publish(new TaskDescriptionEdited(
				taskId, this.description, description));
		this.setDescription(description);
	}
	
	public void changeStatus(String status) {
		if(this.status.equals(status)) {
			return;
		}
		if(this.status.equals(TaskStatus.doing) && status.equals(TaskStatus.done)) {
			this.changeRemains(0);
		}else if(this.status.equals(TaskStatus.done) && status.equals(TaskStatus.doing)) {
			this.changeRemains(estimate);
		}
		DomainEventPublisher.getInstance().publish(new TaskStatusChanged(
				taskId, this.status, status));
		this.setStatus(status);
	}
	
	public void changeEstimate(int estimate) {
		if(this.estimate == estimate) {
			return;
		}
		DomainEventPublisher.getInstance().publish(new TaskEstimateChanged(
				taskId, this.estimate, estimate));
		this.setEstimate(estimate);
	}
	
	public void changeRemains(int remains) {
		if(this.remains == remains) {
			return;
		}
		DomainEventPublisher.getInstance().publish(new TaskRemainsChanged(
				taskId, this.remains, remains));
		this.setRemains(remains);
	}
	
	public void editNotes(String notes) {
		if(this.notes.equals(notes)) {
			return;
		}
		DomainEventPublisher.getInstance().publish(new TaskNotesEdited(
				taskId, this.notes, notes));
		this.setNotes(notes);
	}
	
	public void markAsRemoved() {
		DomainEventPublisher.getInstance().publish(new TaskDeleted(
				taskId, taskAttachFiles, backlogItemId));
	}
	
	public void uploadTaskAttachFile(String name, String path) throws Exception {
		TaskAttachFile taskAttachFile = TaskAttachFileBuilder.newInstance()
				.name(name)
				.path(path)
				.taskId(taskId)
				.build();
		taskAttachFiles.add(taskAttachFile);
	}
	
	public void addTaskAttachFile(String taskAttachFileId, String name, String path, Date createTime) {
		TaskAttachFile taskAttachFile = new TaskAttachFile();
		taskAttachFile.setTaskAttachFileId(taskAttachFileId);
		taskAttachFile.setName(name);
		taskAttachFile.setPath(path);
		taskAttachFile.setTaskId(taskId);
		taskAttachFile.setCreateTime(createTime);
		taskAttachFiles.add(taskAttachFile);
	}
	
	public void removeTaskAttachFile(String taskAttachFileId) throws Exception {
		TaskAttachFile taskAttachFile = getTaskAttachFile(taskAttachFileId);
		taskAttachFiles.remove(taskAttachFile);
	}
	
	public TaskAttachFile getTaskAttachFile(String taskAttachFileId) throws Exception {
		for(TaskAttachFile taskAttachFile : taskAttachFiles) {
			if(taskAttachFile.getTaskAttachFileId().equals(taskAttachFileId)) {
				return taskAttachFile;
			}
		}
		throw new Exception("Sorry, the attach file of the task is not exist!");
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(String handlerId) {
		this.handlerId = handlerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getEstimate() {
		return estimate;
	}

	public void setEstimate(int estimate) {
		this.estimate = estimate;
	}
	
	public int getRemains() {
		return remains;
	}

	public void setRemains(int remains) {
		this.remains = remains;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getBacklogItemId() {
		return backlogItemId;
	}

	public void setBacklogItemId(String backlogItemId) {
		this.backlogItemId = backlogItemId;
	}

	public List<TaskAttachFile> getTaskAttachFiles() {
		return taskAttachFiles;
	}

	public void setTaskAttachFiles(List<TaskAttachFile> taskAttachFiles) {
		this.taskAttachFiles = taskAttachFiles;
	}
}
