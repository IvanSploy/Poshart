package es.urjc.dad.poshart.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.service.SessionData;

@Component
public class UserRepositoryAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private SessionData sessionData;
	
	@Autowired
	private UserRepository userRepository;
 
	@Override
	 public Authentication authenticate(Authentication auth) throws AuthenticationException {
		 //Se permite autentificar al usuario usando nombre de usuario o correo.
		 User user = userRepository.findByUsername(auth.getName());
		 if (user == null) {
			 user = userRepository.findByMail(auth.getName());
			 if(user == null) {
				 throw new BadCredentialsException("Username or mail not found");
			 }
		 }
		 String password = (String) auth.getCredentials();
		 if (!password.equals(user.getPassword())) {
			 throw new BadCredentialsException("Wrong password");
		 }
		 List<GrantedAuthority> roles = new ArrayList<>();
		 for (String role : user.getRoles()) {
			 roles.add(new SimpleGrantedAuthority(role));
		 }
		 sessionData.setUser(user.getId());
		 return new UsernamePasswordAuthenticationToken(user.getUsername(), password, roles);
	 }
	
	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}
}

