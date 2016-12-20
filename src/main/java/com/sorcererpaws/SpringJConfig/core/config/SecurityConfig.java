package com.sorcererpaws.SpringJConfig.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sorcererpaws.SpringJConfig.core.secure.CustomUserDetailsService;
import com.sorcererpaws.SpringJConfig.core.secure.handler.CustomAuthenticationFailureHandler;
import com.sorcererpaws.SpringJConfig.core.secure.handler.CustomAuthenticationSuccessHandler;
import com.sorcererpaws.SpringJConfig.core.secure.handler.CustomLogoutSuccessHandler;


@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	@Autowired
	private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {    
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordencoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
		.antMatchers("/technician/**").access("hasRole('ROLE_TECHNICIAN')")
		.antMatchers("/doctor/**").access("hasRole('ROLE_DOCTOR')")
		.antMatchers("/patient/**").access("hasRole('ROLE_PATIENT')")
		.anyRequest().permitAll()
		.and()
		.formLogin().loginPage("/login").loginProcessingUrl("/processLogin")
		.failureHandler(customAuthenticationFailureHandler)
		.successHandler(customAuthenticationSuccessHandler)
		.usernameParameter("email").passwordParameter("password")
		.and()
		.logout().logoutSuccessUrl("/login?logout")
		.logoutSuccessHandler(customLogoutSuccessHandler)
		.and()
		.exceptionHandling().accessDeniedPage("/403")
		.and()
		.csrf();
	}

	@Bean(name="passwordEncoder")
	public PasswordEncoder passwordencoder(){
		return new BCryptPasswordEncoder();
	}
}
