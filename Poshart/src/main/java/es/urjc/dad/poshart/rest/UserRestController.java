package es.urjc.dad.poshart.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.model.JsonInterfaces;
import es.urjc.dad.poshart.repository.UserRepository;

//Usada para ver la estructura de los JSON via Web.
@RestController
public class UserRestController {
	
	@Autowired
	private UserRepository users;
	
	@GetMapping("/users/{id}")
	@JsonView(JsonInterfaces.Basico.class)
	public ResponseEntity<User> getPost(@PathVariable long id) {
		User user = users.findById(id).orElseThrow();
		if (user != null) {
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/users/{id}/showDetails")
	@JsonView(JsonInterfaces.BasicoAvanzado.class)
	public ResponseEntity<User> getPostMoreDetails(@PathVariable long id) {
		User user = users.findById(id).orElseThrow();
		if (user != null) {
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
