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
import java.util.List;

import org.springframework.dao.DataAccessException;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.BaseRiscaBeSrvDAO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr7DTO;

/**
 * The interface SorisFr7 dao.
 *
 * @author CSI PIEMONTE
 */
public interface SorisFr7DAO extends BaseRiscaBeSrvDAO {

	public SorisFr7DTO saveSorisFr7(SorisFr7DTO dto) throws DAOException, DataAccessException, SQLException;

	List<SorisFr7DTO> loadSorisFr7() throws DAOException, DataAccessException, SQLException;
	
	public Integer delete()throws DAOException;
	
}
