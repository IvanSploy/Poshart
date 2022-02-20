package es.urjc.dad.poshart.controller;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.CollectionRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.service.SessionData;

@Controller
@RequestMapping("/collection")
public class CollectionController {
	
	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CollectionRepository repository;

	@Autowired
	private SessionData sessionData;
	
	@PostMapping("/new")
	public String createCollection(Model model, Collection collection) {
		log.warn(""+collection.getId());
		if(sessionData.getUser()!=0) {
			User u = userRepository.findById(sessionData.getUser()).orElseThrow();
			collection.addOwner(u);
			repository.save(collection);
			log.warn(""+collection.getId());
			return "confirm";
		}else {
			return "nouser";
		}
	}
	
	@GetMapping("/{id}")
	public String getCollection(Model model, @PathVariable long id) throws SQLException {
		Collection col = repository.findById(id).orElseThrow();
		model.addAttribute("col", col);
		return "collection";
	}
	
	@GetMapping("/user/{id}")
	public String getCollectionFromUser(Model model, @PathVariable long id) throws SQLException {
		List<Collection> cols = repository.findByOwnerId(id);
		model.addAttribute("cols", cols);
		return "userCollections";
	}
	
	@GetMapping("/{id}/delete")
	public String deleteImage(Model model, @PathVariable long id) {
		Collection col = repository.findById(id).orElseThrow();
		repository.delete(col);
		return "confirm";
	}
}
