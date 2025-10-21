/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.filter;

import java.util.HashMap;
import java.util.Map;

import it.csi.risca.riscaboweb.business.be.AccertamentoApi;
import it.csi.risca.riscaboweb.business.be.CalcoloCanoneApi;
import it.csi.risca.riscaboweb.business.be.CalcoloInteressiApi;
import it.csi.risca.riscaboweb.business.be.ComponentiDtApi;
import it.csi.risca.riscaboweb.business.be.DettaglioPagApi;
import it.csi.risca.riscaboweb.business.be.DettaglioPagSearchResultApi;
import it.csi.risca.riscaboweb.business.be.DocumentiAllegatiApi;
import it.csi.risca.riscaboweb.business.be.ElaboraApi;
import it.csi.risca.riscaboweb.business.be.GruppiApi;
import it.csi.risca.riscaboweb.business.be.ImmagineApi;
import it.csi.risca.riscaboweb.business.be.IndirizziSpedizioneApi;
import it.csi.risca.riscaboweb.business.be.LockRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.PagamentoApi;
import it.csi.risca.riscaboweb.business.be.PingApi;
import it.csi.risca.riscaboweb.business.be.ProvvedimentiIstanzeApi;
import it.csi.risca.riscaboweb.business.be.RataSdApi;
import it.csi.risca.riscaboweb.business.be.ReportApi;
import it.csi.risca.riscaboweb.business.be.RiduzioneAumentoApi;
import it.csi.risca.riscaboweb.business.be.RiscossioneApi;
import it.csi.risca.riscaboweb.business.be.SoggettiApi;
import it.csi.risca.riscaboweb.business.be.StampaRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.StatiElaborazioneApi;
import it.csi.risca.riscaboweb.business.be.StatiRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.StatoDebitorioApi;
import it.csi.risca.riscaboweb.business.be.TipiAutorizzazioneApi;
import it.csi.risca.riscaboweb.business.be.TipiElaborazioneApi;
import it.csi.risca.riscaboweb.business.be.TipiProvvedimentoApi;
import it.csi.risca.riscaboweb.business.be.TipiRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.TipiTitoloApi;
import it.csi.risca.riscaboweb.business.be.TipoDilazioneApi;
import it.csi.risca.riscaboweb.business.be.TipoRicercaMorositaApi;
import it.csi.risca.riscaboweb.business.be.TipoRicercaPagamentiApi;
import it.csi.risca.riscaboweb.business.be.TipoRicercaRimborsiApi;
import it.csi.risca.riscaboweb.business.be.TipoUsoApi;
import it.csi.risca.riscaboweb.business.be.UnitaMisuraApi;

public class ResourceClassMapper {

    private static Map<String, Class<?>> pathToResourceClass = new HashMap<>();

    static {
    	// Mappa tra parti di percorso e classi delle risorse
    	pathToResourceClass.put("accertamenti", AccertamentoApi.class);
    	pathToResourceClass.put("calcolo-canone", CalcoloCanoneApi.class);
    	pathToResourceClass.put("calcolo-interessi", CalcoloInteressiApi.class);
    	pathToResourceClass.put("dettaglio-pag", DettaglioPagApi.class);
    	pathToResourceClass.put("dettaglio-pag-search-result", DettaglioPagSearchResultApi.class);
    	
    	pathToResourceClass.put("classificazioni", DocumentiAllegatiApi.class);
     	pathToResourceClass.put("elabora", ElaboraApi.class);
     	pathToResourceClass.put("gruppi", GruppiApi.class);
     	pathToResourceClass.put("gruppi-qry", GruppiApi.class);
    	pathToResourceClass.put("immagine", ImmagineApi.class);
     	pathToResourceClass.put("indirizzi-spedizione", IndirizziSpedizioneApi.class);
      	pathToResourceClass.put("lock-riscossione", LockRiscossioneApi.class);
      	pathToResourceClass.put("pagamenti", PagamentoApi.class);
      	pathToResourceClass.put("ping", PingApi.class);
      	pathToResourceClass.put("rata-sd", RataSdApi.class);
      	pathToResourceClass.put("report", ReportApi.class);
    	pathToResourceClass.put("riduzione-aumento", RiduzioneAumentoApi.class);
      	pathToResourceClass.put("riscossioni", RiscossioneApi.class);
      	pathToResourceClass.put("_search", RiscossioneApi.class);
      	pathToResourceClass.put("_verify_riscossioni_stdebitori", RiscossioneApi.class);
     	pathToResourceClass.put("utenze-comp", RiscossioneApi.class);
    	pathToResourceClass.put("soggetti", SoggettiApi.class);
    	pathToResourceClass.put("soggetti-qry", SoggettiApi.class);
      	pathToResourceClass.put("stampa-riscossione", StampaRiscossioneApi.class);
    	pathToResourceClass.put("stato-elaborazione", StatiElaborazioneApi.class);
    	pathToResourceClass.put("stati-debitori", StatoDebitorioApi.class);
    	pathToResourceClass.put("stati_debitori", StatoDebitorioApi.class);
    	
    	pathToResourceClass.put("_verify_stato_debitorio_invio_speciale", StatoDebitorioApi.class);
    	pathToResourceClass.put("tipi-elaborazione", TipiElaborazioneApi.class);
    	pathToResourceClass.put("dilazione", TipoDilazioneApi.class);
    	pathToResourceClass.put("ricerca_morosita", TipoRicercaMorositaApi.class);
    	pathToResourceClass.put("ricerca_pagamenti", TipoRicercaPagamentiApi.class);
    	pathToResourceClass.put("ricerca_rimborsi", TipoRicercaRimborsiApi.class);
     	pathToResourceClass.put("unita-misura", UnitaMisuraApi.class);
    	
        // Aggiungi tutte le altre classi come necessario
    }

    public static  Class<?> getResourceClassName(String pathInfo) {
        // Ottieni il nome della classe delle risorse basato sulla parte di percorso dall'URL
        Class<?> clazz = getClassForPath(pathInfo);
        if(clazz == null) {
        	 String[] parts = pathInfo.split("/");
             if (parts.length > 1) {
                 String pathPart = parts[1];
                 clazz = getClassForPathPart(pathPart);
                 if (clazz != null) {
                     return clazz;
                 }
             }
        }
        return clazz != null ? clazz : null;
    }

    private static Class<?> getClassForPathPart(String pathPart) {
        return pathToResourceClass.get(pathPart);
    }
    private static Class<?> getClassForPath(String pathPart) {
 	   if (pathPart.contains("tipi-autorizzazione")) {
 	        return TipiAutorizzazioneApi.class;
 	    } else if (pathPart.contains("tipi-riscossione") || pathPart.contains("codice")) {
 	        return TipiRiscossioneApi.class;
 	    } else if (pathPart.contains("tipi-titolo")) {
 	        return TipiTitoloApi.class;
 	    } else if (pathPart.contains("tipi-uso")) {
 	        return TipoUsoApi.class;
 	    } else if (pathPart.contains("componenti-dt")) {
 	        return ComponentiDtApi.class;
 	    } else if (pathPart.contains("provvedimenti-istanze")) {
 	        return ProvvedimentiIstanzeApi.class;
 	    } else if (pathPart.contains("stati-riscossione")) {
 	        return StatiRiscossioneApi.class;
 	    } else if (pathPart.contains("tipi-provvedimentoistanza")) {
	        return TipiProvvedimentoApi.class;
	    }
 	  
	return null;
 	   
 }
    
}
