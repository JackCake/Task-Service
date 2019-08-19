package ntut.csie.taskService.gateways.repository.event;

import ntut.csie.taskService.model.DomainEvent;

public class StoredEvent {
	private String eventBody;
    private long eventId;
    private String eventType;
    
    public StoredEvent(String eventBody, String eventType) {
    	this.eventBody = eventBody;
    	this.eventType = eventType;
    }
    
	public String getEventBody() {
		return eventBody;
	}
	
	public void setEventBody(String eventBody) {
		this.eventBody = eventBody;
	}
	
	public long getEventId() {
		return eventId;
	}
	
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	
	public String getEventType() {
		return eventType;
	}
	
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
    @SuppressWarnings("unchecked")
	public <T extends DomainEvent> T toDomainEvent() {
        Class<T> domainEventClass = null;

        try {
            domainEventClass = (Class<T>) Class.forName(this.getEventType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        T domainEvent =
            EventSerializer
                .instance()
                .deserialize(this.getEventBody(), domainEventClass);

        return domainEvent;
    }
}
