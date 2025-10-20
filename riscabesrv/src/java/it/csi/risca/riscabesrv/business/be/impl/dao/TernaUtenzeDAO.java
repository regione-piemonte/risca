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

import java.util.List;

/**
 * The interface TernaUtenze dao.
 *
 * @author CSI PIEMONTE
 */
public interface TernaUtenzeDAO extends BaseRiscaBeSrvDAO{
	
    List<String> loadDistinctCodUtenza() throws Exception;
    
}
