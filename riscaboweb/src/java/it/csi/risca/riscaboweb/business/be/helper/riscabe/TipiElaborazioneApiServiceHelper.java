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

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoElaboraExtendedDTO;

public class TipiElaborazioneApiServiceHelper extends AbstractServiceHelper {

    public TipiElaborazioneApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }

        public List<TipoElaboraExtendedDTO> loadTipiElaborazioneByIdAmbitoAndFunzionalitaAndFlgVisibile(MultivaluedMap<String, Object> requestHeaders, Integer idAmbito, Integer idFunzionalita,  Integer flgVisible) throws GenericException {
            LOGGER.debug("[TipiElaborazioneApiServiceHelper::loadTipiElaborazioneByIdAmbitoAndFunzionalitaAndFlgVisibile] BEGIN");
            List<TipoElaboraExtendedDTO> result = new ArrayList<>();
            String targetUrl  = this.endpointBase +"/tipi-elaborazione?idAmbito="+idAmbito+"&idFunzionalita="+idFunzionalita+"&flgVisibile="+flgVisible;
            
            
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiElaborazioneApiServiceHelper::loadTipiElaborazioneByIdAmbitoAndFunzionalitaAndFlgVisibile]  SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipoElaboraExtendedDTO>> tipiAutorizzazioneListType = new GenericType<List<TipoElaboraExtendedDTO>>() {
                };
                result = resp.readEntity(tipiAutorizzazioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiElaborazioneApiServiceHelper::loadTipiElaborazioneByIdAmbitoAndFunzionalitaAndFlgVisibile] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiElaborazioneApiServiceHelper::loadTipiElaborazioneByIdAmbitoAndFunzionalitaAndFlgVisibile] END");
            }
            return result;
        }
}
