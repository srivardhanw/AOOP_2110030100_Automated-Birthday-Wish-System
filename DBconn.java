

import java.sql.*;
public class DBconn {

	static String url = "jdbc:mysql://localhost:3306/db-name?characterEncoding=utf8"; //replace dbname with your database name
	static Connection conn;
	static String username = ""; // mysql username
	static String password = ""; // mysql password
	
	public static void connect() throws ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("Connecting to database...");
		try {
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected successfully!");
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect to the database!", e);
		}
		
	}
	
}
