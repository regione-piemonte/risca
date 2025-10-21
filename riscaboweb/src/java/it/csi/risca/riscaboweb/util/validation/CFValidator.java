/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.util.validation;

public class CFValidator {

    public enum CFValidatorResultEnum {

        VALID("VALID"),

        INVALID_LENGTH("INVALID_LENGTH"),

        INVALID_CHARS("INVALID_CHARS"),

        INVALID_CONTROL_CODE("INVALID_CONTROL_CODE");

        private String description;

        CFValidatorResultEnum(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
    
    public static String ControllaCF(String cf) {
        if (null == cf) cf = "";
        int i, s, c;
        String cf2;
        int setdisp[] = {1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20,
                11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };
        if( cf.length() == 0 ) return CFValidatorResultEnum.INVALID_LENGTH.name();
        if( cf.length() != 16 )
            return CFValidatorResultEnum.INVALID_LENGTH.name();
        cf2 = cf.toUpperCase();
        for( i=0; i<16; i++ ){
            c = cf2.charAt(i);
            if( ! ( c>='0' && c<='9' || c>='A' && c<='Z' ) )
                return CFValidatorResultEnum.INVALID_CHARS.name();
        }
        s = 0;
        for( i=1; i<=13; i+=2 ){
            c = cf2.charAt(i);
            if( c>='0' && c<='9' )
                s = s + c - '0';
            else
                s = s + c - 'A';
        }
        for( i=0; i<=14; i+=2 ){
            c = cf2.charAt(i);
            if( c>='0' && c<='9' )     c = c - '0' + 'A';
            s = s + setdisp[c - 'A'];
        }
        if( s%26 + 'A' != cf2.charAt(15) )
        return CFValidatorResultEnum.INVALID_CONTROL_CODE.name();
            return CFValidatorResultEnum.VALID.name();
    }
    

}