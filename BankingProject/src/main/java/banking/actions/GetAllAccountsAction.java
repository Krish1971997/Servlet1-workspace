package banking.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import banking.interfaces.AccountManager;
import banking.models.CheckingsAccount;
import banking.models.SavingsAccount;

public class GetAllAccountsAction extends ActionSupport {
        private static AccountManager AccountMgr = null;

        public GetAllAccountsAction() {
                try {
                        Class MgrClass = Class.forName("banking.dao.AccountDAO");
                        AccountMgr = (AccountManager) MgrClass.newInstance();

                } catch (Exception e) {
                        System.out.println("Exception occured getting Account Manager");
                }
        }

        public String execute() {

                HttpSession session = ServletActionContext.getRequest().getSession(false);
                List<SavingsAccount> list = null;
                List<CheckingsAccount> list2 = new ArrayList<>();

                boolean everified = (String)session.getAttribute("empverified") != null ? true : false;

                try {
                        if (everified == true) {
                                list = AccountMgr.getAllSavingsAccount(false);
                        }else{
                                list = AccountMgr.getAllSavingsAccount(true);
                        }
                        list2 = AccountMgr.getAllCheckingsAccount();
                        ServletActionContext.getRequest().setAttribute("saccounts", list);
                        ServletActionContext.getRequest().setAttribute("caccounts", list2);
                } catch (Exception e) {
                        session.setAttribute("error", "Exception occured while fetching Accounts");
                        return ERROR;
                }
                return SUCCESS;
        }
}
