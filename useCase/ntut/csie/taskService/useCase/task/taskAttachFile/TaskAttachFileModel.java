package ntut.csie.taskService.useCase.task.taskAttachFile;

public class TaskAttachFileModel {
	private String taskAttachFileId;
	private String name;
	private String taskId;
	private String createTime;

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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
