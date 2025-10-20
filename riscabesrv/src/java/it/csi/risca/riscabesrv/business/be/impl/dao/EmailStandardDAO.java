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

import it.csi.risca.riscabesrv.dto.EmailStandardDTO;

/**
 * The interface Email Standard dao.
 *
 * @author CSI PIEMONTE
 */
public interface EmailStandardDAO {

	/**
	 * Load email servizio by codEmailStandard.
	 *
	 * @param codFonte codFonte
	 * @return FonteDTO
	 */

	EmailStandardDTO loadEmailStandardByCodEmail(String codEmailStandard);

}
