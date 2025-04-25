package banking.actions;

import banking.models.User;
import banking.interfaces.UserManager;

import javax.servlet.http.*;
import java.util.*;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class UpdateUserAction extends ActionSupport {
	private static UserManager UserMgr = null;

	public UpdateUserAction() {
		try {

			Class MgrClass = Class.forName("banking.dao.UserDAO");
			UserMgr = (UserManager) MgrClass.newInstance();

		} catch (Exception e) {
			System.out.println("Exception occured getting User Manager");
		}

	}

	public String execute() {
		HttpSession session = ServletActionContext.getRequest().getSession(false);
		HttpServletRequest req = ServletActionContext.getRequest();
		String id = req.getParameter("uid");
		String name = req.getParameter("name");
		String address = req.getParameter("address");
		String phone = req.getParameter("phone");
		User user;
		try {
			UserMgr.UpdateUser(id, name, address, phone);
			user = UserMgr.getUser(id);
			session.setAttribute("uid", user.getUserId());
			session.setAttribute("phone", user.getPhoneNumber());
			session.setAttribute("name", user.getName());
			session.setAttribute("address", user.getAddress());
		} catch (Exception e) {
			session.setAttribute("error", e.getMessage());
			session.setAttribute("alert", "alert(" + e.getMessage() + ")");
			return ERROR;
		}
		session.setAttribute("alert", "alert('User Updated Successfully')");
		return SUCCESS;

	}
}
