package banking.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;


public class MySQLConnection {
	private static String url;
	private static String username;
	private static String password;

	public static Connection getConnection() throws SQLException {

		try {
			Properties props = new Properties();
			ServletContext context = ServletActionContext.getServletContext();
			InputStream in = context.getResourceAsStream("/WEB-INF/DbConfiguration.properties");
			props.load(in);
			in.close();
			String driver = props.getProperty("jdbc.driver");

			url = props.getProperty("db.url").replace("{port}", props.getProperty("db.port")).replace("{DbName}",
					props.getProperty("db.name"));
			username = props.getProperty("db.username");
			password = props.getProperty("db.password");

			if (driver != null) {
				Class.forName(driver);
			} else {
				throw new Exception("Driver name not found in configuration file");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("MySQL JDBC Driver not found", e);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return DriverManager.getConnection(url, username, password);
	}

	// private static final String JDBC_URL =
	// "jdbc:mysql://localhost:3306/banking_app";
	// private static final String USERNAME = "testsql";
	// private static final String PASSWORD = "sqlpass";

}