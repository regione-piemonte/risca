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

import java.util.List;

import it.csi.risca.riscabesrv.dto.ErrorDTO;

/**
 * The type Generic exception.
 *
 * @author CSI PIEMONTE
 */
public class GenericExceptionList extends Exception{
    private static final long serialVersionUID = 1L;

    private List<ErrorDTO> errors;

//	/**
//     * Gets error.
//     *
//     * @return the error
//     */
//    public ErrorDTO getError() {
//        return error;
//    }
//
//    /**
//     * Sets error.
//     *
//     * @param error the error
//     */
//    public void setError(ErrorDTO error) {
//        this.error = error;
//    }

    public List<ErrorDTO> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorDTO> errors) {
		this.errors = errors;
	}

	/**
     * Instantiates a new Generic exception.
     *
     * @param msg error message
     */
    public GenericExceptionList(String msg) {
        super(msg);
    }

    /**
     * Instantiates a new Generic exception.
     *
     * @param errore ErrorDTO
     */
    public GenericExceptionList(List<ErrorDTO> errore) { this.setErrors(errore); }

    /**
     * Instantiates a new Generic exception.
     *
     * @param arg0 arg
     */
    public GenericExceptionList(Throwable arg0) { super(arg0); }


}