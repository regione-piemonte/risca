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

import it.csi.risca.riscabesrv.dto.EntePagopaDTO;

/**
 * The interface EntePagopa dao.
 *
 * @author CSI PIEMONTE
 */
public interface EntePagopaDAO extends BaseRiscaBeSrvDAO {

	EntePagopaDTO loadEntePagopaPerIuvByAmbito(Long idAmbito);
	
	EntePagopaDTO loadEntePagopaByCodVersamentoEnteCreditore(String codiceVersamento, String cfEnteCreditore, boolean checkIuv);

}
