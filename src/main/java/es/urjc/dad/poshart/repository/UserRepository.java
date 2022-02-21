package es.urjc.dad.poshart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.dad.poshart.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Page<User> findAll(Pageable page);
	
	List<User> findByUsername(String username);
}