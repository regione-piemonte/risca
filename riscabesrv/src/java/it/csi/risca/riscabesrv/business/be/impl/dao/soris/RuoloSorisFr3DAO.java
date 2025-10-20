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
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr3DTO;

/**
 * The interface RuoloSorisFr3 dao.
 *
 * @author CSI PIEMONTE
 */
public interface RuoloSorisFr3DAO extends BaseRiscaBeSrvDAO {

	public RuoloSorisFr3DTO saveRuoloSorisFr3(RuoloSorisFr3DTO dto) throws DAOException, DataAccessException, SQLException;


	public List<RuoloSorisFr3DTO> loadEstrazioneDatiSorisPerInsertPagamenti(Long idElabora, Long idFileSoris);


	public RuoloSorisFr3DTO updateRuoloSorisFr3(RuoloSorisFr3DTO dto) throws DAOException;
	
	
}
