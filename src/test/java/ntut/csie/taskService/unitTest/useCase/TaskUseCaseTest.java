package ntut.csie.taskService.unitTest.useCase;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ntut.csie.taskService.controller.burndownChart.GetBurndownChartPointsBySprintDatesAndTaskIdsRestfulAPI;
import ntut.csie.taskService.controller.history.GetHistoriesByTaskIdRestfulAPI;
import ntut.csie.taskService.controller.task.AddTaskRestfulAPI;
import ntut.csie.taskService.controller.task.DeleteTaskRestfulAPI;
import ntut.csie.taskService.controller.task.EditTaskRestfulAPI;
import ntut.csie.taskService.controller.task.GetTasksByBacklogItemIdRestfulAPI;
import ntut.csie.taskService.controller.task.MoveTaskRestfulAPI;
import ntut.csie.taskService.model.DateProvider;
import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.model.task.TaskStatus;
import ntut.csie.taskService.unitTest.repository.FakeEventStore;
import ntut.csie.taskService.unitTest.repository.FakeTaskRepository;
import ntut.csie.taskService.useCase.DomainEventListener;
import ntut.csie.taskService.useCase.burndownChart.get.GetBurndownChartPointsBySprintDatesAndTaskIdsInput;
import ntut.csie.taskService.useCase.burndownChart.get.GetBurndownChartPointsBySprintDatesAndTaskIdsOutput;
import ntut.csie.taskService.useCase.burndownChart.get.GetBurndownChartPointsBySprintDatesAndTaskIdsUseCase;
import ntut.csie.taskService.useCase.burndownChart.get.GetBurndownChartPointsBySprintDatesAndTaskIdsUseCaseImpl;
import ntut.csie.taskService.useCase.history.get.GetHistoriesByTaskIdInput;
import ntut.csie.taskService.useCase.history.get.GetHistoriesByTaskIdOutput;
import ntut.csie.taskService.useCase.history.get.GetHistoriesByTaskIdUseCase;
import ntut.csie.taskService.useCase.history.get.GetHistoriesByTaskIdUseCaseImpl;
import ntut.csie.taskService.useCase.task.TaskModel;
import ntut.csie.taskService.useCase.task.add.AddTaskInput;
import ntut.csie.taskService.useCase.task.add.AddTaskOutput;
import ntut.csie.taskService.useCase.task.add.AddTaskUseCase;
import ntut.csie.taskService.useCase.task.add.AddTaskUseCaseImpl;
import ntut.csie.taskService.useCase.task.delete.DeleteTaskInput;
import ntut.csie.taskService.useCase.task.delete.DeleteTaskOutput;
import ntut.csie.taskService.useCase.task.delete.DeleteTaskUseCase;
import ntut.csie.taskService.useCase.task.delete.DeleteTaskUseCaseImpl;
import ntut.csie.taskService.useCase.task.edit.EditTaskInput;
import ntut.csie.taskService.useCase.task.edit.EditTaskOutput;
import ntut.csie.taskService.useCase.task.edit.EditTaskUseCase;
import ntut.csie.taskService.useCase.task.edit.EditTaskUseCaseImpl;
import ntut.csie.taskService.useCase.task.get.GetTasksByBacklogItemIdInput;
import ntut.csie.taskService.useCase.task.get.GetTasksByBacklogItemIdOutput;
import ntut.csie.taskService.useCase.task.get.GetTasksByBacklogItemIdUseCase;
import ntut.csie.taskService.useCase.task.get.GetTasksByBacklogItemIdUseCaseImpl;
import ntut.csie.taskService.useCase.task.move.MoveTaskInput;
import ntut.csie.taskService.useCase.task.move.MoveTaskOutput;
import ntut.csie.taskService.useCase.task.move.MoveTaskUseCase;
import ntut.csie.taskService.useCase.task.move.MoveTaskUseCaseImpl;

public class TaskUseCaseTest {
	private FakeTaskRepository fakeTaskRepository;
	private FakeEventStore fakeEventStore;
	
