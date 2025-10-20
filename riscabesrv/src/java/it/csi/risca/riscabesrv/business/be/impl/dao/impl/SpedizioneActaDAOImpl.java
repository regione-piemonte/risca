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
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SpedizioneActaDAO;
import it.csi.risca.riscabesrv.dto.SpedizioneActaDTO;

/**
 * The type ParametroElabora dao.
 *
 * @author CSI PIEMONTE
 */
public class SpedizioneActaDAOImpl extends RiscaBeSrvGenericDAO<SpedizioneActaDTO> implements SpedizioneActaDAO {

	private static final String QUERY_INSERT_SPEDIZIONE_ACTA = "INSERT INTO risca_r_spedizione_acta "
			+ "(id_spedizione_acta, id_spedizione, flg_archiviata_acta, nome_dirigente_protempore, "
			+ " gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ " VALUES(:idSpedizioneActa, :idSpedizione, :flgArchiviataActa, :nomeDirigenteProtempore, "
			+ " :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";

	private static final String QUERY_UPDATE_SPEDIZIONE_ACTA = "UPDATE risca_r_spedizione_acta "
			+ "SET id_spedizione = :idSpedizione, flg_archiviata_acta = :flgArchiviataActa, nome_dirigente_protempore = :nomeDirigenteProtempore,  "
			+ "gest_data_upd = :gestDataUpd, gest_attore_upd=:gestAttoreUpd,  "
			+ "gest_uid=:gestUid, id_elabora = :idElabora WHERE id_spedizione_acta = :idSpedizioneActa";

	private static final String QUERY_LOAD_SPEDIZIONI_DA_ARCHIVIARE = "select rdts.cod_tipo_spedizione, rts.id_tipo_spedizione, rrsa.* from risca_r_spedizione_acta rrsa "
			+ " left join risca_t_elabora rte on rte.id_elabora = rrsa.id_elabora "
			+ " left join risca_d_stato_elabora rdte on rdte.id_stato_elabora = rte.id_stato_elabora "
			+ " left join risca_t_spedizione rts on rts.id_spedizione = rrsa.id_spedizione  "
			+ " left join risca_d_tipo_spedizione rdts on rdts.id_tipo_spedizione  = rts.id_tipo_spedizione "
			+ " where rrsa.id_elabora is null or rdte.cod_stato_elabora = 'INVACTA_KO' ";

	private static final String QUERY_LOAD_SPEDIZIONE_BY_ID = "select rdts.cod_tipo_spedizione, rts.id_tipo_spedizione, rrsa.* from risca_r_spedizione_acta rrsa "
			+ "left join risca_t_spedizione rts on rts.id_spedizione = rrsa.id_spedizione "
			+ "left join risca_d_tipo_spedizione rdts on rdts.id_tipo_spedizione  = rts.id_tipo_spedizione "
			+ "where rrsa .id_spedizione_acta = :idSpedizioneActa ";

	@Override
	public SpedizioneActaDTO saveSpedizioneActa(SpedizioneActaDTO dto) {
		LOGGER.debug("[SpedizioneActaDAOImpl::saveSpedizioneActa] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_r_spedizione_acta");

			map.put("idSpedizioneActa", genId);
			map.put("idSpedizione", dto.getIdSpedizione());
			map.put("flgArchiviataActa", dto.getFlgArchiviataActa());
			map.put("nomeDirigenteProtempore", dto.getNomeDirigenteProtempore());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT_SPEDIZIONE_ACTA, null, null), params, keyHolder);

