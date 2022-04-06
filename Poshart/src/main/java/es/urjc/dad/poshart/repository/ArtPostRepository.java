package es.urjc.dad.poshart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import es.urjc.dad.poshart.model.ArtPost;

public interface ArtPostRepository extends JpaRepository<ArtPost, Long> {
	Page<ArtPost> findAll(Pageable page);
	List<ArtPost> findByName (String name);
	//Esta petición busca dentro de todos los usuarios en busca del usuario indicado como seguidor.
	//Después devuelve todas las obras de arte de esos usuarios y finalmente las ordena por fecha.
	@Query("SELECT ap FROM ArtPost ap WHERE ap.owner in (SELECT uf.id FROM User uf WHERE "
			+ "(SELECT u FROM User u WHERE u.id = ?1)"
			+ " MEMBER OF uf.followers) ORDER BY ap.date")
	Page<ArtPost> findByUserFollows(long id, Pageable page);
	
	@Query("SELECT ap FROM ArtPost ap WHERE ap.name LIKE %?1% "
			+ "or ap.description LIKE %?1% or ap.owner.username LIKE %?1% ORDER BY ap.name")
	Page<ArtPost> findBySearch(String search, Pageable page);
}
