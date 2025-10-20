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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.GruppiSoggettoDTO;

/**
 * The interface SoggettoGruppoDAO.
 *
 * @author CSI PIEMONTE
 */
public interface SoggettoGruppoDAO {

    /**
     * loadIdSoggettoGruppoByIdSoggetto.
     *
     * @param  idSoggetto
     * @return List<Long> 
     * @throws Exception 
     */
	List<Long> loadIdSoggettoGruppoByIdSoggetto(Long idSoggetto) throws Exception;
    /**
     * loadSoggettoGruppoByIdGruppoSoggetto.
     *
     * @param  idGruppoSoggetto
     * @return List<GruppiSoggettoDTO> 
     * @throws Exception 
     */
	List<GruppiSoggettoDTO> loadSoggettoGruppoByIdGruppoSoggetto(Long idGruppoSoggetto) throws Exception;
	
    /**
     * deleteSoggettoGruppoByIdSoggetto.
     *
     * @param  idSoggetto
     * @throws Exception 
     */
    public void deleteSoggettoGruppoByIdSoggetto(Long idSoggetto) throws Exception;
	public List<GruppiSoggettoDTO> getGruppoSoggettoByCf(String cf) throws DAOException, Exception;
	public long countSoggettoGruppoByIdSoggetto(Long idSoggetto) throws DAOException, SystemException;
	public List<String> findFlgCapogruppoByIdSoggetto(Long idSoggetto) throws DAOException, SystemException;
}

