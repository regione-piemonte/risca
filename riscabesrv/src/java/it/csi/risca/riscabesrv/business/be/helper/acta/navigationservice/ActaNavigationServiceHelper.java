/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.acta.navigationservice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import it.csi.risca.riscabesrv.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscabesrv.util.actanavigation.NavigationServiceLocator;
import it.csi.risca.riscabesrv.util.actanavigation.NavigationServicePort_PortType;
import it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType;
import it.csi.risca.riscabesrv.util.actanavigation.PagingResponseType;
import it.csi.risca.riscabesrv.util.actanavigation.PrincipalIdType;
import it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType;
import it.doqui.acta.acaris.objectservice.AcarisException;

public class ActaNavigationServiceHelper   extends AbstractServiceHelper {
	   private NavigationServicePort_PortType service;
	    /**
	     * The Url service.
	     */
	    protected String urlService = "";

	    /**
	     * Instantiates a new Acta Navigation service helper.
	     *
	     * @param urlService urlService
	     */
	    public ActaNavigationServiceHelper(String urlService) {
	        this.urlService = urlService;
	        this.service = this.getService(urlService);
	    }

	    private NavigationServicePort_PortType getService(String urlService) {
	        LOGGER.debug("[ActaNavigationServiceHelper::getService] BEGIN");
	        NavigationServicePort_PortType server  = null;
	        try {
	            NavigationServiceLocator locator = new NavigationServiceLocator();
	            server = locator.getNavigationServicePort(new URL(urlService));
	            

	            LOGGER.debug("[ActaNavigationServiceHelper::getService] Service 'JavaServiceDesc' INITIALIZED");
	            LOGGER.debug("[ActaNavigationServiceHelper::getService] END");
	        } catch (MalformedURLException | ServiceException e) {
	            LOGGER.error("[ActaNavigationServiceHelper::getService] ERROR : invalid url [" + urlService + "]");
	        }
	        return server;
	    }

	    /**
	     * Gets children.
	     *
	     * @return PagingResponseType children
	     * @throws Exception 
	     */
	    public PagingResponseType getChildren(ObjectIdType repositoryId, ObjectIdType folderId, PrincipalIdType principalId, PropertyFilterType filter, Integer maxItems, Integer skipCount) throws Exception{
	        LOGGER.debug("[ActaNavigationServiceHelper::getChildren] BEGIN");
	        PagingResponseType children = null;
	        try {
	        	children = this.service.getChildren(repositoryId, folderId, principalId, filter, maxItems, skipCount);
	            return children;
	        } catch (Exception e) {
	            LOGGER.error("[ActaNavigationServiceHelper::getChildren] ERROR ", e);
	            throw e;
	        }

	    }
}
