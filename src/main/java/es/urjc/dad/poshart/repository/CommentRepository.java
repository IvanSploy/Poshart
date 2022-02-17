package es.urjc.dad.poshart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.urjc.dad.poshart.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
