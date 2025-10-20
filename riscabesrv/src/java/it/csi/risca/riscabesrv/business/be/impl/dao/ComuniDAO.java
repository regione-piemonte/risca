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

import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.dto.ComuneDTO;

/**
 * The interface Comuni dao.
 *
 * @author CSI PIEMONTE
 */
public interface ComuniDAO {

	   /**
     * Load comuni list.
     *
     * @param attivo  attivo
     * @return List<ComuniDTO> list
     */
    List<ComuneExtendedDTO> loadComuni(boolean attivo);
    /**
     * Load comuni by id or code (regione, provincia comune).
     *
     * @param idRegione        idRegione
     * @param idProvincia      idProvincia
     * @param codIstatComune   codIstatComune
     * @return List<ComuniDTO> list
     */
    List<ComuneDTO> loadComuniByIdOrCod(String idRegione, String idProvincia, String codIstatComune);

    /**
     * Load comuni by codice regione.
     *
     * @param codRegione codRegione
     * @param codRegione codProvincia
     * @return List<ComuniDTO> list
     */
    List<ComuneDTO> loadComuniByCodRegione(String codRegione, String codProvincia);
    /**
     * Load comuni by search string
     *
     * @param q        q
     * @param attivo      attivo
     * @param codIstatComune    codIstatComune
     * @return List<ComuniDTO> list
     */
    List<ComuneExtendedDTO> loadComuniByRicerca(String q, boolean attivo);

    /**
     * Load comuni by codIstatComune.
     *
     * @param codIstatComune codIstatComune
     * @return ComuniDTO ComuniDTO
     */
    ComuneDTO loadComuneByCodIstatComune(String codIstatComune);
    
    /**
     * Load comuni by idComune.
     *
     * @param idComune idComune
     * @return ComuneExtendedDTO ComuneExtendedDTO
     */
    ComuneExtendedDTO loadComuneById(Long idComune);


}
