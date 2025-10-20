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

import org.springframework.dao.DataAccessException;

/**
 * The interface Calcolo Canone dao.
 *
 * @author CSI PIEMONTE
 */
public interface BaseRiscaBeSrvDAO {
	
    /**
     * Calcola canone.
     *
     * @return List<UnitaMisuraDTO> list
     */
    Long decodeId(String fromTableName, String searchFieldCriteris);
    
    Long findNextSequenceValue(String sequenceName) throws DataAccessException, SQLException;
   
    String getTableNameAString();
    

}
