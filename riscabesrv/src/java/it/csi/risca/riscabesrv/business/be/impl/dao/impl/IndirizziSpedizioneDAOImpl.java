/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.impl.dao.IndirizziSpedizioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;

/**
 * The type Indirizzi Spedizione DAO Impl.
 *
 * @author CSI PIEMONTE
 */
public class IndirizziSpedizioneDAOImpl extends RiscaBeSrvGenericDAO<IndirizzoSpedizioneDTO> implements IndirizziSpedizioneDAO {

    
    private static final String QUERY_INSERT_RECAPITO_POSTEL = "INSERT INTO risca_r_recapito_postel (id_recapito_postel, id_recapito, id_gruppo_soggetto, destinatario_postel, presso_postel, indirizzo_postel, citta_postel, " +
    		"provincia_postel, cap_postel, frazione_postel, nazione_postel, ind_valido_postel, gest_attore_ins, " + 
    		"gest_data_ins, gest_data_upd, gest_attore_upd, gest_uid) VALUES(nextval('seq_risca_r_recapito_postel'), :idRecapito, :idGruppoSoggetto, :destinatarioPostel, :pressoPostel, :indirizzoPostel, :cittaPostel, " +
    		":provinciaPostel, :capPostel, :frazionePostel, :nazionePostel, :indValidoPostel, :gestAttoreIns, :gestDataIns, :gestDataUpd, :gestAttoreUpd, :gestUid); ";
    
    private static final String QUERY_LOAD_INDIRIZZO_SPEDIZIONE = " SELECT * FROM risca_r_recapito_postel ";
    
    private static final String QUERY_LOAD_INDIRIZZO_SPEDIZIONE_BY_ID_RECAPITO = QUERY_LOAD_INDIRIZZO_SPEDIZIONE 
    		+ " WHERE id_recapito = :idRecapito ";
    
    private static final String QUERY_LOAD_INDIRIZZO_SPEDIZIONE_BY_ID_GRUPPO_SOGGETTO = QUERY_LOAD_INDIRIZZO_SPEDIZIONE
    		+ " WHERE id_gruppo_soggetto = :idGruppoSoggetto ";
    
    private static final String QUERY_AND_LIST_ID_GRUPPO =" AND id_gruppo_soggetto IN (:listIdGruppoSoggetto)";
    private static final String QUERY_UPDATE_RECAPITO_POSTEL = "UPDATE risca_r_recapito_postel SET  id_recapito =:idRecapito, id_gruppo_soggetto =:idGruppoSoggetto, destinatario_postel = :destinatarioPostel, presso_postel = :pressoPostel, indirizzo_postel = :indirizzoPostel, citta_postel =:cittaPostel, " +
    		"provincia_postel = :provinciaPostel, cap_postel = :capPostel, frazione_postel =:frazionePostel, nazione_postel = :nazionePostel, ind_valido_postel = :indValidoPostel, " + 
    		" gest_data_upd = :gestDataUpd , gest_attore_upd = :gestAttoreUpd, gest_uid = :gestUid "
    		+ "WHERE id_recapito_postel = :idRecapitoPostel;";
    
