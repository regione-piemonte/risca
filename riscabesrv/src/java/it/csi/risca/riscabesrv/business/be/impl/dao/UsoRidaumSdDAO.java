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

import org.springframework.dao.DataAccessException;

import it.csi.risca.riscabesrv.dto.UsoRidaumSdExtendedDTO;

/**
 * The interface Uso Ridaum Sd DAO.
 *
 * @author CSI PIEMONTE
 */
public interface UsoRidaumSdDAO {

	
    /**
     * Load  Uso Ridaum sd By idAnnualitaUsoSd
     *
     * @param idAnnualitaUsoSd idAnnualitaUsoSd
     *
     * @return UsoRidaumSdDTO
     */
	List<UsoRidaumSdExtendedDTO> loadUsoRidaumSdByIdAnnualitaUsoSd(long idAnnualitaUsoSd);
	
    /**
     * save  Uso Ridaum sd
     *
     * @param usoRidaumSdExtendedDTO
     *
     * @return UsoRidaumSdDTO
     * @throws Exception 
     */
	UsoRidaumSdExtendedDTO saveUsoRidaumSD(UsoRidaumSdExtendedDTO usoRidaumSdExtendedDTO) throws DataAccessException, SQLException, Exception ;
	
    /**
     * update  Uso Ridaum sd
     *
     * @param usoRidaumSdExtendedDTO
     *
     * @return UsoRidaumSdDTO
     * @throws Exception 
     */
	UsoRidaumSdExtendedDTO updateUsoRidaumSD(UsoRidaumSdExtendedDTO usoRidaumSdExtendedDTO) throws DataAccessException, SQLException, Exception ;
	
	
    /**
     * DELETE  Uso Ridaum sd
     *
     * @param usoRidaumSdExtendedDTO
     *
     * @return UsoRidaumSdDTO
     */
	UsoRidaumSdExtendedDTO deleteUsoRidaumSD(UsoRidaumSdExtendedDTO usoRidaumSdExtendedDTO) throws DataAccessException, SQLException ;

}
