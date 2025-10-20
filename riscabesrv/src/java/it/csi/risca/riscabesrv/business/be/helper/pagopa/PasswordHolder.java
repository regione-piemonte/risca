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

public class PasswordHolder {

	private static PasswordHolder instance;
	private String password;

	private PasswordHolder() {
	}

	public static PasswordHolder getInstance() {
		if (instance == null) {
			instance = new PasswordHolder();
		}
		return instance;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
