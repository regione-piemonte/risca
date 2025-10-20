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
import it.csi.risca.riscabesrv.business.be.impl.dao.SpedizioneDAO;
import it.csi.risca.riscabesrv.dto.SpedizioneDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type ParametroElabora dao.
 *
 * @author CSI PIEMONTE
 */
public class SpedizioneDAOImpl extends RiscaBeSrvGenericDAO<SpedizioneDTO> implements SpedizioneDAO {

	private static final String QUERY_LOAD_SPEDIZIONE_BY_PK = "SELECT * FROM risca_t_spedizione rts "
			+ "INNER JOIN risca_d_tipo_spedizione rdts ON rdts.id_tipo_spedizione = rts.id_tipo_spedizione "
			+ "WHERE id_spedizione = :idSpedizione ";

	private static final String QUERY_LOAD_SPEDIZIONE_WORKING_BY_PK = "SELECT * FROM risca_w_spedizione rws "
			+ "INNER JOIN risca_d_tipo_spedizione rdts ON rdts.id_tipo_spedizione = rws.id_tipo_spedizione "
			+ "WHERE id_spedizione = :idSpedizione ";

	private static final String QUERY_LOAD_SPEDIZIONE_WORKING_BY_ELAB = "SELECT * FROM risca_w_spedizione rws "
			+ "INNER JOIN risca_d_tipo_spedizione rdts ON rdts.id_tipo_spedizione = rws.id_tipo_spedizione "
			+ "WHERE id_ambito  = :idAmbito " + "and id_elabora = :idElabora "
			+ "and rdts.cod_tipo_spedizione =:codTipoSpedizione " + " order by id_spedizione desc ";

	private static final String QUERY_LOAD_SPEDIZIONE_BY_ELAB = "SELECT * FROM risca_t_spedizione rts "
			+ "INNER JOIN risca_d_tipo_spedizione rdts ON rdts.id_tipo_spedizione = rts.id_tipo_spedizione "
			+ "WHERE id_ambito  = :idAmbito " + "and rdts.cod_tipo_spedizione =:codTipoSpedizione "
			+ " order by id_spedizione desc ";

	private static final String QUERY_INSERT_SPEDIZIONE = "INSERT INTO risca_t_spedizione(id_spedizione, "
			+ "id_ambito, id_tipo_spedizione, data_spedizione, data_protocollo, "
			+ "num_protocollo, data_scadenza, num_determina, data_determina,anno, gest_attore_ins,gest_attore_upd, gest_data_ins, gest_data_upd, gest_uid, id_elabora) "
			+ "SELECT :idSpedizione, :idAmbito, id_tipo_spedizione, :dataSpedizione, "
			+ ":dataProtocollo, :numProtocollo, :dataScadenza, :numDetermina, :dataDetermina , :anno, :gestAttoreIns, :gestAttoreUpd, :gestDataIns, :gestDataUpd, :gestUid, :idElabora "
			+ "FROM risca_d_tipo_spedizione ts " + "WHERE ts.cod_tipo_spedizione  = :codTipoSpedizione";

	private static final String QUERY_INSERT_SPEDIZIONE_WORKING = "INSERT INTO risca_w_spedizione(id_spedizione, "
			+ "id_ambito, id_tipo_spedizione, data_spedizione, data_protocollo, "
			+ "num_protocollo, data_scadenza, num_determina, data_determina,anno, id_elabora) "
			+ "SELECT :idSpedizione, :idAmbito, id_tipo_spedizione, :dataSpedizione, "
			+ ":dataProtocollo, :numProtocollo, :dataScadenza, :numDetermina, :dataDetermina , :anno, :idElabora "
			+ "FROM risca_d_tipo_spedizione ts " + "WHERE ts.cod_tipo_spedizione  = :codTipoSpedizione";

	private static final String QUERY_UPDATE_SPEDIZIONE = "UPDATE risca_t_spedizione SET id_ambito = :idAmbito, "
			+ "id_tipo_spedizione = (SELECT id_tipo_spedizione FROM risca_d_tipo_spedizione "
			+ "WHERE cod_tipo_spedizione  = :codTipoSpedizione), data_spedizione = :dataSpedizione, "
			+ "data_protocollo = :dataProtocollo, num_protocollo = :numProtocollo, "
			+ "data_scadenza = :dataScadenza, num_determina = :numDetermina, data_determina = :dataDetermina, "
			+ "anno = :anno WHERE id_spedizione = :idSpedizione";

