package es.urjc.dad.poshart.internal_service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class MapperService {

	ObjectMapper mapper = new ObjectMapper();
	
	//Convierte un objeto de cualquier tipo en un ObjectNode reconocible como un objeto JSON.
	public ObjectNode convertObjectToNode(Object object, Class view) {
		try {
			String json = mapper.writerWithView(view).writeValueAsString(object);
			return mapper.readValue(json, ObjectNode.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//Convierte un conjunto de objetos de cualquier tipo en una lista de ObjectNodes reconocibles como objetos JSON.
	public List<ObjectNode> convertObjectsToNodes(Object[] objects, Class view) {
		List<ObjectNode> nodes = new ArrayList<ObjectNode>();
		try {
			for(Object o : objects) {
				String json = mapper.writerWithView(view).writeValueAsString(o);
				nodes.add(mapper.readValue(json, ObjectNode.class));
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodes;
	}
}
