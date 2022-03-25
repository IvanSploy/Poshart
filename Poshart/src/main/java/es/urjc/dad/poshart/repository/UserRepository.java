package es.urjc.dad.poshart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.urjc.dad.poshart.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query("SELECT u FROM User u WHERE u.name LIKE %?1% or u.username LIKE %?1% or u.surname LIKE %?1%")
	Page<User> findBySearch(String search, Pageable page);
	
	Page<User> findAll(Pageable page);
	
	User findByUsername(String username);

	User findByMail(String mail);
}