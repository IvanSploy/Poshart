package es.urjc.dad.poshart.model;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String description;
	private String commentDate;
	
	@OneToOne
	private User owner;
	
	@ManyToOne
	private ArtPost post;
	
	@ManyToOne
	private Comment responseTo;
	
	@OneToMany(mappedBy = "responseTo", cascade=CascadeType.ALL, orphanRemoval = true)
	private List<Comment> responses= new ArrayList<>();
	
	
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

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
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

	public Comment getResponseTo() {
		return responseTo;
	}

	public void setResponseTo(Comment responseTo) {
		this.responseTo = responseTo;
	}

	public List<Comment> getResponses() {
		return responses;
	}
	
	public void addResponse(Comment comment) {
		responses.add(comment);
		comment.setResponseTo(this);
	}
	
	public void removeResponse(Comment comment) {
		responses.remove(comment);
		comment.setResponseTo(this);
	}
	
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(this==obj) return true;
		if(getClass()!=obj.getClass()) return false;
		return getId()==((Comment)obj).getId();
	}
}
