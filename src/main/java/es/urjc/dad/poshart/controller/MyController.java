package es.urjc.dad.poshart.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.model.Image;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.ArtPostRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.service.ImageService;
import es.urjc.dad.poshart.service.SessionData;

@Controller
public class MyController {
	
	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;


	@Autowired
	private ArtPostRepository artRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private SessionData sessionData;
	
	@PostConstruct
	public void init() {
		for(int i = 0; i<20; i++){
			User u = new User("Correo "+i, "Usuario "+i, "Contraseña "+i);
			Collection c = new Collection("Colección "+i, "Descripcion " + i);
			Collection c2 = new Collection("Colección2-"+i, "Descripcion2-" + i);
			ArtPost art = new ArtPost("Post "+i, i*10);
			u.addPost(art);
			u.addCollection(c);
			u.addCollection(c2);
			userRepository.save(u);
		}
	}
		
	@GetMapping("/")
	public String startPage(Model model, HttpSession sesion) {
		if(sesion.isNew()) {
			log.warn("Usuario sin cuenta!");
			long i = 0;
			Optional<User> user;
			do {
				i++;
				user = userRepository.findById(i);
			}while(!user.isPresent());
			sessionData.setUser(user.get().getId());
			model.addAttribute("userid", user.get().getId());
		}else {
			log.warn("Usuario con cuenta!");
			model.addAttribute("userid", sessionData.getUser());
		}
		return "start";
	}
	@GetMapping("/muro")
	public String getMuro(Model model) {
		return "muro";
	}
	@GetMapping("/shopping")
	public String getShopping(Model model) {
		return "shoppingCart";
	}
	@GetMapping("/home")
	public String getHome(Model model) {
		return "home";
	}
	@GetMapping("/search")
	public String getSearch(Model model) {
		return "search";
	}
	@GetMapping("/config")
	public String getConfig(Model model) {
		return "config";
	}
	@GetMapping("/user/{id}")
	public String getUser(Model model, @PathVariable long id) {
		User u = userRepository.findById(id).orElseThrow();
		model.addAttribute("user", u);
		return "users";
	}
	@GetMapping("/users")
	public String getUser(Model model,Pageable page) {
		Page<User> p = userRepository.findAll(page);
		model.addAttribute("page", p);
		model.addAttribute("hasPrev", p.hasPrevious());
		model.addAttribute("hasNext", p.hasNext());
		model.addAttribute("nextPage", p.getNumber()+1);
		model.addAttribute("prevPage", p.getNumber()-1);
		return "allusers";
	}
}
