package com.apress.spring.config;

import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableGlobalAuthentication
public class JdbcSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	private static final Logger log = LoggerFactory.getLogger(JdbcSecurityConfiguration.class);
	private static int n = 0;

	@Bean
	public UserDetailsService newUserDetailsService(JdbcTemplate jdbcTemplate) {
		RowMapper<User> userRowMapper = (ResultSet rs, int i) -> new User( // <init>
				rs.getString("ACCOUNT_NAME"), // username
				rs.getString("PASSWORD"), // password
				rs.getBoolean("ENABLED"), // enabled
				rs.getBoolean("ENABLED"), // accountNonExpired
				rs.getBoolean("ENABLED"), // credentialsNonExpired
				rs.getBoolean("ENABLED"), // accountNonLocked
				AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN") // authorities
		);
		return username -> jdbcTemplate.queryForObject("SELECT * from ACCOUNT where ACCOUNT_NAME = ?", userRowMapper,
				username);
	}

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		log.info("{" + (n++) + "} JdbcSecurityConfiguration.init(AuthenticationManagerBuilder)");
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		log.info("{" + (n++) + "} JdbcSecurityConfiguration.configure(AuthenticationManagerBuilder)");
	}
}
