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

import it.csi.risca.riscabesrv.dto.TipiSedeDTO;

/**
 * The interface Tipi sede dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipiSedeDAO {
	

    /**
     * Load tipo sede list.
     *
     * @return List<TipiSedeDTO> list
     */
    List<TipiSedeDTO> loadTipiSede(String tipoSoggetto);

}
