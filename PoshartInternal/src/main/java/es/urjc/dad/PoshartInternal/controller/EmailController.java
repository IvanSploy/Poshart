package es.urjc.dad.PoshartInternal.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.urjc.dad.PoshartInternal.service.EmailSenderService;

@RestController
@RequestMapping("/email")
public class EmailController {
	
	@Autowired
	EmailSenderService emailService;

	Logger log = LoggerFactory.getLogger(getClass());
	
	//Se crea un correo que informa al usuario que se ha registrado en la aplicación.
	@PostMapping("/confirmUser")
	public void sendEmailArtpost(@RequestBody ObjectNode user) throws URISyntaxException {
		String email = user.get("mail").asText();
		String username = user.get("username").asText();
		String name = user.get("name").asText();
		String surname = user.get("surname").asText();
		String description = user.get("description").asText();
		
		String subject = "¡Bienvenido a Poshart " + username + "!";
		String body = "Hola " + name + ",\n" +
				"ha recibido este correo para confirmar su registro en Poshart.\n\n" +
				"Estos son sus datos:\n" +
				"\tUsuario: " + username + "\n" +
				"\tEmail: " + email + "\n" +
				"\tNombre: " + name + "\n" +
				"\tApellidos: " + surname + "\n" +
				"\tDescripción: " + description + "\n\n" + 
				"Puede cambiarlos cuando quiera en la web.\n" +
				"¡Disfrute su estancia!";
		emailService.sendEmail(email, subject, body);
	}
	
	@PostMapping("/notifyComment")
	public void sendEmailNewComment(@RequestBody ObjectNode comment) throws URISyntaxException {
		JsonNode owner = comment.get("owner");
		JsonNode post = comment.get("post");
		JsonNode receiver = post.get("owner");
		
		String email = receiver.get("mail").asText();
		String userReceiver = receiver.get("username").asText();
		String name = owner.get("username").asText();
		String followers = owner.get("countFollowers").asText();
		String description = comment.get("description").asText();
		String postName = post.get("name").asText();
		
		
		String subject = "¡Nuevo comentario en tu publicación!";
		String body = "El usuario " + name + " ha comentado en tu post " + postName + " de Poshart:\n" +
				"\tComentario: " + description + "\n" +
				"\t¡Tiene  un total de " + followers + " seguidores!\n" +
				userReceiver + ", tu post cada vez tiene más fama, ¡alguien terminará por comprarlo!\n" +
				"Atentamente, el equipo de Poshart.";
		emailService.sendEmail(email, subject, body);
	}
	
	@PostMapping("/purchase")
	public void sendNotifyPurchase(@RequestBody ObjectNode post) throws URISyntaxException {
		JsonNode owner = post.get("owner");
		
		String email = post.get("email").asText();
		String price = post.get("price").asText();
		String username = owner.get("username").asText();
		String postName = post.get("name").asText();
		
		String subject = "¡Te han comprado una obra!";
		String body = "Enhorabuena,\n" +
				"el usuario " + username + " ha comprado tu obra " + postName +" por la cantidad de " + price +"€\n\n" +
				"¡Siga ganando dinero con nuestra web!";
		emailService.sendEmail(email, subject, body);
	}
	
	@PostMapping("/receipt")
	public void sendPurchaseReceipt(@RequestBody ObjectNode post) throws URISyntaxException {
		JsonNode owner = post.get("buyer");
		ArrayNode arts = (ArrayNode) post.get("art");
		
		String email = owner.get("mail").asText();
		String price = post.get("price").asText();
		
		String body = "Su compra ha sido realizada completamente,\n" +
			"a continuación se le muestra el resumen de su compra:\n";
		
		for (int i = 0; i < arts.size(); i++) {
			 JsonNode item = arts.get(i);
			 String nombre = item.get("name").asText();
			 String precioItem = item.get("price").asText();
			 body += "\t" + nombre + "\t" + precioItem + "€\n";
		}
		
		body += "Gasto total realizado: " + price + "€\n" +
		"¡No deje de ver nuestra web!";
		
		String subject = "¡Compra realizada!";
		
		emailService.sendEmail(email, subject, body);
	}
}
