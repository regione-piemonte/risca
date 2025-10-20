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

import it.csi.risca.riscabesrv.dto.PagNonPropriExtendedDTO;

/**
 * The interface Pag Non Propri DAO .
 *
 * @author CSI PIEMONTE
 */
public interface PagNonPropriDAO {
	
    /**
     * save PagNonPropri.
     *
     * @param PagNonPropriExtendedDTO
     * @return PagNonPropriExtendedDTO
     * @throws Exception 
     */
	PagNonPropriExtendedDTO savePagNonPropri(PagNonPropriExtendedDTO pagNonPropriExtendedDTO) throws Exception;
	
    /**
     * update PagNonPropri.
     *
     * @param PagNonPropriExtendedDTO
     * @return PagNonPropriExtendedDTO
     * @throws Exception 
     */
	PagNonPropriExtendedDTO updatePagNonPropri(PagNonPropriExtendedDTO pagNonPropriExtendedDTO) throws Exception;
	
	
    /**
     * delete PagNonPropri.
     *
     * @param idPagamento
     * @throws Exception 
     */
	void deletePagNonPropriByIdPagamento(Long idPagamento) throws Exception;

}
