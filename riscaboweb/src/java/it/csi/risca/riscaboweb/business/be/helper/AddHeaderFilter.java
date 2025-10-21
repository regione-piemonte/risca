/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class AddHeaderFilter implements ClientRequestFilter {
	private final String key;
	private final String value;

	public AddHeaderFilter(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		String header = key + " : " + value;
		requestContext.getHeaders().add(key, value);
	}
}
