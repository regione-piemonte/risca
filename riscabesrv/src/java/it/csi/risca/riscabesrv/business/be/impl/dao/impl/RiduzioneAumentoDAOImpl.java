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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiduzioneAumentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.RiduzioneAumentoDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRiduzioneAumentoDTO;

/**
 * The type Riduzione Aumento dao.
 *
 * @author CSI PIEMONTE
 */
public class RiduzioneAumentoDAOImpl extends RiscaBeSrvGenericDAO<RiduzioneAumentoDTO> implements RiduzioneAumentoDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String ID_AMBITO = "idAmbito";
    
    private static final String QUERY_RIDUZIONE_AUMENTO = "SELECT distinct rdra.* "
            + "FROM risca_d_riduzione_aumento rdra ";
    
    private static final String QUERY_TIPI_USO = "SELECT rdtu.*, rdtu.id_ambito AS ambito_id, rda.* "
            + "FROM risca_d_tipo_uso rdtu "
            + "INNER JOIN risca_d_ambito rda ON rdtu.id_ambito = rda.id_ambito ";
    
    private static final String QUERY_TIPI_USO_BY_ID_AMBITO = QUERY_TIPI_USO
            + "where rdtu.id_ambito = :idAmbito ";
    
    private static final String QUERY_RIDUZIONE_AUMENTO_BY_ID_AMBITO = "SELECT distinct rdra.* FROM risca_d_riduzione_aumento rdra "
    		+ "INNER JOIN risca_r_tipo_uso_ridaum rrurid ON rdra.id_riduzione_aumento = rrurid.id_riduzione_aumento "
            + "WHERE rrurid.id_tipo_uso = :idRiscossioneUso ";
               
    private static final String QUERY_RIDUZIONE_AUMENTO_BY_ID_RIDUZIONE_AUMENTO_LIST = QUERY_RIDUZIONE_AUMENTO
            + "WHERE id_riduzione_aumento in(:idRiduzioneAumento)";
    
    private static final String QUERY_ID_RIDUZIONE_AUMENTO_BY_ID_TIPO_USO = "SELECT rrtur.* "
            + "FROM risca_r_tipo_uso_ridaum rrtur WHERE rrtur.id_tipo_uso = :idTipoUso ";
    
    private static final String QUERY_RIDUZIONE_AUMENTO_BY_ID_TIPO_USO_AND_FLF_RIDAUM = QUERY_RIDUZIONE_AUMENTO
            + "WHERE rdra.id_riduzione_aumento in(:idRiduzioneAumento) "
            + "AND rdra.flg_riduzione_aumento = :flgRidAum "
            + "ORDER BY ordina_riduzione_aumento asc ";
            
    
    private static final String QUERY_RIDUZIONE_AUMENTO_BY_ID_RIDUZIONE_AUMENTO = QUERY_RIDUZIONE_AUMENTO
            + "WHERE id_riduzione_aumento = :idRiduzioneAumento ";

    private static final String QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO = QUERY_RIDUZIONE_AUMENTO
            + "WHERE cod_riduzione_aumento = :codRiduzioneAumento ";
    
    private static final String QUERY_DATA_INIZIO_VALIDITA =" rdra.data_inizio_validita >= :dataInizioValidita ";
    
    private static final String QUERY_DATA_FINE_VALIDITA =" rdra.data_fine_validita <= :dataFineValidita ";

    private static final String ORDER_BY_ORDINA_RIDUZIONE_AUMENTO =" ORDER BY rdra.ordina_riduzione_aumento ASC ";
    
    
    private static final String QUERY_RIDUZIONI_BY_ID_TIPO_USO_AND_FLG_MANUALE = QUERY_RIDUZIONE_AUMENTO
    		+" INNER JOIN RISCA_R_TIPO_USO_RIDAUM rrtur on rdra.id_riduzione_aumento = rrtur.id_riduzione_aumento"
    		+"	INNER join RISCA_D_TIPO_USO rdtu on rrtur.id_tipo_uso = rdtu.id_tipo_uso "
    		+"	where rdra.flg_riduzione_aumento = 1 AND  rdtu.id_tipo_uso = :idTipoUso";
    
    private static final String QUERY_RIDUZIONI_BY_COD_TIPO_USO_AND_FLG_MANUALE = QUERY_RIDUZIONE_AUMENTO
    		+" INNER JOIN RISCA_R_TIPO_USO_RIDAUM rrtur on rdra.id_riduzione_aumento = rrtur.id_riduzione_aumento"
    		+"	INNER join RISCA_D_TIPO_USO rdtu on rrtur.id_tipo_uso = rdtu.id_tipo_uso "
    		+"	where rdra.flg_riduzione_aumento = 1 AND  rdtu.cod_tipo_uso = :codTipoUso ";
    
    private static final String QUERY_AUMENTI_BY_ID_TIPO_USO_AND_FLG_MANUALE = QUERY_RIDUZIONE_AUMENTO
    		+" INNER JOIN RISCA_R_TIPO_USO_RIDAUM rrtur on rdra.id_riduzione_aumento = rrtur.id_riduzione_aumento"
    		+"	INNER join RISCA_D_TIPO_USO rdtu on rrtur.id_tipo_uso = rdtu.id_tipo_uso "
    		+"	where rdra.flg_riduzione_aumento = 2 AND  rdtu.id_tipo_uso = :idTipoUso"; 
    
    private static final String QUERY_AUMENTI_BY_COD_TIPO_USO_AND_FLG_MANUALE = QUERY_RIDUZIONE_AUMENTO
    		+" INNER JOIN RISCA_R_TIPO_USO_RIDAUM rrtur on rdra.id_riduzione_aumento = rrtur.id_riduzione_aumento"
    		+"	INNER join RISCA_D_TIPO_USO rdtu on rrtur.id_tipo_uso = rdtu.id_tipo_uso "
    		+"	where rdra.flg_riduzione_aumento = 2 AND  rdtu.cod_tipo_uso = :codTipoUso";
    
    private static final String QUERY_FLAG_MANUALE =" rdra.flg_manuale = :flagManuale ";		
    
    private static final String QUERY_RIDUZIONI_BY_FLG_RIDUZIONE_AUMENTO = QUERY_RIDUZIONE_AUMENTO
    		+"	where rdra.flg_riduzione_aumento = 1 ";
    
    private static final String QUERY_AUMENTI_BY_FLG_RIDUZIONE_AUMENTO = QUERY_RIDUZIONE_AUMENTO
    		+"	where rdra.flg_riduzione_aumento = 2 ";
    
    private static final String QUERY_RIDUZIONE_AUMENTO_BY_ANNUALITA_SD = "select b.* "
    		+ "from risca_r_annualita_uso_sd_ra a, RISCA_D_RIDUZIONE_AUMENTO b "
    		+ "where a.id_annualita_uso_sd = :idAnnualitaUsoSd "
    		+ "and a.id_riduzione_aumento = b.id_riduzione_aumento";
    
    private static final String QUERY_RIDUZIONE_AUMENTO_BY_RISCOSSIONE_TIPO_USO = "select b.*  "
    		+ "from RISCA_R_USO_RIDAUM a,  "
    		+ "RISCA_D_RIDUZIONE_AUMENTO b,  "
    		+ "RISCA_R_RISCOSSIONE_USO c, "
    		+ "risca_d_tipo_uso d  "
    		+ "where c.id_riscossione = :idRiscossione "
    		+ "     and d.cod_tipo_uso = :codTipoUso "
    		+ "     and c.id_tipo_uso = d.id_tipo_uso "
    		+ "     and c.id_riscossione_uso = a. id_riscossione_uso "
    		+ "     and a.id_riduzione_aumento = b.id_riduzione_aumento";
    
	@Override
	public List<RiduzioneAumentoDTO> loadRiduzioneAumento() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<RiduzioneAumentoDTO> list = null;
		try {
			list = findListByQuery(CLASSNAME, methodName, QUERY_RIDUZIONE_AUMENTO, null);
		} catch (Exception e) {
		       throw e ;
		}
        return list;
	}

	@Override
	public List<RiduzioneAumentoDTO> loadRiduzioneAumentoByIdAmbito(Long idAmbito) throws SQLException {
        LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdAmbito] BEGIN");
        List<RiduzioneAumentoDTO> listRiduzioneAumento = new ArrayList<RiduzioneAumentoDTO>();
        try {
	    	String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	        Map<String, Object> mapTU = new HashMap<>();
	        mapTU.put(ID_AMBITO, idAmbito);
	        List<TipoUsoExtendedDTO> tipiUso = template.query(QUERY_TIPI_USO_BY_ID_AMBITO, mapTU, getRowMapperTU());
	        Map<String, Object> mapRA = new HashMap<>();
	        Long idRiscossioneUso; 
	        List<RiduzioneAumentoDTO> listRiduzioneAumentoByIdAmbito = new ArrayList<RiduzioneAumentoDTO>();
	       
	        if(tipiUso != null && !tipiUso.isEmpty()) {
	         for(int i=0; i<tipiUso.size(); i++) {
	        	 idRiscossioneUso = tipiUso.get(i).getIdTipoUso(); 
	        	 mapRA.put("idRiscossioneUso",idRiscossioneUso);
	        	 listRiduzioneAumentoByIdAmbito = findListByQuery(CLASSNAME, methodName, QUERY_RIDUZIONE_AUMENTO_BY_ID_AMBITO, mapRA);
	        	 listRiduzioneAumento.addAll(listRiduzioneAumentoByIdAmbito);
	        	}

	        }
            return listRiduzioneAumento;
        } catch (DataAccessException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdAmbito] Errore nell'accesso ai dati", e);
             throw e ;
        } catch (SQLException e) {
        	  LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdAmbito] Errore SQL ", e);
        	    throw e ;
		} finally {
            LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdAmbito] END");
        }
	}

	@Override
	public List<RiduzioneAumentoDTO> loadRiduzioneAumentoByIdTipoUso(Long idTipoUso) throws SQLException {
        LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdTipoUso] BEGIN");
        List<RiduzioneAumentoDTO> listRiduzioneAumento = new ArrayList<RiduzioneAumentoDTO>();
        try {

            Map<String, Object> mapRATU = new HashMap<>();
            mapRATU.put("idTipoUso", idTipoUso);
            List<TipoUsoRiduzioneAumentoDTO> tipoUsoRidAumList = template.query(QUERY_ID_RIDUZIONE_AUMENTO_BY_ID_TIPO_USO, mapRATU, getRowMapperTR());
            if(tipoUsoRidAumList != null) {
            	Map<String, Object> map = new HashMap<>();
            	List<Integer> idRiduzioneAumento = new ArrayList<Integer>();
            	for(int i=0; i<tipoUsoRidAumList.size(); i++) {
            		Integer singleIdRiduzioneAumento = tipoUsoRidAumList.get(i).getIdRiduzioneAumento().intValue();
            		idRiduzioneAumento.add(singleIdRiduzioneAumento);      		
            	}
            	if(idRiduzioneAumento.size()==0)
            		idRiduzioneAumento = null;
        		map.put("idRiduzioneAumento", idRiduzioneAumento);
        		MapSqlParameterSource params = getParameterValue(map);
                listRiduzioneAumento = template.query(QUERY_RIDUZIONE_AUMENTO_BY_ID_RIDUZIONE_AUMENTO_LIST, params, getRowMapper());
            }
           return listRiduzioneAumento;

		} catch (SQLException e) {
			 LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdTipoUso] Errore SQL ", e);
			    throw e ;
        } catch (DataAccessException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdTipoUso] Errore nell'accesso ai dati", e);
            throw e ;
		} finally {
            LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdTipoUso] END");
        }

	}

	@Override
	public List<RiduzioneAumentoDTO> loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum(Long idTipoUso, String flgRidAum) throws SQLException {
        LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum] BEGIN");
        List<RiduzioneAumentoDTO> listRiduzioneAumento = new ArrayList<RiduzioneAumentoDTO>();
        try {
       	
            Map<String, Object> mapRATU = new HashMap<>();
            mapRATU.put("idTipoUso", idTipoUso);
            List<TipoUsoRiduzioneAumentoDTO> tipoUsoRidAumList = template.query(QUERY_ID_RIDUZIONE_AUMENTO_BY_ID_TIPO_USO, mapRATU, getRowMapperTR());
            if(tipoUsoRidAumList != null) {
            	Map<String, Object> map = new HashMap<>();
            	List<Integer> idRiduzioneAumento = new ArrayList<Integer>();
            	for(int i=0; i<tipoUsoRidAumList.size(); i++) {
            		Integer singleIdRiduzioneAumento = tipoUsoRidAumList.get(i).getIdRiduzioneAumento().intValue();
            		idRiduzioneAumento.add(singleIdRiduzioneAumento);         		
            	}
            	if(idRiduzioneAumento.size()==0)
            		idRiduzioneAumento = null;
        		map.put("idRiduzioneAumento", idRiduzioneAumento);
        		int flgRidAumInt = Integer.valueOf(flgRidAum);
        		map.put("flgRidAum", flgRidAumInt);
        		MapSqlParameterSource params = getParameterValue(map);
        		listRiduzioneAumento = template.query(QUERY_RIDUZIONE_AUMENTO_BY_ID_TIPO_USO_AND_FLF_RIDAUM, params, getRowMapper());

            }
           return listRiduzioneAumento;
        } catch (SQLException e) {
	   		 LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum] Errore SQL ", e);
	   	    throw e ;
        } catch (DataAccessException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum] Errore nell'accesso ai dati", e);
            throw e ;
		} finally {
            LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdTipoUsoAndFlgRidAum] END");
        }
	
	}

	@Override
	public RiduzioneAumentoDTO loadRiduzioneAumentoByIdRiduzioneAumento(Long idRiduzioneAumento) throws SQLException {
        LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdRiduzioneAumento] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("idRiduzioneAumento", idRiduzioneAumento);
            MapSqlParameterSource params = getParameterValue(map);
            return template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_ID_RIDUZIONE_AUMENTO, params, getRowMapper());
        } catch (SQLException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdRiduzioneAumento] Errore nell'esecuzione della query", e);
            throw e ;
        } catch (DataAccessException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdRiduzioneAumento] Errore nell'accesso ai dati", e);
            throw e ;
        } finally {
            LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByIdRiduzioneAumento] END");
        }
	}
	
	@Override
	public RiduzioneAumentoDTO loadRiduzioniByIdOrCodRiduzioneAumento(String idOrCodRiduzioneAumento, String dataIniVal,
			String dataFineVal) throws SQLException, ParseException {
		 LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] BEGIN");
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            Map<String, Object> map = new HashMap<>();
				Long idRiduzioneAumento = 0L;
				boolean isIdRiduzioneAumento = false;
				try {
					idRiduzioneAumento = Long.parseLong(idOrCodRiduzioneAumento);
					map.put("idRiduzioneAumento", idRiduzioneAumento);
					isIdRiduzioneAumento = true;
				} catch (NumberFormatException nfe) {
					isIdRiduzioneAumento = false;
					map.put("codRiduzioneAumento", idOrCodRiduzioneAumento);
				}

		            if (dataIniVal  !=  null && dataFineVal !=  null) {
						Date dataIniValidita = formatter.parse(dataIniVal);
						Date dataFineValidita = formatter.parse(dataFineVal);
						map.put("dataInizioValidita", dataIniValidita);
						map.put("dataFineValidita", dataFineValidita);
					    MapSqlParameterSource params = getParameterValue(map);
						if (isIdRiduzioneAumento) {
				            try {
								return template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_ID_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							} catch(EmptyResultDataAccessException e) {
								LOGGER.debug("1A - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
								return new RiduzioneAumentoDTO();

						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("1B - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
						    	return new RiduzioneAumentoDTO();
						    } 
						}else {
						    try {
								return template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							} catch(EmptyResultDataAccessException e) {
								LOGGER.debug("2A - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
								return new RiduzioneAumentoDTO();

						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("2B - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
						    	return new RiduzioneAumentoDTO();
						    } 
						}
				      }
		            else if (dataIniVal  !=  null && dataFineVal ==  null) {
						Date dataIniValidita = formatter.parse(dataIniVal);
						map.put("dataInizioValidita", dataIniValidita);
					    MapSqlParameterSource params = getParameterValue(map);
					    if (isIdRiduzioneAumento) {
				            try {
								return template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_ID_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA +" and rdra.data_fine_validita is null "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							} catch(EmptyResultDataAccessException e) {
								LOGGER.debug("3A - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
								return new RiduzioneAumentoDTO();
	
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("3B - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
						    	return new RiduzioneAumentoDTO();
						    } 
						}else {
						    try {
								return template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA +" and rdra.data_fine_validita is null "+ ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							} catch(EmptyResultDataAccessException e) {
								LOGGER.debug("4A - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
								return new RiduzioneAumentoDTO();
	
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("4B - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
						    	return new RiduzioneAumentoDTO();
						    } 
						}
		        	 }
		            else if (dataIniVal  ==  null && dataFineVal !=  null) {
						Date dataFineValidita = formatter.parse(dataFineVal);
						map.put("dataFineValidita", dataFineValidita);
					    MapSqlParameterSource params = getParameterValue(map);
					    if (isIdRiduzioneAumento) {
				            try {
								return template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_ID_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_FINE_VALIDITA +ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							} catch(EmptyResultDataAccessException e) {
								LOGGER.debug("5A - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
								return new RiduzioneAumentoDTO();
	
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("5B - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
						    	return new RiduzioneAumentoDTO();
						    } 
						}else {
						    try {
								return template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							} catch(EmptyResultDataAccessException e) {
								LOGGER.debug("6A - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
								return new RiduzioneAumentoDTO();
	
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("6B - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
						    	return new RiduzioneAumentoDTO();
						    } 
						}
				   }
		        else {
		            MapSqlParameterSource params = getParameterValue(map);
				    if (isIdRiduzioneAumento) {
			            try {
							return template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_ID_RIDUZIONE_AUMENTO  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						}catch(EmptyResultDataAccessException e) {
							LOGGER.debug("7A - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
							return new RiduzioneAumentoDTO();

					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("7B - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
					    	return new RiduzioneAumentoDTO();
					    } 
					}else {
					    try {
							return template.queryForObject(QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						} catch(EmptyResultDataAccessException e) {
							LOGGER.debug("8A - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
							return new RiduzioneAumentoDTO();

					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("8B - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] No record found in database for "+idOrCodRiduzioneAumento, e);
					    	return new RiduzioneAumentoDTO();
					    } 
					}
		        }
	        } catch (SQLException e) {
	            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] Errore nell'esecuzione della query", e);
	            throw e ;
	        } catch (DataAccessException e) {
	            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] Errore nell'accesso ai dati", e);
	            throw e ;
	        } catch (ParseException e) {
	    		 LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] Errore nel parse ", e);
	    		 throw e ;
			}finally {
	            LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodRiduzioneAumento] END");
	        }
//			return null;
	}
	

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<RiduzioneAumentoDTO> getRowMapper() throws SQLException {
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

	@Override
	public RowMapper<RiduzioneAumentoDTO> getExtendedRowMapper() throws SQLException {
		return new RiduzioneAumentoExtendedRowMapper();
	}
	
	
	public RowMapper<TipoUsoRiduzioneAumentoDTO> getRowMapperTR() throws SQLException {
		 return new TipoUsoRiduzioneAumentoRowMapper();
	}
	
	public RowMapper<TipoUsoExtendedDTO> getRowMapperTU() throws SQLException {
		 return new TipoUsoExtendedRowMapper();
	}
	
    /**
     * The type Tipo uso row mapper.
     */
    public static class TipoUsoRiduzioneAumentoRowMapper implements RowMapper<TipoUsoRiduzioneAumentoDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoUsoRiduzioneAumentoRowMapper() throws SQLException {
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
        public TipoUsoRiduzioneAumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	TipoUsoRiduzioneAumentoDTO bean = new TipoUsoRiduzioneAumentoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipoUsoRiduzioneAumentoDTO bean) throws SQLException {
            bean.setIdRiscossioneUso(rs.getLong("id_tipo_uso"));
            bean.setIdRiduzioneAumento(rs.getLong("id_riduzione_aumento"));

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
        }

        private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setCodAmbito(rs.getString("cod_ambito"));
            bean.setDesAmbito(rs.getString("des_ambito"));
        }
    }

    /**
     * The type Riduzione Aumento row mapper.
     */
    public static class RiduzioneAumentoExtendedRowMapper implements RowMapper<RiduzioneAumentoDTO> {

        /**
         * Instantiates a new Riduzione Aumento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public RiduzioneAumentoExtendedRowMapper() throws SQLException {
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
        	bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
        	bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            bean.setIdRiduzioneAumento(rs.getLong("id_riduzione_aumento"));
            bean.setSiglaRiduzioneAumento(rs.getString("sigla_riduzione_aumento"));
            bean.setDesRiduzioneAumento(rs.getString("des_riduzione_aumento"));
            bean.setPercRiduzioneAumento(rs.getLong("perc_riduzione_aumento"));
            bean.setFlgRiduzioneAumento(rs.getString("flg_riduzione_aumento"));
            bean.setFlgManuale(rs.getString("flg_manuale"));
            bean.setFlgDaApplicare(rs.getString("flg_da_applicare"));
            bean.setOrdinaRiduzioneAumento(rs.getLong("ordina_riduzione_aumento"));
            
        }
    }

	@Override
	public List<RiduzioneAumentoDTO> loadRiduzioniByIdOrCodTipoUsoFlgManuale(String idOrCodTipoUso, String flgManuale,
			String dataIniVal, String dataFineVal) throws SQLException, ParseException {
		LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] BEGIN");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<RiduzioneAumentoDTO> listRiduzioneAumento = new ArrayList<RiduzioneAumentoDTO>();
        try {
            Map<String, Object> map = new HashMap<>();
			Long idTipoUso = 0L;
			boolean isIdTipoUso= false;
		   Integer flgManualeInt = null;
		   try {
				flgManualeInt = Integer.parseInt(flgManuale);
			  map.put("flagManuale", flgManualeInt);
			} catch (NumberFormatException nfe) {
				}
			try {
				idTipoUso = Long.parseLong(idOrCodTipoUso);
				map.put("idTipoUso", idTipoUso);
				isIdTipoUso = true;
			} catch (NumberFormatException nfe) {
				isIdTipoUso = false;
				map.put("codTipoUso", idOrCodTipoUso);
			}

	            if (dataIniVal  !=  null && dataFineVal !=  null) {
					Date dataIniValidita = formatter.parse(dataIniVal);
					Date dataFineValidita = formatter.parse(dataFineVal);
					map.put("dataInizioValidita", dataIniValidita);
					map.put("dataFineValidita", dataFineValidita);
				    MapSqlParameterSource params = getParameterValue(map);
					if (isIdTipoUso) {
						if(flgManuale != null) {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("1 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("2 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
						    	return listRiduzioneAumento;
						    } 
						}else {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("3 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("4 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
						    	return listRiduzioneAumento;
						    } 
						}
			            
			            
					}else {
						if(flgManuale != null) {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("5 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("6 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
						    	return listRiduzioneAumento;
						    } 
						}else {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("7 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("8 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
						    	return listRiduzioneAumento;
						    } 
						}
					}
			      }
	            if (dataIniVal  !=  null && dataFineVal ==  null) {
				Date dataIniValidita = formatter.parse(dataIniVal);
				map.put("dataInizioValidita", dataIniValidita);
			    MapSqlParameterSource params = getParameterValue(map);
			    if (isIdTipoUso) {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("9 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("10 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("11 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("12 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
		            
		            
				}else {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("13 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("14 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA +  ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("15 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("16 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
				}
	        	}
	        	if (dataIniVal  ==  null && dataFineVal !=  null) {
				Date dataFineValidita = formatter.parse(dataFineVal);
				map.put("dataFineValidita", dataFineValidita);
			    MapSqlParameterSource params = getParameterValue(map);
			    if (isIdTipoUso) {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("17 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("18 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("19 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("20 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
		            
		            
				}else {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("21 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("22 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("23 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("24 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
				}
			  }
	        else {
	            MapSqlParameterSource params = getParameterValue(map);
	            if (isIdTipoUso) {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento =  template.query(QUERY_RIDUZIONI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("25 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("26 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_ID_TIPO_USO_AND_FLG_MANUALE  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("27 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("28 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
		            
		            
				}else {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("29 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("30 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_COD_TIPO_USO_AND_FLG_MANUALE  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("31 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("32 - [RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
				}
	        }
        } catch (SQLException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] Errore nell'esecuzione della query", e);
            throw e ;
        } catch (DataAccessException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] Errore nell'accesso ai dati", e);
            throw e ;
        } catch (ParseException e) {
    		 LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] Errore nel parse ", e);
          throw e ;
		}finally {
            LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioniByIdOrCodTipoUsoFlgManuale] END");
        }
	}

	@Override
	public List<RiduzioneAumentoDTO> loadAumentiByIdOrCodTipoUsoFlgManuale(String idOrCodTipoUso, String flgManuale,
			String dataIniVal, String dataFineVal) throws SQLException, ParseException {
		LOGGER.debug("[RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] BEGIN");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<RiduzioneAumentoDTO> listRiduzioneAumento = new ArrayList<RiduzioneAumentoDTO>();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("flagManuale", flgManuale);
			Long idTipoUso = 0L;
			boolean isIdTipoUso= false;
			   Integer flgManualeInt = null;
			   try {
					flgManualeInt = Integer.parseInt(flgManuale);
				  map.put("flagManuale", flgManualeInt);
				} catch (NumberFormatException nfe) {
					}
			try {
				idTipoUso = Long.parseLong(idOrCodTipoUso);
				map.put("idTipoUso", idTipoUso);
				isIdTipoUso = true;
			} catch (NumberFormatException nfe) {
				isIdTipoUso = false;
				map.put("codTipoUso", idOrCodTipoUso);
			}

	            if (dataIniVal  !=  null && dataFineVal !=  null) {
					Date dataIniValidita = formatter.parse(dataIniVal);
					Date dataFineValidita = formatter.parse(dataFineVal);
					map.put("dataInizioValidita", dataIniValidita);
					map.put("dataFineValidita", dataFineValidita);
				    MapSqlParameterSource params = getParameterValue(map);
					if (isIdTipoUso) {
						if(flgManuale != null) {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("1 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("2 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
						    	return listRiduzioneAumento;
						    } 
						}else {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("3 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale]No record found in database for "+isIdTipoUso, e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("4 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
						    	return listRiduzioneAumento;
						    } 
						}
			            
			            
					}else {
						if(flgManuale != null) {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("5 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("6 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
						    	return listRiduzioneAumento;
						    } 
						}else {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("7 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("8 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
						    	return listRiduzioneAumento;
						    } 
						}
					}
			      }
	            else if (dataIniVal  !=  null && dataFineVal ==  null) {
				Date dataIniValidita = formatter.parse(dataIniVal);
				map.put("dataInizioValidita", dataIniValidita);
			    MapSqlParameterSource params = getParameterValue(map);
			    if (isIdTipoUso) {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("9 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("10 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("11 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("12 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
		            
		            
				}else {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("13 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("14 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA +  ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("15 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale]No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("16 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
				}
	        	}
	            else if (dataIniVal  ==  null && dataFineVal !=  null) {
				Date dataFineValidita = formatter.parse(dataFineVal);
				map.put("dataFineValidita", dataFineValidita);
			    MapSqlParameterSource params = getParameterValue(map);
			    if (isIdTipoUso) {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("17 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("18 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("19 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("20 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
		            
		            
				}else {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("21 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale]No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("22 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("23 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("24 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
				}
			  }
	        else {
	            MapSqlParameterSource params = getParameterValue(map);
	            if (isIdTipoUso) {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_ID_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("25 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("26 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_ID_TIPO_USO_AND_FLG_MANUALE  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("27 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("28 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
		            
		            
				}else {
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_COD_TIPO_USO_AND_FLG_MANUALE + " and "+ QUERY_FLAG_MANUALE  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("29 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("30 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale]No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_COD_TIPO_USO_AND_FLG_MANUALE  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("31 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("32 - [RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] No record found in database for "+isIdTipoUso, e);
					    	return listRiduzioneAumento;
					    } 
					}
				}
	        }
        } catch (SQLException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] Errore nell'esecuzione della query", e);
           throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] Errore nell'accesso ai dati", e);
            throw e;
        } catch (ParseException e) {
    		 LOGGER.error("[RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] Errore nel parse ", e);
    		  throw e;
		}finally {
            LOGGER.debug("[RiduzioneAumentoDAOImpl::loadAumentiByIdOrCodTipoUsoFlgManuale] END");
        }
	}

	@Override
	public List<RiduzioneAumentoDTO> loadRiduzioniByflgManuale(String flgManuale, String dataIniVal,
			String dataFineVal) throws SQLException, ParseException {
		LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] BEGIN");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<RiduzioneAumentoDTO> listRiduzioneAumento = new ArrayList<RiduzioneAumentoDTO>();
        try {
            Map<String, Object> map = new HashMap<>();
            Integer flgManualeInt = null;
			try {
				flgManualeInt = Integer.parseInt(flgManuale);
				 map.put("flagManuale", flgManualeInt);
			} catch (NumberFormatException nfe) {
			}
			
	            if (dataIniVal  !=  null && dataFineVal !=  null) {
					Date dataIniValidita = formatter.parse(dataIniVal);
					Date dataFineValidita = formatter.parse(dataFineVal);
					map.put("dataInizioValidita", dataIniValidita);
					map.put("dataFineValidita", dataFineValidita);
				    MapSqlParameterSource params = getParameterValue(map);
			
						if(flgManuale != null) {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("1 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("2 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
						    	return listRiduzioneAumento;
						    } 
						}else {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("3 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("4 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
						    	return listRiduzioneAumento;
						    } 
						}
			            
			      }
	            else if (dataIniVal  !=  null && dataFineVal ==  null) {
				Date dataIniValidita = formatter.parse(dataIniVal);
				map.put("dataInizioValidita", dataIniValidita);
			    MapSqlParameterSource params = getParameterValue(map);
		
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("5 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("6 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("7 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("8 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
					    	return listRiduzioneAumento;
					    } 
					}
		        
	        	}
	            else if (dataIniVal  ==  null && dataFineVal !=  null) {
					Date dataFineValidita = formatter.parse(dataFineVal);
					map.put("dataFineValidita", dataFineValidita);
				    MapSqlParameterSource params = getParameterValue(map);
			
						if(flgManuale != null) {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_FLAG_MANUALE + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("9 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("10 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
						    	return listRiduzioneAumento;
						    } 
						}else {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_FLG_RIDUZIONE_AUMENTO + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("11 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("12 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
						    	return listRiduzioneAumento;
						    } 
						}
			            
		      
			  }
	        else {
	            MapSqlParameterSource params = getParameterValue(map);
	          
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_FLAG_MANUALE  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("13 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("14 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_RIDUZIONI_BY_FLG_RIDUZIONE_AUMENTO  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("15 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("16 - [RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] No record found in database  ", e);
					    	return listRiduzioneAumento;
					    } 
					}
		            
		       
	        }
        } catch (SQLException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] Errore nell'esecuzione della query", e);
            throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] Errore nell'accesso ai dati", e);
            throw e;
        } catch (ParseException e) {
    		 LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] Errore nel parse ", e);
    		  throw e;
		}finally {
            LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioniByflgManuale] END");
        }
	}

	@Override
	public List<RiduzioneAumentoDTO> loadAumentiByflgManuale(String flgManuale, String dataIniVal, String dataFineVal) throws SQLException, ParseException {
		LOGGER.debug("[RiduzioneAumentoDAOImpl::loadAumentiByflgManuale] BEGIN");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<RiduzioneAumentoDTO> listRiduzioneAumento = new ArrayList<RiduzioneAumentoDTO>();
        try {
            Map<String, Object> map = new HashMap<>();
            Integer flgManualeInt = null;
			try {
				flgManualeInt = Integer.parseInt(flgManuale);
				 map.put("flagManuale", flgManualeInt);
			} catch (NumberFormatException nfe) {
			}
	            if (dataIniVal  !=  null && dataFineVal !=  null) {
					Date dataIniValidita = formatter.parse(dataIniVal);
					Date dataFineValidita = formatter.parse(dataFineVal);
					map.put("dataInizioValidita", dataIniValidita);
					map.put("dataFineValidita", dataFineValidita);
				    MapSqlParameterSource params = getParameterValue(map);
			
						if(flgManuale != null) {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("1 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("2 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
						    	return listRiduzioneAumento;
						    } 
						}else {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("3 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("4 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
						    	return listRiduzioneAumento;
						    } 
						}
			            
			      }
	            else if (dataIniVal  !=  null && dataFineVal ==  null) {
					Date dataIniValidita = formatter.parse(dataIniVal);
					map.put("dataInizioValidita", dataIniValidita);
				    MapSqlParameterSource params = getParameterValue(map);
			
						if(flgManuale != null) {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_FLAG_MANUALE + " and "+ QUERY_DATA_INIZIO_VALIDITA  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("5 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("6 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
						    	return listRiduzioneAumento;
						    } 
						}else {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_DATA_INIZIO_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("7 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("8 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
						    	return listRiduzioneAumento;
						    } 
						}
			        
	        	}
	            else if (dataIniVal  ==  null && dataFineVal !=  null) {
					Date dataFineValidita = formatter.parse(dataFineVal);
					map.put("dataFineValidita", dataFineValidita);
				    MapSqlParameterSource params = getParameterValue(map);
			
						if(flgManuale != null) {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_FLAG_MANUALE + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("9 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("10 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
						    	return listRiduzioneAumento;
						    } 
						}else {
							try {
				            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_FLG_RIDUZIONE_AUMENTO + " and "+QUERY_DATA_FINE_VALIDITA + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
							    return listRiduzioneAumento;
				            } catch(EmptyResultDataAccessException e) {
								LOGGER.debug("11 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
								return listRiduzioneAumento;
						    }catch(IncorrectResultSizeDataAccessException e) {
						    	LOGGER.debug("12 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
						    	return listRiduzioneAumento;
						    } 
						}
			            
			      
			  }
	        else {
	            MapSqlParameterSource params = getParameterValue(map);
	          
					if(flgManuale != null) {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_FLG_RIDUZIONE_AUMENTO + " and "+ QUERY_FLAG_MANUALE  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("13 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("14 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
					    	return listRiduzioneAumento;
					    } 
					}else {
						try {
			            	listRiduzioneAumento=  template.query(QUERY_AUMENTI_BY_FLG_RIDUZIONE_AUMENTO  + ORDER_BY_ORDINA_RIDUZIONE_AUMENTO, params, getRowMapper());
						    return listRiduzioneAumento;
			            } catch(EmptyResultDataAccessException e) {
							LOGGER.debug("15 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
							return listRiduzioneAumento;
					    }catch(IncorrectResultSizeDataAccessException e) {
					    	LOGGER.debug("16 - [RiduzioneAumentoDAOImpl::loadAumentiByflgManuale]  No record found in database  ", e);
					    	return listRiduzioneAumento;
					    } 
					}
		            
		       
	        }
        } catch (SQLException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadAumentiByflgManuale] Errore nell'esecuzione della query", e);
            throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadAumentiByflgManuale] Errore nell'accesso ai dati", e);
            throw e;
        } catch (ParseException e) {
    		 LOGGER.error("[RiduzioneAumentoDAOImpl::loadAumentiByflgManuale] Errore nel parse ", e);
    		  throw e;
		}finally {
            LOGGER.debug("[RiduzioneAumentoDAOImpl::loadAumentiByflgManuale] END");
        }
	}
	
	@Override
	public List<RiduzioneAumentoDTO> loadRiduzioneAumentoByAnnualitaUsoSd(Long idAnnualitaUsoSd) throws SQLException {
        LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByAnnualitaUsoSd] BEGIN");
        List<RiduzioneAumentoDTO> listRiduzioneAumento = new ArrayList<RiduzioneAumentoDTO>();
        try {
	        Map<String, Object> map = new HashMap<>();
	        map.put("idAnnualitaUsoSd", idAnnualitaUsoSd);
	        
	        MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_RIDUZIONE_AUMENTO_BY_ANNUALITA_SD, null, null),
					params, getRowMapper());
	        
        } catch (DataAccessException e) {
            LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByAnnualitaUsoSd] Errore nell'accesso ai dati", e);
            throw e;
        } catch (SQLException e) {
        	  LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByAnnualitaUsoSd] Errore SQL ", e);
        	  throw e;
		} finally {
            LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByAnnualitaUsoSd] END");
        }

	}
	
	@Override
	public List<RiduzioneAumentoDTO> loadRiduzioneAumentoByRiscossioneTipoUso(Long idRiscossione, String codTipoUso) throws SQLException {
		LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByRiscossioneTipoUso] BEGIN");
		List<RiduzioneAumentoDTO> listRiduzioneAumento = new ArrayList<RiduzioneAumentoDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRiscossione", idRiscossione);
			map.put("codTipoUso", codTipoUso);

			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_RIDUZIONE_AUMENTO_BY_RISCOSSIONE_TIPO_USO, null, null), params,
					getRowMapper());

		} catch (DataAccessException e) {
			LOGGER.error(
					"[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByRiscossioneTipoUso] Errore nell'accesso ai dati",
					e);
			  throw e;
		} catch (SQLException e) {
			LOGGER.error("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByRiscossioneTipoUso] Errore SQL ", e);
			  throw e;
		} finally {
			LOGGER.debug("[RiduzioneAumentoDAOImpl::loadRiduzioneAumentoByRiscossioneTipoUso] END");
		}
	
	}


    
}
