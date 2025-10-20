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

import it.csi.risca.riscabesrv.dto.ComponenteDtExtendedDTO;

/**
 * The interface ComponentiDt dao.
 *
 * @author CSI PIEMONTE
 */
public interface ComponentiDtDAO {

	   /**
     * Load componentiDt list.
     *
     * @param idAmbito  idAmbito
     * @param codTipoComponente  codTipoComponente
     * @param attivo  attivo
     * @return List<ComponenteDtExtendedDTO> list
     */
    List<ComponenteDtExtendedDTO> loadComponentiDt(Long idAmbito, String codTipoComponente, boolean attivo) throws SQLException ;

    ComponenteDtExtendedDTO loadComponenteDt(Long idAmbito, String codTipoComponente, String dataRif) throws SQLException ;
}
