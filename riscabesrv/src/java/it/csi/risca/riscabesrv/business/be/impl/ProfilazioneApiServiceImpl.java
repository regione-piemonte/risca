/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.iridefed.entity.Ruolo;
import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.exceptions.MalformedIdTokenException;
import it.csi.risca.riscabesrv.business.be.ProfilazioneApi;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.ProfilazioneDAO;
import it.csi.risca.riscabesrv.dto.ProfilazioneDTO;

/**
 * The type Profilazione api service.
 *
 * @author CSI PIEMONTE
 */

@Component
public class ProfilazioneApiServiceImpl  extends BaseApiServiceImpl implements ProfilazioneApi{
	
	private static final String IDENTITY = "identity";

	@Autowired
    private ProfilazioneDAO profilazioneDAO;
	
    @Autowired
    IrideServiceHelper serviceHelper;
	
	@Override
	public Response loadProfilazione(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
        LOGGER.debug("[ProfilazioneApiServiceImpl::loadProfilazione] BEGIN");

        String ruolo = "";
		MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
		Iterator<Map.Entry<String, List<String>>> s = headers.entrySet().iterator();
		while (s.hasNext()) {
			Map.Entry<String, List<String>> obj = s.next();
			String key = obj.getKey();
			if(key.equals("Shib-Iride-IdentitaDigitale")) {
				String marker = obj.getValue().get(0);
				try {
					Identita identita = new Identita(normalizeToken(marker));
                    
                    Application cod = new Application();
                    cod.setId("RISCA");
                    
                    Ruolo[] ruoli = serviceHelper.getRuoli(identita, cod);
                    if(ruoli != null && ruoli.length > 0 ) {
                        LOGGER.debug("[loadProfilazione::getRuoli] ruoli trovati: " + ruoli.length );
                    	LOGGER.debug("[loadProfilazione::getRuoli] ruolo: " + ruoli[0].getCodiceRuolo() );
                    	   ruolo = ruoli[0].getCodiceRuolo();
                    }

                 
				} catch (MalformedIdTokenException e) {
					LOGGER.error("[loadProfilazione::getRuoli] errore. " + e);
				}
			}
		}
        if(ruolo.equals("")) {
            ruolo = "AMMINISTRATORE";
        }
		ProfilazioneDTO profilazione = null;
		try {
			profilazione = profilazioneDAO.loadProfilazione(ruolo);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
        LOGGER.debug("[ProfilazioneApiServiceImpl::loadProfilazione] END");
		return Response.ok(profilazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
    private String normalizeToken(String token) {
        return token;
    }
	
}
