package es.urjc.dad.poshart.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true)
	private String username;
	private String password;
	private String mail;
	private String name;
	private String surname;
	private String description;
	
	@ManyToOne
	private Image image;

	@ManyToMany
	private Set<User> follows = new HashSet<>();

	@ManyToMany(mappedBy = "follows")
	private Set<User> followers = new HashSet<>();

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Collection> collections = new ArrayList<>();

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ArtPost> myPosts = new ArrayList<>();

	@OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
	private ShoppingCart cart;

	public User() {
		// Used by JPA.
	}

	// Constructor con lo necesario no estrictamente obligatorio.
	public User(String mail, String username, String password, String name, String surname, String description) {
		super();
		this.mail = mail;
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.description = description;
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
	
	public void addFollow(User follow) {
		this.follows.add(follow);
	}

	public Set<User> getFollows() {
		return follows;
	}
	
	public void removeFollow(User follow) {
		this.follows.remove(follow);
	}
	
	public void addFollower(User follower) {
		this.followers.add(follower);
	}

	public Set<User> getFollowers() {
		return followers;
	}
	
	public void removeFollower(User follower) {
		this.followers.remove(follower);
	}
	
	public void addCollection(Collection collection) {
		this.collections.add(collection);
		collection.setOwner(this);
	}

	public List<Collection> getCollections() {
		return collections;
	}
	
	public void removeCollection(Collection collection) {
		this.collections.remove(collection);
		collection.setOwner(null);
	}
	
	public void addPost(ArtPost post) {
		this.myPosts.add(post);
		post.setOwner(this);
	}

	public List<ArtPost> getMyPosts() {
		return myPosts;
	}

	public void removePost(ArtPost post) {
		this.myPosts.remove(post);
		post.setOwner(null);
	}
	
	public void addCart(ShoppingCart cart) {
		this.cart = cart;
		cart.setBuyer(this);
	}

	public ShoppingCart getCart() {
		return cart;
	}

	public void removeCart() {
		if(cart!=null) {
			cart.setBuyer(null);
			this.cart = null;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(this==obj) return true;
		if(getClass()!=obj.getClass()) return false;
		return getId()==((User)obj).getId();
	}
}
