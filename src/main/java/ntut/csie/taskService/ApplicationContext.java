package ntut.csie.taskService;

import ntut.csie.taskService.gateways.repository.event.MySqlEventStoreImpl;
import ntut.csie.taskService.gateways.repository.task.MySqlTaskRepositoryImpl;
import ntut.csie.taskService.useCase.EventStore;
import ntut.csie.taskService.useCase.burndownChart.get.GetBurndownChartPointsBySprintDatesAndTaskIdsUseCase;
import ntut.csie.taskService.useCase.burndownChart.get.GetBurndownChartPointsBySprintDatesAndTaskIdsUseCaseImpl;
import ntut.csie.taskService.useCase.history.get.GetHistoriesByTaskIdUseCase;
import ntut.csie.taskService.useCase.history.get.GetHistoriesByTaskIdUseCaseImpl;
import ntut.csie.taskService.useCase.task.TaskRepository;
import ntut.csie.taskService.useCase.task.add.AddTaskUseCase;
import ntut.csie.taskService.useCase.task.add.AddTaskUseCaseImpl;
import ntut.csie.taskService.useCase.task.delete.DeleteTaskUseCase;
import ntut.csie.taskService.useCase.task.delete.DeleteTaskUseCaseImpl;
import ntut.csie.taskService.useCase.task.edit.EditTaskUseCase;
import ntut.csie.taskService.useCase.task.edit.EditTaskUseCaseImpl;
import ntut.csie.taskService.useCase.task.get.GetTasksByBacklogItemIdUseCase;
import ntut.csie.taskService.useCase.task.get.GetTasksByBacklogItemIdUseCaseImpl;
import ntut.csie.taskService.useCase.task.move.MoveTaskUseCase;
import ntut.csie.taskService.useCase.task.move.MoveTaskUseCaseImpl;

public class ApplicationContext {
	private static ApplicationContext instance = null;
	
	private static TaskRepository taskRepository = null;
	private static EventStore eventStore = null;
	
	private AddTaskUseCase addTaskUseCase;
	private GetTasksByBacklogItemIdUseCase getTasksByBacklogItemIdUseCase;
	private EditTaskUseCase editTaskUseCase;
	private DeleteTaskUseCase deleteTaskUseCase;
	private MoveTaskUseCase moveTaskUseCase;
	private GetHistoriesByTaskIdUseCase getHistoriesByTaskIdUseCase;
	private GetBurndownChartPointsBySprintDatesAndTaskIdsUseCase getBurndownChartPointsBySprintDatesAndTaskIdsUseCase;
	
	private ApplicationContext() {}
	
	public static synchronized ApplicationContext getInstance() {
		if(instance == null){
			instance = new ApplicationContext();
		}
		return instance;
	}
	
	public TaskRepository newTaskRepository() {
		if(taskRepository == null) {
			taskRepository = new MySqlTaskRepositoryImpl();
		}
		return taskRepository;
	}
	
	public EventStore newEventStore() {
		if(eventStore == null) {
			eventStore = new MySqlEventStoreImpl();
		}
		return eventStore;
	}
	
	public AddTaskUseCase newAddTaskUseCase() {
		addTaskUseCase = new AddTaskUseCaseImpl(newTaskRepository());
		return addTaskUseCase;
	}
	
	public GetTasksByBacklogItemIdUseCase newGetTasksByBacklogItemIdUseCase() {
		getTasksByBacklogItemIdUseCase = new GetTasksByBacklogItemIdUseCaseImpl(newTaskRepository());
		return getTasksByBacklogItemIdUseCase;
	}
	
	public EditTaskUseCase newEditTaskUseCase() {
		editTaskUseCase = new EditTaskUseCaseImpl(newTaskRepository());
		return editTaskUseCase;
	}
	
	public DeleteTaskUseCase newDeleteTaskUseCase() {
		deleteTaskUseCase = new DeleteTaskUseCaseImpl(newTaskRepository());
		return deleteTaskUseCase;
	}
	
	public MoveTaskUseCase newMoveTaskUseCase() {
		moveTaskUseCase = new MoveTaskUseCaseImpl(newTaskRepository());
		return moveTaskUseCase;
	}
	
	public GetHistoriesByTaskIdUseCase newGetHistoriesByTaskIdUseCase() {
		getHistoriesByTaskIdUseCase = new GetHistoriesByTaskIdUseCaseImpl(newEventStore());
		return getHistoriesByTaskIdUseCase;
	}
	
	public GetBurndownChartPointsBySprintDatesAndTaskIdsUseCase newGetBurndownChartPointsBySprintDatesAndTaskIdsUseCase() {
		getBurndownChartPointsBySprintDatesAndTaskIdsUseCase = new GetBurndownChartPointsBySprintDatesAndTaskIdsUseCaseImpl(newTaskRepository(), newEventStore());
		return getBurndownChartPointsBySprintDatesAndTaskIdsUseCase;
	}
}
