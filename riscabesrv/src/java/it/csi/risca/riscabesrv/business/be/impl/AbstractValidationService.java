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

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AbstractValidationService extends BaseApiServiceImpl {

    protected final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    protected String validateParams(Map<String, Object> inputParams) {
        Set<ConstraintViolation<Map<String, Object>>> violations = validator.validate(inputParams);
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder("Errori di validazione:");
            for (ConstraintViolation<Map<String, Object>> violation : violations) {
                errorMessages.append("\n").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            }
            return errorMessages.toString();
        }

        return null; 
    }
}
