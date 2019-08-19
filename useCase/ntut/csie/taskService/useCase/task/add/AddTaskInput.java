package ntut.csie.taskService.useCase.task.add;

import ntut.csie.taskService.useCase.Input;

public interface AddTaskInput extends Input {
	public String getDescription();
	
	public void setDescription(String description);
	
	public int getEstimate();
	
	public void setEstimate(int estimate);
	
	public String getNotes();
	
	public void setNotes(String notes);
	
	public String getBacklogItemId();
	
	public void setBacklogItemId(String backlogItemId);
}
