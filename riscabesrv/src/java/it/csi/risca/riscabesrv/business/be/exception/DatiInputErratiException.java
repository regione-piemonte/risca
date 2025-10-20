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

import it.csi.risca.riscabesrv.dto.ErrorObjectDTO;

public class DatiInputErratiException extends Exception {
	
	private static final long serialVersionUID = -4406252823837767071L;
	
	private ErrorObjectDTO error;
	protected Throwable throwable;

	public DatiInputErratiException(ErrorObjectDTO error) {
		super();
		this.setError(error);		
	}

	/**
	 * Method 'SystemException'
	 * 
	 * @param message
	 */
	public DatiInputErratiException(String message) {
		super(message);
	}

	/**
	 * Method 'SystemException'
	 * 
	 * @param message
	 * @param throwable
	 */
	public DatiInputErratiException(String message, Throwable throwable) {
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

	public ErrorObjectDTO getError() {
		return error;
	}

	public void setError(ErrorObjectDTO error) {
		this.error = error;
	}
}
