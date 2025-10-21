/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CsiLogAuditDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;

public class CsiLogAuditApiServiceHelper extends AbstractServiceHelper{

	   public CsiLogAuditApiServiceHelper(String hostname, String endpointBase) {
	        this.hostname = hostname;
	        this.endpointBase = hostname + endpointBase;
	    }
	   
	   
	   
	   public  CsiLogAuditDTO saveCsiLogAudit(MultivaluedMap<String, Object> requestHeaders, CsiLogAuditDTO csiLogAudit) throws GenericException {
	        LOGGER.debug("[CsiLogAuditApiServiceHelper::saveCsiLogAudit] BEGIN");
	         CsiLogAuditDTO result = new CsiLogAuditDTO();
	        String targetUrl = this.endpointBase + "/csi-log-audit";
	        try {
	        	
	            Entity<CsiLogAuditDTO> entity = Entity.json(csiLogAudit);
	            Response resp = getInvocationBuilder(targetUrl, requestHeaders).post(entity);
	            LOGGER.debug("[CsiLogAuditApiServiceHelper::saveCsiLogAudit] RESPONSE STATUS : " + resp.getStatus());
	            if (resp.getStatus() >= 400) {
	                ErrorDTO err = getError(resp);
	                LOGGER.debug("[CsiLogAuditApiServiceHelper::saveCsiLogAudit] SERVER EXCEPTION : " + err);
	                throw new GenericException(err);
	            }
	            GenericType<CsiLogAuditDTO> dto = new GenericType<CsiLogAuditDTO>()  {
	            };
	            result = resp.readEntity(dto);
	        } catch (ProcessingException e) {
	            LOGGER.debug("[saveCsiLogAudit] EXCEPTION : " + e);
	            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	        } finally {
	            LOGGER.debug("[saveCsiLogAudit] END");
	        }
	        return result;
	    }
		
}
