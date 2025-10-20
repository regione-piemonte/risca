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
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoRicercaRimborsoDTO;

/**
 * The interface TipoRicercaRimborsiDAO
 *
 * @author CSI PIEMONTE
 */
public interface TipoRicercaRimborsiDAO {

	
	 List<TipoRicercaRimborsoDTO>  loadAllTipoRicercaRimborsi() throws DAOException;

	List<StatoDebitorioExtendedDTO> ricercaRimborsi(String tipoRicercaRimborsi,Long idAmbito, Integer anno, Integer offset,
			Integer limit, String sort) throws Exception;

	Integer countRicercaRimborsi(String tipoRicercaRimborsi,Long idAmbito, Integer anno) throws Exception;
}
