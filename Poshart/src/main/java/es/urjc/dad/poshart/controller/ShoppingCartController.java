package es.urjc.dad.poshart.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import es.urjc.dad.poshart.internalService.DownloadService;
import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.ShoppingCartRepository;
import es.urjc.dad.poshart.repository.UserRepository;


import org.springframework.core.io.ByteArrayResource;
@Controller
@RequestMapping("/shopping")
public class ShoppingCartController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	DownloadService downloadService;

	
	@GetMapping("")
	public RedirectView getShopping(HttpServletRequest request, Model model) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		return new RedirectView("/shopping/" + u.getId());
	}
	@GetMapping("/{id}")
	public String getShopping(HttpServletRequest request, Model model, @PathVariable long id) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		if(u.getId()!=id) id = u.getId();
		model.addAttribute("cart", u.getCart());
		return "shoppingCart";
	}
	
	@GetMapping("/{id}/clear")
	public String getShoppingClear(HttpServletRequest request, Model model, @PathVariable long id) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		if(u.getId()!=id) {
			return "redirect:/shopping";
		}
		ShoppingCart c = u.getCart();
		c.clear();
		shoppingCartRepository.save(c);
		model.addAttribute("cart", c);
		return "shoppingCart";
	}
	
	/*@GetMapping("/{id}/buy")
	public String getShoppingBuy(HttpServletRequest request, Model model, @PathVariable long id) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		if(u.getId()!=id) {
			return "redirect:/shopping";
		}
		ShoppingCart c = u.getCart();
		c.buy();
		userRepository.save(c.getBuyer());
		shoppingCartRepository.save(c);
		model.addAttribute("cart", c);
		return "shoppingCart";
	}*/
	
	@GetMapping(value="/{id}/buy")
	public String getShoppingBuy(HttpServletRequest request, @PathVariable long id) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		ShoppingCart c = u.getCart();
		downloadService.downloadPdf(c);
		return "shoppingCart";
	}
	
	@GetMapping("/{id}/remove/{id2}")
	public String getShoppingRemove(HttpServletRequest request, Model model, @PathVariable long id, @PathVariable int id2) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		if(u.getId()!=id) {
			return "redirect:/shopping";
		}
		ShoppingCart c = u.getCart();
		c.removeArt(id2-1);
		shoppingCartRepository.save(c);
		model.addAttribute("cart", c);
		return "shoppingCart";
	}
}
