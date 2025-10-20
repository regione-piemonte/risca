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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.RpPagopaDTO;

/**
 * The interface RpPagopa dao.
 *
 * @author CSI PIEMONTE
 */
public interface RpPagopaDAO extends BaseRiscaBeSrvDAO {

	RpPagopaDTO saveRpPagopaDTO(RpPagopaDTO dto) throws DAOException;
	
	List<RpPagopaDTO> loadRpPagopaByElabora(Long idElabora);

}
