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

import java.sql.SQLException;
import java.util.List;

import it.csi.risca.riscabesrv.dto.TipiSoggettoDTO;

/**
 * The interface Tipi soggetto dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipiSoggettoDAO {
	

    /**
     * Load tipo soggetto list.
     *
     * @return List<TipiSoggettoDTO> list
     */
    List<TipiSoggettoDTO> loadTipiSoggetto();

    /**
     * Load tipo soggeti list by idTipoSoggetto or codTipoSoggetto.
     *
     * @param idTipoSoggetto idTipoSoggetto
     * @return TipiSoggettoDTO tipoSoggettoDTO
     * @throws SQLException 
     */
    TipiSoggettoDTO loadTipiSoggettoByIdOrCodTipoSoggetto(String idTipoSoggetto) throws SQLException;

}
