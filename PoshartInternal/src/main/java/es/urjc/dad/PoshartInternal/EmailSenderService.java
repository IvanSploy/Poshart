package es.urjc.dad.PoshartInternal;

import java.net.PasswordAuthentication;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.server.RSocketServer.Transport;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String toEmail, String subject2, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("poshartco@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject2);
		
		mailSender.send(message);
		System.out.print("Mail Sent successfully...");
		
		
	}
}