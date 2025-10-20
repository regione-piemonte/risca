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

import it.csi.risca.riscabesrv.dto.FonteDTO;

/**
 * The interface Fonte dao.
 *
 * @author CSI PIEMONTE
 */
public interface FonteDAO {
	
    /**
     * Load fonte by codFonte.
     *
     * @param codFonte codFonte
     * @return FonteDTO
     */

	FonteDTO loadFonteByCodFonte(String codFonte);
    /**
     * Load fonte by chiave.
     *
     * @param chiave chiave
     * @return FonteDTO
     */

	FonteDTO loadFonteByChiaveFonte(String fruitore, String chiave);
}
