package com.mtdev.una.business.interfaces;

public interface MailManager {

	public boolean sendMail(String pAddressee);
	
	public boolean sendMailWithAttachment(String pAddressee, String pAttachmentPath);
	
}