	@Override
	public IndirizzoSpedizioneDTO saveIndirizziSpedizione(IndirizzoSpedizioneDTO indSped) throws Exception {
		LOGGER.debug("[IndizziSpedizioneDAOImpl::saveIndirizziSpedizione] BEGIN");
        IndirizzoSpedizioneDTO indSpedNew = new IndirizzoSpedizioneDTO();
        Map<String, Object> map = new HashMap<>();
        boolean valid = true;
        
        String nazione = null; 
        if(indSped.getNazionePostel() != null)
        	nazione = indSped.getNazionePostel().toUpperCase();
        
        try {
        	
        	Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			
        	map.put("idRecapito", indSped.getIdRecapito());
        	indSpedNew.setIdRecapito(indSped.getIdRecapito());
        	
            if(indSped.getIdGruppoSoggetto() != null) { 
	        	if(indSped.getIdGruppoSoggetto() > 0 ) {
		            map.put("idGruppoSoggetto", indSped.getIdGruppoSoggetto());
		            indSpedNew.setIdGruppoSoggetto(indSped.getIdGruppoSoggetto());
	        	}else {
		            map.put("idGruppoSoggetto", null);
	        	}
            }else 
	            map.put("idGruppoSoggetto", null);
            
            String destPost= indSped.getDestinatarioPostel();
            if(destPost != null) {
            	String destinatarioPostel = transformString(destPost);
	            if(destinatarioPostel.length() <=100) {
	            	map.put("destinatarioPostel", destinatarioPostel);
	            	indSpedNew.setDestinatarioPostel(destinatarioPostel);

	            }
	            else {
	            	indSpedNew.setDestinatarioPostel(destinatarioPostel);
	            	valid = false;
	            }
            }
            else
            	map.put("destinatarioPostel", null);
            
            String presPost = indSped.getPressoPostel();
            if(presPost != null) {
            	String pressoPostel = transformString(presPost);
	            if(pressoPostel.length() <=100) {
	            	map.put("pressoPostel", pressoPostel);
	            	indSpedNew.setPressoPostel(pressoPostel);
	            }
	            else {
	            	indSpedNew.setPressoPostel(pressoPostel);
	            	valid = false;
	            }
            }
            else
            	map.put("pressoPostel", null);
            
            String indPost = indSped.getIndirizzoPostel();
            if(indPost != null) {
            	String indirizzoPostel = transformString(indPost);
	            if(indirizzoPostel.length() <= 100) {
	            	map.put("indirizzoPostel", indirizzoPostel);
	            	indSpedNew.setIndirizzoPostel(indirizzoPostel);           	
	            }
	            else {
	            	indSpedNew.setIndirizzoPostel(indirizzoPostel); 
	            	valid = false;
	            }
            }
            else
            	map.put("indirizzoPostel", null);
            
            String citPost = indSped.getCittaPostel();
            if(citPost != null) {
            	String cittaPostel = transformString(citPost);
	            if(cittaPostel.length() <=90) {
	            	map.put("cittaPostel", cittaPostel);
	            	indSpedNew.setCittaPostel(cittaPostel);
	            	
	            }
	            else {
	            	indSpedNew.setCittaPostel(citPost);
	            	valid = false;
	            }
            }
            else
            	map.put("cittaPostel", null);
            
            String provPost = indSped.getProvinciaPostel();
            if(provPost != null) {
            	String provinciaPostel = transformString(provPost);
            	if(indSped.getNazionePostel() != null && nazione.equals("ITALIA")) {
		            if(provinciaPostel.length() <=3) {
		            	map.put("provinciaPostel", provinciaPostel);
		            	indSpedNew.setProvinciaPostel(provinciaPostel);		            	
		            }
		            else {
		            	indSpedNew.setProvinciaPostel(provinciaPostel);
		            	valid = false;
		            }
            	}
            	else {
            		map.put("provinciaPostel", provinciaPostel);
	            	indSpedNew.setProvinciaPostel(provinciaPostel);	
            	}
            }
            else
            	map.put("provinciaPostel", null);
            
            String capPost = indSped.getCapPostel();
            if(capPost != null) {
            	String capPostel = transformString(capPost);
            	if(indSped.getNazionePostel() != null && nazione.equals("ITALIA")) {
		            if(capPostel.length() <= 5) {
		            	map.put("capPostel", capPostel);
		            	indSpedNew.setCapPostel(capPostel);
		            	
		            }
		            else {
		            	indSpedNew.setCapPostel(capPostel);
		            	valid = false;
		            }
            	}
            	else {
		            if(capPostel.length() <= 10) {
		            	map.put("capPostel", capPostel);
		            	indSpedNew.setCapPostel(capPostel);
		            	
		            }
		            else {
		            	indSpedNew.setCapPostel(capPostel);
		            	valid = false;
		            }
            	}
            }
            else
            	map.put("capPostel", null);
            
            String frazPost = indSped.getFrazionePostel();
            if(frazPost != null) {
            	String frazionePostel = transformString(frazPost);
	            if(frazionePostel.length() <= 100) {
	            	map.put("frazionePostel", frazionePostel);
	            	indSpedNew.setFrazionePostel(frazionePostel);            	
	            }
	            else {
	            	indSpedNew.setFrazionePostel(frazionePostel);
	            	valid = false;
	            }
            }
            else
            	map.put("frazionePostel", null);
            
            String nazPost = indSped.getNazionePostel();
            if(nazPost != null) {
            	String nazionePostel = transformString(nazPost);
	            if(nazionePostel.length() <= 60) {
	            	map.put("nazionePostel", nazionePostel);
		            indSpedNew.setNazionePostel(nazionePostel);       
	            }
	            else {
	            	indSpedNew.setNazionePostel(nazionePostel);
	            	valid = false;
	            }
            }
            else
            	map.put("nazionePostel", null);
            
            if(valid)
            	indSpedNew.setIndValidoPostel(1L);
            else
            	indSpedNew.setIndValidoPostel(0L);
            
            map.put("indValidoPostel", indSpedNew.getIndValidoPostel());
			map.put("gestDataIns", now);
			map.put("gestAttoreIns", indSped.getGestAttoreIns());
			indSpedNew.setGestAttoreIns(indSped.getGestAttoreIns());
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", indSped.getGestAttoreUpd());
			indSpedNew.setGestAttoreUpd(indSped.getGestAttoreUpd());
			map.put("gestUid", generateGestUID(indSped.getGestAttoreIns() + indSped.getGestAttoreUpd() + now));
            if(indSpedNew.getIndValidoPostel() != null && indSpedNew.getIndValidoPostel() != 0L) {
	            KeyHolder keyHolder = new GeneratedKeyHolder();
	            MapSqlParameterSource paramsPostel = getParameterValue(map);
	            template.update(getQuery(QUERY_INSERT_RECAPITO_POSTEL, null, null), paramsPostel, keyHolder, new String[]{"id_recapito_postel"});  	
	            Number key = keyHolder.getKey();
	            Long idRecapitoPostel = key.longValue();
	            indSpedNew.setIdRecapitoPostel(idRecapitoPostel);
	            LOGGER.debug("[IndizziSpedizioneDAOImpl::saveIndirizziSpedizione] END");
	            return indSpedNew;
            }
            else {
    	        Long generatedIdRecapitoPostel = findNextSequenceValue("seq_risca_r_recapito_postel");
            	LOGGER.debug("[IndizziSpedizioneDAOImpl::saveIndirizziSpedizione] Indirizzo spedizione non valido");
                LOGGER.debug("[IndizziSpedizioneDAOImpl::saveIndirizziSpedizione] END");
                indSpedNew.setIdRecapitoPostel(generatedIdRecapitoPostel);
                return indSpedNew;
            }
            
		} catch (Exception e) {
            LOGGER.error("[IndizziSpedizioneDAOImpl::saveIndirizziSpedizione] Errore generale ", e);
            LOGGER.debug("[IndizziSpedizioneDAOImpl::saveIndirizziSpedizione] END");
            throw e;
		}
	}

	
	@Override
	public IndirizzoSpedizioneDTO updateIndirizziSpedizione(IndirizzoSpedizioneDTO indSped, Long modVerifica) throws GenericExceptionList {
		LOGGER.debug("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] BEGIN");
        IndirizzoSpedizioneDTO indSpedNew = new IndirizzoSpedizioneDTO();
        Map<String, Object> map = new HashMap<>();
        ErrorDTO[] errorArray = new ErrorDTO[7];
        List<ErrorDTO> errorsList = new ArrayList<ErrorDTO>();
        boolean valid = true;
        
        
        map.put("idRecapitoPostel", indSped.getIdRecapitoPostel());
        
        String nazione = null; 
        if(indSped.getNazionePostel() != null)
        	nazione = indSped.getNazionePostel().toUpperCase();
        
        if(modVerifica == null || modVerifica == 0) {
	        try {
	        	
	        	Calendar cal = Calendar.getInstance();
				Date now = cal.getTime();
				
	        	map.put("idRecapito", indSped.getIdRecapito());
	        	indSpedNew.setIdRecapito(indSped.getIdRecapito());
	            if(indSped.getIdGruppoSoggetto() != null) { 
		        	if(indSped.getIdGruppoSoggetto() > 0 ) {
			            map.put("idGruppoSoggetto", indSped.getIdGruppoSoggetto());
			            indSpedNew.setIdGruppoSoggetto(indSped.getIdGruppoSoggetto());
		        	}else {
			            map.put("idGruppoSoggetto", null);
		        	}
	            }else 
		            map.put("idGruppoSoggetto", null);
	            
	            String destPost= indSped.getDestinatarioPostel();
	            if(destPost != null) {
	            	String destinatarioPostel = transformString(destPost);
		            if(destinatarioPostel.length() <=100) {
		            	map.put("destinatarioPostel", destinatarioPostel);
		            	indSpedNew.setDestinatarioPostel(destinatarioPostel);

		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E056", "Attenzione: Il valore del campo \"Destinatario\" eccede il numero dei caratteri previsti (max 100):\n" + 
		            			"E' necessario ridurre a 100 il numero di caratteri del \"Destinatario\" per proseguire.", null, null);
		            	errorsList.add(error);
		            	indSpedNew.setDestinatarioPostel(destinatarioPostel);
		            	valid = false;
		            }
	            }
	            else
	            	map.put("destinatarioPostel", null);
	            
	            String presPost = indSped.getPressoPostel();
	            if(presPost != null) {
	            	String pressoPostel = transformString(presPost);
		            if(pressoPostel.length() <=100) {
		            	map.put("pressoPostel", pressoPostel);
		            	indSpedNew.setPressoPostel(pressoPostel);
		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E057", "Attenzione: Il valore del campo \"Presso\" eccede il numero dei caratteri previsti (max 100):\n" + 
		            			"E' necessario ridurre a 100 il numero di caratteri del \"Presso\" per proseguire.", null, null);
		            	errorsList.add(error);
		            	indSpedNew.setPressoPostel(pressoPostel);
		            	valid = false;
		            }
	            }
	            else
	            	map.put("pressoPostel", null);
	            
	            String indPost = indSped.getIndirizzoPostel();
	            if(indPost != null) {
	            	String indirizzoPostel = transformString(indPost);
		            if(indirizzoPostel.length() <= 100) {
		            	map.put("indirizzoPostel", indirizzoPostel);
		            	indSpedNew.setIndirizzoPostel(indirizzoPostel);           	
		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E059", "Attenzione: Il valore del campo \"Indirizzo\" eccede il numero dei caratteri previsti (max 100):\n" + 
		            			"E' necessario ridurre a 100 il numero di caratteri del \" Indirizzo \" per proseguire.", null, null);
		            	errorsList.add(error);
		            	indSpedNew.setIndirizzoPostel(indirizzoPostel); 
		            	valid = false;
		            }
	            }
	            else
	            	map.put("indirizzoPostel", null);
	            
