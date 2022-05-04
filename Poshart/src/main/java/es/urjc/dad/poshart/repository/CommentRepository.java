package es.urjc.dad.poshart.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.model.Comment;
import es.urjc.dad.poshart.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {	

	List<Comment> findByPostId(long id);
	
	@CacheEvict(value = "Posts", allEntries = true)
	void delete(Comment newComment);
	
	@CacheEvict(value = "Posts", allEntries = true)
	Comment save(Comment newComment);
}
