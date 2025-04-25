package banking.actions;

import javax.servlet.http.*;

import banking.dao.AccountDAO;
import banking.dao.EmployeeDAO;
import banking.interfaces.AccountManager;
import banking.interfaces.EmployeeManager;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class EmployeeLoginAction extends ActionSupport {
	private static EmployeeManager EmployeeMgr = null;

	public EmployeeLoginAction() {
		try {

			Class MgrClass = Class.forName("banking.dao.EmployeeDAO");
			EmployeeMgr = (EmployeeManager) MgrClass.newInstance();

		} catch (Exception e) {
			System.out.println("Exception occured getting Employee Manager");
		}
	}

	public String execute() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletRequest req = ServletActionContext.getRequest();
		String empid = (String) req.getParameter("empid");
		String passwd = (String) req.getParameter("passwd");
		try {

			String hashedpasswd = EmployeeMgr.getMD5(passwd);
			String storedpasswd = EmployeeMgr.getPassword(empid);
			if (hashedpasswd.equals(storedpasswd)) {
				session.setAttribute("empid", empid);
				session.setAttribute("ename", EmployeeMgr.getEmployeeName(empid));
				session.setAttribute("everified", "true");
			} else {
				throw new Exception("Incorrect password entered");
			}
		} catch (Exception e) {
			session.setAttribute("error", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
}
