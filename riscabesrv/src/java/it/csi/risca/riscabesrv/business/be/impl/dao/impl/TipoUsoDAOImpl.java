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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.dto.AccertamentoBilancioDTO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.UnitaMisuraDTO;

/**
 * The type Tipo uso dao.
 *
 * @author CSI PIEMONTE
 */
public class TipoUsoDAOImpl extends RiscaBeSrvGenericDAO<TipoUsoDTO> implements TipoUsoDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String ID_AMBITO = "idAmbito";
    
    private static final String QUERY_TIPI_USO = "SELECT rdtu.*, rdtu.id_ambito AS ambito_id, rda.*, rdum.* "
            + "FROM risca_d_tipo_uso rdtu "
            + "INNER JOIN risca_d_ambito rda ON rdtu.id_ambito = rda.id_ambito "
    		+ "INNER JOIN risca_d_unita_misura rdum ON rdtu.id_unita_misura = rdum.id_unita_misura ";
    		//+ "INNER JOIN risca_d_accerta_bilancio rdab ON rdtu.id_accerta_bilancio = rdab.id_accerta_bilancio ";
    
    private static final String QUERY_TIPI_USO_BY_ID_AMBITO = QUERY_TIPI_USO
            + "where rdtu.id_ambito = :idAmbito ";
    
    private static final String QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE = QUERY_TIPI_USO
            + "and rdtu.id_tipo_uso_padre = :idTipoUsoPadre ";
    
    private static final String QUERY_TIPI_USO_BY_ID_TIPO_USO = QUERY_TIPI_USO
            + "where rdtu.id_tipo_uso = :idTipoUso ";
    
    private static final String QUERY_TIPI_USO_BY_COD_TIPO_USO = QUERY_TIPI_USO
            + "where rdtu.cod_tipo_uso = :idTipoUso ";
    
    private static final String QUERY_TIPI_USO_BY_CODE_TIPO_USO_AND_BY_ID_AMBITO = QUERY_TIPI_USO_BY_ID_AMBITO
            + "and UPPER(rdtu.cod_tipo_uso) = UPPER(:codTipoUso) ";
    
    private static final String QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE_AND_BY_ID_AMBITO_FOR_USO_SPECIFICO = QUERY_TIPI_USO_BY_ID_AMBITO 
            + "and rdtu.id_tipo_uso_padre = :idTipoUsoPadre ";
           
    
    private static final String QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE_AND_BY_ID_AMBITO_FOR_USO_LEGGE = QUERY_TIPI_USO_BY_ID_AMBITO 
            + "and rdtu.id_tipo_uso_padre is null ";

    
    private static final String QUERY_LOAD_ACCERTAMENTO_BILANCIO = "SELECT rdab.* "
    		+ "FROM risca_d_accerta_bilancio rdab "
    		+ "WHERE rdab.id_accerta_bilancio = :idAccertaBilancio";
    
    private static final String QUERY_DATA_INIZIO_VALIDITA =" rdtu.data_inizio_validita >= :dataInizioValidita ";
    
    private static final String QUERY_DATA_FINE_VALIDITA =" rdtu.data_fine_validita <= :dataFineValidita ";
    
    private static final String ORDER_BY_ORDINA_TIPI_USO ="  ORDER BY rdtu.ordina_tipo_uso ASC ";
    
    private static final String QUERY_TIPO_USO_BY_RISCOSSIONE = " select u.* "
    		+ " from RISCA_R_RISCOSSIONE_USO ru, RISCA_D_TIPO_USO u "
    		+ " where ru.id_riscossione = :idRiscossione "
    		+ " and ru.id_tipo_uso = u.id_tipo_uso "
    		+ " and u.id_tipo_uso_padre IS NULL "
    		+ " order by u.des_tipo_uso ";
    
	@Override
	public List<TipoUsoExtendedDTO> loadTipoUso() {
		LOGGER.debug("[TipoUsoDAOImpl::loadTipoUso] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<TipoUsoExtendedDTO> tipoUso = new ArrayList<TipoUsoExtendedDTO>();
        try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params;
			
			AccertamentoBilancioDTO accBil = new AccertamentoBilancioDTO();
			tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO +ORDER_BY_ORDINA_TIPI_USO, null);
			if(tipoUso != null && tipoUso.size()>0) {
				for(int i=0; i<tipoUso.size(); i++) {
					Long idAccBil = tipoUso.get(i).getIdAccertaBilancio();
					map.put("idAccertaBilancio", idAccBil);
					params = getParameterValue(map);
					try {
						if(idAccBil > 0) {
							accBil = template.queryForObject(QUERY_LOAD_ACCERTAMENTO_BILANCIO, params, getAccertamentoBilancioRowMapper());
							tipoUso.get(i).setAccertamentoBilancio(accBil);
						}
					} catch (Exception e) {
						LOGGER.error("[TipoUsoDAOImpl::loadTipoUso] Nessun accertamento bilancio o errore nell'accesso al db");
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoPadre] Nessun accertamento bilancio o errore nell'accesso al db", e);
            throw e;
		}
        LOGGER.debug("[TipoUsoDAOImpl::loadTipoUso] END");
        return tipoUso;
	}

	@Override
	public List<TipoUsoExtendedDTO> loadTipoUsoByIdAmbito(Long idAmbito ,String dataIniVal,  String dataFineVal) throws Exception {
		LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByIdAmbito] BEGIN");
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Object> map = new HashMap<>();
		map.put(ID_AMBITO, idAmbito);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<TipoUsoExtendedDTO> tipoUso = new ArrayList<TipoUsoExtendedDTO>();
		try {
			MapSqlParameterSource params;
			AccertamentoBilancioDTO accBil = new AccertamentoBilancioDTO();
			if (dataIniVal != null && dataFineVal != null) {
				Date dataIniValidita = formatter.parse(dataIniVal);
				Date dataFineValidita = formatter.parse(dataFineVal);
				map.put("dataInizioValidita", dataIniValidita);
				map.put("dataFineValidita", dataFineValidita);
				params = getParameterValue(map);
				tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_AMBITO + " and "
						+ QUERY_DATA_INIZIO_VALIDITA + " and " + QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_TIPI_USO,
						map);

			} else if (dataIniVal != null && dataFineVal == null) {
				Date dataIniValidita = formatter.parse(dataIniVal);
				map.put("dataInizioValidita", dataIniValidita);
				params = getParameterValue(map);
				tipoUso = findListByQuery(CLASSNAME, methodName,
						QUERY_TIPI_USO_BY_ID_AMBITO + " and " + QUERY_DATA_INIZIO_VALIDITA + ORDER_BY_ORDINA_TIPI_USO,
						map);

			} else if (dataIniVal == null && dataFineVal != null) {
				Date dataFineValidita = formatter.parse(dataFineVal);
				map.put("dataFineValidita", dataFineValidita);
				params = getParameterValue(map);
				tipoUso = findListByQuery(CLASSNAME, methodName,
						QUERY_TIPI_USO_BY_ID_AMBITO + " and " + QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_TIPI_USO,
						map);
			} else {
				params = getParameterValue(map);
				tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_AMBITO + ORDER_BY_ORDINA_TIPI_USO,
						map);

			}
			if (tipoUso != null && tipoUso.size() > 0) {
				for (int i = 0; i < tipoUso.size(); i++) {
					Long idAccBil = tipoUso.get(i).getIdAccertaBilancio();
					map.put("idAccertaBilancio", idAccBil);
					params = getParameterValue(map);
					try {
						if(idAccBil > 0) {
							accBil = template.queryForObject(QUERY_LOAD_ACCERTAMENTO_BILANCIO, params,
									getAccertamentoBilancioRowMapper());
							tipoUso.get(i).setAccertamentoBilancio(accBil);
						}
					} catch (Exception e) {
						LOGGER.error(
								"[TipoUsoDAOImpl::loadTipoUsoByIdAmbito] Nessun accertamento bilancio o errore nell'accesso al db", e);
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdAmbito] Nessun accertamento bilancio o errore nell'accesso al db", e);
throw e;
		}
		LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByIdAmbito] END");
		return tipoUso;
	}
	
	@Override
	public List<TipoUsoExtendedDTO> loadTipoUsoByIdTipoUsoPadre(String idTipoUsoPadre) {		
		    LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoPadre] BEGIN");
        	String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            Map<String, Object> map = new HashMap<>();
            Long idTipoUsoPadreL = Long.parseLong(idTipoUsoPadre);
            map.put("idTipoUsoPadre", idTipoUsoPadreL);
            MapSqlParameterSource params = getParameterValue(map);
           // return findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE, map);
            
            
            List<TipoUsoExtendedDTO> tipoUso = new ArrayList<TipoUsoExtendedDTO>();
            try {

    			//MapSqlParameterSource params;
    			
    			AccertamentoBilancioDTO accBil = new AccertamentoBilancioDTO();
    			tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE, map);
    			if(tipoUso != null && tipoUso.size()>0) {
    				for(int i=0; i<tipoUso.size(); i++) {
    					Long idAccBil = tipoUso.get(i).getIdAccertaBilancio();
    					map.put("idAccertaBilancio", idAccBil);
    					params = getParameterValue(map);
    					try {
    						if(idAccBil > 0) {
	    						accBil = template.queryForObject(QUERY_LOAD_ACCERTAMENTO_BILANCIO, params, getAccertamentoBilancioRowMapper());
	    						tipoUso.get(i).setAccertamentoBilancio(accBil);
    						}
    					} catch (Exception e) {
    						LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoPadre] Nessun accertamento bilancio o errore nell'accesso al db", e);
    					}
    				}

    			}
    		} catch (Exception e) {
				LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoPadre] ERRORE", e);

    			throw e;
    		}
            LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoPadre] BEGIN");
            return tipoUso;
	}

	@Override
	public TipoUsoExtendedDTO loadTipoUsoByIdTipoUsoOrCodTipoUso(String idTipoUso) throws SQLException {		
        LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            MapSqlParameterSource params;
            
            TipoUsoExtendedDTO tipoUso = new TipoUsoExtendedDTO();
            AccertamentoBilancioDTO accBil = new AccertamentoBilancioDTO();
            
			int id = 0;
			boolean bool = false;
			try {
				id = Integer.parseInt(idTipoUso);
				bool = true;
			} catch (NumberFormatException nfe) {
				bool = false;
			}
            
			
			if (bool) {
				map.put("idTipoUso", id);
				params = getParameterValue(map);
				tipoUso = template.queryForObject(QUERY_TIPI_USO_BY_ID_TIPO_USO, params, getExtendedRowMapper());
				if(tipoUso != null) {
					Long idAccBil = tipoUso.getIdAccertaBilancio();
					map.put("idAccertaBilancio", idAccBil);
					params = getParameterValue(map);
	        		try {
	        			if(idAccBil > 0) {
		        			accBil = template.queryForObject(QUERY_LOAD_ACCERTAMENTO_BILANCIO, params, getAccertamentoBilancioRowMapper());
		        			tipoUso.setAccertamentoBilancio(accBil);
	        			}
	        		} catch (Exception e) {
						LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] Nessun accertamento bilancio o errore nell'accesso al db",e);
					}
				}

			} else {
				map.put("idTipoUso", idTipoUso);
				params = getParameterValue(map);
				tipoUso = template.queryForObject(QUERY_TIPI_USO_BY_COD_TIPO_USO, params, getExtendedRowMapper());
				if(tipoUso != null) {
					Long idAccBil = tipoUso.getIdAccertaBilancio();
					map.put("idAccertaBilancio", idAccBil);
					params = getParameterValue(map);
	        		try {
	        			if(idAccBil > 0) {
		        			accBil = template.queryForObject(QUERY_LOAD_ACCERTAMENTO_BILANCIO, params, getAccertamentoBilancioRowMapper());
		        			tipoUso.setAccertamentoBilancio(accBil);
	        			}
	        		} catch (Exception e) {
						LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] Nessun accertamento bilancio o errore nell'accesso al db",e);
					}
				}
			}
			LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] END");
            return tipoUso;
        } catch (SQLException e) {
            LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] Errore nell'esecuzione della query", e);
        	throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] Errore nell'accesso ai dati", e);
        	throw e;
        } finally {
            LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] END");
        }
	}

	@Override
	public TipoUsoExtendedDTO loadTipoUsoByCodeAndIdAmbito(String codTipoUso, Long idAmbito) throws SQLException {
        LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByCodeAndIdAmbito] BEGIN");
        TipoUsoExtendedDTO tipoUso = new TipoUsoExtendedDTO();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("codTipoUso",codTipoUso);
            map.put(ID_AMBITO, idAmbito);
            MapSqlParameterSource params = getParameterValue(map);
