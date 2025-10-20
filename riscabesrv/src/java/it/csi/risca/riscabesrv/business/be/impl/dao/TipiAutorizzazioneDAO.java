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
import java.text.ParseException;
import java.util.List;

import it.csi.risca.riscabesrv.dto.TipiAutorizzazioneExtendedDTO;

/**
 * The interface Tipo autorizzazione dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipiAutorizzazioneDAO {
	

    /**
     * Load tipi autorizzazione list.
     *
     * @return List<TipiAutorizzazioneExtendedDTO> list
     * @throws Exception 
     */
    List<TipiAutorizzazioneExtendedDTO> loadTipiAutorizzazione() throws Exception;
    /**
     * Load tipi autorizzazione list by id ambito.
     *
     * @param idAmbito idAmbito
     * @return List<TipiAutorizzazioneExtendedDTO> list
     * @throws ParseException 
     */
    List<TipiAutorizzazioneExtendedDTO> loadTipiAutorizzazioneByIdAmbito(Long idAmbito,String dataIniVal,  String dataFineVal) throws ParseException;

    /**
     * Load tipo autorizzazione by id tipo autorizzazione and id ambito
     *
     * @param idTipoAutorizzazione idTipoAutorizzazione
     * @param idAmbito idAmbito
     * @return TipoAutorizzazioneExtendedDTO
     * @throws SQLException 
     */
    TipiAutorizzazioneExtendedDTO loadTipoAutorizzazioneByIdTipiAutorizzazioneAndIdAmbito(Long IdTipoAutorizzazione, Long IdAmbito) throws SQLException;

    /**
     * Load tipo autorizzazione by cod tipo autorizzazione and id ambito
     *
     * @param codTipoAutorizzazione codTipoAutorizzazione
     * @param idAmbito idAmbito
     * @return TipoAutorizzazioneExtendedDTO
     * @throws SQLException 
     */
    TipiAutorizzazioneExtendedDTO loadTipoAutorizzazioneByCodeAndIdAmbito(String codTipoAutorizzazione, Long idAmbito) throws SQLException;


}
