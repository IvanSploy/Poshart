package es.urjc.dad.poshart.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Collection {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private String description;

	@ManyToOne
	private User owner;

	@ManyToMany
	private List<ArtPost> posts;

	public Collection() {
		// Used by JPA.
	}

	// Constructor con lo necesario no estrictamente obligatorio.
	public Collection(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<ArtPost> getPosts() {
		return posts;
	}

	public void setPosts(List<ArtPost> posts) {
		this.posts = posts;
	}
}
