package es.urjc.dad.poshart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.dad.poshart.model.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long>{

	List<Collection> findByOwnerId(long id);
}
