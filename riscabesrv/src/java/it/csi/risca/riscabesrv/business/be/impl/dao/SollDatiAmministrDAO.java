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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.SollDatiAmministrDTO;

/**
 * The interface SollDatiAmministr dao.
 *
 * @author CSI PIEMONTE
 */
public interface SollDatiAmministrDAO extends BaseRiscaBeSrvDAO {

	SollDatiAmministrDTO saveSollDatiAmministrWorking(SollDatiAmministrDTO dto) throws DAOException;

	Integer updateCodiceAvvisoWorkingByIdAccertamento(String codiceAvviso, Long idAccertamento) throws DAOException;
	
	Integer deleteSollDatiAmministrWorkingByElabora(Long idElabora) throws DAOException;
	
	Integer deleteSollDatiAmministrByIdAccertamento(Long idAccertamento) throws DAOException;
	
	Integer copySollDatiAmministrFromWorking(Long idElabora, String attore) throws DAOException;
}