			dto.setIdSpedizioneActa(genId);
		} catch (Exception e) {
			LOGGER.error("[SpedizioneActaDAOImpl::saveSpedizioneActa] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[SpedizioneActaDAOImpl::saveSpedizioneActa] END");
		}

		return dto;
	}

	@Override
	public SpedizioneActaDTO updateSpedizioneActa(SpedizioneActaDTO dto) {
		LOGGER.debug("[SpedizioneActaDAOImpl::updateSpedizioneActa] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();

			map.put("idSpedizioneActa", dto.getIdSpedizioneActa());
			map.put("idSpedizione", dto.getIdSpedizione());
			map.put("flgArchiviataActa", dto.getFlgArchiviataActa());
			map.put("nomeDirigenteProtempore", dto.getNomeDirigenteProtempore());
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			map.put("idElabora", dto.getIdElabora());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_UPDATE_SPEDIZIONE_ACTA, null, null), params, keyHolder);
			
			dto.setGestDataUpd(now);
			
		} catch (Exception e) {
			LOGGER.error("[SpedizioneActaDAOImpl::updateSpedizioneActa] ERROR : " + e.getMessage());
		} finally {
			LOGGER.debug("[SpedizioneActaDAOImpl::updateSpedizioneActa] END");
		}
		
		return dto;
	}

	@Override
	public List<SpedizioneActaDTO> loadSpedizioniActaDaArchiviare() {
		LOGGER.debug("[SpedizioneActaDAOImpl::loadSpedizioniActaDaArchiviare] BEGIN");
		try {
			String query = QUERY_LOAD_SPEDIZIONI_DA_ARCHIVIARE;
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(query, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[SpedizioneActaDAOImpl::loadSpedizioniActaDaArchiviare] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[SpedizioneActaDAOImpl::loadSpedizioniActaDaArchiviare] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[SpedizioneActaDAOImpl::loadSpedizioniActaDaArchiviare] END");
		}
	}

	@Override
	public SpedizioneActaDTO loadSpedizioneActaById(Long idSpedizioneActa) {
		LOGGER.debug("[SpedizioneActaDAOImpl::loadSpedizioneActaById] BEGIN");
		try {
			String query = QUERY_LOAD_SPEDIZIONE_BY_ID;
			Map<String, Object> map = new HashMap<>();
			map.put("idSpedizioneActa", idSpedizioneActa);
			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(getQuery(query, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[SpedizioneActaDAOImpl::loadSpedizioneActaById] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[SpedizioneActaDAOImpl::loadSpedizioneActaById] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[SpedizioneActaDAOImpl::loadSpedizioneActaById] END");
		}
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
	public RowMapper<SpedizioneActaDTO> getRowMapper() throws SQLException {
		return new SpedizioneActaRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SpedizioneActaDTO> getExtendedRowMapper() throws SQLException {
		return new SpedizioneActaRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class SpedizioneActaRowMapper implements RowMapper<SpedizioneActaDTO> {

		/**
		 * Instantiates a new SpedizioneActa row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SpedizioneActaRowMapper() throws SQLException {
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
		public SpedizioneActaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SpedizioneActaDTO bean = new SpedizioneActaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SpedizioneActaDTO bean) throws SQLException {
			bean.setIdSpedizioneActa(rs.getLong("id_spedizione_acta"));
			bean.setIdSpedizione(rs.getLong("id_spedizione"));
			bean.setIdElabora(getResultSetLong(rs, "id_elabora"));
			bean.setFlgArchiviataActa(rs.getInt("flg_archiviata_acta"));
			bean.setNomeDirigenteProtempore(rs.getString("nome_dirigente_protempore"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			
			if(rsHasColumn(rs, "id_tipo_spedizione"))
				bean.setIdTipoSpedizione(rs.getLong("id_tipo_spedizione"));
			if(rsHasColumn(rs, "cod_tipo_spedizione"))
				bean.setCodTipoSpedizione(rs.getString("cod_tipo_spedizione"));
		}
		
		private Long getResultSetLong(ResultSet resultset, String colName) throws SQLException {
			long v = resultset.getLong(colName);
			if (resultset.wasNull()) {
				return null;
			}
			return Long.valueOf(v);
		}
		
		private boolean rsHasColumn(ResultSet rs, String column) {
			try {
				rs.findColumn(column);
				return true;
			} catch (SQLException sqlex) {
				// Column not present in resultset
			}
			return false;
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}