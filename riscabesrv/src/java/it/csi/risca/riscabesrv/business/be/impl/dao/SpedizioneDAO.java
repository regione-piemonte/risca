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

import java.sql.SQLException;
import java.util.List;

import it.csi.risca.riscabesrv.dto.SpedizioneDTO;

/**
 * The interface Spedizione dao.
 *
 * @author CSI PIEMONTE
 */
public interface SpedizioneDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Load spedizione by idSpedizione
	 *
	 * @param idSpedizione idSpedizione
	 * @return SpedizioneDTO list
	 * @throws Exception 
	 */
	SpedizioneDTO loadSpedizioneByPk(String idSpedizione, boolean isWorking) throws  Exception;

	/**
	 * Save spedizione.
	 *
	 * @param dto SpedizioneDTO
	 * @return SpedizioneDTO
	 * @throws Exception 
	 */
	SpedizioneDTO saveSpedizione(SpedizioneDTO dto, boolean isWorking) throws  Exception;

	/**
	 * Update spedizione.
	 *
	 * @param dto SpedizioneDTO
	 * @return SpedizioneDTO
	 */
	SpedizioneDTO updateSpedizione(SpedizioneDTO dto, boolean isWorking) throws Exception;

	List<SpedizioneDTO> loadSpedizioneByElaborazione(String idAmbito, String idElabora, String codTipoSpedizione,
			boolean isWorking) throws  Exception;
	
	Integer deleteSpedizioneByPk(Long idSpedizione, boolean isWorking) throws Exception;
	
	Integer copySpedizioneFromWorking(Long idSpedizione, String attore) throws Exception;
}
