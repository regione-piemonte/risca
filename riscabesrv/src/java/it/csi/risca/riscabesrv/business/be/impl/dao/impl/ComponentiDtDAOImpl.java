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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.ComponentiDtDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.ComponenteDtExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoComponenteDtDTO;

/**
 * The type Province dao.
 *
 * @author CSI PIEMONTE
 */
public class ComponentiDtDAOImpl extends RiscaBeSrvGenericDAO<ComponenteDtExtendedDTO> implements ComponentiDtDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
    private static final String QUERY_COMPONENTE_DT = "SELECT distinct rdcdt.*, rdtcdt.* FROM risca_d_componente_dt rdcdt "
    		+ "INNER JOIN risca_d_ambito rda ON rdcdt.id_ambito = rda.id_ambito "
    		+ "INNER JOIN risca_d_tipo_componente_dt rdtcdt ON rdcdt.id_tipo_componente_dt = rdtcdt.id_tipo_componente_dt "
    		+ "WHERE rdcdt.id_ambito = :idAmbito ";
    
    private static final String DATA_INIZIO_E_DATA_FINE_VALIDITA = "AND data_inizio_validita <= :current_date "
    		+ "AND (data_fine_validita is null OR data_fine_validita > current_date)";
	
    private static final String QUERY_COMPONENTE_DT_BY_COD_TIPO_COMP = QUERY_COMPONENTE_DT + " AND rdtcdt.cod_tipo_componente_dt= :codTipoComponente ";
    
	private static final String DATA_RIFERIMENTO = "current_date";
	
	private static final String QUERY_COMPONENTE_DT_BY_COD_AND_DATE = " select rdcd.*, rdtcd.* "
			+ " from risca_d_componente_dt rdcd, risca_d_tipo_componente_dt rdtcd  "
			+ " where rdcd.id_tipo_componente_dt = rdtcd.id_tipo_componente_dt  "
			+ " and id_ambito = :idAmbito "
			+ " and rdtcd.cod_tipo_componente_dt = :codTipoComponente "
			+ " and (TO_DATE(:dataRif) between data_inizio_validita and data_fine_validita or  "
			+ " (TO_DATE(:dataRif) >= data_inizio_validita and data_fine_validita is null))";
    
    

	@Override
	public List<ComponenteDtExtendedDTO> loadComponentiDt(Long idAmbito, String codTipoComponente, boolean attivo) throws SQLException {
        LOGGER.debug("[ComponentiDtDAOImpl::loadComponentiDt] BEGIN");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
       
        try {
            Map<String, Object> map = new HashMap<>();
    		map.put("idAmbito", idAmbito);
    			if(attivo) {
    	    		map.put(DATA_RIFERIMENTO, now);
    				if(codTipoComponente != null) {
            			map.put("codTipoComponente", codTipoComponente); 
                    	MapSqlParameterSource params = getParameterValue(map);
                    	return template.query(QUERY_COMPONENTE_DT_BY_COD_TIPO_COMP + DATA_INIZIO_E_DATA_FINE_VALIDITA , params, getRowMapper());
            		}
            		else {
            			MapSqlParameterSource params = getParameterValue(map);
                    	return template.query(QUERY_COMPONENTE_DT + DATA_INIZIO_E_DATA_FINE_VALIDITA , params, getRowMapper());
            		}
    				
    			}else {
    				if(codTipoComponente != null) {
            			map.put("codTipoComponente", codTipoComponente); 
                    	MapSqlParameterSource params = getParameterValue(map);
                    	return template.query(QUERY_COMPONENTE_DT_BY_COD_TIPO_COMP, params, getRowMapper());
            		}
            		else {
            			MapSqlParameterSource params = getParameterValue(map);
                    	return template.query(QUERY_COMPONENTE_DT, params, getRowMapper());
            		}
    				
    			}
	        	
        } catch (SQLException e) {
            LOGGER.error("[ComponentiDtDAOImpl::loadComponentiDt] Errore nell'esecuzione della query", e);
            LOGGER.debug("[ComponentiDtDAOImpl::loadComponentiDt] END");
           throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[ComponentiDtDAOImpl::loadComponentiDt] Errore nell'accesso ai dati", e);
            LOGGER.debug("[ComponentiDtDAOImpl::loadComponentiDt] END");
            throw e;
        } finally {
            LOGGER.debug("[ComponentiDtDAOImpl::loadComponentiDt] END");
        }
	}
	
	@Override
	public ComponenteDtExtendedDTO loadComponenteDt(Long idAmbito, String codTipoComponente, String dataRif) throws SQLException {
		LOGGER.debug("[ComponentiDtDAOImpl::loadComponenteDt] BEGIN");

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			map.put("codTipoComponente", codTipoComponente);
			map.put("dataRif", dataRif);
			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(QUERY_COMPONENTE_DT_BY_COD_AND_DATE, params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[ComponentiDtDAOImpl::loadComponenteDt] Errore nell'esecuzione della query", e);
			LOGGER.debug("[ComponentiDtDAOImpl::loadComponenteDt] END");
		    throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[ComponentiDtDAOImpl::loadComponenteDt] Errore nell'accesso ai dati", e);
			LOGGER.debug("[ComponentiDtDAOImpl::loadComponenteDt] END");
		    throw e;
		} finally {
			LOGGER.debug("[ComponentiDtDAOImpl::loadComponentiDt] END");
		}
	}



	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public RowMapper<ComponenteDtExtendedDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new ComponentiDtRowMapper();
	}



	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
  /**
  * The type ComponentiDt row mapper.
  */
 public static class ComponentiDtRowMapper implements RowMapper<ComponenteDtExtendedDTO> {

     /**
      * Instantiates a new ComponentiDt row mapper.
      *
      * @throws SQLException the sql exception
      */
     public ComponentiDtRowMapper() throws SQLException {
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
     public ComponenteDtExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
    	 ComponenteDtExtendedDTO bean = new ComponenteDtExtendedDTO();
         populateBean(rs, bean);
         return bean;
     }

     private void populateBean(ResultSet rs, ComponenteDtExtendedDTO bean) throws SQLException {
         bean.setIdComponenteDt(rs.getLong("id_componente_dt"));
         bean.setIdAmbito(rs.getLong("id_ambito"));
         bean.setNomeComponenteDt(rs.getString("nome_componente_dt"));
         bean.setDesComponenteDt(rs.getString("des_componente_dt"));
         bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
         bean.setDataFineValidita(rs.getDate("data_fine_validita"));
         TipoComponenteDtDTO tipoComponenteDt = new TipoComponenteDtDTO();
         populateBeanTipoComponenteDt(rs, tipoComponenteDt);
         bean.setTipoComponenteDt(tipoComponenteDt);
     
 }

	private void populateBeanTipoComponenteDt(ResultSet rs, TipoComponenteDtDTO bean) throws SQLException{
	    bean.setIdTipoComponenteDt(rs.getLong("id_tipo_componente_dt"));
	    bean.setCodTipoComponenteDt(rs.getString("cod_tipo_componente_dt"));
	    bean.setDesTipoComponenteDt(rs.getString("des_tipo_componente_dt"));

	}
 }


 

 
}
