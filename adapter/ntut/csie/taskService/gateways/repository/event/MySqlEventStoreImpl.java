package ntut.csie.taskService.gateways.repository.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ntut.csie.taskService.gateways.database.EventTable;
import ntut.csie.taskService.gateways.database.SqlDatabaseHelper;
import ntut.csie.taskService.model.DomainEvent;
import ntut.csie.taskService.useCase.EventStore;

public class MySqlEventStoreImpl implements EventStore {
	private SqlDatabaseHelper sqlDatabaseHelper;
	private EventSerializer eventSerializer;
	
	public MySqlEventStoreImpl() {
		sqlDatabaseHelper = new SqlDatabaseHelper();
		this.setEventSerializer(EventSerializer.instance());
	}

	@Override
	public synchronized void save(DomainEvent event) {
		sqlDatabaseHelper.connectToDatabase();
		PreparedStatement preparedStatement = null;
		try {
			StoredEvent storedEvent = new StoredEvent(
					this.getEventSerializer().serialize(event),
					event.getClass().getName());
			String sql = String.format("Insert Into %s (%s, %s) Values (?, ?)", 
					EventTable.tableName, 
					EventTable.eventBody, 
					EventTable.eventType);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, storedEvent.getEventBody());
			preparedStatement.setString(2, storedEvent.getEventType());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			sqlDatabaseHelper.releaseConnection();
		}
	}
	
	@Override
	public synchronized List<DomainEvent> getByEventType(String eventType) {
		sqlDatabaseHelper.connectToDatabase();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<DomainEvent> events = new ArrayList<>();
		try {
			String sql = String.format("Select * From %s Where %s = ? Order By %s",
					EventTable.tableName, 
					EventTable.eventType, 
					EventTable.eventId);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, eventType);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String eventBody = resultSet.getString(EventTable.eventBody);
				
				StoredEvent storedEvent = new StoredEvent(eventBody, eventType);
				
				DomainEvent event = storedEvent.toDomainEvent();
				events.add(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			sqlDatabaseHelper.releaseConnection();
		}
		return events;
	}

	@Override
	public synchronized List<DomainEvent> getAllEvent() {
		sqlDatabaseHelper.connectToDatabase();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<DomainEvent> events = new ArrayList<>();
		try {
			String sql = String.format("Select * From %s Order By %s",
					EventTable.tableName,
					EventTable.eventId);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String eventBody = resultSet.getString(EventTable.eventBody);
				String eventType = resultSet.getString(EventTable.eventType);
				
				StoredEvent storedEvent = new StoredEvent(eventBody, eventType);
				DomainEvent event = storedEvent.toDomainEvent();
				
				events.add(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			sqlDatabaseHelper.releaseConnection();
		}
		return events;
	}
	
	public EventSerializer getEventSerializer() {
		return eventSerializer;
	}

	public void setEventSerializer(EventSerializer eventSerializer) {
		this.eventSerializer = eventSerializer;
	}
}
