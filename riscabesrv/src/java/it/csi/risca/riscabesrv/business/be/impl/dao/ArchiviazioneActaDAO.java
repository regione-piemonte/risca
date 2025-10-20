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

import it.csi.risca.riscabesrv.dto.ArchiviazioneActaDTO;

/**
 * 
 * The interface SpedizioniArchiviazioneActa dao.
 *
 * @author CSI PIEMONTE
 */
public interface ArchiviazioneActaDAO extends BaseRiscaBeSrvDAO {

	List<ArchiviazioneActaDTO> loadDocumentiActaDaArchiviareBySped(Long idSpedizione, String codTipoSpedizione,
			Long limit);

	List<ArchiviazioneActaDTO> loadDocumentiActaNonArchiviatiBySpedActa(Long idSpedizioneActa,
			String codTipoSpedizione);

}
