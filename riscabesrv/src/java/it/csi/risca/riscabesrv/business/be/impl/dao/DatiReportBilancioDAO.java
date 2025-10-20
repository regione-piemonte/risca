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
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;

/**
 * The interface ReportAmbienteDAO.
 *
 * @author CSI PIEMONTE
 */
public interface DatiReportBilancioDAO extends BaseRiscaBeSrvDAO {

	public List<OutputDatiDTO> getDettagliBilAcc(Integer anno) throws DAOException;
	
	public List<OutputDatiDTO> getAltriPagamenti(Integer anno) throws DAOException;
	
	public List<OutputDatiDTO> getCanoniEMonetizzazioni(Integer anno) throws DAOException;
	
	public List<OutputDatiDTO> getEccedenze(Integer anno) throws DAOException;
	
	public OutputDatiDTO getCompensazioni(Integer anno) throws DAOException;
	
	public OutputDatiDTO getRimborsi(Integer anno) throws DAOException;
	
	public List<OutputDatiDTO> getNonIdentificatiDaAssegnare(Integer anno) throws DAOException;
	
	public List<OutputDatiDTO> getInteressi(Integer anno) throws DAOException;
	
	public List<OutputDatiDTO> getSpeseNotifica(Integer anno) throws DAOException;
	
	public List<OutputDatiDTO> getPagamentiNonDiCompetenza(Integer anno) throws DAOException;
	
	public List<OutputDatiDTO> getPagamentiDaRimborsare(Integer anno) throws DAOException;
}
