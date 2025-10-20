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

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.PingDAO;
import it.csi.risca.riscabesrv.util.Constants;


/**
 * The type Ping dao.
 *
 * @author CSI PIEMONTE
 */
public class PingDAOImpl implements PingDAO {

    private static final String QUERY_PING_DB_ON = "SELECT 1 AS ping";
    
    /**
     * The constant LOGGER.
     */
    protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME + ".dao");

    /**
     * The Template.
     */
    protected NamedParameterJdbcTemplate template;

    public void setTemplate(NamedParameterJdbcTemplate template) {
        this.template = template;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String pingDB() throws DAOException {
        try {
            LOGGER.debug("[PingDAOImpl::pingDB] BEGIN");
            MapSqlParameterSource params = new MapSqlParameterSource();
            return template.queryForObject(QUERY_PING_DB_ON, params, getRowMapper());
        } catch (DataAccessResourceFailureException e) {
            LOGGER.error("[PingDAOImpl::pingDB] service unavailable : unable to get DB connection. The DB may be down",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
        }
    }

    /**
     * {@inheritDoc}
     */
    public RowMapper<String> getRowMapper() {
        return new StringRowMapper();
    }


    /**
     * Specific inner class for 'RowMapper' implementation
     */
    public static class StringRowMapper implements RowMapper<String> {

        /**
         * Instantiates a new String row mapper.
         */
        public StringRowMapper() {
            // Instatiate class
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
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("ping");
        }
    }

}