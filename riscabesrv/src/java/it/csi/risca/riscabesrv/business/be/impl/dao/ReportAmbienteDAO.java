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

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.ReportResultDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchDTO;

/**
 * The interface ReportAmbienteDAO.
 *
 * @author CSI PIEMONTE
 */
public interface ReportAmbienteDAO  extends BaseRiscaBeSrvDAO {
	
	
	 ReportResultDTO creaReportRicercaAvanzata(ElaboraDTO elabora, RiscossioneSearchDTO riscossioneSearch, AmbitoDTO ambito,
			String modalitaRicerca, String attore) throws BusinessException, Exception;
	
	 ReportResultDTO creaReportRicercaMorosita(ElaboraDTO elabora, String tipoRicercaMorosita, Integer anno,
			Integer flgRest, Integer flgAnn, String lim, AmbitoDTO ambito, String attore)  throws BusinessException, Exception;
	 
	 ReportResultDTO creaReportRicercaRimborsi(ElaboraDTO elabora, String tipoRicercaRimborsi, Integer anno,
				AmbitoDTO ambito, String attore) throws BusinessException, Exception;
}
