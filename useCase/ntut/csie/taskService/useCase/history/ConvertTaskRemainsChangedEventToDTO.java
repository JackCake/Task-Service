package ntut.csie.taskService.useCase.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ntut.csie.taskService.model.task.TaskBehavior;
import ntut.csie.taskService.model.task.TaskRemainsChanged;

public class ConvertTaskRemainsChangedEventToDTO {
	public static HistoryModel transform(TaskRemainsChanged taskRemainsChanged) {
		HistoryModel dto = new HistoryModel();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String occurredOn = simpleDateFormat.format(taskRemainsChanged.occurredOn());
		dto.setOccurredOn(occurredOn);
		dto.setBehavior(TaskBehavior.changeRemains);
		dto.setDescription("\"" + taskRemainsChanged.originalRemains() + "\" → \"" + taskRemainsChanged.newRemains() + "\"");
		return dto;
	}
}
