package es.urjc.dad.poshart.internalService;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.urjc.dad.poshart.model.ArtPost;
import es.urjc.dad.poshart.model.JsonInterfaces;
import es.urjc.dad.poshart.model.ShoppingCart;
import es.urjc.dad.poshart.model.User;
import es.urjc.dad.poshart.repository.ArtPostRepository;
import es.urjc.dad.poshart.repository.ShoppingCartRepository;
import es.urjc.dad.poshart.repository.UserRepository;
import es.urjc.dad.poshart.rest.ArtPostRestController;

//Servicio encargado de realizar peticiones asíncronas al servicio interno.
@Service
public class DownloadService {
	
	@Autowired
	MapperService mapper;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ShoppingCartRepository shoppingCartRepository;
	
    public void downloadPdf(ShoppingCart s){
    	ObjectNode request = mapper.convertObjectToNode(s, JsonInterfaces.Avanzado.class);
        String url = "http://localhost:8080/download/receipt/";
        restTemplate.postForObject(url, request, ByteArrayResource.class);
    }
}
