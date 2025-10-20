/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.acta.relationshipsservice;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import it.csi.risca.riscabesrv.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType;
import it.csi.risca.riscabesrv.util.actarelationships.PrincipalIdType;
import it.csi.risca.riscabesrv.util.actarelationships.PropertyFilterType;
import it.csi.risca.riscabesrv.util.actarelationships.RelationshipPropertiesType;
import it.csi.risca.riscabesrv.util.actarelationships.RelationshipsServiceLocator;
import it.csi.risca.riscabesrv.util.actarelationships.RelationshipsServicePort_PortType;

public class ActaRelationshipsServiceHelper   extends AbstractServiceHelper {
	   private RelationshipsServicePort_PortType service;
	    /**
	     * The Url service.
	     */
	    protected String urlService = "";

	    /**
	     * Instantiates a new Acta Relationships service helper.
	     *
	     * @param urlService urlService
	     */
	    public ActaRelationshipsServiceHelper(String urlService) {
	        this.urlService = urlService;
	        this.service = this.getService(urlService);
	    }

	    private RelationshipsServicePort_PortType getService(String urlService) {
	        LOGGER.debug("[ActaRelationshipsServiceHelper::getService] BEGIN");
	        RelationshipsServicePort_PortType server  = null;
	        try {
	            RelationshipsServiceLocator locator = new RelationshipsServiceLocator();
	            server = locator.getRelationshipsServicePort(new URL(urlService));
	            

	            LOGGER.debug("[ActaRelationshipsServiceHelper::getService] Service 'JavaServiceDesc' INITIALIZED");
	            LOGGER.debug("[ActaRelationshipsServiceHelper::getService] END");
	        } catch (MalformedURLException | ServiceException e) {
	            LOGGER.error("[ActaRelationshipsServiceHelper::getService] ERROR : invalid url [" + urlService + "]");
	        }
	        return server;
	    }

	    /**
	     * Gets object relationships
	     *
	     * @return RelationshipPropertiesType[] getObjectRelationships
	     */
	    public RelationshipPropertiesType[] getObjectRelationships(ObjectIdType repositoryId, PrincipalIdType principalId, ObjectIdType objectId, String typeId, String direction, PropertyFilterType filter) throws it.doqui.acta.acaris.objectservice.AcarisException {
	        LOGGER.debug("[ActaRelationshipsServiceHelper::getObjectRelationships] BEGIN");
	        RelationshipPropertiesType[] objRel = null;
	        try {
	        	objRel = this.service.getObjectRelationships(repositoryId, principalId, objectId, typeId, direction, filter);
	            return objRel;
	        } 
	        catch (Exception e) {
	            LOGGER.error("[ActaRelationshipsServiceHelper::getObjectRelationships] ERROR ", e);
	        }
	        LOGGER.debug("[ActaRelationshipsServiceHelper::getObjectRelationships] END");
	        return objRel;
	    }
}
