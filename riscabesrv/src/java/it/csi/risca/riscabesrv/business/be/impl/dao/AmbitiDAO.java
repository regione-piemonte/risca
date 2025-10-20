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

import it.csi.risca.riscabesrv.dto.AmbitoDTO;

/**
 * The interface Ambiti dao.
 *
 * @author CSI PIEMONTE
 */
public interface AmbitiDAO extends BaseRiscaBeSrvDAO{
	
    /**
     * Load ambiti list.
     *
     * @return List<AmbitoDTO> list
     * @throws Exception 
     */
    List<AmbitoDTO> loadAmbiti() throws Exception;
    
    /**
     * Load ambito by id Ambito.
     *
     * @return AmbitoDTO 
     * @throws Exception 
     */
    AmbitoDTO loadAmbitoByIdAmbito(Long idAmbito) ;
    /**
     * loadAmbitiByCodTipoElabora.
     *
     * @return List<AmbitoDTO> list 
     * @throws Exception 
     */
    List<AmbitoDTO> loadAmbitiByCodTipoElabora(String codTipoElabora) throws Exception;
}
