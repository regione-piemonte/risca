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
import it.csi.risca.riscabesrv.dto.PagopaListaCarIuvDTO;

/**
 * The interface PagopaListaCarIuv dao.
 *
 * @author CSI PIEMONTE
 */
public interface PagopaListaCarIuvDAO extends BaseRiscaBeSrvDAO {

	PagopaListaCarIuvDTO savePagopaListaCarIuv(PagopaListaCarIuvDTO dto);
	Integer updateEsitoListaCarIuvByNap(String codEsito, String descEsito, String codiceAvviso, String iuv, String nap) throws DAOException;
	List<PagopaListaCarIuvDTO> loadPagopaListaCarIuvByNap(String nap);
}
