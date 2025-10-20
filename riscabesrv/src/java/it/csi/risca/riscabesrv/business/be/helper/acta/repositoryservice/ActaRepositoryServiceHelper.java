/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.acta.repositoryservice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import it.csi.risca.riscabesrv.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscabesrv.util.actarepository.AcarisRepositoryEntryType;
import it.csi.risca.riscabesrv.util.actarepository.RepositoryServiceLocator;
import it.csi.risca.riscabesrv.util.actarepository.RepositoryServicePort_PortType;

public class ActaRepositoryServiceHelper   extends AbstractServiceHelper {
	   private RepositoryServicePort_PortType service;
	    /**
	     * The Url service.
	     */
	    protected String urlService = "";

	    /**
	     * Instantiates a new Acta Repository service helper.
	     *
	     * @param urlService urlService
	     */
	    public ActaRepositoryServiceHelper(String urlService) {
	        this.urlService = urlService;
	        this.service = this.getService(urlService);
	    }

	    private RepositoryServicePort_PortType getService(String urlService) {
	        LOGGER.debug("[ActaRepositoryServiceHelper::getService] BEGIN");
	        RepositoryServicePort_PortType server  = null;
	        try {
	            RepositoryServiceLocator locator = new RepositoryServiceLocator();
	            server = locator.getRepositoryServicePort(new URL(urlService));
	            

	            LOGGER.debug("[ActaRepositoryServiceHelper::getService] Service 'JavaServiceDesc' INITIALIZED");
	            LOGGER.debug("[ActaRepositoryServiceHelper::getService] END");
	        } catch (MalformedURLException | ServiceException e) {
	            LOGGER.error("[ActaRepositoryServiceHelper::getService] ERROR : invalid url [" + urlService + "]");
	        }
	        return server;
	    }

	    /**
	     * Gets repositories.
	     *
	     * @return AcarisRepositoryEntryType[] repositories
	     */
	    public AcarisRepositoryEntryType[] getRepositories() {
	        LOGGER.debug("[ActaRepositoryServiceHelper::getRepositories] BEGIN");
	    	AcarisRepositoryEntryType[] repositories = null;
	        try {
	        	repositories = this.service.getRepositories();
	            return repositories;
	        } catch (Exception e) {
	            LOGGER.error("[ActaRepositoryServiceHelper::getRepositories] ERROR ", e);
	        }
	        LOGGER.debug("[ActaRepositoryServiceHelper::getRepositories] END");
	        return repositories;
	    }
}
