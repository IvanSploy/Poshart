package es.urjc.dad.poshart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.urjc.dad.poshart.model.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long>{

	List<Collection> findByOwnerId(long id);
	
	@Query("SELECT c FROM Collection c WHERE c.name LIKE %?1% or c.description LIKE %?1% or c.owner.username LIKE %?1%")
	Page<Collection> findBySearch(String search, Pageable page);
}
