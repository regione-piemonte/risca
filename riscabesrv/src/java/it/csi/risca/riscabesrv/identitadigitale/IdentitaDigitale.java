/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.identitadigitale;

import java.sql.SQLException;

import javax.ws.rs.core.HttpHeaders;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;

public interface IdentitaDigitale {
	
	 Boolean isIdentitaDigitale(Identita identita, String fruitore, Long idAmbitoInput, String codOggetto);
	 Boolean isFruitoreBatch(String fruitore, String key);
	 Boolean isIdentitaAutentica(String fruitore, Identita identita);
	 void verificaIdentitaDigitale(String fruitore, Long idAmbitoInput, HttpHeaders httpHeaders,  String codOggettoProfilo) throws BusinessException, SQLException, SystemException;
}
