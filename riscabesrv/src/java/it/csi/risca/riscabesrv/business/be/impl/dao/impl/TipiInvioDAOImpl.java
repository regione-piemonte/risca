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
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiInvioDAO;
import it.csi.risca.riscabesrv.dto.TipiInvioDTO;


/**
 * The type Tipo invio dao.
 *
 * @author CSI PIEMONTE
 */
public class TipiInvioDAOImpl extends RiscaBeSrvGenericDAO<TipiInvioDTO> implements TipiInvioDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
    
    private static final String QUERY_TIPI_INVIO = "SELECT rdti.* "
            + "FROM risca_d_tipo_invio rdti "
            + "ORDER BY rdti.ordina_tipo_invio";


	@Override
	public List<TipiInvioDTO> loadTipiInvio() {
		LOGGER.debug("[TipiInvioDAOImpl::loadTipiInvio] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<TipiInvioDTO> listTipiInvio = new ArrayList<TipiInvioDTO>();
        try {
			listTipiInvio = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_INVIO, null);
		} catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[TipiInvioDAOImpl::loadTipiInvio] No record found in database for Tipo Elabora ", e);
		  return Collections.emptyList();
		} catch (Exception e) {
			LOGGER.error("[TipiInvioDAOImpl::loadTipiInvio] ERROR : " +e);
			throw e;
		}
        LOGGER.debug("[TipiInvioDAOImpl::loadTipiInvio] END");
        return listTipiInvio;
	}


	@Override
	public String getPrimaryKeySelect() {

		return null;
	}

	@Override
	public RowMapper<TipiInvioDTO> getRowMapper() throws SQLException {
		 return new TipoInvioRowMapper();
	}

    /**
     * The type Tipo invio row mapper.
     */
    public static class TipoInvioRowMapper implements RowMapper<TipiInvioDTO> {

        /**
         * Instantiates a new Tipo invio row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoInvioRowMapper() throws SQLException {
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
        public TipiInvioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiInvioDTO bean = new TipiInvioDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiInvioDTO bean) throws SQLException {
            bean.setIdTipoInvio(rs.getLong("id_tipo_invio"));
            bean.setCodTipoInvio(rs.getString("cod_tipo_invio"));
            bean.setDesTipoInvio(rs.getString("des_tipo_invio"));
            bean.setOrdinaTipoInvio(rs.getLong("ordina_tipo_invio"));
        }
    }

	@Override
	public RowMapper<TipiInvioDTO> getExtendedRowMapper() throws SQLException {

		return new TipiInvioExtendedRowMapper();
	}
	
	
    /**
     * The type Tipo invio extended row mapper.
     */
    public static class TipiInvioExtendedRowMapper implements RowMapper<TipiInvioDTO> {

        /**
         * Instantiates a new Tipo invio extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiInvioExtendedRowMapper() throws SQLException {
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
        public TipiInvioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiInvioDTO bean = new TipiInvioDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiInvioDTO bean) throws SQLException {
            bean.setIdTipoInvio(rs.getLong("id_tipo_invio"));
            bean.setCodTipoInvio(rs.getString("cod_tipo_invio"));
            bean.setDesTipoInvio(rs.getString("des_tipo_invio"));
            bean.setOrdinaTipoInvio(rs.getLong("ordina_tipo_invio"));
        }
    }


}
