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

import it.csi.risca.riscabesrv.dto.EmailStPuntiValoriDTO;

/**
 * The interface EmailStPuntiValoriDAO.
 *
 * @author CSI PIEMONTE
 */
public interface EmailStPuntiValoriDAO {

	/**
	 * @param codEmailStandard codEmailStandard
	 * @param nomeFoglio       nomeFoglio
	 * @return List<EmailStPuntiValoriDTO>
	 */

	List<EmailStPuntiValoriDTO> loadEmailStPuntiValoriByEmailFoglio(String codEmailStandard, String nomeFoglio);

}
