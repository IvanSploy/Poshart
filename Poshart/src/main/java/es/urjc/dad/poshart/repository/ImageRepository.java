package es.urjc.dad.poshart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.dad.poshart.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{
	
}
