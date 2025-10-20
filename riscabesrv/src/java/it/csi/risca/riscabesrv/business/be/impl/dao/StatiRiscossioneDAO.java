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

import it.csi.risca.riscabesrv.dto.StatiRiscossioneExtendedDTO;

/**
 * The interface Stati riscossione dao.
 *
 * @author CSI PIEMONTE
 */
public interface StatiRiscossioneDAO {

    /**
     * Load tipi riscossione list.
     *
     * @return List<TipoRiscossioneExtendedDTO> list
     * @throws Exception 
     */
    List<StatiRiscossioneExtendedDTO> loadStatiRiscossione() throws Exception;

    /**
     * Load tipi riscossione list by id ambito.
     *
     * @param idAmbito idAmbito
     * @return List<TipoRiscossioneExtendedDTO> list
     * @throws Exception 
     */
    List<StatiRiscossioneExtendedDTO> loadStatiRiscossioneByIdAmbito(Long idAmbito) throws Exception;
}
