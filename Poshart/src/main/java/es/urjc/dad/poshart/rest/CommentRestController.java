package es.urjc.dad.poshart.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.dad.poshart.model.Comment;
import es.urjc.dad.poshart.model.JsonInterfaces;
import es.urjc.dad.poshart.repository.CommentRepository;

// Usada para ver la estructura de los JSON via Web.
@RestController
public class CommentRestController {
	
	@Autowired
	private CommentRepository comments;
	
	@JsonView(JsonInterfaces.Basico.class)
	@GetMapping("/comments/{id}")
	public ResponseEntity<Comment> getComment(@PathVariable long id) {
		Comment artpost = comments.findById(id).orElseThrow();
		if (artpost != null) {
			return ResponseEntity.ok(artpost);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@JsonView(JsonInterfaces.BasicoAvanzado.class)
	@GetMapping("/comments/{id}/showDetails")
	public ResponseEntity<Comment> getCommentMoreDetails(@PathVariable long id) {
		Comment comment = comments.findById(id).orElseThrow();
		if (comment != null) {
			return ResponseEntity.ok(comment);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
