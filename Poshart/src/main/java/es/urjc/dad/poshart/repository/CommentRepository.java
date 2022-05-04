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
	@CacheEvict(value = "Posts", allEntries = true)
	@Cacheable("Comments")
	List<Comment> findByPostId(long id);
	@CacheEvict(value = "Comments", allEntries = true)
	void delete(Comment newComment);
	@CacheEvict(value = "Comments", allEntries = true)
	Comment save(Comment newComment);
}
