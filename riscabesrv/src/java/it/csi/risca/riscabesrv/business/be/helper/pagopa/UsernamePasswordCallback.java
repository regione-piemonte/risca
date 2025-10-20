/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.pagopa;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.log4j.Logger;
import org.apache.wss4j.common.ext.WSPasswordCallback;

import it.csi.risca.riscabesrv.util.Constants;

public class UsernamePasswordCallback implements CallbackHandler {
	
	private WSPasswordCallback pc;
	private static final Logger log = Logger.getLogger(Constants.LOGGER_NAME);
	
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		log.info("[UsernamePasswordCallback::handle] BEGIN");
		pc = (WSPasswordCallback) callbacks[0];
		if (WSPasswordCallback.USERNAME_TOKEN == pc.getUsage()) {
			try {
				setPassordUsernameToken();
			} catch (Exception e) {
				log.error("[UsernamePasswordCallback::handle] Error: " + e.getMessage());
			}
		}
	}

	private void setPassordUsernameToken() throws Exception {
		pc.setPassword(PasswordHolder.getInstance().getPassword());
	}

}
