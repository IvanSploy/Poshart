package es.urjc.dad.poshart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.repository.ShoppingCartRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.service.SessionData;

@Controller
@RequestMapping("/shopping")
public class ShoppingCartController {
	
	@Autowired
	private SessionData sessionData;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	
	@GetMapping("")
	public RedirectView getShopping(Model model) {
		if (sessionData.checkUser()) {
			return new RedirectView("/shopping/" + sessionData.getUser());
		} else {
			return new RedirectView("/user");
		}
	}
	@GetMapping("/{id}")
	public String getShopping(Model model, @PathVariable long id) {
		ShoppingCart c = shoppingCartRepository.findById(id).orElseThrow();
		model.addAttribute("cart", c);
		return "shoppingCart";
	}
	@GetMapping("/{id}/clear")
	public String getShoppingClear(Model model, @PathVariable long id) {
		ShoppingCart c = shoppingCartRepository.findById(id).orElseThrow();
		c.clear();
		shoppingCartRepository.save(c);
		model.addAttribute("cart", c);
		return "shoppingCart";
	}
	@GetMapping("/{id}/buy")
	public String getShoppingBuy(Model model, @PathVariable long id) {
		ShoppingCart c = shoppingCartRepository.findById(id).orElseThrow();
		c.buy();
		userRepository.save(c.getBuyer());
		shoppingCartRepository.save(c);
		model.addAttribute("cart", c);
		return "shoppingCart";
	}
	@GetMapping("/{id}/remove/{id2}")
	public String getShoppingRemove(Model model, @PathVariable long id, @PathVariable long id2) {
		ShoppingCart c = shoppingCartRepository.findById(id).orElseThrow();
		c.removeArt((int)id2);
		shoppingCartRepository.save(c);
		model.addAttribute("cart", c);
		return "shoppingCart";
	}
}
