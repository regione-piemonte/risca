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

import it.csi.risca.riscabesrv.dto.EmailServizioDTO;

/**
 * The interface Email Servizio dao.
 *
 * @author CSI PIEMONTE
 */
public interface EmailServizioDAO {

	/**
	 * Load email servizio by codEmailServizio.
	 *
	 * @param codFonte codFonte
	 * @return FonteDTO
	 */

	EmailServizioDTO loadEmailServizioByCodEmail(String codEmailServizio);

}
