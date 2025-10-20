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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.TipoRimborsoDTO;

/**
 * The interface Tipo Rimborso DAO
 *
 * @author CSI PIEMONTE
 */
public interface TipoRimborsoDAO extends BaseRiscaBeSrvDAO {

	List<TipoRimborsoDTO> loadTipiRimborso(SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DAOException;
	
	
	TipoRimborsoDTO loadTipiRimborsoById(Long idTipoRimborso,SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DAOException;
}
