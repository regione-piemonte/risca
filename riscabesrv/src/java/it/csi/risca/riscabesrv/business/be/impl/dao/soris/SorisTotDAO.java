/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.soris;


import it.csi.risca.riscabesrv.business.be.impl.dao.BaseRiscaBeSrvDAO;
import it.csi.risca.riscabesrv.dto.soris.SorisTotDTO;

/**
 * The interface SorisTot dao.
 *
 * @author CSI PIEMONTE
 */
public interface SorisTotDAO extends BaseRiscaBeSrvDAO {

	SorisTotDTO saveSorisTot(SorisTotDTO dto);

	
}
