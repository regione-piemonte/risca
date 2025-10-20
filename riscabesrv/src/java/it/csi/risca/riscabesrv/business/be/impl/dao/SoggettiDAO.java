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

import org.springframework.dao.DataAccessException;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.GenericException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.SoggettiDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;

/**
 * The interface Soggetti dao.
 *
 * @author CSI PIEMONTE
 */
public interface SoggettiDAO extends BaseRiscaBeSrvDAO {
	
    /**
     * Load soggetto by idAmbito and TipoSoggetto and cfSoggetto.
     *
     * @param idAmbito idAmbito
     * @param tipoSoggetto tipoSoggetto
     * @param cfSoggetto cfSoggetto
     * @return List<SoggettiDTO>
     * @throws Exception 
     */
	SoggettiExtendedDTO loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg(Long idAmbito, Long idTipoSoggetto, String cfSoggetto) throws Exception;
	
    /**
     * Load soggetti by idSoggetto.
     *
     * @param idSoggetto idSoggetto
     * @return SoggettiExtendedDTO
     * @throws SQLException 
     * @throws Exception 
     */
	SoggettiExtendedDTO loadSoggettoById(Long idSoggetto) throws SQLException, Exception;

	
	
    /**
     * Save soggetti 
     *
     * @param dto SoggettiExtendedDTO
     * @return long id_soggetti 
     * @throws GenericException 
     */
    Long saveSoggetto(SoggettiExtendedDTO dto, String fruitore) throws GenericException, Exception;
    
    /**
     * Update soggetti
     *
     * @param dto SoggettiDTO
     * @return numero record aggiornati
     * @throws Exception 
     */
    Long updateSoggetto(SoggettiExtendedDTO dto, String fruitore, Long indModManuale) throws GenericException, Exception;
    
    /**
     * Delete soggetti
     *
     * @param Long idSoggetto
     * @param Long idRecapito
     * @return SoggettiExtendedDTO
     * @throws Exception 
     */
    SoggettiExtendedDTO deleteSoggetto(Long idSoggetto, Long idRecapito) throws Exception;
    

    /**
     * Load soggetti by idAmbito and cfSoggetto.
     *
     * @param idAmbito idAmbito
     * @param campoRicerca campoRicerca
     * @param flgTipoRicerca flgTipoRicerca
     * @return dto SoggettiDTO
     * @throws SQLException 
     * @throws Exception 
     */
    List<SoggettiExtendedDTO> loadSoggettiByCampoRicerca(Long idAmbito, String campoRicerca ,Integer offset, Integer limit,String sort) throws SQLException, Exception;
    
    


//	List<Map<String, Object>> findSoggettoGruppoByIdGruppoSoggetto(Long IdGruppoSoggetto);
    /**
     * count soggetti by idAmbito and cfSoggetto.
     *
     * @param idAmbito idAmbito
     * @param campoRicerca campoRicerca
     * @param flgTipoRicerca flgTipoRicerca
     * @return Integer
     * @throws SQLException 
     */
	Integer countSoggettiByCampoRicerca(Long idAmbito, String campoRicerca);
	
    /**
     *  getSoggettoById.
     *
     * @param idSoggetto idSoggetto
     * @return SoggettiExtendedDTO SoggettiExtendedDTO
     * @throws SQLException 
     */
	SoggettiExtendedDTO getSoggettoById(Long idSoggetto)throws SQLException;

	long countSoggettoByCfOrPI(String cf) throws DAOException, SystemException;
	
	public SoggettiExtendedDTO loadIdTipoSoggettoCfPartIvaByIdSoggetto(Long idSoggetto) throws DataAccessException, SQLException,DAOException,SystemException;
    
}
