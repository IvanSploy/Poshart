package es.urjc.dad.poshart.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.dad.poshart.service.ImageService;

@Controller
@RequestMapping("/image")
public class ImageController {
	
	@Autowired
	private ImageService imageService;

	@PostMapping("/new")
	public String newImage(Model model, @RequestParam MultipartFile image) throws IOException {
		
		imageService.createImage(image);
		return "confirm";
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getImage(Model model, @PathVariable long id) throws SQLException {
		
		return imageService.createResponseFromImage(id);
	}
	
	@GetMapping("/{id}/delete")
	public String deleteImage(Model model, @PathVariable long id) {
		
		imageService.deleteImage(id);
		return "confirm";
	}
}
