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
import java.util.Map;

import it.csi.risca.riscabesrv.dto.ConfigurazioneDTO;


/**
 * The interface Configurazione dao.
 *
 * @author CSI PIEMONTE
 */
public interface ConfigurazioneDAO {


    /**
     * Load config list.
     *
     * @return List<ConfigurazioneDTO> list
     */
    List<ConfigurazioneDTO> loadConfig();

    /**
     * Load config by key list.
     *
     * @param key chiave
     * @return List<ConfigurazioneDTO> list
     */
    List<ConfigurazioneDTO> loadConfigByKey(String key);

    /**
     * Load config by key list map.
     *
     * @param keys lista di chiavi
     * @return Map<String, String> map
     */
    Map<String, String> loadConfigByKeyList(List<String> keys);
    
    /**
     * insert new config .
     *
     * @param configurazione DTO
     * @return configurazione DTO
     */
    ConfigurazioneDTO insertConfigurazioneDTO(ConfigurazioneDTO configurazioneDTO);
    
    
    /**
     * update new config .
     *
     * @param configurazione DTO
     * @return configurazione DTO
     */
    ConfigurazioneDTO updatetConfigurazioneDTO(ConfigurazioneDTO configurazioneDTO);
    
}
