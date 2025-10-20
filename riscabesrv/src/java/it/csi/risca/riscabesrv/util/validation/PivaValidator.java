/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.util.validation;

import org.apache.commons.lang3.StringUtils;

import it.csi.risca.riscabesrv.dto.enumeration.ValidationResultEnum;

/**
 * The type Piva validator.
 *
 * @author CSI PIEMONTE
 */
public class PivaValidator {

    /**
     * validate a piva
     *
     * @param piva piva
     * @return ValidationResultEnum ValidationResultEnum
     */
    public static ValidationResultEnum isValid(String piva) {
        if (StringUtils.isBlank(piva)) {
            return ValidationResultEnum.VALID;
        }

        if (piva.trim().length() != 11) {
            return ValidationResultEnum.INVALID_LENGTH;
        }

        int i, c, s;
        for (i = 0; i < 11; i++) {
            if (piva.charAt(i) < '0' || piva.charAt(i) > '9') {
                return ValidationResultEnum.INVALID_CHARS;
            }
        }

        s = 0;
        for (i = 0; i <= 9; i += 2) {
            s += piva.charAt(i) - '0';
        }

        for (i = 1; i <= 9; i += 2) {
            c = 2 * (piva.charAt(i) - '0');
            if (c > 9) {
                c = c - 9;
            }
            s += c;
        }

        boolean validChecksum = (10 - s % 10) % 10 == piva.charAt(10) - '0';
        if(!validChecksum) {
            return ValidationResultEnum.INVALID_CONTROL_CODE;
        }

        return ValidationResultEnum.VALID;
    }
}