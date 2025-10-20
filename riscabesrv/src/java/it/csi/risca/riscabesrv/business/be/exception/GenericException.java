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

import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.ErrorObjectDTO;

/**
 * The type Generic exception.
 *
 * @author CSI PIEMONTE
 */
public class GenericException extends Exception{
    private static final long serialVersionUID = 1L;

    private ErrorDTO error;
    private ErrorObjectDTO erroObjectDTO;
    
    /**
     * Gets error.
     *
     * @return the error
     */
    public ErrorDTO getError() {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(ErrorDTO error) {
        this.error = error;
    }

    public ErrorObjectDTO getErroObjectDTO() {
		return erroObjectDTO;
	}

	public void setErroObjectDTO(ErrorObjectDTO erroObjectDTO) {
		this.erroObjectDTO = erroObjectDTO;
	}

	/**
     * Instantiates a new Generic exception.
     *
     * @param msg error message
     */
    public GenericException(String msg) {
        super(msg);
    }

    /**
     * Instantiates a new Generic exception.
     *
     * @param errore ErrorDTO
     */
    public GenericException(ErrorDTO errore) { this.setError(errore); }
    /**
     * Instantiates a new Generic exception.
     *
     * @param erroObjectDTO ErrorObjectDTO
     */
    public GenericException(ErrorObjectDTO erroObjectDTO) {
		super();
		this.erroObjectDTO = erroObjectDTO;
	}

	/**
     * Instantiates a new Generic exception.
     *
     * @param arg0 arg
     */
    public GenericException(Throwable arg0) { super(arg0); }

}