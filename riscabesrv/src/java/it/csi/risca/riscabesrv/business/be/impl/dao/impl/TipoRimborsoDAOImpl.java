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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRimborsoDAO;

import it.csi.risca.riscabesrv.dto.TipoRimborsoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipo Rimborso dao.
 *
 * @author CSI PIEMONTE
 */
public class TipoRimborsoDAOImpl extends RiscaBeSrvGenericDAO<TipoRimborsoDTO> implements TipoRimborsoDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	public static final String QUERY_SELECT_ALL_STATI_DEBITORI = "select rdtr.* from RISCA_D_TIPO_RIMBORSO rdtr ";

	public static final String QUERY_SELECT_TIPO_RIMBORSO_BY_ID = "select rdtr.* from RISCA_D_TIPO_RIMBORSO rdtr"
			+ " WHERE rdtr.id_tipo_rimborso = :idTipoRimborso ";
	
	@Override
	public List<TipoRimborsoDTO> loadTipiRimborso(SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DAOException {
		LOGGER.debug("[TipoRimborsoDAOImpl::loadTipiRimborso] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<TipoRimborsoDTO> res = new ArrayList<TipoRimborsoDTO>();
		try {

			res = findListByQuery(CLASSNAME, methodName, QUERY_SELECT_ALL_STATI_DEBITORI, null);

		} catch (Exception e) {
			LOGGER.error("[TipoRimborsoDAOImpl::loadTipiRimborso] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[TipoRimborsoDAOImpl::loadTipiRimborso] END");
		}

		return res;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowMapper<TipoRimborsoDTO> getRowMapper() throws SQLException {
		return new TipoRimborsoRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<TipoRimborsoDTO> getExtendedRowMapper() throws SQLException {
		return new TipoRimborsoRowMapper();
	}

	/**
	 * The type Tipo Rimborso row mapper.
	 */
	public static class TipoRimborsoRowMapper implements RowMapper<TipoRimborsoDTO> {
		
		/**
		 * Instantiates a new Tipo Rimborso row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public TipoRimborsoRowMapper() throws SQLException {
		}

		/**
		 * Implementations must implement this method to map each row of data in the
		 * ResultSet. This method should not call {@code next()} on the ResultSet; it is
		 * only supposed to map values of the current row.
		 *
		 * @param rs     the ResultSet to map (pre-initialized for the current row)
		 * @param rowNum the number of the current row
		 * @return the result object for the current row (may be {@code null})
		 * @throws SQLException if a SQLException is encountered getting column values
		 *                      (that is, there's no need to catch SQLException)
		 */
		@Override
		public TipoRimborsoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TipoRimborsoDTO bean = new TipoRimborsoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, TipoRimborsoDTO bean) throws SQLException {
			bean.setIdTipoRimborso(rs.getLong("id_tipo_rimborso"));
			bean.setCodTipoRimborso(rs.getString("cod_tipo_rimborso"));
			bean.setDesTipoRimborso(rs.getString("des_tipo_rimborso"));

		}

	}

	@Override
	public TipoRimborsoDTO loadTipiRimborsoById(Long idTipoRimborso, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws DAOException {
		LOGGER.debug("[TipoRimborsoDAOImpl::loadTipiRimborsoById] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        TipoRimborsoDTO res = new TipoRimborsoDTO();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idTipoRimborso", idTipoRimborso);
			MapSqlParameterSource params = getParameterValue(map);

			res = template.queryForObject(getQuery(QUERY_SELECT_TIPO_RIMBORSO_BY_ID, null, null),
					params, getRowMapper());;

		} catch (Exception e) {
			LOGGER.error("[TipoRimborsoDAOImpl::loadTipiRimborsoById] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[TipoRimborsoDAOImpl::loadTipiRimborsoById] END");
		}

		return res;
	}


	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}