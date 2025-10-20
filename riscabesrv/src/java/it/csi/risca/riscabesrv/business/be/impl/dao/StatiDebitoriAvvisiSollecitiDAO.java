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

import it.csi.risca.riscabesrv.dto.AvvisoSollecitoCheckSDResultDTO;
import it.csi.risca.riscabesrv.dto.AvvisoSollecitoCheckSoggettoResultDTO;
import it.csi.risca.riscabesrv.dto.StatiDebitoriAvvisiSollecitiDTO;

/**
 * The interface StatiDebitoriAvvisiSolleciti dao.
 *
 * @author CSI PIEMONTE
 */
public interface StatiDebitoriAvvisiSollecitiDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Load list of StatiDebitoriAvvisiSollecitiDTO by codAmbito, codAttivitaStatoDeb
	 * 
	 * @param codAmbito
	 * @param codAttivitaStatoDeb
	 * @return List<StatiDebitoriAvvisiSollecitiDTO>
	 */
	List<StatiDebitoriAvvisiSollecitiDTO> loadStatiDebitoriAvvisiSolleciti(String codAmbito, String codAttivitaStatoDeb);


	AvvisoSollecitoCheckSDResultDTO checkCondizioneSdPerPagopa(Long idSoggetto, Long idAttivitaStatoDeb);
	
	List<AvvisoSollecitoCheckSoggettoResultDTO> loadSoggettoSdPraticaPerCheckCondizione2(Long idSoggetto, Long idAttivitaStatoDeb);

}
