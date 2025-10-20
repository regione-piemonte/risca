/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.PingApi;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.PingDAO;
import it.csi.risca.riscabesrv.dto.Ping;
import it.csi.risca.riscabesrv.dto.UserInfo;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Ping api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class PingApiServiceImpl implements PingApi {

	private final String CLASSNAME = this.getClass().getSimpleName();

	protected static final Logger LOG = Logger
			.getLogger(it.csi.risca.riscabesrv.util.Constants.COMPONENT_NAME + ".security");

	@Autowired
	PingDAO pingDAO;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response ping(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// PingDAO pingDAO = (PingDAO)
		// SpringApplicationContextHelper.getBean("pingDAO");

		List<Ping> pings = new ArrayList<>();
		try {
			Ping ping = new Ping();
			pingDAO.pingDB();
			ping.setLabel("Database");
			ping.setStatus("OK");
			pings.add(ping);
		} catch (DAOException e) {
			Ping ping = new Ping();
			ping.setLabel("Database");
			ping.setStatus("KO");
			pings.add(ping);
		}

		return Response.ok(pings).build();
	}

	@Override
	public Response testAnagamb(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response testEmail(String email, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response testSPID(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(Constants.USERINFO_SESSIONATTR);
		LOG.debug("ENTRO QUA e : " + userInfo);
		return Response.ok(userInfo).build();
	}

	@Override
	public Response testPDF(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response compilaDoc(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response testJsonIstanza(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response testTransaction(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response testRiscossioneEnte(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response testAdempiTipoPagamento(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response testPagamentoIstanza(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
