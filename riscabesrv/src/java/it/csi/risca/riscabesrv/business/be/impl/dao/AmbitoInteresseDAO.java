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

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.AmbitoInteresseDTO;

/**
 * The interface Tipo autorizzazione dao.
 *
 * @author CSI PIEMONTE
 */
public interface AmbitoInteresseDAO extends BaseRiscaBeSrvDAO{
	/**
	 * get Ambito Interesse By Date And Tipo Interesse.
	 *
	 * @param  idAmbito, tipoInteressi , dataInizio,  dataFine
	 * @return 	List<AmbitoInteresseExtendedDTO>
	 */
	
	List<AmbitoInteresseDTO> getAmbitoInteresseByDateAndTipo(Long idAmbito,String tipoInteresse, Date dataInizio,  Date dataFine) throws DataAccessException, SQLException;

	
	List<AmbitoInteresseDTO> getAmbitoInteresseByDateInizioAndTipo(Long idAmbito,String tipoInteresse,   Date dataFine) throws DataAccessException, SQLException;

	
	List<AmbitoInteresseDTO> getAmbitoInteresseByDateFineAndTipo(Long idAmbito,String tipoInteresse,  Date dataFine) throws DataAccessException, SQLException;

}
