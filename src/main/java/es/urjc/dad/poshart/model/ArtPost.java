package es.urjc.dad.poshart.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ArtPost {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	private String description;
	private float price;
	private Date date;
	
	@ManyToOne
	private User owner;
	
	@OneToMany (mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> postComments = new ArrayList<>();	
	
	@ManyToOne
	private Image image;

	public ArtPost() {
		//Used by JPA
	}
	
	//Constructor con lo necesario no estrictamente obligatorio.
	public ArtPost(String name, float price) {
		super();
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public void addComment(Comment comment) {
		postComments.add(comment);
		comment.setPost(this);
	}
	
	public List<Comment> getPostComments() {
		return postComments;
	}
	
	public void removeComment(Comment comment) {
		postComments.remove(comment);
		comment.setPost(this);
	}
}
