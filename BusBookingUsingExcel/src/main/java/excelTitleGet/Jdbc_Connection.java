package excelTitleGet;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Jdbc_Connection {

	public static Connection getConnection() throws SQLException {
		String jdbcUrl = "jdbc:sqlserver://localhost:1433;databaseName=Krishna_Testing;trustServerCertificate=true";

		String username = "sa";
		String password = "15848";

		Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
		if (connection != null) {
		} else {
			System.out.println("Failed to connect to the database!");
		}
		return connection;

	}

	public static void insertOperation(String title, String lastUpdated, String href)
			throws SQLException, ParseException {
		String query = "insert into Zoho_questions1(Title,CreatedDate,Link)" + "values(?,?,?)";
		Connection conn = getConnection();
		PreparedStatement pt = conn.prepareStatement(query);

		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
		java.util.Date parsedDate = formatter.parse(lastUpdated);
		java.sql.Date date = new java.sql.Date(parsedDate.getTime());

		pt.setString(1, title);
		pt.setDate(2, date);
		pt.setString(3, href);
		pt.executeUpdate();

	}

	public static int truncateTable() throws SQLException {
		Connection conn = getConnection();
		String query = "Truncate table Zoho_questions1";
		PreparedStatement pt = conn.prepareStatement(query);
		int updateNumber = pt.executeUpdate();
		System.out.println("updateNumber : " + updateNumber);
		return updateNumber;
	}

	public static void sent_email() throws SQLException {
		Connection conn = getConnection();
		CallableStatement cs = conn.prepareCall("{call zoho_questions_Email1}");
		cs.execute();
		System.out.println("Email sent successfully...");
		System.out.println("End Time : "+(System.currentTimeMillis()-WebScraperFinal.startTime)/1000+ " sec");
	}
}




