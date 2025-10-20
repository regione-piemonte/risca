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

/**
 * The interface TernaProdEnerg dao.
 *
 * @author CSI PIEMONTE
 */
public interface TernaProdEnergDAO extends BaseRiscaBeSrvDAO {

	BigDecimal loadTotEnergProdAnno(String codUtenza, String anno) throws Exception;
	
	BigDecimal loadTotRicaviAnno(String codUtenza, String anno) throws Exception;

}
