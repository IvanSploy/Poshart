package es.urjc.dad.poshart.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import es.urjc.dad.poshart.internal_service.EmailService;
import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.model.Image;
import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.CollectionRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.service.ImageService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CollectionRepository collectionRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("")
	public String logIn(Model model, @RequestParam(defaultValue = "false") boolean hasFailed) {
		model.addAttribute("hasFailed", hasFailed);
		return "logIn";
	}
	
	@GetMapping("/create")
	public String SingIn(Model model, @RequestParam(defaultValue = "false") boolean hasFailed) {
		model.addAttribute("hasFailed", hasFailed);
		return "singIn";
	}
	
	@PostMapping("/signIn")
	public RedirectView trySingIn(HttpServletRequest request, Model model, User newUser, @RequestParam(required = false) MultipartFile imagen) throws IOException {
		//Comprobamos que el usuario o correo no están repetidos.
		User u = userRepository.findByUsername(newUser.getUsername());
		if(u!=null) {
			return new RedirectView("/user/create/?hasFailed=true");
		}
		u = userRepository.findByMail(newUser.getMail());
		if(u!=null) {
			return new RedirectView("/user/create/?hasFailed=true");
		}
		if(!imagen.isEmpty()) {
			Image newImage = imageService.createImage(imagen);
			newUser.setImage(newImage);
		}
		String realPassword = newUser.getPassword();
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		ShoppingCart sc = new ShoppingCart(0, Date.from(Instant.now()));
		newUser.addCart(sc);
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_USER");
		newUser.setRoles(roles);
		userRepository.save(newUser);
		
		//Se envía correo de confirmación empleando el servicio interno. 
		emailService.sendConfimationEmail(newUser.getId());
		
		//Tras crear el usuario se inicia la sesión automáticamente.
		try {
			request.login(newUser.getUsername(), realPassword);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			return new RedirectView("/user/create/?hasFailed=true");
		}
		return new RedirectView("/");
	}
	
	@GetMapping("/{id}/edit")
	public String editUser(HttpServletRequest request, Model model, @PathVariable long id) {
		if(request.isUserInRole("USER")) {
			User u;
			if(!request.isUserInRole("ADMIN")) {
				u = userRepository.findByUsername(request.getUserPrincipal().getName());
				if(id!=u.getId()) id=u.getId();
			}else {
				u = userRepository.findById(id).orElseThrow();
			}
			model.addAttribute("webuser", u);
			return "editUser";
		}
		return "redirect:/";
	}

	@PostMapping("/{id}/edit/confirm")
	public String editUserConfirm(HttpServletRequest request, Model model, @PathVariable long id, User newUser, @RequestParam MultipartFile imagen) throws IOException {
		if(request.isUserInRole("USER")) {
			User u;
			if(!request.isUserInRole("ADMIN")) {
				u = userRepository.findByUsername(request.getUserPrincipal().getName());
				if(id!=u.getId()) return "redirect:/user/" + u.getId() + "/edit";
			}else {
				u = userRepository.findById(id).orElseThrow();
			}
			long oldImageId = -1;
			if(!imagen.isEmpty()) {
				if(u.getImage()!=null) oldImageId = u.getImage().getId();
				Image newImage = imageService.createImage(imagen);
				u.setImage(newImage);
			}
			
			//Comprobamos que el usuario o correo no están repetidos.
			User checkUser = userRepository.findByUsername(newUser.getUsername());
			
			String newUsername = "";
			if(checkUser==null) {
				u.setUsername(newUser.getUsername());
				newUsername = u.getUsername();
			}
			checkUser = userRepository.findByMail(newUser.getMail());
			if(checkUser==null) {
				u.setMail(newUser.getMail());
			}
			
			//Se gestiona la contraseña si se requiere reemplazar o no.
			String newPassword = "";
			if(!newUser.getPassword().equals("")) {
				u.setPassword(passwordEncoder.encode(newUser.getPassword()));
				newPassword = u.getPassword();
			}

			//Cambiamos los datos.
			u.setName(newUser.getName());
			u.setSurname(newUser.getSurname());
			u.setDescription(newUser.getDescription());
			
			//Actualizamos el usuario
			userRepository.save(u);
			
			//Actualizamos la sesión con el nuevo usuario.
			if(!newUsername.equals("") || !newPassword.equals("")) {
				List<GrantedAuthority> roles = new ArrayList<>();
				 for (String role : u.getRoles()) {
					 roles.add(new SimpleGrantedAuthority(role));
				 }
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(newUsername, newPassword, roles);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
			//En caso de que se haya cambiado la imagen, se elimina la foto anterior.
			if(oldImageId!=-1)
				imageService.deleteImage(oldImageId);
			
			return "redirect:/user/" + id + "/edit";
		}
		return "redirect:/";
	}
	
	@GetMapping("/{id}")
	public String getMuro(HttpServletRequest request, Model model, @PathVariable long id, @RequestParam(defaultValue = "-1") long colId) {
		User u = userRepository.findById(id).orElseThrow();
		model.addAttribute("webuser", u);
		if(colId!=-1) {
			Optional<Collection> c = collectionRepository.findById(colId);
			if(c.isPresent())
				if(u.getCollections().contains(c.get())) 
					model.addAttribute("selectedCol", c.get());
		}
		boolean isMine = false;
		boolean followed = false;
		if(request.isUserInRole("USER")) {
			User uMine = userRepository.findByUsername(request.getUserPrincipal().getName());
			isMine = id==uMine.getId();
			if(!isMine) followed = u.getFollowers().contains(uMine);
		}
		model.addAttribute("isMine", isMine);
		model.addAttribute("followed", followed);
		return "muro";
	}
	
	@GetMapping("/{id}/delete")
	public String deleteUser(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable long id) {
		if(request.isUserInRole("USER")) {
			User u = userRepository.findByUsername(request.getUserPrincipal().getName());
			if(id!=u.getId()) return "redirect:/user/" + u.getId() + "/edit";
			
			//Cerramos la sesión.
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    if (auth != null){
		        new SecurityContextLogoutHandler().logout(request, response, auth);
		    }
		    
		    //Se elimina el usuario.
			if(u.getImage()!=null) imageService.deleteImage(u.getImage().getId());
			//Desvinculamos todos los seguidores.
			for(User us : u.getFollowers()) {
				us.removeFollow(u);
				userRepository.save(us);
			}
			//Desvinculamos todos los seguidos.
			for(User us : u.getFollows()) {
				us.removeFollower(u);
				userRepository.save(us);
			}
			userRepository.delete(u);
			return "redirect:/";
		}
		return "redirect:/user/" + id;
	}
	
	@GetMapping("/{id}/follow")
	public String followUser(HttpServletRequest request, Model model, @PathVariable long id) {
		if(request.isUserInRole("USER")) {
			User u = userRepository.findById(id).orElseThrow();
			User myUser = userRepository.findByUsername(request.getUserPrincipal().getName());
			if(u.getId()!=myUser.getId()) {
				myUser.addFollow(u);
				u.addFollower(myUser);
				userRepository.save(myUser);
			}
		}
		return "redirect:/user/" + id;
	}
	
	@GetMapping("/{id}/unfollow")
	public String unFollowUser(HttpServletRequest request, Model model, @PathVariable long id) {
		if(request.isUserInRole("USER")) {
			User u = userRepository.findById(id).orElseThrow();
			User myUser = userRepository.findByUsername(request.getUserPrincipal().getName());
			if(u.getId()!=myUser.getId()) {
				myUser.removeFollow(u);
				u.removeFollower(myUser);
				userRepository.save(myUser);
			}
		}
		return "redirect:/user/"+id;
	}
}
