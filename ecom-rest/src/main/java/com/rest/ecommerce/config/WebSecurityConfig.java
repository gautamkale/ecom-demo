package com.rest.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.rest.ecommerce.security.AuthenticationFailureHandler;
import com.rest.ecommerce.security.AuthenticationSuccessHandler;
import com.rest.ecommerce.security.TokenAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public TokenAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
		return new TokenAuthenticationFilter();
	}

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.addFilterBefore(jwtAuthenticationTokenFilter(), BasicAuthenticationFilter.class).authorizeRequests()
				.antMatchers("/product/image/**").permitAll().antMatchers(HttpMethod.GET, "/product/**").permitAll()
				.antMatchers(HttpMethod.GET, "/group/**").permitAll().antMatchers("/cart/**").permitAll()
				.anyRequest().authenticated().anyRequest().hasAuthority("admin")

				.and().formLogin().successHandler(authenticationSuccessHandler)
				.failureHandler(authenticationFailureHandler)

				.and().csrf().disable();
	}
	
	 @Override
	  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.inMemoryAuthentication().withUser("user").password("{noop}password").roles("USER");
	  }
}
