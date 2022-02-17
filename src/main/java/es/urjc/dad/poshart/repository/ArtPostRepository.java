package es.urjc.dad.poshart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.dad.poshart.model.ArtPost;

public interface ArtPostRepository extends JpaRepository<ArtPost, Long> {

}
