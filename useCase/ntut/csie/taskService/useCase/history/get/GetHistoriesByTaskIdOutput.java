package ntut.csie.taskService.useCase.history.get;

import java.util.List;

import ntut.csie.taskService.useCase.Output;
import ntut.csie.taskService.useCase.history.HistoryModel;

public interface GetHistoriesByTaskIdOutput extends Output{
	public List<HistoryModel> getHistoryList();
	
	public void setHistoryList(List<HistoryModel> historyList); 
}
