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

import it.csi.risca.riscabesrv.dto.TipoElaboraExtendedDTO;

public interface TipiElaborazioneDAO {

    List<TipoElaboraExtendedDTO> loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile(Long idAmbito, Long idFunzionalita, Integer flgVisible) throws Exception;
}
