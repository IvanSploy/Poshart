package es.urjc.dad.poshart.internalService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.ArtPostRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.rest.ArtPostRestController;

//Servicio encargado de realizar peticiones asíncronas al servicio interno.
@Service
@EnableAsync
public class EmailService {
	
	@Autowired
	MapperService mapper;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ArtPostRepository artPostRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	//Se enviará un Email de confimación cuando un usuario cree o
	//realice algún cambio en su usuario.
	public void sendConfimationEmail(long id) {
		User u = userRepository.findById(id).orElseThrow();
		List<ArtPost> post = u.getMyPosts();
		List<ObjectNode> request = mapper.convertObjectsToNodes(post.toArray(), ArtPost.Basico.class);
		if(request!=null) restTemplate.postForLocation("http://localhost:8080/email/artposts", request);
	}
	
	//Se enviará un Email con los posts recomendados tras
	//seguir a un nuevo usuario.
	public void sendRecommendedPostEmail(String email, long id) {
		ArtPost post = artPostRepository.findById(id).orElseThrow();
		ObjectNode request = mapper.convertObjectToNode(post, ArtPost.Basico.class);
		request.put("email", email);
		if(request!=null) restTemplate.postForLocation("http://localhost:8080/email/artpost", request);
	}
	
	//Se enviará un Email con los posts recomendados tras
	//seguir a un nuevo usuario.
	public void sendRecommendedPostsEmail(long id) {
		User u = userRepository.findById(id).orElseThrow();
		List<ArtPost> post = u.getMyPosts();
		List<ObjectNode> request = mapper.convertObjectsToNodes(post.toArray(), ArtPost.Basico.class);
		
		//Para añadir el campo de correo necesario en la petición.
		request.get(0).put("email", u.getMail());
		if(request!=null) restTemplate.postForLocation("http://localhost:8080/email/artposts", request);
	}
}