	private static final String QUERY_UPDATE_SPEDIZIONE_WORKING = "UPDATE risca_w_spedizione SET id_ambito = :idAmbito, "
			+ "id_tipo_spedizione = (SELECT id_tipo_spedizione FROM risca_d_tipo_spedizione "
			+ "WHERE cod_tipo_spedizione  = :codTipoSpedizione), data_spedizione = :dataSpedizione, "
			+ "data_protocollo = :dataProtocollo, num_protocollo = :numProtocollo, "
			+ "data_scadenza = :dataScadenza, num_determina = :numDetermina, data_determina = :dataDetermina, "
			+ "anno = :anno, id_elabora = :idElabora WHERE id_spedizione = :idSpedizione";

	private static final String COPY_SPEDIZIONE_FROM_WORKING = " INSERT INTO risca_t_spedizione "
			+ "(id_spedizione, id_ambito, id_tipo_spedizione, data_spedizione, data_protocollo, num_protocollo, data_scadenza, num_determina, data_determina, anno,  "
			+ "gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid, id_elabora) "
			+ "SELECT id_spedizione, id_ambito, id_tipo_spedizione, data_spedizione, data_protocollo, num_protocollo, data_scadenza, num_determina, data_determina, anno,  "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid, id_elabora " + "FROM risca_w_spedizione "
			+ "where id_spedizione = :idSpedizione ";

	private static final String QUERY_DELETE_SPEDIZIONE = " delete from risca_t_spedizione where id_spedizione = :idSpedizione ";

	private static final String QUERY_DELETE_SPEDIZIONE_WORKING = " delete from risca_w_spedizione where id_spedizione = :idSpedizione ";

	/**
	 * {@inheritDoc}
	 * @throws Exception 
	 */
	@Override
	public SpedizioneDTO loadSpedizioneByPk(String idSpedizione, boolean isWorking) throws Exception {
		LOGGER.debug("[SpedizioneDAOImpl::loadSpedizioneByPk] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idSpedizione", Long.parseLong(idSpedizione));
			MapSqlParameterSource params = getParameterValue(map);
			if (!isWorking) {
				return template.queryForObject(getQuery(QUERY_LOAD_SPEDIZIONE_BY_PK, null, null), params,
						getRowMapper());
			} else {
				return template.queryForObject(getQuery(QUERY_LOAD_SPEDIZIONE_WORKING_BY_PK, null, null), params,
						getRowMapper());
			}
		} catch (SQLException e) {
			LOGGER.error("[SpedizioneDAOImpl::loadSpedizioneByPk] Errore nell'esecuzione della query", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (DataAccessException e) {
			LOGGER.error("[SpedizioneDAOImpl::loadSpedizioneByPk] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SpedizioneDAOImpl::loadSpedizioneByPk] END");
		}
	}

	@Override
	public List<SpedizioneDTO> loadSpedizioneByElaborazione(String idAmbito, String idElabora, String codTipoSpedizione,
			boolean isWorking) throws Exception {
		LOGGER.debug("[SpedizioneDAOImpl::loadSpedizioneByElaborazione] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", Long.parseLong(idAmbito));
			map.put("codTipoSpedizione", codTipoSpedizione);
			map.put("codTipoSpedizione", codTipoSpedizione);
			if (isWorking) {
				map.put("idElabora", Long.parseLong(idElabora));
			}

			MapSqlParameterSource params = getParameterValue(map);
			if (!isWorking) {
				return template.query(getQuery(QUERY_LOAD_SPEDIZIONE_BY_ELAB, null, null), params, getRowMapper());
			} else {
				return template.query(getQuery(QUERY_LOAD_SPEDIZIONE_WORKING_BY_ELAB, null, null), params,
						getRowMapper());
			}
		} catch (SQLException e) {
			LOGGER.error("[SpedizioneDAOImpl::loadSpedizioneByElaborazione] Errore nell'esecuzione della query", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (DataAccessException e) {
			LOGGER.error("[SpedizioneDAOImpl::loadSpedizioneByElaborazione] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SpedizioneDAOImpl::loadSpedizioneByElaborazione] END");
		}
	}

	@Override
	public SpedizioneDTO saveSpedizione(SpedizioneDTO dto, boolean isWorking) throws Exception {
		LOGGER.debug("[SpedizioneDAOImpl::saveSpedizione] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_t_spedizione");

			map.put("idSpedizione", genId);
			map.put("idAmbito", dto.getIdAmbito());
			map.put("codTipoSpedizione", dto.getCodTipoSpedizione());
			map.put("dataSpedizione", dto.getDataSpedizione());
			map.put("dataProtocollo", dto.getDataProtocollo());
			map.put("numProtocollo", dto.getNumProtocollo());
			map.put("dataScadenza", dto.getDataScadenza());
			map.put("numDetermina", dto.getNumDetermina());
			map.put("dataDetermina", dto.getDataDetermina());
			map.put("anno", dto.getAnno());
			map.put("idElabora", dto.getIdElabora());
			
			if (!isWorking) {
				map.put("gestAttoreIns", dto.getGestAttoreIns());
				map.put("gestAttoreUpd", dto.getGestAttoreUpd());
				map.put("gestDataIns", now);
				map.put("gestDataUpd", now);
				map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			}

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			if (!isWorking) {
				template.update(getQuery(QUERY_INSERT_SPEDIZIONE, null, null), params, keyHolder);
			} else {
				template.update(getQuery(QUERY_INSERT_SPEDIZIONE_WORKING, null, null), params, keyHolder);
			}
			dto.setIdSpedizione(genId);
		} catch (Exception e) {
			LOGGER.error("[SpedizioneDAOImpl::saveSpedizione] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);

		} finally {
			LOGGER.debug("[SpedizioneDAOImpl::saveSpedizione] END");
		}

		return dto;
	}

	@Override
	public SpedizioneDTO updateSpedizione(SpedizioneDTO dto, boolean isWorking) throws Exception {
		LOGGER.debug("[SpedizioneDAOImpl::updateSpedizione] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idAmbito", dto.getIdAmbito());
			map.put("codTipoSpedizione", dto.getCodTipoSpedizione());
			map.put("dataSpedizione", dto.getDataSpedizione());
			map.put("dataProtocollo", dto.getDataProtocollo());
			map.put("numProtocollo", dto.getNumProtocollo());
			map.put("dataScadenza", dto.getDataScadenza());
			map.put("numDetermina", dto.getNumDetermina());
			map.put("dataDetermina", dto.getDataDetermina());
			map.put("anno", dto.getAnno());
			map.put("idSpedizione", dto.getIdSpedizione());
			map.put("idElabora", dto.getIdElabora());
			if (!isWorking) {
				map.put("gestAttoreUpd", dto.getGestAttoreUpd());
				map.put("gestDataUpd", now);
			}

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			if (!isWorking) {
				template.update(getQuery(QUERY_UPDATE_SPEDIZIONE, null, null), params, keyHolder);
			} else {
				template.update(getQuery(QUERY_UPDATE_SPEDIZIONE_WORKING, null, null), params, keyHolder);
			}
		} catch (Exception e) {
			LOGGER.error("[SpedizioneDAOImpl::updateSpedizione] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SpedizioneDAOImpl::updateSpedizione] END");
		}

		return dto;
	}

	@Override
	public Integer deleteSpedizioneByPk(Long idSpedizione, boolean isWorking) throws Exception {
		LOGGER.debug("[SpedizioneDAOImpl::deleteSpedizioneByPk] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idSpedizione", idSpedizione);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			if (!isWorking) {
				return template.update(getQuery(QUERY_DELETE_SPEDIZIONE, null, null), params, keyHolder);
			} else {
				return template.update(getQuery(QUERY_DELETE_SPEDIZIONE_WORKING, null, null), params, keyHolder);
			}
		} catch (Exception e) {
			LOGGER.error("[SpedizioneDAOImpl::deleteSpedizioneByPk] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SpedizioneDAOImpl::deleteSpedizioneByPk] END");
		}
	}

