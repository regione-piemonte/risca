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

import it.csi.risca.riscabesrv.dto.StatoContribuzioneDTO;

/**
 * The interface Stato Contribuzione DAO.
 *
 * @author CSI PIEMONTE
 */
public interface StatoContribuzioneDAO {	

    /**
     * Load StatoContribuzione
     *
     * @return List<StatoContribuzioneDTO> 
     */
	List<StatoContribuzioneDTO> loadStatoContribuzione() throws Exception;

    /**
     * Load StatoContribuzione by id StatoContribuzioneDTO.
     *
     * @return StatoContribuzioneDTO 
     * @throws Exception 
     */
	StatoContribuzioneDTO loadStatoContribuzioneById(Long idStatoContribuzione) throws Exception;
	
	
}
