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
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.SollDatiTitolareDTO;

/**
 * The interface SollDatiTitolare dao.
 *
 * @author CSI PIEMONTE
 */
public interface SollDatiTitolareDAO extends BaseRiscaBeSrvDAO {

	SollDatiTitolareDTO saveSollDatiTitolareWorking(SollDatiTitolareDTO dto) throws DAOException;
	
	SollDatiTitolareDTO saveSollDatiTitolare(SollDatiTitolareDTO dto) throws DAOException;
	
	Integer deleteSollDatiTitolareWorkingByElabora(Long idElabora) throws DAOException;
	
	Integer deleteSollDatiTitolareByIdAccertamento(Long idAccertamento) throws DAOException;
	
	Integer copySollDatiTitolareFromWorking(Long idElabora, String attore) throws DAOException;
	
	public SollDatiTitolareDTO loadSollDatiTitolareByIdAccertamento(Long idAccertamento) throws DataAccessException, SQLException, DAOException, SystemException;
}
