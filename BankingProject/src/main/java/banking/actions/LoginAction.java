package banking.actions;

import banking.models.User;
import banking.interfaces.UserManager;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {
	private static UserManager UserMgr = null;
	private String userId, passwd;
	private User user;
	HttpSession session = ServletActionContext.getRequest().getSession();

	public LoginAction() {
		try {
			Class MgrClass = Class.forName("banking.dao.UserDAO");
			UserMgr = (UserManager) MgrClass.newInstance();

		} catch (Exception e) {
			System.out.println("Exception occured getting User Manager");
		}
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String execute() throws Exception {
		try {
			if (UserMgr.Login(this.userId, this.passwd)) {
				this.user = UserMgr.getUser(this.userId);

				session.setAttribute("uverified", "true");
				session.setAttribute("uid", user.getUserId());
				session.setAttribute("name", user.getName());
				session.setAttribute("address", user.getAddress());
				session.setAttribute("phone", user.getPhoneNumber());
				return SUCCESS;
			} else {
				throw new Exception("Invalid Password");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
}
