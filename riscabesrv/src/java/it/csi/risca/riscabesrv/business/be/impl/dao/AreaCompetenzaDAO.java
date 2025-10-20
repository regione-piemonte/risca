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

import it.csi.risca.riscabesrv.dto.AreaCompetenzaExtendedDTO;

/**
 * The interface AreaCompetenza dao.
 *
 * @author CSI PIEMONTE
 */
public interface AreaCompetenzaDAO extends BaseRiscaBeSrvDAO {



    /**
     * getAreaCompetenzaByCod.
     * 
  	 * @param idAmbito
	 * @param codTipoAreaCompetenza
	 * 
     * @return AreaCompetenzaExtendedDTO 
     * @throws Exception 
     */
	AreaCompetenzaExtendedDTO getAreaCompetenzaByCod(Long idAmbito, String codTipoAreaCompetenza) throws Exception;
}
