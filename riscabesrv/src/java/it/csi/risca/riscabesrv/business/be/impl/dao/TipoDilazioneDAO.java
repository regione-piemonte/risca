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

import it.csi.risca.riscabesrv.dto.TipoDilazioneDTO;

/**
 * The interface Tipo Dilazione dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipoDilazioneDAO {

	/**
	 * Load tipo dilazione list by dataScadenzaPagamento.
	 *
	 * @param dataScadenzaPagamento dataScadenzaPagamento
	 * @return TipoDilazioneDTO list
	 */
	TipoDilazioneDTO loadTipoDilazioneByDataScadenzaPagamento(String dataScadenzaPagamento, Long idAmbito);

	TipoDilazioneDTO loadTipoDilazioneByIdAmbito(Long idAmbito);
	
	List<TipoDilazioneDTO> loadTipoDilazioneByIdAmbitoAndIdTipoDilazione(Long idAmbito, Long idTipoDilazione) throws SQLException;

}
