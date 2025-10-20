/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.dto.CalcoloCanoneDTO;
import it.csi.risca.riscabesrv.dto.DatoTecnicoDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.RiduzioneAumentoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneUsoDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaDTO;
import it.csi.risca.riscabesrv.dto.ambiente.AumentoDTDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatiGeneraliDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatiTecniciAmbienteDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatoTecnicoJsonDTO;
import it.csi.risca.riscabesrv.dto.ambiente.RiduzioneDTDTO;
import it.csi.risca.riscabesrv.dto.ambiente.RiscossioneDatoTecnicoDTO;
import it.csi.risca.riscabesrv.dto.ambiente.TipoUsoDatoTecnicoDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type Riscossione dao.
 *
 * @author CSI PIEMONTE
 */
public class DatoTecnicoAmbienteDAOImpl extends RiscaBeSrvGenericDAO<DatoTecnicoDTO>  {

	
	
	
    @Autowired
    private TipoUsoDAO tipoUsoDAO;
    @Autowired
    private MessaggiDAO messaggiDAO;
	private static final String AMBIENTE = "AMBIENTE";
	private static final String OPERE_PUBBLICHE = "Opere pubbliche";
	private static final String ATTIVITA_ESTRATTIVE = "Attivita estrattive";
	private static final String TRIBUTI = "TRIBUTI";
    
    private static final String QUERY_UPDATE_RISCOSSIONE = "UPDATE risca_t_riscossione "
            + "SET json_dt = to_jsonb(:jsonDt::json) " 
            + "WHERE id_riscossione = :idRiscossione";
    
    private static final String QUERY_INSERT_TIPO_USO_RIDAUM = "INSERT INTO risca_r_uso_ridaum (id_riscossione_uso, id_riduzione_aumento, "  
    		+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid, id_uso_ridaum ) " 
    	    + "VALUES(:idRiscossioneUso, :idRiduzioneAumento, "  
    		+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid, nextval('seq_risca_r_uso_ridaum'))";
    
    private static final String QUERY_INSERT_RISCOSSIONE_USO = "INSERT INTO risca_r_riscossione_uso (id_riscossione_uso, id_riscossione, id_tipo_uso, "
            + "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
            + "VALUES(nextval('seq_risca_r_riscossione_uso'), :idRiscossione, :idTipoUso, "
            + ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";
    
	private static final String QUERY_LOAD_RISCOSSIONE_USO = "SELECT distinct rrru.* FROM risca_r_riscossione_uso rrru "
			+ "WHERE rrru.id_riscossione = :idRiscossione ";
	
	private static final String QUERY_DELETE_TIPO_USO_RIDAUM = "DELETE FROM risca_r_uso_ridaum rrur "
			+ "WHERE rrur.id_riscossione_uso = :idRiscossioneUso ";
	
	private static final String QUERY_DELETE_RISCOSSIONE_USO  = "DELETE FROM risca_r_riscossione_uso rrru "
			+ "WHERE rrru.id_riscossione_uso = :idRiscossioneUso ";
	
    private static final String QUERY_FOR_AMBITO = "   SELECT rda.cod_ambito AS codice  " + 
    		"   FROM risca_d_ambito rda  " + 
    		"   INNER JOIN risca_d_tipo_riscossione rdtr ON rda.id_ambito = rdtr.id_ambito " + 
    		"   INNER JOIN risca_t_riscossione rtr on rdtr.id_tipo_riscossione = rtr.id_tipo_riscossione " + 
    		"   WHERE rtr.id_riscossione = :idRiscossione";
        
	private static final String DATA_RIFERIMENTO = "dataRiferimento";

//    private static final String QUERY_RISCOSSIONE_USO = "SELECT rrru.id_tipo_uso "
//    		+ "FROM risca_r_riscossione_uso rrru "
//            + "WHERE id_riscossione = :idRiscossione";
    
    private static final String QUERY_TIPO_USO_REGOLA= "SELECT rrtur.* "
    		+ "FROM risca_r_tipo_uso_regola rrtur " 
    		+ "WHERE id_tipo_uso in (:idTipoUso) "
    		+ "AND data_inizio <= :dataRiferimento "
    		+ "AND (data_fine is null OR data_fine > current_date)";
    
    private static final String QUERY_TIPO_USO_REGOLA_SINGOLO= "SELECT rrtur.* "
    		+ "FROM risca_r_tipo_uso_regola rrtur " 
    		+ "WHERE id_tipo_uso = :idTipoUso "
    		+ "AND data_inizio <= :dataRiferimento "
    		+ "AND (data_fine is null OR data_fine > current_date)";
    
