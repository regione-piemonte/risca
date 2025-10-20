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

import it.csi.risca.riscabesrv.dto.TipoRiscossioneExtendedDTO;

/**
 * The interface Tipo riscossione dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipiRiscossioneDAO {
	

    /**
     * Load tipi riscossione list.
     *
     * @return List<TipoRiscossioneExtendedDTO> list
     * @throws Exception 
     */
    List<TipoRiscossioneExtendedDTO> loadTipiRiscossione() throws Exception;

    /**
     * Load tipi riscossione list by id ambito.
     *
     * @param idAmbito idAmbito
     * @return List<TipoRiscossioneExtendedDTO> list
     * @throws ParseException 
     */
    List<TipoRiscossioneExtendedDTO> loadTipiRiscossioneByIdAmbitoAndDateValidita(Long idAmbito, String dataIniVal, String dataFineVal) throws ParseException ;

    /**
     * Load tipo riscossione by id tipo riscossione and id ambito
     *
     * @param idTipoRiscossione idTipoRiscossione
     * @param idAmbito idAmbito
     * @return TipoRiscossioneExtendedDTO
     * @throws SQLException 
     */
    TipoRiscossioneExtendedDTO loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(String idOrCodTipoRiscossione, Long IdAmbito) throws SQLException;

    /**
     * Load tipo riscossione by cod tipo riscossione and id ambito
     *
     * @param codTipoRiscossione codTipoRiscossione
     * @param idAmbito idAmbito
     * @return TipoRiscossioneExtendedDTO
     * @throws SQLException 
     */
    TipoRiscossioneExtendedDTO loadTipoRiscossioneByCodeAndIdAmbito(String codTipoRiscossione, Long idAmbito) throws SQLException;


}
