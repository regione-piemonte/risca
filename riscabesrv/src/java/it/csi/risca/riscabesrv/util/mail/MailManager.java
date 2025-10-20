/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.util.mail;

import java.util.Properties;

//import javax.activation.DataHandler;
import javax.activation.DataSource;
//import javax.mail.Multipart;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.Logger;
import org.apache.soap.util.mime.ByteArrayDataSource;

public class MailManager {

	private static final Logger log = Logger.getLogger("risca.mail");

	private String mailHost;
	private String mailPort;

	public MailManager(String mailHost, String mailPort) {
		this.mailHost = mailHost;
		this.mailPort = mailPort;
	}

	public String getMailHost() {
		return mailHost;
	}

	public String getMailPort() {
		return mailPort;
	}

	/* public void sendMail(MailInfo mailInfo) throws MailException {

		java.util.Properties props = new java.util.Properties();
		props.put("mail.smtp.host", mailInfo.getHost());
		props.put("mail.smtp.port", Integer.valueOf(mailInfo.getPort()));
		Session session = Session.getDefaultInstance(props, null);
		// Construct the message
		javax.mail.Message message = new MimeMessage(session);

		try {
			InternetAddress[] recipients = new InternetAddress[1];
			recipients[0] = new InternetAddress(mailInfo.getDestinatario());
			message.setFrom(new InternetAddress(mailInfo.getMittente()));
			message.setRecipients(javax.mail.Message.RecipientType.TO, recipients);
			message.setSubject(mailInfo.getOggetto());

			Multipart multipart = new MimeMultipart("mixed");

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(mailInfo.getTesto());
			multipart.addBodyPart(messageBodyPart);

			for (AttachmentData attachment : mailInfo.getAttachments()) {
				MimeBodyPart attachBodyPart = new MimeBodyPart();
				DataSource source = new ByteArrayDataSource(attachment.getData(), attachment.getMimeType());
				attachBodyPart.setDataHandler(new DataHandler(source));
				attachBodyPart.setFileName(attachment.getFilename());
				multipart.addBodyPart(attachBodyPart);
			}

			message.setContent(multipart);

			// Send the message
			Transport.send(message);

			log.debug("MailManager - mailInvio - MAIL SENT");
		} catch (Exception e) {
			log.error("MailManager - mailInvio: " + e.getMessage());
			throw new MailException(e);
		}
	}*/

	public void sendMail(MailInfo mailInfo) throws MailException {
		// Prepare system properties
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.ssl.enable", "true");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.ssl.socketFactory.fallback", "false");

		try {
			MultiPartEmail email = new MultiPartEmail();
			email.setDebug(true);

			email.setHostName(mailInfo.getHost());
			email.setSmtpPort(Integer.valueOf(mailInfo.getPort()));
			email.setAuthentication(mailInfo.getUsername(), mailInfo.getPassword());
			email.setSSL(true);

			email.setFrom(mailInfo.getMittente());
			email.setMsg(mailInfo.getTesto());
			email.setSubject(mailInfo.getOggetto());
			email.addTo(mailInfo.getDestinatario(), "");
             if(mailInfo.getAttachments() != null) {
				for (AttachmentData attachment : (mailInfo.getAttachments())) {
					DataSource source = new ByteArrayDataSource(attachment.getData(), attachment.getMimeType());
					email.attach(source, attachment.getFilename(), "");
				}
             }
			email.send();
			log.debug("MailManager - sendMail - MAIL SENT");
		} catch (Exception e) {
			log.error("MailManager - sendMail - ERROR: " + e.getMessage());
			throw new MailException(e);
		}
	}
	
	

}
