package cc.shinbi.tsubuyaki.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
	private static final String DB_DRIVER = "jdbc:mysql://localhost/tsubuyaki";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	public static Connection connect() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(
				DB_DRIVER,
				DB_USER,
				DB_PASSWORD
		);
		return connection;
	}
}