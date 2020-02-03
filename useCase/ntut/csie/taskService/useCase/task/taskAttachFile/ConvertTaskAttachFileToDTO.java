package ntut.csie.taskService.useCase.task.taskAttachFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ntut.csie.taskService.model.task.TaskAttachFile;

public class ConvertTaskAttachFileToDTO {
	public static TaskAttachFileModel transform(TaskAttachFile taskAttachFile) {
		TaskAttachFileModel dto = new TaskAttachFileModel();
		dto.setTaskAttachFileId(taskAttachFile.getTaskAttachFileId());
		dto.setName(taskAttachFile.getName());
		dto.setTaskId(taskAttachFile.getTaskId());
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String createTime = simpleDateFormat.format(taskAttachFile.getCreateTime());
		dto.setCreateTime(createTime);
		return dto;
	}
}
