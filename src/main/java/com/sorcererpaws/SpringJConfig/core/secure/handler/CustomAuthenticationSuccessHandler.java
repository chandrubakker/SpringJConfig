package com.sorcererpaws.SpringJConfig.core.secure.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sorcererpaws.SpringJConfig.core.service.user.UserService;


@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
					throws IOException, ServletException {

		logger.info("authentication success");
		logger.info("authenticated email : "+authentication.getName());
		logger.info("setting target URL");
		
		String targetUrl = request.getContextPath();
		logger.info("context path : "+targetUrl);
		
		setDefaultTargetUrl(request.getContextPath()+"/postLoginAction");
		logger.info("redirecting to target URL");
		
		response.sendRedirect(getDefaultTargetUrl());
	}

	//Getters and setters
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
