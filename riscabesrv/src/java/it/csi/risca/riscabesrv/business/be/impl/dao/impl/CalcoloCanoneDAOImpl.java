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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.CalcoloCanoneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.CalcoloCanoneDTO;
import it.csi.risca.riscabesrv.dto.CalcoloCanoneSingoloDTO;
import it.csi.risca.riscabesrv.dto.CanoneUsoDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatiTecniciAmbienteDTO;
import it.csi.risca.riscabesrv.dto.ambiente.RiscossioneDatoTecnicoDTO;
import it.csi.risca.riscabesrv.dto.ambiente.TipoUsoDatoTecnicoDTO;

/**
 * The type Riscossione dao.
 *
 * @author CSI PIEMONTE
 */
public class CalcoloCanoneDAOImpl extends RiscaBeSrvGenericDAO<TipoUsoRegolaDTO> implements CalcoloCanoneDAO {
	
	private static final String DATA_RIFERIMENTO = "dataRiferimento";
    @Autowired
    private MessaggiDAO messaggiDAO;
    private static final String QUERY_RISCOSSIONE_USO = "SELECT rrru.id_tipo_uso "
    		+ "FROM risca_r_riscossione_uso rrru "
    		+ "join risca_d_tipo_uso rdtu "
    		+ "on rrru.id_tipo_uso = rdtu.id_tipo_uso "
            + "WHERE id_riscossione = :idRiscossione "
            + "and rdtu.id_tipo_uso_padre is null ";
    
    private static final String QUERY_TIPO_USO_REGOLA= "SELECT rrtur.* "
    		+ "FROM risca_r_tipo_uso_regola rrtur " 
    		+ "WHERE id_tipo_uso in (:idTipoUso) "
    		+ "AND data_inizio <= :dataRiferimento "
    		+ "AND (data_fine is null OR data_fine >= :dataRiferimento)";
    
    private static final String QUERY_RISCOSSIONE_JSON_DT = "SELECT rtr.json_dt "
    		+ "FROM risca_t_riscossione rtr "
            + "WHERE id_riscossione = :idRiscossione";
 
    private static final String QUERY_ALGORITMO = "SELECT rda.script_algoritmo "
    		+ "FROM risca_d_algoritmo rda "
            + "WHERE id_algoritmo = :idAlgoritmo";
    
