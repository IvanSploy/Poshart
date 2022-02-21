package es.urjc.dad.poshart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.dad.poshart.model.ArtPost;

public interface ArtPostRepository extends JpaRepository<ArtPost, Long> {
	Page<ArtPost> findAll(Pageable page);
	List<ArtPost> findByArtPostName (String ArtPostName);
}
