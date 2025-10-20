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
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.SoggettoDelegaDTO;
import it.csi.risca.riscabesrv.dto.SoggettoDelegaExtendedDTO;

/**
 * The interface SoggettoDelegaDAO.
 *
 * @author CSI PIEMONTE
 */
public interface SoggettoDelegaDAO {


    /**
     * loadSoggettiDelegaByIdDelegato.
     *
     * @param  idDelegato
     * @return List<SoggettoDelegaExtendedDTO> 
     * @throws Exception 
     */
	List<SoggettoDelegaExtendedDTO> loadSoggettiDelegaByIdDelegato(Long idDelegato) throws Exception;

	SoggettoDelegaDTO createSoggettoDelega(SoggettoDelegaDTO soggettoDelega) throws DAOException, SystemException;
}
