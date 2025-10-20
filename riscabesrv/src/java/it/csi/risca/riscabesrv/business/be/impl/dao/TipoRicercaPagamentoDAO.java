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

import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RicercaPagamentoDTO;
import it.csi.risca.riscabesrv.dto.TipoRicercaPagamentoDTO;

/**
 * The interface TipoRicercaPagamentoDAO
 *
 * @author CSI PIEMONTE
 */
public interface TipoRicercaPagamentoDAO {
	
	
    /**
     * loadAllTipiRicercaPagamenti.
     * 
     * @return List<TipoRicercaPagamentoDTO> list
     * @throws Exception 
     */
	List<TipoRicercaPagamentoDTO> loadAllTipiRicercaPagamenti();
	
	  /**
     * ricercaPagamenti.
     * 
     * @return List<PagamentoExtendedDTO> list
     * @throws Exception 
     */
	List<PagamentoExtendedDTO> ricercaPagamenti(RicercaPagamentoDTO ricercaPagamentoDTO, Integer offset, Integer limit,
			String sort) throws Exception;
	  /**
     * countPagamenti.
     * 
     * @return Integer number
     * @throws Exception 
     */
	Integer countPagamenti(RicercaPagamentoDTO ricercaPagamentoDTO) throws Exception;

}
