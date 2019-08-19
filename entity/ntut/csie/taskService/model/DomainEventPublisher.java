package ntut.csie.taskService.model;

import java.util.ArrayList;
import java.util.List;

public class DomainEventPublisher {
	private static DomainEventPublisher instance = null;
	
	private boolean publishing;
	
	@SuppressWarnings("rawtypes")
	private List subscribers;
	
	public static synchronized DomainEventPublisher getInstance() {
		if(instance == null) {
			instance = new DomainEventPublisher();
		}
		return instance;
	}
	
	private DomainEventPublisher() {
		this.setPublishing(false);
		this.ensureSubscribersList();
	}
	
	@SuppressWarnings("unchecked")
	public <T> void publish(final T domainEvent) {
		if(!this.isPublishing() && this.hasSubscribers()) {
			try {
				this.setPublishing(true);
				List<DomainEventSubscriber<T>> allSubscribers = this.getSubscribers();
				if(allSubscribers != null) {
					Class<?> eventType = domainEvent.getClass();
					for(DomainEventSubscriber<T> subscriber : allSubscribers) {
						Class<?> subscribedToType = subscriber.subscribedToEventType();
						if(eventType == subscribedToType) {
							subscriber.handleEvent(domainEvent);
						}
					}
				}
			} finally {
				this.setPublishing(false);
			}
		}
	}
	
	public void reset() {
        if (!this.isPublishing()) {
            this.setSubscribers(null);
        }
    }
	
	@SuppressWarnings("unchecked")
	public <T> void subscribe(DomainEventSubscriber<T> subscriber) {
		if(!this.isPublishing()) {
			this.ensureSubscribersList();
			this.subscribers.add(subscriber);
		}
	}
	
	private boolean isPublishing() {
		return publishing;
	}

	private void setPublishing(boolean publishing) {
		this.publishing = publishing;
	}
	
	@SuppressWarnings("rawtypes")
	private void ensureSubscribersList() {
		if(!this.hasSubscribers()) {
			this.setSubscribers(new ArrayList());
		}
	}
	
	private boolean hasSubscribers() {
		return getSubscribers() != null;
	}

	@SuppressWarnings("rawtypes")
	private List getSubscribers() {
		return subscribers;
	}

	@SuppressWarnings("rawtypes")
	private void setSubscribers(List subscribers) {
		this.subscribers = subscribers;
	}
}
