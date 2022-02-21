package es.urjc.dad.poshart.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.ArtPostRepository;
import es.urjc.dad.poshart.repository.CollectionRepository;
import es.urjc.dad.poshart.repository.ShoppingCartRepository;
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
	private CollectionRepository collectionRepository;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private SessionData sessionData;
	
	@PostConstruct
	public void init() {
		for(int i = 0; i<20; i++){
			User u = new User("Correo "+i, "Usuario "+i, "Contraseña "+i, "Nombre "+i, "Apellidos "+i, "Descripción "+i);
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
	@GetMapping("/shopping")
	public String getShopping(Model model, Pageable page) {
		model.addAttribute("productos",true);
		Page<ShoppingCart> p = shoppingCartRepository.findAll(page);
		model.addAttribute("page", p);
		List<Integer> pageNumbers = new ArrayList<>();
		for(int i = 0; i < p.getTotalPages(); i++) {
			pageNumbers.add(i);
		}
		model.addAttribute("totalPages", pageNumbers);
		model.addAttribute("hasPrev", p.hasPrevious());
		model.addAttribute("hasNext", p.hasNext());
		model.addAttribute("nextPage", p.getNumber()+1);
		model.addAttribute("prevPage", p.getNumber()-1);
		return "shoppingCart";
	}
	@GetMapping("/newPost")
	public String getPost(Model model) {
		return "NewPost";
	}
	@GetMapping("/viewComment")
	public String getComment(Model model) {
		return "ViewCommentBuyPost";
	}
	@GetMapping("/home")
	public String getHome(Model model, Pageable page) {
		model.addAttribute("users",true);
		Page<User> p = userRepository.findAll(page);
		model.addAttribute("page", p);
		List<Integer> pageNumbers = new ArrayList<>();
		for(int i = 0; i < p.getTotalPages(); i++) {
			pageNumbers.add(i);
		}
		model.addAttribute("totalPages", pageNumbers);
		model.addAttribute("hasPrev", p.hasPrevious());
		model.addAttribute("hasNext", p.hasNext());
		model.addAttribute("nextPage", p.getNumber()+1);
		model.addAttribute("prevPage", p.getNumber()-1);
		return "home";
	}
	@GetMapping("/checkUser")
	public RedirectView checkUser(Model model) {
		long userId = sessionData.getUser();
		if(userId<=0) {
			return new RedirectView("/user/");
		}else {
			return new RedirectView("/user/"+userId);
		}
	}
	@GetMapping("/editUser")
	public RedirectView editUser(Model model) {
		long userId = sessionData.getUser();
		if(userId<=0) {
			return new RedirectView("/user/create");
		}else {
			return new RedirectView("/user/"+userId+"/edit");
		}
	}
	@GetMapping("/search/{id}")
	public String getSearch(Model model, @PathVariable long id, Pageable page) {
		
		if(id==0) {
			model.addAttribute("post",true);
			Page<ArtPost> p = artRepository.findAll(page);
			model.addAttribute("page", p);
			List<Integer> pageNumbers = new ArrayList<>();
			for(int i = 0; i < p.getTotalPages(); i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("totalPages", pageNumbers);
			model.addAttribute("hasPrev", p.hasPrevious());
			model.addAttribute("hasNext", p.hasNext());
			model.addAttribute("nextPage", p.getNumber()+1);
			model.addAttribute("prevPage", p.getNumber()-1);
		}else if(id==1){
			model.addAttribute("collections",true);
			Page<Collection> p = collectionRepository.findAll(page);
			model.addAttribute("page", p);
			List<Integer> pageNumbers = new ArrayList<>();
			for(int i = 0; i < p.getTotalPages(); i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("totalPages", pageNumbers);
			model.addAttribute("hasPrev", p.hasPrevious());
			model.addAttribute("hasNext", p.hasNext());
			model.addAttribute("nextPage", p.getNumber()+1);
			model.addAttribute("prevPage", p.getNumber()-1);
		}else if(id==2){
			model.addAttribute("users",true);
			Page<User> p = userRepository.findAll(page);
			model.addAttribute("page", p);
			List<Integer> pageNumbers = new ArrayList<>();
			for(int i = 0; i < p.getTotalPages(); i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("totalPages", pageNumbers);
			model.addAttribute("hasPrev", p.hasPrevious());
			model.addAttribute("hasNext", p.hasNext());
			model.addAttribute("nextPage", p.getNumber()+1);
			model.addAttribute("prevPage", p.getNumber()-1);
		}
		return "search";
		
	}
	@GetMapping("/users")
	public String getUser(Model model,Pageable page) {
		Page<User> p = userRepository.findAll(page);
		model.addAttribute("page", p);
		List<Integer> pageNumbers = new ArrayList<>();
		for(int i = 0; i < p.getTotalPages(); i++) {
			pageNumbers.add(i);
		}
		model.addAttribute("totalPages", pageNumbers);
		model.addAttribute("hasPrev", p.hasPrevious());
		model.addAttribute("hasNext", p.hasNext());
		model.addAttribute("nextPage", p.getNumber()+1);
		model.addAttribute("prevPage", p.getNumber()-1);
		return "allusers";
	}
}
