package es.urjc.dad.poshart.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.Date;

@Entity
public class ShoppingCart {
	
	@Id
	@SequenceGenerator(initialValue = 1, name = "cartGen")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartGen")
	@JsonView(JsonInterfaces.Basico.class)
	private long id;
	
	@JsonView(JsonInterfaces.Basico.class)
	private float price;
	
	@JsonView(JsonInterfaces.Basico.class)
	private Date date;
	
	@JsonView(JsonInterfaces.Avanzado.class)
	@ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
	private List<ArtPost> art = new ArrayList<>();
	
	public void addArtPost(ArtPost post) {
		art.add(post);
	}
	
	public void removeArtPost(ArtPost post) {
		art.remove(post);
	}

	@JsonView(JsonInterfaces.Avanzado.class)
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
	
	public ShoppingCart(float price) {
		super();
		this.setPrice(price);
		this.setDate(new Date(1000));
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getPrice() {
		price = 0;
		for(int i=0; i<art.size(); i++)
		{
			price+=art.get(i).getPrice();
		}
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

	public void setArt(List<ArtPost> art) {
		this.art = art;
	}
	
	public List<ArtPost> getArt() {
		return art;
	}

	public void addArt(ArtPost artPost) {
		art.add(artPost);
	}
	
	public void removeArt(ArtPost artPost) {
		art.remove(artPost);
	}
	public void clear() {
		art.clear();
	}
	
	public void removeArt(int i) {
		if(i<art.size()) {
			art.remove(i);
		}	
	}
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(this==obj) return true;
		if(getClass()!=obj.getClass()) return false;
		return getId()==((ShoppingCart)obj).getId();
	}	
}
