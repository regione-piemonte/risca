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
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.TipiAccertamentoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiAccertamentoDTO;

public class TipiAccertamentoApiServiceHelper extends AbstractServiceHelper {

    public TipiAccertamentoApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipiAccertamentoDTO> loadTipiAccertamento( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiAccertamentoApiServiceHelper::loadTipiAccertamento] BEGIN");
            List<TipiAccertamentoDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-accertamento";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipiAccertamentoApi tipiAccertamentoApi = rtarget.proxy(TipiAccertamentoApi.class);
                Response resp = tipiAccertamentoApi.loadTipiAccertamento( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiAccertamentoApiServiceHelper::loadTipiAccertamento] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiAccertamentoDTO>> tipiAccertamentoListType = new GenericType<List<TipiAccertamentoDTO>>() {
                };
                result = resp.readEntity(tipiAccertamentoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiAccertamentoApiServiceHelper::loadTipiAccertamento] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiAccertamentoApiServiceHelper::loadTipiAccertamento] END");
            }
            return result;
        }
}
