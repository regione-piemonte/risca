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
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.RicercaPagamentiDaVisionareDTO;
import it.csi.risca.riscabesrv.dto.SommaCanoneTipoUsoSdDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.UtRimbDTO;
import it.csi.risca.riscabesrv.dto.VerifyStatoDebitorioDTO;

/**
 * The interface Stato Debitorio DAO
 *
 * @author CSI PIEMONTE
 */
public interface StatoDebitorioDAO extends BaseRiscaBeSrvDAO {

	/**
	 * Save stato debitorio.
	 *
	 * @param dto StatoDebitorioDTO
	 * @return StatoDebitorioDTO
	 */
	StatoDebitorioExtendedDTO saveStatoDebitorio(StatoDebitorioExtendedDTO dto, Long idAmbito) throws DAOException;

	/**
	 * update stato debitorio.
	 *
	 * @param dto StatoDebitorioDTO
	 * @return StatoDebitorioDTO
	 */
	StatoDebitorioExtendedDTO updateStatoDebitorio(StatoDebitorioExtendedDTO dto, Long idAmbito) throws DAOException;

	/**
	 * Save stato debitorio working.
	 *
	 * @param dto StatoDebitorioDTO
	 * @return StatoDebitorioDTO
	 */
	StatoDebitorioExtendedDTO saveStatoDebitorioWorking(StatoDebitorioExtendedDTO dto, boolean generateId) throws DAOException;

	/**
	 * Inserisce nella tabella RISCA_W_STATO_DEBITORIO_UPD, prendendoli dalla
	 * tabella RISCA_T_STATO_DEBITORIO, gli stati debitori legati a Rimborsi "Da
	 * compensare"
	 *
	 * @return Integer
	 */
	Integer saveStatiDebitoriUpdDaCompensare(Long idElabora, Long idAmbito) throws DAOException;

	Integer updateStatoDebitorioUpdRegolarizzato(Long idStatoDebitorio, Long idElabora) throws DAOException;

	/**
	 * Aggiorna la compensazione applicata (imp_compens_canone), per lo stato
	 * debitorio idStatoDebitorio
	 * 
	 * @param idStatoDebitorio
	 * @param compensCorente
	 * @return
	 */
	Integer updateStatoDebitorioWorkingCompensazione(Long idStatoDebitorio, BigDecimal compensCorrente)
			throws DAOException;

	List<StatoDebitorioExtendedDTO> loadAllStatoDebitorioOrByIdRiscossione(Long idRiscossione, Integer offset,Integer limit, String sort) throws DAOException;


	StatoDebitorioExtendedDTO loadStatoDebitorioByIdStatoDebitorio(Long idStatoDebitorio)throws DAOException;

	StatoDebitorioDTO loadStatoDebitorioLightById(Long idStatoDebitorio)throws DAOException;

