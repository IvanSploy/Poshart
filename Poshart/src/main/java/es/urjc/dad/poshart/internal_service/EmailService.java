package es.urjc.dad.poshart.internal_service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.model.Comment;
import es.urjc.dad.poshart.model.JsonInterfaces;
import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.ArtPostRepository;
import es.urjc.dad.poshart.repository.CommentRepository;
import es.urjc.dad.poshart.repository.ShoppingCartRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.rest.ArtPostRestController;

//Servicio encargado de realizar peticiones asíncronas al servicio interno.
@Service
@EnableAsync
public class EmailService {
	
	@Value("${poshart.internal.uri}")
	private String uri;
	
	@Autowired
	MapperService mapper;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ArtPostRepository artPostRepository;

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	//Se enviará un Email de confimación cuando un usuario cree o
	//realice algún cambio en su usuario.
	@Async
	public void sendConfimationEmail(long id) {
		User u = userRepository.findById(id).orElseThrow();
		ObjectNode request = mapper.convertObjectToNode(u, JsonInterfaces.Basico.class);
		if(request!=null) restTemplate.postForLocation(uri + "/email/confirmUser", request);
	}
	
	//Se enviará un Email con el comentario realizado sobre una publicación.
	@Async
	public void sendNotifyCommentEmail(Comment comment) {
		ObjectNode request = mapper.convertObjectToNode(comment, JsonInterfaces.BasicoAvanzado.class);
		if(request!=null) restTemplate.postForLocation(uri + "/email/notifyComment", request);
	}
	
	//Se enviará un Email para avisar a un usuario de que su obra ha sido comprada.
	@Async
	public void sendNotifyPurchase(String email, long id) {
		ArtPost post = artPostRepository.findById(id).orElseThrow();
		ObjectNode request = mapper.convertObjectToNode(post, JsonInterfaces.BasicoAvanzado.class);
		//Para añadir el campo de correo necesario en la petición.
		request.put("email", email);
		restTemplate.postForLocation(uri + "/email/purchase", request);
	}
	
	//Se enviará un Email con el recibo de la compra realizada.
	public void sendPurchaseReceipt(long id) {
		ShoppingCart sc = shoppingCartRepository.getById(id);
		ObjectNode request = mapper.convertObjectToNode(sc, JsonInterfaces.BasicoAvanzado.class);
		sendPurchaseReceiptAsync(request);
	}
	
	@Async
	public void sendPurchaseReceiptAsync(ObjectNode request) {
		restTemplate.postForLocation(uri + "/email/receipt", request);
	}
}
