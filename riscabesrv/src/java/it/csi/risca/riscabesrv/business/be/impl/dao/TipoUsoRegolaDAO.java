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

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaExtendedDTO;

/**
 * The interface TipoUsoRegola dao.
 *
 * @author CSI PIEMONTE
 */
public interface TipoUsoRegolaDAO {

	/**
	 * Carica un oggetto TipoUsoRegolaDTO basato sul codice del tipo uso specificato.
	 * @param dataRiferimento La data di riferimento per il caricamento.
	 * @param codTipoUso Il codice del tipo uso.
	 * @return Il TipoUsoRegolaDTO corrispondente al codice del tipo uso specificato.
	 */
	TipoUsoRegolaDTO loadTipoUsoRegolaByCodTipoUso(String dataRiferimento, String codTipoUso);

	/**
	 * Carica tutti gli anni a partire dalla data di inizio dato un determinato idAmbito.
	 * @param idAmbito L'identificatore dell'ambito.
	 * @return Una lista di anni a partire dalla data di inizio.
	 * @throws Exception Lanciata se si verifica un'eccezione generica.
	 * @throws BusinessException Lanciata se si verifica un errore di business.
	 */
	List<Integer> loadAllAnniFromDTInizio(Integer idAmbito) throws Exception, BusinessException;

	/**
	 * Carica tutti gli oggetti TipoUsoRegolaExtendedDTO basati sull'id dell'ambito e sull'anno specificati.
	 * @param idAmbito L'identificatore dell'ambito.
	 * @param anno L'anno.
	 * @return Una lista di TipoUsoRegolaExtendedDTO corrispondenti all'id dell'ambito e all'anno specificati.
	 * @throws Exception Lanciata se si verifica un'eccezione generica.
	 * @throws BusinessException Lanciata se si verifica un errore di business.
	 */
	List<TipoUsoRegolaExtendedDTO> loadAllUsoRegolaByIdAmbitoAndAnno(Integer idAmbito, Integer anno,Integer offset,Integer limit,String sort) throws Exception, BusinessException;

	/**
	 * update un oggetto TipoUsoRegolaExtendedDTO.
	 * @param tipoUsoRegola L'oggetto TipoUsoRegolaExtendedDTO da aggiornare.
	 * @return Il TipoUsoRegolaExtendedDTO salvato.
	 * @throws Exception Lanciata se si verifica un'eccezione generica.
	 */
	TipoUsoRegolaExtendedDTO updateTipoUsoRegola(TipoUsoRegolaExtendedDTO tipoUsoRegola) throws Exception;
	
	/**
	 * Conta il numero totale di oggetti TipoUsoRegolaExtendedDTO basati sull'ID dell'ambito e sull'anno specificati.
	 * 
	 * @param idAmbito L'identificatore dell'ambito.
	 * @param anno L'anno di riferimento.
	 * @return Il numero totale di oggetti TipoUsoRegolaExtendedDTO corrispondenti all'ID dell'ambito e all'anno specificati.
     * @throws Exception Lanciata se si verifica un'eccezione generica.
	 */
	Integer countAllUsoRegolaByIdAmbitoAndAnno(Integer idAmbito, Integer anno) throws Exception;


	/**
	 * Carica tutti gli oggetti TipoUsoRegolaExtendedDTO basati sull'id dell'ambito e data fine null.
	 * @param idAmbito L'identificatore dell'ambito.
	 * @return Una lista di TipoUsoRegolaExtendedDTO corrispondenti all'id dell'ambito .
	 * @throws Exception Lanciata se si verifica un'eccezione generica.
	 * @throws BusinessException Lanciata se si verifica un errore di business.
	 */
	List<TipoUsoRegolaExtendedDTO> loadAllUsoRegolaByIdAmbitoAndDataFineIsNull(Integer idAmbito) throws Exception, BusinessException;


	/**
	 * updateDataFineTipoUsoRegola.
	 * @param cf 
	 * @param List<Long> idTipoUsoReogla da aggiornare.
	 * @param Integer anno.
	 * @throws Exception Lanciata se si verifica un'eccezione generica.
	 */
	void updateDataFineTipoUsoRegola(List<Long> idTipoUsoReogla, Integer anno, String cf) throws Exception;
	
	/**
	 * save un oggetto TipoUsoRegolaExtendedDTO.
	 * @param tipoUsoRegola L'oggetto TipoUsoRegolaExtendedDTO da salvare.
	 * @return Il TipoUsoRegolaExtendedDTO salvato.
	 * @throws Exception Lanciata se si verifica un'eccezione generica.
	 */
	TipoUsoRegolaExtendedDTO saveTipoUsoRegola(TipoUsoRegolaExtendedDTO tipoUsoRegola) throws Exception;
	
	/**
	 * Load l' oggetto TipoUsoRegolaExtendedDTO.
	 * @param idTipoUsoRegola .
	 * @return Il TipoUsoRegolaExtendedDTO .
	 * @throws Exception Lanciata se si verifica un'eccezione generica.
	 */
	TipoUsoRegolaExtendedDTO loadTipoUsoRegola(Long idTipoUsoRegola) throws Exception;
	
	
	/**
	 * Verifica se l'anno specificato esiste 
	 * 
	 * @param idAmbito l'idAmbito da verificare
	 * @param anno l'anno da verificare
	 * @return true se l'anno esiste, altrimenti false
	 * @throws Exception se si verifica un errore generico durante il controllo
	 * @throws BusinessException se si verifica un errore specifico del business durante il controllo
	 */
	Boolean checkAnnoExistence(Integer idAmbito, Integer anno) throws Exception, BusinessException;
}
