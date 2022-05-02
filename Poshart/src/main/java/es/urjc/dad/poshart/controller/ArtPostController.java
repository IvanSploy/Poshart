package es.urjc.dad.poshart.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
import es.urjc.dad.poshart.model.Comment;
import es.urjc.dad.poshart.model.Image;
import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.internal_service.EmailService;
import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.repository.CommentRepository;
import es.urjc.dad.poshart.repository.ShoppingCartRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.repository.ArtPostRepository;
import es.urjc.dad.poshart.repository.CollectionRepository;
import es.urjc.dad.poshart.service.ImageService;

@Controller
@RequestMapping("/post")
public class ArtPostController {
	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ArtPostRepository artPostRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CollectionRepository collectionRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ShoppingCartRepository cartRepository;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/createPost")
	public String newPost(Model model) {
		return "newPost";
	}
	
	@PostMapping("/newPost")
	public RedirectView tryNewPost(Model model, ArtPost newArtPost, HttpServletRequest request,
			@RequestParam(required = false) MultipartFile imagen) throws IOException {
		
		if(!imagen.isEmpty()) {
			Image newImage = imageService.createImage(imagen);
			newArtPost.setImage(newImage);
		}
		newArtPost.addOwner(userRepository.findByUsername(request.getUserPrincipal().getName()));
		newArtPost.setDate(Date.from(Instant.now()));
		artPostRepository.save(newArtPost);
		return new RedirectView("/post/"+newArtPost.getId());
	}
	
	@GetMapping("/{id}")
	public String viewPost(HttpServletRequest request, Model model, @PathVariable long id) {
		ArtPost ap = artPostRepository.findById(id).orElseThrow();
		if(request.isUserInRole("USER")) {
			User u = userRepository.findByUsername(request.getUserPrincipal().getName());
			if(u == ap.getOwner()) {
				model.addAttribute("isMine", true);
			}
			model.addAttribute("isBought", u.getCart().getArt().contains(ap));
			model.addAttribute("myCollections", u.getCollections());
		}
		model.addAttribute("ArtPost", ap);
		return "viewPost";
	}
	
	@GetMapping("/{id}/addToShopping")
	public RedirectView addToCart(HttpServletRequest request, Model model, @PathVariable long id) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		ArtPost ap = artPostRepository.findById(id).orElseThrow();
		//Solo te deja añadir al carro de la compra si no eres su dueño y si no lo tienes ya añadido
		if(u != ap.getOwner()) {
			if(!u.getCart().getArt().contains(ap)) {
				u.getCart().addArt(ap);
				cartRepository.save(u.getCart());
			}
		}
		return new RedirectView("/post/"+ap.getId());
	}
	
	@GetMapping("/{id}/edit")
	public String editPost(HttpServletRequest request, Model model, @PathVariable long id) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		ArtPost ap = artPostRepository.findById(id).orElseThrow();
		model.addAttribute("thisArtPost", ap);
		if(u == ap.getOwner()) {
			return "editPost";
		}else return "viewPost";
	}

	@PostMapping("/{id}/edit/confirm")
	public RedirectView editPostConfirm(Model model, ArtPost newArtPost, @PathVariable long id) {
		ArtPost ap = artPostRepository.findById(id).orElseThrow();
		ap.setName(newArtPost.getName());
		ap.setDescription(newArtPost.getDescription());
		ap.setPrice(newArtPost.getPrice());
		artPostRepository.save(ap);
		return new RedirectView("/post/" + id);
	}
	
	@GetMapping("{id}/delete")
	public RedirectView deletePost(HttpServletRequest request, Model model, @PathVariable long id) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		ArtPost ap = artPostRepository.findById(id).orElseThrow();
		if(u == ap.getOwner()) {
			u.removePost(ap);
			for(ShoppingCart sc : ap.getCarts()) {
				sc.removeArt(ap);
			}
			artPostRepository.delete(ap);
		}
		return new RedirectView("/");
	}
	//RELACIONADO CON COMENTARIOS
	@PostMapping("/{id}/comment/new")
	public RedirectView createComment(HttpServletRequest request, Model model, @PathVariable long id, Comment comment) {
		User u = userRepository.findByUsername(request.getUserPrincipal().getName());
		ArtPost ap = artPostRepository.findById(id).orElseThrow();
		comment.setId(0);
		comment.setOwner(u);
		comment.setCommentDate(Date.from(Instant.now()));
		ap.addComment(comment);
		artPostRepository.save(ap);
		emailService.sendNotifyCommentEmail(comment);
		return new RedirectView("/post/"+id);
	}
	
	@GetMapping("/{id}/comment/{idComment}/delete")
	public RedirectView removeComment(Model model, @PathVariable long id, @PathVariable long idComment) {
		ArtPost ap = artPostRepository.findById(id).orElseThrow();
		Comment c = commentRepository.findById(idComment).orElseThrow();
		c.removePost(ap);
		c.setOwner(null);
		commentRepository.delete(c);
		return new RedirectView("/post/"+id);
	}
	
	//RELACIONADO CON COLECCIONES
	@GetMapping("/{id}/add")
	public RedirectView addPostToCollection(HttpServletRequest request, Model model, @PathVariable long id, @RequestParam long colId) {
		ArtPost ap = artPostRepository.findById(id).orElseThrow();
		if(colId > 0) {
			Collection c = collectionRepository.findById(colId).orElseThrow();
			if(request.isUserInRole("ROLE_USER")) {
				User myUser = userRepository.findByUsername(request.getUserPrincipal().getName());
				if(myUser.getCollections().contains(c)) {
					c.addPost(ap);
					collectionRepository.save(c);
				}
			}
		}
		return new RedirectView("/post/"+id);
	}
	
	@GetMapping("/{id}/remove/{colId}")
	public RedirectView removePostFromCollection(HttpServletRequest request, Model model, @PathVariable long id, @PathVariable long colId) {
		ArtPost ap = artPostRepository.findById(id).orElseThrow();
		User myUser = userRepository.findByUsername(request.getUserPrincipal().getName());
		if(colId > 0) {
			Collection c = collectionRepository.findById(colId).orElseThrow();
			if(myUser.getCollections().contains(c)) {
				c.removePost(ap);
				collectionRepository.save(c);
			}
			return new RedirectView("/user/"+myUser.getId()+"/?colId="+colId);
		}
		return new RedirectView("/user/"+myUser.getId());
	}
}
