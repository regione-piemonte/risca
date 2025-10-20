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

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.BaseRiscaBeSrvDAO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr1DTO;

/**
 * The interface RuoloSorisFr1 dao.
 *
 * @author CSI PIEMONTE
 */
public interface RuoloSorisFr1DAO extends BaseRiscaBeSrvDAO {

	public RuoloSorisFr1DTO saveRuoloSorisFr1(RuoloSorisFr1DTO dto) throws DAOException, DataAccessException, SQLException;

	Boolean isSumImportEquals(Long idFileSoris, Long idRuolo) throws Exception, BusinessException;

	List<RuoloSorisFr1DTO> loadConfrontoImporti(Long idFileSoris) throws Exception, BusinessException;


	
}
