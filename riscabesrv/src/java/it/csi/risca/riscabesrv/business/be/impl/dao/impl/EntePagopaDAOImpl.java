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
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.EntePagopaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.EntePagopaDTO;

/**
 * The type ambiti dao impl.
 *
 * @author CSI PIEMONTE
 */
public class EntePagopaDAOImpl extends RiscaBeSrvGenericDAO<EntePagopaDTO> implements EntePagopaDAO {

	private static final String QUERY_ENTE_PAGOPA_BY_ID_AMBITO = "select * from risca_d_ente_pagopa "
			+ " where id_ambito = :idAmbito "
			+ " and flg_richiesta_iuv = 1 ";
	
	private static final String QUERY_ENTE_PAGOPA_BY_COD_VERS_ENTE = "select * from risca_d_ente_pagopa "
			+ " where cod_versamento = :codiceVersamento "
			+ " and cf_ente_creditore = :cfEnteCreditore ";
	
	private static final String COND_FLG_RICHIESTA_IUV = " and flg_richiesta_iuv = 1 ";

	@Override
	public EntePagopaDTO loadEntePagopaPerIuvByAmbito(Long idAmbito) {
		LOGGER.debug("[EntePagopaDAOImpl::loadEntePagopaPerIuvByAmbito] BEGIN");
		Map<String, Object> map = new HashMap<>();
		EntePagopaDTO entePagopaDTO = null;
		try {
			map.put("idAmbito", idAmbito);
			MapSqlParameterSource params = getParameterValue(map);
			entePagopaDTO = template.queryForObject(QUERY_ENTE_PAGOPA_BY_ID_AMBITO, params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("[EntePagopaDAOImpl::loadEntePagopaPerIuvByAmbito] Errore generale ", e);
			LOGGER.debug("[EntePagopaDAOImpl::loadEntePagopaPerIuvByAmbito] END");
			return null;
		}
		LOGGER.debug("[AmbitiDAOImpl::loadEntePagopaPerIuvByAmbito] END");
		return entePagopaDTO;
	}
	
	@Override
	public EntePagopaDTO loadEntePagopaByCodVersamentoEnteCreditore(String codiceVersamento, String cfEnteCreditore, boolean checkIuv) {
		LOGGER.debug("[EntePagopaDAOImpl::loadEntePagopaByCodVersamentoEnteCreditore] BEGIN");
		Map<String, Object> map = new HashMap<>();
		EntePagopaDTO entePagopaDTO = null;
		try {
			map.put("codiceVersamento", codiceVersamento);
			map.put("cfEnteCreditore", cfEnteCreditore);
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_ENTE_PAGOPA_BY_COD_VERS_ENTE;
			if(checkIuv) {
				query += COND_FLG_RICHIESTA_IUV;
			}
			entePagopaDTO = template.queryForObject(query, params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("[EntePagopaDAOImpl::loadEntePagopaByCodVersamentoEnteCreditore] Errore generale ", e);
			LOGGER.debug("[EntePagopaDAOImpl::loadEntePagopaByCodVersamentoEnteCreditore] END");
			return null;
		}
		LOGGER.debug("[EntePagopaDAOImpl::loadEntePagopaByCodVersamentoEnteCreditore] END");
		return entePagopaDTO;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<EntePagopaDTO> getRowMapper() throws SQLException {
		return new EntePagopaRowMapper();
	}

	/**
	 * The type EntePagopa row mapper.
	 */
	public static class EntePagopaRowMapper implements RowMapper<EntePagopaDTO> {

		/**
		 * Instantiates a new EntePagopa row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public EntePagopaRowMapper() throws SQLException {
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
		public EntePagopaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EntePagopaDTO bean = new EntePagopaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, EntePagopaDTO bean) throws SQLException {
			bean.setIdEntePagopa(rs.getLong("id_ente_pagopa"));
			bean.setIdAmbito(rs.getLong("id_ambito"));
			bean.setCodEntePagopa(rs.getString("cod_ente_pagopa"));
			bean.setDesEntePagopa(rs.getString("des_ente_pagopa"));
			bean.setCodSettore(rs.getString("cod_settore"));
			bean.setCfEnteCreditore(rs.getString("cf_ente_creditore"));
			bean.setCodVersamento(rs.getString("cod_versamento"));
			bean.setFlgRichiestaIuv(rs.getInt("flg_richiesta_iuv"));
			bean.setCausale(rs.getString("causale"));		
		}
	}

	@Override
	public RowMapper<EntePagopaDTO> getExtendedRowMapper() throws SQLException {
		return new EntePagopaRowMapper();
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}
