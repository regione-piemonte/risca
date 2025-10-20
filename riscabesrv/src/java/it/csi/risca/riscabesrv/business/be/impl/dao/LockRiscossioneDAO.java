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

import java.sql.SQLException;
import java.util.List;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.LockRiscossioneDTO;

public interface LockRiscossioneDAO {


	public List<LockRiscossioneDTO> getAllLockRiscossione() throws SQLException;
	
	public LockRiscossioneDTO getLockRiscossioneByIdRiscossione(Long idRiscossione) throws SQLException;
	
	public LockRiscossioneDTO saveLockRiscossione(LockRiscossioneDTO lockRiscossioneDTO);

	public void deleteLockRiscossione(Long idRiscossione) throws SQLException ;
	
	public void verifyLockRiscossione(Long idRiscossione, String fruitore, String cf) throws SQLException,BusinessException, SystemException;
}