	            String citPost = indSped.getCittaPostel();
	            if(citPost != null) {
	            	String cittaPostel = transformString(citPost);
		            if(cittaPostel.length() <=90) {
		            	map.put("cittaPostel", cittaPostel);
		            	indSpedNew.setCittaPostel(cittaPostel);
		            	
		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E062", "Attenzione: Il valore del campo \"Citta'\" eccede il numero dei caratteri previsti (max 90):\n" + 
		            			"E' necessario ridurre a 90 il numero di caratteri del \"Citta'\" per proseguire.", null, null);
		            	indSpedNew.setCittaPostel(citPost);
		            	valid = false;
		            }
	            }
	            else
	            	map.put("cittaPostel", null);
	            
	            String provPost = indSped.getProvinciaPostel();
	            if(provPost != null) {
	            	String provinciaPostel = transformString(provPost);
	            	if(indSped.getNazionePostel() != null && nazione.equals("ITALIA")) {
			            if(provinciaPostel.length() <=3) {
			            	map.put("provinciaPostel", provinciaPostel);
			            	indSpedNew.setProvinciaPostel(provinciaPostel);
			            	
			            }
			            else {
			            	ErrorDTO error = new ErrorDTO("400", "E063", "Attenzione: Il valore del campo \"Provincia\" eccede il numero dei caratteri previsti (max 3):\n" + 
			            			"E' necessario ridurre a 3 il numero di caratteri del \" Provincia \" per proseguire.", null, null);
			            	errorsList.add(error);
			            	indSpedNew.setProvinciaPostel(provinciaPostel);
			            	valid = false;
			            }
	            	}
	            	else {
	            		map.put("provinciaPostel", provinciaPostel);
		            	indSpedNew.setProvinciaPostel(provinciaPostel);	
	            	}
	            }
	            else
	            	map.put("provinciaPostel", null);
	            
	            
	            String capPost = indSped.getCapPostel();
	            if(capPost != null) {
	            	String capPostel = transformString(capPost);
	            	if(indSped.getNazionePostel() != null && nazione.equals("ITALIA")) {
			            if(capPostel.length() <= 5) {
			            	map.put("capPostel", capPostel);
			            	indSpedNew.setCapPostel(capPostel);		            	
			            }
			            else {
			            	ErrorDTO error = new ErrorDTO("400", "E060", "Attenzione: Il valore del campo \"CAP\" eccede il numero dei caratteri previsti (max 5):\n" + 
			            			"E' necessario ridurre a 5 il numero di caratteri del \" CAP \" per proseguire.", null, null);
			            	errorsList.add(error);
			            	indSpedNew.setCapPostel(capPostel);
			            	valid = false;
			            }
	            	}
	            	else {
			            if(capPostel.length() <= 10) {
			            	map.put("capPostel", capPostel);
			            	indSpedNew.setCapPostel(capPostel);
			            	
			            }
			            else {
			            	indSpedNew.setCapPostel(capPostel);
			            	valid = false;
			            }
	            	}
	            }
	            else
	            	map.put("capPostel", null);
	            
	            String frazPost = indSped.getFrazionePostel();
	            if(frazPost != null) {
	            	String frazionePostel = transformString(frazPost);
		            if(frazionePostel.length() <= 100) {
		            	map.put("frazionePostel", frazionePostel);
		            	indSpedNew.setFrazionePostel(frazionePostel);            	
		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E058", "Attenzione: Il valore del campo \"Frazione\" eccede il numero dei caratteri previsti (max 100):\n" + 
		            			"E' necessario ridurre a 100 il numero di caratteri del \"Frazione\" per proseguire.", null, null);
		            	errorsList.add(error);
		            	indSpedNew.setFrazionePostel(frazionePostel);
		            	valid = false;
		            }
	            }
	            else
	            	map.put("frazionePostel", null);
	            
	            String nazPost = indSped.getNazionePostel();
	            if(nazPost != null) {
	            	String nazionePostel = transformString(nazPost);
		            if(nazionePostel.length() <= 60) {
		            	map.put("nazionePostel", nazionePostel);
			            indSpedNew.setNazionePostel(nazionePostel);       
		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E064", "Attenzione: Il valore del campo \"Nazione\" eccede il numero dei caratteri previsti (max 60):\n" + 
		            			"E' necessario ridurre a 60 il numero di caratteri del \"Nazione\" per proseguire.", null, null);
		            	errorsList.add(error);
		            	indSpedNew.setNazionePostel(nazionePostel);
		            	valid = false;
		            }
	            }
	            else
	            	map.put("nazionePostel", nazionePostel);
	            
	            if(valid)
	            	indSpedNew.setIndValidoPostel(1L);
	            else {
	            	LOGGER.debug("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] Indirizzo spedizione non valido");
	                LOGGER.debug("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] END");
	              	indSpedNew.setIndValidoPostel(0L);
	            	throw new GenericExceptionList(errorsList);
	            }
	            map.put("indValidoPostel", indSpedNew.getIndValidoPostel());
				map.put("gestDataUpd", now);
				map.put("gestAttoreUpd", indSped.getGestAttoreUpd());
				indSpedNew.setGestAttoreUpd(indSped.getGestAttoreUpd());
				map.put("gestUid", generateGestUID(indSped.getGestAttoreIns() + indSped.getGestAttoreUpd() + now));
	            if(indSpedNew.getIndValidoPostel() != null && indSpedNew.getIndValidoPostel() != 0L) {
		            KeyHolder keyHolder = new GeneratedKeyHolder();
		            MapSqlParameterSource paramsPostel = getParameterValue(map);
		            template.update(getQuery(QUERY_UPDATE_RECAPITO_POSTEL, null, null), paramsPostel, keyHolder, new String[]{"id_recapito_postel"});  	
		            Number key = keyHolder.getKey();
		            Long idRecapitoPostel = key.longValue();
		            indSpedNew.setIdRecapitoPostel(idRecapitoPostel);
		            LOGGER.debug("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] END");
		            return indSpedNew;
	            }
	        }catch(GenericExceptionList gel) {
	        	indSpedNew.setIndValidoPostel(0L);
	        	throw gel;
	        	
	        
			} catch (Exception e) {
	            LOGGER.error("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] Errore generale ", e);
	            LOGGER.debug("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] END");
	            throw e;
			}
        }        
        else {
	        try {
	        	
	        	Calendar cal = Calendar.getInstance();
				Date now = cal.getTime();
				
	        	map.put("idRecapito", indSped.getIdRecapito());
	        	indSpedNew.setIdRecapito(indSped.getIdRecapito());
		        
	            if(indSped.getIdGruppoSoggetto() != null) { 
		        	if(indSped.getIdGruppoSoggetto() > 0 ) {
			            map.put("idGruppoSoggetto", indSped.getIdGruppoSoggetto());
			            indSpedNew.setIdGruppoSoggetto(indSped.getIdGruppoSoggetto());
		        	}else {
			            map.put("idGruppoSoggetto", null);
		        	}
	            }else 
		            map.put("idGruppoSoggetto", null);
	        	
	            String destPost= indSped.getDestinatarioPostel();
	            if(destPost != null) {
	            	String destinatarioPostel = transformString(destPost);
		            if(destinatarioPostel.length() <=100) {
		            	map.put("destinatarioPostel", destinatarioPostel);
		            	indSpedNew.setDestinatarioPostel(destinatarioPostel);

		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E056", "Attenzione: Il valore del campo \"Destinatario\" eccede il numero dei caratteri previsti (max 100):\n" + 
		            			"E' necessario ridurre a 100 il numero di caratteri del \"Destinatario\" per proseguire.", null, null);
		            	errorsList.add(error);
		            	indSpedNew.setDestinatarioPostel(destinatarioPostel);
		            	valid = false;
		            }
	            }
	            
	            String presPost = indSped.getPressoPostel();
	            if(presPost != null) {
	            	String pressoPostel = transformString(presPost);
		            if(pressoPostel.length() <=100) {
		            	map.put("pressoPostel", pressoPostel);
		            	indSpedNew.setPressoPostel(pressoPostel);
		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E057", "Attenzione: Il valore del campo \"Presso\" eccede il numero dei caratteri previsti (max 100):\n" + 
		            			"E' necessario ridurre a 100 il numero di caratteri del \"Presso\" per proseguire.", null, null);
		            	errorsList.add(error);
		            	indSpedNew.setPressoPostel(pressoPostel);
		            	valid = false;
		            }
	            }
	            
	            String indPost = indSped.getIndirizzoPostel();
	            if(indPost != null) {
	            	String indirizzoPostel = transformString(indPost);
		            if(indirizzoPostel.length() <= 100) {
		            	map.put("indirizzoPostel", indirizzoPostel);
		            	indSpedNew.setIndirizzoPostel(indirizzoPostel);           	
		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E059", "Attenzione: Il valore del campo \"Indirizzo\" eccede il numero dei caratteri previsti (max 100):\n" + 
		            			"E' necessario ridurre a 100 il numero di caratteri del \" Indirizzo \" per proseguire.", null, null);
		            	errorsList.add(error);
		            	indSpedNew.setIndirizzoPostel(indirizzoPostel); 
		            	valid = false;
		            }
	            }
	            
	            String citPost = indSped.getCittaPostel();
	            if(citPost != null) {
	            	String cittaPostel = transformString(citPost);
		            if(cittaPostel.length() <=90) {
		            	map.put("cittaPostel", cittaPostel);
		            	indSpedNew.setCittaPostel(cittaPostel);
		            	
		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E062", "Attenzione: Il valore del campo \"Citta'\" eccede il numero dei caratteri previsti (max 90):\n" + 
		            			"E' necessario ridurre a 90 il numero di caratteri del \"Citta'\" per proseguire.", null, null);
		            	indSpedNew.setCittaPostel(citPost);
		            	valid = false;
		            }
	            }
	            
	            String provPost = indSped.getProvinciaPostel();
	            if(provPost != null) {
	            	String provinciaPostel = transformString(provPost);
	            	if(indSped.getNazionePostel() != null && nazione.equals("ITALIA")) {
			            if(provinciaPostel.length() <=3) {
			            	map.put("provinciaPostel", provinciaPostel);
			            	indSpedNew.setProvinciaPostel(provinciaPostel);
			            	
			            }
			            else {
			            	ErrorDTO error = new ErrorDTO("400", "E063", "Attenzione: Il valore del campo \"Provincia\" eccede il numero dei caratteri previsti (max 3):\n" + 
			            			"E' necessario ridurre a 3 il numero di caratteri del \" Provincia \" per proseguire.", null, null);
			            	errorsList.add(error);
			            	indSpedNew.setProvinciaPostel(provinciaPostel);
			            	valid = false;
			            }
	            	}
	            	else {
	            		map.put("provinciaPostel", provinciaPostel);
		            	indSpedNew.setProvinciaPostel(provinciaPostel);	
	            	}
	            }
	            
	            
	            String capPost = indSped.getCapPostel();
	            if(capPost != null) {
	            	String capPostel = transformString(capPost);
	            	if(indSped.getNazionePostel() != null && nazione.equals("ITALIA")) {
			            if(capPostel.length() <= 5) {
			            	map.put("capPostel", capPostel);
			            	indSpedNew.setCapPostel(capPostel);		            	
			            }
			            else {
			            	ErrorDTO error = new ErrorDTO("400", "E060", "Attenzione: Il valore del campo \"CAP\" eccede il numero dei caratteri previsti (max 5):\n" + 
			            			"E' necessario ridurre a 5 il numero di caratteri del \" CAP \" per proseguire.", null, null);
			            	errorsList.add(error);
			            	indSpedNew.setCapPostel(capPostel);
			            	valid = false;
			            }
	            	}
	            	else {
			            if(capPostel.length() <= 10) {
			            	map.put("capPostel", capPostel);
			            	indSpedNew.setCapPostel(capPostel);
			            	
			            }
			            else {
			            	indSpedNew.setCapPostel(capPostel);
			            	valid = false;
			            }
	            	}
	            }
	            
	            String frazPost = indSped.getFrazionePostel();
	            if(frazPost != null) {
	            	String frazionePostel = transformString(frazPost);
		            if(frazionePostel.length() <= 100) {
		            	map.put("frazionePostel", frazionePostel);
		            	indSpedNew.setFrazionePostel(frazionePostel);            	
		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E058", "Attenzione: Il valore del campo \"Frazione\" eccede il numero dei caratteri previsti (max 100):\n" + 
		            			"E' necessario ridurre a 100 il numero di caratteri del \"Frazione\" per proseguire.", null, null);
		            	errorsList.add(error);
		            	indSpedNew.setFrazionePostel(frazionePostel);
		            	valid = false;
		            }
	            }
	            
	            String nazPost = indSped.getNazionePostel();
	            if(nazPost != null) {
	            	String nazionePostel = transformString(nazPost);
		            if(nazionePostel.length() <= 60) {
		            	map.put("nazionePostel", nazionePostel);
			            indSpedNew.setNazionePostel(nazionePostel);       
		            }
		            else {
		            	ErrorDTO error = new ErrorDTO("400", "E064", "Attenzione: Il valore del campo \"Nazione\" eccede il numero dei caratteri previsti (max 60):\n" + 
		            			"E' necessario ridurre a 60 il numero di caratteri del \"Nazione\" per proseguire.", null, null);
		            	errorsList.add(error);
		            	indSpedNew.setNazionePostel(nazionePostel);
		            	valid = false;
		            }
	            }
	            if(valid)
	            	indSpedNew.setIndValidoPostel(1L);
	            else {
	            	indSpedNew.setIndValidoPostel(0L);
	            	LOGGER.debug("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] Indirizzo spedizione non valido");
	                LOGGER.debug("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] END");
	            	throw new GenericExceptionList(errorsList);
	            }
	            map.put("indValidoPostel", indSpedNew.getIndValidoPostel());
				map.put("gestDataIns", now);
				map.put("gestAttoreIns", indSped.getGestAttoreIns());
				indSpedNew.setGestAttoreIns(indSped.getGestAttoreIns());
				map.put("gestDataUpd", now);
				map.put("gestAttoreUpd", indSped.getGestAttoreUpd());
				indSpedNew.setGestAttoreUpd(indSped.getGestAttoreUpd());
				map.put("gestUid", generateGestUID(indSped.getGestAttoreIns() + indSped.getGestAttoreUpd() + now));
	            if(indSpedNew.getIndValidoPostel() != null && indSpedNew.getIndValidoPostel() != 0L) {
	            	indSpedNew.setIdRecapitoPostel(indSped.getIdRecapitoPostel());
	            	LOGGER.debug("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] END");
		            return indSpedNew;
	            }
	            else {
	              	indSpedNew.setIndValidoPostel(0L);
	            	LOGGER.debug("[IndizziSpedizioneDAOImpl::saveIndirizziSpedizione] Indirizzo spedizione non valido");
	                LOGGER.debug("[IndizziSpedizioneDAOImpl::saveIndirizziSpedizione] END");
	                return indSpedNew;
	            }
	            
			}catch(GenericExceptionList gel) {
	        	throw gel;	        	
			}  catch (Exception e) {
	            LOGGER.error("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] Errore generale ", e);
	            LOGGER.debug("[IndizziSpedizioneDAOImpl::updateIndirizziSpedizione] END");
	            throw e;
			}
        }
		return indSpedNew;
	}
	
	
	
	
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<IndirizzoSpedizioneDTO> getRowMapper() throws SQLException {
		return new IndirizzoSpedizioneRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	 /**
     * The type Indirizzo Spedizione RowMapper.
     */
    public static class IndirizzoSpedizioneRowMapper implements RowMapper<IndirizzoSpedizioneDTO> {

        /**
         * Instantiates a new Indirizzo Spedizione RowMapper.
         *
         * @throws SQLException the sql exception
         */
        public IndirizzoSpedizioneRowMapper() throws SQLException {
            // Instantiate class
        }

        /**
         * Implementations must implement this method to map each row of data
         * in the ResultSet. This method should not call {@code next()} on
         * the ResultSet; it is only supposed to map values of the current row.
         *
         * @param rs     the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return the result object for the current row (may be {@code null})
         * @throws SQLException if a SQLException is encountered getting
         *                      column values (that is, there's no need to catch SQLException)
         */
        @Override
        public IndirizzoSpedizioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	IndirizzoSpedizioneDTO bean = new IndirizzoSpedizioneDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, IndirizzoSpedizioneDTO bean) throws SQLException {
        	bean.setIdRecapito(rs.getLong("id_recapito"));
        	bean.setIdRecapitoPostel(rs.getLong("id_recapito_postel"));
        	if(rs.getLong("id_gruppo_soggetto") != 0) {
                bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
        	}
            bean.setDestinatarioPostel(rs.getString("destinatario_postel"));
            bean.setPressoPostel(rs.getString("presso_postel"));         
            bean.setIndirizzoPostel(rs.getString("indirizzo_postel"));
            bean.setCittaPostel(rs.getString("citta_postel"));
            bean.setProvinciaPostel(rs.getString("provincia_postel"));
            bean.setCapPostel(rs.getString("cap_postel"));
            bean.setFrazionePostel(rs.getString("frazione_postel"));
            bean.setNazionePostel(rs.getString("nazione_postel"));
            bean.setIndValidoPostel(rs.getLong("ind_valido_postel"));
            bean.setGestDataIns(rs.getDate("gest_data_ins"));
            bean.setGestAttoreIns(rs.getString("gest_attore_ins"));           
            bean.setGestDataUpd(rs.getDate("gest_data_upd"));
            bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
            bean.setGestUid(rs.getString("gest_uid"));
         
        }
    
    }

    
    

    
    @JsonProperty("cap_postel")
    private String capPostel;
    
    @JsonProperty("frazione_postel")
    private String frazionePostel;
    
    @JsonProperty("nazione_postel")
    private String nazionePostel;
    
    @JsonProperty("ind_valido_postel")
    private Long indValidoPostel;
    


	public static String transformString(String string) {
	    String newString = string;
	    String aGrave = URLDecoder.decode("\u00E0", StandardCharsets.UTF_8);
	    String eGrave = URLDecoder.decode("\u00E8", StandardCharsets.UTF_8);
	    String eAcute = URLDecoder.decode("\u00E9", StandardCharsets.UTF_8);
	    String iGrave = URLDecoder.decode("\u00EC", StandardCharsets.UTF_8);
	    String oGrave = URLDecoder.decode("\u00F2", StandardCharsets.UTF_8);
	    String uGrave = URLDecoder.decode("\u00F9", StandardCharsets.UTF_8);
	    
		if(string.contains(aGrave))
			newString = string.replaceAll(aGrave, "a" + "'");
		if(string.contains(eGrave))
			newString = string.replaceAll(eGrave, "e" + "'");
		if(string.contains(eAcute))
			newString = string.replaceAll(eAcute, "e" + "'");
		if(string.contains(iGrave))
			newString = string.replaceAll(iGrave, "i" + "'");
		if(string.contains(oGrave))
			newString = string.replaceAll(oGrave, "o" + "'");
		if(string.contains(uGrave))
			newString = string.replaceAll(uGrave, "u" + "'");
		
		return newString.toUpperCase();
	}


	@Override
	public List<IndirizzoSpedizioneDTO> getIndirizzoSpedizioneByIdRecapitoAndIdGruppo(Long idRecapito,
			List<Long> listIdGruppoSoggetto) {
		LOGGER.debug("[IndizziSpedizioneDAOImpl::getIndirizzoSpedizioneByIdRecapitoAndIdGruppo] BEGIN");
        List<IndirizzoSpedizioneDTO> listIndirizzoSpedizioneDTO = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("idRecapito", idRecapito);
        MapSqlParameterSource  params = null;
    	try {
            if(listIdGruppoSoggetto != null) {
                map.put("listIdGruppoSoggetto", listIdGruppoSoggetto);
                params = getParameterValue(map);
        		listIndirizzoSpedizioneDTO =   template.query(QUERY_LOAD_INDIRIZZO_SPEDIZIONE_BY_ID_RECAPITO + QUERY_AND_LIST_ID_GRUPPO, params, getRowMapper());
            }
            else {
            	params = getParameterValue(map);
        		listIndirizzoSpedizioneDTO =   template.query(QUERY_LOAD_INDIRIZZO_SPEDIZIONE_BY_ID_RECAPITO , params, getRowMapper());
            }

		
    	}catch (Exception e) {
   		 LOGGER.error("[IndizziSpedizioneDAOImpl::getIndirizzoSpedizioneByIdRecapitoAndIdGruppo] Errore  ", e);
         return listIndirizzoSpedizioneDTO;
	    }

		LOGGER.debug("[IndizziSpedizioneDAOImpl::getIndirizzoSpedizioneByIdRecapitoAndIdGruppo] END");
        return listIndirizzoSpedizioneDTO;
	}


	@Override
	public List<IndirizzoSpedizioneDTO> loadIndirizziSpedizioneByIdGruppoSoggetto(Long idGruppoSoggetto) {
		LOGGER.debug("[IndizziSpedizioneDAOImpl::loadIndirizziSpedizioneByIdGruppoSoggetto] BEGIN ");
		 List<IndirizzoSpedizioneDTO> listIndirizzoSpedizioneDTO = new ArrayList<>();
		 Map<String, Object> map = new HashMap<>();
		 try {
	        map.put("idGruppoSoggetto", idGruppoSoggetto);
	        MapSqlParameterSource  params =  getParameterValue(map);
	        listIndirizzoSpedizioneDTO =   template.query(QUERY_LOAD_INDIRIZZO_SPEDIZIONE_BY_ID_GRUPPO_SOGGETTO , params, getRowMapper());
    		
    		
		} catch (DataAccessException e) {
            LOGGER.error("[IndizziSpedizioneDAOImpl::loadIndirizziSpedizioneByIdGruppoSoggetto] Errore nell'accesso ai dati", e);
            return listIndirizzoSpedizioneDTO;
		} catch (SQLException e) {
            LOGGER.error("[IndizziSpedizioneDAOImpl::loadIndirizziSpedizioneByIdGruppoSoggetto] Errore nell'esecuzione della query", e);
            return listIndirizzoSpedizioneDTO;
		}
		 LOGGER.debug("[IndizziSpedizioneDAOImpl::loadIndirizziSpedizioneByIdGruppoSoggetto] END ");
		 return listIndirizzoSpedizioneDTO;
	}
    
}
