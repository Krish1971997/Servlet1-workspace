package banking.actions;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

import banking.interfaces.UserManager;
import banking.models.User;

public class CreateUserAction extends ActionSupport {
	private static UserManager UserMgr = null;

	public CreateUserAction() {
		try {

			Class MgrClass = Class.forName("banking.dao.UserDAO");
			UserMgr = (UserManager) MgrClass.newInstance();

		} catch (Exception e) {
			System.out.println("Exception occured getting User Manager");
		}

	}

	public String execute() {

		HttpSession session = ServletActionContext.getRequest().getSession();
		AtomicLong uid = new AtomicLong(100);
		try {

			long id = UserMgr.getLastUserId();
			if (id > 0) {
				id++;
				uid.set(id);
			}
		} catch (Exception e) {
			session.setAttribute("error", "Error while generating user id try again later");
			return ERROR;
		}

		String name = ServletActionContext.getRequest().getParameter("name");
		String address = ServletActionContext.getRequest().getParameter("address");
		String phone = ServletActionContext.getRequest().getParameter("phone");
		String passwd = ServletActionContext.getRequest().getParameter("passwd");
		User user = new User(uid.toString(), name, address, phone);

		try {
			UserMgr.createUser(user, passwd);
		} catch (Exception e) {
			session.setAttribute("error", "Error while creating user try again later");
			return ERROR;
		}
		session.setAttribute("uid", uid);
		session.setAttribute("name", name);
		session.setAttribute("address", address);
		session.setAttribute("phone", phone);

		session.setAttribute("alert", "alert('User Created Successfully')");
		return SUCCESS;

	}
}
