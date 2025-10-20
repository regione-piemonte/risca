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

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.UnitaMisuraDAO;
import it.csi.risca.riscabesrv.dto.DatoTecnicoDTO;
import it.csi.risca.riscabesrv.dto.UnitaMisuraDTO;

/**
 * The type Unita misura dao.
 *
 * @author CSI PIEMONTE
 */
public class UnitaMisuraDAOImpl extends RiscaBeSrvGenericDAO<UnitaMisuraDTO> implements UnitaMisuraDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();

	private static final String QUERY_UNITA_MISURA = "SELECT rdum.* " + "FROM risca_d_unita_misura rdum ";

	private static final String QUERY_DATO_TECNICO_BY_KEY_UNITA_MISURA = "SELECT rddt.* FROM risca_d_dato_tecnico rddt "
			+ "WHERE UPPER(rddt.cod_dato_tecnico) = UPPER(:keyUnitaMisura)";

	private static final String QUERY_UNITA_MISURA_BY_ID_UNITA_MISURA = QUERY_UNITA_MISURA
			+ "WHERE rdum.id_unita_misura = :idUnitaMisura ";

	private static final String QUERY_UNITA_MISURA_BY_SIGLA_UNITA_MISURA = QUERY_UNITA_MISURA
			+ "WHERE rdum.sigla_unita_misura = :siglaUnitaMisura ";

	private static final String QUERY_UNITA_MISURA_BY_DESC = " SELECT rdum.* "
			+ "FROM risca_d_unita_misura rdum where des_unita_misura = :desUnitaMisura ";

	@Override
	public List<UnitaMisuraDTO> loadUnitaMisura() {
		LOGGER.debug("[UnitaMisuraDAOImpl::loadUnitaMisura] BEGIN");
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug("[UnitaMisuraDAOImpl::loadUnitaMisura] END");
		return findListByQuery(CLASSNAME, methodName, QUERY_UNITA_MISURA, null);
	}

	@Override
	public List<UnitaMisuraDTO> loadUnitaMisuraByIdAmbito(Long idAmbito) {
		LOGGER.debug("[UnitaMisuraDAOImpl::loadUnitaMisuraByIdAmbito] BEGIN");
		List<UnitaMisuraDTO> unitaMisuraList = new ArrayList<UnitaMisuraDTO>();
		LOGGER.debug("[UnitaMisuraDAOImpl::loadUnitaMisuraByIdAmbito] END");
		return unitaMisuraList;
	}

	@Override
	public UnitaMisuraDTO loadUnitaMisuraByIdUnitaMisura(String idOrSiglaUnitaMisura) throws SQLException {
		LOGGER.debug("[UnitaMisuraDAOImpl::loadUnitaMisuraByIdUnitaMisura] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();

			Long idUnitaMisura = 0L;
			boolean isIdUnitaMisura = false;
			try {
				idUnitaMisura = Long.parseLong(idOrSiglaUnitaMisura);
				map.put("idUnitaMisura", idUnitaMisura);
				isIdUnitaMisura = true;
			} catch (NumberFormatException nfe) {
				isIdUnitaMisura = false;
				map.put("siglaUnitaMisura", idOrSiglaUnitaMisura);
			}
			MapSqlParameterSource params = getParameterValue(map);
			if (isIdUnitaMisura) {
				return template.queryForObject(QUERY_UNITA_MISURA_BY_ID_UNITA_MISURA, params, getRowMapper());
			} else {
				return template.queryForObject(QUERY_UNITA_MISURA_BY_SIGLA_UNITA_MISURA, params, getRowMapper());
			}

		} catch (SQLException e) {
			LOGGER.error("[UnitaMisuraDAOImpl::loadUnitaMisuraByIdUnitaMisura] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[UnitaMisuraDAOImpl::loadUnitaMisuraByIdUnitaMisura] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[UnitaMisuraDAOImpl::loadUnitaMisuraByIdUnitaMisura] END");
		}
	}

	@Override
	public UnitaMisuraDTO loadUnitaMisuraByDesc(String desUnitaMisura) throws SQLException {
		LOGGER.debug("[UnitaMisuraDAOImpl::loadUnitaMisuraByDesc] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("desUnitaMisura", desUnitaMisura);
			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(QUERY_UNITA_MISURA_BY_DESC, params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[UnitaMisuraDAOImpl::loadUnitaMisuraByDesc] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[UnitaMisuraDAOImpl::loadUnitaMisuraByDesc] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[UnitaMisuraDAOImpl::loadUnitaMisuraByDesc] END");
		}
	}

	@Override
	public UnitaMisuraDTO loadUnitaMisuraByKeyUnitaMisura(String keyUnitaMisura) throws SQLException {
		LOGGER.debug("[UnitaMisuraDAOImpl::loadUnitaMisuraByKeyUnitaMisura] BEGIN");
		UnitaMisuraDTO unitaMisura = new UnitaMisuraDTO();
		try {
			Map<String, Object> mapDT = new HashMap<>();
			mapDT.put("keyUnitaMisura", keyUnitaMisura);
			MapSqlParameterSource paramsDT = getParameterValue(mapDT);
			DatoTecnicoDTO datoTecnico;
			Long idUnitaMisura = null;
			datoTecnico = template.queryForObject(QUERY_DATO_TECNICO_BY_KEY_UNITA_MISURA, paramsDT, getRowMapperDT());
			Map<String, Object> mapUM = new HashMap<>();

			if (datoTecnico != null) {
				idUnitaMisura = datoTecnico.getIdUnitaMisura();
			}
			mapUM.put("idUnitaMisura", idUnitaMisura);
			MapSqlParameterSource paramsUM = getParameterValue(mapUM);
			unitaMisura = template.queryForObject(QUERY_UNITA_MISURA_BY_ID_UNITA_MISURA, paramsUM, getRowMapper());
			return unitaMisura;
		} catch (SQLException e) {
			LOGGER.error("[UnitaMisuraDAOImpl::loadUnitaMisuraByKeyUnitaMisura] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[UnitaMisuraDAOImpl::loadUnitaMisuraByKeyUnitaMisura] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[UnitaMisuraDAOImpl::loadUnitaMisuraByKeyUnitaMisura] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<UnitaMisuraDTO> getRowMapper() throws SQLException {
		return new UnitaMisuraRowMapper();
	}

	/**
	 * The type Unita misura row mapper.
	 */
	public static class UnitaMisuraRowMapper implements RowMapper<UnitaMisuraDTO> {

		/**
		 * Instantiates a new Unita misura row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public UnitaMisuraRowMapper() throws SQLException {
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
		public UnitaMisuraDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			UnitaMisuraDTO bean = new UnitaMisuraDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, UnitaMisuraDTO bean) throws SQLException {
			bean.setIdUnitaMisura(rs.getLong("id_unita_misura"));
			bean.setSiglaUnitaMisura(rs.getString("sigla_unita_misura"));
			bean.setDesUnitaMisura(rs.getString("des_unita_misura"));
			bean.setOrdinaUnitaMisura(rs.getLong("ordina_unita_misura"));

		}
	}

	@Override
	public RowMapper<UnitaMisuraDTO> getExtendedRowMapper() throws SQLException {
		return new UnitaMisuraExtendedRowMapper();
	}

	public RowMapper<DatoTecnicoDTO> getRowMapperDT() throws SQLException {
		return new DatoTecnicoRowMapper();
	}

	/**
	 * The type Dato tecnico row mapper.
	 */
	public static class DatoTecnicoRowMapper implements RowMapper<DatoTecnicoDTO> {

		/**
		 * Instantiates a new Dato tecnico row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public DatoTecnicoRowMapper() throws SQLException {
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
		public DatoTecnicoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DatoTecnicoDTO bean = new DatoTecnicoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, DatoTecnicoDTO bean) throws SQLException {
			bean.setIdDatoTecnico(rs.getLong("id_dato_tecnico"));
			bean.setCodDatoTecnico(rs.getString("cod_dato_tecnico"));
			bean.setDesDatoTecnico(rs.getString("des_dato_tecnico"));
			bean.setIdUnitaMisura(rs.getLong("id_unita_misura"));
			bean.setIdTipoDatoTecnico(rs.getLong("id_tipo_dato_tecnico"));
			bean.setFlgCalcolato(rs.getString("flg_calcolato"));

		}
	}

	/**
	 * The type Unita misura row mapper.
	 */
	public static class UnitaMisuraExtendedRowMapper implements RowMapper<UnitaMisuraDTO> {

		/**
		 * Instantiates a new Unita misura row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public UnitaMisuraExtendedRowMapper() throws SQLException {
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
		public UnitaMisuraDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			UnitaMisuraDTO bean = new UnitaMisuraDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, UnitaMisuraDTO bean) throws SQLException {
			bean.setIdUnitaMisura(rs.getLong("id_unita_misura"));
			bean.setSiglaUnitaMisura(rs.getString("sigla_unita_misura"));
			bean.setDesUnitaMisura(rs.getString("des_unita_misura"));
			bean.setOrdinaUnitaMisura(rs.getLong("ordina_unita_misura"));

		}
	}

}
