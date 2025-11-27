package banking.async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.*;
import javax.servlet.ServletContext;
import javax.servlet.http.*;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

import banking.interfaces.AccountManager;
import banking.interfaces.UserManager;
import banking.models.Account;
import banking.models.CheckingsAccount;
import banking.models.SavingsAccount;
import banking.models.User;
import banking.services.TransactionServices;

public class UpdateAndGetJson extends ActionSupport {
	private static AccountManager AccountMgr = null;
	private static UserManager UserMgr = null;

	public UpdateAndGetJson() {
		try {
			Class accountMgrClass = Class.forName("banking.dao.AccountDAO");
			AccountMgr = (AccountManager) accountMgrClass.newInstance();

			Class userMgrClass = Class.forName("banking.dao.UserDAO");
			UserMgr = (UserManager) userMgrClass.newInstance();

		} catch (Exception e) {
			System.out.println("Exception occured getting Manager");
		}
	}

	public JSONArray jsonArray = new JSONArray();
	private String uid, aid, name, passwd, address, phone, response, tpin, atype;
	private double balance, rate, overdraft;

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setOverdraft(double od) {
		this.overdraft = od;
	}

	public void setAtype(String type) {
		this.atype = type;
	}

	public String getAtype() {
		return this.atype;
	}

	public String getTpin() {
		return this.tpin;
	}

	public double getBalance() {
		return this.balance;
	}

	public double getRate() {
		return this.rate;
	}

	public double getOverdraft() {
		return this.overdraft;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return this.uid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAid() {
		return this.aid;
	}

	public String getString() {
		return this.response;
	}

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String execute() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		List<User> list = new ArrayList<>();
		try {
			UserMgr.DeleteUser(uid);
			list = UserMgr.getAllUsers();
			Iterator<User> iterator = list.iterator();
			while (iterator.hasNext()) {
				User tempuser = iterator.next();
				JSONObject json = new JSONObject(tempuser);
				jsonArray.put(json);
			}
			out.print(jsonArray.toString());
			response = jsonArray.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		out.flush();
		out.close();
		return SUCCESS;
	}

	public String savings() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		List<SavingsAccount> list = new ArrayList<>();
		try {
			AccountMgr.deleteSavingsAccount(aid);
			list = AccountMgr.getAllSavingsAccount(true);
			Iterator<SavingsAccount> iterator = list.iterator();
			while (iterator.hasNext()) {
				SavingsAccount account = iterator.next();
				JSONObject json = new JSONObject(account);
				jsonArray.put(json);
			}
			out.print(jsonArray.toString());
			response = jsonArray.toString();
			ServletActionContext.getRequest().getSession(false).setAttribute("empverified", "true");
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		out.flush();
		out.close();
		return SUCCESS;

	}

	public String checkings() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		List<CheckingsAccount> list = new ArrayList<>();
		try {
			AccountMgr.deleteCheckingsAccount(aid);
			list = AccountMgr.getAllCheckingsAccount();
			Iterator<CheckingsAccount> iterator = list.iterator();
			while (iterator.hasNext()) {
				CheckingsAccount account = iterator.next();
				JSONObject json = new JSONObject(account);
				jsonArray.put(json);
			}
			out.print(jsonArray.toString());
			response = jsonArray.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		out.flush();
		out.close();
		return SUCCESS;

	}

	public String makeUser() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		AtomicLong uid = new AtomicLong(100);
		List<User> list = new ArrayList<>();
		try {
			long id = UserMgr.getLastUserId();
			if (id > 0) {
				id++;
				uid.set(id);
			}
			User user = new User(uid.toString(), name, address, phone);
			UserMgr.createUser(user, passwd);

			list = UserMgr.getAllUsers();
			Iterator<User> iterator = list.iterator();
			while (iterator.hasNext()) {
				User tempuser = iterator.next();
				JSONObject json = new JSONObject(tempuser);
				jsonArray.put(json);
			}
			out.print(jsonArray.toString());
			response = jsonArray.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		out.flush();
		out.close();
		return SUCCESS;

	}

	public String makeAccount() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		AtomicLong aid = new AtomicLong(100001);
		System.out.println(aid.toString());
		try {
			long id = AccountMgr.getLastAccountId();
			if (id > 0) {
				id++;
				aid.set(id);
			}
			User user = new User(uid, name, address, phone);
			if (atype.equals("savings")) {
				List<SavingsAccount> list = new ArrayList<>();
				SavingsAccount account = new SavingsAccount(aid.toString(), user, balance, rate);
				AccountMgr.createSavingsAccount(account, tpin);
				list = AccountMgr.getAllSavingsAccount(true);
				Iterator<SavingsAccount> iterator = list.iterator();
				while (iterator.hasNext()) {
					account = iterator.next();
					JSONObject json = new JSONObject(account);
					jsonArray.put(json);
				}
				out.print(jsonArray.toString());
				response = jsonArray.toString();
			} else {
				List<CheckingsAccount> list = new ArrayList<>();
				CheckingsAccount account = new CheckingsAccount(aid.toString(), user, balance, overdraft);
				AccountMgr.createCheckingsAccount(account, tpin);
				list = AccountMgr.getAllCheckingsAccount();
				Iterator<CheckingsAccount> iterator = list.iterator();
				while (iterator.hasNext()) {
					account = iterator.next();
					JSONObject json = new JSONObject(account);
					jsonArray.put(json);
				}
				out.print(jsonArray.toString());
				response = jsonArray.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		out.flush();
		out.close();
		return SUCCESS;
	}

	public String updateSavings() throws Exception {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		List<SavingsAccount> list = new ArrayList<>();
		try {
			AccountMgr.UpdateRate(String.valueOf(rate), aid);
			list = AccountMgr.getAllSavingsAccount(true);
			Iterator<SavingsAccount> iterator = list.iterator();
			while (iterator.hasNext()) {
				SavingsAccount account = iterator.next();
				JSONObject json = new JSONObject(account);
				jsonArray.put(json);
			}
			out.print(jsonArray.toString());
			response = jsonArray.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		out.flush();
		out.close();
		return SUCCESS;

	}

	public String updateCheckings() throws Exception {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		List<CheckingsAccount> list = new ArrayList<>();
		try {
			AccountMgr.UpdateOverDraft(String.valueOf(overdraft), aid);
			list = AccountMgr.getAllCheckingsAccount();
			Iterator<CheckingsAccount> iterator = list.iterator();
			while (iterator.hasNext()) {
				CheckingsAccount account = iterator.next();
				JSONObject json = new JSONObject(account);
				jsonArray.put(json);
			}
			out.print(jsonArray.toString());
			response = jsonArray.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		out.flush();
		out.close();
		return SUCCESS;

	}

	public String getUser() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		try {
			User user = UserMgr.getUser(uid);
			JSONObject obj = new JSONObject(user);
			out.write(obj.toString());
		} catch (Exception e) {
			// pass
		}
		out.flush();
		out.close();
		return SUCCESS;

	}

	public String fetchBalance() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		String aid = ServletActionContext.getRequest().getParameter("aid");
		Account account;

		try {
			account = AccountMgr.getAccountById(aid);
			JSONObject obj = new JSONObject(account);

			out.print(account.toString());
		} catch (Exception e) {
			// pass
			out.print("error");
			e.printStackTrace();
		}
		out.flush();
		out.close();
		return SUCCESS;
	}

	public String fetchBankRecords() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		try {
			ServletContext servletContext = ServletActionContext.getRequest().getServletContext();
			InputStream inStream = servletContext.getResourceAsStream("/WEB-INF/data.json");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				out.print(line);
			}

		} catch (Exception e) {
			// pass
			out.print("error");
			e.printStackTrace();
		}
		out.flush();
		out.close();
		return SUCCESS;
	}

