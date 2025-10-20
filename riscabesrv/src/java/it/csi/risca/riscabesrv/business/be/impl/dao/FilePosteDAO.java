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

import it.csi.risca.riscabesrv.dto.FilePosteDTO;

/**
 * The interface FilePoste dao.
 *
 * @author CSI PIEMONTE
 */
public interface FilePosteDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save file poste
	 *
	 * @param dto FilePosteDTO
	 * @return FilePosteDTO
	 */
	FilePosteDTO saveFilePoste(FilePosteDTO dto);
}
