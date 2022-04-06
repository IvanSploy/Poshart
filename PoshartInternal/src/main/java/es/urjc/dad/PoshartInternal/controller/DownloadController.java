package es.urjc.dad.PoshartInternal.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;



import es.urjc.dad.PoshartInternal.service.DownloadService;

@RestController
@RequestMapping("/download")
public class DownloadController {
	
	@Autowired
	private DownloadService downloadService;
	
	@PostMapping("/receipt")
	public void downloadPDFResource(HttpServletRequest request, @RequestBody ObjectNode post) throws URISyntaxException {
		String dataDirectory = request.getServletContext().getRealPath("/WEB-INF/downloads/pdf/");
		String fileName="recibo.pdf";
		Path file = Paths.get(dataDirectory, fileName);
		downloadService.downloadPDF(post.asText(), file);
	}
}
