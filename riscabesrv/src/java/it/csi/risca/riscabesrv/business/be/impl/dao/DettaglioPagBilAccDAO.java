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
import it.csi.risca.riscabesrv.dto.DettaglioPagBilAccDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.SommaImpAssegnatoTipoUsoSdDTO;

/**
 * The interface DettaglioPagBilAcc DAO.
 *
 * @author CSI PIEMONTE
 */
public interface DettaglioPagBilAccDAO {

	/**
	 * 
	 * @param dettaglioPagBilAccDTO
	 * @return
	 * @throws Exception
	 */
	DettaglioPagBilAccDTO saveDettaglioPagBilAcc(DettaglioPagBilAccDTO dettaglioPagBilAccDTO) throws Exception;

	/**
	 * 
	 * @param idDettaglioPag
	 * @return
	 * @throws DAOException
	 */
	Integer deleteDettaglioPagBilAccByIdDettaglioPag(Long idDettaglioPag) throws DAOException;

	Integer deleteDettaglioPagBilAccByIdPagamento(Long idPagamento) throws DAOException;

	Integer deleteDettaglioPagBilAccByIdRataSd(Long idRataSd) throws DAOException;

	List<DettaglioPagBilAccDTO> getDettaglioPagBilAccByIdRataSd(Long idRataSd) throws DAOException;

	Integer deleteDettaglioPagBilAccByListaIdPagamento(List<Long> listIdPagamento) throws DAOException;

	Integer deleteDettaglioPagBilAccByListaIdRataSd(List<Long> listIdRataSd) throws DAOException;

	SommaImpAssegnatoTipoUsoSdDTO getSommaImpAssegnatoTipoUso(Long idRataSd, Long idAccertaBilancio)
			throws DAOException;
}
