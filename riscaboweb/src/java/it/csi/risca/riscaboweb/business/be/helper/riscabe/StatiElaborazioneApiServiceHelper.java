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
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.StatoElaborazioneDTO;

public class StatiElaborazioneApiServiceHelper extends AbstractServiceHelper {

    public StatiElaborazioneApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }

        
        public List<StatoElaborazioneDTO> loadStatiElaborazioneByIdAmbitoAndIdFunzionalita(MultivaluedMap<String, Object> requestHeaders, Integer idAmbito, Integer idFunzionalita) throws GenericException {
            LOGGER.debug("[StatiElaborazioneApiServiceHelper::loadStatiElaborazioneByIdAmbitoAndIdFunzionalita] BEGIN");
            List<StatoElaborazioneDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase +"/stato-elaborazione?idAmbito="+idAmbito+"&idFunzionalita="+idFunzionalita;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[StatiElaborazioneApiServiceHelper::loadStatiElaborazioneByIdAmbitoAndIdFunzionalita] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<StatoElaborazioneDTO>> statiRiscossioneListType = new GenericType<List<StatoElaborazioneDTO>>() {
                };
                result = resp.readEntity(statiRiscossioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[StatiElaborazioneApiServiceHelper::loadStatiElaborazioneByIdAmbitoAndIdFunzionalita] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[StatiElaborazioneApiServiceHelper::loadStatiElaborazioneByIdAmbitoAndIdFunzionalita] END");
            }
            return result;
        }

}
