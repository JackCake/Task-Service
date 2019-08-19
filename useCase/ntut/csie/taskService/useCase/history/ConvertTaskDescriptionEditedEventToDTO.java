package ntut.csie.taskService.useCase.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ntut.csie.taskService.model.task.TaskBehavior;
import ntut.csie.taskService.model.task.TaskDescriptionEdited;

public class ConvertTaskDescriptionEditedEventToDTO {
	public static HistoryModel transform(TaskDescriptionEdited taskDescriptionEdited) {
		HistoryModel dto = new HistoryModel();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String occurredOn = simpleDateFormat.format(taskDescriptionEdited.occurredOn());
		dto.setOccurredOn(occurredOn);
		dto.setBehavior(TaskBehavior.editDescription);
		dto.setDescription("\"" + taskDescriptionEdited.origianlDescription() + "\" → \"" + taskDescriptionEdited.newDescription() + "\"");
		return dto;
	}
}
