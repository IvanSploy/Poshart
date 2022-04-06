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
import java.nio.file.Path;
import java.sql.Blob;

@Service
public class DownloadService {

	public void downloadPDF(String jsonInput, Path dir) {
		
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
			workbook.save(dir.toString(), SaveFormat.PDF);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}