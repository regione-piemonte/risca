/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.soris;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.Soris99CDAO;
import it.csi.risca.riscabesrv.dto.soris.Soris99CDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Soris99C dao.
 *
 * @author CSI PIEMONTE
 */
public class Soris99CDAOImpl  extends RiscaBeSrvGenericDAO<Soris99CDTO> implements Soris99CDAO {
	
	private static final String QUERY_INSERT = 		
			" insert into risca_w_soris_99C "+
			" (tipo_record, filler1,cod_dest_mitt,filler2,totale_record_invio,cod_tracciato, filler3 ) "+
			" VALUES (:tipoRecord, :filler1, :codDestMitt, :filler2, :totaleRecordInvio, :codTracciato, :filler3 ) ";
	
	private static final String QUERY_DELETE = " delete from risca_w_soris_99C ";
	
	@Override
	public Soris99CDTO saveSoris99C(Soris99CDTO dto) throws DAOException, DataAccessException, SQLException {
		LOGGER.debug("[Soris99CDAO::saveSoris99C] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("tipoRecord", dto.getTipoRecord());
			map.put("filler1", dto.getFiller1());
			map.put("codDestMitt", dto.getCodDestMitt());
			map.put("filler2", dto.getFiller2());
			map.put("totaleRecordInvio", dto.getTotaleRecordInvio());
			map.put("codTracciato", dto.getCodTracciato());
			map.put("filler3", dto.getFiller3());
			
			MapSqlParameterSource params = getParameterValue(map);
			
            template.update(getQuery(QUERY_INSERT, null, null), params);
		} catch (Exception e) {
			LOGGER.error("[Soris99CDAO::saveSoris99C] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[Soris99CDAO::saveSoris99C] END");
		}
		return dto;
	}

	
	public Integer delete() throws DAOException{
		LOGGER.debug("[Soris99CDAO::delete] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE, null, null), params, keyHolder);

		} finally {
			LOGGER.debug("[Soris99CDAO::delete] END");
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
	public RowMapper<Soris99CDTO> getRowMapper() throws SQLException {
		return new Soris99CRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<Soris99CDTO> getExtendedRowMapper() throws SQLException {
		return new Soris99CRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class Soris99CRowMapper implements RowMapper<Soris99CDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public Soris99CRowMapper() throws SQLException {
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
		public Soris99CDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			Soris99CDTO bean = new Soris99CDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, Soris99CDTO bean) throws SQLException {
			bean.setTipoRecord(rs.getString("tipo_record"));
			bean.setFiller1(rs.getString("filler1"));
			bean.setCodDestMitt(rs.getString("cod_dest_mitt"));
			bean.setFiller2(rs.getString("filler2"));
			bean.setTotaleRecordInvio(rs.getString("totale_record_invio"));
			bean.setCodTracciato(rs.getString("cod_tracciato"));
			bean.setFiller3(rs.getString("filler3"));
			}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Long decodeId(String fromTableName, String searchFieldCriteris) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Long findNextSequenceValue(String sequenceName) throws DataAccessException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}



}