    private static final String QUERY_LOAD_CANONE_UNITARIO_BY_ID_TIPO_USO=  "SELECT json_regola -> 'canone_unitario' canone_unitario "
			+"FROM risca_r_tipo_uso_regola   "
			+"WHERE id_tipo_uso = :idTipoUso "
    		+ "AND data_inizio <= :dataRiferimento "
    		+ "AND (data_fine is null OR data_fine >= :dataRiferimento)";
    
    
	@Override
	public CalcoloCanoneDTO calcoloCanone(Long idRiscossione, String dataRiferimento) throws Exception {
		LOGGER.debug("[CalcoloCanoneDAOImpl::calcoloCanone] BEGIN");
		BigDecimal singoloCanone;
		BigDecimal canone = BigDecimal.ZERO;
		CalcoloCanoneDTO canoneDTO = new CalcoloCanoneDTO();
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");

		try {
        	// Query su risca_r_riscossione_uso per recuperare l'idTipoUso
        	Map<String, Object> mapRU = new HashMap<>();
            mapRU.put("idRiscossione", idRiscossione);
            MapSqlParameterSource paramsRU = getParameterValue(mapRU);
            List<Long> idTipoUso = template.queryForList(QUERY_RISCOSSIONE_USO, paramsRU, Long.class);

            if(idTipoUso.isEmpty())
            	return null;
     		// Query su risca_r_tipo_uso_regola con idTipo uso e dataRiferimento compresa tra data_inizio e data_fine per prendere il json_regola e l'id_algoritmo
        	Map<String, Object> mapTUR = new HashMap<>();
            mapTUR.put("idTipoUso", idTipoUso);
            
			if(dataRiferimento != null) {
				if(!dataRiferimento.equals("")) {
				
					Date dataRif = formatter.parse(dataRiferimento);
					mapTUR.put(DATA_RIFERIMENTO, dataRif);
				}
				else {
					mapTUR.put(DATA_RIFERIMENTO, null);
				}
			} 
			else {
				mapTUR.put(DATA_RIFERIMENTO, null);
			}
            
            MapSqlParameterSource paramsTUR = getParameterValue(mapTUR);
            List<TipoUsoRegolaDTO> tipoUsoRegola = template.query(QUERY_TIPO_USO_REGOLA, paramsTUR, getRowMapper());
            
            if(idTipoUso.size() != tipoUsoRegola.size() ) {
            	messaggiDAO.loadMessaggiByCodMessaggio("C003");
            	throw new BusinessException(404,"C003", "Non esiste l'algoritmo della regola", null);
            }
            // Query su risca_t_riscossione con id_riscossione per prendere il json_dt 
            String jsonDt = template.queryForObject(QUERY_RISCOSSIONE_JSON_DT, paramsRU, String.class);
            
            //calcolo
            List<BigDecimal> valoriCanone = new ArrayList<BigDecimal>();
            String jsonRegola = "";
            Long idAlgoritmo;
            Map<String, Object> mapIdA = new HashMap<>();
            for(int i=0; i<tipoUsoRegola.size(); i++) {
            	jsonRegola = tipoUsoRegola.get(i).getJsonRegola();
            	idAlgoritmo = tipoUsoRegola.get(i).getIdAlgoritmo();
            	mapIdA.put("idAlgoritmo", idAlgoritmo);
            	MapSqlParameterSource paramsIdA = getParameterValue(mapIdA);
            	String algoritmo= template.queryForObject(QUERY_ALGORITMO, paramsIdA, String.class);
            	singoloCanone = calcolaCanone(algoritmo,jsonRegola, jsonDt).setScale(2, RoundingMode.HALF_UP);
            	valoriCanone.add(singoloCanone);
            }


            if(valoriCanone.size()>0) {
            	for(BigDecimal valori : valoriCanone)
            		canone =canone.add(valori);
            }
            
            
		} catch(BusinessException be) {
			  LOGGER.error("[CalcoloCanoneDAOImpl::calcoloCanone] Errore Business", be);
        	throw be;
        }  catch (Exception e) {
			  LOGGER.error("[CalcoloCanoneDAOImpl::calcoloCanone] Errore generale", e);
			  LOGGER.debug("[CalcoloCanoneDAOImpl::calcoloCanone] END");
			  throw e;
		}
		canoneDTO.setCalcoloCanone(canone);
		LOGGER.debug("[CalcoloCanoneDAOImpl::calcoloCanone] END");
		return canoneDTO;
	}
	

