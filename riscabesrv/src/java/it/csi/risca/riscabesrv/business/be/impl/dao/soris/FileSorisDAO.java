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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.BaseRiscaBeSrvDAO;
import it.csi.risca.riscabesrv.dto.soris.FileSorisDTO;

/**
 * The interface FileSoris dao.
 *
 * @author CSI PIEMONTE
 */
public interface FileSorisDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save file soris
	 *
	 * @param dto FileSorisDTO
	 * @return FileSorisDTO
	 */
	FileSorisDTO saveFileSoris(FileSorisDTO dto);

	Integer countFileSorisByNomeFile(String nomeFileSoris) throws DAOException, SystemException;
}
