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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.repository.ArtPostRepository;

@RestController
public class ArtPostRestController {
	
	@Autowired
	private ArtPostRepository posts;
	
	@GetMapping("/posts/")
	public List<ArtPost> getPosts() {
		return posts.findAll();
	}
	
	@JsonView(ArtPost.Basico.class)
	@GetMapping("/posts/{id}")
	public ResponseEntity<ArtPost> getPost(@PathVariable long id) {
		ArtPost artpost = posts.findById(id).orElseThrow();
		if (artpost != null) {
			return ResponseEntity.ok(artpost);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	interface ArtPostDetalle extends ArtPost.Basico, ArtPost.DetallesAvanzados {
	}
	@JsonView(ArtPostDetalle.class)
	@GetMapping("/posts/{id}/showDetails")
	public ResponseEntity<ArtPost> getPostMoreDetails(@PathVariable long id) {
		ArtPost artpost = posts.findById(id).orElseThrow();
		if (artpost != null) {
			return ResponseEntity.ok(artpost);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@JsonView(ArtPostDetalle.class)
	@PostMapping("/posts/")
	public ResponseEntity<ArtPost> createPost(@RequestBody ArtPost post) {
		 posts.save(post);
		 URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();
		 return ResponseEntity.created(location).body(post);
	}
	
	@DeleteMapping("/posts/{id}")
	public ResponseEntity<ArtPost> deletePost(@PathVariable long id) {
		ArtPost post = posts.findById(id).orElseThrow();
		if (post != null) {
			posts.deleteById(id);
			return ResponseEntity.ok(post);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/posts/{id}")
	public ResponseEntity<ArtPost> replacePost(@PathVariable long id,@RequestBody ArtPost newArtPost) {
		ArtPost post = posts.findById(id).orElseThrow();
		if (post != null) {
			newArtPost.setId(id);
			posts.save(newArtPost);
			return ResponseEntity.ok(post);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
