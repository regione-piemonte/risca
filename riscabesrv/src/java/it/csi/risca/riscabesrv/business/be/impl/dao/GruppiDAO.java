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
import java.util.List;
import java.util.Map;

import it.csi.risca.riscabesrv.dto.GruppiDTO;

/**
 * The interface Gruppi dao.
 *
 * @author CSI PIEMONTE
 */
public interface GruppiDAO {
	
    /**
     * Load gruppi.	
     * @return List<GruppiDTO>
     * @throws Exception 
     */
	List<GruppiDTO> loadGruppiSoggetto() throws Exception;
	
    /**
     * Load gruppo by coddGruppo.
     *
     * @param codGruppo codGruppo
     * @return GruppiDTO
     * @throws SQLException 
     */
	GruppiDTO loadGruppiById(String codGruppo, String desGruppo) throws SQLException;

    /**
     * Save gruppi 
     *
     * @param dto GruppiDTO
     * @return long id_gruppo_soggetto 
     * @throws Exception 
     */
    Long saveGruppi(GruppiDTO dto) throws Exception;
    
    /**
     * Update gruppi
     *
     * @param dto GruppiDTO
     * @return numero record aggiornati
     * @throws Exception 
     */
    Long updateGruppi(GruppiDTO dto) throws Exception;
    
    /**
     * Delete gruppi
     *
     * @param Long idGruppo
     * @return numero record aggiornati
     */
    Long deleteGruppi(Long idGruppo, Boolean confermato);
    
    /**
     * Load gruppi by idAmbito and campoRicerca.	
     * @return List<GruppiDTO>
     */
	List<GruppiDTO>  loadGruppiByIdAmbitoAndCampoRicerca(Long idAmbito, String campoRicerca, String flgTipoRicerca,Integer offset, Integer limit,String sort);


	
	Long findIdSoggettoByIdGruppoSoggetto(Long IdGruppoSoggetto);
	
	   /**
     * count gruppi by idAmbito and campoRicerca.	
     * @return Integer
     */
	Integer  countGruppiByIdAmbitoAndCampoRicerca(Long idAmbito, String campoRicerca, String flgTipoRicerca);
	
    /**
     * loadGruppiSoggettoByIdGruppo.	
     * @return List<GruppiDTO>
     * @throws Exception 
     */
	List<GruppiDTO> loadGruppiSoggettoByIdGruppo(List<Long> idGruppo) throws Exception;

}
