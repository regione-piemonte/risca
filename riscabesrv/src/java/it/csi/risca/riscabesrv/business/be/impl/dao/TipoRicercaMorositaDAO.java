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
import it.csi.risca.riscabesrv.dto.TipoRicercaMorositaDTO;

/**
 * The interface TipoRicercaMorositaDAO
 *
 * @author CSI PIEMONTE
 */
public interface TipoRicercaMorositaDAO {

	 List<TipoRicercaMorositaDTO>  loadAllTipoRicercaMorosita() throws DAOException;

	List<StatoDebitorioExtendedDTO> ricercaMorosita(String tipoRicercaMorosita,Long idAmbito, Integer anno, Integer flgRest,
			Integer flgAnn, String lim, Integer offset, Integer limit, String sort)throws Exception;

	Integer countRicercaMorosita(String tipoRicercaMorosita,Long idAmbito, Integer anno, Integer flgRest, Integer flgAnn, String lim);
}
