package es.urjc.dad.poshart.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

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

@Controller
public class MyController {
	
	@Autowired
	private UserRepository userRepository;


	@Autowired
	private ArtPostRepository artRepository;
	
	@Autowired
	private ImageService imageService;
	
	@PostConstruct
	public void init() {
		User u = new User();
		for(int i = 0; i<65; i++){
			u = new User("Correo "+i, "Usuario "+i, "Contraseña "+i);
			userRepository.save(u);
		}		
		// Añadimos muchos anuncios
		/*for(int i = 0; i<20; i++){
			artRepository.save(new ArtPost("Post "+i, i*10));
			User u = new User("Correo "+i, "Usuario "+i, "Contraseña "+i);
			Collection c = new Collection("Colección "+i, "Descripcion " + i);
			u.getCollections().add(c);
			userRepository.save(u);
		}*/
	}
		
	@GetMapping("/")
	public String startPage(Model model) {
		return "MiPlantilla";
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
	
	@PostMapping("/newImage")
	public String newImage(Model model, @RequestParam MultipartFile image) throws IOException {
		Image imagen = imageService.createImage(image);
		return "confirm";
	}
	
	@GetMapping("/image/{id}")
	public ResponseEntity<Object> newImage(Model model, @PathVariable long id) throws SQLException {
		return imageService.createResponseFromImage(id);
	}
	
	@GetMapping("/delete/{id}")
	public String deleteImage(Model model, @PathVariable long id) {
		imageService.deleteImage(id);
		return "confirm";
	}
}
