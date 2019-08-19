package ntut.csie.taskService.controller.burndownChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ntut.csie.taskService.ApplicationContext;
import ntut.csie.taskService.useCase.burndownChart.get.GetBurndownChartPointsBySprintDatesAndTaskIdsInput;
import ntut.csie.taskService.useCase.burndownChart.get.GetBurndownChartPointsBySprintDatesAndTaskIdsOutput;
import ntut.csie.taskService.useCase.burndownChart.get.GetBurndownChartPointsBySprintDatesAndTaskIdsUseCase;

@Path("sprints/{sprint_dates}/tasks/{task_ids}/burndown_chart_points")
public class GetBurndownChartPointsBySprintDatesAndTaskIdsRestfulAPI implements GetBurndownChartPointsBySprintDatesAndTaskIdsOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private GetBurndownChartPointsBySprintDatesAndTaskIdsUseCase getBurndownChartPointsBySprintDatesAndTaskIdsUseCase = applicationContext.newGetBurndownChartPointsBySprintDatesAndTaskIdsUseCase();
	
	private List<Integer> realPoints;
	private List<Double> idealPoints;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public GetBurndownChartPointsBySprintDatesAndTaskIdsOutput getBurndownChartPointsBySprintDatesAndTaskIds(
			@PathParam("sprint_dates") String sprintDates, 
			@PathParam("task_ids") String taskIds) {
		GetBurndownChartPointsBySprintDatesAndTaskIdsOutput output = this;
		
		GetBurndownChartPointsBySprintDatesAndTaskIdsInput input = (GetBurndownChartPointsBySprintDatesAndTaskIdsInput) getBurndownChartPointsBySprintDatesAndTaskIdsUseCase;
		List<String> sprintDateList;
		List<String> taskIdList;
		if(sprintDates.isEmpty()) {
			sprintDateList = new ArrayList<>();
		} else {
			sprintDateList = new ArrayList<>(Arrays.asList(sprintDates.split(",")));
		}
		if(taskIds.isEmpty()) {
			taskIdList = new ArrayList<>();
		} else {
			taskIdList = new ArrayList<>(Arrays.asList(taskIds.split(",")));
		}
		input.setSprintDates(sprintDateList);
		input.setTaskIds(taskIdList);
		
		getBurndownChartPointsBySprintDatesAndTaskIdsUseCase.execute(input, output);
		
		return output;
	}
	
	@Override
	public List<Integer> getRealPoints() {
		return realPoints;
	}

	@Override
	public void setRealPoints(List<Integer> realPoints) {
		this.realPoints = realPoints;
	}

	@Override
	public List<Double> getIdealPoints() {
		return idealPoints;
	}

	@Override
	public void setIdealPoints(List<Double> idealPoints) {
		this.idealPoints = idealPoints;
	}
}
