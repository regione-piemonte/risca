/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be;

import java.math.BigDecimal;
import java.sql.SQLException;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;

public interface BusinessLogicFile450 
{

	public String creaM00(String codAmbito, BigDecimal totaleImportoArticoloRuolo)throws SQLException, DAOException, SystemException;
	public String creaM10(String codAmbito, long progressivo) throws SQLException, DAOException, SystemException;
	public String creaM20(String codAmbito, StatoDebitorioExtendedDTO statoDebitorioExtendedDTO, long progressivo, long numeroPartita, String presenzaCointestati) throws SQLException, DAOException, SystemException;
	public String creaM30(String codAmbito, StatoDebitorioExtendedDTO statoDebitorioExtendedDTO, long progressivo, long numeroPartita) throws SQLException, DAOException, SystemException ;
	public String creaM50(String codAmbito, StatoDebitorioExtendedDTO statoDebitorioExtendedDTO, long progressivo,long numeroPartita, long progressivoM50, String codiceEntrata, String tipoCodiceEntrata, BigDecimal importoArticoloRuolo, String numeroAccertamentoBilancio) throws SQLException, DAOException, SystemException;
	public String creaM99(String codAmbito, long totM10, long totM20, long totM30, long totM50 , BigDecimal totaleImportoArticoloRuolo) throws SQLException, DAOException, SystemException;
}
