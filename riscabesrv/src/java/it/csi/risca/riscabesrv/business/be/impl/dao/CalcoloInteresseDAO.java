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
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;

/**
 * The interface Calcolo Interesse dao.
 *
 * @author CSI PIEMONTE
 */
public interface CalcoloInteresseDAO {

	/**
	 * calcoloInteressi.
	 *
	 * @param  idAmbito, importo , dataScadenza,  dataVersamento
	 * @return 	BigDecimal
	 * @throws SystemException 
	 */
	BigDecimal calcoloInteressi(Long idAmbito, BigDecimal importo, String dataScadenza, String dataVersamento)throws BusinessException, DataAccessException, SQLException, SystemException;
}
