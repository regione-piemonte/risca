/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.stardas;

/**
 * Eccezione rilanciata in caso di eccezione nell'invocazione dei servizi.
 * 
 * 
 *
 */
public class StardasException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static Integer esito = null;
	
	/**
	 * @see Exception#Exception()
	 */
	public StardasException() {
		super();
	}

	public StardasException(Integer esitoNew, String message, Throwable cause) {
		super(message, cause);
		esito = esitoNew;
	}

	public StardasException(Integer esitoNew, String message) {
		super(message);
		esito = esitoNew;
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public StardasException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public StardasException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public StardasException(Throwable cause) {
		super(cause);
	}

	
	public static Integer getEsito() {
		return esito;
	}

	public static void setEsito(Integer esitoNew) {
		esito = esitoNew;
	}
	
}
