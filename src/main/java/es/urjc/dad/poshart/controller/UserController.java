package es.urjc.dad.poshart.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.model.Image;
import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.CollectionRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.service.ImageService;
import es.urjc.dad.poshart.service.SessionData;

@Controller
@RequestMapping("/user")
public class UserController {
	
	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CollectionRepository collectionRepository;

	@Autowired
	private SessionData sessionData;

	@GetMapping("")
	public String logIn(Model model, @RequestParam(defaultValue = "false") boolean hasFailed) {
		model.addAttribute("hasFailed", hasFailed);
		return "logIn";
	}
	
	@PostMapping("/logIn")
	public RedirectView tryLogIn(Model model, @RequestParam String name, @RequestParam String password) {
		//Se comprueba el nombre de usuario.
		User u = userRepository.findFirstByUsername(name);
		if(u==null) {
			//Si no, se comprueba el emai.
			u = userRepository.findFirstByMail(name);
			if(u==null) return new RedirectView("/user?hasFailed=true");
		}
		//Se comprueba la contraseña.
		if(u.getPassword().equals(password)) {
			sessionData.setUser(u.getId());
			return new RedirectView("/");
		}else {
			return new RedirectView("/user?hasFailed=true");
		}
	}
	
	@GetMapping("/create")
	public String SingIn(Model model, @RequestParam(defaultValue = "false") boolean hasFailed) {
		model.addAttribute("hasFailed", hasFailed);
		return "singIn";
	}
	
	@PostMapping("/signIn")
	public RedirectView trySingIn(Model model, User newUser, @RequestParam(required = false) MultipartFile imagen) throws IOException {
		//Comprobamos que el usuario o correo no están repetidos.
		User u = userRepository.findFirstByUsername(newUser.getUsername());
		if(u!=null) {
			return new RedirectView("/user/create/?hasFailed=true");
		}
		u = userRepository.findFirstByMail(newUser.getMail());
		if(u!=null) {
			return new RedirectView("/user/create/?hasFailed=true");
		}
		if(!imagen.isEmpty()) {
			Image newImage = imageService.createImage(imagen);
			newUser.setImage(newImage);
		}
		ShoppingCart sc = new ShoppingCart(0, Date.from(Instant.now()));
		newUser.addCart(sc);
		userRepository.save(newUser);
		sessionData.setUser(newUser.getId());
		return new RedirectView("/");
	}
	
	@PostMapping("/signOut")
	public RedirectView singOut(Model model, User newUser, @RequestParam(required = false) MultipartFile imagen) throws IOException {
		sessionData.setUser(0);
		return new RedirectView("/");
	}
	
	@GetMapping("/{id}/edit")
	public String editUser(Model model, @PathVariable long id) {
		User u = userRepository.findById(id).orElseThrow();
		model.addAttribute("user", u);
		return "editUser";
	}

	@PostMapping("/{id}/edit/confirm")
	public RedirectView editUserConfirm(Model model, @PathVariable long id, User newUser, @RequestParam MultipartFile imagen) throws IOException {
		User u = userRepository.findById(id).orElseThrow();
		if(!imagen.isEmpty()) {
			if(u.getImage()!=null) {
				imageService.deleteImage(u.getImage().getId());
			}
			Image newImage = imageService.createImage(imagen);
			newUser.setImage(newImage);
		}
		if(u.getImage()!=null) {
			newUser.setImage(u.getImage());
		}
		newUser.setId(u.getId());
		userRepository.save(newUser);
		return new RedirectView("/user/" + id + "/edit");
	}
	
	@GetMapping("/{id}")
	public String getMuro(Model model, @PathVariable long id, @RequestParam(defaultValue = "-1") long colId) {
		log.warn(""+id);
		User u = userRepository.findById(id).orElseThrow();
		model.addAttribute("follows", u.getFollows().size());
		model.addAttribute("followers", u.getFollowers().size());
		model.addAttribute("user", u);
		if(colId!=-1) {
			Collection c = collectionRepository.findById(colId).orElseThrow();
			model.addAttribute("selectedCol", c);
		}
		boolean isMine = id==sessionData.getUser();
		model.addAttribute("isMine", isMine);
		if(!isMine &&  sessionData.checkUser()) {
			User other = userRepository.findById(sessionData.getUser()).orElseThrow();
			boolean followed = u.getFollowers().contains(other);
			model.addAttribute("followed", followed);
		}else {
			model.addAttribute("followed", false);
		}
		return "muro";
	}
	
	@GetMapping("/{id}/delete")
	public RedirectView deleteUser(Model model, @PathVariable long id) {
		User u = userRepository.findById(id).orElseThrow();
		if(u.getImage()!=null) imageService.deleteImage(u.getImage().getId());
		userRepository.delete(u);
		sessionData.setUser(0);
		return new RedirectView("/");
	}
	
	@GetMapping("/{id}/follow")
	public RedirectView followUser(Model model, @PathVariable long id) {
		User u = userRepository.findById(id).orElseThrow();
		if(!sessionData.checkUser()) {
			return new RedirectView("/user");
		}else {
			User myUser = userRepository.findById(sessionData.getUser()).orElseThrow();
			myUser.addFollow(u);
			u.addFollower(myUser);
			userRepository.save(myUser);
			return new RedirectView("/user/"+id);
		}
	}
	
	@GetMapping("/{id}/unfollow")
	public RedirectView unFollowUser(Model model, @PathVariable long id) {
		User u = userRepository.findById(id).orElseThrow();
		if(!sessionData.checkUser()) {
			return new RedirectView("/user");
		}else {
			User myUser = userRepository.findById(sessionData.getUser()).orElseThrow();
			myUser.removeFollow(u);
			u.removeFollower(myUser);
			userRepository.save(myUser);
			return new RedirectView("/user/"+id);
		}
	}
}
