package ntut.csie.taskService.gateways.repository.task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ntut.csie.taskService.gateways.repository.task.taskAttachFile.TaskAttachFileData;
import ntut.csie.taskService.model.task.Task;

public class TaskMapper {
	public Task transformToTask(TaskData data) throws ParseException {
		Task task = new Task();
		task.setTaskId(data.getTaskId());
		task.setOrderId(data.getOrderId());
		task.setDescription(data.getDescription());
		task.setHandlerId(data.getHandlerId());
		task.setStatus(data.getStatus());
		task.setEstimate(data.getEstimate());
		task.setRemains(data.getRemains());
		task.setNotes(data.getNotes());
		task.setBacklogItemId(data.getBacklogItemId());
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(TaskAttachFileData taskAttachFileData : data.getTaskAttachFileDatas()) {
			Date createTime = simpleDateFormat.parse(taskAttachFileData.getCreateTime());
			task.addTaskAttachFile(taskAttachFileData.getTaskAttachFileId(), taskAttachFileData.getName(), taskAttachFileData.getPath(), createTime);
		}
		return task;
	}
	
	public TaskData transformToTaskData(Task task) {
		TaskData data = new TaskData();
		data.setTaskId(task.getTaskId());
		data.setOrderId(task.getOrderId());
		data.setDescription(task.getDescription());
		data.setHandlerId(task.getHandlerId());
		data.setStatus(task.getStatus());
		data.setEstimate(task.getEstimate());
		data.setRemains(task.getRemains());
		data.setNotes(task.getNotes());
		data.setBacklogItemId(task.getBacklogItemId());
		return data;
	}
}
