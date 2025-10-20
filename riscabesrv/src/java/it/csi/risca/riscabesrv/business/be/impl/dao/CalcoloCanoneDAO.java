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

import it.csi.risca.riscabesrv.dto.CalcoloCanoneDTO;
import it.csi.risca.riscabesrv.dto.CalcoloCanoneSingoloDTO;
import it.csi.risca.riscabesrv.dto.ambiente.RiscossioneDatoTecnicoDTO;

/**
 * The interface Calcolo Canone dao.
 *
 * @author CSI PIEMONTE
 */
public interface CalcoloCanoneDAO {
	
    /**
     * Calcola canone.
     *
     * @return canone
     */
    CalcoloCanoneDTO calcoloCanone(Long idRiscossione, String dataRiferimento)throws Exception;
    
    /**
     * Calcola canone singolo.
     *
     * @return canone
     */
    CalcoloCanoneSingoloDTO calcoloCanoneSingoloEFrazionato(RiscossioneDatoTecnicoDTO dto, String dataRiferimento, String dataFrazionamento, String flgFraz)throws Exception;
}
