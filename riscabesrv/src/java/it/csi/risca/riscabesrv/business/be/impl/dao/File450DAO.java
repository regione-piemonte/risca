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
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.File450DTO;

/**
 * The interface File 450 dao.
 *
 * @author CSI PIEMONTE
 */
public interface File450DAO extends BaseRiscaBeSrvDAO {


	/**
	 * Save file 450
	 *
	 * @param dto File450DTO
	 * @return File450DTO
	 */
	File450DTO saveFile450(File450DTO dto) throws DAOException;

	File450DTO loadFile450ByIdFile450(Long idFile450) throws DAOException;
	
    public long countAnnoDataCreazioneByAnnoCorrente() throws DAOException, SystemException;
}
