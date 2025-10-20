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

import java.math.BigDecimal;
import java.util.List;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.dto.AvvisoPagamentoDTO;

/**
 * The interface Avviso Pagamento dao.
 *
 * @author CSI PIEMONTE
 */
public interface AvvisoPagamentoDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Get max progressivo by nap (like)
	 *
	 * @param partialNap partialNap
	 * @return Integer maxProgressivo
	 */
	Integer getMaxProgressivo(String partialNap);
	
	Integer getMaxProgressivoWorking(String partialNap);

	/**
	 * Save avviso di pagamento.
	 *
	 * @param dto AvvisoPagamentoDTO
	 * @return AvvisoPagamentoDTO
	 */
	AvvisoPagamentoDTO saveAvvisoPagamento(AvvisoPagamentoDTO dto) throws DAOException;

	AvvisoPagamentoDTO saveAvvisoPagamentoWorking(AvvisoPagamentoDTO dto) throws DAOException;

	Integer updateWorkingAvvisoPagamentoImpTotaleDovuto(BigDecimal canoneAnnuo, String nap, Long idSpedizione) throws DAOException;

	Integer updateWorkingAvvisoPagamentoCompensazione(BigDecimal compensazione, String nap) throws DAOException;

	Integer deleteWorkingAvvisoPagamentoSenzaRiscDaBollettare() throws DAOException;

	AvvisoPagamentoDTO loadAvvisoPagamentoByIdNap(String nap) throws DAOException;
	
	List<AvvisoPagamentoDTO> loadAvvisoPagamentoWorkingByIdSpedizione(Long idSpedizione);
	
	Integer copyAvvisoPagamentoFromWorkingByNap(String nap, Long idSpedizione, String attore) throws DAOException;
	
	Integer deleteAvvisoPagamentoWorkingByNap(String nap) throws DAOException;
}
