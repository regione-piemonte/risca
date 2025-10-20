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

import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;

/**
 * The interface AmbitiConfig dao.
 *
 * @author CSI PIEMONTE
 */
public interface AmbitiConfigDAO extends BaseRiscaBeSrvDAO {
	

    /**
     * Load ambiti config list.
     *
     * @return List<AmbitoConfigDTO> list
     */
    List<AmbitoConfigDTO> loadAmbitiConfig();

    /**
     * Load ambiti config list by id ambito.
     *
     * @param idAmbito idAmbito
     * @return List<AmbitoConfigDTO> list
     */
    List<AmbitoConfigDTO> loadAmbitiConfigByIdOrCodAmbito(String idAmbito) throws SQLException ;
    
    /**
     * Load ambiti config by cod ambito and chiave
     *
     * @param keyUnitaMisura keyUnitaMisura
     * @param idAmbito idAmbito
     * @return List<AmbitoConfigDTO> list
     */
    List<AmbitoConfigDTO> loadAmbitiConfigByCodeAndKey(String codAmbito, String chiave) throws SQLException ;






}
