package es.urjc.dad.PoshartInternal.service;

import java.net.PasswordAuthentication;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.aspose.cells.JsonLayoutOptions;
import com.aspose.cells.JsonUtility;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.Blob;

@Service
public class DownloadService {

	public ResponseEntity<ByteArrayResource> downloadPDF(String jsonInput) {
		
		// create a blank Workbook object
		Workbook workbook = new Workbook();
		// access default empty worksheet
		Worksheet worksheet = workbook.getWorksheets().get(0);

		// set JsonLayoutOptions for formatting
		JsonLayoutOptions layoutOptions = new JsonLayoutOptions();
		layoutOptions.setArrayAsTable(true);

		// import JSON data to default worksheet starting at cell A1
		JsonUtility.importData(jsonInput, worksheet.getCells(), 0, 0, layoutOptions);

		
		// save resultant file in JSON-TO-PDF format
		try {
			workbook.save("output.pdf", SaveFormat.PDF);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/*
@RequestMapping(value = "/files/list", method = RequestMethod.GET)
public String listFiles(Model model) {
	List<Path> lodf = new ArrayList<>();
	List<HRefModel> uris = new ArrayList<>();
	
	try {
		lodf = storageService.listSourceFiles(storageService.getUploadLocation());
		for(Path pt : lodf) {
			HRefModel href = new HRefModel();
			href.setHref(MvcUriComponentsBuilder
					.fromMethodName(UploadController.class, "serveFile", pt.getFileName().toString())
					.build()
					.toString());
			
			href.setHrefText(pt.getFileName().toString());
			uris.add(href);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	model.addAttribute("listOfEntries", uris);
	return "file_list :: urlFileList";
}

@GetMapping("/files/{filename:.+}")
@ResponseBody
public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
	Resource file = storageService.loadAsResource(filename);
	return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
			.body(file);
}

@RequestMapping(value = "/files/list", method = RequestMethod.GET)
public String listFiles(Model model) {
 List<Path> lodf = new ArrayList<>();
 List<HRefModel> uris = new ArrayList<>();
 
 try {
 lodf = storageService.listSourceFiles(storageService.getUploadLocation());
 for(Path pt : lodf) {
 HRefModel href = new HRefModel();
 href.setHref(MvcUriComponentsBuilder
 .fromMethodName(UploadController.class, "serveFile", pt.getFileName().toString())
 .build()
 .toString());
 
 href.setHrefText(pt.getFileName().toString());
 uris.add(href);
 }
 } catch (IOException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
 }
 model.addAttribute("listOfEntries", uris);
 return "file_list :: urlFileList";
}
 
@GetMapping("/files/{filename:.+}")
@ResponseBody
public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
 Resource file = storageService.loadAsResource(filename);
 return ResponseEntity.ok()
 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
 .body(file);
}
		 * */
	}
}