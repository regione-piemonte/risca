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
/**
 * The interface Riscossione Recapito DAO.
 *
 * @author CSI PIEMONTE
 */
public interface RiscossioneRecapitoDAO {
	

	/**
	 * deleteRiscossioneRecapitoByIdRecapito
	 * 
	 * @param idRecapito
	 */
	public void deleteRiscossioneRecapitoByIdRecapito(Long idRecapito)throws Exception;

}
