package es.urjc.dad.PoshartInternal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import es.urjc.dad.PoshartInternal.service.EmailSenderService;

@SpringBootApplication
public class PoshartInternalApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PoshartInternalApplication.class, args);
	}
}
