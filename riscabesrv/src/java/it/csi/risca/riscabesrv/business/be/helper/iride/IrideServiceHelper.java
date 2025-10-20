/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.helper.iride;

import java.io.StringReader;


import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.ServiceException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import it.csi.iride2.iridefed.entity.Ruolo;
import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;
import it.csi.iride2.policy.interfaces.PolicyEnforcerBaseService;
import it.csi.risca.riscabesrv.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscabesrv.util.service.integration.iride.PolicyEnforcerBaseServiceServiceLocator;



/**
 * The type Iride service helper.
 *
 * @author CSI PIEMONTE
 */

public class IrideServiceHelper extends AbstractServiceHelper {

    private it.csi.risca.riscabesrv.util.service.integration.iride.PolicyEnforcerBaseService service;
    /**
     * The Url service.
     */
    protected String urlService = "";

    /**
     * Instantiates a new Anagamb service helper.
     *
     * @param urlService urlService
     */
    public IrideServiceHelper(String urlService) {
        this.urlService = urlService;
        this.service = this.getService(urlService);
    }

    private it.csi.risca.riscabesrv.util.service.integration.iride.PolicyEnforcerBaseService getService(String urlService) {
        LOGGER.debug("[IrideServiceHelper::getService] BEGIN");
        it.csi.risca.riscabesrv.util.service.integration.iride.PolicyEnforcerBaseService server = null;
        try {
            PolicyEnforcerBaseServiceServiceLocator locator = new PolicyEnforcerBaseServiceServiceLocator();
            server =   locator.getPolicyEnforcerBase(new URL(urlService));
            

            LOGGER.debug("[IrideServiceHelper::getService] Service 'JavaServiceDesc' INITIALIZED");
            LOGGER.debug("[IrideServiceHelper::getService] END");
        } catch (Exception e) {//MalformedURLException | ServiceException e) {
            LOGGER.error("[IrideServiceHelper::getService] ERROR : invalid url [" + urlService + "]");
        }
        return server;
    }

    /**
     * Gets ruoli.
     *
     * @param identita identita
     * @param application      application
     * @return Ruoli[] ruoli
     */
    public Ruolo[] getRuoli(Identita identita, Application cod) {
        LOGGER.debug("[IrideServiceHelper::getRuoli] BEGIN");
        Ruolo[] ruoli = null;
        try {
            ruoli = this.service.findRuoliForPersonaInApplication(identita, cod);
            if(ruoli!=null && ruoli.length > 0) {
            	LOGGER.debug("[IrideServiceHelper::getRuoli] ruoli trovati: " + ruoli.length );
            	LOGGER.debug("[IrideServiceHelper::getRuoli] ruolo: " + ruoli[0].getCodiceRuolo() );
            }
            LOGGER.debug("[IrideServiceHelper::getRuoli] END");
            return ruoli;
        } catch (Exception e) {
            LOGGER.error("[IrideServiceHelper::getRuoli] ERROR ", e);
        }
        LOGGER.debug("[IrideServiceHelper::getRuoli] END");
        return ruoli;
    }
    
    public String getCompetenzaTerritoriale(Identita identita, UseCase use) {
        LOGGER.debug("[IrideServiceHelper::getCompetenzaTerritoriale] BEGIN");
        String infoPersona = null;
        String competenzaTerritoriale = null;
        try {
           infoPersona = this.service.getInfoPersonaInUseCase(identita, use);
           DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
           InputSource src = new InputSource();
           src.setCharacterStream(new StringReader(infoPersona));

           Document doc = builder.parse(src);
           competenzaTerritoriale = doc.getElementsByTagName("COMPETENZA_TERRITORIALE").item(0).getTextContent();
           LOGGER.info("VALORE COMPETENZA_TERRITORIALE PARSATO: " + competenzaTerritoriale);
        } catch (Exception e) {
            LOGGER.error("[IrideServiceHelper::getCompetenzaTerritoriale] ERROR ", e);
        }
        LOGGER.debug("[IrideServiceHelper::getCompetenzaTerritoriale] END");
        return competenzaTerritoriale;
    }
    public Long getInfoPersonaInUseCase(Identita identita, UseCase use) {
        LOGGER.debug("[IrideServiceHelper::getInfoPersonaInUseCase] BEGIN");
        String infoPersona = null;
        String ambito = null;
        Long idAmbito = null;
        try {
           infoPersona = this.service.getInfoPersonaInUseCase(identita, use);
//            if(ruoli!=null && ruoli.length > 0) {
//            	LOGGER.debug("[IrideServiceHelper::getRuoli] ruoli trovati: " + ruoli.length );
//            	LOGGER.debug("[IrideServiceHelper::getRuoli] ruolo: " + ruoli[0].getCodiceRuolo() );
//            }
//            LOGGER.debug("[IrideServiceHelper::getRuoli] END");
//            return ruoli;
           DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
           InputSource src = new InputSource();
           src.setCharacterStream(new StringReader(infoPersona));

           Document doc = builder.parse(src);
           ambito = doc.getElementsByTagName("AMBITO").item(0).getTextContent();

           LOGGER.debug("VALORE AMBITO PARSATO: " + ambito);
           idAmbito = Long.parseLong(ambito);
           
        } catch (Exception e) {
            LOGGER.error("[IrideServiceHelper::getRuoli] ERROR ", e);
        }
        LOGGER.debug("[IrideServiceHelper::getRuoli] END");
        return idAmbito;
    }
    
    public boolean isIdentitaAutentica(Identita identita) {
        LOGGER.debug("[IrideServiceHelper::isIdentitaAutentica] BEGIN");
        boolean identitaAutentica = true;
        try {
        	identitaAutentica = this.service.isIdentitaAutentica(identita);
            return identitaAutentica;
        } catch (Exception e) {
            LOGGER.error("[IrideServiceHelper::isIdentitaAutentica] ERROR ", e);
        }
        LOGGER.debug("[IrideServiceHelper::isIdentitaAutentica] END");
        return identitaAutentica;
    }
}
