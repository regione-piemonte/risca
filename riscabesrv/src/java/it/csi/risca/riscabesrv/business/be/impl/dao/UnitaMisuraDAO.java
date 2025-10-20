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

import it.csi.risca.riscabesrv.dto.UnitaMisuraDTO;

/**
 * The interface Unita Misura dao.
 *
 * @author CSI PIEMONTE
 */
public interface UnitaMisuraDAO {
	

    /**
     * Load unita misura list.
     *
     * @return List<UnitaMisuraDTO> list
     */
    List<UnitaMisuraDTO> loadUnitaMisura();

    /**
     * Load unita misura list by id ambito.
     *
     * @param idAmbito idAmbito
     * @return List<UnitaMisuraDTO> list
     */
    List<UnitaMisuraDTO> loadUnitaMisuraByIdAmbito(Long idAmbito);
    
    /**
     * Load unita misura by cod unita misura and id ambito
     *
     * @param keyUnitaMisura keyUnitaMisura
     * @param idAmbito idAmbito
     * @return UnitaMisuraDTO
     * @throws SQLException 
     */
    UnitaMisuraDTO loadUnitaMisuraByKeyUnitaMisura(String keyUnitaMisura) throws SQLException;

    /**
     * Load unita misura by id unita misura and id ambito
     *
     * @param idUnitaMisura idUnitaMisura
     * @param idAmbito idAmbito
     * @return UnitaMisuraDTO
     * @throws SQLException 
     */
    UnitaMisuraDTO loadUnitaMisuraByIdUnitaMisura(String IdUnitaMisura) throws SQLException;
    
    /**
     * Load unita misura by desc unita misura
     *
     * @param desUnitaMisura desUnitaMisura
     * @return UnitaMisuraDTO
     * @throws SQLException 
     */
    UnitaMisuraDTO loadUnitaMisuraByDesc(String desUnitaMisura) throws SQLException;




}
