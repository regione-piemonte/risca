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

import it.csi.risca.riscabesrv.dto.RpNonPremarcatiDTO;

/**
 * The interface RpNonPremarcati dao.
 *
 * @author CSI PIEMONTE
 */
public interface RpNonPremarcatiDAO extends BaseRiscaBeSrvDAO {

	RpNonPremarcatiDTO saveRpNonPremarcati(RpNonPremarcatiDTO dto);

	List<RpNonPremarcatiDTO> loadRpNonPremarcatiByElaboraTipoDoc(Long idElabora, Long idFilePoste,
			List<String> tipoDoc);

	Integer deleteRpNonPremarcati(Long idElabora);

}
