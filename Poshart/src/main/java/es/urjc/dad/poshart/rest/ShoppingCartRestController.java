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

import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.repository.ShoppingCartRepository;


public class ShoppingCartRestController {
	@Autowired
	private ShoppingCartRepository carts;
	
	@GetMapping("/carts/")
	public List<ShoppingCart> getPosts() {
		return carts.findAll();
	}
	
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
	
	@JsonView(ShoppingCartDetalle.class)
	@PostMapping("/carts/")
	public ResponseEntity<ShoppingCart> createCart(@RequestBody ShoppingCart cart) {
		 carts.save(cart);
		 URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cart.getId()).toUri();
		 return ResponseEntity.created(location).body(cart);
	}
	
	@DeleteMapping("/carts/{id}")
	public ResponseEntity<ShoppingCart> deleteCart(@PathVariable long id) {
		ShoppingCart cart = carts.findById(id).orElseThrow();
		if (cart != null) {
			carts.deleteById(id);
			return ResponseEntity.ok(cart);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/carts/{id}")
	public ResponseEntity<ShoppingCart> replaceCart(@PathVariable long id,@RequestBody ShoppingCart newShoppingCart) {
		ShoppingCart cart = carts.findById(id).orElseThrow();
		if (cart != null) {
			newShoppingCart.setId(id);
			carts.save(newShoppingCart);
			return ResponseEntity.ok(cart);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
