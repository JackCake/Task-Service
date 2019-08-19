package ntut.csie.taskService.useCase.task.edit;

import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class EditTaskUseCaseImpl implements EditTaskUseCase, EditTaskInput{
	private TaskRepository taskRepository;
	
	private String taskId;
	private String description;
	private int estimate;
	private int remains;
	private String notes;
	
	public EditTaskUseCaseImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	@Override
	public void execute(EditTaskInput input, EditTaskOutput output) {
		String taskId = input.getTaskId();
		Task task = taskRepository.getTaskById(taskId);
		if(task == null) {
			output.setEditSuccess(false);
			output.setErrorMessage("Sorry, the task is not exist.");
			return;
		}
		
		task.editDescription(input.getDescription());
		task.changeEstimate(input.getEstimate());
		task.changeRemains(input.getRemains());
		task.editNotes(input.getNotes());
		
		try {
			taskRepository.save(task);
		} catch (Exception e) {
			output.setEditSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setEditSuccess(true);
	}
	
	@Override
	public String getTaskId() {
		return taskId;
	}
	
	@Override
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
	public int getRemains() {
		return remains;
	}
	
	@Override
	public void setRemains(int remains) {
		this.remains = remains;
	}
	
	@Override
	public String getNotes() {
		return notes;
	}
	
	@Override
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
