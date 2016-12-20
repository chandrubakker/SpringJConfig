package com.sorcererpaws.SpringJConfig.core.service.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sorcererpaws.SpringJConfig.core.dao.user.UserDao;
import com.sorcererpaws.SpringJConfig.core.entity.user.User;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private UserDao userDao;

	@Override
	@Transactional(readOnly = false)
	public User addUser(User user) {
		/*if(user.getId() != null) {
			user.setPassword(getEncryptedPassword(user.getPassword()));
			user.setEnabled(false);
		}else if(user.getNewPassword() != null) {
			user.setPassword(getEncryptedPassword(user.getNewPassword()));
		}*/
		user.setPassword(getEncryptedPassword(user.getPassword()));
		return /*(user.getId() != null ? getUserDao().addUser(user) : null)*/ getUserDao().addUser(user);
	}

	@Override
	@Transactional(readOnly = false)
	public User updateUser(User user) {
		return (user.getId() != null ? getUserDao().updateUser(user) : null);
	}

	@Override
	public User getUser(long userId) {
		return (userId > 0 ? getUserDao().getUser(userId) : null);
	}

	@Override
	@Transactional(readOnly = false)
	public boolean deleteUser(long userId) {
		return (userId > 0 ? getUserDao().deleteUser(userId) : false);
	}

	@Override
	public List<User> users() {
		return getUserDao().users();
	}

	@Override
	public boolean userExists(String email) {
		return ((email == null || email.isEmpty()) ? false : getUserDao().userExists(email));
	}

	@Override
	public User userByEmail(String email) {
		return ((email == null || email.isEmpty()) ? null : getUserDao().userByEmail(email));
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean updateLastLogin(String email, Date lastLogin) {
		return getUserDao().updateLastLogin(email, lastLogin);
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean activateUser(long id) {
		return getUserDao().activateUser(id);
	}

	@Override
	@Transactional(readOnly = false)
	public boolean deActivateUser(long id) {
		return getUserDao().deActivateUser(id);
	}
	
	@Override
	public String getEncryptedPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword,BCrypt.gensalt());
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean changePassword(long userId, String password) {
		return getUserDao().changePassword(userId, getEncryptedPassword(password));
	}
	
	//Getters and setters
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
