package ntut.csie.taskService.unitTest.factory;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.model.task.TaskBuilder;
import ntut.csie.taskService.unitTest.repository.FakeTaskRepository;

public class TestFactory {
	private FakeTaskRepository fakeTaskRepository;
	
	public TestFactory(FakeTaskRepository fakeTaskRepository) {
		this.fakeTaskRepository = fakeTaskRepository;
	}
	
	public Task newTask(String description, int estimate, String notes, String backlogItemId) {
		int orderId = fakeTaskRepository.getTasksByBacklogItemId(backlogItemId).size() + 1;
		Task task = null;
		try {
			task = TaskBuilder.newInstance()
					.orderId(orderId)
					.description(description)
					.estimate(estimate)
					.notes(notes)
					.backlogItemId(backlogItemId)
					.build();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		fakeTaskRepository.save(task);
		return task;
	}
}
