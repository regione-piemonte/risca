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

import it.csi.risca.riscabesrv.dto.DelegatoDTO;
import it.csi.risca.riscabesrv.dto.DelegatoExtendedDTO;
/**
 * The interfaceDelegheDAO.
 *
 * @author CSI PIEMONTE
 */
public interface DelegheDAO {


    /**
     * loadDelegheByCf.
     *
     * @param  codiceFiscale
     * @return DelegatoDTO 
     * @throws Exception 
     */
	 DelegatoDTO loadDelegheByCf(String codiceFiscale) throws Exception;

	DelegatoExtendedDTO saveDelegato(DelegatoExtendedDTO dto)throws Exception;

}
