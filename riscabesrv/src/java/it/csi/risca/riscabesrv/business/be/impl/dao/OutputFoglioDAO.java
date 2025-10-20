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

import java.util.List;

import it.csi.risca.riscabesrv.dto.OutputFoglioDTO;

/**
 * The interface OutputFoglio dao.
 *
 * @author CSI PIEMONTE
 */
public interface OutputFoglioDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Load Output file by idOutputFile
	 *
	 * @param idOutputFile Long
	 * @return OutputFoglioDTO
	 * @throws Exception 
	 */
	List<OutputFoglioDTO> loadOutputFoglioByFile(Long idOutputFile) throws Exception;

}
