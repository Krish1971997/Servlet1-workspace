package banking.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import banking.interfaces.AccountManager;
import banking.models.Account;
import banking.models.SavingsAccount;

public class AccountLoginAction extends ActionSupport {
	private static AccountManager AccountMgr = null;

	public AccountLoginAction() {
		try {

			Class MgrClass = Class.forName("banking.dao.AccountDAO");
			AccountMgr = (AccountManager) MgrClass.newInstance();

		} catch (Exception e) {
			System.out.println("Exception occured getting Account Manager");
		}

	}

	public String execute() {
		HttpSession session = ServletActionContext.getRequest().getSession(false);
		if (session == null) {
			return "lndex";
		}
		HttpServletRequest req = ServletActionContext.getRequest();
		Account account;
		String aid = req.getParameter("aid");
		try {
			if (session.getAttribute("uid").equals(AccountMgr.getAccountHolderUserId(aid))) {

				account = AccountMgr.getAccountById(aid);
				session.setAttribute("aid", account.getAccountId());
				session.setAttribute("balance", account.getBalance());
				if (account instanceof SavingsAccount) {
					session.setAttribute("rate", String.valueOf(account.getRate()));
					session.setAttribute("type", "savings");
				} else {
					session.setAttribute("overdraft", String.valueOf(account.getOverDraftLimit()));
					session.setAttribute("type", "Checkings");
				}
			} else {
				throw new Exception("User Not Authorized");
			}
		} catch (Exception e) {
			session.setAttribute("error", e.getMessage());
			return ERROR;
		}
		return SUCCESS;

	}
}
