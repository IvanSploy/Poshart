package es.urjc.dad.poshart.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.urjc.dad.poshart.model.Collection;
import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	@Cacheable("Users")
	@Query("SELECT u FROM User u WHERE u.name LIKE %?1% or u.username LIKE %?1% or u.surname LIKE %?1%")
	Page<User> findBySearch(String search, Pageable page);
	@Cacheable("Users")
	Page<User> findAll(Pageable page);
	@Cacheable("Users")
	User findByUsername(String username);
	@Cacheable("Users")
	User findByMail(String mail);
	
	@CacheEvict(value = "Users", allEntries = true)
	void delete(User newUser);
	
	@CachePut(value = "Users")
	User save(User newUser);
}