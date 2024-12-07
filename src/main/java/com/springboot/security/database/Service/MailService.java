package com.springboot.security.database.Service;


import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.springboot.security.database.model.MailStructure;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromMail;
	

	
	public void sendMail(String mail, MailStructure mailStructure)
	{
		SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
		
		simpleMailMessage.setFrom(fromMail);
		
		simpleMailMessage.setSubject(mailStructure.getSubject());
		
		simpleMailMessage.setText(mailStructure.getMessage());
		
		simpleMailMessage.setTo(mail);
		
		mailSender.send(simpleMailMessage);
		
	}
	
	 public static String generateOTP() {
	        Random random = new Random();
	        int otp = random.nextInt(1000000);
	        return String.format("%06d", otp);
	    }

}