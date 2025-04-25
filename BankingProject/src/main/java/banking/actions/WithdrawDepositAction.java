package banking.actions;

import javax.servlet.http.*;

import banking.exceptions.InsufficientFundsException;
import banking.interfaces.AccountManager;
import banking.models.Account;
import banking.services.TransactionServices;
import banking.services.AccountServices;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

@SuppressWarnings("unchecked")
public class WithdrawDepositAction extends ActionSupport {
	private static AccountManager AccountMgr = null;
	HttpSession session = ServletActionContext.getRequest().getSession(false);

	public WithdrawDepositAction() {
		try {
			Class MgrClass = Class.forName("banking.dao.AccountDAO");
			AccountMgr = (AccountManager) MgrClass.newInstance();

		} catch (Exception e) {
			session.setAttribute("error", "Unable to process the request.");
			System.out.println("Exception occured getting Account Manager " + e.getMessage());

		}
	}

	public String execute() {
		String action = ServletActionContext.getRequest().getParameter("action");
		Account sourceAccount, destinationAccount;
		AccountServices services = new AccountServices();
		TransactionServices transactionService = new TransactionServices();
		if (session == null) {
			return "login";
		}
		HttpServletRequest req = ServletActionContext.getRequest();
		String aid = (String) session.getAttribute("aid");
		String uid = (String) session.getAttribute("uid");
		String amount = req.getParameter("amount");

		try {
			sourceAccount = AccountMgr.getAccountById(aid);
			if (action.equals("Deposit")) {
				services.deposit(sourceAccount, Double.parseDouble(amount));
				session.setAttribute("action", action);
			} else {
				if (action.equals("Withdraw")) {
					String tpin = (String) req.getParameter("tpin");
					String myhashedtpin = AccountMgr.getTpin(aid);
					String hashedtpin = AccountMgr.getMD5(tpin);
					if (myhashedtpin.equals(hashedtpin)) {
						services.withdraw(sourceAccount, Double.parseDouble(amount));
						session.setAttribute("action", action);

					} else {
						throw new Exception("incorrect tpin entered! ");
					}
				} else if (action.equals("transfer")) {
					String did = req.getParameter("dest-aid");
					amount = req.getParameter("tamount");
					destinationAccount = AccountMgr.getAccountById(did);

					String tpin = (String) req.getParameter("tpin");
					String myhashedtpin = AccountMgr.getTpin(aid);
					String hashedtpin = AccountMgr.getMD5(tpin);

					if (myhashedtpin.equals(hashedtpin)) {
						transactionService.transfer(sourceAccount, destinationAccount, Double.parseDouble(amount));
						session.setAttribute("did", did);
						session.setAttribute("action", action);
						session.setAttribute("transaction", "transfer");
					} else {
						throw new Exception("incorrect tpin entered! ");
					}
				}

			}

			sourceAccount = AccountMgr.getAccountById(aid);
			session.setAttribute("amount", amount);
			session.setAttribute("balance", sourceAccount.getBalance());
		} catch (InsufficientFundsException ex) {
			session.setAttribute("error", ex.getMessage());
			return SUCCESS;
		} catch (Exception e) {
			session.setAttribute("error", e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
}