	@Override
	public CalcoloCanoneSingoloDTO calcoloCanoneSingoloEFrazionato(RiscossioneDatoTecnicoDTO dto, String dataRiferimento, String dataFrazionamento,
			String flgFraz) throws Exception {
		LOGGER.debug("[CalcoloCanoneDAOImpl::calcoloCanoneSingoloEFrazionato] BEGIN");
		BigDecimal singoloCanone;
		BigDecimal canone = BigDecimal.ZERO;
		CalcoloCanoneSingoloDTO oggettoCanone = new CalcoloCanoneSingoloDTO();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		try {

			List<Long> listIdTipoUso = getlistIdTipoUso(dto.getRiscossione().getDatiTecnici());
			List<String> listCodTipoUso = getlistCodTipoUso(dto.getRiscossione().getDatiTecnici());
			

		    Map<String, Object> mapTUR = new HashMap<>();
            mapTUR.put("idTipoUso", listIdTipoUso);
            
			if(dataRiferimento != null) {
				if(!dataRiferimento.equals("")) {
				
					Date dataRif = formatter.parse(dataRiferimento);
					mapTUR.put(DATA_RIFERIMENTO, dataRif);
				}
				else {
					mapTUR.put(DATA_RIFERIMENTO, null);
				}
			} 
			else {
				mapTUR.put(DATA_RIFERIMENTO, null);
			}
            
            MapSqlParameterSource paramsTUR = getParameterValue(mapTUR);
            List<TipoUsoRegolaDTO> tipoUsoRegola = template.query(QUERY_TIPO_USO_REGOLA, paramsTUR, getRowMapper());

            List<CanoneUsoDTO> listCanoneUso = new ArrayList<CanoneUsoDTO>();

            boolean flgCanoneTotale = false;
            //calcolo
            List<BigDecimal> valoriCanone = new ArrayList<BigDecimal>();
            String jsonRegola = "";
            Long idAlgoritmo = null;
            Map<String, Object> mapIdA = new HashMap<>();
            
            JSONObject jsonRisc = new JSONObject();
            String datiTecnici = dto.getRiscossione().getDatiTecnici();
            
    		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
    		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            DatiTecniciAmbienteDTO datiTecniciAmbiente = mapper.readValue(datiTecnici, DatiTecniciAmbienteDTO.class);
            
            JSONObject jsonDT = datiTecniciAmbiente.toJsonObj();
            JSONObject json =  new JSONObject();
    		json.put("gest_UID", dto.getRiscossione().getGestUID());
    		json.put("id_riscossione", dto.getRiscossione().getIdRiscossione());
    		json.put("data_modifica", dto.getRiscossione().getDataModifica());
    		json.put("data_inserimento", dto.getRiscossione().getDataInserimento());
    		json.put("dati_tecnici", jsonDT);
            jsonRisc.put("riscossione", json);

            String jsonString =   jsonRisc.toString();
            int numMesi = 0;
            //[DP] verificare che dataFrazionamento (anno) sia lo stesso anno di dataRiferimento (anno)
            //altrimenti numMesi 12
            Calendar c = Calendar.getInstance();  
            Integer annoDataRiferimento = null;
            Integer annoDataFrazionamento = null;
            
            if(dataRiferimento != null) {
				if(!dataRiferimento.equals("")) {
					Date dataRiferimentoParsificata = formatter.parse(dataRiferimento); 
		            c.setTime(dataRiferimentoParsificata);  
		            annoDataRiferimento = c.get(Calendar.YEAR);
		            System.out.println("anno data riferimento :"+annoDataRiferimento);
				}
            }
            if(dataFrazionamento != null) {
				if(!dataFrazionamento.equals("")) {
					   Date dataFrazionamentoParsificata = formatter.parse(dataFrazionamento);  		           
			            c.setTime(dataFrazionamentoParsificata);  
			            annoDataFrazionamento = c.get(Calendar.YEAR);
			            System.out.println("anno data frazionamento :"+annoDataFrazionamento);
				}
            }          
            
            if(dataFrazionamento != null ) {
            	//[DP] TASK 27 Aggiunta if
            	if((annoDataFrazionamento!=null && annoDataRiferimento != null) && (annoDataFrazionamento.equals(annoDataRiferimento))) {
                   numMesi = mensilitaDaPagare(dataFrazionamento, flgFraz);
            	}else {
            		numMesi = 12;
            	}
            }else {
            	//[DP] TASK 27 
            	numMesi = 12;
            }

            for(int i=0; i<listIdTipoUso.size(); i++) {
            	CanoneUsoDTO canoneUso = new CanoneUsoDTO();
                try {		
					for(int j = 0; j<tipoUsoRegola.size(); j++) {
						int compareLong = Long.compare(tipoUsoRegola.get(j).getIdTipoUso(), listIdTipoUso.get(i));
						if(compareLong==0) {
							jsonRegola = tipoUsoRegola.get(j).getJsonRegola();
							break;
						}

					}

					if(jsonRegola != null) {
												
						for(int k = 0; k<tipoUsoRegola.size(); k++) {
							int compareL = Long.compare(tipoUsoRegola.get(k).getIdTipoUso(), listIdTipoUso.get(i));
							if(compareL == 0) {
								idAlgoritmo = tipoUsoRegola.get(k).getIdAlgoritmo();
								break;
							}							
						}
							
							
						//idAlgoritmo = tipoUsoRegola.get(i).getIdAlgoritmo();
						mapIdA.put("idAlgoritmo", idAlgoritmo);
						MapSqlParameterSource paramsIdA = getParameterValue(mapIdA);
						String algoritmo= template.queryForObject(QUERY_ALGORITMO, paramsIdA, String.class);
						singoloCanone = calcolaCanone(algoritmo,jsonRegola, jsonString).setScale(2, RoundingMode.HALF_UP);
						valoriCanone.add(singoloCanone);
						
		                BigDecimal canoneUsoFrazionato = singoloCanone.divide(BigDecimal.valueOf(12), MathContext.DECIMAL32)
		                		.multiply(BigDecimal.valueOf(numMesi), MathContext.DECIMAL32).setScale(2, RoundingMode.HALF_UP);
		                 
						canoneUso.setAnnoUso(null); 
						canoneUso.setCodTipoUso(listCodTipoUso.get(i)); 
						canoneUso.setCanoneUso(singoloCanone);
						canoneUso.setCanoneUnitario(getCanoneUnitario(listIdTipoUso.get(i), dataRiferimento));
						canoneUso.setJsonRegolaMancante(false);
						canoneUso.setCanoneUsoFrazionato(canoneUsoFrazionato);
						
						listCanoneUso.add(canoneUso);
					}
					else {
						canoneUso.setCodTipoUso(listCodTipoUso.get(i));
						canoneUso.setJsonRegolaMancante(true);
						flgCanoneTotale = true;
						listCanoneUso.add(canoneUso);
					}
				} catch (Exception e) {
					canoneUso.setCodTipoUso(listCodTipoUso.get(i));
					canoneUso.setJsonRegolaMancante(true);
					flgCanoneTotale = true;
					listCanoneUso.add(canoneUso);
				}

            }
    
            if(valoriCanone.size()>0) {
            	for(BigDecimal valori : valoriCanone)
            		canone =canone.add(valori);
            }
            BigDecimal canoneMensile = null;
            BigDecimal importoCanone= null;
            
            if(dataFrazionamento != null) {
                 canoneMensile = canone.divide(BigDecimal.valueOf(12), MathContext.DECIMAL32);
                 importoCanone = canoneMensile.multiply(BigDecimal.valueOf(numMesi), MathContext.DECIMAL32);
                 oggettoCanone.setNumMesi(numMesi);
                 
            }else {
            	importoCanone = canone;
                oggettoCanone.setNumMesi(numMesi);
            }
            oggettoCanone.setCanoneUsi(listCanoneUso);

            if(flgCanoneTotale)
            	oggettoCanone.setJsonRegolaMancante(true);
            else
            	oggettoCanone.setJsonRegolaMancante(false);
            importoCanone = importoCanone.setScale(2, RoundingMode.HALF_UP);
            oggettoCanone.setImportoCanone(importoCanone);
            
		} catch(BusinessException be) {
			LOGGER.error("[CalcoloCanoneDAOImpl::calcoloCanoneSingoloEFrazionato] Errore nella query");
        	throw be;
        }  catch (Exception e) {

        	LOGGER.error("[CalcoloCanoneDAOImpl::calcoloCanoneSingoloEFrazionato] Errore generale", e);
			  LOGGER.debug("[CalcoloCanoneDAOImpl::calcoloCanoneSingoloEFrazionato] END");
				throw e;
		}
		//canoneSingoloDTO.setCalcoloCanone(canone);
		LOGGER.debug("[CalcoloCanoneDAOImpl::calcoloCanoneSingoloEFrazionato] END");
		return oggettoCanone;
	}


