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
import it.csi.risca.riscabesrv.dto.RuoloDTO;

/**
 * The interface Ruolo DAO
 *
 * @author CSI PIEMONTE
 */
public interface RuoloDAO extends BaseRiscaBeSrvDAO {

	

	/**
	 * Inserisce nella tabella RISCA_R_RUOLO,
	 *
	 * @return RuoloDTO
	 * @throws Exception 
	 */
	RuoloDTO saveRuolo(RuoloDTO ruoloDTO, Long idAmbito) throws DAOException, Exception;

	
}
