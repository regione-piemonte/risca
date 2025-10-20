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
import it.csi.risca.riscabesrv.dto.AccertamentoDTO;
import it.csi.risca.riscabesrv.dto.AccertamentoExtendedDTO;

/**
 * The interface Accertamenti DAO
 *
 * @author CSI PIEMONTE
 */
public interface AccertamentoDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save accertamento.
	 *
	 * @param dto AccertamentoExtendedDTO
	 * @return AccertamentoExtendedDTO
	 */
	AccertamentoExtendedDTO saveAccertamenti(AccertamentoExtendedDTO dto) throws DAOException;

	/**
	 * update accertamento.
	 *
	 * @param dto AccertamentoExtendedDTO
	 * @return AccertamentoExtendedDTO
	 */
	AccertamentoExtendedDTO updateAccertamenti(AccertamentoExtendedDTO dto) throws DAOException;



	List<AccertamentoExtendedDTO> loadAllAccertamentiOrByIdStatoDeb(Long idStatoDebitorio, Integer offset,Integer limit, String sort) throws DAOException;
	
	/**
	 * delete all accertamento by id Stato debitorio.
	 */
	void  deleteAccertamentoByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException;
	
	/**
	 * delete accertamento by idAccertamento.
	 */
	void  deleteAccertamentoById(Long idAccertamento) throws DAOException;

	/**
	 * Save accertamento working.
	 *
	 * @param dto AccertamentoDTO
	 * @return AccertamentoDTO
	 */
	AccertamentoDTO saveAccertamentoWorking(AccertamentoDTO dto) throws DAOException;
	
	/**
	 * delete all accertamento by id Stato debitorio.
	 */
	void  deleteAccertamentoWorkingByIdElabora(Long idElabora) throws DAOException;

	Integer copyAccertamentoFromWorking(Long idElabora, String attore) throws DAOException;
	
	public String findMinDataProtocolloByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException, SystemException;
	public AccertamentoDTO findMaxDataAndNumProtocolloByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException, SystemException;
	public AccertamentoExtendedDTO loadAccertamentoByIdAccertamento(Long idAccertamento) throws DAOException;

}
