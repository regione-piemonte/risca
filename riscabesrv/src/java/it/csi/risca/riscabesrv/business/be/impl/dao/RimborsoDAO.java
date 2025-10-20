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
import it.csi.risca.riscabesrv.dto.RimborsoExtendedDTO;

/**
 * The interface Rimborso DAO
 *
 * @author CSI PIEMONTE
 */
public interface RimborsoDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Inserisce nella tabella RISCA_W_RIMBORSO_UPD, prendendoli dalla tabella
	 * RISCA_R_RIMBORSO, tutti i Rimborsi "Da compensare"
	 *
	 * @return Integer
	 */
	Integer saveRimborsiUpdDaCompensare(Long idElabora, Long idAmbito) throws DAOException;

	/**
	 * Cerca i rimborsi da compensare sulle tabelle RISCA_W_RIMBORSO_UPD e
	 * RISCA_W_STATO_DEBITORIO_UPD, per idRiscossione ed idSoggetto oppure solo per
	 * idSoggetto (idRiscossione puo essere null)
	 * 
	 * @param idRiscossione
	 * @param idSoggetto
	 * @param orderBySD
	 * @return List<RimborsoExtendedDTO>
	 */
	List<RimborsoExtendedDTO> loadRimborsiDaCompensareUpd(Long idRiscossione, Long idSoggetto, Long idGruppoSoggetto, boolean orderBySD);

	/**
	 * Imposta il rimborso come 'COMPENSATO' e l'importo restituito para all'importo
	 * del rimborso
	 * 
	 * @param idRimborso
	 * @return
	 * @throws DAOException
	 */
	Integer updateRimborsoUpdCompensato(Long idRimborso, BigDecimal impRimborso, Long idElabora) throws DAOException;

	/**
	 * Aggiorna l'importo restituito con la compensazione
	 * 
	 * @param idRimborso
	 * @param compensazione
	 * @return
	 * @throws DAOException
	 */
	Integer updateRimborsoUpdCompensazione(Long idRimborso, BigDecimal compensazione, Long idElabora) throws DAOException;

	/**
	 * load Rimborsi By Id Stato Debitorio
	 * 
	 * @param idStatoDebitorio
	 * @return List<RimborsoExtendedDTO>
	 */
	List<RimborsoExtendedDTO> loadRimborsiByIdStatoDebitorio(Long idStatoDebitorio);

	/**
	 * Inserisce nella tabella RISCA_R_RIMBORSI,
	 *
	 * @return RimborsoExtendedDTO
	 * @throws Exception 
	 */
	RimborsoExtendedDTO saveRimborso(RimborsoExtendedDTO rimborsoExtendedDTO, Long idAmbito) throws DAOException, Exception;

	/**
	 * UPDATE nella tabella RISCA_R_RIMBORSI,
	 *
	 * @return RimborsoExtendedDTO
	 * @throws Exception 
	 */

	 RimborsoExtendedDTO updateRimborso(RimborsoExtendedDTO rimborsoExtendedDTO) throws DAOException, Exception;
	 
	/**
	 * DELETE nella tabella RISCA_R_RIMBORSI, 
	 *
	 * @return void
	 */
	 void deleteRimborso(Long idStatoDebitorio) throws DAOException;
	 

	/**
	 * Aggiornamento Rimborso dalle tabelle di working a quelle effettive
	 * 
	 * @return
	 * @throws DAOException
	 */
	Integer updateRimborsoFromRimborsoUpd(String attore, Long idElabora) throws DAOException;
	
	Integer deleteRimborsoUpdWorkingByIdElabora(Long idElabora) throws DAOException;

}
