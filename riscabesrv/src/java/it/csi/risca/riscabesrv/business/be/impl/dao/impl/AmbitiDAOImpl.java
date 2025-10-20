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

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;

/**
 * The type ambiti dao impl.
 *
 * @author CSI PIEMONTE
 */
public class AmbitiDAOImpl extends RiscaBeSrvGenericDAO<AmbitoDTO> implements AmbitiDAO {

	private final String className = this.getClass().getSimpleName();

	private static final String QUERY_AMBITI = "SELECT * FROM risca_d_ambito ";

	private static final String QUERY_AMBITO_BY_ID_AMBITO = QUERY_AMBITI + "where id_ambito = :idAmbito";

	private static final String QUERY_AMBITI_BY_COD_TIPO_ELAB = " select rda.* from risca_d_ambito rda "
			+ " inner join risca_d_tipo_elabora rdte on rda.id_ambito = rdte.id_ambito "
			+ " where rdte.cod_tipo_elabora = :codTipoElabora ";

	@Override
	public List<AmbitoDTO> loadAmbiti() throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<AmbitoDTO> listAmbito = new ArrayList<AmbitoDTO>();
		try {
			listAmbito = findListByQuery(className, methodName, QUERY_AMBITI, null);
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e));
			throw e;

		}finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return listAmbito;
	}

	@Override
	public AmbitoDTO loadAmbitoByIdAmbito(Long idAmbito)  {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));	
		Map<String, Object> map = new HashMap<>();
		AmbitoDTO ambitoDTO = null;
		try {
			map.put("idAmbito", idAmbito);
			MapSqlParameterSource params = getParameterValue(map);
			ambitoDTO = template.queryForObject(QUERY_AMBITO_BY_ID_AMBITO, params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e));
			return null;

		}finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return ambitoDTO;
	}

	@Override
	public List<AmbitoDTO> loadAmbitiByCodTipoElabora(String codTipoElabora) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));	
		List<AmbitoDTO> listAmbito = new ArrayList<AmbitoDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codTipoElabora", codTipoElabora);
			MapSqlParameterSource params = getParameterValue(map);
			listAmbito = template.query(QUERY_AMBITI_BY_COD_TIPO_ELAB, params, getRowMapper());
		}  catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e));
			throw e;

		}finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return listAmbito;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<AmbitoDTO> getRowMapper() throws SQLException {
		return new AmbitiRowMapper();
	}

	/**
	 * The type Ambiti row mapper.
	 */
	public static class AmbitiRowMapper implements RowMapper<AmbitoDTO> {

		/**
		 * Instantiates a new Ambiti row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public AmbitiRowMapper() throws SQLException {
			// Instantiate class
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
		public AmbitoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AmbitoDTO bean = new AmbitoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AmbitoDTO bean) throws SQLException {
			bean.setIdAmbito(rs.getLong("id_ambito"));
			bean.setCodAmbito(rs.getString("cod_ambito"));
			bean.setDesAmbito(rs.getString("des_ambito"));
		}
	}

	@Override
	public RowMapper<AmbitoDTO> getExtendedRowMapper() throws SQLException {
		return new AmbitiRowMapper();
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}
