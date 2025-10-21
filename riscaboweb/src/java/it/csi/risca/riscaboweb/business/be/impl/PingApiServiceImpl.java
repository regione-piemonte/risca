/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.impl;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.PingApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.LogAuditHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.PingApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CsiLogAuditDTO;
import it.csi.risca.riscaboweb.dto.UserInfo;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

@Component
public class PingApiServiceImpl extends AbstractApiServiceImpl implements PingApi {

    @Autowired
    private PingApiServiceHelper pingApiServiceHelper;

    @Autowired
    private LogAuditHelper logAuditHelper;
    
    @Override
    public Response ping( HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        return Response.ok(pingApiServiceHelper.ping(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest))).build();
    }

	@Override
	public Response testSPID( HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
        return Response.ok(userInfo).build();
	}

	@Override
	public Response getFasiFake(Boolean protetto, Boolean incidenza, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response testPDF(Long idIstanza,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response login(
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		CsiLogAuditDTO log = new CsiLogAuditDTO();
		//CsiLogAuditDTO2 log = new CsiLogAuditDTO2();

		String ip = logAuditHelper.getClientIpAddr(httpRequest);
		//String codiceFisc = userInfo.getCodFisc();

		log.setIdApp("RISCA_RP_SVI_RISCABE");
		log.setIpAddress(ip);
		log.setKeyOper(null);
		log.setOggOper("login applicativo");
		log.setOperazione("login");
		log.setUtente(userInfo.getCodFisc());
		try {
			logAuditHelper.saveCsiLogAudit(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), log);
		} catch (GenericException e) {
			LOGGER.error("[PingApiServiceImpl::login] ERROR : " + e);
		}
		return null;
	}
   

}