/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.exception;

public class SystemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6937700169485850637L;

	
	protected Throwable throwable;

	/**
	 * Method 'SystemException'
	 * 
	 * @param message
	 */
	public SystemException(String message) {
		super(message);
	}

	/**
	 * Method 'SystemException'
	 * 
	 * @param message
	 * @param throwable
	 */
	public SystemException(String message, Throwable throwable) {
		super(message);
		this.throwable = throwable;
	}

	/**
	 * Method 'getCause'
	 * 
	 * @return Throwable
	 */
	public Throwable getCause() {
		return throwable;
	}
	
}
