/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.util.download;

/**
 * Eccezione sollevata dal download file
 * 
 */
public class DownloadException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1153839665703171505L;

	/**
	
	/**
	 * Inizializza un'istanza della classe ReportGeneratorException
	 */
	public DownloadException() {
	}

	/**
	 * Inizializza un'istanza della classe ReportGeneratorException
	 * 
	 * @param message Messaggio d'errore
	 */
	public DownloadException(String message) {
		super(message);
	}

	/**
	 * Inizializza un'istanza della classe ReportGeneratorException
	 * 
	 * @param cause Causa dell'errore
	 */
	public DownloadException(Throwable cause) {
		super(cause);
	}

	/**
	 * Inizializza un'istanza della classe ReportGeneratorException
	 * 
	 * @param message Messaggio d'errore
	 * @param cause Causa dell'errore
	 */
	public DownloadException(String message, Throwable cause) {
		super(message, cause);
	}
}
