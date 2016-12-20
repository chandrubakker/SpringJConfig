package com.sorcererpaws.SpringJConfig.core.secure.handler;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.sorcererpaws.SpringJConfig.core.service.user.UserService;

@Component("customLogoutSuccessHandler")
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	@Autowired
	private UserService UserService; 
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		if(authentication != null && authentication.getDetails() != null) {
			try {
				request.getSession().invalidate();
				boolean logoutSuccess = getUserService().updateLastLogin(authentication.getName(), new Date());
				if(logoutSuccess) {
					System.out.println("SUPER");
				}
			} catch (Exception e) {
				e.printStackTrace();
				e = null;
			}
		}
		String targetUrl = request.getContextPath();
		response.setStatus(HttpServletResponse.SC_OK);
		//redirect to login
		response.sendRedirect(targetUrl+"/login?logout");
	}

	//Getters and setters
	public UserService getUserService() {
		return UserService;
	}

	public void setUserService(UserService userService) {
		UserService = userService;
	}
}