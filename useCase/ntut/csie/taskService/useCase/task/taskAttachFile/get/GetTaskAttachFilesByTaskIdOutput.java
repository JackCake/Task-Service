package ntut.csie.taskService.useCase.task.taskAttachFile.get;

import java.util.List;

import ntut.csie.taskService.useCase.Output;
import ntut.csie.taskService.useCase.task.taskAttachFile.TaskAttachFileModel;


public interface GetTaskAttachFilesByTaskIdOutput extends Output {
	public List<TaskAttachFileModel> getTaskAttachFileList();
	
	public void setTaskAttachFileList(List<TaskAttachFileModel> taskAttachFileList);
}
