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
import it.csi.risca.riscabesrv.dto.LottoDTO;

/**
 * The interface Lotto dao.
 *
 * @author CSI PIEMONTE
 */
public interface LottoDAO extends BaseRiscaBeSrvDAO {

	LottoDTO saveLotto(LottoDTO dto);

	Integer updateEsitoLottoByIdLotto(Long idLotto, Integer flgInviato, String codEsito, String descEsito) throws DAOException;
	
	Integer updateFlgRicevutoElaboratoByNomeLotto(Long idElabora, String nomeLotto, Integer flgRicevuto, Integer flgElaborato, String codEsitoAcquisizione, String descEsitoAcquisizione) throws DAOException;
	
	List<LottoDTO> loadLottoInviatoByName(Long idElabora, String nomeLotto);
	
	List<LottoDTO> findLottiAttesi(Long idElabora);
	
	List<LottoDTO> findLottiRicevuti(Long idElabora, List<Long> listaLotti);
	
	List<LottoDTO> findLottiAnomali(Long idElabora, List<Long> listaLotti);

}
