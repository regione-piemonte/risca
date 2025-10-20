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
import java.util.List;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.AvvisoDatiAmminDTO;

/**
 * The interface Avviso Dati Ammin dao.
 *
 * @author CSI PIEMONTE
 */
public interface AvvisoDatiAmminDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save avviso dati amministrativi.
	 *
	 * @param dto AvvisoDatiAmminDTO
	 * @return AvvisoDatiAmminDTO
	 */
	AvvisoDatiAmminDTO saveAvvisoDatiAmmin(AvvisoDatiAmminDTO dto) throws DAOException;

	AvvisoDatiAmminDTO saveAvvisoDatiAmminWorking(AvvisoDatiAmminDTO dto) throws DAOException;

	Integer updateWorkingAvvisoDatiAmminPeriodoContrib(String periodoDiContribuzione, String nap, String codiceUtenza) throws DAOException;
	
	Integer updateWorkingAvvisoDatiAmminTotaleUtenza(BigDecimal canoneAnnuo, String nap, String codiceUtenza) throws DAOException;

	Integer updateWorkingAvvisoDatiAmminCompensazione(BigDecimal compensazione, String nap, String codiceUtenza) throws DAOException;

	List<AvvisoDatiAmminDTO> loadAvvisoDatiAmminWorkingByNap(String nap);
	
	Integer copyAvvisoDatiAmminFromWorkingByNap(String nap, String attore) throws DAOException;
	
	Integer deleteAvvisoDatiAmminWorkingByNap(String nap) throws DAOException;

}
