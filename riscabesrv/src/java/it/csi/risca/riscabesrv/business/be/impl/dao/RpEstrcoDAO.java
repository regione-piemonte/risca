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

import it.csi.risca.riscabesrv.dto.RpEstrcoDTO;

/**
 * The interface RpEstrco dao.
 *
 * @author CSI PIEMONTE
 */
public interface RpEstrcoDAO extends BaseRiscaBeSrvDAO {

	RpEstrcoDTO saveRpEstrco(RpEstrcoDTO dto);

	List<RpEstrcoDTO> loadRpEstrcoByElaboraNumeroConto(Long idElabora, Long idFilePoste, String numeroConto);

	Integer deleteRpEstrco(Long idElabora);
}
