package com.apress.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalAuthentication
// @org.springframework.context.annotation.Profile("memory")
public class ResourceSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(ResourceSecurityConfiguration.class);
	private static int n = 0;

	// @Autowired
	// UsernamePasswordAuthenticationFilter
	// myUsernamePasswordAuthenticationFilter;

	// @Bean
	public UsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
		UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
		filter.setUsernameParameter("user_name");
		filter.setPasswordParameter("user_password");
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		log.info("[" + (n++) + "] ResourceSecurityConfiguration.configure(AuthenticationManagerBuilder)");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// super.configure(http);
		http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/api/**").authenticated().and().httpBasic()
				.and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();
		// http.addFilterBefore(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		log.info("[" + (n++) + "] ResourceSecurityConfiguration.configure(HttpSecurity)");
	}

	@Override
	public void init(WebSecurity web) throws Exception {
		super.init(web);
		log.info("[" + (n++) + "] ResourceSecurityConfiguration.init(WebSecurity)");
	}

}
