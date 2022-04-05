package es.urjc.dad.poshart.model;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Image {
	@Id
	@SequenceGenerator(initialValue = 1, name = "imageGen")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imageGen")
	@JsonView(JsonInterfaces.Basico.class)
	private long id;

	@JsonIgnore
	private String image;
	
	@JsonView(JsonInterfaces.Basico.class)
	private String imageType;

	@JsonView(JsonInterfaces.Basico.class)
	@Lob
	private Blob imageFile;

	public Image() {
		//Used by JPA.
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
	public Blob getImageFile() {
		return imageFile;
	}

	public void setImageFile(Blob imageFile) {
		this.imageFile = imageFile;
	}
	
	public boolean equals(Object obj) {
		if(obj==null) return false;
		if(this==obj) return true;
		if(getClass()!=obj.getClass()) return false;
		return getId()==((Image)obj).getId();
	}
}
