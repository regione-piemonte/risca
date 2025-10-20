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

import it.csi.risca.riscabesrv.dto.StatoElaborazioneDTO;

/**
 * The interface Stati Elaborazione DAO.
 *
 * @author CSI PIEMONTE
 */
public interface StatiElaborazioneDAO {

    /**
     * Load Stati Elaborazione.
     * 
     * @param idAmbito idAmbito
     * @param idFunzionalita idFunzionalita
     * 
     * @return List<StatoElaborazioneDTO>
     */
	List<StatoElaborazioneDTO> loadStatiElaborazioneByIdAmbitoAndIdFunzionalita(Long idAmbito, Long idFunzionalita);
}
