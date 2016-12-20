package com.sorcererpaws.SpringJConfig.web;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sorcererpaws.SpringJConfig.core.entity.user.User;
import com.sorcererpaws.SpringJConfig.core.service.user.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserService userService;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value = "/dash", method = RequestMethod.GET)
	public String dash() {
		
		return "dash";
	}
	
	@RequestMapping(value = "/postLoginAction" , method = RequestMethod.GET)
	public String processLoginAction(Authentication authentication, HttpSession httpSession) {
		User loggedInUser = null;
		boolean isAdmin = false;
		if(authentication != null){
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {
				if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
					isAdmin = true;
					break;
				}
			}
			
			if(isAdmin){
				
				logger.info("logged in user : "+authentication.getName());
				loggedInUser = userService.userByEmail(authentication.getName());
				httpSession.setAttribute("loggedInUser", loggedInUser);
				return "redirect:/dash";
			} else {
				
				throw new IllegalStateException("Not authorized user.");
			}
		}else {
			
			logger.info("Authentication failed.");
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView LoginPage(@RequestParam(value = "logout" , required = false)String logout, @RequestParam(value = "sessionExp" , required = false)String sessionExp, Principal principal) {
		
		ModelAndView modelAndView =  new ModelAndView();
		
		if(principal == null) {
			modelAndView.addObject("user", new User());
			if(logout != null){
				logout = "You Have Been Successfully Logged Out";
				modelAndView.addObject("logout", logout);
			}
			
			if(sessionExp != null){
				sessionExp = "Your Session Expired! Login Again";
				modelAndView.addObject("sessionExp", sessionExp);
			}
			modelAndView.setViewName("login");
		}else {
			modelAndView.setViewName("redirect:/");
		}
		return modelAndView;
	}
}
