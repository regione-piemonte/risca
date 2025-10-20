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

import it.csi.risca.riscabesrv.business.be.exception.GenericException;
import it.csi.risca.riscabesrv.dto.ProvvedimentoDTO;

/**
 * The interface Provvedimenti Istanze DAO.
 *
 * @author CSI PIEMONTE
 */

public interface ProvvedimentiIstanzeDAO {


    /**
     * Load  provvedimenti istanza list.
     *
     * @return List<ProvvedimentoDTO> list
     * @throws SQLException 
     */
    List<ProvvedimentoDTO> getProvvedimentiIstanze() throws SQLException;
    
    /**
     * Load  Provvedimenti Istanze By idRiscossione .
     *
     * @return List<ProvvedimentoDTO> list
     * @throws SQLException 
     */
    List<ProvvedimentoDTO>  getProvvedimentiIstanzeByidRiscossione(Long idRiscossione) throws SQLException;
    /**
     * Load  Provvedimenti Istanze By Provvedimenti .
     *
     * @return ProvvedimentoDTO
     * @throws SQLException 
     */
	ProvvedimentoDTO getProvvedimentoIstanzaByIdProvvedimenti(Long idProvvedimentiIstanze) throws SQLException;
	  /**
     * save Provvedimenti Istanze.
     *
     * @return id ProvvedimentoDTO
	 * @throws Exception 
     */
	Long saveProvvedimentiIstanze(ProvvedimentoDTO provvedimentoDTO) throws Exception;
	  /**
     * update Provvedimenti Istanze.
     *
     * @return id ProvvedimentoDTO
	 * @throws Exception 
     */
	Long updateProvvedimentiIstanze(ProvvedimentoDTO provvedimentoDTO) throws Exception;
    /**
     * delete  Provvedimenti Istanze By id Provvedimenti .
     *
     * @return ProvvedimentoDTO
     * @throws SQLException 
     */
	ProvvedimentoDTO deleteProvvedimentiIstanze(Long idProvvedimentiIstanze) throws GenericException, SQLException;
	
    /**
     * delete  Provvedimenti Istanze By id Riscossione .
     *
     * @return idRiscossione
     * @throws Exception 
     */
	void deleteProvvedimentiIstanzeByIdRiscossione(Long idRiscossione) throws  Exception;
    
}
