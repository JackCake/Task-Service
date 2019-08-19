package ntut.csie.taskService.useCase;

import ntut.csie.taskService.model.DomainEventPublisher;
import ntut.csie.taskService.model.DomainEventSubscriber;
import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.model.task.TaskAdded;
import ntut.csie.taskService.model.task.TaskDeleted;
import ntut.csie.taskService.model.task.TaskDescriptionEdited;
import ntut.csie.taskService.model.task.TaskEstimateChanged;
import ntut.csie.taskService.model.task.TaskNotesEdited;
import ntut.csie.taskService.model.task.TaskRemainsChanged;
import ntut.csie.taskService.model.task.TaskStatusChanged;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class DomainEventListener {
	private static DomainEventListener instance = null;
	
	private TaskRepository taskRepository;
	private EventStore eventStore;
	
	private DomainEventListener() {}
	
	public static synchronized DomainEventListener getInstance() {
		if(instance == null) {
			instance = new DomainEventListener();
		}
		return instance;
	}
	
	public void init(TaskRepository taskRepository, EventStore eventStore) {
		this.taskRepository = taskRepository;
		this.eventStore = eventStore;
		DomainEventPublisher.getInstance().reset();
		eventSubscribe();
	}
	
	private void eventSubscribe() {
		DomainEventPublisher.getInstance().subscribe(new DomainEventSubscriber<TaskAdded>() {

			@Override
			public void handleEvent(TaskAdded domainEvent) {
				eventStore.save(domainEvent);
			}

			@Override
			public Class<TaskAdded> subscribedToEventType() {
				return TaskAdded.class;
			}
           
        });
		
		DomainEventPublisher.getInstance().subscribe(new DomainEventSubscriber<TaskDescriptionEdited>() {

			@Override
			public void handleEvent(TaskDescriptionEdited domainEvent) {
				eventStore.save(domainEvent);
			}

			@Override
			public Class<TaskDescriptionEdited> subscribedToEventType() {
				return TaskDescriptionEdited.class;
			}
           
        });
		
		DomainEventPublisher.getInstance().subscribe(new DomainEventSubscriber<TaskEstimateChanged>() {

			@Override
			public void handleEvent(TaskEstimateChanged domainEvent) {
				eventStore.save(domainEvent);
			}

			@Override
			public Class<TaskEstimateChanged> subscribedToEventType() {
				return TaskEstimateChanged.class;
			}
           
        });
		
		DomainEventPublisher.getInstance().subscribe(new DomainEventSubscriber<TaskRemainsChanged>() {

			@Override
			public void handleEvent(TaskRemainsChanged domainEvent) {
				eventStore.save(domainEvent);
			}

			@Override
			public Class<TaskRemainsChanged> subscribedToEventType() {
				return TaskRemainsChanged.class;
			}
           
        });
		
		DomainEventPublisher.getInstance().subscribe(new DomainEventSubscriber<TaskStatusChanged>() {

			@Override
			public void handleEvent(TaskStatusChanged domainEvent) {
				eventStore.save(domainEvent);
			}

			@Override
			public Class<TaskStatusChanged> subscribedToEventType() {
				return TaskStatusChanged.class;
			}
           
        });
		
		DomainEventPublisher.getInstance().subscribe(new DomainEventSubscriber<TaskNotesEdited>() {

			@Override
			public void handleEvent(TaskNotesEdited domainEvent) {
				eventStore.save(domainEvent);
			}

			@Override
			public Class<TaskNotesEdited> subscribedToEventType() {
				return TaskNotesEdited.class;
			}
           
        });
		
		DomainEventPublisher.getInstance().subscribe(new DomainEventSubscriber<TaskDeleted>() {

			@Override
			public void handleEvent(TaskDeleted domainEvent) {
				int newOrderId = 0;
				for(Task task : taskRepository.getTasksByBacklogItemId(domainEvent.backlogItemId())) {
					task.setOrderId(++newOrderId);
					try {
						taskRepository.save(task);
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
			}

			@Override
			public Class<TaskDeleted> subscribedToEventType() {
				return TaskDeleted.class;
			}
           
        });
	}
}
