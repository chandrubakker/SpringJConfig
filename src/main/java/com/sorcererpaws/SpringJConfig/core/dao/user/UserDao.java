package com.sorcererpaws.SpringJConfig.core.dao.user;

import java.util.Date;
import java.util.List;

import com.sorcererpaws.SpringJConfig.core.entity.user.User;

public interface UserDao {

	public User addUser(User user);
	public User updateUser(User user);
	public User getUser(long userId);
	public boolean deleteUser(long userId);
	public List<User> users();
	public boolean userExists(String userEmail);
	public User userByEmail(String userEmail);
	public boolean updateLastLogin(String email, Date lastLogin);
	public boolean activateUser(long id);
	public boolean deActivateUser(long id);
	public boolean changePassword(long userId, String password);
}
