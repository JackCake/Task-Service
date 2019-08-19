package ntut.csie.taskService.useCase.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ntut.csie.taskService.model.task.TaskBehavior;
import ntut.csie.taskService.model.task.TaskNotesEdited;

public class ConvertTaskNotesEditedEventToDTO {
	public static HistoryModel transform(TaskNotesEdited taskNotesEdited) {
		HistoryModel dto = new HistoryModel();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String occurredOn = simpleDateFormat.format(taskNotesEdited.occurredOn());
		dto.setOccurredOn(occurredOn);
		dto.setBehavior(TaskBehavior.editNotes);
		dto.setDescription("\"" + taskNotesEdited.originalNotes() + "\" → \"" + taskNotesEdited.newNotes() + "\"");
		return dto;
	}
}
