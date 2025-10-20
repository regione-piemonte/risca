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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.ConfigurazioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.ConfigurazioneDTO;

/**
 * The type Configurazione dao.
 *
 * @author CSI PIEMONTE
 */
public class ConfigurazioneDAOImpl extends RiscaBeSrvGenericDAO<ConfigurazioneDTO> implements ConfigurazioneDAO {

    private static final String QUERY_LOAD_CONFIG = "SELECT * FROM risca_d_configurazione ";

    private static final String QUERY_LOAD_CONFIG_BY_KEY = QUERY_LOAD_CONFIG + "WHERE chiave = :key";

    private static final String QUERY_LOAD_CONFIG_BY_KEY_LIST = QUERY_LOAD_CONFIG + "WHERE TRIM(BOTH FROM chiave) IN (:keys)";

	private static final String QUERY_INSERT_CONFIGURAZIONE = "INSERT INTO risca_d_configurazione (chiave, valore, note) "
				+ "VALUES( :chiave, :valore, :note)";
	
	private static final String QUERY_UPDATE_CONFIGURAZIONE = "UPDATE risca_d_configurazione SET valore =:valore , note =:note "
	 		+ "WHERE chiave = :chiave;";
    /**
     * @return List<ConfigurazioneDTO>
     */
    @Override
    public List<ConfigurazioneDTO> loadConfig() {
        LOGGER.debug("[ConfigurazioneDAOImpl::loadConfig] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            MapSqlParameterSource params = getParameterValue(map);
            return template.query(getQuery(QUERY_LOAD_CONFIG, null, null), params, getRowMapper());
        } catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[ConfigurazioneDAOImpl::loadConfig]Data not found ");
			return Collections.emptyList();
		}  catch (SQLException e) {
            LOGGER.error("[ConfigurazioneDAOImpl::loadConfig] Errore nell'esecuzione della query", e);
            return null;
        } catch (DataAccessException e) {
            LOGGER.error("[ConfigurazioneDAOImpl::loadConfig] Errore nell'accesso ai dati", e);
            return null;
        } finally {
            LOGGER.debug("[ConfigurazioneDAOImpl::loadConfig] END");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConfigurazioneDTO> loadConfigByKey(String key) {
        LOGGER.debug("[ConfigurazioneDAOImpl::loadConfigByKey] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("key", key);
            MapSqlParameterSource params = getParameterValue(map);
            return template.query(getQuery(QUERY_LOAD_CONFIG_BY_KEY, null, null), params, getRowMapper());
        } catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[ConfigurazioneDAOImpl::loadConfigByKey]Data not found ");
			return Collections.emptyList();
		}  catch (SQLException e) {
            LOGGER.error("[ConfigurazioneDAOImpl::loadConfigByKey] Errore nell'esecuzione della query", e);
            return null;
        } catch (DataAccessException e) {
            LOGGER.error("[ConfigurazioneDAOImpl::loadConfigByKey] Errore nell'accesso ai dati", e);
            return null;
        } finally {
            LOGGER.debug("[ConfigurazioneDAOImpl::loadConfigByKey] END");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> loadConfigByKeyList(List<String> keys) {
        LOGGER.debug("[ConfigurazioneDAOImpl::loadConfigByKey] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("keys", keys);
            MapSqlParameterSource params = getParameterValue(map);
            List<ConfigurazioneDTO> res = template.query(getQuery(QUERY_LOAD_CONFIG_BY_KEY_LIST, null, null), params, getRowMapper());
            Map<String, String> configs = new HashMap<>();
            for (ConfigurazioneDTO dto : res) {
                configs.put(dto.getChiave(), dto.getValore());
            }
            return configs;
        } catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[ConfigurazioneDAOImpl::loadConfigByKey]Data not found ");
			return new HashMap<>();
		} catch (SQLException e) {
            LOGGER.error("[ConfigurazioneDAOImpl::loadConfigByKey] Errore nell'esecuzione della query", e);
            return null;
        } catch (DataAccessException e) {
            LOGGER.error("[ConfigurazioneDAOImpl::loadConfigByKey] Errore nell'accesso ai dati", e);
            return null;
        } finally {
            LOGGER.debug("[ConfigurazioneDAOImpl::loadConfigByKey] END");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrimaryKeySelect() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RowMapper<ConfigurazioneDTO> getRowMapper() throws SQLException {
        return new ConfigurazioneRowMapper();
    }

    /**
     * Returns a RowMapper for a new bean instance
     *
     * @return RowMapper<T>
     */
    @Override
    public RowMapper<ConfigurazioneDTO> getExtendedRowMapper() throws SQLException {
        return new ConfigurazioneRowMapper();
    }

    /**
     * The type Configurazione row mapper.
     */
    public static class ConfigurazioneRowMapper implements RowMapper<ConfigurazioneDTO> {

        /**
         * Instantiates a new Configurazione row mapper.
         *
         * @throws SQLException the sql exception
         */
        public ConfigurazioneRowMapper() throws SQLException {
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
        public ConfigurazioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConfigurazioneDTO bean = new ConfigurazioneDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, ConfigurazioneDTO bean) throws SQLException {
            bean.setChiave(rs.getString("chiave"));
            bean.setValore(rs.getString("valore"));
            bean.setNote(rs.getString("note"));
            bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
            bean.setGestDataIns(rs.getDate("gest_data_ins"));
            bean.setGestDataUpd(rs.getDate("gest_data_upd"));
            bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
            bean.setGestUid(rs.getString("gest_uid"));
        }
    }

	@Override
	public ConfigurazioneDTO insertConfigurazioneDTO(ConfigurazioneDTO configurazioneDTO) {

        LOGGER.debug("[ConfigurazioneDAOImpl::insertConfigurazioneDTO] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            
			map.put("chiave", configurazioneDTO.getChiave());
			map.put("valore", configurazioneDTO.getValore());
			map.put("note", configurazioneDTO.getNote());
            MapSqlParameterSource params = getParameterValue(map);

			template.update(QUERY_INSERT_CONFIGURAZIONE, params);
			
       } catch (DataAccessException e) {
            LOGGER.error("[ConfigurazioneDAOImpl::insertConfigurazioneDTO] Errore nell'accesso ai dati", e);
            return null;
        } finally {
            LOGGER.debug("[ConfigurazioneDAOImpl::insertConfigurazioneDTO] END");
        }
		return configurazioneDTO;
	}

	@Override
	public ConfigurazioneDTO updatetConfigurazioneDTO(ConfigurazioneDTO configurazioneDTO) {
        LOGGER.debug("[ConfigurazioneDAOImpl::updatetConfigurazioneDTO] BEGIN");
        try {
            Map<String, Object> map = new HashMap<>();
            
			map.put("chiave", configurazioneDTO.getChiave());
			map.put("valore", configurazioneDTO.getValore());
			map.put("note", configurazioneDTO.getNote());
            MapSqlParameterSource params = getParameterValue(map);

			template.update(QUERY_UPDATE_CONFIGURAZIONE, params);
			
       } catch (DataAccessException e) {
            LOGGER.error("[ConfigurazioneDAOImpl::updatetConfigurazioneDTO] Errore nell'accesso ai dati", e);
            return null;
        } finally {
            LOGGER.debug("[ConfigurazioneDAOImpl::updatetConfigurazioneDTO] END");
        }
		return configurazioneDTO;
	}

}