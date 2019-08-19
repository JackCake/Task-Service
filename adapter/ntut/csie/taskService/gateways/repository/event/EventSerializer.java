package ntut.csie.taskService.gateways.repository.event;

import com.google.gson.Gson;

import ntut.csie.taskService.model.DomainEvent;

public class EventSerializer {
	private static EventSerializer eventSerializer;

    public static synchronized EventSerializer instance() {
        if (EventSerializer.eventSerializer == null) {
            EventSerializer.eventSerializer = new EventSerializer();
        }

        return EventSerializer.eventSerializer;
    }
    
    public String serialize(DomainEvent domainEvent) {
        String eventBody = new Gson().toJson(domainEvent);

        return eventBody;
    }

    public <T extends DomainEvent> T deserialize(String eventBody, final Class<T> eventType) {
        T domainEvent = new Gson().fromJson(eventBody, eventType);

        return domainEvent;
    }
}
