package es.urjc.dad.poshart.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.repository.ArtPostRepository;

//Usada para ver la estructura de los JSON via Web.
@RestController
public class ArtPostRestController {
	
	@Autowired
	private ArtPostRepository posts;
	
	@GetMapping("/posts/{id}")
	@JsonView(ArtPost.Basico.class)
	public ResponseEntity<ArtPost> getPost(@PathVariable long id) {
		ArtPost artpost = posts.findById(id).orElseThrow();
		if (artpost != null) {
			return ResponseEntity.ok(artpost);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/posts/{id}/showDetails")
	@JsonView(ArtPost.ArtPostDetalle.class)
	public ResponseEntity<ArtPost> getPostMoreDetails(@PathVariable long id) {
		ArtPost artpost = posts.findById(id).orElseThrow();
		if (artpost != null) {
			return ResponseEntity.ok(artpost);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
