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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.NazioneDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ProvinciaExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.RegioneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ComuniDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.ComuneDTO;

/**
 * The type Province dao.
 *
 * @author CSI PIEMONTE
 */
public class ComuniDAOImpl extends RiscaBeSrvGenericDAO<ComuneDTO> implements ComuniDAO { 

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String COD_ISTAT_COMUNE = "codIstatComune";
	private static final String ID_COMUNE = "idComune";
	
    private static final String QUERY_COMUNI = "SELECT distinct rdc.*, rdp.*, rdr.* , rdn.* FROM risca_d_comune rdc "
    		+ "INNER JOIN risca_d_provincia rdp ON rdc.id_provincia = rdp.id_provincia "
    		+ "INNER JOIN risca_d_regione rdr ON rdp.id_regione = rdr.id_regione  "
    		+ "	INNER JOIN risca_d_nazione rdn   ON rdr.id_nazione = rdn.id_nazione ";
	
	private static final String QUERY_COMUNI_LIKE = "SELECT distinct rdc.*, rdp.*, rdr.* , rdn.* FROM risca_d_comune rdc "
    		+ "INNER JOIN risca_d_provincia rdp ON rdc.id_provincia = rdp.id_provincia "
    		+ "INNER JOIN risca_d_regione rdr ON rdp.id_regione = rdr.id_regione  "
    		+ "INNER JOIN risca_d_nazione rdn   ON rdr.id_nazione = rdn.id_nazione "
			+ "WHERE rdc.denom_comune ilike :q ";
    
    private static final String AND_QUERY_COMUNI = "AND rdc.data_fine_validita is null ";
        
    private static final String WHERE_QUERY_COMUNI = "WHERE rdc.data_fine_validita is null ";
    
    private static final String ORDER_BY = "ORDER BY rdc.denom_comune ";
    
    private static final String AND_COD_ISTAT_COMUNE =  " and rdc.cod_istat_comune = :codIstatComune ";
    
    private static final String QUERY_COMUNI_BY_ID_OR_COD_PROVINCE_AND_ID_OR_COD_REGIONI = "SELECT distinct rdc.* FROM risca_d_comune rdc   "
    		+ "INNER JOIN risca_d_provincia rdp ON rdc.id_provincia = rdp.id_provincia "
    		+ "INNER JOIN risca_d_regione rdr ON rdp.id_regione = rdr.id_regione  "
    		+ "WHERE (rdc.id_provincia = :idProvincia OR rdp.cod_provincia = :codProvincia ) "
    		+ "AND (rdp.id_regione = :idRegione OR  rdr.cod_regione = :codRegione ) "
    		+ "and rdc.data_fine_validita is null ";
    
//    private static final String QUERY_COMUNI_BY_COD_PROVINCE_AND_COD_REGIONI = "SELECT distinct rdc.* FROM risca_d_comune rdc    "
//    		+ "INNER JOIN risca_d_provincia rdp ON rdc.id_provincia = rdp.id_provincia "
//    		+ "INNER JOIN risca_d_regione rdr ON rdp.id_regione = rdr.id_regione  "
//    		+ "WHERE rdp.cod_provincia = :codProvincia AND "
//    		+ "and rdc.data_fine_validita is null ";
    
    private static final String QUERY_COMUNE_BY_COD_ISTAT_COMUNE = QUERY_COMUNI
            + "where rdc.cod_istat_comune = :codIstatComune ";
    
    private static final String QUERY_COMUNE_BY_ID_COMUNE = QUERY_COMUNI
            + "where rdc.id_comune = :idComune ";