	private String backlogItemId;
	
	@Before
	public void setUp() {
		fakeTaskRepository = new FakeTaskRepository();
		fakeEventStore = new FakeEventStore();
		DomainEventListener.getInstance().init(fakeTaskRepository, fakeEventStore);
		
		backlogItemId = "1";
	}
	
	@After
	public void tearDown() {
		fakeTaskRepository.clearAll();
	}
	
	@Test
	public void Should_Success_When_AddTask() {
		AddTaskOutput output = addNewTask("Implement the entity of the task.", 3, "This is the notes for the task.");
		boolean isAddSuccess = output.isAddSuccess();
		assertTrue(isAddSuccess);
	}
	
	@Test
	public void Should_ReturnTaskList_When_GetTasksOfBacklogItem() {
		String[] descriptions = {"Implement the entity of the task.", "Implement the use case of adding the task."};
		int[] estimates = {3, 5};
		
		int numberOfTasks = descriptions.length;
		
		for(int i = 0; i < numberOfTasks; i++) {
			addNewTask(descriptions[i], estimates[i], "");
		}
		
		GetTasksByBacklogItemIdOutput output = getTasksByBacklogItemId();
		List<TaskModel> taskList = output.getTaskList();
		
		assertEquals(numberOfTasks, taskList.size());
	}
	
