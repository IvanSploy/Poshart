package es.urjc.dad.poshart.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Comment {
	@Id
	@SequenceGenerator(initialValue = 1, name = "commentGen")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentGen")
	private long id;
	
	private String description;
	private Date commentDate;
	
	@OneToOne
	private User owner;
	
	@ManyToOne
	private ArtPost post;
	
	
	public Comment() {
		//Used by JPA.
	}
	
	//Constructor con lo necesario no estrictamente obligatorio.
	public Comment(String description) {
		super();
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public ArtPost getPost() {
		return post;
	}

	public void setPost(ArtPost post) {
		this.post = post;
	}
	
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(this==obj) return true;
		if(getClass()!=obj.getClass()) return false;
		return getId()==((Comment)obj).getId();
	}
}
