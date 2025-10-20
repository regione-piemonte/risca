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

import java.math.BigDecimal;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.SollDatiPagopaDTO;

/**
 * The interface SollDatiPagopa dao.
 *
 * @author CSI PIEMONTE
 */
public interface SollDatiPagopaDAO extends BaseRiscaBeSrvDAO {

	SollDatiPagopaDTO loadSollDatiPagopaWorkingByIuv(String iuv);
	
	SollDatiPagopaDTO saveSollDatiPagopaWorking(SollDatiPagopaDTO dto) throws DAOException;
	
	Integer updateSollDatiPagopaWorkingImportoByIuv(String iuv, BigDecimal importoDaVersare) throws DAOException;
	
	Integer deleteSollDatiPagopaWorkingByElabora(Long idElabora) throws DAOException;
	
	Integer deleteSollDatiPagopaByIdAccertamento(Long idAccertamento) throws DAOException;

	Integer copySollDatiPagopaFromWorking(Long idElabora, String attore) throws DAOException;
}
