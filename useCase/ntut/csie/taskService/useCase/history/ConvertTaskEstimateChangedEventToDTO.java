package ntut.csie.taskService.useCase.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ntut.csie.taskService.model.task.TaskBehavior;
import ntut.csie.taskService.model.task.TaskEstimateChanged;

public class ConvertTaskEstimateChangedEventToDTO {
	public static HistoryModel transform(TaskEstimateChanged taskEstimateChanged) {
		HistoryModel dto = new HistoryModel();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String occurredOn = simpleDateFormat.format(taskEstimateChanged.occurredOn());
		dto.setOccurredOn(occurredOn);
		dto.setBehavior(TaskBehavior.changeEstimate);
		dto.setDescription("\"" + taskEstimateChanged.originalEstimate() + "\" → \"" + taskEstimateChanged.newEstimate() + "\"");
		return dto;
	}
}
