package es.urjc.dad.poshart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import es.urjc.dad.poshart.repository.UserRepositoryAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	public UserRepositoryAuthenticationProvider authenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Public pages
		http.authorizeRequests().antMatchers("/").permitAll();
		http.authorizeRequests().antMatchers("/search/**").permitAll();
		http.authorizeRequests().antMatchers("/home/**").permitAll();
		http.authorizeRequests().antMatchers("/user/**").permitAll();
		// Private pages (el resto de páginas)
		http.authorizeRequests().anyRequest().authenticated();
		// Login form
		http.formLogin().loginPage("/user");
		http.formLogin().usernameParameter("username");
		http.formLogin().passwordParameter("password");
		http.formLogin().defaultSuccessUrl("/");
		http.formLogin().failureUrl("/user?hasFailed=true");
		// Logout
		http.logout().logoutUrl("/user/signOut");
		http.logout().logoutSuccessUrl("/user/signOut/confirm");
		http.logout().invalidateHttpSession(true);
		// Desactiva el uso de CSRF por el momento
		//http.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Proveedor de autentificación empleando la base de datos
		auth.authenticationProvider(authenticationProvider);
	}
	
	//Permite acceder a los recursos estáticos.
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/css/**", "/img/**");
	}
}
