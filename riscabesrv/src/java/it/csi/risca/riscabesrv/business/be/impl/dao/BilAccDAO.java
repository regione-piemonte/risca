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

import it.csi.risca.riscabesrv.dto.BilAccDTO;

/**
 * The interface BilAcc dao.
 *
 * @author CSI PIEMONTE
 */
public interface BilAccDAO extends BaseRiscaBeSrvDAO {

	
	/**
	 * 
	 * @param idAmbito
	 * @param anno
	 * @param annoCompetenza
	 * @param idAccertaBilancio
	 * @return
	 */
	BilAccDTO loadBilAccByCodAccertaBilancio(Long idAmbito, Integer anno, Integer annoCompetenza,
			String codAccertaBilancio, Integer annoPag);
	
	BilAccDTO loadBilAccByIdAccertaBilancio(Long idAmbito, Integer anno, Integer annoCompetenza,
			Long idAccertaBilancio, Integer annoPag);
	
	BilAccDTO getNumeroAccBilancioByDataScadenzaPag(String dataScadenzaPag, int idAccertaBilancio) throws Exception;

	BilAccDTO loadBilAccValidoPiuVecchio(Long idAmbito, String codAccertaBilancio, Integer annoPag);

}
