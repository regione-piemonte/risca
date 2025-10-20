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
import it.csi.risca.riscabesrv.dto.AvvisoAnnualitaDTO;

/**
 * The interface Avviso Annualita dao.
 *
 * @author CSI PIEMONTE
 */
public interface AvvisoAnnualitaDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save avviso annualita.
	 *
	 * @param dto AvvisoAnnualitaDTO
	 * @return AvvisoAnnualitaDTO
	 */
	AvvisoAnnualitaDTO saveAvvisoAnnualita(AvvisoAnnualitaDTO dto) throws DAOException;

	/**
	 * Save avviso annualita working.
	 *
	 * @param dto AvvisoAnnualitaDTO
	 * @return AvvisoAnnualitaDTO
	 */
	AvvisoAnnualitaDTO saveAvvisoAnnualitaWorking(AvvisoAnnualitaDTO dto) throws DAOException;

	Integer updateWorkingAvvisoAnnualitaTotaleCanoneAnnoCalc(BigDecimal sumCanoneUsoPcalc, BigDecimal frazionato,
			String nap, String codRiscossione, int annoRichPagamento) throws DAOException;

	List<AvvisoAnnualitaDTO> loadAvvisoAnnualitaWorkingByNap(String nap);
	
	Integer copyAvvisoAnnualitaFromWorkingByNap(String nap, String attore) throws DAOException;
	
	Integer deleteAvvisoAnnualitaWorkingByNap(String nap) throws DAOException;
}
