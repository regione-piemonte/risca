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
import it.csi.risca.riscabesrv.dto.SollDestinatariDTO;

/**
 * The interface SollDestinatari dao.
 *
 * @author CSI PIEMONTE
 */
public interface SollDestinatariDAO extends BaseRiscaBeSrvDAO {

	SollDestinatariDTO saveSollDestinatariWorking(SollDestinatariDTO dto) throws DAOException;

}
