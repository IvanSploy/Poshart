package es.urjc.dad.poshart.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.dad.poshart.model.ArtPost.Basico;
import es.urjc.dad.poshart.model.ArtPost.DetallesAvanzados;

@Entity
public class User {

	public interface Basico {}
	public interface DetallesAvanzados{}
	public interface UserDetalle extends Basico, DetallesAvanzados {}
	
	@Id
	@SequenceGenerator(initialValue = 1, name = "userGen")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userGen")
	private long id;

	@Column(unique = true)
	@JsonView(Basico.class)
	private String username;
	
	@JsonIgnore
	private String password;
	
	@Column(unique = true)
	@JsonView(Basico.class)
	private String mail;
	
	@JsonView(Basico.class)
	private String name;
	
	@JsonView(Basico.class)
	private String surname;
	
	@JsonView(Basico.class)
	private String description;
	
	@JsonView(Basico.class)
	private int countFollows;
	
	@JsonView(Basico.class)
	private int countFollowers;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JsonView(Basico.class)
	private Image image;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@JsonIgnore
	private List<String> roles;//lista de los roles que puede tomar un usuario

	@ManyToMany
	@JsonIgnore
	private Set<User> follows = new HashSet<>();

	@ManyToMany(mappedBy = "follows")
	@JsonIgnore
	private Set<User> followers = new HashSet<>();

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonView(DetallesAvanzados.class)
	private List<Collection> collections = new ArrayList<>();

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonView(DetallesAvanzados.class)
	private List<ArtPost> myPosts = new ArrayList<>();

	@OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private ShoppingCart cart;

	public User() {
		// Used by JPA.
	}

	// Constructor con lo necesario estrictamente obligatorio.
	public User(String mail, String username, String password, String... roles) {
		super();
		this.mail = mail;
		this.username = username;
		this.password = password;
		setRoles(Arrays.asList(roles));
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
	
	public int getCountFollows() {
		return countFollows;
	}

	public void setCountFollows(int countFollows) {
		this.countFollows = countFollows;
	}

	public int getCountFollowers() {
		return countFollowers;
	}

	public void setCountFollowers(int countFollowers) {
		this.countFollowers = countFollowers;
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	
	
	public void addFollow(User follow) {
		this.follows.add(follow);
		countFollows++;
	}

	public Set<User> getFollows() {
		return follows;
	}
	
	public List<String> getRoles() {
	    return roles;
	}
	
	public void setRoles(List<String> roles) {
	   	this.roles = roles;
	}
	
	public void removeFollow(User follow) {
		this.follows.remove(follow);
		countFollows--;
	}
	
	public void addFollower(User follower) {
		this.followers.add(follower);
		countFollowers++;
	}

	public Set<User> getFollowers() {
		return followers;
	}
	
	public void removeFollower(User follower) {
		this.followers.remove(follower);
		countFollowers--;
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
