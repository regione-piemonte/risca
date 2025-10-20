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
import java.text.ParseException;
import java.util.List;

import it.csi.risca.riscabesrv.dto.RiduzioneAumentoDTO;

/**
 * The interface Riduzione Aumento dao.
 *
 * @author CSI PIEMONTE
 */
public interface RiduzioneAumentoDAO {
	

    /**
     * Load riduzione aumento list.
     *
     * @return List<RiduzioneAumentoDTO> list
     */
    List<RiduzioneAumentoDTO> loadRiduzioneAumento();

    /**
     * Load riduzione aumento list by id ambito.
     *
     * @param idAmbito idAmbito
     * @return List<RiduzioneAumentoDTO> list
     * @throws SQLException 
     */
    List<RiduzioneAumentoDTO> loadRiduzioneAumentoByIdAmbito(Long idAmbito) throws SQLException;
    
    /**
     * Load riduzione aumento by IdTipoUso
     *
     * @param IdTipoUso IdTipoUso
     * @return List<RiduzioneAumentoDTO> list
     * @throws SQLException 
     */
    List<RiduzioneAumentoDTO> loadRiduzioneAumentoByIdTipoUso(Long IdTipoUso) throws SQLException;

    /**
     * Load riduzione aumento by IdTipoUso and flgRidAum
     *
     * @param IdTipoUso IdTipoUso
     * @param flgRidAum flgRidAum
     * @return List<RiduzioneAumentoDTO> list
     * @throws SQLException 
     */
    List<RiduzioneAumentoDTO> loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum(Long IdTipoUso, String flgRidAum) throws SQLException;
    
    /**
     * Load riduzione aumento by id riduzione aumento
     *
     * @param IdRiduzioneAumento IdRiduzioneAumento
     * @return RiduzioneAumentoDTO
     * @throws SQLException 
     */
    RiduzioneAumentoDTO loadRiduzioneAumentoByIdRiduzioneAumento(Long IdRiduzioneAumento) throws SQLException;
    /**
     * Load riduzione aumento by id Or Cod riduzione aumento
     *
     * @param idOrCodRiduzioneAumento idOrCodRiduzioneAumento
     * @return RiduzioneAumentoDTO
     * @throws SQLException 
     * @throws ParseException 
     */
	RiduzioneAumentoDTO loadRiduzioniByIdOrCodRiduzioneAumento(String idOrCodRiduzioneAumento, String dataIniVal,
			String dataFineVal) throws SQLException, ParseException;

    /**
     * Load riduzioni by Id Or Cod Tipo Uso and Flg Manuale e date ininzio e fine validita
     *
     * @param idOrCodTipoUso idOrCodTipoUso
     * @return RiduzioneAumentoDTO
     * @throws SQLException 
     */
	List<RiduzioneAumentoDTO> loadRiduzioniByIdOrCodTipoUsoFlgManuale(String idOrCodTipoUso, String flgManuale,
			String dataIniVal, String dataFineVal) throws SQLException, ParseException;
	
    /**
     * Load aumenti by Id Or Cod Tipo Uso and Flg Manuale e date ininzio e fine validita
     *
     * @param idOrCodTipoUso idOrCodTipoUso
     * @return RiduzioneAumentoDTO
     */
	List<RiduzioneAumentoDTO> loadAumentiByIdOrCodTipoUsoFlgManuale(String idOrCodTipoUso, String flgManuale,
			String dataIniVal, String dataFineVal) throws SQLException, ParseException;
    /**
     * Load riduzioni by Flg Manuale e date ininzio e fine validita
     *
     * @param flgManuale flgManuale
     * @return RiduzioneAumentoDTO
     * @throws SQLException 
     * @throws ParseException 
     */
	List<RiduzioneAumentoDTO> loadRiduzioniByflgManuale(String flgManuale, String dataIniVal, String dataFineVal) throws SQLException, ParseException;
    /**
     * Load Aumenti by Flg Manuale e date ininzio e fine validita
     *
     * @param flgManuale flgManuale
     * @return RiduzioneAumentoDTO
     * @throws SQLException 
     * @throws ParseException 
     */
	List<RiduzioneAumentoDTO> loadAumentiByflgManuale(String flgManuale, String dataIniVal, String dataFineVal) throws SQLException, ParseException;


	/**
     * Load Riduzioni/Aumenti by idAnnualitaUsoSd
     *
     * @param idAnnualitaSd idAnnualitaUsoSd
     * @return RiduzioneAumentoDTO
	 * @throws SQLException 
     */
	List<RiduzioneAumentoDTO> loadRiduzioneAumentoByAnnualitaUsoSd(Long idAnnualitaUsoSd) throws SQLException;
	
	/**
     * Load Riduzioni/Aumenti by riscossione e tipo uso
     *
     * @param idRiscossione idRiscossione
     * @param idTipoUso idTipoUso
     * @return RiduzioneAumentoDTO
	 * @throws SQLException 
     */
	List<RiduzioneAumentoDTO> loadRiduzioneAumentoByRiscossioneTipoUso(Long idRiscossione, String codTipoUso) throws SQLException;
	
	


}
