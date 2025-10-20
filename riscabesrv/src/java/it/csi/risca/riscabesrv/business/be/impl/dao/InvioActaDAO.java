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
import it.csi.risca.riscabesrv.dto.InvioActaDTO;

/**
 * 
 * The interface InvioActa dao.
 *
 * @author CSI PIEMONTE
 */
public interface InvioActaDAO extends BaseRiscaBeSrvDAO {

	InvioActaDTO saveInvioActa(InvioActaDTO dto);

	InvioActaDTO updateInvioActa(InvioActaDTO dto);

	InvioActaDTO loadInvioActaByNap(String nap);
	
	InvioActaDTO loadInvioActaByIdAccertamento(Long idAccertamento);

	List<InvioActaDTO> loadDocumentiDaArchiviareBySpedizioneActa(Long idSpedizioneActa);
	
	List<InvioActaDTO> loadDocumentiWarningError(Long idSpedizioneActa);
	
	Integer deleteInvioActaById(Long idInvioActa) throws DAOException;
	
	Integer deleteInvioActaByNap(String nap) throws DAOException;
	
	Integer deleteInvioActaByAccertamento(Long idAccertamento) throws DAOException;
	
}