//            return template.queryForObject(QUERY_TIPI_USO_BY_CODE_TIPO_USO_AND_BY_ID_AMBITO, params, getExtendedRowMapper());
            
            
            AccertamentoBilancioDTO accBil = new AccertamentoBilancioDTO();
//			map.put("idTipoUso", idTipoUso);
//			params = getParameterValue(map);
			tipoUso = template.queryForObject(QUERY_TIPI_USO_BY_CODE_TIPO_USO_AND_BY_ID_AMBITO, params, getExtendedRowMapper());
			if(tipoUso != null) {
				Long idAccBil = tipoUso.getIdAccertaBilancio();
				map.put("idAccertaBilancio", idAccBil);
				params = getParameterValue(map);
        		try {
        			if(idAccBil > 0) {
	        			accBil = template.queryForObject(QUERY_LOAD_ACCERTAMENTO_BILANCIO, params, getAccertamentoBilancioRowMapper());
	        			tipoUso.setAccertamentoBilancio(accBil);
        			}
        		} catch (Exception e) {
					LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoOrCodTipoUso] Nessun accertamento bilancio o errore nell'accesso al db", e);
				}
			}
            
            
        } catch (SQLException e) {
            LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByCodeAndIdAmbito] Errore nell'esecuzione della query", e);
        	throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByCodeAndIdAmbito] Errore nell'accesso ai dati", e);
        	throw e;
        } finally {
            LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByCodeAndIdAmbito] END");
        }
        LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByCodeAndIdAmbito] END");
        return tipoUso;
	}

	@Override
	public List<TipoUsoExtendedDTO> loadTipoUsoByIdTipoUsoPadreAndIdAmbito(String idTipoUsoPadre, Long idAmbito ,String dataIniVal,  String dataFineVal) throws Exception {
		LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] BEGIN");
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        Map<String, Object> map = new HashMap<>();
        List<TipoUsoExtendedDTO> tipoUso = new ArrayList<TipoUsoExtendedDTO>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 MapSqlParameterSource params=null;
        map.put(ID_AMBITO, idAmbito);
        
        try {
       			
			AccertamentoBilancioDTO accBil = new AccertamentoBilancioDTO();
			  if (dataIniVal  !=  null && dataFineVal !=  null) {
					Date dataIniValidita = formatter.parse(dataIniVal);
					Date dataFineValidita = formatter.parse(dataFineVal);
					map.put("dataInizioValidita", dataIniValidita);
					map.put("dataFineValidita", dataFineValidita);
					 if(idTipoUsoPadre != null) {
			        	Long idTipoUsoPadreL = Long.parseLong(idTipoUsoPadre);
			        	map.put("idTipoUsoPadre", idTipoUsoPadreL);
			        	params = getParameterValue(map);
			        	tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE_AND_BY_ID_AMBITO_FOR_USO_SPECIFICO +" and "+QUERY_DATA_INIZIO_VALIDITA +" and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_TIPI_USO, map);
			        }
			        else {
			        	params = getParameterValue(map);
			        	tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE_AND_BY_ID_AMBITO_FOR_USO_LEGGE+" and "+QUERY_DATA_INIZIO_VALIDITA +" and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_TIPI_USO, map);
			        }
			            
			      }
			  else if (dataIniVal  !=  null && dataFineVal ==  null) {
					Date dataIniValidita = formatter.parse(dataIniVal);
					map.put("dataInizioValidita", dataIniValidita);
					 if(idTipoUsoPadre != null) {
			        	Long idTipoUsoPadreL = Long.parseLong(idTipoUsoPadre);
			        	map.put("idTipoUsoPadre", idTipoUsoPadreL);
			        	params = getParameterValue(map);
			        	tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE_AND_BY_ID_AMBITO_FOR_USO_SPECIFICO+" and "+QUERY_DATA_INIZIO_VALIDITA + ORDER_BY_ORDINA_TIPI_USO, map);
			        }
			        else {
			        	params = getParameterValue(map);
			        	tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE_AND_BY_ID_AMBITO_FOR_USO_LEGGE +" and "+QUERY_DATA_INIZIO_VALIDITA + ORDER_BY_ORDINA_TIPI_USO, map);
			        }
		        
	        	}
			  else if (dataIniVal  ==  null && dataFineVal !=  null) {
					Date dataFineValidita = formatter.parse(dataFineVal);
					map.put("dataFineValidita", dataFineValidita);
					 if(idTipoUsoPadre != null) {
			        	Long idTipoUsoPadreL = Long.parseLong(idTipoUsoPadre);
			        	map.put("idTipoUsoPadre", idTipoUsoPadreL);
			        	params = getParameterValue(map);
			        	tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE_AND_BY_ID_AMBITO_FOR_USO_SPECIFICO +" and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_TIPI_USO, map);
			        }
			        else {
			        	params = getParameterValue(map);
			        	tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE_AND_BY_ID_AMBITO_FOR_USO_LEGGE +" and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_TIPI_USO, map);
			        }
		            
		      
			  }
	        else {
		        if(idTipoUsoPadre != null) {
		        	Long idTipoUsoPadreL = Long.parseLong(idTipoUsoPadre);
		        	map.put("idTipoUsoPadre", idTipoUsoPadreL);
		        	params = getParameterValue(map);
		        	tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE_AND_BY_ID_AMBITO_FOR_USO_SPECIFICO + ORDER_BY_ORDINA_TIPI_USO, map);
		        }
		        else {
		        	params = getParameterValue(map);
		        	tipoUso = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_USO_BY_ID_TIPO_USO_PADRE_AND_BY_ID_AMBITO_FOR_USO_LEGGE + ORDER_BY_ORDINA_TIPI_USO, map);
		        }
		       
	        }
	        
			if(tipoUso != null && tipoUso.size()>0) {
				for(int i=0; i<tipoUso.size(); i++) {
					Long idAccBil = tipoUso.get(i).getIdAccertaBilancio();
					map.put("idAccertaBilancio", idAccBil);
					params = getParameterValue(map);
					try {
						if(idAccBil > 0) {
							accBil = template.queryForObject(QUERY_LOAD_ACCERTAMENTO_BILANCIO, params, getAccertamentoBilancioRowMapper());
							tipoUso.get(i).setAccertamentoBilancio(accBil);
						}
					} catch (Exception e) {
						LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] Nessun accertamento bilancio o errore nell'accesso al db",e);
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] Errore",e);
			throw e;
			
		}
        LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByIdTipoUsoPadreAndIdAmbito] END");
        return tipoUso;       
      
	}
	
	@Override
	public List<TipoUsoDTO> loadTipoUsoByRiscossione(Long idRiscossione) throws Exception {
		LOGGER.debug("[TipoUsoDAOImpl::loadTipoUsoByRiscossione] BEGIN");
		Map<String, Object> map = new HashMap<>();
		List<TipoUsoDTO> tipoUso = new ArrayList<TipoUsoDTO>();
		try {
            map.put("idRiscossione", idRiscossione);
            MapSqlParameterSource params = getParameterValue(map);
            tipoUso=  template.query(QUERY_TIPO_USO_BY_RISCOSSIONE, params, getRowMapper());
		} catch (Exception e) {
            LOGGER.error("[AmbitiDAOImpl::loadTipoUsoByRiscossione] Errore nell'accesso ai dati ", e);
        	throw e;
		}
		LOGGER.debug("[TipoUsoDAOImpl::loadTipoUso] END");
		return tipoUso;
	}
	
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<TipoUsoDTO> getRowMapper() throws SQLException {
		 return new TipoUsoRowMapper();
	}

	@Override
	public RowMapper<TipoUsoExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new TipoUsoExtendedRowMapper();
	}
	

	public RowMapper<AccertamentoBilancioDTO> getAccertamentoBilancioRowMapper() throws SQLException {
		 return new AccertamentoBilancioRowMapper();
	}

    /**
     * The type Tipo uso row mapper.
     */
    public static class TipoUsoRowMapper implements RowMapper<TipoUsoDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoUsoRowMapper() throws SQLException {
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
        public TipoUsoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipoUsoDTO bean = new TipoUsoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipoUsoDTO bean) throws SQLException {
            bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setIdUnitaMisura(rs.getLong("id_unita_misura"));
            bean.setIdAccertaBilancio(rs.getLong("id_accerta_bilancio"));
            bean.setCodTipouso(rs.getString("cod_tipo_uso"));
            bean.setDesTipouso(rs.getString("des_tipo_uso"));
            bean.setIdTipoUsoPadre(rs.getString("id_tipo_uso_padre"));
            bean.setFlgUsoPrincipale(rs.getString("flg_uso_principale"));
            bean.setOrdinaTipoUso(rs.getLong("ordina_tipo_uso"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setFlgDefault(rs.getInt("flg_default"));
        }
    }
    
    /**
     * The type Tipo uso extended row mapper.
     */
    public static class TipoUsoExtendedRowMapper implements RowMapper<TipoUsoExtendedDTO> {

        /**
         * Instantiates a new Tipo adempimento extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoUsoExtendedRowMapper() throws SQLException {
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
        public TipoUsoExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipoUsoExtendedDTO bean = new TipoUsoExtendedDTO();
            populateBeanExtended(rs, bean);
            return bean;
        }

        private void populateBeanExtended(ResultSet rs, TipoUsoExtendedDTO bean) throws SQLException {
            bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setIdUnitaMisura(rs.getLong("id_unita_misura"));
            bean.setIdAccertaBilancio(rs.getLong("id_accerta_bilancio"));
            bean.setCodTipouso(rs.getString("cod_tipo_uso"));
            bean.setDesTipouso(rs.getString("des_tipo_uso"));
            bean.setIdTipoUsoPadre(rs.getString("id_tipo_uso_padre"));
            bean.setFlgUsoPrincipale(rs.getString("flg_uso_principale"));
            bean.setOrdinaTipoUso(rs.getLong("ordina_tipo_uso"));
            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
            UnitaMisuraDTO unitaMisura = new UnitaMisuraDTO();
            populateBeanUnitaMisura(rs, unitaMisura);
            bean.setUnitaMisura(unitaMisura);
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setFlgDefault(rs.getInt("flg_default"));
//            AccertamentoBilancioDTO accertamentoBilancio = new AccertamentoBilancioDTO();
//            populateBeanAccertamentoBilancio(rs, accertamentoBilancio);
//            bean.setAccertamentoBilancio(accertamentoBilancio);
        }

        private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setCodAmbito(rs.getString("cod_ambito"));
            bean.setDesAmbito(rs.getString("des_ambito"));
        }
        
        private void populateBeanUnitaMisura(ResultSet rs, UnitaMisuraDTO bean) throws SQLException {
            bean.setIdUnitaMisura(rs.getLong("id_unita_misura"));
            bean.setSiglaUnitaMisura(rs.getString("sigla_unita_misura"));
            bean.setDesUnitaMisura(rs.getString("des_unita_misura"));
            bean.setOrdinaUnitaMisura(rs.getLong("ordina_unita_misura"));
        }
        
//        private void populateBeanAccertamentoBilancio(ResultSet rs, AccertamentoBilancioDTO bean) throws SQLException {
//            bean.setIdAccertaBilancio(rs.getLong("id_accerta_bilancio"));
//            bean.setCodAccertaBilancio(rs.getString("cod_accerta_bilancio"));
//            bean.setDesAccertaBilancio(rs.getString("des_accerta_bilancio"));
//        }

    }
    
    
    /**
     * The type accertamento bilancio row mapper.
     */
    public static class AccertamentoBilancioRowMapper implements RowMapper<AccertamentoBilancioDTO> {

        /**
         * Instantiates a new accertamento bilancio row mapper.
         *
         * @throws SQLException the sql exception
         */
        public AccertamentoBilancioRowMapper() throws SQLException {
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
        public AccertamentoBilancioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AccertamentoBilancioDTO bean = new AccertamentoBilancioDTO();
        	populateBeanAccertamentoBilancio(rs, bean);
            return bean;
        }


        private void populateBeanAccertamentoBilancio(ResultSet rs, AccertamentoBilancioDTO bean) throws SQLException {
            bean.setIdAccertaBilancio(rs.getLong("id_accerta_bilancio"));
            bean.setCodAccertaBilancio(rs.getString("cod_accerta_bilancio"));
            bean.setDesAccertaBilancio(rs.getString("des_accerta_bilancio"));
        }

    }
    
    
}
