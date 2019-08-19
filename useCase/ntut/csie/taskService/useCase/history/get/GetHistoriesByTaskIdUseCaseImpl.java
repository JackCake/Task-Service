package ntut.csie.taskService.useCase.history.get;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.taskService.model.DomainEvent;
import ntut.csie.taskService.model.task.TaskAdded;
import ntut.csie.taskService.model.task.TaskDescriptionEdited;
import ntut.csie.taskService.model.task.TaskEstimateChanged;
import ntut.csie.taskService.model.task.TaskNotesEdited;
import ntut.csie.taskService.model.task.TaskRemainsChanged;
import ntut.csie.taskService.model.task.TaskStatusChanged;
import ntut.csie.taskService.useCase.EventStore;
import ntut.csie.taskService.useCase.history.ConvertTaskAddedEventToDTO;
import ntut.csie.taskService.useCase.history.ConvertTaskDescriptionEditedEventToDTO;
import ntut.csie.taskService.useCase.history.ConvertTaskEstimateChangedEventToDTO;
import ntut.csie.taskService.useCase.history.ConvertTaskNotesEditedEventToDTO;
import ntut.csie.taskService.useCase.history.ConvertTaskRemainsChangedEventToDTO;
import ntut.csie.taskService.useCase.history.ConvertTaskStatusChangedEventToDTO;
import ntut.csie.taskService.useCase.history.HistoryModel;

public class GetHistoriesByTaskIdUseCaseImpl implements GetHistoriesByTaskIdUseCase, GetHistoriesByTaskIdInput {
	private EventStore eventStore;
	
	private String taskId;
	
	public GetHistoriesByTaskIdUseCaseImpl(EventStore eventStore) {
		this.eventStore = eventStore;
	}
	
	@Override
	public void execute(GetHistoriesByTaskIdInput input, GetHistoriesByTaskIdOutput output) {
		String taskId = input.getTaskId();
		List<HistoryModel> historyList = new ArrayList<>();
		for(DomainEvent domainEvent : eventStore.getAllEvent()) {
			if(domainEvent instanceof TaskAdded) {
				TaskAdded taskAdded = (TaskAdded) domainEvent;
				if(taskAdded.taskId().equals(taskId)) {
					historyList.add(ConvertTaskAddedEventToDTO.transform(taskAdded));
				}
			} else if(domainEvent instanceof TaskDescriptionEdited) {
				TaskDescriptionEdited taskDescriptionEdited = (TaskDescriptionEdited) domainEvent;
				if(taskDescriptionEdited.taskId().equals(taskId)) {
					historyList.add(ConvertTaskDescriptionEditedEventToDTO.transform(taskDescriptionEdited));
				}
			} else if(domainEvent instanceof TaskStatusChanged) {
				TaskStatusChanged taskStatusChanged = (TaskStatusChanged) domainEvent;
				if(taskStatusChanged.taskId().equals(taskId)) {
					historyList.add(ConvertTaskStatusChangedEventToDTO.transform(taskStatusChanged));
				}
			} else if(domainEvent instanceof TaskEstimateChanged) {
				TaskEstimateChanged taskEstimateChanged = (TaskEstimateChanged) domainEvent;
				if(taskEstimateChanged.taskId().equals(taskId)) {
					historyList.add(ConvertTaskEstimateChangedEventToDTO.transform(taskEstimateChanged));
				}
			} else if(domainEvent instanceof TaskRemainsChanged) {
				TaskRemainsChanged taskRemainsChanged = (TaskRemainsChanged) domainEvent;
				if(taskRemainsChanged.taskId().equals(taskId)) {
					historyList.add(ConvertTaskRemainsChangedEventToDTO.transform(taskRemainsChanged));
				}
			} else if(domainEvent instanceof TaskNotesEdited) {
				TaskNotesEdited taskNotesEdited = (TaskNotesEdited) domainEvent;
				if(taskNotesEdited.taskId().equals(taskId)) {
					historyList.add(ConvertTaskNotesEditedEventToDTO.transform(taskNotesEdited));
				}
			}
		}
		output.setHistoryList(historyList);
	}

	@Override
	public String getTaskId() {
		return taskId;
	}

	@Override
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
