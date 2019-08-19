package ntut.csie.taskService.useCase.burndownChart.get;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ntut.csie.taskService.model.DateProvider;
import ntut.csie.taskService.model.DomainEvent;
import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.model.task.TaskAdded;
import ntut.csie.taskService.model.task.TaskRemainsChanged;
import ntut.csie.taskService.useCase.EventStore;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class GetBurndownChartPointsBySprintDatesAndTaskIdsUseCaseImpl implements GetBurndownChartPointsBySprintDatesAndTaskIdsUseCase, GetBurndownChartPointsBySprintDatesAndTaskIdsInput {
	private TaskRepository taskRepository;
	private EventStore eventStore;
	
	private List<String> sprintDates;
	private List<String> taskIds;
	
	public GetBurndownChartPointsBySprintDatesAndTaskIdsUseCaseImpl(TaskRepository taskRepository, EventStore eventStore) {
		this.taskRepository = taskRepository;
		this.eventStore = eventStore;
	}
	
	@Override
	public void execute(GetBurndownChartPointsBySprintDatesAndTaskIdsInput input, GetBurndownChartPointsBySprintDatesAndTaskIdsOutput output) {
		List<String> sprintDates = input.getSprintDates();
		List<String> taskIds = input.getTaskIds();
		List<Integer> realPoints = new ArrayList<>();
		List<Double> idealPoints = new ArrayList<>();
		if(taskIds.isEmpty() || sprintDates.isEmpty()) {
			output.setRealPoints(realPoints);
			output.setIdealPoints(idealPoints);
			return;
		}
		Map<String, Map<String, Integer>> taskProcessWithTaskId = null;
		try {
			taskProcessWithTaskId = buildTaskProcessWithTaskIdMap(taskIds, sprintDates);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for(String date : sprintDates) {
			Date sprintDate = null;
			Date today = DateProvider.now();
			try {
				sprintDate = simpleDateFormat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (sprintDate.after(today)) {
				realPoints.add(null);
			} else {
				int realPoint = 0;
				for(String taskId : taskProcessWithTaskId.keySet()) {
					Map<String, Integer> taskProcessMap = taskProcessWithTaskId.get(taskId);
					realPoint += taskProcessMap.get(date);
				}
				realPoints.add(realPoint);
			}
		}
		double idealPointsOfFirstDate = 0;
		if(realPoints.get(0) == null) {
			int realPointsOfFirstDate = 0;
			for(String taskId : taskIds) {
				Task task = taskRepository.getTaskById(taskId);
				realPointsOfFirstDate += task.getRemains();
			}
			realPoints.set(0, realPointsOfFirstDate);
		}
		idealPointsOfFirstDate = realPoints.get(0);
		idealPoints.add(idealPointsOfFirstDate);
		for(int i = 1; i < sprintDates.size() - 1; i++) {
			idealPoints.add(idealPoints.get(i - 1) - (idealPointsOfFirstDate / (sprintDates.size() - 1)));
		}
		idealPoints.add(0.0);
		output.setRealPoints(realPoints);
		output.setIdealPoints(idealPoints);
	}
	
	private Map<String, List<DomainEvent>> buildTaskEventWithTaskIdMap(List<String> taskIds){
		Map<String, List<DomainEvent>> eventListWithTaskId = new HashMap<>();
		for(DomainEvent domainEvent : eventStore.getAllEvent()) {
			String taskId = "";
			if(domainEvent instanceof TaskAdded) {
				TaskAdded taskAdded = (TaskAdded) domainEvent;
				taskId = taskAdded.taskId();
			} else if(domainEvent instanceof TaskRemainsChanged) {
				TaskRemainsChanged taskRemainsChanged = (TaskRemainsChanged) domainEvent;
				taskId = taskRemainsChanged.taskId();
			}
			if(taskIds.contains(taskId)) {
				List<DomainEvent> eventList;
				if(eventListWithTaskId.containsKey(taskId)) {
					eventList = eventListWithTaskId.get(taskId);
				} else {
					eventList = new ArrayList<>();
				}
				eventList.add(domainEvent);
				eventListWithTaskId.put(taskId, eventList);
			}
		}
		return eventListWithTaskId;
	}
	
	private Map<String, Map<String, Integer>> buildTaskProcessWithTaskIdMap(List<String> taskIds, List<String> sprintDates) throws ParseException {
		Map<String, Map<String, Integer>> taskProcessWithTaskId = new HashMap<>();
		Map<String, List<DomainEvent>> eventListWithTaskId = buildTaskEventWithTaskIdMap(taskIds);
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for(String taskId : eventListWithTaskId.keySet()) {
			for(DomainEvent domainEvent : eventListWithTaskId.get(taskId)) {
				String date = simpleDateFormat.format(domainEvent.occurredOn());
				if(domainEvent instanceof TaskAdded) {
					TaskAdded taskAdded = (TaskAdded) domainEvent;
					Task task = taskRepository.getTaskById(taskAdded.taskId());
					if(task == null) {
						continue;
					}
					Map<String, Integer> taskProcessMap = new HashMap<>();
					taskProcessMap.put(date, task.getEstimate());
					taskProcessWithTaskId.put(taskId, taskProcessMap);
				} else if(domainEvent instanceof TaskRemainsChanged) {
					TaskRemainsChanged taskRemainsChanged = (TaskRemainsChanged) domainEvent;
					Map<String, Integer> taskProcessMap = taskProcessWithTaskId.get(taskId);
					taskProcessMap.put(date, taskRemainsChanged.newRemains());
					taskProcessWithTaskId.put(taskId, taskProcessMap);
				}
			}
		}
		for(String taskId : eventListWithTaskId.keySet()) {
			Map<String, Integer> taskProcessMap = taskProcessWithTaskId.get(taskId);
			for(int i = 0; i < sprintDates.size(); i++) {
				if(!taskProcessMap.containsKey(sprintDates.get(i))) {
					if(i == 0) {
						taskProcessMap.put(sprintDates.get(i), 0);
					} else {
						int lastSprintDateRealPoints = taskProcessMap.get(sprintDates.get(i - 1));
						taskProcessMap.put(sprintDates.get(i), lastSprintDateRealPoints);
						
					}
					taskProcessWithTaskId.put(taskId, taskProcessMap);
				}
			}
		}
		return taskProcessWithTaskId;
	}

	@Override
	public List<String> getSprintDates() {
		return sprintDates;
	}

	@Override
	public void setSprintDates(List<String> sprintDates) {
		this.sprintDates = sprintDates;
	}

	@Override
	public List<String> getTaskIds() {
		return taskIds;
	}

	@Override
	public void setTaskIds(List<String> taskIds) {
		this.taskIds = taskIds;
	}
}
