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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TracciamentoDAO;
import it.csi.risca.riscabesrv.dto.TracciamentoDTO;

public class TracciamentoDAOImpl extends RiscaBeSrvGenericDAO<TracciamentoDTO> implements TracciamentoDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();

	private static final String QUERY_INSERT_TRACCIAMENTO = "INSERT INTO risca_s_tracciamento (id_tracciamento, data_ora, "
			+ "flg_operazione, id_riscossione, json_tracciamento, tipo_json) VALUES(nextval('seq_risca_s_tracciamento'), :dataOra, "
			+ ":flgOperazione, :idRiscossione, to_jsonb(:jsonTracciamento::json), :tipoJson) ";

	@Override
	public TracciamentoDTO saveTracciamento(TracciamentoDTO track) {
		LOGGER.debug("[TracciamentoDaoImpl::saveTracciamento] BEGIN");
		try {
				Map<String, Object> map = new HashMap<>();
				Calendar cal = Calendar.getInstance();
				Date now = cal.getTime();
		
				map.put("dataOra", now);
				map.put("flgOperazione", track.getFlgOperazione());
				map.put("idRiscossione", track.getIdRiscossione());
				map.put("jsonTracciamento", track.getJsonTracciamento());
				map.put("tipoJson", track.getTipoJson());

				MapSqlParameterSource params = getParameterValue(map);
				KeyHolder keyHolder = new GeneratedKeyHolder();
				template.update(getQuery(QUERY_INSERT_TRACCIAMENTO, null, null), params, keyHolder);


		} catch (Exception e) {
			LOGGER.error("[TracciamentoDaoImpl::saveTracciamento] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		    throw e;

		} finally {
			LOGGER.debug("[TracciamentoDaoImpl::saveTracciamento] END");
		}

		return track;
	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<TracciamentoDTO> getRowMapper() throws SQLException {
		
		return new TracciamentoRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new TracciamentoExtendedRowMapper();
	}
	
	
	public static class TracciamentoRowMapper implements RowMapper<TracciamentoDTO> {

	    public TracciamentoRowMapper() throws SQLException {
	         // Instantiate class
	     }
		@Override
		public TracciamentoDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			TracciamentoDTO bean = new TracciamentoDTO();
	         populateBean(rs, bean);
	         return bean;
		}
		private void populateBean(ResultSet rs, TracciamentoDTO bean) throws SQLException {
			bean.setDataOra(rs.getDate("data_ora"));
			bean.setFlgOperazione(rs.getString("flg_operazione"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setJsonTracciamento(rs.getString("json_tracciamento"));
			bean.setTipoJson(rs.getString("tipo_json"));
		}

	}

	public static class TracciamentoExtendedRowMapper implements RowMapper<TracciamentoDTO> {

	    public TracciamentoExtendedRowMapper() throws SQLException {
	         // Instantiate class
	     }
		@Override
		public TracciamentoDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			TracciamentoDTO bean = new TracciamentoDTO();
	         populateBean(rs, bean);
	         return bean;
		}
		private void populateBean(ResultSet rs, TracciamentoDTO bean) throws SQLException {
			bean.setDataOra(rs.getDate("data_ora"));
			bean.setFlgOperazione(rs.getString("flg_operazione"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setJsonTracciamento(rs.getString("json_tracciamento"));
			bean.setTipoJson(rs.getString("tipo_json"));
		}

	}
}
