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

import it.csi.risca.riscabesrv.dto.TipiProvvedimentoExtendedDTO;

/**
 * The interface Tipo provvedimento dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipiProvvedimentoDAO {
	

    /**
     * Load tipi provvedimento list.
     *
     * @return List<TipiProvvedimentoExtendedDTO> list
     * @throws Exception 
     */
    List<TipiProvvedimentoExtendedDTO> loadTipiProvvedimento() throws Exception;
    /**
     * Load tipi provvedimento list by id ambito.
     *
     * @param idAmbito idAmbito
     * @param flgIstanza flgIstanza
     * @return List<TipiProvvedimentoExtendedDTO> list
     * @throws ParseException 
     */
    List<TipiProvvedimentoExtendedDTO> loadTipiProvvedimentoByIdAmbitoAndFlgIstanza(Long idAmbito, int flgIstanza, String dataIniVal,  String dataFineVal) throws ParseException;

    /**
     * Load tipo provvedimento by id tipo provvedimento and id ambito
     *
     * @param idTipoProvvedimento idTipoProvvedimento
     * @param idAmbito idAmbito
     * @return TipoProvvedimentoExtendedDTO
     * @throws SQLException 
     */
    TipiProvvedimentoExtendedDTO loadTipoProvvedimentoByIdTipiProvvedimentoAndIdAmbito(Long IdTipoProvvedimento, Long IdAmbito) throws SQLException;

    /**
     * Load tipo provvedimento by cod tipo provvedimento and id ambito
     *
     * @param codTipoProvvedimento codTipoProvvedimento
     * @param idAmbito idAmbito
     * @return TipoProvvedimentoExtendedDTO
     * @throws SQLException 
     */
    TipiProvvedimentoExtendedDTO loadTipoProvvedimentoByCodeAndIdAmbito(String codTipoProvvedimento, Long idAmbito) throws SQLException;


}
