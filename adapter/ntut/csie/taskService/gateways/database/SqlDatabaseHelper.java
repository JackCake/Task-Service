package ntut.csie.taskService.gateways.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDatabaseHelper {
	private String serverUrl;
	private String databaseName;
	private String account;
	private String password;
	private Connection connection;
	private boolean transacting;
	
	public SqlDatabaseHelper() {
		serverUrl = "127.0.0.1";
		databaseName = "task_service";
		account = "root";
		password = "root";
		transacting = false;
	}
	
	public void initialize() {
		createTaskServiceDatabase();
		ConnectionPool.getInstance().initialize(serverUrl, databaseName, account, password);
		createTaskTable();
		createEventTable();
	}
	
	public void connectToDatabase() {
		try {
			connection = ConnectionPool.getConnection();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createTaskServiceDatabase() {
		Statement statement = null;
		String sql = "Create Database If Not Exists " + databaseName;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://"+serverUrl+":3306?useSSL=false&autoReconnect=true", account, password);
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeStatement(statement);
			closeConnection();
		}
	}
	
	private void createTaskTable() {
		connectToDatabase();
		Statement statement = null;
		String sql = "Create Table If Not Exists " + TaskTable.tableName + " ("
				+ TaskTable.taskId + " Varchar(50) Not Null, "
				+ TaskTable.orderId + " Integer Not Null, "
				+ TaskTable.description + " Varchar(256) Not Null, "
				+ TaskTable.handlerId + " Varchar(50), "
				+ TaskTable.status + " Varchar(50) Not Null, "
				+ TaskTable.estimate + " TinyInt(3) Unsigned Not Null Default '0', "
				+ TaskTable.remains + " TinyInt(3) Unsigned Not Null Default '0', "
				+ TaskTable.notes + " Text, "
				+ TaskTable.backlogItemId + " Varchar(50), "
				+ "Primary Key (" + TaskTable.taskId + ") "
				+ ")";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(statement);
			releaseConnection();
		}
	}
	
	private void createEventTable() {
		connectToDatabase();
		Statement statement = null;
		String sql = "Create Table If Not Exists " + EventTable.tableName + " ("
				+ EventTable.eventId + " Int(11) Not Null Auto_Increment, "
				+ EventTable.eventBody + " LongText Not Null, "
				+ EventTable.eventType + " Varchar(250) Not Null, "
				+ "Primary Key (" + EventTable.eventId + ")"
				+ ")";
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(statement);
			releaseConnection();
		}
	}
	
	public boolean isTransacting() {
		return transacting;
	}
	
	public void transactionStart() throws SQLException {
		connection.setAutoCommit(false);
		transacting = true;
	}
	
	public void transactionError(){
		try {
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void transactionEnd() throws SQLException {
		connection.commit();
		connection.setAutoCommit(true);
		transacting = false;
	}
	
	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		return preparedStatement;
	}
	
	public ResultSet getResultSet(String query) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		return resultSet;
	}
	
	public void closeStatement(Statement statement) {
		if(statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closePreparedStatement(PreparedStatement preparedStatement) {
		if(preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeResultSet(ResultSet resultSet) {
		if(resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void closeConnection() {
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void releaseConnection() {
		ConnectionPool.releaseConnection(connection);
	}
}
