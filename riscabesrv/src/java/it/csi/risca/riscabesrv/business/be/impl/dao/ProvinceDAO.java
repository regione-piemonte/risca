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

import it.csi.risca.riscabesrv.dto.ProvinceExtendedDTO;


/**
 * The interface Province dao.
 *
 * @author CSI PIEMONTE
 */
public interface ProvinceDAO {

	   /**
     * Load province list.
     *
     * @return List<ProvinceExtendedDTO> list
     */
    List<ProvinceExtendedDTO> loadProvince(boolean attivo)throws Exception;
    /**
     * Load province list by codIstatComune.
     *
     * @param codIstatComune codIstatComune
     * @return List<ProvinceExtendedDTO> list
     */
    List<ProvinceExtendedDTO> loadProvinceByCodIstatComune(String codIstatComune)throws Exception;

    /**
     * Load province by codRegione
     *
     * @param codRegione codRegione
     * @return List<ProvinceExtendedDTO> list
     */
    List<ProvinceExtendedDTO> loadProvinceByCodRegione(String codRegione)throws Exception;
    /**
     * Load provincia by codRegione
     *
     * @param codRegione codRegione
     * @return List<ProvinceExtendedDTO> list
     * @throws Exception 
     */
   ProvinceExtendedDTO loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia(String codRegione, String codProvincia) throws Exception;

}
