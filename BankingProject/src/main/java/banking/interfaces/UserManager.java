package banking.interfaces;

import java.sql.SQLException;
import java.util.List;

import banking.exceptions.UserNotFoundException;
import banking.models.User;

public interface UserManager {
    public long createUser(User user, String passwd) throws SQLException;

    public boolean Login(String uild, String passwd) throws SQLException;

    public int DeleteUser(String uid);

    public List<User> getAllUsers() throws SQLException;

    public String getPasswd(String user_id) throws SQLException;

    public String getMD5(String passwd) throws SQLException;

    public void UpdateUser(String userId, String name, String address, String phone);

    public User getUser(String userId) throws SQLException, UserNotFoundException;

    public long getLastUserId() throws SQLException;
}
