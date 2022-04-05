package es.urjc.dad.PoshartInternal.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/email")
public class EmailController {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@PostMapping("/artpost")
	public void sendEmailArtpost(@RequestBody ObjectNode post) throws URISyntaxException {

		String valor = post.get("name").asText();
		log.warn(valor);
	}
	
	@PostMapping("/artposts")
	public void sendEmailArtposts(@RequestBody List<ObjectNode> posts) throws URISyntaxException {

		for (ObjectNode o : posts) {
			String valor = o.get("name").asText();
			log.warn(valor);
		}
	}
}
