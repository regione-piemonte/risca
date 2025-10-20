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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.PagopaListaCaricoDTO;

/**
 * The interface PagopaListaCarico dao.
 *
 * @author CSI PIEMONTE
 */
public interface PagopaListaCaricoDAO extends BaseRiscaBeSrvDAO {

	PagopaListaCaricoDTO savePagopaListaCarico(PagopaListaCaricoDTO dto);

	Integer updateEsitoListaCaricoByNap(String codEsito, String descEsito, String nap, Long idLotto, String attore)
			throws DAOException;

	PagopaListaCaricoDTO loadPagopaListaCaricoByLottoNap(Long idLotto, String nap);
}
