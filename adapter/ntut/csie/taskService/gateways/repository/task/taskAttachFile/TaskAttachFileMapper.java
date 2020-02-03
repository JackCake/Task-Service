package ntut.csie.taskService.gateways.repository.task.taskAttachFile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ntut.csie.taskService.model.task.TaskAttachFile;

public class TaskAttachFileMapper {
	public TaskAttachFile transformToTaskAttachFile(TaskAttachFileData data) throws ParseException {
		TaskAttachFile taskAttachFile = new TaskAttachFile();
		taskAttachFile.setTaskAttachFileId(data.getTaskAttachFileId());
		taskAttachFile.setName(data.getName());
		taskAttachFile.setPath(data.getPath());
		taskAttachFile.setTaskId(data.getTaskId());
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date createTime = simpleDateFormat.parse(data.getCreateTime());
		taskAttachFile.setCreateTime(createTime);
		return taskAttachFile;
	}
	
	public TaskAttachFileData transformToTaskAttachFileData(TaskAttachFile taskAttachFile) {
		TaskAttachFileData data = new TaskAttachFileData();
		data.setTaskAttachFileId(taskAttachFile.getTaskAttachFileId());
		data.setName(taskAttachFile.getName());
		data.setPath(taskAttachFile.getPath());
		data.setTaskId(taskAttachFile.getTaskId());
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = simpleDateFormat.format(taskAttachFile.getCreateTime());
		data.setCreateTime(createTime);
		return data;
	}
}
