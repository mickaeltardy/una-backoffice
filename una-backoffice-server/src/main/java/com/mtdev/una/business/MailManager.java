package com.mtdev.una.business;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;


@Component
public class MailManager {

	public boolean sendMail(String pRecipients, String pSubject, String pBody){
		final String lUsername = "support@una-club.fr";
		final String lPassword = "unanantes2.0";
 
		String lSender = "support@una-club.fr";
		Properties lProps = new Properties();
		lProps.put("mail.smtp.auth", "true");
		lProps.put("mail.smtp.starttls.enable", "true");
		lProps.put("mail.smtp.host", "auth.smtp.1and1.fr");
		lProps.put("mail.smtp.port", "587");
 
		Session lSession = Session.getInstance(lProps,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(lUsername, lPassword);
			}
		  });
 
		try {
 
			Message lMessage = new MimeMessage(lSession);
			lMessage.setFrom(new InternetAddress(lSender));
			
			lMessage.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(pRecipients));
			

			lMessage.addRecipients(Message.RecipientType.BCC,
				InternetAddress.parse(lSender));
			
			lMessage.setSubject(pSubject);
			lMessage.setText(pBody);
			
 
			Transport.send(lMessage);
 
			return true;
		} catch (MessagingException e) {
			
			return false;
		}
		
		
		
	}
	
}
