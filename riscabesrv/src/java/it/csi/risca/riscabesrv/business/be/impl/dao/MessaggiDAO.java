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

import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;


/**
 * The interface Messaggi dao.
 *
 * @author CSI PIEMONTE
 */
public interface MessaggiDAO {

    /**
     * Load messaggi list.
     *
     * @param codMessaggio codMessaggio
     * @return MessaggiDTO messaggiDTO
     */
    List<MessaggiDTO> loadMessaggi();
    
    /**
     * Load messaggi list by codMessaggio.
     *
     * @param codMessaggio codMessaggio
     * @return MessaggiDTO messaggiDTO
     * @throws SQLException 
     * @throws SystemException 
     */
    MessaggiDTO loadMessaggiByCodMessaggio(String codMessaggio) throws SQLException;

}
