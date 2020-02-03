package ntut.csie.taskService.model.task;

import java.util.Date;
import java.util.UUID;

import ntut.csie.taskService.model.DateProvider;

public class TaskAttachFileBuilder {
	private String taskAttachFileId;
	private String name;
	private String path;
	private String taskId;
	private Date createTime;
	
	public static TaskAttachFileBuilder newInstance() {
		return new TaskAttachFileBuilder();
	}
	
	public TaskAttachFileBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	public TaskAttachFileBuilder path(String path) {
		this.path = path;
		return this;
	}
	
	public TaskAttachFileBuilder taskId(String taskId) {
		this.taskId = taskId;
		return this;
	}
	
	public TaskAttachFile build() throws Exception {
		String exceptionMessage = "";
		if(name == null || name.isEmpty()) {
			exceptionMessage += "The name of the task attach file should be required!\n";
		}
		if(!exceptionMessage.isEmpty()) {
			throw new Exception(exceptionMessage);
		}
		
		this.taskAttachFileId = UUID.randomUUID().toString();
		this.createTime = DateProvider.now();
		TaskAttachFile taskAttachFile = new TaskAttachFile(taskAttachFileId, name, path, taskId, createTime);
		return taskAttachFile;
	}
}
