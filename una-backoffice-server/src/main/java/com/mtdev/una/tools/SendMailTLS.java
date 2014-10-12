package com.mtdev.una.tools;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS {

	public static void main(String[] args) {

		final String username = "support@una-club.fr";
		final String password = "unanantes2.0";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "auth.smtp.1and1.fr");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		List<String> lMails = new ArrayList<String>();
		lMails.add("mishgunn@gmail.com");
		/*
		lMails.add("sarah.khaddage@gmail.com");
		lMails.add("lam.doan@laposte.net");
		lMails.add("tim.briand@icloud.com");
		lMails.add("joel.mounes@gmail.com");
		lMails.add("ksehaqui@hotmail.com");
		lMails.add("alain.quidu0652@orange.fr");
		lMails.add("shirinekhaksar@gmail.com");
		lMails.add("anne.baumstimler@wanadoo.fr");
		lMails.add("duretete16@up.edu");
		lMails.add("pierregillier@gmail.com");
		lMails.add("vdo.bourez@wanadoo.fr");
		lMails.add("alain.bourhis80@orange.fr");
		lMails.add("philippe.sauvetre@free.fr");
		lMails.add("clothilde.pitel@gmail.com");
		lMails.add("colzasalome@gmail.com");
		lMails.add("Louise.gury@hotmail.fr");
		lMails.add("sherlock-marston@hotmail.fr");
		lMails.add("jacinthe.malthieu@gmail.com");
		lMails.add("tousalamer44@orange.fr");
		lMails.add("david.theriot@free.fr");
		lMails.add("antoine-tuf@hotmail.fr");
		lMails.add("chloemolteni@yahoo.fr");
		lMails.add("togagnebet@gmail.com");
		lMails.add("catherine.rolland17@gmail.com");
		lMails.add("kawtar.touijer@hotmail.fr");
		lMails.add("elise.mouchard@laposte.net");
		lMails.add("florane.vuillaume@gmail.com");
		lMails.add("gonet.francois.44@gmail.com");
		lMails.add("corentin.herin01@gmail.com");
		lMails.add("rem2390@hotmail.fr");
		lMails.add("alex.gobin@yahoo.fr");
		lMails.add("cedricpageau@yahoo.fr");
		lMails.add("vraibenjamin.soule@gmail.com");
		lMails.add("sophie-drenou@outlook.fr");
		lMails.add("baglinemmanuelle@gmail.com");
		lMails.add("bordieralex@hotmail.fr");
		lMails.add("pierrecouedel@orange.fr");
		lMails.add("clairetterob@yahoo.fr");
		lMails.add("louis.thomas93@gmail.com");
		lMails.add("dedoyard.guilhem@hotmail.fr");
		lMails.add("marinono25@gmail.com");
		lMails.add("sebcestfun@gmx.fr");
		lMails.add("alex.gobin@orange.fr");
		lMails.add("sebiii.86@hotmail.fr");
		lMails.add("julien.paitel@gmx.fr");
		lMails.add("phgorioux@gmail.com");
		lMails.add("acboat@gmail.com");
		lMails.add("gilles.lazuech@univ-nantes.fr");
		lMails.add("maximetalvast@msn.com");
		lMails.add("slimcr@hotmail.com");
		lMails.add("angeliquerenaud7@gmail.com");
		lMails.add("b_mandart@aol.fr");
		lMails.add("freyskeyd@gmail.com");
		lMails.add("clement.legal@hotmail.fr");
		lMails.add("elise.mouchard@univ-nantes.fr");
*/
		for (String lMail : lMails) {
			try {

				Message message = new MimeMessage(session);
				
					message.setFrom(new InternetAddress ("support@una-club.fr", "Eauipe de support UNA"));
			
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(lMail));
				message.setSubject("[UNA] Ouverture des inscriptions");
				message.setText("Bonjour,"
						+ "\n\nSuite à votre demande, nous vous informons que "
						+ "les inscriptions pour la saison 2014/2015 sont ouvertes "
						+ "depuis quelque temps. Vous pouvez vous inscrire en commençant "
						+ "par le formulaire d'inscription sur le site http://una-club.fr "
						+ "ou passer directement au club muni de toutes les pièces nécessaires "
						+ "\n- Certificat médical \n- Photo d'identité \n- Règlement en chèque ou espèces \n- Brevet de natation de 50 m pour les -18 ans \n- Autorisation parentale pour les -18 ans ou formulaire d'inscription signé \n\nA bientôt, \nUNA");

				Transport.send(message);

				System.out.println("Done");

				Thread.sleep(3000);

				System.out.println("Stop sleeping");

			} catch (MessagingException e) {
				throw new RuntimeException(e);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}