package ntut.csie.taskService.gateways.repository.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ntut.csie.taskService.gateways.database.SqlDatabaseHelper;
import ntut.csie.taskService.gateways.database.TaskAttachFileTable;
import ntut.csie.taskService.gateways.database.TaskTable;
import ntut.csie.taskService.gateways.repository.task.taskAttachFile.TaskAttachFileData;
import ntut.csie.taskService.gateways.repository.task.taskAttachFile.TaskAttachFileMapper;
import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.model.task.TaskAttachFile;
import ntut.csie.taskService.useCase.task.TaskRepository;

public class MySqlTaskRepositoryImpl implements TaskRepository {
	private SqlDatabaseHelper sqlDatabaseHelper;
	private TaskMapper taskMapper;
	private TaskAttachFileMapper taskAttachFileMapper;
	
	public MySqlTaskRepositoryImpl() {
		sqlDatabaseHelper = new SqlDatabaseHelper();
		taskMapper = new TaskMapper();
		taskAttachFileMapper = new TaskAttachFileMapper();
	}

	@Override
	public synchronized void save(Task task) throws Exception {
		sqlDatabaseHelper.connectToDatabase();
		PreparedStatement preparedStatement = null;
		try {
			sqlDatabaseHelper.transactionStart();
			//當記憶體中的task下的taskAttachFile被移除時，那麼資料庫也必須被同步刪除
			Task taskInDatabase = getTaskById(task.getTaskId());
			if(taskInDatabase != null) {
				Set<String> taskAttachFileIds = new HashSet<>();
				for(TaskAttachFile taskAttachFile : task.getTaskAttachFiles()) {
					taskAttachFileIds.add(taskAttachFile.getTaskAttachFileId());
				}
				for(TaskAttachFile taskAttachFile : taskInDatabase.getTaskAttachFiles()) {
					if(!taskAttachFileIds.contains(taskAttachFile.getTaskAttachFileId())) {
						removeTaskAttachFile(taskAttachFile);
					}
				}
				
				//開始儲存
				for(TaskAttachFile taskAttachFile : task.getTaskAttachFiles()) {
					addTaskAttachFile(taskAttachFile);
				}
			}
			
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
			throw new Exception("Sorry, there is the database problem when save the task. Please contact to the system administrator!");
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
			for(TaskAttachFile taskAttachFile : task.getTaskAttachFiles()) {
				removeTaskAttachFile(taskAttachFile);
			}
			
			TaskData data = taskMapper.transformToTaskData(task);
			String sql = String.format("Delete From %s Where %s = ?",
					TaskTable.tableName,
					TaskTable.taskId);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, data.getTaskId());
			preparedStatement.executeUpdate();
			sqlDatabaseHelper.transactionEnd();
		} catch(SQLException e) {
			sqlDatabaseHelper.transactionError();
			e.printStackTrace();
			throw new Exception("Sorry, there is the database problem when remove the task. Please contact to the system administrator!");
		} finally {
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			sqlDatabaseHelper.releaseConnection();
		}
	}

	@Override
	public synchronized Task getTaskById(String taskId) {
		if(!sqlDatabaseHelper.isTransacting()) {
			sqlDatabaseHelper.connectToDatabase();
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Task task = null;
		try {
			String sql = String.format("Select * From %s Where %s = ?",
					TaskTable.tableName,
					TaskTable.taskId);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, taskId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.first()) {
				int orderId = resultSet.getInt(TaskTable.orderId);
				String description = resultSet.getString(TaskTable.description);
				String handlerId = resultSet.getString(TaskTable.handlerId);
				String status = resultSet.getString(TaskTable.status);
				int estimate = resultSet.getInt(TaskTable.estimate);
				int remains = resultSet.getInt(TaskTable.remains);
				String notes = resultSet.getString(TaskTable.notes);
				String backlogItemId = resultSet.getString(TaskTable.backlogItemId);
				
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
				data.setTaskAttachFileDatas(getTaskAttachFileDatasByTaskId(taskId));

				task = taskMapper.transformToTask(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			if(!sqlDatabaseHelper.isTransacting()) {
				sqlDatabaseHelper.releaseConnection();
			}
		}
		return task;
	}
	
	@Override
	public synchronized Collection<Task> getTasksByBacklogItemId(String backlogItemId){
		if(!sqlDatabaseHelper.isTransacting()) {
			sqlDatabaseHelper.connectToDatabase();
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Task> tasks = new ArrayList<>();
		try {
			String sql = String.format("Select * From %s Where %s = ? Order By %s",
					TaskTable.tableName, 
					TaskTable.backlogItemId, 
					TaskTable.orderId);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, backlogItemId);
			resultSet = preparedStatement.executeQuery();
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
				data.setTaskAttachFileDatas(getTaskAttachFileDatasByTaskId(taskId));

				Task task = taskMapper.transformToTask(data);
				tasks.add(task);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			if(!sqlDatabaseHelper.isTransacting()) {
				sqlDatabaseHelper.releaseConnection();
			}
		}
		return tasks;
	}
	
	public void addTaskAttachFile(TaskAttachFile taskAttachFile) throws Exception {
		TaskAttachFileData data = taskAttachFileMapper.transformToTaskAttachFileData(taskAttachFile);
		String sql = String.format("Insert Into %s Values (?, ?, ?, ?, ?) "
				+ "On Duplicate Key Update %s=?, %s=?", 
				TaskAttachFileTable.tableName, 
				TaskAttachFileTable.name, 
				TaskAttachFileTable.path);
		PreparedStatement preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
		preparedStatement.setString(1, data.getTaskAttachFileId());
		preparedStatement.setString(2, data.getName());
		preparedStatement.setString(3, data.getPath());
		preparedStatement.setString(4, data.getTaskId());
		preparedStatement.setString(5, data.getCreateTime());
		preparedStatement.setString(6, data.getName());
		preparedStatement.setString(7, data.getPath());
		preparedStatement.executeUpdate();
		sqlDatabaseHelper.closePreparedStatement(preparedStatement);
	}

	public void removeTaskAttachFile(TaskAttachFile taskAttachFile) throws Exception {
		TaskAttachFileData data = taskAttachFileMapper.transformToTaskAttachFileData(taskAttachFile);
		String sql = String.format("Delete From %s Where %s = ?",
				TaskAttachFileTable.tableName,
				TaskAttachFileTable.taskAttachFileId,
				taskAttachFile.getTaskAttachFileId());
		PreparedStatement preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
		preparedStatement.setString(1, data.getTaskAttachFileId());
		preparedStatement.executeUpdate();
		sqlDatabaseHelper.closePreparedStatement(preparedStatement);
	}
	
	public Collection<TaskAttachFileData> getTaskAttachFileDatasByTaskId(String taskId){
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<TaskAttachFileData> taskAttachFileDatas = new ArrayList<>();
		try {
			String sql = String.format("Select * From %s Where %s = ? Order By %s",
					TaskAttachFileTable.tableName, 
					TaskAttachFileTable.taskId, 
					TaskAttachFileTable.createTime);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, taskId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String taskAttachFileId = resultSet.getString(TaskAttachFileTable.taskAttachFileId);
				String name = resultSet.getString(TaskAttachFileTable.name);
				String path = resultSet.getString(TaskAttachFileTable.path);
				String createTime = resultSet.getString(TaskAttachFileTable.createTime);
				
				TaskAttachFileData data = new TaskAttachFileData();
				data.setTaskAttachFileId(taskAttachFileId);
				data.setName(name);
				data.setPath(path);
				data.setTaskId(taskId);
				data.setCreateTime(createTime);

				taskAttachFileDatas.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
		}
		return taskAttachFileDatas;
	}
}
