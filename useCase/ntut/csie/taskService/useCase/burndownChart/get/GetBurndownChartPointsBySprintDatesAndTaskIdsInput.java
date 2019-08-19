package ntut.csie.taskService.useCase.burndownChart.get;

import java.util.List;

import ntut.csie.taskService.useCase.Input;

public interface GetBurndownChartPointsBySprintDatesAndTaskIdsInput extends Input{
	public List<String> getSprintDates();
	
	public void setSprintDates(List<String> sprintDates);
	
	public List<String> getTaskIds();
	
	public void setTaskIds(List<String> taskIds);
}
