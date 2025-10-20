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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;

public interface RecapitoPostelDAO {
    
    public List<IndirizzoSpedizioneDTO> getRecapitoPostelByIdRecapitoAndIdGruppo(Long idRecapito,
			List<Long> listIdGruppoSoggetto) throws DAOException;
    
    public void deleteRecapitoAlternativoPostelByIdRecapito(Long idRecapito) throws DAOException;
    
    public void deleteRecapitoAlternativoPostelByIdSoggetto(Long idSoggetto) throws DAOException;
}
