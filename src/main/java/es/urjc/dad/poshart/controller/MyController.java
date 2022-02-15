package es.urjc.dad.poshart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.urjc.dad.poshart.repository.UserRepository;

@Controller
public class MyController {
	
	@Autowired
	private UserRepository repository;
	
	@GetMapping("/")
	public String startPage(Model model) {
		return "start";
	}
	
	@GetMapping("/mustache")
	public String devuelvePlantilla() {
		return "MiPlantilla";
	}
}
