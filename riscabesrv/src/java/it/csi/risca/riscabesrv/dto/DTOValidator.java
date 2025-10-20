/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import it.csi.risca.riscabesrv.business.be.exception.ValidationException;

public class DTOValidator<T> {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    public void validateDTO(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder("Errore di validazione del DTO: ");
            for (ConstraintViolation<T> violation : violations) {
                errorMessages.append(violation.getMessage()).append(", ");
            }
            errorMessages.setLength(errorMessages.length() - 2);
            
            throw new ValidationException(errorMessages.toString());
        }

    }
}
