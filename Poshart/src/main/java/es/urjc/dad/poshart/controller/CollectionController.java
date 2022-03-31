package es.urjc.dad.poshart.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.CollectionRepository;
import es.urjc.dad.poshart.repository.UserRepository;

@Controller
@RequestMapping("/collection")
public class CollectionController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CollectionRepository collectionRepository;

	@GetMapping("")
	public String createCollection(Model model) {
		return "collectionNew";
	}
	
	@PostMapping("/new")
	public RedirectView createCollection(HttpServletRequest request, Model model, Collection collection) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		collection.addOwner(u);
		collectionRepository.save(collection);
		return new RedirectView("/user/"+u.getId()+"/?colId=" + collection.getId());
	}
	
	@GetMapping("/{id}/edit")
	public String editUser(Model model, @PathVariable long id) {
		Collection c = collectionRepository.findById(id).orElseThrow();
		model.addAttribute("collection", c);
		return "collectionEdit";
	}
	
	@PostMapping("/{id}/edit/confirm")
	public RedirectView editCollection(HttpServletRequest request, Model model, @PathVariable long id, Collection collection) {
		Collection c = collectionRepository.findById(id).orElseThrow();
		c.setName(collection.getName());
		c.setDescription(collection.getDescription());
		collectionRepository.save(c);
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		return new RedirectView("/user/"+ u.getId() +"/?colId=" + c.getId());
	}
	
	@GetMapping("/{id}/delete")
	public RedirectView deleteCollection(HttpServletRequest request, Model model, @PathVariable long id) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		Collection col = collectionRepository.findById(id).orElseThrow();
		if(u.getCollections().contains(col)) {
			col.removeOwner();
			collectionRepository.delete(col);
		}
		return new RedirectView("/user/"+u.getId());
	}
}
