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
import it.csi.risca.riscabesrv.dto.PagopaScompVarIuvDTO;
import it.csi.risca.riscabesrv.dto.ScomposizioneRichiestaIuvDTO;

/**
 * The interface PagopaScompVarIuv dao.
 *
 * @author CSI PIEMONTE
 */
public interface PagopaScompVarIuvDAO extends BaseRiscaBeSrvDAO {

	PagopaScompVarIuvDTO savePagopaScompVarIuv(PagopaScompVarIuvDTO dto);

	PagopaScompVarIuvDTO savePagopaScompVarIuvWorking(PagopaScompVarIuvDTO dto);

	List<PagopaScompVarIuvDTO> loadScompVarIuvWorkingByNap(String nap);
	
	List<PagopaScompVarIuvDTO> loadScompVarIuvWorkingByIdIuv(Long idIuv, Long idElabora);
	
	List<ScomposizioneRichiestaIuvDTO> loadScomposizioneCanone(Long idElabora);
	
	List<ScomposizioneRichiestaIuvDTO> loadScomposizioneInteressi(Long idElabora);
	
	List<ScomposizioneRichiestaIuvDTO> loadScomposizioneSpeseNotifica(Long idElabora);
	
	Integer deletePagopaScompVarIuvWorkingWhereNap(List<String> listaNap) throws DAOException;
	
	Integer deletePagopaScompVarIuvWorking() throws DAOException;

}
