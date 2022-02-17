package es.urjc.dad.poshart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.dad.poshart.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

}
