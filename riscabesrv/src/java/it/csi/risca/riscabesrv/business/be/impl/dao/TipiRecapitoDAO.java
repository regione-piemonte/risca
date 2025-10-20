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

import it.csi.risca.riscabesrv.dto.TipiRecapitoDTO;

/**
 * The interface Tipi recapito dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipiRecapitoDAO {
	

    /**
     * Load tipo recapito list.
     *
     * @return List<TipiRecapitoDTO> list
     */
    List<TipiRecapitoDTO> loadTipiRecapito();

}
