package ntut.csie.taskService.controller.history;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ntut.csie.taskService.ApplicationContext;
import ntut.csie.taskService.useCase.history.HistoryModel;
import ntut.csie.taskService.useCase.history.get.GetHistoriesByTaskIdInput;
import ntut.csie.taskService.useCase.history.get.GetHistoriesByTaskIdOutput;
import ntut.csie.taskService.useCase.history.get.GetHistoriesByTaskIdUseCase;

@Path("/tasks/{task_id}/histories")
public class GetHistoriesByTaskIdRestfulAPI implements GetHistoriesByTaskIdOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private GetHistoriesByTaskIdUseCase getHistoriesByTaskIdUseCase = applicationContext.newGetHistoriesByTaskIdUseCase();
	
	private List<HistoryModel> historyList;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public GetHistoriesByTaskIdOutput getHistoriesByTaskId(@PathParam("task_id") String taskId) {
		GetHistoriesByTaskIdOutput output = this;
		
		GetHistoriesByTaskIdInput input = (GetHistoriesByTaskIdInput) getHistoriesByTaskIdUseCase;
		input.setTaskId(taskId);
		
		getHistoriesByTaskIdUseCase.execute(input, output);
		
		return output;
	}
	
	@Override
	public List<HistoryModel> getHistoryList() {
		return historyList;
	}

	@Override
	public void setHistoryList(List<HistoryModel> historyList) {
		this.historyList = historyList;
	}
}
