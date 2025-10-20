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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdExtendedDTO;

/**
 * The interface AnnualitaUsoSd dao.
 *
 * @author CSI PIEMONTE
 */
public interface AnnualitaUsoSdDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Load list of AnnualitaUsoSdDTO by idAnnualitaSd 
	 * @param idAnnualitaSd
	 * @return List<AnnualitaUsoSdExtendedDTO>
	 */
	List<AnnualitaUsoSdExtendedDTO> loadAnnualitaUsiPrincipaliSd(Long idAnnualitaSd);
	
	/**
	 * Load list of AnnualitaUsoSdDTO by idAnnualitaSd 
	 * @param idAnnualitaSd
	 * @return List<AnnualitaUsoSdExtendedDTO>
	 */
	List<AnnualitaUsoSdDTO> loadAnnualitaUsiByIdAnnualitaSd(Long idAnnualitaSd);
	
	/**
	 * saveAnnualitaUsoSdDTO
	 * @param annualitaUsoSdDTO
	 * @return AnnualitaUsoSdDTO
	 * @throws Exception 
	 */
	
	AnnualitaUsoSdDTO saveAnnualitaUsoSdDTO(AnnualitaUsoSdDTO annualitaUsoSdDTO) throws DataAccessException, SQLException, Exception;
	
	/**
	 * saveAnnualitaUsoSdWorking
	 * @param annualitaUsoSdDTO
	 * @return AnnualitaUsoSdDTO
	 */
	
	AnnualitaUsoSdDTO saveAnnualitaUsoSdWorking(AnnualitaUsoSdDTO annualitaUsoSdDTO) throws DataAccessException, SQLException;
	
	/**
	 * update AnnualitaUsoSdDTO
	 * @param annualitaUsoSdDTO
	 * @return AnnualitaUsoSdDTO
	 * @throws Exception 
	 */
	
	AnnualitaUsoSdDTO updateAnnualitaUsoSdDTO(AnnualitaUsoSdDTO annualitaUsoSdDTO) throws DataAccessException, SQLException, Exception;
	
	/**
	 * update AnnualitaUsoSdDTO
	 * @param annualitaUsoSdDTO
	 * @return AnnualitaUsoSdDTO
	 * @throws Exception 
	 */
	
	AnnualitaUsoSdDTO deleteAnnualitaUsoSdDTO(AnnualitaUsoSdDTO annualitaUsoSdDTO) throws DataAccessException, SQLException, Exception;
	
	
	List<AnnualitaUsoSdDTO> loadAnnualitaUsoSdWorkingByIdAnnualitaSd(Long idAnnualitaSd) throws DAOException;
	
	Integer deleteAnnualitaUsoSdWorkingByIdAnnualitaSd(Long idAnnualitaSd) throws DAOException;
	
	List<AnnualitaUsoSdDTO> copyAnnualitaUsoSdFromWorkingByAnnualitaSd(Long idAnnualitaSd, String attore) throws DAOException;

}
