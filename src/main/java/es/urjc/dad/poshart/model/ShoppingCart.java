package es.urjc.dad.poshart.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import java.util.Date;

@Entity
public class ShoppingCart {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String price;
	private Date date;
	
	@ManyToMany
	private List<ArtPost> art;

	@OneToOne
	private User buyer; 
	
	public ShoppingCart() {
		//Used by JPA.
	}
	
	//Constructor con lo necesario no estrictamente obligatorio.
	public ShoppingCart(String price, Date date) {
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
}
