/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.pagopa;

import it.csi.epay.epaywso.epaywso2enti.types.EsitoInserimentoListaDiCaricoRequest;
import it.csi.epay.epaywso.epaywso2enti.types.TrasmettiNotifichePagamentoRequest;
import it.csi.epay.epaywso.types.ResponseType;

public interface IEpayToRiscaWS {

	public ResponseType EsitoInserimentoListaDiCarico(EsitoInserimentoListaDiCaricoRequest parameters) throws Exception;
	
	public ResponseType TrasmettiNotifichePagamento(TrasmettiNotifichePagamentoRequest parameters);

}
