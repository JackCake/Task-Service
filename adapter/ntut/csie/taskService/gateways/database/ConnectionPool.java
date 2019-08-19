package ntut.csie.taskService.gateways.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConnectionPool {
	private static ConnectionPool instance = null;
	private static List<Connection> availableConnections;
	private static Set<Connection> usedConnections;
	private final int MAX_CONNECTIONS = 5;
	
	private String serverUrl;
	private String databaseName;
	private String account;
	private String password;
	
	private ConnectionPool() {}
	
	public static synchronized ConnectionPool getInstance() {
		if(instance == null){
			instance = new ConnectionPool();
		}
		return instance;
	}
	
	public void initialize(String serverUrl, String databaseName, String account, String password) {
		availableConnections = new ArrayList<>();
		usedConnections = new HashSet<>();
		
		this.serverUrl = serverUrl;
		this.databaseName = databaseName;
		this.account = account;
		this.password = password;

		for (int i = 0; i <MAX_CONNECTIONS; i++) {
			availableConnections.add(this.createConnection());
		}
	}

	private Connection createConnection(){
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://"+serverUrl+":3306/" + databaseName + "?useSSL=false&autoReconnect=true", account, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static synchronized Connection getConnection() {
		if (availableConnections.size() == 0) {
			System.out.println("All connections are Used !!");
			return null;
		} else {
			Connection connection = availableConnections.remove(availableConnections.size() - 1);
			usedConnections.add(connection);
			return connection;
		}
	}

	public static synchronized void releaseConnection(Connection connection) {
		if (connection != null) {
			usedConnections.remove(connection);
			availableConnections.add(connection);
		}
	}
}
