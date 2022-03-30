package es.urjc.dad.poshart.rest;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.dad.poshart.model.Comment;
import es.urjc.dad.poshart.repository.CommentRepository;


public class CommentRestController {
	
	@Autowired
	private CommentRepository comments;
	
	@GetMapping("/comments/")
	public List<Comment> getPosts() {
		return comments.findAll();
	}
	
	@JsonView(Comment.Basico.class)
	@GetMapping("/comments/{id}")
	public ResponseEntity<Comment> getComment(@PathVariable long id) {
		Comment artpost = comments.findById(id).orElseThrow();
		if (artpost != null) {
			return ResponseEntity.ok(artpost);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	interface CommentArtPostDetalle extends Comment.Basico, Comment.DetallesAvanzados {
	}
	@JsonView(CommentArtPostDetalle.class)
	@GetMapping("/comments/{id}/showDetails")
	public ResponseEntity<Comment> getCommentMoreDetails(@PathVariable long id) {
		Comment comment = comments.findById(id).orElseThrow();
		if (comment != null) {
			return ResponseEntity.ok(comment);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@JsonView(CommentArtPostDetalle.class)
	@PostMapping("/comments/")
	public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
		 comments.save(comment);
		 URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId()).toUri();
		 return ResponseEntity.created(location).body(comment);
	}
	
	@DeleteMapping("/comments/{id}")
	public ResponseEntity<Comment> deleteComment(@PathVariable long id) {
		Comment post = comments.findById(id).orElseThrow();
		if (post != null) {
			comments.deleteById(id);
			return ResponseEntity.ok(post);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/comments/{id}")
	public ResponseEntity<Comment> replacePost(@PathVariable long id,@RequestBody Comment newComment) {
		Comment comment = comments.findById(id).orElseThrow();
		if (comment != null) {
			newComment.setId(id);
			comments.save(newComment);
			return ResponseEntity.ok(comment);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