	public String transferapi() throws IOException {
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		TransactionServices transactionService = new TransactionServices();
		String source_aid = ServletActionContext.getRequest().getParameter("aid");
		String destination_aid = ServletActionContext.getRequest().getParameter("destination_aid");
		String Mypin = ServletActionContext.getRequest().getParameter("tpin");
		String transfer_amount = ServletActionContext.getRequest().getParameter("amount");
		try {		
			if (Double.parseDouble(transfer_amount) > 500) {
				throw new Exception("Transfer amount cannot exceed 500.");
			}
			Account source = AccountMgr.getAccountById(source_aid);
			Account destination = AccountMgr.getAccountById(destination_aid);
			String myhashedtpin = AccountMgr.getTpin(source_aid);
			String hashedtpin = AccountMgr.getMD5(Mypin);

			if (myhashedtpin.equals(hashedtpin)) {
				transactionService.transfer(source, destination, Double.parseDouble(transfer_amount));
			} else {
				throw new Exception("incorrect tpin entered! ");
			}
			source = AccountMgr.getAccountById(source_aid);
			destination = AccountMgr.getAccountById(destination_aid);
			out.println("Transfer success!");
			out.println("Source AccountID:" + source_aid);
			out.println("Current Balance: " + source.getBalance());

		} catch (Exception e) {
			out.print("error while invoking API");
			e.printStackTrace();
		}
		out.flush();
		out.close();
		return SUCCESS;

	}

}
