package es.urjc.dad.poshart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import es.urjc.dad.poshart.repository.UserRepositoryAuthenticationProvider;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	 public UserRepositoryAuthenticationProvider authenticationProvider;

	@Override
	 protected void configure(HttpSecurity http) throws Exception {

	 // Public pages
	 http.authorizeRequests().antMatchers("/").hasAnyRole("USER");
	 http.authorizeRequests().antMatchers("/user").permitAll();
	 http.authorizeRequests().antMatchers("/user/logIn").permitAll();
	 http.authorizeRequests().antMatchers("/user?hasFailed=true").permitAll();
	 http.authorizeRequests().antMatchers("/user/create").permitAll();
	 http.authorizeRequests().antMatchers("/user/signOut").permitAll();
	 // Private pages (all other pages)
	 http.authorizeRequests().anyRequest().authenticated();
	 // Login form
	 http.formLogin().loginPage("/user");
	 http.formLogin().usernameParameter("username");
	 http.formLogin().passwordParameter("password");
	 http.formLogin().defaultSuccessUrl("/user/{id}");
	 http.formLogin().failureUrl("/user?hasFailed=true");
	 // Logout
	 http.logout().logoutUrl("/user/signOut");
	 http.logout().logoutSuccessUrl("/");

	 // Disable CSRF at the moment
	 http.csrf().disable();
	 }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER");

		auth.inMemoryAuthentication().withUser("admin").password("adminpass").roles("USER", "ADMIN");
		
	}

	
}
