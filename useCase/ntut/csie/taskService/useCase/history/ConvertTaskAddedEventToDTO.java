package ntut.csie.taskService.useCase.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ntut.csie.taskService.model.task.TaskAdded;
import ntut.csie.taskService.model.task.TaskBehavior;

public class ConvertTaskAddedEventToDTO {
	public static HistoryModel transform(TaskAdded taskAdded) {
		HistoryModel dto = new HistoryModel();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String occurredOn = simpleDateFormat.format(taskAdded.occurredOn());
		dto.setOccurredOn(occurredOn);
		dto.setBehavior(TaskBehavior.create);
		dto.setDescription("Create Task");
		return dto;
	}
}
