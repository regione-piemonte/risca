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

import it.csi.risca.riscabesrv.dto.TipiTitoloExtendedDTO;

/**
 * The interface Tipo titolo dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipiTitoloDAO {
	

    /**
     * Load tipi titolo list.
     *
     * @return List<TipiTitoloExtendedDTO> list
     */
    List<TipiTitoloExtendedDTO> loadTipiTitolo();
    /**
     * Load tipi titolo list by id ambito.
     *
     * @param idAmbito idAmbito
     * @return List<TipiTitoloExtendedDTO> list
     * @throws ParseException 
     */
    List<TipiTitoloExtendedDTO> loadTipiTitoloByIdAmbito(Long idAmbito, String dataIniVal,  String dataFineVal) throws ParseException;

    /**
     * Load tipo titolo by id tipo titolo and id ambito
     *
     * @param idTipoTitolo idTipoTitolo
     * @param idAmbito idAmbito
     * @return TipoTitoloExtendedDTO
     * @throws SQLException 
     */
    TipiTitoloExtendedDTO loadTipoTitoloByIdTipiTitoloAndIdAmbito(Long IdTipoTitolo, Long IdAmbito) throws SQLException;

    /**
     * Load tipo titolo by cod tipo titolo and id ambito
     *
     * @param codTipoTitolo codTipoTitolo
     * @param idAmbito idAmbito
     * @return TipoTitoloExtendedDTO
     * @throws SQLException 
     */
    TipiTitoloExtendedDTO loadTipoTitoloByCodeAndIdAmbito(String codTipoTitolo, Long idAmbito) throws SQLException;


}
