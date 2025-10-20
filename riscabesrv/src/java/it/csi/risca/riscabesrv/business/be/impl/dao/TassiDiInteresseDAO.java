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
import it.csi.risca.riscabesrv.dto.TassiDiInteresseDTO;


/**
 * The interface TassiDiInteresse dao.
 * 
 */
public interface TassiDiInteresseDAO extends BaseRiscaBeSrvDAO {
    /**
     * Load elaboration requests by id_ambito, codTipoElabora, codStatoElabora.
     * 
     * @param idAmbito idAmbito
     * @param codTipoElabora codTipoElabora
     * @param codStatoElabora codStatoElabora
     * @return List<TassiDiInteresseDTO> list
     */
    List<TassiDiInteresseDTO> loadTassiDiInteresse(
            Long idAmbito, 
            String tipoDiInteresse,
            Integer offset, 
            Integer limit,
			String sort);
    
    /**
     * Conteggio totale degli elementi TassiDiInteresse.
     * @return
     */
	Integer countAllTassiDiInteresse(
			Long idAmbito, 
            String tipoDiInteresse
            );
    
    
    /**
     * 
     * @param idAmbitoInteresse
     * @return TassiDiInteresseDTO
     */
    TassiDiInteresseDTO loadTassoDiInteresseByIdAmbitoInteresse(Long idAmbitoInteresse);


    /**
     * saveTassiDiInteresse
     * 
     * @param tassiDiInteresse
     * @return
     * @throws DAOException
     */

	TassiDiInteresseDTO saveTassiDiInteresse(TassiDiInteresseDTO tassiDiInteresse, Long idAmbito) throws DAOException;


	/**
	 * deleteTassiDiInteresse
	 * 
	 * @param idAmbitoInteresse
	 * @return
	 * @throws DAOException
	 */

	Long deleteTassiDiInteresse(Long idAmbitoInteresse, Long idAmbito, String tipoInteresse) throws DAOException;
	
	
	TassiDiInteresseDTO updateTassiDiInteresse(TassiDiInteresseDTO dto) throws Exception;

	
	 /**
     * 
     * @param idAmbitoInteresse
     * @return TassiDiInteresseDTO
     */
    TassiDiInteresseDTO loadTassoDiInteressePiuRecente(Long idAmbito, String tipoDiInteresse);
	
	
	
	
	

}
