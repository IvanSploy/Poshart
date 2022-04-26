package es.urjc.dad.poshart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import es.urjc.dad.poshart.internal_service.EmailService;
import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.ShoppingCartRepository;
import es.urjc.dad.poshart.repository.UserRepository;

@Controller
@RequestMapping("/shopping")
public class ShoppingCartController {
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	
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
	
	@GetMapping("/{id}/buy")
	public String getShoppingBuy(HttpServletRequest request, Model model, @PathVariable long id) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		
		if(u.getId()!=id) {
			return "redirect:/shopping";
		}
		
		ShoppingCart c = u.getCart();

		//Se envia el correo con el recibo.
		emailService.sendPurchaseReceipt(c.getId());
		
		List<String> emails = new ArrayList<>();
		List<Long> idPosts = new ArrayList<>();
		for(int i=0; i<c.getArt().size(); i++) {
			emails.add(c.getArt().get(i).getOwner().getMail());
			idPosts.add(c.getArt().get(i).getId());
			c.getBuyer().addPost(c.getArt().get(i));
		}
		c.getArt().clear();
		
		userRepository.save(u);
		shoppingCartRepository.save(c);
		
		//Se envía un correo a todas las personas cuyo arte ha sido comprado.
		for(int i = 0; i < emails.size(); i++) {
			emailService.sendNotifyPurchase(emails.get(i), idPosts.get(i));
		}
		
		model.addAttribute("cart", c);
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
