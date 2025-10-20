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
import it.csi.risca.riscabesrv.dto.AnnualitaSdDTO;
import it.csi.risca.riscabesrv.dto.DTGrandeIdroDTO;

/**
 * The interface AnnualitaSd dao.
 *
 * @author CSI PIEMONTE
 */
public interface AnnualitaSdDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Load list of AnnualitaSdDTO by idStatoDebitorio
	 * 
	 * @param idStatoDebitorio
	 * @return List<AnnualitaSdDTO>
	 */
	List<AnnualitaSdDTO> loadAnnualitaSd(Long idStatoDebitorio, Boolean speciali, Boolean intere)throws SQLException ;
	
	AnnualitaSdDTO loadAnnualitaSdByNapCodiceUtenzaAnno(String nap, String codiceUtenza, int anno);
	
	/**
	 * insert AnnualitaSdDTO 
	 * 
	 * @param annualitaSdDTO
	 * @return AnnualitaSdDTO
	 * @throws Exception 
	 */
	AnnualitaSdDTO insertAnnualitaSd(AnnualitaSdDTO annualitaSdDTO, Long idAmbito) throws DataAccessException, SQLException, Exception ;
	
	/**
	 * update AnnualitaSdDTO 
	 * 
	 * @param annualitaSdDTO
	 * @return AnnualitaSdDTO
	 */
	AnnualitaSdDTO updateAnnualitaSd(AnnualitaSdDTO annualitaSdDTO) throws DataAccessException, SQLException ;
	
	/**
	 * insert AnnualitaSdDTO in working
	 * 
	 * @param annualitaSdDTO
	 * @return AnnualitaSdDTO
	 */
	AnnualitaSdDTO insertAnnualitaSdWorking(AnnualitaSdDTO annualitaSdDTO) throws DataAccessException, SQLException ;

	
	
	/**
	 * delete  AnnualitaSdDTO 
	 * 
	 * @param annualitaSdDTO
	 * @return AnnualitaSdDTO
	 * @throws Exception 
	 */
	AnnualitaSdDTO deleteAnnualitaSd(AnnualitaSdDTO annualitaSdDTO) throws DataAccessException, SQLException, Exception ;

	AnnualitaSdDTO updateAnnualitaSdWorking(AnnualitaSdDTO annualitaSdDTO) throws DataAccessException, SQLException ;
	
	List<AnnualitaSdDTO> loadAnnualitaSdWorking(Long idStatoDebitorio)throws SQLException ;

	Integer deleteAnnualitaSdWorkingById(Long idAnnualitaSd) throws DAOException;
	
	Integer copyAnnualitaSdFromWorkingByStatoDebitorio(Long idStatoDebitorio, String attore) throws DAOException;

	AnnualitaSdDTO loadAnnualitaSdById(Integer idAnnualitaSd);
	
	AnnualitaSdDTO updateDatiTecniciGrandeIdro(Integer idAnnualitaSd, DTGrandeIdroDTO dTGrandeIdroDTO) throws Exception;
	
	AnnualitaSdDTO loadAnnualitaSdGrandeIdro(Long idStatoDebitorio) throws SQLException;
	
}
