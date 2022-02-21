package es.urjc.dad.poshart.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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
public class GeneralController {

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
	private SessionData sessionData;

	@PostConstruct
	public void init() {
		User u1 = new User("a", "a", "a", "a", "a", "a");
		ShoppingCart s = new ShoppingCart(100);
		for (int i = 30; i < 50; i++) {
			ArtPost art = new ArtPost("Post " + i, i * 10);
			s.addArt(art);
			u1.addPost(art);
		}
		u1.addCart(s);
		userRepository.save(u1);
		for (int i = 0; i < 20; i++) {
			User u = new User("Correo " + i, "Usuario " + i, "Contraseña " + i, "Nombre " + i, "Apellidos " + i,
					"Descripción " + i);
			Collection c = new Collection("Colección " + i, "Descripcion " + i);
			Collection c2 = new Collection("Colección2-" + i, "Descripcion2-" + i);
			ArtPost art = new ArtPost("Post " + i, i * 10);
			ShoppingCart sc = new ShoppingCart(0, Date.from(Instant.now()));
			u.addPost(art);
			u.addCollection(c);
			u.addCollection(c2);
			u.addCart(sc);
			userRepository.save(u);
		}
	}

	@GetMapping("/shopping")
	public RedirectView getShopping(Model model) {
		if (sessionData.checkUser()) {
			return new RedirectView("/shopping/" + sessionData.getUser());
		} else {
			return new RedirectView("/user");
		}
	}

	@GetMapping("/shopping/{id}")
	public String getShopping(Model model, @PathVariable long id) {
		ShoppingCart c = shoppingCartRepository.findById(id).orElseThrow();
		model.addAttribute("cart", c);
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

	@GetMapping("/")
	public RedirectView getHome(Model model) {
		return new RedirectView("home/?page=0&size=5");
	}

	@GetMapping("/home")
	public String getHome(Model model, Pageable page) {
		Page<ArtPost> p;
		if (sessionData.checkUser()) {
			p = artRepository.findByUserFollows(sessionData.getUser(), page);
		} else {
			p = artRepository.findAll(page);
		}
		model.addAttribute("page", p);
		List<Integer> pageNumbers = new ArrayList<>();
		for (int i = 0; i < p.getTotalPages(); i++) {
			pageNumbers.add(i);
		}
		model.addAttribute("totalPages", pageNumbers);
		model.addAttribute("hasPrev", p.hasPrevious());
		model.addAttribute("hasNext", p.hasNext());
		model.addAttribute("nextPage", p.getNumber() + 1);
		model.addAttribute("prevPage", p.getNumber() - 1);
		return "home";
	}

	@GetMapping("/checkUser")
	public RedirectView checkUser(Model model) {
		long userId = sessionData.getUser();
		if (userId <= 0) {
			return new RedirectView("/user/");
		} else {
			return new RedirectView("/user/" + userId);
		}
	}

	@GetMapping("/editUser")
	public RedirectView editUser(Model model) {
		long userId = sessionData.getUser();
		if (userId <= 0) {
			return new RedirectView("/user/create");
		} else {
			return new RedirectView("/user/" + userId + "/edit");
		}
	}

	@GetMapping("/search/{id}")
	public String getSearch(Model model, @PathVariable int id, Pageable page) {
		if (id == 0) {
			model.addAttribute("post", true);
			Page<ArtPost> p = artRepository.findAll(page);
			model.addAttribute("page", p);
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = 0; i < p.getTotalPages(); i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("totalPages", pageNumbers);
			model.addAttribute("hasPrev", p.hasPrevious());
			model.addAttribute("hasNext", p.hasNext());
			model.addAttribute("nextPage", p.getNumber() + 1);
			model.addAttribute("prevPage", p.getNumber() - 1);
		} else if (id == 1) {
			model.addAttribute("collections", true);
			Page<Collection> p = collectionRepository.findAll(page);
			model.addAttribute("page", p);
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = 0; i < p.getTotalPages(); i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("totalPages", pageNumbers);
			model.addAttribute("hasPrev", p.hasPrevious());
			model.addAttribute("hasNext", p.hasNext());
			model.addAttribute("nextPage", p.getNumber() + 1);
			model.addAttribute("prevPage", p.getNumber() - 1);
		} else if (id == 2) {
			model.addAttribute("users", true);
			Page<User> p = userRepository.findAll(page);
			model.addAttribute("page", p);
			List<Integer> pageNumbers = new ArrayList<>();
			for (int i = 0; i < p.getTotalPages(); i++) {
				pageNumbers.add(i);
			}
			model.addAttribute("totalPages", pageNumbers);
			model.addAttribute("hasPrev", p.hasPrevious());
			model.addAttribute("hasNext", p.hasNext());
			model.addAttribute("nextPage", p.getNumber() + 1);
			model.addAttribute("prevPage", p.getNumber() - 1);
		}
		return "search";
	}

	@GetMapping("/users")
	public String getUser(Model model, Pageable page) {
		Page<User> p = userRepository.findAll(page);
		model.addAttribute("page", p);
		List<Integer> pageNumbers = new ArrayList<>();
		for (int i = 0; i < p.getTotalPages(); i++) {
			pageNumbers.add(i);
		}
		model.addAttribute("totalPages", pageNumbers);
		model.addAttribute("hasPrev", p.hasPrevious());
		model.addAttribute("hasNext", p.hasNext());
		model.addAttribute("nextPage", p.getNumber() + 1);
		model.addAttribute("prevPage", p.getNumber() - 1);
		return "allusers";
	}
}
