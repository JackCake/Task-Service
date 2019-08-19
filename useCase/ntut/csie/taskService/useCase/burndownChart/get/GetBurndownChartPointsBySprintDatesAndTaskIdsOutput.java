package ntut.csie.taskService.useCase.burndownChart.get;

import java.util.List;

import ntut.csie.taskService.useCase.Output;

public interface GetBurndownChartPointsBySprintDatesAndTaskIdsOutput extends Output{
	public List<Integer> getRealPoints();
	
	public void setRealPoints(List<Integer> realPoints);
	
	public List<Double> getIdealPoints();
	
	public void setIdealPoints(List<Double> idealPoints);
}
