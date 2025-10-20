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

import it.csi.risca.riscabesrv.dto.TipoUsoDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;

/**
 * The interface Tipo uso dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipoUsoDAO {
	

    /**
     * Load tipo uso list.
     *
     * @return List<TipoUsoExtendedDTO> list
     */
    List<TipoUsoExtendedDTO> loadTipoUso();

    /**
     * Load tipo uso list by id ambito.
     *
     * @param idAmbito idAmbito
     * @return List<TipoUsoExtendedDTO> list
     * @throws Exception 
     */
    List<TipoUsoExtendedDTO> loadTipoUsoByIdAmbito(Long idAmbito, String dataIniVal,  String dataFineVal) throws Exception;

    /**
     * Load tipo uso by id tipo uso padre
     *
     * @param idTipoUsoPadre idTipoUsoPadre
     * @return List<TipoUsoExtendedDTO> list
     */
    List<TipoUsoExtendedDTO> loadTipoUsoByIdTipoUsoPadre(String idTipoUsoPadre);
    
    /**
     * Load tipo uso by id tipo uso
     *
     * @param idTipoUso idTipoUso
     * @return TipoUsoExtendedDTO
     * @throws SQLException 
     */
    TipoUsoExtendedDTO loadTipoUsoByIdTipoUsoOrCodTipoUso(String idTipoUso) throws SQLException;

    /**
     * Load tipo uso by cod tipo uso and id ambito
     *
     * @param codTipoUso codTipoUso
     * @param idAmbito idAmbito
     * @return TipoUsoExtendedDTO
     * @throws SQLException 
     */
    TipoUsoExtendedDTO loadTipoUsoByCodeAndIdAmbito(String codTipoUso, Long idAmbito) throws SQLException;
    
    /**
     * Load tipo uso by id tipo uso padre and id ambito
     *
     * @param idTipoUsoPadre idTipoUsoPadre
     * @param idAmbito idAmbito
     * @return List<TipoUsoExtendedDTO> list
     * @throws Exception 
     */
    List<TipoUsoExtendedDTO> loadTipoUsoByIdTipoUsoPadreAndIdAmbito(String idTipoUsoPadre, Long idAmbito ,String dataIniVal, String dataFineVal) throws Exception;

    /**
     * Load tipo uso by id riscossione
     *
     * @param idRiscossione idRiscossione
     * @return List<TipoUsoDTO> list
     * @throws Exception 
     */
    List<TipoUsoDTO> loadTipoUsoByRiscossione(Long idRiscossione) throws Exception;


}
