package ntut.csie.taskService.useCase.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ntut.csie.taskService.model.task.TaskBehavior;
import ntut.csie.taskService.model.task.TaskStatusChanged;

public class ConvertTaskStatusChangedEventToDTO {
	public static HistoryModel transform(TaskStatusChanged taskStatusChanged) {
		HistoryModel dto = new HistoryModel();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String occurredOn = simpleDateFormat.format(taskStatusChanged.occurredOn());
		dto.setOccurredOn(occurredOn);
		dto.setBehavior(TaskBehavior.changeStatus);
		dto.setDescription("\"" + taskStatusChanged.originalStatus() + "\" → \"" + taskStatusChanged.newStatus() + "\"");
		return dto;
	}
}
