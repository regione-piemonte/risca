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
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;

public interface RecapitoDAO extends BaseRiscaBeSrvDAO  {
	
	public RecapitiExtendedDTO getRecapitiExtendedByIdRecapito(long idRecapito);

	public RecapitiExtendedDTO createRecapito(RecapitiExtendedDTO recapito)throws DAOException, SystemException ;

	public long countRecapitoPrincipaleByIdSoggetto(Long idSoggetto)throws DAOException, SystemException;

	public RecapitiExtendedDTO updateRecapito(RecapitiExtendedDTO recapito)throws DAOException, SystemException ;

	public long countRecapitoByIdSoggettoAndIdRecapito(Long idSoggetto, Long idRecapito) throws DAOException, SystemException;
	
	public List<RecapitiExtendedDTO> getRecapitiExtendedByIdSoggetto(long idSoggetto) throws DAOException;
	
	public void deleteRecapitiByIdSoggetto(long idSoggetto) throws DAOException;
	
	public void deleteRecapitoAlternativoByIdRecapito(long idRecapito) throws DAOException;
   
}
