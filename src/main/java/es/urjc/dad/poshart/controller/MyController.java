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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.urjc.dad.poshart.model.Image;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.service.ImageService;

@Controller
public class MyController {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private ImageService imageService;
	
	@GetMapping("/")
	public String startPage(Model model) {
		return "start";
	}
	
	@GetMapping("/mustache")
	public String devuelvePlantilla() {
		return "MiPlantilla";
	}
	
	@PostMapping("/newImage")
	public String newImage(Model model, @RequestParam MultipartFile image) throws IOException {
		
		Image imagen = imageService.createImage(image);
		return "confirm";
	}
	
	@GetMapping("/image/{id}")
	public ResponseEntity<Object> newImage(Model model, @PathVariable long id) throws SQLException {
		
		return imageService.createResponseFromImage(id);
	}
	
	@GetMapping("/delete/{id}")
	public String deleteImage(Model model, @PathVariable long id) {
		
		imageService.deleteImage(id);
		return "confirm";
	}
}
