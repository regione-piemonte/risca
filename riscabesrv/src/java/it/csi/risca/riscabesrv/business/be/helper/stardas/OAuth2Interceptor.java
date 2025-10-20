/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.stardas;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class OAuth2Interceptor extends AbstractPhaseInterceptor<Message> {

	private String token;

	public OAuth2Interceptor(String token) {
		super(Phase.WRITE);
		this.token = token;
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		Map<String, List<String>> headers = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
		headers.put("Authorization", Collections.singletonList("Bearer " + token));
	}
}
