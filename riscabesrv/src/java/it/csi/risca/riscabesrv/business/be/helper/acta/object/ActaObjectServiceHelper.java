/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.acta.object;

import java.net.URL;

import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOMFeature;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import it.csi.risca.riscabesrv.business.be.helper.AbstractServiceHelper;
import it.doqui.acta.acaris.objectservice.AcarisException;
import it.doqui.acta.acaris.objectservice.ObjectServicePort;
import it.doqui.acta.actasrv.dto.acaris.type.common.NavigationConditionInfoType;
import it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType;
import it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType;
import it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType;
import it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType;
import it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType;
import it.doqui.acta.actasrv.dto.acaris.type.common.QueryableObjectType;

public class ActaObjectServiceHelper extends AbstractServiceHelper {
	
	private ObjectServicePort service;
//	private ObjectServicePort_PortType service;
    /**
     * The Url service.
     */
    protected String urlService = "";

    /**
     * Instantiates a new Acta Object service helper.
     *
     * @param urlService urlService
     */
    public ActaObjectServiceHelper(String urlService) {
        this.urlService = urlService;
        this.service = this.getService(urlService, true);
    }

    private ObjectServicePort getService(String urlService, boolean useMtom) {
        LOGGER.debug("[ActaObjectServiceHelper::getService] BEGIN");
        ObjectServicePort server  = null;
          WebServiceFeature[] features = useMtom ? new WebServiceFeature[] { new MTOMFeature(true) } : new WebServiceFeature[] {};
		try {
	       it.doqui.acta.acaris.objectservice.ObjectService objectService = new it.doqui.acta.acaris.objectservice.ObjectService(new URL(this.urlService));
	       server = objectService.getObjectServicePort(features);
           Client client = ClientProxy.getClient(server);
           client.getInInterceptors().add(new LoggingInInterceptor());
           client.getOutInterceptors().add(new LoggingOutInterceptor());
           client.getInInterceptors().add(new EncodingInterceptor());
		} catch (Exception e) {
            LOGGER.error("[ActaObjectServiceHelper::getService] ERROR ", e);
		}
		

		LOGGER.debug("[ActaObjectServiceHelper::getService] Service 'JavaServiceDesc' INITIALIZED");
		LOGGER.debug("[ActaObjectServiceHelper::getService] END");
        return server;
    }
    
    /**
     * Gets pagingResponseType.
     *
     * @return PagingResponseType
     */
    
    public PagingResponseType query(ObjectIdType repositoryId, PrincipalIdType principalId, QueryableObjectType target, PropertyFilterType filter, QueryConditionType[] criteria, NavigationConditionInfoType navigationLimits, Integer maxItems, Integer skipCount) throws AcarisException {
        LOGGER.debug("[ActaObjectServiceHelper::query] BEGIN");
        PagingResponseType pagingResponseType = null;
        try {
       	pagingResponseType = this.service.query(repositoryId, principalId, target, filter, criteria, navigationLimits, maxItems, skipCount);

        } catch (Exception e) {
            LOGGER.error("[ActaObjectServiceHelper::query] ERROR ", e);
            throw e;
        }
        LOGGER.debug("[ActaObjectServiceHelper::query] END");
        return pagingResponseType;
    }
    
    /**
     * Gets pagingResponseType.
     *
     * @return AcarisContentStreamType[]
     */
    
    public it.doqui.acta.actasrv.dto.acaris.type.common.AcarisContentStreamType[] getContentStream(it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repositoryId, it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType documentId, it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principalId, it.doqui.acta.actasrv.dto.acaris.type.common.EnumStreamId streamId) throws it.doqui.acta.acaris.objectservice.AcarisException {
        LOGGER.debug("[ActaObjectServiceHelper::query] BEGIN");
        it.doqui.acta.actasrv.dto.acaris.type.common.AcarisContentStreamType[] contentStreamType = null;
        try {
        	
        	contentStreamType = this.service.getContentStream(repositoryId, documentId, principalId, streamId);

        } catch (Exception e) {
            LOGGER.error("[ActaObjectServiceHelper::query] ERROR ", e);
        }
        LOGGER.debug("[ActaObjectServiceHelper::query] END");
        return contentStreamType;
    }
}
