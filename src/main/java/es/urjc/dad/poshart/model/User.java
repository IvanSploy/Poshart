package es.urjc.dad.poshart.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String username;
	private String password;
	private String mail;
	private String name;
	private String surname;
	private String description;
	
	@ManyToOne
	private Image image;

	@ManyToMany
	private List<User> follows;

	@ManyToMany(mappedBy = "follows")
	private List<User> followers;

	@OneToMany(mappedBy = "owner")
	private List<Collection> collections;

	@OneToMany(mappedBy = "owner")
	private List<ArtPost> myPosts;

	@OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
	private ShoppingCart cart;

	public User() {
		// Used by JPA.
	}

	// Constructor con lo necesario no estrictamente obligatorio.
	public User(String mail, String username, String password) {
		super();
		this.mail = mail;
		this.username = username;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public List<User> getFollows() {
		return follows;
	}

	public void setFollows(List<User> follows) {
		this.follows = follows;
	}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	public List<Collection> getCollections() {
		return collections;
	}

	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}

	public List<ArtPost> getMyPosts() {
		return myPosts;
	}

	public void setMyPosts(List<ArtPost> myPosts) {
		this.myPosts = myPosts;
	}

	public ShoppingCart getCart() {
		return cart;
	}

	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}
}
