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

import it.csi.risca.riscabesrv.dto.TracciamentoDTO;

/**
 * The interface Tracciamento DAO.
 *
 * @author CSI PIEMONTE
 */
public interface TracciamentoDAO {
 
    /**
     * Save  Tracciamento
     *
     * @param dto TracciamentoDTO
     */
	TracciamentoDTO saveTracciamento(TracciamentoDTO tracciamento);
}
