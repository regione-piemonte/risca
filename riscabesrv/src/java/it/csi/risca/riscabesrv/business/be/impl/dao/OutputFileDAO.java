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

import it.csi.risca.riscabesrv.dto.OutputFileDTO;

/**
 * The interface OutputFile dao.
 *
 * @author CSI PIEMONTE
 */
public interface OutputFileDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Load Output file by idOutputFile
	 *
	 * @param idOutputFile Long
	 * @return OutputFileDTO
	 */
	OutputFileDTO loadOutputFile(Long idOutputFile);

	OutputFileDTO loadOutputFileByAmbitoTipoElabNomeFile(String codAmbito, String codTipoElabora, String nomeFile) throws Exception;

}
