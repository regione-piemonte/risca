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
import it.csi.risca.riscabesrv.dto.AvvisoUsoDTO;

/**
 * The interface Avviso Uso dao.
 *
 * @author CSI PIEMONTE
 */
public interface AvvisoUsoDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save avviso uso.
	 *
	 * @param dto AvvisoUsoDTO
	 * @return AvvisoUsoDTO
	 */
	AvvisoUsoDTO saveAvvisoUso(AvvisoUsoDTO dto) throws DAOException;
	
	AvvisoUsoDTO saveAvvisoUsoWorking(AvvisoUsoDTO dto) throws DAOException;
	
	/**
	 * Load sum canone uso p calc by nap, codiceUtenza, anno
	 * @param idAnnualitaSd
	 * @return BigDecimal
	 */
	BigDecimal sumCanoneUsoPcalc(String nap, String codiceUtenza, int anno) throws DAOException;

	List<AvvisoUsoDTO> loadAvvisoUsoWorkingByNap(String nap);
	
	Integer copyAvvisoUsoFromWorkingByNap(String nap, String attore) throws DAOException;
	
	Integer deleteAvvisoUsoWorkingByNap(String nap) throws DAOException;
}