	@Override
	public List<ComuneExtendedDTO> loadComuni(boolean attivo) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<ComuneExtendedDTO> listaComuni = new ArrayList<ComuneExtendedDTO>();
        if(attivo)
        	listaComuni = findListByQuery(CLASSNAME, methodName, QUERY_COMUNI + WHERE_QUERY_COMUNI + ORDER_BY, null);
        else
        	listaComuni = findListByQuery(CLASSNAME, methodName, QUERY_COMUNI + ORDER_BY, null);
        return listaComuni;
	}

	@Override
	public List<ComuneDTO> loadComuniByIdOrCod(String idRegione, String idProvincia, String codIstatComune) {
        LOGGER.debug("[ComuniDAOImpl::loadComuniByIdOrCod] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            int idProv = Integer.parseInt(idProvincia);
            int idReg = Integer.parseInt(idRegione);
            
        		map.put("idProvincia", idProv);
	            map.put("idRegione", idReg);
                map.put("codProvincia",idProvincia);
                map.put("codRegione",idRegione);
	            map.put("codIstatComune", codIstatComune); 
	            MapSqlParameterSource params = getParameterValue(map);
	            return template.query(QUERY_COMUNI_BY_ID_OR_COD_PROVINCE_AND_ID_OR_COD_REGIONI  + AND_COD_ISTAT_COMUNE + ORDER_BY, params, getRowMapper());
	        	
        } catch (SQLException e) {
            LOGGER.error("[ComuniDAOImpl::loadComuniByIdOrCod] Errore nell'esecuzione della query", e);
            return new ArrayList<ComuneDTO>();
        } catch (DataAccessException e) {
            LOGGER.error("[ComuniDAOImpl::loadComuniByIdOrCod] Errore nell'accesso ai dati", e);
            return new ArrayList<ComuneDTO>();
        } finally {
            LOGGER.debug("[ComuniDAOImpl::loadComuniByIdOrCod] END");
        }
	}

	@Override
	public List<ComuneDTO> loadComuniByCodRegione(String codRegione, String codProvincia) {
		 LOGGER.debug("[ComuniDAOImpl::loadComuniByCodRegione] BEGIN");
	        try {
	        	  Map<String, Object> map = new HashMap<>();
	              int idProv = Integer.parseInt(codProvincia);
	              int idReg = Integer.parseInt(codRegione);
	              
	          		map.put("idProvincia", idProv);
	  	            map.put("idRegione", idReg);
	                  map.put("codProvincia",codProvincia);
	                  map.put("codRegione",codRegione);
	  	            MapSqlParameterSource params = getParameterValue(map);
	  	            return template.query(QUERY_COMUNI_BY_ID_OR_COD_PROVINCE_AND_ID_OR_COD_REGIONI   + ORDER_BY, params, getRowMapper());

	            

	        } catch (SQLException e) {
	            LOGGER.error("[ComuniDAOImpl::loadComuniByCodRegione] Errore nell'esecuzione della query", e);
	            return new ArrayList<ComuneDTO>();
	        } catch (DataAccessException e) {
	            LOGGER.error("[ComuniDAOImpl::loadComuniByCodRegione] Errore nell'accesso ai dati", e);
	            return new ArrayList<ComuneDTO>();
	        } finally {
	            LOGGER.debug("[ComuniDAOImpl::loadComuniByCodRegione] END");
	        }
	}

	@Override
	public List<ComuneExtendedDTO> loadComuniByRicerca(String q, boolean attivo) {
		LOGGER.debug("[ComuniDAOImpl::loadComuniByRicerca] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<ComuneExtendedDTO> listaComuni = new ArrayList<ComuneExtendedDTO>();
        Map<String, Object> map = new HashMap<>();
        map.put("q", q + "%");
        if(attivo)
        	listaComuni = findListByQuery(CLASSNAME, methodName, QUERY_COMUNI_LIKE + AND_QUERY_COMUNI + ORDER_BY, map);
        else
        	listaComuni = findListByQuery(CLASSNAME, methodName, QUERY_COMUNI_LIKE + ORDER_BY, map);
        LOGGER.debug("[ComuniDAOImpl::loadComuniByRicerca] END");
        return listaComuni;
	}

	@Override
	public ComuneDTO loadComuneByCodIstatComune(String codIstatComune) {
		LOGGER.debug("[ComuniDAOImpl::loadComuneByCodIstatComune] BEGIN");
	    try {
	        Map<String, Object> map = new HashMap<>();
	        map.put(COD_ISTAT_COMUNE, codIstatComune);
	        MapSqlParameterSource params = getParameterValue(map);
	        return template.queryForObject(QUERY_COMUNE_BY_COD_ISTAT_COMUNE, params, getRowMapper());
	    } catch (SQLException e) {
	         LOGGER.error("[ComuniDAOImpl::loadComuneByCodIstatComune] Errore nell'esecuzione della query", e);
	         return null;
	    } catch (DataAccessException e) {
	         LOGGER.error("[ComuniDAOImpl::loadComuneByCodIstatComune] Errore nell'accesso ai dati", e);
	         return null;
	    } finally {
	         LOGGER.debug("[ComuniDAOImpl::loadComuneByCodIstatComune] END");
	    }
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<ComuneDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new ComuniRowMapper();
	}

	@Override
	public RowMapper<ComuneExtendedDTO> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new ComuniExtendedRowMapper();
	}
	
  /**
  * The type Comune row mapper.
  */
 public static class ComuniRowMapper implements RowMapper<ComuneDTO> {

     /**
      * Instantiates a new Provincia row mapper.
      *
      * @throws SQLException the sql exception
      */
     public ComuniRowMapper() throws SQLException {
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
     public ComuneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
         ComuneDTO bean = new ComuneDTO();
         populateBean(rs, bean);
         return bean;
     }

     private void populateBean(ResultSet rs, ComuneDTO bean) throws SQLException {
         bean.setIdComune(rs.getLong("id_comune"));
         bean.setCodIstatComune(rs.getString("cod_istat_comune"));
         bean.setCodBelfioreComune(rs.getString("cod_belfiore_comune"));
         bean.setDenomComune(rs.getString("denom_comune"));
         bean.setIdProvincia(rs.getLong("id_provincia"));
         bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
         bean.setDataFineValidita(rs.getDate("data_fine_validita"));
         bean.setDtIdComune(rs.getLong("dt_id_comune"));
         bean.setDtIdComunePrev(rs.getLong("dt_id_comune_prev"));
         bean.setDtIdComuneNext(rs.getLong("dt_id_comune_next"));
         bean.setCapComune(rs.getString("cap_comune"));
     }
 }

 /**
  * The type Comune extended row mapper.
  */
 public static class ComuniExtendedRowMapper implements RowMapper<ComuneExtendedDTO> {

     /**
      * Instantiates a new Provincia row mapper.
      *
      * @throws SQLException the sql exception
      */
     public ComuniExtendedRowMapper() throws SQLException {
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
     public ComuneExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
    	 ComuneExtendedDTO bean = new ComuneExtendedDTO();
         populateBean(rs, bean);
         return bean;
     }

     private void populateBean(ResultSet rs, ComuneExtendedDTO bean) throws SQLException {
         bean.setIdComune(rs.getLong("id_comune"));
         bean.setCodIstatComune(rs.getString("cod_istat_comune"));
         bean.setCodBelfioreComune(rs.getString("cod_belfiore_comune"));
         bean.setDenomComune(rs.getString("denom_comune"));
         bean.setIdProvincia(rs.getLong("id_provincia"));
         bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
         bean.setDataFineValidita(rs.getDate("data_fine_validita"));
         bean.setDtIdComune(rs.getLong("dt_id_comune"));
         bean.setDtIdComunePrev(rs.getLong("dt_id_comune_prev"));
         bean.setDtIdComuneNext(rs.getLong("dt_id_comune_next"));
         bean.setCapComune(rs.getString("cap_comune"));
         ProvinciaExtendedDTO ProvinciaExtendedDTO = new ProvinciaExtendedDTO();
         populateBeanProvincia(rs, ProvinciaExtendedDTO);
         bean.setProvincia(ProvinciaExtendedDTO);
         
     }

	private void populateBeanProvincia(ResultSet rs, ProvinciaExtendedDTO bean) throws SQLException{
        bean.setIdProvincia(rs.getLong("id_provincia"));
        bean.setCodProvincia(rs.getString("cod_provincia"));
        bean.setDenomProvincia(rs.getString("denom_provincia"));
        bean.setSiglaProvincia(rs.getString("sigla_provincia"));
        bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
        bean.setDataFineValidita(rs.getDate("data_fine_validita"));
  
        RegioneExtendedDTO regione = new RegioneExtendedDTO();
        populateBeanRegione(rs, regione);
        bean.setRegione(regione);
	}
    private void populateBeanRegione(ResultSet rs, RegioneExtendedDTO bean) throws SQLException {
        bean.setIdRegione(rs.getLong("id_regione"));
        bean.setCodRegione(rs.getString("cod_regione"));
        bean.setDenomRegione(rs.getString("denom_regione"));
        //bean.setIdNazione(rs.getLong("id_nazione"));
        bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
        bean.setDataFineValidita(rs.getDate("data_fine_validita"));
        
        NazioneDTO nazione = new NazioneDTO();
        populateBeanNazione(rs, nazione);
        bean.setNazione(nazione);
    }
    
    private void populateBeanNazione(ResultSet rs, NazioneDTO bean) throws SQLException {
        bean.setIdNazione(rs.getLong("id_nazione"));
        bean.setCodIstatNazione(rs.getString("cod_istat_nazione"));
        bean.setCodBelfioreNazione(rs.getString("cod_belfiore_nazione"));
        bean.setDenomNazione(rs.getString("denom_nazione"));
        bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
        bean.setDataFineValidita(rs.getDate("data_fine_validita"));
        bean.setDtIdStato(rs.getLong("dt_id_stato"));
        bean.setDtIdStatoPrev(rs.getLong("dt_id_stato_prev"));
        bean.setDtIdStatoNext(rs.getLong("dt_id_stato_next"));
        bean.setCodIso2(rs.getString("cod_iso2"));
        
    }
 }

@Override
public ComuneExtendedDTO loadComuneById(Long idComune) {
	LOGGER.debug("[ComuniDAOImpl::loadComuneById] BEGIN");
    try {
        Map<String, Object> map = new HashMap<>();
        map.put(ID_COMUNE, idComune);
        MapSqlParameterSource params = getParameterValue(map);
        return template.queryForObject(QUERY_COMUNE_BY_ID_COMUNE, params, getExtendedRowMapper());
    } catch (SQLException e) {
         LOGGER.error("[ComuniDAOImpl::loadComuneById] Errore nell'esecuzione della query", e);
         return null;
    } catch (DataAccessException e) {
         LOGGER.error("[ComuniDAOImpl::loadComuneById] Errore nell'accesso ai dati", e);
         return null;
    } finally {
         LOGGER.debug("[ComuniDAOImpl::loadComuneById] END");
    }
}

 
}
