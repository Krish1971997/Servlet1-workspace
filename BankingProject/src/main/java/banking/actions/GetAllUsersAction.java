package banking.actions;

import banking.models.User;
import banking.interfaces.UserManager;

import org.json.*;
import java.util.*;
import javax.servlet.http.*;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class GetAllUsersAction extends ActionSupport {
	private static UserManager UserMgr = null;

	public GetAllUsersAction() {
		try {
			Class MgrClass = Class.forName("banking.dao.UserDAO");
			UserMgr = (UserManager) MgrClass.newInstance();

		} catch (Exception e) {
			System.out.println("Exception occured getting User Manager");
		}
	}

	public String execute() {
		HttpSession session = ServletActionContext.getRequest().getSession(false);
		JSONArray jsonArray = new JSONArray();
		List<User> list = new ArrayList<>();

		try {
			list = UserMgr.getAllUsers();
			Iterator<User> iterator = list.iterator();
			while (iterator.hasNext()) {
				User tempuser = iterator.next();
				JSONObject json = new JSONObject(tempuser);
				jsonArray.put(json);
			}
			// System.out.println(jsonArray.toString());

			ServletActionContext.getRequest().setAttribute("users", list);
		} catch (Exception e) {
			session.setAttribute("error", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
}
