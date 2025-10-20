/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto.utils;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;


public class ValidatorDto
{

	/**
	 * Verifica se un campo è vuoto
	 * @param field
	 * @return boolean
	 */
	public static boolean isEmpty(Object field)
	{
		if (field == null || field.toString().length() == 0)
		{
			return true;
		}

		return false;
	}

	public static HashMap<String, String> getInvalidMandatoryFields(String containerObjectName, Object ... fields) {
		HashMap<String, String> fieldsMap = new HashMap<String, String>();

		for(int j=0; j<fields.length; j+=2) {
			Object field = fields[j+1];
			String fieldId = (String)fields[j];

			if(field == null 
					|| (field instanceof String && StringUtils.isBlank((String)field))
					|| (field instanceof Boolean && !(Boolean)field) )
				fieldsMap.put(containerObjectName + fieldId, "campo obbligatorio"); 

		}

		return fieldsMap;
	}

}
