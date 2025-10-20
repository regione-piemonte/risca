/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.soris;


import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.BaseRiscaBeSrvDAO;
import it.csi.risca.riscabesrv.dto.soris.Soris99CDTO;

/**
 * The interface Soris99C dao.
 *
 * @author CSI PIEMONTE
 */
public interface Soris99CDAO extends BaseRiscaBeSrvDAO {

	public Soris99CDTO saveSoris99C(Soris99CDTO dto) throws DAOException, DataAccessException, SQLException;

	public Integer delete()throws DAOException;
}