	private BigDecimal getCanoneUnitario(Long idTipoUso, String dataRiferimento) {
		LOGGER.debug("[CalcoloCanoneDAOImpl::getCanoneUnitario] BEGIN");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> map = new HashMap<>();
		map.put("idTipoUso", idTipoUso);
		if(dataRiferimento != null) {
			if(!dataRiferimento.equals("")) {
				Date dataRif = null;
				try {
					dataRif = formatter.parse(dataRiferimento);
				} catch (ParseException e) {
					LOGGER.error("[CalcoloCanoneDAOImpl::getCanoneUnitario] Errore nel parse data riferimento", e);
					return null;
				}
				map.put(DATA_RIFERIMENTO, dataRif);
			}
			else {
				map.put(DATA_RIFERIMENTO, null);
			}
		} 
		else {
			map.put(DATA_RIFERIMENTO, null);
		}
        
		MapSqlParameterSource params = getParameterValue(map);
		BigDecimal canoneUnitario = template.query(QUERY_LOAD_CANONE_UNITARIO_BY_ID_TIPO_USO, params, new ResultSetExtractor<BigDecimal>(){
		    @Override
		    public BigDecimal extractData(ResultSet rs) throws SQLException,DataAccessException {
		    	BigDecimal canoneUnitario = null;
		        while(rs.next()){
		        	canoneUnitario = rs.getBigDecimal("canone_unitario");
		        }
		        return canoneUnitario;
		    }
		});
		
		LOGGER.debug("[CalcoloCanoneDAOImpl::getCanoneUnitario] BEGIN");
		return canoneUnitario;
	}
	
	private List<String> getlistCodTipoUso(String datiTecnici){
		LOGGER.debug("[CalcoloCanoneDAOImpl::getlistCodTipoUso] BEGIN");
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
		List<String> listCodTipoUso = new ArrayList<>();  
		if (datiTecnici != null) {
			try {
				DatiTecniciAmbienteDTO datiTecniciAmbiente = mapper.readValue(datiTecnici, DatiTecniciAmbienteDTO.class);
				for (String codTipoUso : datiTecniciAmbiente.getUsi().keySet()) {
					listCodTipoUso.add(codTipoUso);
				}
				
			} catch (IOException e) {
				LOGGER.error("[CalcoloCanoneDAOImpl::getlistCodTipoUso] Errore nella lettura dati json dt", e);
			}
		}
		LOGGER.debug("[CalcoloCanoneDAOImpl::getlistCodTipoUso] BEGIN");
		return listCodTipoUso;
		
	}
	
