package es.urjc.dad.poshart.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.model.Image;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.repository.CommentRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.repository.ArtPostRepository;
import es.urjc.dad.poshart.service.ImageService;
import es.urjc.dad.poshart.service.SessionData;

@Controller
@RequestMapping("post")
public class ArtPostController {
	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ArtPostRepository artPostRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private SessionData sessionData;//Permite saber si soy yo el usuario o no
	
	@GetMapping("/createPost")
	public String newPost(Model model) {
		return "newPost";
	}
	
	@PostMapping("/newPost")
	public RedirectView tryNewPost(Model model, ArtPost newArtPost, @RequestParam(required = false) MultipartFile imagen) throws IOException {
		
		if(!imagen.isEmpty()) {
			Image newImage = imageService.createImage(imagen);
			newArtPost.setImage(newImage);
		}
		newArtPost.addOwner(userRepository.findById(sessionData.getUser()).orElseThrow());
		artPostRepository.save(newArtPost);
		return new RedirectView("/post");
	}
	
	@GetMapping("/post")
	public String viewPost(Model model) {
		return "ViewCommentBuyPost";
	}
	
	@GetMapping("post/addToShopping")
	public String añadeACesta(Model model, ArtPost thisArtPost) {
		User u = userRepository.findById(sessionData.getUser()).orElseThrow();
		//Solo te deja añadir al carro de la compra si no eres su dueño y si no lo tienes ya añadido
		if(u != thisArtPost.getOwner()) {
			if(!u.getCart().getArt().contains(thisArtPost))
				u.getCart().addArt(thisArtPost);
		}
		
		return "ViewCommentBuyPost";
	}
	
	@GetMapping("/post/edit")
	public String editPost(Model model, ArtPost thisArtPost) {
		User u = userRepository.findById(sessionData.getUser()).orElseThrow();
		if(u == thisArtPost.getOwner()) {
			model.addAttribute("thisArtPost", thisArtPost);
			return "editPost";
		}else return "ViewCommentBuyPost";
		
	}

	@PostMapping("/post/edit/confirm")
	public RedirectView editPostConfirm(Model model, ArtPost thisArtPost, @RequestParam(required = false) MultipartFile imagen) throws IOException {
		if(!imagen.isEmpty()) {
			Image newImage = imageService.createImage(imagen);
			thisArtPost.setImage(newImage);
		}
		artPostRepository.save(thisArtPost);
		return new RedirectView("/post");
	}
	
	@GetMapping("/post/delete")
	public RedirectView deleteUser(Model model, ArtPost thisArtPost) {
		User u = userRepository.findById(sessionData.getUser()).orElseThrow();
		if(u != thisArtPost.getOwner()) {
			u.getMyPosts().remove(thisArtPost);
			artPostRepository.delete(thisArtPost);
			return new RedirectView("/");
		}else return new RedirectView("/post/edit");
	}
	

}
