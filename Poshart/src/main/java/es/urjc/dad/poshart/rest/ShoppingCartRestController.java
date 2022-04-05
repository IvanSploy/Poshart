package es.urjc.dad.poshart.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.repository.ShoppingCartRepository;

//Usada para ver la estructura de los JSON via Web.
public class ShoppingCartRestController {
	@Autowired
	private ShoppingCartRepository carts;
	
	@JsonView(ShoppingCart.Basico.class)
	@GetMapping("/carts/{id}")
	public ResponseEntity<ShoppingCart> getCart(@PathVariable long id) {
		ShoppingCart shopcart = carts.findById(id).orElseThrow();
		if (shopcart != null) {
			return ResponseEntity.ok(shopcart);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	interface ShoppingCartDetalle extends ShoppingCart.Basico, ShoppingCart.ArteComprado {
	}
	@JsonView(ShoppingCartDetalle.class)
	@GetMapping("/carts/{id}/listaDeArte")
	public ResponseEntity<ShoppingCart> getCartMoreDetails(@PathVariable long id) {
		ShoppingCart shopcart = carts.findById(id).orElseThrow();
		if (shopcart != null) {
			return ResponseEntity.ok(shopcart);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
