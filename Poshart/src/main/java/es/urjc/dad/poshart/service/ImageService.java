package es.urjc.dad.poshart.service;

import java.io.IOException;
import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.urjc.dad.poshart.model.Image;
import es.urjc.dad.poshart.repository.ImageRepository;

//Solo se deben emplear los métodos de este servicio cuando 
//se ha comprobado la existencia de un objeto.
//Además, después de usarse deberá ser guardado el 
//objeto en la correspondiente base de datos.
@Service
public class ImageService {

	Logger console = LoggerFactory.getLogger(ImageService.class);

	@Autowired
	private ImageRepository repository;

	public Image createImage(MultipartFile imageFile) throws IOException {

		Image image = new Image();

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

		image.setImage(location.toString());

		console.warn(imageFile.getOriginalFilename());

		String[] urlType = imageFile.getOriginalFilename().split("\\.");

		image.setImageType(urlType[urlType.length - 1]);

		Blob imageData = BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize());

		image.setImageFile(imageData);

		repository.save(image);

		return image;
	}

	public ResponseEntity<Object> createResponseFromImage(long id) throws SQLException {

		Image image = repository.findById(id).orElseThrow();
		if (image.getImageFile() != null) {
			Resource file = new InputStreamResource(image.getImageFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/" + image.getImageType())
					.contentLength(image.getImageFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public void deleteImage(long id) {
		Image image = repository.findById(id).orElseThrow();

		repository.delete(image);
	}

}