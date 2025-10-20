/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.tracciamento;

import javax.servlet.http.HttpServletRequest;


import it.csi.iride2.policy.entity.Identita;

public interface TracciamentoManager {

  void saveTracciamento(String fruitore, Object tracciabile, Identita identita, Long idRiscossione, String tipoJson, String keyOper,
			String oggOperazione, String flgOperazione, String operazione, boolean isLogAudit, boolean isTracciamento,
			HttpServletRequest httpRequest) throws Exception;
}
