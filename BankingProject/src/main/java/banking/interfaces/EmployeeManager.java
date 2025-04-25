package banking.interfaces;

import java.sql.SQLException;

public interface EmployeeManager {
	public String getPassword(String empid);

	public String getEmployeeName(String empid);

	public String getMD5(String pin) throws SQLException;
}
