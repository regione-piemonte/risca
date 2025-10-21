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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.DatoTecnicoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente.RiscossioneDatoTecnicoDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;


public class DatoTecnicoApiServiceHelper extends AbstractServiceHelper {

    public DatoTecnicoApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }

    public RiscossioneDatoTecnicoDTO saveDatoTecnico(RiscossioneDatoTecnicoDTO datoTecnico, String fruitore, 
	HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[DatoTecnicoApiServiceHelper::saveDatoTecnico] BEGIN");
        RiscossioneDatoTecnicoDTO result = new RiscossioneDatoTecnicoDTO();
        String targetUrl = null ;
        if(fruitore != null)
            targetUrl = this.endpointBase + "/dati-tecnici?fruitore="+fruitore;
      
        targetUrl = this.endpointBase + "/dati-tecnici";
        try {
        	
        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<RiscossioneDatoTecnicoDTO> entity = Entity.json(datoTecnico);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
            
            LOGGER.debug("[DatoTecnicoApiServiceHelper::saveDatoTecnico] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.debug("[DatoTecnicoApiServiceHelper::saveDatoTecnico] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<RiscossioneDatoTecnicoDTO> dtoType = new GenericType<RiscossioneDatoTecnicoDTO>() {
            };
            result = resp.readEntity(dtoType);
        } catch (ProcessingException e) {
            LOGGER.debug("[DatoTecnicoApiServiceHelper::saveDatoTecnico] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[DatoTecnicoApiServiceHelper::saveDatoTecnico] END");
        }
        return result;
    }


}
