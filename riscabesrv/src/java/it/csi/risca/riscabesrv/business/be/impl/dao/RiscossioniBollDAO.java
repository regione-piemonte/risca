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

import it.csi.risca.riscabesrv.dto.RiscossioneBollDTO;

/**
 * The interface RiscossioniBoll dao.
 *
 * @author CSI PIEMONTE
 */
public interface RiscossioniBollDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Load list of RiscossioneBollDTO by codAmbito, dtScadPagamento
	 * 
	 * @param idAmbito
	 * @param dtScadPagamento
	 * @return List<RiscossioneBollDTO>
	 * @throws Exception
	 */
	List<RiscossioneBollDTO> loadRiscossioniBollettazioneSpeciale(String codAmbito, String dtScadPagamento,
			String tipoElabora) throws Exception;

	/**
	 * Load list of RiscossioneBollDTO by codAmbito, dtScadPagamento, anno
	 * 
	 * @param idAmbito
	 * @param dtScadPagamento
	 * @param anno
	 * @return List<RiscossioneBollDTO>
	 * @throws Exception
	 */
	List<RiscossioneBollDTO> loadRiscossioniBollettazioneOrdinaria(String codAmbito, String dtScadPagamento,
			String anno) throws Exception;

}
