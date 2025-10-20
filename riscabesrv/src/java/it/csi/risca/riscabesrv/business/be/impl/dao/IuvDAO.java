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
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import java.math.BigDecimal;

import it.csi.risca.riscabesrv.dto.IuvDTO;
import it.csi.risca.riscabesrv.dto.IuvExtendedDTO;

/**

 * The interface Iuv dao.
 *
 * @author CSI PIEMONTE
 */
public interface IuvDAO extends BaseRiscaBeSrvDAO {
	
    /**
     * get iuv by nap
     *
     * @return IuvDTO 
     * @throws Exception 
     */
	IuvDTO getIuvByNap(String nap) throws Exception;	
	
	IuvDTO getIuvById(Long idIuv) throws Exception;	
	
	IuvDTO saveIuv(IuvDTO dto) throws Exception;
	
	Integer updateStatoIuvInseritoByNap(String nap);
	
	Integer updateStatoIuvByNap(String nap, String codStatoIuv, String attore);
	
	Integer updateImportoIuvByNap(String nap, BigDecimal importo, String attore);
	
	IuvDTO getIuvByCodiceAvviso(String codiceAvviso);

	List<IuvExtendedDTO> getSumCanoneSpeseAndTotVersatoByNap(String nap) throws DAOException, SystemException;
}
