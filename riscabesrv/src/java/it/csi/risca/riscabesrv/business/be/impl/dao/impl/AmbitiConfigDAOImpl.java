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

import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;

/**
 * The type ambiti config dao impl.
 *
 * @author CSI PIEMONTE
 */
public class AmbitiConfigDAOImpl extends RiscaBeSrvGenericDAO<AmbitoConfigDTO> implements AmbitiConfigDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
    
    private static final String QUERY_AMBITI_CONFIG_BYFLG = "SELECT rrac.* "
            + "FROM risca_r_ambito_config rrac "
            + "WHERE flg_attivo = 1";
    //Forse da rivedere cosi
    // where id_ambito = ambito attore in linea and chiave = vedere elenco di seguito riportato and flg_attivo=1
    //possibile incoerenza tra pagina 17 delle specifiche trasverali e pagina 51 del doc dei servizi
    private static final String QUERY_AMBITI_CONFIG ="SELECT rrac.* "
    		+ "FROM risca_r_ambito_config rrac "  
    		+ "INNER JOIN risca_d_ambito rda ON rrac.id_ambito = rda.id_ambito ";
    
    private static final String  ID_AMBITO = " rda.id_ambito = :idAmbito";
    
    private static final String  COD_AMBITO = " rda.cod_ambito = :codAmbito";
    private static final String  WHERE =" WHERE "; 
    private static final String  AND =" AND "; 
    private static final String QUERY_AMBITI_CONFIG_BY_ID_AMBITO =   QUERY_AMBITI_CONFIG +WHERE
    		+ ID_AMBITO;
    
    private static final String QUERY_AMBITI_CONFIG_BY_COD_AMBITO =  QUERY_AMBITI_CONFIG +WHERE
    		+ COD_AMBITO;
    		
    
    private static final String QUERY_AMBITI_CONFIG_BY_COD_AMBITO_AND_KEY = QUERY_AMBITI_CONFIG
    		+ "WHERE rrac.chiave = :chiave "  ;
    
	
	@Override
	public List<AmbitoConfigDTO> loadAmbitiConfig() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<AmbitoConfigDTO> listAmbitoConfig = new ArrayList<AmbitoConfigDTO>();
        try {
        	listAmbitoConfig = findListByQuery(CLASSNAME, methodName, QUERY_AMBITI_CONFIG_BYFLG, null);
		} catch (Exception e) {
            LOGGER.error("[AmbitiConfigDAOImpl::loadAmbitiConfig] Errore generale ", e);
            throw e;
		}
        return listAmbitoConfig;
	}

	@Override
	public List<AmbitoConfigDTO> loadAmbitiConfigByIdOrCodAmbito(String idAmbito) throws SQLException {
        LOGGER.debug("[AmbitiConfigDAOImpl::loadAmbitiConfigByIdOrCodAmbito] BEGIN");
        List<AmbitoConfigDTO> listAmbitoConfig = new ArrayList<AmbitoConfigDTO>();
        try {
            Map<String, Object> map = new HashMap<>();
            int id = 0;
            boolean bool = false;
            try {
            	id = Integer.parseInt(idAmbito);
            	bool = true;
            } catch (NumberFormatException nfe) {
                bool = false;
            }
       
        	if(bool) {
	            map.put("idAmbito", id);
	            MapSqlParameterSource params = getParameterValue(map);
	            listAmbitoConfig = template.query(QUERY_AMBITI_CONFIG_BY_ID_AMBITO, params, getRowMapper());
	            return listAmbitoConfig;
	        	}
            else {
                map.put("codAmbito",idAmbito);
                MapSqlParameterSource params = getParameterValue(map);
                listAmbitoConfig = template.query(QUERY_AMBITI_CONFIG_BY_COD_AMBITO, params, getRowMapper());
                return listAmbitoConfig;
            }
        } catch (SQLException e) {
            LOGGER.error("[AmbitiConfigDAOImpl::loadAmbitiConfigByIdOrCodAmbito] Errore nell'esecuzione della query", e);
            throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[AmbitiConfigDAOImpl::loadAmbitiConfigByIdOrCodAmbito] Errore nell'accesso ai dati", e);
            throw e;
        } finally {
            LOGGER.debug("[AmbitiConfigDAOImpl::loadAmbitiConfigByIdOrCodAmbito] END");
        }
	}

	@Override
	public List<AmbitoConfigDTO> loadAmbitiConfigByCodeAndKey(String codAmbito, String chiave) throws SQLException {
        LOGGER.debug("[AmbitiConfigDAOImpl::loadAmbitiConfigByCodeAndKey] BEGIN");
        List<AmbitoConfigDTO> listAmbitoConfig = new ArrayList<AmbitoConfigDTO>();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("chiave", chiave);
                int id = 0;
                boolean bool = false;
                try {
                	id = Integer.parseInt(codAmbito);
                	bool = true;
                } catch (NumberFormatException nfe) {
                    bool = false;
                }
           
            	if(bool) {
    	            map.put("idAmbito", id);
    	            MapSqlParameterSource params = getParameterValue(map);
    	            listAmbitoConfig = template.query(QUERY_AMBITI_CONFIG_BY_COD_AMBITO_AND_KEY +AND+ID_AMBITO, params, getRowMapper());
    	            return listAmbitoConfig;
    	        	}
                else {
                    map.put("codAmbito",codAmbito);
                    MapSqlParameterSource params = getParameterValue(map);
                    listAmbitoConfig = template.query(QUERY_AMBITI_CONFIG_BY_COD_AMBITO_AND_KEY  +AND+COD_AMBITO, params, getRowMapper());
                    return listAmbitoConfig;
                }
        } catch (SQLException e) {
            LOGGER.error("[AmbitiConfigDAOImpl::loadAmbitiConfigByCodeAndKey] Errore nell'esecuzione della query", e);
            throw e;
        } catch (DataAccessException e) {
            LOGGER.error("[AmbitiConfigDAOImpl::loadAmbitiConfigByCodeAndKey] Errore nell'accesso ai dati", e);
            throw e;
        } finally {
            LOGGER.debug("[AmbitiConfigDAOImpl::loadAmbitiConfigByCodeAndKey] END");
        }
	}


	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<AmbitoConfigDTO> getRowMapper() throws SQLException {
		 return new AmbitiConfigRowMapper();
	}

    /**
     * The type Ambiti config row mapper.
     */
    public static class AmbitiConfigRowMapper implements RowMapper<AmbitoConfigDTO> {

        /**
         * Instantiates a new Ambiti config row mapper.
         *
         * @throws SQLException the sql exception
         */
        public AmbitiConfigRowMapper() throws SQLException {
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
        public AmbitoConfigDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            AmbitoConfigDTO bean = new AmbitoConfigDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, AmbitoConfigDTO bean) throws SQLException {
            bean.setIdAmbitoConfig(rs.getLong("id_ambito_config"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setChiave(rs.getString("chiave"));
            bean.setValore(rs.getString("valore"));
//            bean.setOrdinamento(rs.getLong("ordinamento"));
            bean.setFlgAttivo(rs.getString("flg_attivo"));
            bean.setNote(rs.getString("note"));
            
        }
    }

	@Override
	public RowMapper<AmbitoConfigDTO> getExtendedRowMapper() throws SQLException {
		return new AmbitiConfigExtendedRowMapper();
	}
	
    
    /**
     * The type Ambiti config row mapper.
     */
    public static class AmbitiConfigExtendedRowMapper implements RowMapper<AmbitoConfigDTO> {

        /**
         * Instantiates a new Ambiti config row mapper.
         *
         * @throws SQLException the sql exception
         */
        public AmbitiConfigExtendedRowMapper() throws SQLException {
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
        public AmbitoConfigDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            AmbitoConfigDTO bean = new AmbitoConfigDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, AmbitoConfigDTO bean) throws SQLException {
            bean.setIdAmbitoConfig(rs.getLong("id_ambito_config"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setChiave(rs.getString("chiave"));
            bean.setValore(rs.getString("valore"));
//            bean.setOrdinamento(rs.getLong("ordinamento"));
            bean.setFlgAttivo(rs.getString("flg_attivo"));
            bean.setNote(rs.getString("note"));        
        }
    }

	@Override
	public String getTableNameAString() {
		return null;
	}

    
}