	VerifyStatoDebitorioDTO verifyStatoDebitorio(String modalita, String fruitore, String flgUpDataScadenza, StatoDebitorioExtendedDTO statoDebitorio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException;

	/**
	 * loadStatoDebitorioByIdRimborso
	 *
	 * @param idRimborso
	 * @return List<UtRimbDTO>
	 */

	List<UtRimbDTO> loadStatoDebitorioByIdRimborso(Long idRimborso) throws DAOException;

	/**
	 * loadStatoDebitorioByIdRimborso
	 *
	 * @param nap
	 * @param sdDaEscludere 
	 * @return List<StatoDebitorioExtendedDTO>
	 */

	List<StatoDebitorioExtendedDTO> loadAllStatoDebitorioByNap(String nap, List<Integer> sdDaEscludere , Integer offset,Integer limit, String sort) throws DAOException;


	StatoDebitorioExtendedDTO loadStatoDebitorioAnnoPrecedente(Long idRiscossione) throws DAOException;

	Integer updateNotaRinnovoStatoDebitorioWorking(Long idStatoDebitorio, String notaRinnovo) throws DAOException;
	
	Integer updateStatoDebitorioFromStatoDebitorioUpd(String attore, Long idElabora) throws DAOException;
	
	List<StatoDebitorioDTO> loadStatoDebitorioWorkingByNap(String nap) throws DAOException;
	
	Integer updateStatoDebitorioFromStatoDebitorioWorking(StatoDebitorioDTO sdWorkingDto, String attore) throws DAOException;

	Integer deleteStatoDebitorioWorkingByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException;
	
	Integer deleteStatoDebitorioUpdWorkingByIdElabora(Long idElabora) throws DAOException;
	

	Integer	 verifyStatoDebitorioInvioSpeciale(Long idRiscossione, Long idStatoDebitorio )throws DAOException;
	
	/**
	 * countAllStatoDebitorioOrByIdRiscossione
	 *
	 * @param idRiscossione
	 * @return Integer
	 */
	Integer countAllStatoDebitorioOrByIdRiscossione(Long idRiscossione)throws DAOException;
	/**
	 * countAllStatoDebitorioByNap
	 *
	 * @param nap
	 * @param sdDaEscludere 
	 * @return Integer
	 */
	Integer countAllStatoDebitorioByNap(String nap, List<Integer> sdDaEscludere)throws DAOException; 

	
	Integer updateAttivitaStatoDebitorio(Long idStatoDebitorio) throws DAOException;
	
	Integer copyStatoDebitorioFromWorkingByNap(String nap, String attore) throws DAOException;
	
	/**
	 * loadAllStatoDebitorioByCodUtenza
	 *
	 * @param codUtenza
	 * @param sdDaEscludere 
	 * @return List<StatoDebitorioExtendedDTO> 
	 */
	List<StatoDebitorioExtendedDTO> loadAllStatoDebitorioByCodUtenza(String codUtenza, List<Integer> sdDaEscludere, Integer offset, Integer limit,
			String sort) throws DAOException;
	
	/**
	 * countAllStatoDebitorioByCodUtenza
	 *
	 * @param codUtenza
	 * @param sdDaEscludere 
	 * @return Integer
	 */
	Integer countAllStatoDebitorioByCodUtenza(String codUtenza,  List<Integer> sdDaEscludere)throws DAOException;
	/**
	 * updateAttivitaForAllStatoDebitori
	 *
	 * @param List<StatoDebitorioExtendedDTO> statoDebitorio
	 * 
	 */
	void updateAttivitaForAllStatoDebitori(List<StatoDebitorioExtendedDTO> statoDebitorio, String idAttivitaStatoDeb) throws DAOException; 
	
	Integer countStatoDebitorioBySoggettoTipoAttivita(Long idSoggetto, Long idGruppoSoggetto, Long idAttivitaStatoDeb) throws DAOException;

	List<StatoDebitorioExtendedDTO> loadAllStatoDebitorio(Integer offset, Integer limit, String sort)throws DAOException;

	Integer countAllStatoDebitorio()throws DAOException;

	void updateStatoContribuzioneForStatoDebitorio(Long idStatoContribuzione, Long idStatoDebitorio, String fruitore)throws DAOException;

	Integer updateSpeseNotificaStatoDebitorio(Long idStatoDebitorio, BigDecimal impSpeseNotifica, String attore) throws DAOException;
	
	BigDecimal sommaAllCanoneDovutoByNap(String nap) throws DAOException;
	
	List<StatoDebitorioExtendedDTO> loadStatiDebitoriModificatiOggi(Integer offset, Integer limit, String sort)throws DAOException;

	StatoDebitorioExtendedDTO loadStatoDebitorioByIdRata(Long idRata)throws DAOException;

	List<StatoDebitorioExtendedDTO> loadStatiDebitoriPerPagamentiDaVisionare(
			RicercaPagamentiDaVisionareDTO ricercaPagamentiDaVisionareDTO, Long idAmbito, Integer offset, Integer limit,
			String sort)throws DAOException;

	Integer countStatiDebitoriPerPagamentiDaVisionare(RicercaPagamentiDaVisionareDTO ricercaPagamentiDaVisionareDTO,
			Long idAmbito) throws DAOException;


	public StatoDebitorioExtendedDTO findStatoDebitorioForM50ByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException, SystemException;

	List<SommaCanoneTipoUsoSdDTO> loadSommaCanonePerTipoUsoAnnualitaSd(Long idStatoDebitorio) throws DAOException;

	public Integer countStatoDebitorioByIdRecapito(Long idRecapito)throws DAOException;

}
