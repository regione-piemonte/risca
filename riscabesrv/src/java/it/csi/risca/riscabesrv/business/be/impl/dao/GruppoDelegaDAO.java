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
import it.csi.risca.riscabesrv.dto.GruppoDelegaDTO;
import it.csi.risca.riscabesrv.dto.GruppoDelegaExtendedDTO;

/**
 * The interface GruppoDelegaDAO.
 *
 * @author CSI PIEMONTE
 */
public interface GruppoDelegaDAO {


    /**
     * loadSoggettiDelegaByIdDelegato.
     *
     * @param  idDelegato
     * @return List<GruppoDelegaExtendedDTO> 
     * @throws Exception 
     */
	List<GruppoDelegaExtendedDTO> loadGruppiDelegaByIdDelegato(Long idDelegato) throws Exception;

	GruppoDelegaDTO createGruppoDelega(GruppoDelegaDTO gruppoDelega) throws DAOException, SystemException;
}
