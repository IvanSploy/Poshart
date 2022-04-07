package es.urjc.dad.PoshartInternal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String toEmail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("poshartco@gmail.com");
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		
		try {
			mailSender.send(message);
			System.out.print("Mail Sent successfully...");
		}catch (Exception e) {
			// TODO: handle exception
			System.out.print("Email not Sent successfully...");
		}
	}
}