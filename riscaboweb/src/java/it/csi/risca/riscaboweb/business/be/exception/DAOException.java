/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.exception;

public class DAOException extends GenericException {

    private static final long serialVersionUID = 1L;

    public DAOException(String msg) {
        super(msg);
    }

    public DAOException(Throwable arg0) {
        super(arg0);

    }

}