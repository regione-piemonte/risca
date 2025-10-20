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
import it.csi.risca.riscabesrv.dto.AvvisoDatiTitolareDTO;

/**
 * The interface Avviso Dati Titolare dao.
 *
 * @author CSI PIEMONTE
 */
public interface AvvisoDatiTitolareDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save avviso dati del titolare.
	 *
	 * @param dto AvvisoDatiTitolareDTO
	 * @return AvvisoDatiTitolareDTO
	 */
	AvvisoDatiTitolareDTO saveAvvisoDatiTitolare(AvvisoDatiTitolareDTO dto) throws DAOException;

	AvvisoDatiTitolareDTO saveAvvisoDatiTitolareWorking(AvvisoDatiTitolareDTO dto) throws DAOException;

	Integer updateWorkingDatiTitolareImportoDaVersare(BigDecimal canoneAnnuo, int annualitaPagamento, String nap)
			throws DAOException;

	Integer updateWorkingDatiTitolareDilazioneScandenza(String dilazione, String scadenzaPagamento2, String nap)
			throws DAOException;

	Integer updateWorkingDatiTitolareCompensazione(BigDecimal compensazione, String nap) throws DAOException;

	List<AvvisoDatiTitolareDTO> loadAvvisoDatiTitolareWorkingBySpedizione(Long idSpedizione);

	Integer deleteWorkingDatiTitolareSenzaRiscDaBollettare() throws DAOException;

	Integer updateWorkingDatiTitolareNumUtenze() throws DAOException;

	Integer copyAvvisoDatiTitolareFromWorkingByNap(String nap, String attore) throws DAOException;

	Integer deleteAvvisoDatiTitolarenWorkingByNap(String nap) throws DAOException;

	AvvisoDatiTitolareDTO loadAvvisoDatiTitolareByNap(String nap);

}
