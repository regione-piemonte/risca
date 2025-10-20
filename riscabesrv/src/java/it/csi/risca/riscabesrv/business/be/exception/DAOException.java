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

/**
 * The type Dao exception.
 *
 * @author CSI PIEMONTE
 */
public class DAOException extends GenericException{

    /**
     * Instantiates a new Dao exception.
     *
     * @param msg the msg
     */
    public DAOException(String msg) {
        super(msg);
    }

    /**
     * Instantiates a new Dao exception.
     *
     * @param arg0 the arg 0
     */
    public DAOException(Throwable arg0) {
        super(arg0);

    }

}