//    private static final String QUERY_RISCOSSIONE_JSON_DT = "SELECT rtr.json_dt "
//    		+ "FROM risca_t_riscossione rtr "
//            + "WHERE id_riscossione = :idRiscossione";
 
    private static final String QUERY_ALGORITMO = "SELECT rda.script_algoritmo "
    		+ "FROM risca_d_algoritmo rda "
            + "WHERE id_algoritmo = :idAlgoritmo";
    
    private static final String QUERY_FOR_ID_TIPO_USO = "SELECT rdtu.id_tipo_uso "
    		+ "FROM risca_d_tipo_uso rdtu "
            + "WHERE cod_tipo_uso = :codTipoUso";
    
    private static final String QUERY_FOR_COD_TIPO_USO = "SELECT rdtu.cod_tipo_uso "
    		+ "FROM risca_d_tipo_uso rdtu "
            + "WHERE id_tipo_uso = :idTipoUso";

    private static final String QUERY_RIDUZIONE_AUMENTO = "SELECT distinct rdra.* "
            + "FROM risca_d_riduzione_aumento rdra ";
    
    private static final String QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO = QUERY_RIDUZIONE_AUMENTO
            + "WHERE cod_riduzione_aumento = :codRiduzioneAumento ";
    
    private static final String QUERY_DATA_INIZIO_VALIDITA =" rdra.data_inizio_validita <= :dataAttuale ";
    
    private static final String QUERY_DATA_FINE_VALIDITA =" rdra.data_fine_validita >= :dataAttuale";

    private static final String ORDER_BY_ORDINA_RIDUZIONE_AUMENTO =" ORDER BY rdra.ordina_riduzione_aumento ASC ";

    
	public RiscossioneDatoTecnicoDTO saveDatoTecnico(RiscossioneDatoTecnicoDTO dto, String codFisc) throws Exception{
        LOGGER.debug("[DatoTecnicoDAOImpl::saveDatoTecnico] BEGIN");
        JSONObject jsonRisc = new JSONObject();
        try {
            Map<String, Object> map = new HashMap<>();
   		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();
            
            map.put("idRiscossione", dto.getRiscossione().getIdRiscossione());
            String datiTecnici = dto.getRiscossione().getDatiTecnici();

//            JSONObject jsonDT = new JSONObject(datiTecnici);
//           
//            JSONObject json =  new JSONObject();
//    		json.put("gest_UID", dto.getRiscossione().getGestUID());
//    		json.put("id_riscossione", dto.getRiscossione().getIdRiscossione());
//    		json.put("data_modifica", dto.getRiscossione().getDataModifica());
//    		json.put("data_inserimento", dto.getRiscossione().getDataInserimento());
//    		json.put("dati_tecnici", jsonDT);
    		
    		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
    		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    		DatiTecniciAmbienteDTO datiTecniciAmbiente = mapper.readValue(datiTecnici, DatiTecniciAmbienteDTO.class);

            Map<String, TipoUsoDatoTecnicoDTO> mapDT = datiTecniciAmbiente.getUsi();
            DatiGeneraliDTO dg = datiTecniciAmbiente.getDatiGenerali();
            Set<String> keys = mapDT.keySet();

//inizio parte nuova
            String gestMan = datiTecniciAmbiente.getDatiGenerali().getGestioneManuale();
            MapSqlParameterSource params = getParameterValue(map);
            String ambito = template.queryForObject(QUERY_FOR_AMBITO, params, String.class);
            if(ambito.equals("AMBIENTE")) {
                for(String chiave : keys) {
                	if(!mapDT.get(chiave).getUnitaDiMisura().equals("l/sec")) {
                		if (mapDT.get(chiave).getQuantita() == null || mapDT.get(chiave).getQuantita().signum() < 0) { 
            				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E038);
            				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
            	         }
                	}
                	
                	if(mapDT.get(chiave).getUnitaDiMisura().equals("l/sec")) {
                		if( (mapDT.get(chiave).getQuantita() == null || mapDT.get(chiave).getQuantita().signum() < 0) &&
                            	(mapDT.get(chiave).getQuantitaFaldaProfonda() != null 
                            	&& mapDT.get(chiave).getQuantitaFaldaProfonda().compareTo(BigDecimal.ZERO) >= 0) ) 
                				 {                			
                			MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E041);
            				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
            	 
                			}
                	}

                	if (mapDT.get(chiave).getQuantita() != null && mapDT.get(chiave).getQuantitaFaldaProfonda() != null) {
                		BigDecimal quantita = mapDT.get(chiave).getQuantita();
                        // Ottieni la parte intera
                        int interi = String.valueOf(quantita.intValue()).length();
                        // Ottieni la parte decimali
                        BigDecimal decimaliBigDecimal = quantita.remainder(BigDecimal.ONE);
                        int decimali = String.valueOf(decimaliBigDecimal.movePointRight(quantita.scale()).intValue()).length();
                	    if (interi > 6 || decimali > 4) {
                	    	MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E026);
            				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
            	 	
                	    }
                	        // Controllo E027:quantita Deve essere maggiore o uguale a QuantitaFaldaProfonda
                	        if ( mapDT.get(chiave).getQuantitaFaldaProfonda().compareTo(quantita)  > 0) {
                	    		MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E027);
                				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
                	        }
                	}
                	
                	if((mapDT.get(chiave).getQuantita() == null || mapDT.get(chiave).getQuantita().compareTo(BigDecimal.ZERO) < 0)) {
                		mapDT.get(chiave).setQuantita(BigDecimal.ZERO);
                	}
                	if(dg.getPortataDaAssegnare() == null || dg.getPortataDaAssegnare().compareTo(BigDecimal.ZERO) < 0) {
                		dg.setPortataDaAssegnare(BigDecimal.ZERO);
                	}
                	if((mapDT.get(chiave).getQuantitaFaldaProfonda() == null || mapDT.get(chiave).getQuantitaFaldaProfonda().compareTo(BigDecimal.ZERO) <= 0) ) {
                		if(Utils.isNotEmpty(mapDT.get(chiave).getAumento())) {
                			List<AumentoDTDTO> listAumTriplicaz = mapDT.get(chiave).getAumento()
                    				.stream().filter(f -> "Triplicazione".equals(f.getMotivazione())).collect(Collectors.toList());
                    		if(Utils.isNotEmpty(listAumTriplicaz)) {
                    			mapDT.get(chiave).setQuantitaFaldaProfonda(mapDT.get(chiave).getQuantita());
                    			mapDT.get(chiave).setPercFaldaProfonda(new BigDecimal(100));
                    			mapDT.get(chiave).setQuantitaFaldaSuperficiale(BigDecimal.ZERO);
                    			mapDT.get(chiave).setPercQuantitaFaldaSuperficiale(BigDecimal.ZERO);
//                				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E100);
//                				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
                			}
                		}
                		
                	}
                	if(mapDT.size() == 1) {
                    	if(mapDT.get(chiave).getUnitaDiMisura().equals("l/sec") 
                    			&& (mapDT.get(chiave).getQuantita() == null 
                    			|| mapDT.get(chiave).getQuantita().compareTo(BigDecimal.ZERO) == 0)) {
                    		if(dg.getPortataDaAssegnare().compareTo(BigDecimal.ZERO) > 0) {
                    			mapDT.get(chiave).setQuantita(dg.getPortataDaAssegnare());
                        	}
                    	}
                    	
                	}
                		
                }

            }
            
            datiTecniciAmbiente.setDatiGenerali(dg);
            if(gestMan.equals("N")) {
            	boolean ridR1Presente = false;
            	boolean ridR1PresenteNOISO = false;
            	boolean ridR2Presente = false;
            	boolean ridR2PresenteNOISO = false;
            	boolean ridR3Presente = false;
            	boolean ridR3PresenteNOISO = false;
            	boolean ridR5Presente = false;
	            Date dataAttuale = formatter.parse(formatter.format(now));
	            map.put("dataAttuale", dataAttuale);
	            for(String chiave : keys) {
	            	if(chiave.equals("PROD_BENI_SERVIZI") || chiave.equals("PR_BENI_SERV_NO_ISO")) {
	            		String[] usiSpecifici = mapDT.get(chiave).getTipoUsoSpecifico();
	            		List<RiduzioneDTDTO> list = mapDT.get(chiave).getRiduzione();
	            		if(!(usiSpecifici.length > 1) && usiSpecifici.length > 0) {
		        			if(chiave.equals("PROD_BENI_SERVIZI") && usiSpecifici[0].equals("XXXXX-52")) {
		        				for(int i=0; i<list.size(); i++) {
		        					if(list.get(i).getIdRiduzione().equals(6L))
		        						ridR1Presente = true;
		        				}
		        				if(!ridR1Presente) {
        		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
		        					String codiceR1="R1";
		        		            map.put("codRiduzioneAumento", codiceR1);
		        	        		params = getParameterValue(map);
		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	 	    
				            		list.add(rid);
				            		mapDT.get(chiave).setRiduzione(list);
				            		datiTecniciAmbiente.setUsi(mapDT);
		        				}
		        				
		        		    }
		        			if(chiave.equals("PR_BENI_SERV_NO_ISO") && usiSpecifici[0].equals("XXXXX-100")) {	
		        				for(int i=0; i<list.size(); i++) {
		        					if(list.get(i).getIdRiduzione().equals(6L))
		        						ridR1PresenteNOISO = true;
		        				}
		        				if(!ridR1PresenteNOISO) {
        		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
		        					String codiceR1="R1";
		        		            map.put("codRiduzioneAumento", codiceR1);
		        	        		params = getParameterValue(map);
		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	             		
				            		list.add(rid);
				            		mapDT.get(chiave).setRiduzione(list);
				            		datiTecniciAmbiente.setUsi(mapDT);
		        				}
		        		    }
		        			if(chiave.equals("PROD_BENI_SERVIZI") && usiSpecifici[0].equals("XXXXX-55")) {
		        				for(int i=0; i<list.size(); i++) {
		        					if(list.get(i).getIdRiduzione().equals(7L))
		        						ridR2Presente = true;
		        				}
		        				if(!ridR2Presente) {
        		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
		        					String codiceR2="R2";
		        		            map.put("codRiduzioneAumento", codiceR2);
		        	        		params = getParameterValue(map);
		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	 
				            		list.add(rid);
				            		mapDT.get(chiave).setRiduzione(list);
				            		datiTecniciAmbiente.setUsi(mapDT);
		        				}
		        		    }
		        			if(chiave.equals("PR_BENI_SERV_NO_ISO") && usiSpecifici[0].equals("XXXXX-103")) {
		        				for(int i=0; i<list.size(); i++) {
		        					if(list.get(i).getIdRiduzione().equals(7L))
		        						ridR2PresenteNOISO = true;
		        				}
		        				if(!ridR2PresenteNOISO) {
        		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
		        					String codiceR2="R2";
		        		            map.put("codRiduzioneAumento", codiceR2);
		        	        		params = getParameterValue(map);
		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	
				            		list.add(rid);
				            		mapDT.get(chiave).setRiduzione(list);
				            		datiTecniciAmbiente.setUsi(mapDT);
		        				}
		        		    }
		        			if(chiave.equals("PROD_BENI_SERVIZI") && usiSpecifici[0].equals("XXXXX-54")) {
		        				for(int i=0; i<list.size(); i++) {
		        					if(list.get(i).getIdRiduzione().equals(9L))
		        						ridR3Presente = true;
		        				}
		        				if(!ridR3Presente) {
        		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
		        					String codiceR3="R3";		        		
		        		            map.put("codRiduzioneAumento", codiceR3);
		        	        		params = getParameterValue(map);
		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	
				            		list.add(rid);
				            		mapDT.get(chiave).setRiduzione(list);
				            		datiTecniciAmbiente.setUsi(mapDT);
		        				}
		        		    }
		        			if(chiave.equals("PR_BENI_SERV_NO_ISO") && usiSpecifici[0].equals("XXXXX-102")) {
		        				for(int i=0; i<list.size(); i++) {
		        					if(list.get(i).getIdRiduzione().equals(9L))
		        						ridR3PresenteNOISO = true;
		        				}
		        				if(!ridR3PresenteNOISO) {
        		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
		        					String codiceR3="R3";		        		
		        		            map.put("codRiduzioneAumento", codiceR3);
		        	        		params = getParameterValue(map);
		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	
				            		list.add(rid);
				            		mapDT.get(chiave).setRiduzione(list);
				            		datiTecniciAmbiente.setUsi(mapDT);
		        				}
		        		    }
	            		}
	            		Date dataScadEmasIso;
	        			if(chiave.equals("PROD_BENI_SERVIZI") && mapDT.get(chiave).getDataScadenzaEmasIso() != null &&   !mapDT.get(chiave).getDataScadenzaEmasIso().isEmpty() ) {
	        				dataScadEmasIso = formatter.parse(mapDT.get(chiave).getDataScadenzaEmasIso());
	        				int currentYear = cal.get(Calendar.YEAR);
	        				cal.setTime(dataScadEmasIso);
	        				int emasIsoYear = cal.get(Calendar.YEAR);
	        				//int resultCompareDate = dataScadEmasIso.compareTo(now);
	        				if(emasIsoYear >= currentYear) {
	        					for(int i=0; i<list.size(); i++) {
	        						if(list.get(i).getIdRiduzione().equals(5L))
	        							ridR5Presente = true;
	        					}
	        					if(!ridR5Presente) {
        		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
	        						String codiceR5 = "R5";	        		
		        		            map.put("codRiduzioneAumento", codiceR5);
		        	        		params = getParameterValue(map);
		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	
				            		list.add(rid);
				            		mapDT.get(chiave).setRiduzione(list);
				            		datiTecniciAmbiente.setUsi(mapDT);
	        					}

	        				}
	        		    }

	            	}
	            }
	            boolean noUp3Up2 = false;
    			if((keys.contains("AGRICOLO") || keys.contains("AGRICOLO_BNT")) && (keys.contains("ENE_MEDIO_GRANDE") || keys.contains("ENERGETICO_PICCOLO") ||
    					keys.contains("ENERGETICO_MICRO") || keys.contains("ENERGETICO_MEDIO") || keys.contains("GRANDE_IDRO_AGG") || keys.contains("GRANDE_IDRO_VAR") ||
    					keys.contains("MONET_ENERGIA_GRAT") || keys.contains("GRANDE_IDROELETTRICO"))) {
        				if(((keys.contains("AGRICOLO") && keys.contains("AGRICOLO_BNT")) || ((keys.contains("AGRICOLO") && !keys.contains("AGRICOLO_BNT")))) && (keys.contains("ENE_MEDIO_GRANDE") || keys.contains("ENERGETICO_PICCOLO") ||
            					keys.contains("ENERGETICO_MICRO") || keys.contains("ENERGETICO_MEDIO") || keys.contains("GRANDE_IDRO_AGG") || keys.contains("GRANDE_IDRO_VAR") ||
            					keys.contains("MONET_ENERGIA_GRAT") || keys.contains("GRANDE_IDROELETTRICO"))) {
        					
        					
        					////// CALCOLO CANONE
        					
        					BigDecimal singoloCanone;
        					BigDecimal canone = BigDecimal.ZERO;
        					BigDecimal canoneEne = BigDecimal.ZERO;
        					BigDecimal canoneAgr = BigDecimal.ZERO;
        					CalcoloCanoneDTO canoneDTO = new CalcoloCanoneDTO();
        					boolean quantitaAcquaAgricolo = false;
        					try {

        						List<Long> idTipoUsoList = new ArrayList<Long>();
        						for(String key : keys) {
        							if((key.contains("AGRICOLO") || key.contains("ENE_MEDIO_GRANDE") || key.contains("ENERGETICO_PICCOLO") ||
        									key.contains("ENERGETICO_MICRO") || key.contains("ENERGETICO_MEDIO") || keys.contains("GRANDE_IDRO_AGG") || keys.contains("GRANDE_IDRO_VAR") ||
        			    					keys.contains("MONET_ENERGIA_GRAT") || keys.contains("GRANDE_IDROELETTRICO"))) {
        								map.put("codTipoUso", key);
        								params = getParameterValue(map);
        								Long idTipoUso = template.queryForObject(QUERY_FOR_ID_TIPO_USO, params, Long.class);
        								idTipoUsoList.add(idTipoUso);
        							}
        							if(key.equals("AGRICOLO")) {
        								BigDecimal quantita = mapDT.get(key).getQuantita();
        								BigDecimal portataDaAssegnare = datiTecniciAmbiente.getDatiGenerali().getPortataDaAssegnare();
        								
        								if(quantita == null || quantita.equals(BigDecimal.ZERO)) {        									
        									mapDT.get(key).setQuantita(portataDaAssegnare);
        									datiTecniciAmbiente.setUsi(mapDT);
        									quantitaAcquaAgricolo = true;
        								}
        									
        							}
        							
        						}
        			     		
        			        	Map<String, Object> mapTUR = new HashMap<>();
        			            mapTUR.put("idTipoUso", idTipoUsoList);
        			            mapTUR.put(DATA_RIFERIMENTO, now);
        			            
        			            MapSqlParameterSource paramsTUR = getParameterValue(mapTUR);
        			            List<TipoUsoRegolaDTO> tipoUsoRegola = template.query(QUERY_TIPO_USO_REGOLA, paramsTUR, getTipoUsoRegolaRowMapper());
        			            
	            
        			            JSONObject jsonDT = datiTecniciAmbiente.toJsonObj();
        			            JSONObject json =  new JSONObject();
        			    		json.put("gest_UID", dto.getRiscossione().getGestUID());
        			    		json.put("id_riscossione", dto.getRiscossione().getIdRiscossione());
        			    		json.put("data_modifica", dto.getRiscossione().getDataModifica());
        			    		json.put("data_inserimento", dto.getRiscossione().getDataInserimento());
        			    		json.put("dati_tecnici", jsonDT);
        			            jsonRisc.put("riscossione", json);

        			            String jsonString =   jsonRisc.toString();
        			            
        			            
        			            //calcolo
        			            List<BigDecimal> valoriCanone = new ArrayList<BigDecimal>();
        			            String jsonRegola = "";
        			            Long idAlgoritmo;
        			            Map<String, Object> mapIdA = new HashMap<>();
        			            Map<String, BigDecimal> usoECanoneEne = new HashMap<String, BigDecimal>();
        			            Map<String, BigDecimal> usoECanoneAgr = new HashMap<String, BigDecimal>();
        			            BigDecimal minCanoneEne = BigDecimal.ZERO;
        			            int count = 0;
        			            for(int i=0; i<tipoUsoRegola.size(); i++) {
        			            	jsonRegola = tipoUsoRegola.get(i).getJsonRegola();
        			            	idAlgoritmo = tipoUsoRegola.get(i).getIdAlgoritmo();
        			            	mapIdA.put("idAlgoritmo", idAlgoritmo);
        			            	MapSqlParameterSource paramsIdA = getParameterValue(mapIdA);
        			            	String algoritmo= template.queryForObject(QUERY_ALGORITMO, paramsIdA, String.class);
        			            	singoloCanone = calcolaCanone(algoritmo,jsonRegola, jsonString);
        			            	
        			            	//metto nella mappa l'uso e il suo canone
        			            	map.put("idTipoUso", tipoUsoRegola.get(i).getIdTipoUso());
    								params = getParameterValue(map);
        			            	String codTipoUso = template.queryForObject(QUERY_FOR_COD_TIPO_USO, params, String.class);
        			            	if(!codTipoUso.equals("AGRICOLO")) {
        			            		canoneEne = canoneEne.add(singoloCanone);        			            		
        			            		if(count == 0) {       			            			
        			            			minCanoneEne = singoloCanone;
        			            			usoECanoneEne.put(codTipoUso, minCanoneEne);
        			            			count++;
        			            		}
        			            		else {
        			            			if (singoloCanone.compareTo(minCanoneEne) < 0){
        			            				minCanoneEne = singoloCanone;
        			            				usoECanoneEne = new HashMap<String, BigDecimal>();
        			            				usoECanoneEne.put(codTipoUso, minCanoneEne);
        			            			}
        			            		}

        			            	}
        			            	else {
        			            		canoneAgr = singoloCanone;
        			            		usoECanoneAgr.put(codTipoUso, canoneAgr);        			            	
        			            	}
        			            	        			            	


        			            }


        			            boolean ridUP1Presente = false;
        			            if (canoneEne.compareTo(canoneAgr) < 0)  {
        		            		List<RiduzioneDTDTO> list = mapDT.get(usoECanoneEne.keySet().toArray()[0]).getRiduzione();
        		            		for(int i=0; i<list.size(); i++) {
        		            			if(list.get(i).getIdRiduzione().equals(12L))
        		            				ridUP1Presente = true;
        		            		}
        		            		if(!ridUP1Presente) {
            		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
            		            		String codiceUP1="UP1";
    		        		            map.put("codRiduzioneAumento", codiceUP1);
    		        	        		params = getParameterValue(map);
    		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
    		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
    			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
    			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	
        			            		list.add(rid);
        			            		mapDT.get(usoECanoneEne.keySet().toArray()[0]).setRiduzione(list);
        			            		datiTecniciAmbiente.setUsi(mapDT);
        			            		noUp3Up2 = true;
        		            		}
        			            }
        			            else {
        		            		List<RiduzioneDTDTO> list = mapDT.get("AGRICOLO").getRiduzione();
        		            		for(int i=0; i<list.size(); i++) {
        		            			if(list.get(i).getIdRiduzione().equals(12L))
        		            				ridUP1Presente = true;
        		            		}
        		            		if(!ridUP1Presente) {
	        		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
	        		            		String codiceUP1="UP1";
    		        		            map.put("codRiduzioneAumento", codiceUP1);
    		        	        		params = getParameterValue(map);
    		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
    		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
    			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
    			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	
	    			            		list.add(rid);
	    			            		mapDT.get("AGRICOLO").setRiduzione(list);
	    			            		if(quantitaAcquaAgricolo)
	    			            			mapDT.get("AGRICOLO").setQuantita(BigDecimal.ZERO);
	    			            		datiTecniciAmbiente.setUsi(mapDT);
	        			            	noUp3Up2 = true;
        		            		}
        			            }
        			            
        			            
        					} catch (Exception e) {
        						LOGGER.error("Errore nel calcolo della condizione per UP1: " + e);

        					}
        					
        					
        					
        					////// FINE CALCOLO CANONE E CONDIZIONE UP1
        					
        					
        				}
        				
        				else if((!keys.contains("AGRICOLO") && keys.contains("AGRICOLO_BNT")) && (keys.contains("ENE_MEDIO_GRANDE") || keys.contains("ENERGETICO_PICCOLO") ||
            					keys.contains("ENERGETICO_MICRO") || keys.contains("ENERGETICO_MEDIO") || keys.contains("GRANDE_IDRO_AGG") || keys.contains("GRANDE_IDRO_VAR") ||
            					keys.contains("MONET_ENERGIA_GRAT") || keys.contains("GRANDE_IDROELETTRICO"))) {
        					
        					
        					////// CALCOLO CANONE
        					
        					BigDecimal singoloCanone;
        					BigDecimal canone = BigDecimal.ZERO;
        					BigDecimal canoneEne = BigDecimal.ZERO;
        					BigDecimal canoneAgr = BigDecimal.ZERO;
        					CalcoloCanoneDTO canoneDTO = new CalcoloCanoneDTO();

        					try {

        						List<Long> idTipoUsoList = new ArrayList<Long>();
        						for(String key : keys) {
        							if((key.contains("AGRICOLO_BNT") || key.contains("ENE_MEDIO_GRANDE") || key.contains("ENERGETICO_PICCOLO") ||
        									key.contains("ENERGETICO_MICRO") || key.contains("ENERGETICO_MEDIO") || keys.contains("GRANDE_IDRO_AGG") || keys.contains("GRANDE_IDRO_VAR") ||
        			    					keys.contains("MONET_ENERGIA_GRAT") || keys.contains("GRANDE_IDROELETTRICO"))) {
        								map.put("codTipoUso", key);
        								params = getParameterValue(map);
        								Long idTipoUso = template.queryForObject(QUERY_FOR_ID_TIPO_USO, params, Long.class);
        								idTipoUsoList.add(idTipoUso);
        							}
        							
        						}
        			     		// Query su risca_r_tipo_uso_regola con idTipo uso e dataRiferimento compresa tra data_inizio e data_fine per prendere il json_regola e l'id_algoritmo
        			        	Map<String, Object> mapTUR = new HashMap<>();
        			            mapTUR.put("idTipoUso", idTipoUsoList);
        			            mapTUR.put(DATA_RIFERIMENTO, now);
        			            
        			            MapSqlParameterSource paramsTUR = getParameterValue(mapTUR);
        			            List<TipoUsoRegolaDTO> tipoUsoRegola = template.query(QUERY_TIPO_USO_REGOLA, paramsTUR, getTipoUsoRegolaRowMapper());
        			                	    
        			            
        			            JSONObject jsonDT = datiTecniciAmbiente.toJsonObj();
        			            JSONObject json =  new JSONObject();
        			    		json.put("gest_UID", dto.getRiscossione().getGestUID());
        			    		json.put("id_riscossione", dto.getRiscossione().getIdRiscossione());
        			    		json.put("data_modifica", dto.getRiscossione().getDataModifica());
        			    		json.put("data_inserimento", dto.getRiscossione().getDataInserimento());
        			    		json.put("dati_tecnici", jsonDT);
        			            jsonRisc.put("riscossione", json);
        			            
        			            String jsonString =   jsonRisc.toString();
        			            
        			            
        			            //calcolo
        			            List<BigDecimal> valoriCanone = new ArrayList<BigDecimal>();
        			            String jsonRegola = "";
        			            Long idAlgoritmo;
        			            Map<String, Object> mapIdA = new HashMap<>();
        			            Map<String, BigDecimal> usoECanoneEne = new HashMap<String, BigDecimal>();
        			            Map<String, BigDecimal> usoECanoneAgr = new HashMap<String, BigDecimal>();
        			            BigDecimal minCanoneEne = BigDecimal.ZERO;
        			            int count = 0;
        			            for(int i=0; i<tipoUsoRegola.size(); i++) {
        			            	jsonRegola = tipoUsoRegola.get(i).getJsonRegola();
        			            	idAlgoritmo = tipoUsoRegola.get(i).getIdAlgoritmo();
        			            	mapIdA.put("idAlgoritmo", idAlgoritmo);
        			            	MapSqlParameterSource paramsIdA = getParameterValue(mapIdA);
        			            	String algoritmo= template.queryForObject(QUERY_ALGORITMO, paramsIdA, String.class);
        			            	singoloCanone = calcolaCanone(algoritmo,jsonRegola, jsonString);
        			            	
        			            	//metto nella mappa l'uso e il suo canone
        			            	map.put("idTipoUso", tipoUsoRegola.get(i).getIdTipoUso());
    								params = getParameterValue(map);
        			            	String codTipoUso = template.queryForObject(QUERY_FOR_COD_TIPO_USO, params, String.class);
        			            	if(!codTipoUso.equals("AGRICOLO_BNT")) {
        			            		canoneEne = canoneEne.add(singoloCanone);        			            		
        			            		if(count == 0) {       			            			
        			            			minCanoneEne = singoloCanone;
        			            			usoECanoneEne.put(codTipoUso, minCanoneEne);
        			            			count++;
        			            		}
        			            		else {
        			            			if (singoloCanone.compareTo(minCanoneEne) < 0)  {
        			            				minCanoneEne = singoloCanone;
        			            				usoECanoneEne = new HashMap<String, BigDecimal>();
        			            				usoECanoneEne.put(codTipoUso, minCanoneEne);
        			            			}
        			            		}

        			            	}
        			            	else {
        			            		canoneAgr = singoloCanone;
        			            		usoECanoneAgr.put(codTipoUso, canoneAgr);
        			            	}
        			            	        			            	


        			            }

        			            boolean ridUP1Presente = false;
        			            if (canoneEne.compareTo(canoneAgr) < 0) {
        		            		List<RiduzioneDTDTO> list = mapDT.get(usoECanoneEne.keySet().toArray()[0]).getRiduzione();
        		            		for(int i=0; i<list.size(); i++) {
        		            			if(list.get(i).getIdRiduzione().equals(12L))
        		            				ridUP1Presente = true;
        		            		}
        		            		if(!ridUP1Presente) {
	        		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
	        		            		String codiceUP1="UP1";
    		        		            map.put("codRiduzioneAumento", codiceUP1);
    		        	        		params = getParameterValue(map);
    		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
    		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
    			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
    			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	
	    			            		list.add(rid);
	    			            		mapDT.get(usoECanoneEne.keySet().toArray()[0]).setRiduzione(list);
	    			            		datiTecniciAmbiente.setUsi(mapDT);
	    			            		noUp3Up2 = true;
        		            		}
        			            }
        			            else {
        		            		List<RiduzioneDTDTO> list = mapDT.get("AGRICOLO_BNT").getRiduzione();
        		            		for(int i=0; i<list.size(); i++) {
        		            			if(list.get(i).getIdRiduzione().equals(12L))
        		            				ridUP1Presente = true;
        		            		}
        		            		if(!ridUP1Presente) {
	        		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
	        		            		String codiceUP1="UP1";
    		        		            map.put("codRiduzioneAumento", codiceUP1);
    		        	        		params = getParameterValue(map);
    		        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
    		        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
    			        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
    			        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	
	    			            		list.add(rid);
	    			            		mapDT.get("AGRICOLO_BNT").setRiduzione(list);
	    			            		datiTecniciAmbiente.setUsi(mapDT);
	        			            	noUp3Up2 = true;
        		            		}
        			            }
        			            
        			            
        					} catch (Exception e) {

        						LOGGER.error("Errore nel calcolo della condizione per UP1: " + e);

        					}
		
        					////// FINE CALCOLO CANONE E CONDIZIONE UP1
        					
        					
        				}
        			}
    			
    			//UP3
    			boolean noUp2 = false;
    			if(keys.contains("CIVILE")) {
    				BigDecimal portata = mapDT.get("CIVILE").getQuantita();
    				BigDecimal portataComplessConcessa = BigDecimal.ZERO;
    				
    				for(String key : keys) {			 
    						if((key.equals("AGRICOLO") && !noUp3Up2))
    							portataComplessConcessa = portataComplessConcessa.add(mapDT.get(key).getQuantita());
    						else if(mapDT.get(key).getUnitaDiMisura().equals("l/sec")) 
    							portataComplessConcessa = portataComplessConcessa.add(mapDT.get(key).getQuantita());
    					
    				}
    				portataComplessConcessa = portataComplessConcessa.add(datiTecniciAmbiente.getDatiGenerali().getPortataDaAssegnare());
    				

    				BigDecimal divisor = new BigDecimal("2");
    				BigDecimal range = new BigDecimal("0.1");
    				boolean ridUP3Presente = false;
    				if (portata.compareTo(BigDecimal.ZERO) > 0 && portata.compareTo(range) < 0 && portata.compareTo(portataComplessConcessa.divide(divisor)) < 0) {
	            		List<RiduzioneDTDTO> list = mapDT.get("CIVILE").getRiduzione();
	            		for(int l = 0; l<list.size(); l++) {
	            			if(list.get(l).getIdRiduzione().equals(3L))
	            				ridUP3Presente=true;
	            		}
	            		if(!ridUP3Presente) {
		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
		            		String codiceUP3 ="UP3";
        		            map.put("codRiduzioneAumento", codiceUP3);
        	        		params = getParameterValue(map);
        					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
        				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
	        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
	        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	
		            		list.add(rid);
		            		mapDT.get("CIVILE").setRiduzione(list);
		            		datiTecniciAmbiente.setUsi(mapDT);
			            	noUp2 = true;			           
	            		}

    				}
  				
    			}
    			
    			int countUsi = 0;
    			for(String uso : keys) {
    				if(uso.equals("AGRICOLO") && noUp3Up2)
						continue;
    				else if(mapDT.get(uso).getUnitaDiMisura().equals("l/sec") && (mapDT.get(uso).getQuantita().compareTo(BigDecimal.ZERO) == 0 || mapDT.get(uso).getQuantita() == null))
    					countUsi++;
    			}
    			
    			//UP2
    			boolean ridUP2Presente = false;
    			if(countUsi > 1) {
    			if(datiTecniciAmbiente.getDatiGenerali().getPortataDaAssegnare() != null &&     datiTecniciAmbiente.getDatiGenerali().getPortataDaAssegnare().compareTo(BigDecimal.ZERO) > 0) {

    				
    				BigDecimal canoneSingolo;
					BigDecimal can = BigDecimal.ZERO;
		            Map<String, BigDecimal> usoECanone = new HashMap<String, BigDecimal>();
//					BigDecimal canoneEne = BigDecimal.ZERO;
//					BigDecimal canoneAgr = BigDecimal.ZERO;
					CalcoloCanoneDTO canoneDTO = new CalcoloCanoneDTO();
    				for(String uso : keys) {
    					if((uso.equals("CIVILE") && noUp2) || ((uso.equals("AGRICOLO") || uso.equals("AGRICOLO_BNT") || uso.equals("ENE_MEDIO_GRANDE") || uso.equals("ENERGETICO_PICCOLO") ||
    							uso.equals("ENERGETICO_MICRO") || uso.equals("ENERGETICO_MEDIO")) && noUp3Up2)) {
    						continue;
    					}
    					else if(mapDT.get(uso).getUnitaDiMisura().equals("l/sec") && (mapDT.get(uso).getQuantita().compareTo(BigDecimal.ZERO) == 0 || mapDT.get(uso).getQuantita() == null)) {
        					try {

        			            Map<String, Object> mapUP2 = new HashMap<>();
        			            MapSqlParameterSource paramsUP2; 

        						mapUP2.put("codTipoUso", uso);
    							paramsUP2 = getParameterValue(mapUP2);;
    							Long idTipoUso = template.queryForObject(QUERY_FOR_ID_TIPO_USO, paramsUP2, Long.class);
    							Map<String, Object> mapTURUP2 = new HashMap<>();
        			        	mapTURUP2.put("idTipoUso", idTipoUso);
        			        	mapTURUP2.put(DATA_RIFERIMENTO, now);
        			            
        			            MapSqlParameterSource paramsTURUP2 = getParameterValue(mapTURUP2);
        			            TipoUsoRegolaDTO tipoUsoRegola = (TipoUsoRegolaDTO) template.queryForObject(QUERY_TIPO_USO_REGOLA_SINGOLO, paramsTURUP2, getTipoUsoRegolaRowMapper());
    			            
        			            JSONObject jsonDT = datiTecniciAmbiente.toJsonObj();
        			            JSONObject json =  new JSONObject();
        			    		json.put("gest_UID", dto.getRiscossione().getGestUID());
        			    		json.put("id_riscossione", dto.getRiscossione().getIdRiscossione());
        			    		json.put("data_modifica", dto.getRiscossione().getDataModifica());
        			    		json.put("data_inserimento", dto.getRiscossione().getDataInserimento());
        			    		json.put("dati_tecnici", jsonDT);
        			            jsonRisc.put("riscossione", json);
        			            
        			            String jsonString =   jsonRisc.toString();
        			               			            
        			            //calcolo
        			            List<BigDecimal> valoriCanone = new ArrayList<BigDecimal>();
        			            String jsonRegola = "";
        			            Long idAlgoritmo;
        			            Map<String, Object> mapIdAUP2 = new HashMap<>();


        			            BigDecimal minCanoneEne = BigDecimal.ZERO;
        			            int count = 0;

    			            	jsonRegola = tipoUsoRegola.getJsonRegola();
    			            	idAlgoritmo = tipoUsoRegola.getIdAlgoritmo();
    			            	mapIdAUP2.put("idAlgoritmo", idAlgoritmo);
    			            	MapSqlParameterSource paramsIdAUP2 = getParameterValue(mapIdAUP2);
    			            	String algoritmo= template.queryForObject(QUERY_ALGORITMO, paramsIdAUP2, String.class);
    			            	canoneSingolo = calcolaCanone(algoritmo,jsonRegola, jsonString);
    			            	
    			            	//metto nella mappa l'uso e il suo canone
    			            	map.put("idTipoUso", tipoUsoRegola.getIdTipoUso());
								params = getParameterValue(map);
    			            	String codTipoUso = template.queryForObject(QUERY_FOR_COD_TIPO_USO, params, String.class);

    			            	usoECanone.put(codTipoUso, canoneSingolo);
        					} catch (Exception e) {

        						LOGGER.error("Errore nel calcolo della condizione per UP2: " + e);

        					}
        					
    					}
    				}
//        			            }

        			            // leggo la mappa
        			              // verifico tra gli usi quale canone e piu grande
        			              // a questo setto la quantita uguale alla portata da assegnare generale
        			              // ciclo ancora, escludendo l'uso di sopra, e imposto la riduzione up2
        			            Map.Entry<String, BigDecimal> maxEntry = null;
        			            for (Map.Entry<String, BigDecimal> entry : usoECanone.entrySet()) {
        			              if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
        			                maxEntry = entry;
        			              }
        			            }
        			            String maxKey = maxEntry.getKey();
        			            
        			            for(String keyUso : keys) {
        		    				if(keyUso.equals("AGRICOLO") && noUp3Up2)
        								continue;
        			            	if(keyUso.equals(maxKey)) {
        			            		mapDT.get(keyUso).setQuantita(datiTecniciAmbiente.getDatiGenerali().getPortataDaAssegnare());
        			            		datiTecniciAmbiente.setUsi(mapDT);
        			            	}
        			            	else if(mapDT.get(keyUso).getUnitaDiMisura().equals("l/sec") && (mapDT.get(keyUso).getQuantita().equals(BigDecimal.ZERO)) || mapDT.get(keyUso).getQuantita() == null){
            		            		List<RiduzioneDTDTO> list = mapDT.get(keyUso).getRiduzione();
            		            		for(int j=0; j<list.size(); j++) {
            		            			if(list.get(j).getIdRiduzione().equals(2L))
            		            				ridUP2Presente = true;
            		            		}
            		            		if(!ridUP2Presente) {
	            		            		RiduzioneDTDTO rid = new RiduzioneDTDTO();
	            		            		String codiceUP2="UP2";
	                    		            map.put("codRiduzioneAumento", codiceUP2);
	                    	        		params = getParameterValue(map);
	                    					RiduzioneAumentoDTO riduzioneAumentoDTO = template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA  + " and ("+ QUERY_DATA_FINE_VALIDITA  +" or rdra.data_fine_validita is null ) "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapperRiduzioneAumento());
	                    				    rid.setIdRiduzione(riduzioneAumentoDTO.getIdRiduzioneAumento());
	            	        				rid.setMotivazione(riduzioneAumentoDTO.getDesRiduzioneAumento());
	            	        				rid.setPercentuale(riduzioneAumentoDTO.getPercRiduzioneAumento());	
	        			            		list.add(rid);
	        			            		mapDT.get(keyUso).setRiduzione(list);
	        			            		datiTecniciAmbiente.setUsi(mapDT);
            		            		}
        			            	}
        			            }
    			}
              }
            }
