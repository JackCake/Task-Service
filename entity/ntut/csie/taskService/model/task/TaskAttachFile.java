package ntut.csie.taskService.model.task;

import java.util.Date;

public class TaskAttachFile {
	private String taskAttachFileId;
	private String name;
	private String path;
	private String taskId;
	private Date createTime;
	
	public TaskAttachFile() {}
	
	public TaskAttachFile(String taskAttachFileId, String name, String path, String taskId, Date createTime) {
		this.taskAttachFileId = taskAttachFileId;
		this.name = name;
		this.path = path;
		this.taskId = taskId;
		this.createTime = createTime;
	}

	public String getTaskAttachFileId() {
		return taskAttachFileId;
	}

	public void setTaskAttachFileId(String taskAttachFileId) {
		this.taskAttachFileId = taskAttachFileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
