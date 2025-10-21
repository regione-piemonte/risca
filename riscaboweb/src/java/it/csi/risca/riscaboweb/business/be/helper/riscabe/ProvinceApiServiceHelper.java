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

import it.csi.risca.riscaboweb.business.be.ProvinceApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ProvinceExtendedDTO;

public class ProvinceApiServiceHelper extends AbstractServiceHelper {

    public ProvinceApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<ProvinceExtendedDTO> getProvince(boolean attivo,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[ProvinceApiServiceHelper::getProvince] BEGIN");
            List<ProvinceExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/province?attivo=" + attivo;
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                ProvinceApi provinceApi = rtarget.proxy(ProvinceApi.class);
                Response resp = provinceApi.getProvince(attivo,  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[ProvinceApiServiceHelper::getProvince] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<ProvinceExtendedDTO>> provinceListType = new GenericType<List<ProvinceExtendedDTO>>() {
                };
                result = resp.readEntity(provinceListType);
            } catch (ProcessingException e) {
                LOGGER.error("[ProvinceApiServiceHelper::getProvince] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[ProvinceApiServiceHelper::getProvince] END");
            }
            return result;
        }
        
        public List<ProvinceExtendedDTO> getProvinceByCodIstatComune(MultivaluedMap<String, Object> requestHeaders, String codIstatComune) throws GenericException {
            LOGGER.debug("[ProvinceApiServiceHelper::getProvinceByCodIstatComune] BEGIN");
            List<ProvinceExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/province/comune/" + codIstatComune;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[ProvinceApiServiceHelper::getProvinceByCodIstatComune] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<ProvinceExtendedDTO>> provinceListType = new GenericType<List<ProvinceExtendedDTO>>() {
                };
                result = resp.readEntity(provinceListType);
            } catch (ProcessingException e) {
                LOGGER.error("[ProvinceApiServiceHelper::getProvinceByCodIstatComune] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[ProvinceApiServiceHelper::getProvinceByCodIstatComune] END");
            }
            return result;
        }
        
        public List<ProvinceExtendedDTO> getProvinceByCodRegione(MultivaluedMap<String, Object> requestHeaders, String codRegione) throws GenericException {
            LOGGER.debug("[ProvinceApiServiceHelper::getProvinceByCodRegione] BEGIN");
            List<ProvinceExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/regione/" + codRegione+"/province";
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[ProvinceApiServiceHelper::getProvinceByCodRegione] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<ProvinceExtendedDTO>> provinceListType = new GenericType<List<ProvinceExtendedDTO>>() {
                };
                result = resp.readEntity(provinceListType);
            } catch (ProcessingException e) {
                LOGGER.error("[ProvinceApiServiceHelper::getProvinceByCodRegione] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[ProvinceApiServiceHelper::getProvinceByCodRegione] END");
            }
            return result;
        }
		public ProvinceExtendedDTO loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia(
				MultivaluedMap<String, Object> requestHeaders, String codRegione, String codProvincia) throws GenericException {
			  LOGGER.debug("[ProvinceApiServiceHelper::loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia] BEGIN");
	            ProvinceExtendedDTO result = new ProvinceExtendedDTO();
	            String targetUrl = this.endpointBase + "/regioni/" + codRegione + "/province/"+codProvincia;
	            try {
	                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
	                if (resp.getStatus() >= 400) {
	                    ErrorDTO err = getError(resp);
	                    LOGGER.error("[ProvinceApiServiceHelper::loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia] SERVER EXCEPTION : " + err);
	                    throw new GenericException(err);
	                }
	                GenericType<ProvinceExtendedDTO> provincia = new GenericType<ProvinceExtendedDTO>() {
	                };
	                result = resp.readEntity(provincia);
	            } catch (ProcessingException e) {
	                LOGGER.error("[ProvinceApiServiceHelper::loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia] EXCEPTION : " + e);
	                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	            } finally {
	                LOGGER.debug("[ProvinceApiServiceHelper::loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia] END");
	            }
	            return result;
			
		}
}
