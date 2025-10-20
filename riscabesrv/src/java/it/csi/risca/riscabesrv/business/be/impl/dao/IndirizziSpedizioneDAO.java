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

import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;

/**
 * The interface Indirizzi Spedizione dao.
 *
 * @author CSI PIEMONTE
 */
public interface IndirizziSpedizioneDAO {
	
    /**
     * save Indirizzi Spedizione .
     *
     * @return IndirizzoSpedizioneDTO indirizzo
     * @throws Exception 
     */
    IndirizzoSpedizioneDTO saveIndirizziSpedizione(IndirizzoSpedizioneDTO indSped) throws Exception;
    
    IndirizzoSpedizioneDTO updateIndirizziSpedizione(IndirizzoSpedizioneDTO indSped, Long modVerifica) throws GenericExceptionList;
    
    List<IndirizzoSpedizioneDTO> getIndirizzoSpedizioneByIdRecapitoAndIdGruppo(Long idRecapito, List<Long> listIdGruppoSoggetto);

    List<IndirizzoSpedizioneDTO> loadIndirizziSpedizioneByIdGruppoSoggetto(Long idGruppoSoggetto);

}
