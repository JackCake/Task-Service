package ntut.csie.taskService.useCase.history;

import java.util.UUID;

public class HistoryModel {
	private String historyId;
	private String occurredOn;
	private String behavior;
	private String description;
	
	public HistoryModel() {
		historyId = UUID.randomUUID().toString();
	}
	
	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public String getOccurredOn() {
		return occurredOn;
	}
	
	public void setOccurredOn(String occurredOn) {
		this.occurredOn = occurredOn;
	}
	
	public String getBehavior() {
		return behavior;
	}
	
	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
