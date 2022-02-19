package es.urjc.dad.poshart.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import java.util.ArrayList;
import java.util.Date;

@Entity
public class ShoppingCart {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private float price;
	private Date date;
	
	@ManyToMany
	private List<ArtPost> art= new ArrayList<>();
	
	public void addArtPost(ArtPost post) {
		art.add(post);
	}
	
	public void removeArtPost(ArtPost post) {
		art.remove(post);
	}

	@OneToOne
	private User buyer; 
	

	public ShoppingCart() {
		//Used by JPA.
	}
	
	//Constructor con lo necesario no estrictamente obligatorio.
	public ShoppingCart(float price, Date date) {
		super();
		this.setPrice(price);
		this.setDate(date);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}
	
	
	
}
