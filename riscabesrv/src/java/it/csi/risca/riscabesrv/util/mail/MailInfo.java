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

import java.io.Serializable;
import java.util.Arrays;

public class MailInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	protected long tipoMail;
	protected String mittente;
	protected String oggetto;
	protected String testo;
	protected String destinatario;
	protected String host;
	protected String port;
	protected String protocollo;
	protected String username;
	protected String password;

	protected AttachmentData[] attachments;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

	/**
	 * Method 'GericaTMailInfo'
	 * 
	 */
	public MailInfo() {
	}

	/**
	 * Method 'getTipoMail'
	 * 
	 * @return long
	 */
	public long getTipoMail() {
		return tipoMail;
	}

	/**
	 * Method 'setTipoMail'
	 * 
	 * @param tipoMail
	 */
	public void setTipoMail(long tipoMail) {
		this.tipoMail = tipoMail;
	}

	/**
	 * Method 'getMittente'
	 * 
	 * @return String
	 */
	public String getMittente() {
		return mittente;
	}

	/**
	 * Method 'setMittente'
	 * 
	 * @param mittente
	 */
	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	/**
	 * Method 'getOggetto'
	 * 
	 * @return String
	 */
	public String getOggetto() {
		return oggetto;
	}

	/**
	 * Method 'setOggetto'
	 * 
	 * @param oggetto
	 */
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	/**
	 * Method 'getTesto'
	 * 
	 * @return String
	 */
	public String getTesto() {
		return testo;
	}

	/**
	 * Method 'setTesto'
	 * 
	 * @param testo
	 */
	public void setTesto(String testo) {
		this.testo = testo;
	}

	/**
	 * Method 'getDestinatario'
	 * 
	 * @return String
	 */
	public String getDestinatario() {
		return destinatario;
	}

	/**
	 * Method 'setDestinatario'
	 * 
	 * @param destinatario
	 */
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public AttachmentData[] getAttachments() {
		return attachments;
	}

	public void setAttachments(AttachmentData[] attachments) {
		this.attachments = attachments;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "MailInfo [tipoMail=" + tipoMail + ", mittente=" + mittente + ", oggetto=" + oggetto + ", testo=" + testo
				+ ", destinatario=" + destinatario + ", host=" + host + ", port=" + port + ", protocollo=" + protocollo
				+ ", username=" + username + ", password=" + password + ", attachments=" + Arrays.toString(attachments)
				+ "]";
	}

}
