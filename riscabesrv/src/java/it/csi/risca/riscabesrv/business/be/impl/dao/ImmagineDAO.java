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

import it.csi.risca.riscabesrv.dto.ImmagineDTO;

/**
 * The interface FilePoste dao.
 *
 * @author CSI PIEMONTE
 */
public interface ImmagineDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save file immagine
	 *
	 * @param dto ImmagineDTO
	 * @return ImmagineDTO
	 */
	ImmagineDTO saveImmagine(ImmagineDTO dto);

	/**
	 * load Immagine By idImmagine
	 *
	 * @param  idImmagine
	 * @return ImmagineDTO
	 */
	ImmagineDTO loadImmagineById(Integer idImmagine) throws SQLException;
}
