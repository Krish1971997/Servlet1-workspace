package banking.actions;

import banking.models.Account;
import banking.interfaces.AccountManager;

import javax.servlet.http.*;
import java.util.*;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class UpdateCheckingsAccountAction extends ActionSupport {
        private static AccountManager AccountMgr = null;

        public UpdateCheckingsAccountAction() {
                try {
                        Class MgrClass = Class.forName("banking.dao.AccountDAO");
                        AccountMgr = (AccountManager) MgrClass.newInstance();

                } catch (Exception e) {
                        System.out.println("Exception occured getting Account Manager");
                }
        }

        public String execute() {
                HttpSession session = ServletActionContext.getRequest().getSession(false);

                HttpServletRequest req = ServletActionContext.getRequest();
                String aid = req.getParameter("aid");
                String od = req.getParameter("overdraft");
                Account account;

                try {
                        AccountMgr.UpdateOverDraft(od, aid);
                        account = AccountMgr.getAccountById(aid);
                        req.setAttribute("aid", account.getAccountId());
                        req.setAttribute("uid", account.getAccountHolder().getUserId());
                        req.setAttribute("balance", account.getBalance());
                        req.setAttribute("overdraft", account.getOverDraftLimit());
                        req.setAttribute("type", "checkings");
                } catch (Exception e) {
                        session.setAttribute("error", e.getMessage());
                        return ERROR;
                }
                return SUCCESS;

        }
}
