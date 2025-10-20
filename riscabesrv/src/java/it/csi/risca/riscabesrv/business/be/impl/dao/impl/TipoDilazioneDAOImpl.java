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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoDilazioneDAO;
import it.csi.risca.riscabesrv.dto.TipoDilazioneDTO;

/**
 * The type Tipo dilazione dao.
 *
 * @author CSI PIEMONTE
 */
public class TipoDilazioneDAOImpl extends RiscaBeSrvGenericDAO<TipoDilazioneDTO> implements TipoDilazioneDAO {


	private static final String QUERY_TIPO_DILAZIONE_BY_SCAD_PAG = " select id_tipo_dilazione, data_inizio_val, "
			+ " data_fine_val, COALESCE(num_annualita_magg, 0) num_annualita_magg, COALESCE(importo_magg, 0) importo_magg, " 
			+ " COALESCE(importo_min, 0) importo_min,  num_mesi, num_rate, id_ambito "
			+ " from risca_d_tipo_dilazione "
			+ " where TO_DATE(:dataScadenzaPagamento) >= data_inizio_val "
			+ " and ( data_fine_val is null or data_fine_val >= TO_DATE(:dataScadenzaPagamento) )"
			+ " and id_ambito = :idAmbito ";

	
	private static final String QUERY_TIPO_DILAZIONE_BY_ID_AMBITO = "select rdtd.* from RISCA_D_TIPO_DILAZIONE rdtd "
			+ "where rdtd.id_ambito = :idAmbito ";

	private static final String QUERY_TIPO_DILAZIONE_BY_ID_TIPO_DILAZIONE_AND_ID_AMBITO = "select rdtd.* from RISCA_D_TIPO_DILAZIONE rdtd "
			+ "inner join risca_t_stato_debitorio rtsd on rdtd.id_tipo_dilazione = rtsd.id_tipo_dilazione "  
			+ "where rdtd.id_ambito = :idAmbito "
			+ "and rdtd.id_tipo_dilazione = :idTipoDilazione";
	
    private static final String DATA_INIZIO_E_DATA_FINE_VALIDITA = "AND rdtd.data_inizio_val <= CURRENT_DATE "
    		+ "AND  rdtd.data_fine_val >= CURRENT_DATE ";
	@Override
	public TipoDilazioneDTO loadTipoDilazioneByDataScadenzaPagamento(String dataScadenzaPagamento, Long idAmbito) {
		LOGGER.debug("[TipoDilazioneDAOImpl::loadTipoDilazioneByDataScadenzaPagamento] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("dataScadenzaPagamento", dataScadenzaPagamento);
			map.put("idAmbito", idAmbito);

			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(QUERY_TIPO_DILAZIONE_BY_SCAD_PAG, params, getRowMapper());

		} catch (SQLException e) {
			LOGGER.error(
					"[TipoDilazioneDAOImpl::loadTipoDilazioneByDataScadenzaPagamento] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[TipoDilazioneDAOImpl::loadTipoDilazioneByDataScadenzaPagamento] Data not found for dataScadenzaPagamento: "+ dataScadenzaPagamento);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[TipoDilazioneDAOImpl::loadTipoDilazioneByDataScadenzaPagamento] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[TipoDilazioneDAOImpl::loadTipoDilazioneByDataScadenzaPagamento] END");
		}
	}
	
	@Override
	public TipoDilazioneDTO loadTipoDilazioneByIdAmbito(Long idAmbito) {
		LOGGER.debug("[TipoDilazioneDAOImpl::loadTipoDilazioneByIdAmbito] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);

			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(QUERY_TIPO_DILAZIONE_BY_ID_AMBITO + DATA_INIZIO_E_DATA_FINE_VALIDITA, params, getRowMapper());

		} catch (SQLException e) {
			LOGGER.error(
					"[TipoDilazioneDAOImpl::loadTipoDilazioneByIdAmbito] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[TipoDilazioneDAOImpl::loadTipoDilazioneByIdAmbito] Data not found for dataScadenzaPagamento: "+ idAmbito);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[TipoDilazioneDAOImpl::loadTipoDilazioneByIdAmbito] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[TipoDilazioneDAOImpl::loadTipoDilazioneByIdAmbito] END");
		}
	}
	
	@Override
	public List<TipoDilazioneDTO> loadTipoDilazioneByIdAmbitoAndIdTipoDilazione(Long idAmbito, Long idTipoDilazione) throws SQLException {
		LOGGER.debug("[TipoDilazioneDAOImpl::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			if(idTipoDilazione != null) {
				map.put("idTipoDilazione", idTipoDilazione);
	
				MapSqlParameterSource params = getParameterValue(map);
				return template.query(QUERY_TIPO_DILAZIONE_BY_ID_TIPO_DILAZIONE_AND_ID_AMBITO, params, getRowMapper());
			}
			else {
				MapSqlParameterSource params = getParameterValue(map);
				return template.query(QUERY_TIPO_DILAZIONE_BY_ID_AMBITO, params, getRowMapper());
			}

		} catch (SQLException e) {
			LOGGER.error(
					"[TipoDilazioneDAOImpl::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] Errore nell'esecuzione della query",
					e);
           throw e;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[TipoDilazioneDAOImpl::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] Data not found for dataScadenzaPagamento: "+ idAmbito);
			return Collections.emptyList();
		} catch (DataAccessException e) {
			LOGGER.error("[TipoDilazioneDAOImpl::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] Errore nell'accesso ai dati",
					e);
			 throw e;
		} finally {
			LOGGER.debug("[TipoDilazioneDAOImpl::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<TipoDilazioneDTO> getRowMapper() throws SQLException {
		return new TipoDilazioneRowMapper();
	}

	@Override
	public RowMapper<TipoDilazioneDTO> getExtendedRowMapper() throws SQLException {
		return new TipoDilazioneRowMapper();
	}

	/**
	 * The type Tipo dilazione row mapper.
	 */
	public static class TipoDilazioneRowMapper implements RowMapper<TipoDilazioneDTO> {

		/**
		 * Instantiates a new Tipo adempimento row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public TipoDilazioneRowMapper() throws SQLException {
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
		public TipoDilazioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TipoDilazioneDTO bean = new TipoDilazioneDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, TipoDilazioneDTO bean) throws SQLException {
			bean.setIdTipoDilazione(rs.getLong("id_tipo_dilazione"));
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			if(rs.getDate("data_inizio_val") != null) {
				bean.setDataInizioValidita(form.format(rs.getDate("data_inizio_val")));
			}
			if(rs.getDate("data_fine_val") != null) {
				bean.setDataFineValidita(form.format(rs.getDate("data_fine_val")));
			}	
			bean.setNumAnnualitaMagg(rs.getInt("num_annualita_magg"));
			bean.setImportoMagg(rs.getBigDecimal("importo_magg"));
			bean.setImportoMin(rs.getBigDecimal("importo_min"));
			bean.setNumMesi(rs.getInt("num_mesi"));
			bean.setNumRate(rs.getInt("num_rate"));
			bean.setIdAmbito(rs.getLong("id_ambito"));
		}
	}

}
