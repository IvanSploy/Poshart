package es.urjc.dad.poshart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.ArtPostRepository;
import es.urjc.dad.poshart.repository.CollectionRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.service.SessionData;

@Controller
@RequestMapping("/collection")
public class CollectionController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ArtPostRepository postRepository;
	
	@Autowired
	private CollectionRepository collectionRepository;
	
	@Autowired
	private SessionData sessionData;

	@GetMapping("")
	public String createCollection(Model model) {
		return "NewCollection";
	}
	
	@PostMapping("/new")
	public RedirectView createCollection(Model model, Collection collection) {
		User u = userRepository.findById(sessionData.getUser()).orElseThrow();
		collection.addOwner(u);
		collectionRepository.save(collection);
		return new RedirectView("/user/"+u.getId()+"/?colId=" + collection.getId());
	}
	
	@GetMapping("/{id}/delete")
	public RedirectView deleteImage(Model model, @PathVariable long id) {
		User u = userRepository.findById(sessionData.getUser()).orElseThrow();
		Collection col = collectionRepository.findById(id).orElseThrow();
		if(u.getCollections().contains(col))
			collectionRepository.delete(col);
		return new RedirectView("/user/"+u.getId());
	}
	
	//Va en PostController.
		@GetMapping("/{id}/add/{colId}")
		public RedirectView addPostToCollection(Model model, @PathVariable long id, @PathVariable long colId) {
			ArtPost ap = postRepository.findById(id).orElseThrow();
			Collection c = collectionRepository.findById(colId).orElseThrow();
			if(sessionData.checkUser()) {
				User myUser = userRepository.findById(sessionData.getUser()).orElseThrow();
				if(myUser.getCollections().contains(c)) {
					c.addPost(ap);
					collectionRepository.save(c);
				}
			}
			return new RedirectView("/"+id);
		}
		
		@GetMapping("/{id}/remove/{colId}")
		public RedirectView removePostFromCollection(Model model, @PathVariable long id, @PathVariable long colId) {
			ArtPost ap = postRepository.findById(id).orElseThrow();
			Collection c = collectionRepository.findById(colId).orElseThrow();
			if(sessionData.checkUser()) {
				User myUser = userRepository.findById(sessionData.getUser()).orElseThrow();
				if(myUser.getCollections().contains(c)) {
					c.removePost(ap);
					collectionRepository.save(c);
				}
			}
			return new RedirectView("/"+id);
		}
}
