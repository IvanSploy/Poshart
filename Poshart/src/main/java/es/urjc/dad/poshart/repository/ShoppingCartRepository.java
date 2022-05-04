package es.urjc.dad.poshart.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
	 @CacheEvict(value = "Posts", allEntries = true)
	 ShoppingCart save(ShoppingCart newShoppingCart);	
}
