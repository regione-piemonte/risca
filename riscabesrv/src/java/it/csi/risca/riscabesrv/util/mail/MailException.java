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

/**
 * Eccezione sollevata dalla generazione dei report
 * 
 * @author Marco Giacometto
 */
public class MailException extends Exception {
	/**
	 * Identificativo univoco della classe
	 */
	private static final long serialVersionUID = 973817983851404292L;

	/**
	 * Inizializza un'istanza della classe ReportGeneratorException
	 */
	public MailException() {
	}

	/**
	 * Inizializza un'istanza della classe ReportGeneratorException
	 * 
	 * @param message Messaggio d'errore
	 */
	public MailException(String message) {
		super(message);
	}

	/**
	 * Inizializza un'istanza della classe ReportGeneratorException
	 * 
	 * @param cause Causa dell'errore
	 */
	public MailException(Throwable cause) {
		super(cause);
	}

	/**
	 * Inizializza un'istanza della classe ReportGeneratorException
	 * 
	 * @param message Messaggio d'errore
	 * @param cause Causa dell'errore
	 */
	public MailException(String message, Throwable cause) {
		super(message, cause);
	}
}
