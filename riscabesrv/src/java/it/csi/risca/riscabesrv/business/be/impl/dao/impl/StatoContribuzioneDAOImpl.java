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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoContribuzioneDAO;
import it.csi.risca.riscabesrv.dto.StatoContribuzioneDTO;

public class StatoContribuzioneDAOImpl extends RiscaBeSrvGenericDAO<StatoContribuzioneDTO> implements StatoContribuzioneDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();

	public static final String QUERY_SELECT_STATO_CONTRIBUZIONE_BY_ID = "SELECT * FROM risca_d_stato_contribuzione "
			+ "WHERE id_stato_contribuzione = :idStatoContribuzione  ";
	
	public static final String QUERY_SELECT_STATO_CONTRIBUZIONE = "SELECT * FROM risca_d_stato_contribuzione ";
	
	@Override
	public StatoContribuzioneDTO loadStatoContribuzioneById(Long idStatoContribuzione) throws Exception  {

		LOGGER.debug("[StatoContribuzioneDAOImpl::loadStatoContribuzioneById] BEGIN");
        Map<String, Object> map = new HashMap<>();
        StatoContribuzioneDTO statoContribuzioneDTO = null;
        try {
            map.put("idStatoContribuzione", idStatoContribuzione);
            MapSqlParameterSource params = getParameterValue(map);
            statoContribuzioneDTO=  template.queryForObject(QUERY_SELECT_STATO_CONTRIBUZIONE_BY_ID, params, getRowMapper());
		}catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[StatoContribuzioneDAOImpl::loadStatoContribuzioneById] Data not found for idStatoContribuzione: "+ idStatoContribuzione);
			return null;
		}  catch (Exception e) {
            LOGGER.error("[StatoContribuzioneDAOImpl::loadStatoContribuzioneById] Errore generale ", e);
            LOGGER.debug("[StatoContribuzioneDAOImpl::loadStatoContribuzioneById] END");
            throw e;
		}
        LOGGER.debug("[StatoContribuzioneDAOImpl::loadStatoContribuzioneById] END");
        return statoContribuzioneDTO;
        
	}
	
	@Override
	public List<StatoContribuzioneDTO> loadStatoContribuzione() {
		LOGGER.debug("[StatoContribuzioneDAOImpl::loadStatoContribuzione] BEGIN");
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<StatoContribuzioneDTO> listStatoContribuzione = new ArrayList<StatoContribuzioneDTO>();
        try {
        	listStatoContribuzione=  findListByQuery(CLASSNAME, methodName, QUERY_SELECT_STATO_CONTRIBUZIONE, null);
		}catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[StatoContribuzioneDAOImpl::loadStatoContribuzione] Data not found ");
			return Collections.emptyList();
		}  catch (Exception e) {
            LOGGER.error("[StatoContribuzioneDAOImpl::loadStatoContribuzione] Errore generale ", e);
            LOGGER.debug("[StatoContribuzioneDAOImpl::loadStatoContribuzione] END");
          throw e;
		}
        LOGGER.debug("[StatoContribuzioneDAOImpl::loadStatoContribuzione] END");
        return listStatoContribuzione;
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<StatoContribuzioneDTO> getRowMapper()
			throws SQLException {

		return new StatoContribuzioneRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return new StatoContribuzioneRowMapper();
	}

    /**
     * The type AttivitaStatoDebitorioRowMapper .
     */
    public static class StatoContribuzioneRowMapper implements RowMapper<StatoContribuzioneDTO> {

        /**
         * Instantiates a new StatoContribuzioneRowMapper .
         *
         * @throws SQLException the sql exception
         */
        public StatoContribuzioneRowMapper() throws SQLException {
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
        public StatoContribuzioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	StatoContribuzioneDTO bean = new StatoContribuzioneDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, StatoContribuzioneDTO bean) throws SQLException {
        
        	bean.setIdStatoContribuzione(rs.getLong("id_stato_contribuzione"));
            bean.setCodStatoContribuzione(rs.getString("cod_stato_contribuzione"));
            bean.setDesStatoContribuzione(rs.getString("des_stato_contribuzione"));
        }
    }



}
