package ntut.csie.taskService.gateways.repository.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import ntut.csie.taskService.gateways.database.SqlDatabaseHelper;
import ntut.csie.taskService.gateways.database.TaskTable;
import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class MySqlTaskRepositoryImpl implements TaskRepository {
	private SqlDatabaseHelper sqlDatabaseHelper;
	private TaskMapper taskMapper;
	
	public MySqlTaskRepositoryImpl() {
		sqlDatabaseHelper = new SqlDatabaseHelper();
		taskMapper = new TaskMapper();
	}

	@Override
	public synchronized void save(Task task) throws Exception {
		sqlDatabaseHelper.connectToDatabase();
		PreparedStatement preparedStatement = null;
		try {
			sqlDatabaseHelper.transactionStart();
			TaskData data = taskMapper.transformToTaskData(task);
			String sql = String.format("Insert Into %s Values (?, ?, ?, ?, ?, ?, ?, ?, ?) "
					+ "On Duplicate Key Update %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?",
					TaskTable.tableName, TaskTable.orderId, TaskTable.description, TaskTable.handlerId, TaskTable.status, 
					TaskTable.estimate, TaskTable.remains, TaskTable.notes, TaskTable.backlogItemId);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, data.getTaskId());
			preparedStatement.setInt(2, data.getOrderId());
			preparedStatement.setString(3, data.getDescription());
			preparedStatement.setString(4, data.getHandlerId());
			preparedStatement.setString(5, data.getStatus());
			preparedStatement.setInt(6, data.getEstimate());
			preparedStatement.setInt(7, data.getRemains());
			preparedStatement.setString(8, data.getNotes());
			preparedStatement.setString(9, data.getBacklogItemId());
			preparedStatement.setInt(10, data.getOrderId());
			preparedStatement.setString(11, data.getDescription());
			preparedStatement.setString(12, data.getHandlerId());
			preparedStatement.setString(13, data.getStatus());
			preparedStatement.setInt(14, data.getEstimate());
			preparedStatement.setInt(15, data.getRemains());
			preparedStatement.setString(16, data.getNotes());
			preparedStatement.setString(17, data.getBacklogItemId());
			preparedStatement.executeUpdate();
			sqlDatabaseHelper.transactionEnd();
		} catch(SQLException e) {
			sqlDatabaseHelper.transactionError();
			e.printStackTrace();
			throw new Exception("Sorry, there is the problem when save the task. Please try again!");
		} finally {
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			sqlDatabaseHelper.releaseConnection();
		}
	}

	@Override
	public synchronized void remove(Task task) throws Exception {
		sqlDatabaseHelper.connectToDatabase();
		PreparedStatement preparedStatement = null;
		try {
			sqlDatabaseHelper.transactionStart();
			String sql = String.format("Delete From %s Where %s = '%s'",
					TaskTable.tableName,
					TaskTable.taskId,
					task.getTaskId());
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.executeUpdate();
			sqlDatabaseHelper.transactionEnd();
		} catch(SQLException e) {
			sqlDatabaseHelper.transactionError();
			e.printStackTrace();
			throw new Exception("Sorry, there is the problem when remove the task. Please try again!");
		} finally {
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			sqlDatabaseHelper.releaseConnection();
		}
	}

	@Override
	public synchronized Task getTaskById(String taskId) {
		sqlDatabaseHelper.connectToDatabase();
		ResultSet resultSet = null;
		Task task = null;
		try {
			String query = String.format("Select * From %s Where %s = '%s'",
					TaskTable.tableName,
					TaskTable.taskId,
					taskId);
			resultSet = sqlDatabaseHelper.getResultSet(query);
			if (resultSet.first()) {
				int orderId = resultSet.getInt(TaskTable.orderId);
				String description = resultSet.getString(TaskTable.description);
				String handlerId = resultSet.getString(TaskTable.handlerId);
				String status = resultSet.getString(TaskTable.status);
				int estimate = resultSet.getInt(TaskTable.estimate);
				int remains = resultSet.getInt(TaskTable.remains);
				String notes = resultSet.getString(TaskTable.notes);
				String workItemId = resultSet.getString(TaskTable.backlogItemId);
				
				TaskData data = new TaskData();
				data.setTaskId(taskId);
				data.setOrderId(orderId);
				data.setDescription(description);
				data.setHandlerId(handlerId);
				data.setStatus(status);
				data.setEstimate(estimate);
				data.setRemains(remains);
				data.setNotes(notes);
				data.setBacklogItemId(workItemId);

				task = taskMapper.transformToTask(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.releaseConnection();
		}
		return task;
	}
	
	@Override
	public synchronized Collection<Task> getTasksByBacklogItemId(String backlogItemId){
		sqlDatabaseHelper.connectToDatabase();
		ResultSet resultSet = null;
		Collection<Task> tasks = new ArrayList<>();
		try {
			String query = String.format("Select * From %s Where %s = '%s' Order By %s",
					TaskTable.tableName, 
					TaskTable.backlogItemId, 
					backlogItemId, 
					TaskTable.orderId);
			resultSet = sqlDatabaseHelper.getResultSet(query);
			while (resultSet.next()) {
				String taskId = resultSet.getString(TaskTable.taskId);
				int orderId = resultSet.getInt(TaskTable.orderId);
				String description = resultSet.getString(TaskTable.description);
				String handlerId = resultSet.getString(TaskTable.handlerId);
				String status = resultSet.getString(TaskTable.status);
				int estimate = resultSet.getInt(TaskTable.estimate);
				int remains = resultSet.getInt(TaskTable.remains);
				String notes = resultSet.getString(TaskTable.notes);
				
				TaskData data = new TaskData();
				data.setTaskId(taskId);
				data.setOrderId(orderId);
				data.setDescription(description);
				data.setHandlerId(handlerId);
				data.setStatus(status);
				data.setEstimate(estimate);
				data.setRemains(remains);
				data.setNotes(notes);
				data.setBacklogItemId(backlogItemId);

				Task task = taskMapper.transformToTask(data);
				tasks.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.releaseConnection();
		}
		return tasks;
	}
}
