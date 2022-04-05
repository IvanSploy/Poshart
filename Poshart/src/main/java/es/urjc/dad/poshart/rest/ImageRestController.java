package es.urjc.dad.poshart.rest;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.urjc.dad.poshart.service.ImageService;

//Usada para ver la estructura de los JSON via Web.
public class ImageRestController {

	@Autowired
	private ImageService imageService;
	
	@GetMapping("/{id}/image")
	public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
	 return this.imageService.createResponseFromImage(id);
	}
}