	@Override
	public Integer copySpedizioneFromWorking(Long idSpedizione, String attore) throws Exception {
		LOGGER.debug("[SpedizioneDAOImpl::copySpedizioneFromWorking] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idSpedizione", idSpedizione);
			map.put("gestAttoreIns", attore);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataIns", now);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(COPY_SPEDIZIONE_FROM_WORKING, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SpedizioneDAOImpl::copySpedizioneFromWorking] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SpedizioneDAOImpl::copySpedizioneFromWorking] END");
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
	public RowMapper<SpedizioneDTO> getRowMapper() throws SQLException {
		return new ParametroElaboraRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SpedizioneDTO> getExtendedRowMapper() throws SQLException {
		return new ParametroElaboraRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class ParametroElaboraRowMapper implements RowMapper<SpedizioneDTO> {

		/**
		 * Instantiates a new Elabora row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public ParametroElaboraRowMapper() throws SQLException {
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
		public SpedizioneDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SpedizioneDTO bean = new SpedizioneDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SpedizioneDTO bean) throws SQLException {
			bean.setIdSpedizione(rs.getLong("id_spedizione"));
			bean.setIdAmbito(rs.getLong("id_ambito"));
			bean.setCodTipoSpedizione(rs.getString("cod_tipo_spedizione"));
			bean.setDataSpedizione(rs.getDate("data_spedizione"));
			bean.setDataProtocollo(rs.getDate("data_protocollo"));
			bean.setNumProtocollo(rs.getString("num_protocollo"));
			bean.setDataScadenza(rs.getDate("data_scadenza"));
			bean.setNumDetermina(rs.getString("num_determina"));
			bean.setDataDetermina(rs.getDate("data_determina"));
			bean.setAnno(rs.getInt("anno"));
			bean.setIdElabora(rs.getLong("id_elabora"));
			
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
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