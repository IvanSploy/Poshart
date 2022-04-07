package es.urjc.dad.poshart.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.dad.poshart.model.JsonInterfaces;
import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.repository.ShoppingCartRepository;

//Usada para ver la estructura de los JSON via Web.
@RestController
public class ShoppingCartRestController {
	@Autowired
	private ShoppingCartRepository carts;
	
	@JsonView(JsonInterfaces.Basico.class)
	@GetMapping("/carts/{id}")
	public ResponseEntity<ShoppingCart> getCart(@PathVariable long id) {
		ShoppingCart shopcart = carts.findById(id).orElseThrow();
		if (shopcart != null) {
			return ResponseEntity.ok(shopcart);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@JsonView(JsonInterfaces.BasicoAvanzado.class)
	@GetMapping("/carts/{id}/showDetails")
	public ResponseEntity<ShoppingCart> getCartMoreDetails(@PathVariable long id) {
		ShoppingCart shopcart = carts.findById(id).orElseThrow();
		if (shopcart != null) {
			return ResponseEntity.ok(shopcart);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
