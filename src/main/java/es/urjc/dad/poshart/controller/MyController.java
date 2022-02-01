package es.urjc.dad.poshart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {
	
	//Cuando se le llama a esta dirección URL se ejecuta el método con GetMapping.
	@GetMapping("/mustache")
	public String devuelvePlantilla() {
		return "MiPlantilla";
	}
}
