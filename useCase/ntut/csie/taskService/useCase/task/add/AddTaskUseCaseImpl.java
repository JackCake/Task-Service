package ntut.csie.taskService.useCase.task.add;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.model.task.TaskBuilder;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class AddTaskUseCaseImpl implements AddTaskUseCase, AddTaskInput {
	private TaskRepository taskRepository;
	
	private String description;
	private int estimate;
	private String notes;
	private String backlogItemId;
	
	public AddTaskUseCaseImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	@Override
	public void execute(AddTaskInput input, AddTaskOutput output) {
		String backlogItemId = input.getBacklogItemId();
		int orderId = taskRepository.getTasksByBacklogItemId(backlogItemId).size() + 1;
		try {
			Task task = TaskBuilder.newInstance()
					.orderId(orderId)
					.description(input.getDescription())
					.estimate(input.getEstimate())
					.notes(input.getNotes())
					.backlogItemId(backlogItemId)
					.build();
			taskRepository.save(task);
		} catch (Exception e) {
			output.setAddSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setAddSuccess(true);
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int getEstimate() {
		return estimate;
	}

	@Override
	public void setEstimate(int estimate) {
		this.estimate = estimate;
	}
	
	@Override
	public String getNotes() {
		return notes;
	}

	@Override
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Override
	public String getBacklogItemId() {
		return backlogItemId;
	}

	@Override
	public void setBacklogItemId(String backlogItemId) {
		this.backlogItemId = backlogItemId;
	}
}
