/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

@Service
public class InputValidationService extends AbstractValidationService {

	@Autowired
	private MessaggiDAO messaggiDAO;
	
    public void validaParametri(Map<String, Object> inputParams) throws BusinessException, SQLException {

        String risultatoValidazioneGenerica = validateParams(inputParams);
        if (risultatoValidazioneGenerica != null) {
            MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E003);
    	    throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));

        }
    }
}
