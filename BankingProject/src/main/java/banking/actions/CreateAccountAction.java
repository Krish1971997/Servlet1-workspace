package banking.actions;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import banking.interfaces.AccountManager;
import banking.models.CheckingsAccount;
import banking.models.SavingsAccount;
import banking.models.User;;

public class CreateAccountAction extends ActionSupport {
	private String rate, balance, tpin, type, overdraft;
	private static AccountManager AccountMgr = null;

	public CreateAccountAction() {
		try {

			Class MgrClass = Class.forName("banking.dao.AccountDAO");
			AccountMgr = (AccountManager) MgrClass.newInstance();

		} catch (Exception e) {
			System.out.println("Exception occured getting Account Manager");
		}

	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public void setTpin(String pin) {
		this.tpin = pin;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setOverdraft(String od) {
		this.overdraft = od;
	}

	public String getOverdraft() {
		return this.overdraft;
	}

	public String getType() {
		return this.type;
	}

	public String getTpin() {
		return this.tpin;
	}

	public String getRate() {
		return this.rate;
	}

	public String getBalance() {
		return this.balance;
	}

	public String execute() {
		HttpSession session = ServletActionContext.getRequest().getSession(false);
		if (session == null || session.getAttribute("uverified") != "true") {
			return "login";
		}
		if (session.getAttribute("aid") != null) {
			return SUCCESS;
		}
		AtomicLong aid = new AtomicLong(100001);
		User user;
		try {
			long id = AccountMgr.getLastAccountId();
			if (id > 1) {
				id++;
				aid.set(id);
			}
		} catch (Exception e) {
			session.setAttribute("error", "Error While creating Account Id. Please try Again Later");
			return ERROR;
		}

		String uid = (String) session.getAttribute("uid");
		String name = (String) session.getAttribute("name");
		String address = (String) session.getAttribute("address");
		String phone = (String) session.getAttribute("phone");
		user = new User(uid, name, address, phone);
		if (type.charAt(0) == 's') {
			SavingsAccount account = new SavingsAccount(aid.toString(), user, Double.parseDouble(this.balance),
					Double.parseDouble(this.rate));
			int ress = 2;
			try {
				ress = AccountMgr.createSavingsAccount(account, this.tpin);
			} catch (Exception e) {
				session.setAttribute("error", "Error While creating Savings Account. Please try Again Later");
				return ERROR;
			}
			session.setAttribute("type", type);
			session.setAttribute("rate", rate);

		} else {
			CheckingsAccount accountt = new CheckingsAccount(aid.toString(), user, Double.parseDouble(this.balance),
					Double.parseDouble(this.overdraft));
			int ress = 2;
			try {
				ress = AccountMgr.createCheckingsAccount(accountt, this.tpin);
			} catch (Exception e) {
				session.setAttribute("error", "Error While creating Checkings Account. Please try Again Later");
				return ERROR;
			}
			session.setAttribute("type", type);
			session.setAttribute("overdraft", overdraft);
		}

		session.setAttribute("aid", String.valueOf(aid));
		session.setAttribute("balance", balance);
		return SUCCESS;

	}
}