	@Test
	public void Should_Success_When_EditTask() {
		addNewTask("Write Unit Test to test adding task.", 13, "This is the notes");
		List<Task> taskList = new ArrayList<>(fakeTaskRepository.getTasksByBacklogItemId(backlogItemId));
		String editedTaskId = taskList.get(taskList.size() - 1).getTaskId();
		
		String editedDescription = "Write Unit Test to test editing task.";
		int editedEstimate = 8;
		int editedRemains = 5;
		String editedNotes = "This is the notes about editing task.";
		
		EditTaskOutput output = editTask(editedTaskId, editedDescription, editedEstimate, editedRemains, editedNotes);
		
		boolean isEditSuccess = output.isEditSuccess();
		assertTrue(isEditSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_EditNotExistTask() {
		String editedDescription = "As a user, I want to edit task.";
		int editedEstimate = 8;
		int editedRemains = 5;
		String editedNotes = "This is the notes about editing task.";
		
		EditTaskOutput output = editTask(null, editedDescription, editedEstimate, editedRemains, editedNotes);
		
		boolean isEditSuccess = output.isEditSuccess();
		String expectedErrorMessage = "Sorry, the task is not exist.";
		assertFalse(isEditSuccess);
		assertEquals(expectedErrorMessage, output.getErrorMessage());
	}
	
	@Test
	public void Should_Success_When_DeleteTask() {
		addNewTask("Write Unit Test to test adding task.", 13, "This is the notes");
		List<Task> taskList = new ArrayList<>(fakeTaskRepository.getTasksByBacklogItemId(backlogItemId));
		String deletedTaskId = taskList.get(taskList.size() - 1).getTaskId();
		
		DeleteTaskOutput output = deleteTask(deletedTaskId);
		
		boolean isDeleteSuccess = output.isDeleteSuccess();
		assertTrue(isDeleteSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_DeleteNotExistTask() {
		DeleteTaskOutput output = deleteTask(null);
		
		boolean isDeleteSuccess = output.isDeleteSuccess();
		String expectedDeleteMessage = "Sorry, the task is not exist.";
		assertFalse(isDeleteSuccess);
		assertEquals(expectedDeleteMessage, output.getErrorMessage());
	}
	
	@Test
	public void Should_OrderIdUpdated_When_DeleteTask() {
		String[] descriptions = {"Write Unit Test to test adding task.", "Create task use case.", "Fix Bug of adding task."};
		int[] estimates = {13, 5, 3};
		int numberOfTasks = descriptions.length;
		
		for(int i = 0; i < numberOfTasks; i++) {
			addNewTask(descriptions[i], estimates[i], "");
		}
		
		List<Task> taskList = new ArrayList<>(fakeTaskRepository.getTasksByBacklogItemId(backlogItemId));
		String deletedTaskId = taskList.get(1).getTaskId();
		deleteTask(deletedTaskId);
		
		GetTasksByBacklogItemIdOutput output = getTasksByBacklogItemId();
		List<TaskModel> taskListAfterDeleted = output.getTaskList();
		
		for(int i = 0; i < taskListAfterDeleted.size(); i++) {
			assertEquals(i + 1, taskListAfterDeleted.get(i).getOrderId());
		}
	}
	
	@Test
	public void Should_ChangeTaskStatus_When_MoveTask() {
		addNewTask("Write Unit Test to test adding task.", 3, "");
		
		List<Task> taskList = new ArrayList<>(fakeTaskRepository.getTasksByBacklogItemId(backlogItemId));
		String taskId = taskList.get(0).getTaskId();
		Task task  = fakeTaskRepository.getTaskById(taskId);
		
		assertTrue(moveTask(taskId, TaskStatus.doing).isMoveSuccess());
		assertEquals(TaskStatus.doing, task.getStatus());
		
		assertTrue(moveTask(taskId, TaskStatus.done).isMoveSuccess());
		assertEquals(TaskStatus.done, task.getStatus());
		
		assertTrue(moveTask(taskId, TaskStatus.doing).isMoveSuccess());
		assertEquals(TaskStatus.doing, task.getStatus());
		
		assertTrue(moveTask(taskId, TaskStatus.toDo).isMoveSuccess());
		assertEquals(TaskStatus.toDo, task.getStatus());
	}
	
	@Test
	public void Should_ChangeRemainsToZero_When_MoveTaskFromDoingToDone() {
		addNewTask("Write Unit Test to test adding task.", 3, "");
		
		List<Task> taskList = new ArrayList<>(fakeTaskRepository.getTasksByBacklogItemId(backlogItemId));
		String taskId = taskList.get(0).getTaskId();
		
		moveTask(taskId, TaskStatus.doing);
		moveTask(taskId, TaskStatus.done);
		
		Task task  = fakeTaskRepository.getTaskById(taskId);
		assertEquals(0, task.getRemains());
	}
	
	@Test
	public void Should_RestoreRemainsToEstimate_When_MoveTaskFromDoneToDoing() {
		int estimate = 3;
		addNewTask("Write Unit Test to test adding task.", estimate,  "");
		
		List<Task> taskList = new ArrayList<>(fakeTaskRepository.getTasksByBacklogItemId(backlogItemId));
		String taskId = taskList.get(0).getTaskId();
		
		moveTask(taskId, TaskStatus.doing);
		moveTask(taskId, TaskStatus.done);
		moveTask(taskId, TaskStatus.doing);
		
		Task task  = fakeTaskRepository.getTaskById(taskId);
		assertEquals(estimate, task.getRemains());
	}
	
	@Test
	public void Should_ReturnHistoryList_When_GetHistoriesByTaskId() {
		addNewTask("Implement the entity of the task.", 3, "This is the notes for the task.");
		
		List<Task> taskList = new ArrayList<>(fakeTaskRepository.getTasksByBacklogItemId(backlogItemId));
		String taskId = taskList.get(0).getTaskId();
		
		moveTask(taskId, TaskStatus.doing);
		editTask(taskId, "As a user, I want to edit task.", 8, 5, "This is the notes about editing task.");
		moveTask(taskId, TaskStatus.done);
		
		GetHistoriesByTaskIdOutput output = getHistoriesByTaskId(taskId);
		assertEquals(8, output.getHistoryList().size());
	}
	
	@Test
	public void Should_ReturnTaskBurndownChartInformation_When_GetTaskBurndownChartPointsBySprintDatesAndTaskId() {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar sprintCalendar = Calendar.getInstance();
		sprintCalendar.set(2019, 8, 1);
		DateProvider.setDate(sprintCalendar.getTime());
		List<String> descriptions = new ArrayList<>(Arrays.asList(new String[]{"Write Unit Test to test adding task.", "Create task use case.", "Fix Bug of adding task."}));
		List<Integer> estimates = new ArrayList<>(Arrays.asList(new Integer[]{13, 5, 3}));
		int numberOfTasks = descriptions.size();
		for(int i = 0; i < numberOfTasks; i++) {
			addNewTask(descriptions.get(i), estimates.get(i), "");
		}
		
		List<String> sprintDates = new ArrayList<>();
		for(int i = 0; i < 14; i++) {
			sprintDates.add(simpleDateFormat.format(sprintCalendar.getTime()));
			sprintCalendar.add(Calendar.DATE, 1);
		}
		
		List<String> taskIds = new ArrayList<>();
		GetTasksByBacklogItemIdOutput getTasksByBacklogItemIdOutput = getTasksByBacklogItemId();
		for(TaskModel task : getTasksByBacklogItemIdOutput.getTaskList()) {
			taskIds.add(task.getTaskId());
		}
		
		sprintCalendar.set(2019, 8, 2);
		DateProvider.setDate(sprintCalendar.getTime());
		moveTask(taskIds.get(0), TaskStatus.doing);
		editTask(taskIds.get(0), descriptions.get(0), estimates.get(0), 8, "");
		
		sprintCalendar.set(2019, 8, 3);
		DateProvider.setDate(sprintCalendar.getTime());
		moveTask(taskIds.get(0), TaskStatus.done);
		
		sprintCalendar.set(2019, 8, 4);
		DateProvider.setDate(sprintCalendar.getTime());
		moveTask(taskIds.get(1), TaskStatus.doing);
		moveTask(taskIds.get(1), TaskStatus.done);
		
		sprintCalendar.set(2019, 8, 5);
		DateProvider.setDate(sprintCalendar.getTime());
		moveTask(taskIds.get(2), TaskStatus.doing);
		moveTask(taskIds.get(2), TaskStatus.done);
		
		sprintCalendar.set(2019, 8, 6);
		DateProvider.setDate(sprintCalendar.getTime());
		descriptions.add("Write Unit Test to test editing task.");
		estimates.add(8);
		addNewTask(descriptions.get(3), estimates.get(3), "");
		GetTasksByBacklogItemIdOutput getTasksByBacklogItemIdOutput2 = getTasksByBacklogItemId();
		taskIds.add(getTasksByBacklogItemIdOutput2.getTaskList().get(3).getTaskId());
		
		sprintCalendar.set(2019, 8, 7);
		DateProvider.setDate(sprintCalendar.getTime());
		editTask(taskIds.get(3), descriptions.get(3), estimates.get(3), 5, "");
		
		sprintCalendar.set(2019, 8, 8);
		DateProvider.setDate(sprintCalendar.getTime());
		moveTask(taskIds.get(3), TaskStatus.doing);
		moveTask(taskIds.get(3), TaskStatus.done);
		
		sprintCalendar.set(2019, 8, 9);
		DateProvider.setDate(sprintCalendar.getTime());
		moveTask(taskIds.get(0), TaskStatus.doing);
		
		sprintCalendar.set(2019, 8, 10);
		DateProvider.setDate(sprintCalendar.getTime());
		editTask(taskIds.get(0), descriptions.get(0), estimates.get(0), 8, "");
		
		sprintCalendar.set(2019, 8, 11);
		DateProvider.setDate(sprintCalendar.getTime());
		editTask(taskIds.get(0), descriptions.get(0), estimates.get(0), 5, "");
		
		sprintCalendar.set(2019, 8, 12);
		DateProvider.setDate(sprintCalendar.getTime());
		editTask(taskIds.get(0), descriptions.get(0), estimates.get(0), 2, "");
		
		sprintCalendar.set(2019, 8, 13);
		DateProvider.setDate(sprintCalendar.getTime());
		moveTask(taskIds.get(0), TaskStatus.done);
		
		sprintCalendar.set(2019, 8, 14);
		DateProvider.setDate(sprintCalendar.getTime());
		
		List<Integer> expectedRealPoints = new ArrayList<>(Arrays.asList(new Integer[] {21, 16, 8, 3, 0, 8, 5, 0, 13, 8, 5, 2, 0, 0}));
		GetBurndownChartPointsBySprintDatesAndTaskIdsOutput output = getBurndownChartPointsBySprintDatesAndTaskIds(sprintDates, taskIds);
		List<Integer> realPoints = output.getRealPoints();
		assertArrayEquals(expectedRealPoints.toArray(), realPoints.toArray());
	}
	
	@Test
	public void Should_ReturnTaskBurndownChartInformationWithFirstDate_When_GetTaskBurndownChartPointsWithFinishingSomeTasksFirstDate() {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar sprintCalendar = Calendar.getInstance();
		sprintCalendar.set(2019, 8, 1);
		DateProvider.setDate(sprintCalendar.getTime());
		List<String> descriptions = new ArrayList<>(Arrays.asList(new String[]{"Write Unit Test to test adding task.", "Create task use case.", "Fix Bug of adding task."}));
		List<Integer> estimates = new ArrayList<>(Arrays.asList(new Integer[]{13, 5, 3}));
		int numberOfTasks = descriptions.size();
		for(int i = 0; i < numberOfTasks; i++) {
			addNewTask(descriptions.get(i), estimates.get(i), "");
		}
		
		List<String> sprintDates = new ArrayList<>();
		for(int i = 0; i < 14; i++) {
			sprintDates.add(simpleDateFormat.format(sprintCalendar.getTime()));
			sprintCalendar.add(Calendar.DATE, 1);
		}
		
		List<String> taskIds = new ArrayList<>();
		GetTasksByBacklogItemIdOutput getTasksByBacklogItemIdOutput = getTasksByBacklogItemId();
		for(TaskModel task : getTasksByBacklogItemIdOutput.getTaskList()) {
			taskIds.add(task.getTaskId());
		}
		
		moveTask(taskIds.get(0), "Doing");
		moveTask(taskIds.get(0), "Done");
		
		List<Integer> expectedRealPoints = new ArrayList<>(Arrays.asList(new Integer[] {8, null, null, null, null, null, null, null, null, null, null, null, null, null}));
		GetBurndownChartPointsBySprintDatesAndTaskIdsOutput output = getBurndownChartPointsBySprintDatesAndTaskIds(sprintDates, taskIds);
		List<Integer> realPoints = output.getRealPoints();
		assertArrayEquals(expectedRealPoints.toArray(), realPoints.toArray());
	}
	
	@Test
	public void Should_ReturnNullRealPoints_When_GetTaskBurndownChartPointsBySprintDatesButNoTaskIds() {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar sprintCalendar = Calendar.getInstance();
		sprintCalendar.set(2019, 8, 1);
		DateProvider.setDate(sprintCalendar.getTime());
		
		List<String> sprintDates = new ArrayList<>();
		for(int i = 0; i < 14; i++) {
			sprintDates.add(simpleDateFormat.format(sprintCalendar.getTime()));
			sprintCalendar.add(Calendar.DATE, 1);
		}
		
		List<String> taskIds = new ArrayList<>();
		
		List<Integer> expectedRealPoints = new ArrayList<>();
		GetBurndownChartPointsBySprintDatesAndTaskIdsOutput output = getBurndownChartPointsBySprintDatesAndTaskIds(sprintDates, taskIds);
		List<Integer> realPoints = output.getRealPoints();
		assertArrayEquals(expectedRealPoints.toArray(), realPoints.toArray());
	}
	
	private AddTaskOutput addNewTask(String description, int estimate, String notes) {
		AddTaskUseCase addTaskUseCase = new AddTaskUseCaseImpl(fakeTaskRepository);
		AddTaskInput input = (AddTaskInput) addTaskUseCase;
		input.setDescription(description);
		input.setEstimate(estimate);
		input.setNotes(notes);
		input.setBacklogItemId(backlogItemId);
		AddTaskOutput output = new AddTaskRestfulAPI();
		addTaskUseCase.execute(input, output);
		return output;
	}
	
	private GetTasksByBacklogItemIdOutput getTasksByBacklogItemId() {
		GetTasksByBacklogItemIdUseCase getTasksByWorkItemIdUseCase = new GetTasksByBacklogItemIdUseCaseImpl(fakeTaskRepository);
		GetTasksByBacklogItemIdInput input = (GetTasksByBacklogItemIdInput) getTasksByWorkItemIdUseCase;
		input.setBacklogItemId(backlogItemId);
		GetTasksByBacklogItemIdOutput output = new GetTasksByBacklogItemIdRestfulAPI();
		getTasksByWorkItemIdUseCase.execute(input, output);
		
		return output;
	}
	
	private EditTaskOutput editTask(String taskId, String editedDescription, int editedEstimate, int editedRemains, String editedNotes) {
		EditTaskUseCase editTaskUseCase = new EditTaskUseCaseImpl(fakeTaskRepository);
		EditTaskInput input = (EditTaskInput) editTaskUseCase;
		input.setTaskId(taskId);
		input.setDescription(editedDescription);
		input.setEstimate(editedEstimate);
		input.setRemains(editedRemains);
		input.setNotes(editedNotes);
		EditTaskOutput output = new EditTaskRestfulAPI();
		editTaskUseCase.execute(input, output);
		return output;
	}
	
	private DeleteTaskOutput deleteTask(String taskId) {
		DeleteTaskUseCase deleteTaskUseCase = new DeleteTaskUseCaseImpl(fakeTaskRepository);
		DeleteTaskInput input = (DeleteTaskInput) deleteTaskUseCase;
		input.setTaskId(taskId);
		DeleteTaskOutput output = new DeleteTaskRestfulAPI();
		deleteTaskUseCase.execute(input, output);
		return output;
	}
	
	private MoveTaskOutput moveTask(String taskId, String newStatus) {
		MoveTaskUseCase moveTaskUseCase = new MoveTaskUseCaseImpl(fakeTaskRepository);
		MoveTaskInput input = (MoveTaskInput) moveTaskUseCase;
		input.setTaskId(taskId);
		input.setStatus(newStatus);
		MoveTaskOutput output = new MoveTaskRestfulAPI();
		moveTaskUseCase.execute(input, output);
		return output;
	}
	
	private GetHistoriesByTaskIdOutput getHistoriesByTaskId(String taskId) {
		GetHistoriesByTaskIdUseCase getHistoriesByTaskIdUseCase = new GetHistoriesByTaskIdUseCaseImpl(fakeEventStore);
		GetHistoriesByTaskIdInput input = (GetHistoriesByTaskIdInput) getHistoriesByTaskIdUseCase;
		input.setTaskId(taskId);
		GetHistoriesByTaskIdOutput output = new GetHistoriesByTaskIdRestfulAPI();
		getHistoriesByTaskIdUseCase.execute(input, output);
		return output;
	}
	
	private GetBurndownChartPointsBySprintDatesAndTaskIdsOutput getBurndownChartPointsBySprintDatesAndTaskIds(List<String> sprintDates, List<String> taskIds) {
		GetBurndownChartPointsBySprintDatesAndTaskIdsUseCase getBurndownChartPointsBySprintDatesAndTaskIdsUseCase = new GetBurndownChartPointsBySprintDatesAndTaskIdsUseCaseImpl(fakeTaskRepository, fakeEventStore);
		GetBurndownChartPointsBySprintDatesAndTaskIdsInput input = (GetBurndownChartPointsBySprintDatesAndTaskIdsInput) getBurndownChartPointsBySprintDatesAndTaskIdsUseCase;
		input.setSprintDates(sprintDates);
		input.setTaskIds(taskIds);
		GetBurndownChartPointsBySprintDatesAndTaskIdsOutput output = new GetBurndownChartPointsBySprintDatesAndTaskIdsRestfulAPI();
		getBurndownChartPointsBySprintDatesAndTaskIdsUseCase.execute(input, output);
		return output;
	}
}
