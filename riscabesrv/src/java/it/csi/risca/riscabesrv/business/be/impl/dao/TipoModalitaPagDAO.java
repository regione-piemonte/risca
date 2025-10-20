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

import it.csi.risca.riscabesrv.dto.TipoModalitaPagDTO;
import it.csi.risca.riscabesrv.dto.TipoRicercaPagamentoDTO;

/**
 * The interface Tipo modalita pag dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipoModalitaPagDAO {
	

    /**
     * Load tipo modalita pag .
     *
     * @return TipoModalitaPagDTO
     */
    TipoModalitaPagDTO loadTipoModalitaPagByCodTipoModalitaPag(String codTipoModalitaPag);

    /**
     * load All Tipi Modalita Pagamenti .
     *
     * @return List<TipoModalitaPagDTO>
     */
	List<TipoModalitaPagDTO> loadAllTipiModalitaPagamenti();

}
