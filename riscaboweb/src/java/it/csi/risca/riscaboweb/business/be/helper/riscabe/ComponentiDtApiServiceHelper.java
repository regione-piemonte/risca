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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.ComponentiDtApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ComponenteDtExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;


public class ComponentiDtApiServiceHelper extends AbstractServiceHelper {

    public ComponentiDtApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }

    public List<ComponenteDtExtendedDTO> loadComponentiDt(Integer idAmbito, String codTipoComponente, boolean attivo, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[ComponentiDtApiServiceHelper::loadComponentiDt] BEGIN");
        List<ComponenteDtExtendedDTO> result = new ArrayList<>();
        String targetUrl ="";
        if(codTipoComponente != null )
        	targetUrl = this.endpointBase + "/ambiti/" + idAmbito + "/componenti-dt?codTipoComponente=" + codTipoComponente+"&attivo=" +attivo;
        else 
        	targetUrl = this.endpointBase + "/ambiti/" + idAmbito + "/componenti-dt?attivo=" +attivo;
        try {

        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
        	Client client = null;
        	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
         		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
            }else {
                  client = ClientBuilder.newClient();
        	}
             WebTarget target = client.target(this.endpointBase);
            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
            ComponentiDtApi componentiDtApi = rtarget.proxy(ComponentiDtApi.class);
            Response resp = componentiDtApi.loadComponentiDt(idAmbito.toString(), codTipoComponente, attivo, 
       			 httpHeaders, httpRequest);
            LOGGER.debug("[ComponentiDtApiServiceHelper::loadComponentiDt] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[ComponentiDtApiServiceHelper::loadComponentiDt] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<List<ComponenteDtExtendedDTO>> componentiDt = new GenericType<List<ComponenteDtExtendedDTO>>() {
            };
            result = resp.readEntity(componentiDt);
        } catch (ProcessingException e) {
            LOGGER.error("[ComponentiDtApiServiceHelper::loadComponentiDt] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[ComponentiDtApiServiceHelper::loadComponentiDt] END");
        }
        return result;
    }

}
