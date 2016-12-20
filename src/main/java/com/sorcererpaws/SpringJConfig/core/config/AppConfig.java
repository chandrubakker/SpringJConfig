package com.sorcererpaws.SpringJConfig.core.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

@SuppressWarnings("deprecation")
@Configuration
@ComponentScan(basePackages = {"com.sorcererpaws.SpringJConfig"}, 
excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = {"com.sorcererpaws.SpringJConfig.web.*"}))
@PropertySource(value = { "classpath:application.properties" })
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching
public class AppConfig {
	
	@Autowired
	private Environment environment;

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer()
	{
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public CacheManager cacheManager()
	{
		return new ConcurrentMapCacheManager();
	}
	
	@Bean
	public JavaMailSenderImpl javaMailSenderImpl() {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		mailSenderImpl.setHost(environment.getProperty("mail.smtp.host"));
		mailSenderImpl.setPort(environment.getProperty("mail.smtp.port", Integer.class));
		mailSenderImpl.setUsername(environment.getProperty("mailGunUname"));
		mailSenderImpl.setPassword(environment.getProperty("mailGunPass"));

		Properties javaMailProps = new Properties();
		javaMailProps.put("mail.smtp.auth", environment.getProperty("mail.smtp.auth"));
		javaMailProps.put("mail.smtp.starttls.enable", environment.getProperty("mail.smtp.starttls.enable"));
		javaMailProps.put("mail.debug", environment.getProperty("mail.debug"));	

		mailSenderImpl.setJavaMailProperties(javaMailProps);

		return mailSenderImpl;
	}
	
	@Bean(name = "velocityEngine")
	public VelocityEngineFactoryBean velocityEngineFactoryBean() {
		VelocityEngineFactoryBean velocityEngineFactoryBean = new VelocityEngineFactoryBean();
		velocityEngineFactoryBean.setResourceLoaderPath("classpath:META-INF/velocityTemplates/");
		return velocityEngineFactoryBean;
	}
}
