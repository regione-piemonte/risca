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

import it.csi.risca.riscabesrv.dto.ParametroElaboraDTO;

/**
 * The interface ParametroElabora dao.
 *
 * @author CSI PIEMONTE
 */
public interface ParametroElaboraDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Load elaboration requests by idElabora, raggruppamento.
	 *
	 * @param idElabora idElabora
	 * @param raggruppamento raggruppamento
	 * @return List<ParametroElaboraDTO> list
	 */
	List<ParametroElaboraDTO> loadParametroElaboraByElaboraRaggruppamento(String idElabora, String raggruppamento);

	/**
	 * Save parametro elabora.
	 *
	 * @param dto ParametroElaboraDTO
	 * @return ParametroElaboraDTO
	 * @throws Exception 
	 */
	ParametroElaboraDTO saveParametroElabora(ParametroElaboraDTO dto) throws Exception;
	
	/**
	 * Update parametro elabora.
	 *
	 * @param dto ParametroElaboraDTO
	 * @return ParametroElaboraDTO
	 */
	ParametroElaboraDTO updateParametroElabora(ParametroElaboraDTO dto) throws Exception;
}
