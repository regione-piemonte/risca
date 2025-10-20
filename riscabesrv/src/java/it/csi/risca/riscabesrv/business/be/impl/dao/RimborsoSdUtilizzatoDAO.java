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
import it.csi.risca.riscabesrv.dto.RimborsoSdUtilizzatoDTO;

/**
 * The interface RimborsoSdUtilizzato DAO
 *
 * @author CSI PIEMONTE
 */
public interface RimborsoSdUtilizzatoDAO extends BaseRiscaBeSrvDAO {

	/**
	 * 
	 * @param dto
	 * @return
	 * @throws DAOException
	 */
	RimborsoSdUtilizzatoDTO saveRimborsoSdUtilizzatoWorking(RimborsoSdUtilizzatoDTO dto, Long idElabora) throws DAOException;
	
	/**
	 * 
	 * @param id
	 * @return RimborsoSdUtilizzatoDTO
	 * @throws DAOException
	 */
	RimborsoSdUtilizzatoDTO getRimborsoSdUtilizzatoByIdStatoDebitorio(Long id) throws DAOException;
	/**
	 * 
	 * @param dto
	 * @return
	 * @throws DAOException
	 */
	RimborsoSdUtilizzatoDTO saveRimborsoSdUtilizzato(RimborsoSdUtilizzatoDTO dto) throws DAOException;
	
	Integer insertRimborsoSdUtilizzatoFromWorking(Long idStatoDebitorio, String attore, Long idElabora) throws DAOException;
	
	Integer deleteRimborsoSdUtilizzatoWorkingByIdStatoDebitorio(Long idStatoDebitorio, Long idElabora) throws DAOException;
	
	/**
	 * 
	 * @param List<Long> listIdRimborsi
	 * @return List<RimborsoSdUtilizzatoDTO>
	 * @throws DAOException
	 */
	List<RimborsoSdUtilizzatoDTO> getRimborsoSdUtilizzatoByIdRimborsi(List<Long> listIdRimborsi) throws DAOException;
	
	
}
