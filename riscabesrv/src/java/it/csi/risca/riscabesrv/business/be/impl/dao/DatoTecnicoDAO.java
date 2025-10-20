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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.dto.DTGrandeIdroDTO;
import it.csi.risca.riscabesrv.dto.ambiente.RiscossioneDatoTecnicoDTO;

/**
 * The interface Dato tecnico dao.
 *
 * @author CSI PIEMONTE
 */
public interface DatoTecnicoDAO {

    /**
     * Save dato tecnico long.
     *
     * @param dto DatoTecnicoDTO
     * @return id_riscossione long
     * @throws Exception 
     */
	RiscossioneDatoTecnicoDTO saveDatoTecnico(RiscossioneDatoTecnicoDTO dto, String codFisc, Long idAmbito) throws Exception;
    /**
     * updateDTGrandeIdro.
     *
     * @param idRiscossioni idRiscossioni
     * @param datiTerna datiTerna
     * @return id_riscossione long
     * @throws Exception 
     */
	RiscossioneDatoTecnicoDTO updateDTGrandeIdro(Integer idRiscossioni, DTGrandeIdroDTO dTGrandeIdroDTO,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws BusinessException, Exception ;
}