//fine parte nuova

           // JSONObject jsonDT = new JSONObject(datiTecnici);

            JSONObject jsonDT = datiTecniciAmbiente.toJsonObj();
            JSONObject json =  new JSONObject();
    		json.put("gest_UID", dto.getRiscossione().getGestUID());
    		json.put("id_riscossione", dto.getRiscossione().getIdRiscossione());
    		json.put("data_modifica", dto.getRiscossione().getDataModifica());
    		json.put("data_inserimento", dto.getRiscossione().getDataInserimento());
    		json.put("dati_tecnici", jsonDT);
            jsonRisc.put("riscossione", json);
            
            String jsonString =   jsonRisc.toString();

            map.put("jsonDt", jsonString);

            params = getParameterValue(map);

            template.update(getQuery(QUERY_UPDATE_RISCOSSIONE, null, null), params);
  
        	List<RiscossioneUsoDTO>   ListRiscossioneUso = template.query(QUERY_LOAD_RISCOSSIONE_USO, params, getRiscossioneUsoRowMapper());
  			for (RiscossioneUsoDTO riscossioneUso : ListRiscossioneUso) {
            	if(riscossioneUso.getIdRiscossioneUso() != null) {
            		Map<String, Object> myMap = new HashMap<>();
            		myMap.put("idRiscossioneUso", riscossioneUso.getIdRiscossioneUso());

  					template.update(QUERY_DELETE_TIPO_USO_RIDAUM, myMap);
  					template.update(QUERY_DELETE_RISCOSSIONE_USO, myMap);
  				}
  			}  
            KeyHolder keyHolderProvv = new GeneratedKeyHolder();
            Number key = keyHolderProvv.getKey();
            for(String k : keys) {
  
	            map.put("idRiscossione", dto.getRiscossione().getIdRiscossione());
	            map.put("idTipoUso", mapDT.get(k).getIdTipoUsoLegge());
	            map.put("gestDataIns", now);
	            map.put("gestAttoreIns", codFisc);
	            map.put("gestDataUpd", now);
	            map.put("gestAttoreUpd", codFisc);
	            map.put("gestUid", generateGestUID(codFisc + codFisc + now));
	            MapSqlParameterSource paramsRiscUso = getParameterValue(map);
	            template.update(getQuery(QUERY_INSERT_RISCOSSIONE_USO, null, null), paramsRiscUso, keyHolderProvv, new String[]{"id_riscossione_uso"});
	            if(mapDT.get(k).getTipoUsoSpecifico().length >0) {
	            	for (String tipoUsoSpecifico : mapDT.get(k).getTipoUsoSpecifico()) {
	            		TipoUsoExtendedDTO tipoUsoExtendedDTO = tipoUsoDAO.loadTipoUsoByIdTipoUsoOrCodTipoUso(tipoUsoSpecifico);
	            	    KeyHolder keyHolderProvvUsoSpecifico = new GeneratedKeyHolder();
	            		map.put("idRiscossione", dto.getRiscossione().getIdRiscossione());
			            map.put("idTipoUso", tipoUsoExtendedDTO.getIdTipoUso());
			            map.put("gestDataIns", now);
			            map.put("gestAttoreIns", codFisc);
			            map.put("gestDataUpd", now);
			            map.put("gestAttoreUpd", codFisc);
			            map.put("gestUid", generateGestUID(codFisc + codFisc + now));
			            MapSqlParameterSource paramsRiscSpeci = getParameterValue(map);
			            template.update(getQuery(QUERY_INSERT_RISCOSSIONE_USO, null, null), paramsRiscSpeci, keyHolderProvvUsoSpecifico, new String[]{"id_riscossione_uso"});
				            
					}
	            }
	            key = keyHolderProvv.getKey();
	            List<RiduzioneDTDTO> listRiduzioni = mapDT.get(k).getRiduzione();
	            List<AumentoDTDTO> listAumenti = mapDT.get(k).getAumento();
	            
	            if(key != null) {
	            	if(listRiduzioni != null) {
		            	for(RiduzioneDTDTO rid : listRiduzioni) {
				            map.put("idRiscossioneUso", key.longValue());
				            map.put("idRiduzioneAumento", rid.getIdRiduzione());
				            map.put("gestDataIns", now);
				            map.put("gestAttoreIns", codFisc);
				            map.put("gestDataUpd", now);
				            map.put("gestAttoreUpd", codFisc);
				            map.put("gestUid", generateGestUID(codFisc + codFisc + now));
				
				            MapSqlParameterSource paramsRid = getParameterValue(map);
				
				            template.update(getQuery(QUERY_INSERT_TIPO_USO_RIDAUM, null, null), paramsRid);
		            		}
	            	}
	            	if(listAumenti != null) {
		            	for(AumentoDTDTO aum : listAumenti) {
				            map.put("idRiscossioneUso", key.longValue());
				            map.put("idRiduzioneAumento", aum.getIdAumento());
				            map.put("gestDataIns", now);
				            map.put("gestAttoreIns", codFisc);
				            map.put("gestDataUpd", now);
				            map.put("gestAttoreUpd", codFisc);
				            map.put("gestUid", generateGestUID(codFisc + codFisc + now));
				
				            MapSqlParameterSource paramsAum = getParameterValue(map);
				
				            template.update(getQuery(QUERY_INSERT_TIPO_USO_RIDAUM, null, null), paramsAum);
		            	}
	            	}
	            }

            }
        
            String st = datiTecniciAmbiente.toJsonString();

            dto.getRiscossione().setDatiTecnici(st);
            return dto; //key.longValue();
        }catch (BusinessException e) {
        	LOGGER.error("[DatoTecnicoDaoImpl::saveDatoTecnico] ERROR : " +e);
        	throw e;
        } 
        catch (Exception e) {
        	LOGGER.error("[DatoTecnicoDaoImpl::saveDatoTecnico] ERROR : " + e);
        	if(e.getMessage().contains("Duplicate key")) {
        		messaggiDAO.loadMessaggiByCodMessaggio("U001");
        		throw new BusinessException(400, "E042", "uso di legge duplicato");
        	}

			return null;

        } finally {
            LOGGER.debug("[DatoTecnicoDAOImpl::saveDatoTecnico] END");
        }

	}
	
	private BigDecimal calcolaCanone(String algoritmo, String parametri, String datiTecnici){
		LOGGER.debug("[DatoTecnicoDAOImpl::calcolaCanone] BEGIN calcolo canone singolo");
    	ScriptEngineManager factory = new ScriptEngineManager();
    	ScriptEngine engine = factory.getEngineByName("groovy");

    	try {
			engine.eval(algoritmo);
		} catch (ScriptException e) {
			  LOGGER.error("[DatoTecnicoDAOImpl::calcolaCanone] Errore nel caricamento dell'algoritmo", e);
		}

    	Invocable inv = (Invocable) engine;
    	try {
			Object result = inv.invokeFunction("calcolaCanone", parametri, datiTecnici);
			BigDecimal ret = BigDecimal.ZERO;
	         if(result instanceof BigDecimal) {
	               ret = (BigDecimal) result;
	            } else if(result instanceof Integer) {
	                ret = new BigDecimal((Integer) result);
	            } else if(result instanceof Long) {
	                ret = new BigDecimal((Long) result);
	            } 
	        LOGGER.debug("[DatoTecnicoDAOImpl::calcolaCanone] END");
			return ret;
		} catch (NoSuchMethodException e) {
			  LOGGER.error("[DatoTecnicoDAOImpl::calcolaCanone] Errore esecuzione algoritmo ", e);
		} catch (ScriptException e) {
			  LOGGER.error("[DatoTecnicoDAOImpl::calcolaCanone] Errore Script", e);
		}
    	LOGGER.debug("[DatoTecnicoDAOImpl::calcolaCanone] END calcolo canone singolo");
    	return BigDecimal.ZERO;
    }


	public RowMapper getRiscossioneUsoRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new RiscossioneUsoRowMapper();
	}


	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	
	@Override
	public RowMapper<DatoTecnicoDTO> getRowMapper() throws SQLException {
		return new DatoTecnicoRowMapper();
	}
	

	public RowMapper<DatoTecnicoJsonDTO> getRowMapperJsonDT() throws SQLException {
		return new DatoTecnicoJsonRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}
	
    /**
     * The type Tipo riscossione row mapper.
     */
	 public static class RiscossioneUsoRowMapper implements RowMapper<RiscossioneUsoDTO> {

	        /**
	         * Instantiates a new Tipo adempimento row mapper.
	         *
	         * @throws SQLException the sql exception
	         */
	        public RiscossioneUsoRowMapper() throws SQLException {
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
	        public RiscossioneUsoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	RiscossioneUsoDTO bean = new RiscossioneUsoDTO();
	            populateBean(rs, bean);
	            return bean;
	        }

	        private void populateBean(ResultSet rs, RiscossioneUsoDTO bean) throws SQLException {
	            bean.setIdRiscossioneUso(rs.getLong("id_riscossione_uso"));
	            bean.setIdRiscossione(rs.getLong("id_riscossione"));
	            bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
	            bean.setGestDataIns(rs.getDate("gest_data_ins"));
				bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
				bean.setGestDataUpd(rs.getDate("gest_data_upd"));
				bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
				bean.setGestUid(rs.getString("gest_uid"));

	        }
	       
	 }
    /**
     * The type Tipo riscossione row mapper.
     */
    public static class DatoTecnicoJsonRowMapper implements RowMapper<DatoTecnicoJsonDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public DatoTecnicoJsonRowMapper() throws SQLException {
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
        public DatoTecnicoJsonDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	DatoTecnicoJsonDTO bean = new DatoTecnicoJsonDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, DatoTecnicoJsonDTO bean) throws SQLException {
            bean.setIdRiscossione(rs.getLong("id_riscossione"));
            bean.setDataModifica(rs.getString("data_modifica"));
            bean.setDataInserimento(rs.getString("data_inserimento"));
            bean.setDatiTecnici(rs.getString("dati_tecnici"));
            bean.setGestUID(rs.getString("gest_uid"));
        }
    }
    
    public static class DatoTecnicoRowMapper implements RowMapper<DatoTecnicoDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public DatoTecnicoRowMapper() throws SQLException {
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
        public DatoTecnicoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            DatoTecnicoDTO bean = new DatoTecnicoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, DatoTecnicoDTO bean) throws SQLException {
            bean.setIdDatoTecnico(rs.getLong("id_riscossione"));
            bean.setCodDatoTecnico(rs.getString("id_template"));
            bean.setDesDatoTecnico(rs.getString("id_stato_riscossione"));
            bean.setIdUnitaMisura(rs.getLong("id_soggetto"));
            bean.setIdTipoDatoTecnico(rs.getLong("id_tipo_riscossione"));
            bean.setFlgCalcolato(rs.getString("id_gruppo_soggetto"));

        }
    }


	public RowMapper<TipoUsoRegolaDTO> getTipoUsoRegolaRowMapper() throws SQLException {
		return new TipoUsoRegolaRowMapper();
	}


	public RowMapper<?> getExtendedTipoUsoRegolaRowMapper() throws SQLException {
		return null;
	}

    public static class TipoUsoRegolaRowMapper implements RowMapper<TipoUsoRegolaDTO> {

        /**
         * Instantiates a new tipo uso regola row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoUsoRegolaRowMapper() throws SQLException {
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
        public TipoUsoRegolaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	TipoUsoRegolaDTO bean = new TipoUsoRegolaDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipoUsoRegolaDTO bean) throws SQLException {
            bean.setIdTipoUsoRegola(rs.getLong("id_tipo_uso_regola"));
            bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
            bean.setDataInizio(rs.getString("data_inizio"));
            bean.setDataFine(rs.getString("data_fine"));
            bean.setJsonRegola(rs.getString("json_regola"));
            bean.setIdAlgoritmo(rs.getLong("id_algoritmo"));

        }
    }

	public RowMapper<RiduzioneAumentoDTO> getRowMapperRiduzioneAumento() throws SQLException {
		 return new RiduzioneAumentoRowMapper();
	}
    /**
     * The type Riduzione Aumento row mapper.
     */
    public static class RiduzioneAumentoRowMapper implements RowMapper<RiduzioneAumentoDTO> {

        /**
         * Instantiates a new Riduzione Aumento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public RiduzioneAumentoRowMapper() throws SQLException {
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
        public RiduzioneAumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            RiduzioneAumentoDTO bean = new RiduzioneAumentoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, RiduzioneAumentoDTO bean) throws SQLException {
            bean.setIdRiduzioneAumento(rs.getLong("id_riduzione_aumento"));
            bean.setSiglaRiduzioneAumento(rs.getString("sigla_riduzione_aumento"));
            bean.setDesRiduzioneAumento(rs.getString("des_riduzione_aumento"));
            bean.setPercRiduzioneAumento(rs.getLong("perc_riduzione_aumento"));
            bean.setFlgRiduzioneAumento(rs.getString("flg_riduzione_aumento"));
            bean.setFlgManuale(rs.getString("flg_manuale"));
            bean.setFlgDaApplicare(rs.getString("flg_da_applicare"));
            bean.setOrdinaRiduzioneAumento(rs.getLong("ordina_riduzione_aumento"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            
        }
    }
}
