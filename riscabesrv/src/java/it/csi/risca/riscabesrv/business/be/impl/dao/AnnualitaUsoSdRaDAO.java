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

import org.springframework.dao.DataAccessException;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdRaDTO;

/**
 * The interface AnnualitaUsoSdRa dao.
 *
 * @author CSI PIEMONTE
 */
public interface AnnualitaUsoSdRaDAO extends BaseRiscaBeSrvDAO {
	
	/**
	 * saveAnnualitaUsoSdRa
	 * @param annualitaUsoSdRaDTO
	 * @return AnnualitaUsoSdRaDTO
	 */
	
	AnnualitaUsoSdRaDTO saveAnnualitaUsoSdRa(AnnualitaUsoSdRaDTO annualitaUsoSdRaDTO) throws DataAccessException, SQLException;

	/**
	 * saveAnnualitaUsoSdRaWorking
	 * @param annualitaUsoSdRaDTO
	 * @return AnnualitaUsoSdRaDTO
	 */
	
	AnnualitaUsoSdRaDTO saveAnnualitaUsoSdRaWorking(AnnualitaUsoSdRaDTO annualitaUsoSdRaDTO) throws DataAccessException, SQLException;
	
	Integer deleteAnnualitaUsoSdRaWorkingByIdAnnualitaUsoSd(Long idAnnualitaUsoSd) throws DAOException;
	
	Integer copyAnnualitaUsoSdRaFromWorkingByAnnualitaUsoSd(Long idAnnualitaUsoSd, String attore) throws DAOException;
	

}
