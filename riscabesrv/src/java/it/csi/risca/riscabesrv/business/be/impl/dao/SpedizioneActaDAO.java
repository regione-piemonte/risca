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

import it.csi.risca.riscabesrv.dto.SpedizioneActaDTO;

/**
 * The interface SpedizioneActa dao.
 *
 * @author CSI PIEMONTE
 */
public interface SpedizioneActaDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save spedizioneActa.
	 *
	 * @param dto SpedizioneActaDTO
	 * @return SpedizioneActaDTO
	 */
	SpedizioneActaDTO saveSpedizioneActa(SpedizioneActaDTO dto);

	SpedizioneActaDTO updateSpedizioneActa(SpedizioneActaDTO dto);

	List<SpedizioneActaDTO> loadSpedizioniActaDaArchiviare();

	SpedizioneActaDTO loadSpedizioneActaById(Long idSpedizioneActa);

}
