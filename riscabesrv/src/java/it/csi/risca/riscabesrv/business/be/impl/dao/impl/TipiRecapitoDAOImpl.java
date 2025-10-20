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
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiRecapitoDAO;
import it.csi.risca.riscabesrv.dto.TipiRecapitoDTO;


/**
 * The type Tipo recapito dao.
 *
 * @author CSI PIEMONTE
 */
public class TipiRecapitoDAOImpl extends RiscaBeSrvGenericDAO<TipiRecapitoDTO> implements TipiRecapitoDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
    
    private static final String QUERY_TIPI_RECAPITO = "SELECT rdtr.* "
            + "FROM risca_d_tipo_recapito rdtr ";


	@Override
	public List<TipiRecapitoDTO> loadTipiRecapito() {
		LOGGER.debug("[TipiRecapitoDAOImpl::loadTipiRecapito] BEGIN ");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<TipiRecapitoDTO> listTipiRecapito = new ArrayList<TipiRecapitoDTO>();
        try {
			listTipiRecapito = findListByQuery(CLASSNAME, methodName, QUERY_TIPI_RECAPITO, null);
		} catch (Exception e) {
			LOGGER.error("[TipiRecapitoDAOImpl::loadTipiRecapito] ERROR : " +e);
			throw e;
		}
        LOGGER.debug("[TipiRecapitoDAOImpl::loadTipiRecapito] END ");
        return listTipiRecapito;
	}


	@Override
	public String getPrimaryKeySelect() {

		return null;
	}

	@Override
	public RowMapper<TipiRecapitoDTO> getRowMapper() throws SQLException {
		 return new TipoRecapitoRowMapper();
	}

    /**
     * The type Tipo recapito row mapper.
     */
    public static class TipoRecapitoRowMapper implements RowMapper<TipiRecapitoDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoRecapitoRowMapper() throws SQLException {
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
        public TipiRecapitoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiRecapitoDTO bean = new TipiRecapitoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiRecapitoDTO bean) throws SQLException {
            bean.setIdTipoRecapito(rs.getLong("id_tipo_recapito"));
            bean.setCodTipoRecapito(rs.getString("cod_tipo_recapito"));
            bean.setDesTipoRecapito(rs.getString("des_tipo_recapito"));
        }
    }

	@Override
	public RowMapper<TipiRecapitoDTO> getExtendedRowMapper() throws SQLException {

		return new TipiRecapitoExtendedRowMapper();
	}
	
	
    /**
     * The type Tipo recapito extended row mapper.
     */
    public static class TipiRecapitoExtendedRowMapper implements RowMapper<TipiRecapitoDTO> {

        /**
         * Instantiates a new Tipo recapito extended row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipiRecapitoExtendedRowMapper() throws SQLException {
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
        public TipiRecapitoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TipiRecapitoDTO bean = new TipiRecapitoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipiRecapitoDTO bean) throws SQLException {
            bean.setIdTipoRecapito(rs.getLong("id_tipo_recapito"));
            bean.setCodTipoRecapito(rs.getString("cod_tipo_recapito"));
            bean.setDesTipoRecapito(rs.getString("des_tipo_recapito"));
        }
    }


}
