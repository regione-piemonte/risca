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

import it.csi.risca.riscabesrv.dto.AttivitaStatoDebitorioDTO;

/**
 * The interface Attivita Stato Debitorio DAO .
 *
 * @author CSI PIEMONTE
 */ 
public interface AttivitaStatoDebitorioDAO {
	
    /**
     * get Attivita Stato Debitorio By id AttivitaStatoDebitorio.
     *
     * @return AttivitaStatoDebitorioDTO 
     */
	AttivitaStatoDebitorioDTO getAttivitaStatoDebitorioById(Long idAttivitaStatoDebitorio);
	
    /**
     * get Attivita Stato Debitorio
     *
     * @return List AttivitaStatoDebitorioDTO 
     */
	List<AttivitaStatoDebitorioDTO> getAttivitaStatoDeb(String tipoAttivita);	

}
