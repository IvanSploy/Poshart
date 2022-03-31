package es.urjc.dad.poshart.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.UserRepository;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
	
	Logger log = LoggerFactory.getLogger(UserAuthenticationProvider.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
 
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
		 if (!passwordEncoder.matches((String) auth.getCredentials(), user.getPassword())) {
			 throw new BadCredentialsException("Wrong password");
		 }
		 List<GrantedAuthority> roles = new ArrayList<>();
		 for (String role : user.getRoles()) {
			 roles.add(new SimpleGrantedAuthority(role));
		 }
		 return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), roles);
	 }
	
	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}
}

