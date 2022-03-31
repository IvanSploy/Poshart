package es.urjc.dad.poshart.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.urjc.dad.poshart.model.Image;
import es.urjc.dad.poshart.repository.ArtPostRepository;
import es.urjc.dad.poshart.repository.ImageRepository;
import es.urjc.dad.poshart.service.ImageService;


public class ImageRestController {
	
	@Autowired
	private ImageRepository images;
	@Autowired
	private ImageService imageService;
	
	@PostMapping("/{id}/image")
	public ResponseEntity<Object> uploadImage(@RequestParam MultipartFile imageFile) throws SQLException, IOException {
		Image image = imageService.createImage(imageFile);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/image/{id}").buildAndExpand(image.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/{id}/image")
	public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
	 return this.imageService.createResponseFromImage(id);
	}
	
	@DeleteMapping("/{id}/image")
	public ResponseEntity<Object> deleteImage(@PathVariable long id) throws SQLException {
	 Image image = images.findById(id).orElseThrow();
	 if(image != null) {
		 image.setImage(null);
		 images.save(image);
		 this.imageService.deleteImage(id);
		 return ResponseEntity.noContent().build();
	 } else {
		 return ResponseEntity.notFound().build();
	 }
	}

}
