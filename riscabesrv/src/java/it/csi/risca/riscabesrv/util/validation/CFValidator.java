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

import it.csi.risca.riscabesrv.dto.enumeration.ValidationResultEnum;

/**
 * The type Cf validator.
 *
 * @author CSI PIEMONTE
 */
public class CFValidator {

    /**
     * validate the cf
     *
     * @param codiceFiscale codiceFiscale
     * @return ValidationResultEnum ValidationResultEnum
     */
    public static ValidationResultEnum ControllaCF(String codiceFiscale) {
        if (null == codiceFiscale) {
            codiceFiscale = "";
        }
        int i, s, c;
        String cf2;
        int[] setdisp = { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };
        if (codiceFiscale.length() == 0) {
            return ValidationResultEnum.INVALID_LENGTH;
        }
        if (codiceFiscale.length() != 16) {
            return ValidationResultEnum.INVALID_LENGTH;
        }
        cf2 = codiceFiscale.toUpperCase();
        for (i = 0; i < 16; i++) {
            c = cf2.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'Z')) {
                return ValidationResultEnum.INVALID_CHARS;
            }
        }
        s = 0;
        for (i = 1; i <= 13; i += 2) {
            c = cf2.charAt(i);
            if (c >= '0' && c <= '9') {
                s = s + c - '0';
            }
            else {
                s = s + c - 'A';
            }
        }
        for (i = 0; i <= 14; i += 2) {
            c = cf2.charAt(i);
            if (c >= '0' && c <= '9') {
                c = c - '0' + 'A';
            }
            s = s + setdisp[c - 'A'];
        }
        if (s % 26 + 'A' != cf2.charAt(15)) {
            return ValidationResultEnum.INVALID_CONTROL_CODE;
        }
        return ValidationResultEnum.VALID;
    }

}