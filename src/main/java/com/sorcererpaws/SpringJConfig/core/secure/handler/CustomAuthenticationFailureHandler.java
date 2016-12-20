package com.sorcererpaws.SpringJConfig.core.secure.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.sorcererpaws.SpringJConfig.core.entity.user.Role;
import com.sorcererpaws.SpringJConfig.core.entity.user.User;
import com.sorcererpaws.SpringJConfig.core.service.user.UserService;

@Component("customAuthenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
	
	@Autowired
	private UserService userService; 
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		logger.info("authentication failed");
		logger.info("disabling session creation");
		
		setAllowSessionCreation(false);
		logger.info("redirecting to login page");
		
		List<User> users = getUserService().users();
		boolean thereIsAdmin = false;
		
		for(User admin : users){
			for(Role role: admin.getRoles()) {
				if(role.getName().equalsIgnoreCase("ROLE_ADMIN")){
					thereIsAdmin = true;
					break;
				}
			}
		}
		
		if(thereIsAdmin){
			response.sendRedirect(request.getContextPath()+"/login");
		}else {
			response.sendRedirect(request.getContextPath()+"/home");
		}
	}

	//Getters and Setters
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
