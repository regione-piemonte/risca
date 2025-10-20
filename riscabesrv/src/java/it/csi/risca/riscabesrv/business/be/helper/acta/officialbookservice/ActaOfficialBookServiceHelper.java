/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.acta.officialbookservice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import it.csi.risca.riscabesrv.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscabesrv.util.actaofficialbook.NavigationConditionInfoType;
import it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType;
import it.csi.risca.riscabesrv.util.actaofficialbook.OfficialBookServiceLocator;
import it.csi.risca.riscabesrv.util.actaofficialbook.OfficialBookServicePort_PortType;
import it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType;
import it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType;
import it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType;
import it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType;
import it.csi.risca.riscabesrv.util.actaofficialbook.QueryableObjectType;

public class ActaOfficialBookServiceHelper   extends AbstractServiceHelper {
	   private OfficialBookServicePort_PortType service;
	    /**
	     * The Url service.
	     */
	    protected String urlService = "";

	    /**
	     * Instantiates a new Acta OfficialBook service helper.
	     *
	     * @param urlService urlService
	     */
	    public ActaOfficialBookServiceHelper(String urlService) {
	        this.urlService = urlService;
	        this.service = this.getService(urlService);
	    }

	    private OfficialBookServicePort_PortType getService(String urlService) {
	        LOGGER.debug("[ActaOfficialBookServiceHelper::getService] BEGIN");
	        OfficialBookServicePort_PortType server  = null;
	        try {
	            OfficialBookServiceLocator locator = new OfficialBookServiceLocator();
	            server = locator.getOfficialBookServicePort(new URL(urlService));
	            

	            LOGGER.debug("[ActaOfficialBookServiceHelper::getService] Service 'JavaServiceDesc' INITIALIZED");
	            LOGGER.debug("[ActaOfficialBookServiceHelper::getService] END");
	        } catch (MalformedURLException | ServiceException e) {
	            LOGGER.error("[ActaOfficialBookServiceHelper::getService] ERROR : invalid url [" + urlService + "]");
	        }
	        return server;
	    }

	    /**
	     * Gets children.
	     *
	     * @return PagingResponseType children
	     */
	    public PagingResponseType query(ObjectIdType repositoryId, PrincipalIdType principalId, QueryableObjectType target, PropertyFilterType filter, QueryConditionType[] criteria, NavigationConditionInfoType navigationLimits, Integer maxItems, Integer skipCount) {
	        LOGGER.debug("[ActaOfficialBookServiceHelper::getChildren] BEGIN");
	        it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType pagingResponseType = null;
	        try {
	        	pagingResponseType = this.service.query(repositoryId, principalId, target, filter, criteria, navigationLimits, maxItems, skipCount);
	            return pagingResponseType;
	        } catch (Exception e) {
	            LOGGER.error("[ActaOfficialBookServiceHelper::getChildren] ERROR ", e);
	        }
	        LOGGER.debug("[ActaOfficialBookServiceHelper::getChildren] END");
	        return pagingResponseType;
	    }
}