	private List<Long> getlistIdTipoUso(String datiTecnici){
		LOGGER.debug("[CalcoloCanoneDAOImpl::getlistIdTipoUso] BEGIN");
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
		List<Long> listIdTipoUso = new ArrayList<>();  
		if (datiTecnici != null) {
			try {
				DatiTecniciAmbienteDTO datiTecniciAmbiente = mapper.readValue(datiTecnici, DatiTecniciAmbienteDTO.class);
				for (TipoUsoDatoTecnicoDTO TipoUsoDatoTecnicoDTO : datiTecniciAmbiente.getUsi().values()) {
					listIdTipoUso.add(TipoUsoDatoTecnicoDTO.getIdTipoUsoLegge());
				}
				
			} catch (IOException e) {
				LOGGER.error("[CalcoloCanoneDAOImpl::getlistIdTipoUso] Errore nella lettura dati json dt", e);
			}
		}
		LOGGER.debug("[CalcoloCanoneDAOImpl::getlistIdTipoUso] BEGIN");
		return listIdTipoUso;
		
	}
	public static int mensilitaDaPagare(String dataFrazionamento, String flgFraz) throws ParseException {
	    LOGGER.debug("[CalcoloCanoneDAOImpl::mensilitaDaPagare] BEGIN");
	    
	    // Definizione del formato data
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    Date dataInizio = formatter.parse(dataFrazionamento);

	    // Estrai il giorno e il mese dalla data di Frazionamento
	    int giorno = dataInizio.getDate();
	    //aggiungo 1 al mese perche' getMonth parte da zero 
	    int mese = dataInizio.getMonth() + 1; 

	    // Inizializza il numero di mensilita' a 0
	    int mensilita = 0;

	    // Crea un oggetto Calendar per ottenere il numero di giorni nel mese
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(dataInizio);
	    int giorniDelMese = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

	    if ("inizio".equals(flgFraz)) {
	        // Calcolo basato su 'inizio'
	        mensilita = 12 - mese;
	        switch (giorniDelMese) {
	            case 28:
	                if (giorno <= 14) {
	                    mensilita++;
	                }
	                break;
	            case 29:
	                if (giorno <= 15) {
	                    mensilita++;
	                }
	                break;
	            case 30:
	                if (giorno <= 16) {
	                    mensilita++;
	                }
	                break;
	            case 31:
	                if (giorno <= 17) {
	                    mensilita++;
	                }
	                break;
	            default:
	                throw new IllegalArgumentException("Valore non valido per giorniDelMese: " + giorniDelMese);
	        }
	    } 
	    if ("fine".equals(flgFraz)) {
	        // Calcolo basato su 'fine'
	        mensilita = mese;
	        if (giorno < 15) {
	            mensilita--;
	        }
	    }

	    LOGGER.debug("[CalcoloCanoneDAOImpl::mensilitaDaPagare] END");
	    return mensilita;
	}



	private BigDecimal calcolaCanone(String algoritmo, String parametri, String datiTecnici) throws Exception {
		LOGGER.debug("[CalcoloCanoneDAOImpl::calcolaCanone] BEGIN");
    	ScriptEngineManager factory = new ScriptEngineManager();
    	ScriptEngine engine = factory.getEngineByName("groovy");

    	try {
			engine.eval(algoritmo);
		} catch (ScriptException e) {
			  LOGGER.error("[CalcoloCanoneDAOImpl::calcoloCanone] Errore generale", e);
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
	        LOGGER.debug("[CalcoloCanoneDAOImpl::calcolaCanone] END");
			return ret;
		} catch (NoSuchMethodException e) {
			  LOGGER.error("[CalcoloCanoneDAOImpl::calcoloCanone] Errore No Such Method Exception", e);
			  throw e;
		} catch (ScriptException e) {
			  LOGGER.error("[CalcoloCanoneDAOImpl::calcoloCanone] Errore Script", e);
			  throw e;
		}
    	//LOGGER.debug("[CalcoloCanoneDAOImpl::calcolaCanone] END: canone uguale a zero");
    	//return BigDecimal.ZERO;
    }

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	
	@Override
	public RowMapper<TipoUsoRegolaDTO> getRowMapper() throws SQLException {
		return new TipoUsoRegolaRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
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

}
