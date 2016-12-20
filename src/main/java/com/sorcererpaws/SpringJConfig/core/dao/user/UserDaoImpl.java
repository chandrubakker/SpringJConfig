package com.sorcererpaws.SpringJConfig.core.dao.user;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.sorcererpaws.SpringJConfig.core.entity.user.User;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public User addUser(User user) {
		getEntityManager().persist(user);
		return user;
	}

	@Override
	public User updateUser(User user) {
		user = (User) getEntityManager().merge(user);
		return user;
	}

	@Override
	public User getUser(long userId) {
		return (User) getEntityManager().find(User.class, userId);
	}

	@Override
	public boolean deleteUser(long userId) {
		return ((getEntityManager().createQuery("DELETE FROM User U WHERE U.id = :userId", User.class)
				.setParameter("userId", userId)
				.executeUpdate()) > 0 ? true : false);
	}

	@Override
	public List<User> users() {
		TypedQuery<User> typedQuery = getEntityManager().createQuery("FROM User U", User.class);
		return typedQuery.getResultList();
	}

	@Override
	public boolean userExists(String userEmail) {
		TypedQuery<User> typedQuery = getEntityManager()
				.createQuery("FROM User U WHERE U.email = :userEmail", User.class);
		typedQuery.setParameter("userEmail", userEmail);
		return (typedQuery.getResultList().isEmpty() ? false : true);
	}

	@Override
	public User userByEmail(String userEmail) {
		TypedQuery<User> typedQuery = getEntityManager()
				.createQuery("SELECT U FROM User U WHERE U.email = :email", User.class);
		typedQuery.setParameter("email", userEmail);
		try {
			return typedQuery.getSingleResult();
		} catch (NonUniqueResultException e) {
			return null;
		}
	}

	@Override
	public boolean updateLastLogin(String email, Date lastLogin) {
		String hQuery = "UPDATE User U SET U.lastLogin = :lastLogin WHERE U.email = :email";
		return ((getEntityManager().createQuery(hQuery)
				.setParameter("email", email)
				.setParameter("lastLogin", lastLogin, TemporalType.TIMESTAMP)
				.executeUpdate()) > 0 ? true : false);
	}

	@Override
	public boolean activateUser(long id) {
		String hQuery = "UPDATE User U SET U.enabled = :enabled WHERE U.id = :userId";
		return ((getEntityManager().createQuery(hQuery)
				.setParameter("enabled", true)
				.setParameter("userId", id)
				.executeUpdate()) > 0 ? true : false);
	}

	@Override
	public boolean deActivateUser(long id) {
		String hQuery = "UPDATE User U SET U.enabled = :enabled WHERE U.id = :userId";
		return ((getEntityManager().createQuery(hQuery)
				.setParameter("enabled", false)
				.setParameter("userId", id)
				.executeUpdate()) > 0 ? true : false);
	}

	@Override
	public boolean changePassword(long userId, String password) {
		String hQuery = "UPDATE User U SET U.password = :password WHERE U.id = :userId";
		return ((getEntityManager().createQuery(hQuery)
				.setParameter("password", password)
				.setParameter("userId", userId)
				.executeUpdate()) > 0 ? true : false);
	}

	//Getters and setters
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
