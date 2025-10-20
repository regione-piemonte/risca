/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;

/**
 * The interface Ping dao.
 *
 * @author CSI PIEMONTE
 */
public interface PingDAO {

    /**
     * Ping db string.
     *
     * @return OK /KO
     * @throws DAOException DAOException
     */
    String pingDB() throws DAOException;

}