/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be;

import java.math.BigDecimal;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaExtendedDTO;

public interface VerifyTipoUsoRegola {
	
	/**
	 * Questo metodo controlla il tipo di utilizzo della regola specificata nel DTO esteso.
	 * 
	 * @param tipoUsoRegolaExtendedDTO Il DTO esteso contenente le informazioni sul tipo di utilizzo della regola da controllare.
	 * @throws BusinessException Se si verifica un errore di business durante il controllo del tipo di utilizzo della regola.
	 * @throws Exception Se si verifica un errore generico durante il controllo del tipo di utilizzo della regola.
	 */
	public void checkTipoUsoRegola(TipoUsoRegolaExtendedDTO tipoUsoRegolaExtendedDTO) throws BusinessException, Exception;

	public void verificaAnno(Integer idAmbito, Integer anno)  throws BusinessException, Exception;

	public boolean verificaCanone(BigDecimal canone, String pattern);


}
