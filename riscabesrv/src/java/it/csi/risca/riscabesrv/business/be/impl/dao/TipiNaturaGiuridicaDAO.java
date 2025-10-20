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

import it.csi.risca.riscabesrv.dto.TipiNaturaGiuridicaDTO;

/**
 * The interface Tipi invio dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipiNaturaGiuridicaDAO {
	

    /**
     * Load tipi natura giuridica list.
     *
     * @return List<TipiInvioDTO> list
     * @throws Exception 
     */
    List<TipiNaturaGiuridicaDTO> loadTipiNaturaGiuridica() throws Exception;
    
    /**
     * Load tipo natura giuridica.
     *
     * @return TipiNaturaGiuridicaDTO
     * @throws SQLException 
     */
    TipiNaturaGiuridicaDTO loadTipoNaturaGiuridicaByIdOrCod(String idTipoNaturaGiuridica) throws SQLException;

}
