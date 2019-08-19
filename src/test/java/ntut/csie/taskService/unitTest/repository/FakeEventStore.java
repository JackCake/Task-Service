package ntut.csie.taskService.unitTest.repository;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.taskService.model.DomainEvent;
import ntut.csie.taskService.useCase.EventStore;

public class FakeEventStore implements EventStore{
	List<DomainEvent> events;
	
	public FakeEventStore() {
		events = new ArrayList<>();
	}

	@Override
	public void save(DomainEvent event) {
		events.add(event);
	}

	@Override
	public List<DomainEvent> getByEventType(String eventType) {
		List<DomainEvent> _events = new ArrayList<>();
		
		for(DomainEvent event : events) {
			if(event.getClass().getSimpleName().equals(eventType)) {
				_events.add(event);
			}
		}
		
		return _events;
	}
	
	public void clearAll() {
		events.clear();
	}

	@Override
	public List<DomainEvent> getAllEvent() {
		List<DomainEvent> _events = new ArrayList<>();
		
		for(DomainEvent event:events) {
			_events.add(event);
		}
		return _events;
	}
}
