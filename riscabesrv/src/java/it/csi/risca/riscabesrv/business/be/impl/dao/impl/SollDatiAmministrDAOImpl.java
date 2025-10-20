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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SollDatiAmministrDAO;
import it.csi.risca.riscabesrv.dto.SollDatiAmministrDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type SollDatiAmministr dao.
 *
 * @author CSI PIEMONTE
 */
public class SollDatiAmministrDAOImpl extends RiscaBeSrvGenericDAO<SollDatiAmministrDTO>
		implements SollDatiAmministrDAO {

	public static final String QUERY_INSERT_W = " INSERT INTO risca_w_soll_dati_amministr "
			+ "(id_soll_dati_amministr, id_accertamento, codice_utente, codice_utenza, tipo_sollecito, "
			+ "tipo_titolo, num_titolo, data_titolo, corpo_idrico, comune_di_presa, annualita_pagamento, "
			+ "anno_rich_pagamento, scadenza_pagamento, motivo_soll, canone_dovuto, importo_versato, "
			+ "importo_mancante, interessi_mancanti, spese_notifica, interessi_teorici, interessi_versati, "
			+ "dilazione, codice_avviso) "
			+ "VALUES(:idSollDatiAmministr, :idAccertamento, :codiceUtente, :codiceUtenza, :tipoSollecito, "
			+ ":tipoTitolo, :numTitolo, :dataTitolo, :corpoIdrico, :comuneDiPresa, :annualitaPagamento, "
			+ ":annoRichPagamento, :scadenzaPagamento, :motivoSoll, :canoneDovuto, :importoVersato, "
			+ ":importoMancante, :interessiMancanti, :speseNotifica, :interessiTeorici, :interessiVersati, "
			+ ":dilazione, :codiceAvviso) ";

	public static final String UPDATE_CODICE_AVVISO_W_BY_ID_ACCERT = "update risca_w_soll_dati_amministr "
			+ "set codice_avviso = :codiceAvviso " + "where id_accertamento = :idAccertamento ";

	public static final String QUERY_DELETE_W_BY_ELABORA = " delete from risca_w_soll_dati_amministr "
			+ " where id_accertamento in "
			+ " (SELECT id_accertamento FROM RISCA_W_ACCERTAMENTO  WHERE id_elabora = :idElabora)";
	
	public static final String QUERY_DELETE_BY_ID_ACCERTAMENTO = "DELETE FROM RISCA_R_SOLL_DATI_AMMINISTR WHERE id_accertamento = :idAccertamento";


	public static final String QUERY_COPY_FROM_WORKING = "INSERT INTO RISCA_R_SOLL_DATI_AMMINISTR( "
			+ "id_soll_dati_amministr, id_accertamento, codice_utente, codice_utenza, "
			+ "tipo_sollecito, tipo_titolo, num_titolo, data_titolo, corpo_idrico, "
			+ "comune_di_presa, annualita_pagamento, " + "anno_rich_pagamento, scadenza_pagamento, "
			+ "motivo_soll, canone_dovuto, importo_versato, importo_mancante, "
			+ "interessi_mancanti, spese_notifica, interessi_teorici, interessi_versati, "
			+ "dilazione, codice_avviso,  "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd , gest_attore_upd , gest_uid) "
			+ "SELECT id_soll_dati_amministr, id_accertamento, codice_utente, codice_utenza, "
			+ "tipo_sollecito, tipo_titolo, num_titolo, data_titolo, corpo_idrico, "
			+ "comune_di_presa, annualita_pagamento, anno_rich_pagamento, scadenza_pagamento, "
			+ "motivo_soll, canone_dovuto, importo_versato, importo_mancante, "
			+ "interessi_mancanti, spese_notifica, interessi_teorici, interessi_versati, "
			+ "dilazione, codice_avviso, :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid "
			+ "FROM RISCA_W_SOLL_DATI_AMMINISTR a WHERE a.id_accertamento in ( "
			+ "	SELECT b.id_accertamento FROM RISCA_W_ACCERTAMENTO b WHERE b.id_elabora = :idElabora)";

	@Override
	public SollDatiAmministrDTO saveSollDatiAmministrWorking(SollDatiAmministrDTO dto) throws DAOException {
		LOGGER.debug("[SollDatiAmministrDAOImpl::saveSollDatiAmministrWorking] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Long genId = findNextSequenceValue("seq_risca_r_soll_dati_amministr");

			map.put("idSollDatiAmministr", genId);
			map.put("idAccertamento", dto.getIdAccertamento());
			map.put("codiceUtente", dto.getCodiceUtente());
			map.put("codiceUtenza", dto.getCodiceUtenza());
			map.put("tipoSollecito", dto.getTipoSollecito());
			map.put("tipoTitolo", dto.getTipoTitolo());
			map.put("numTitolo", dto.getNumTitolo());
			map.put("dataTitolo", dto.getDataTitolo());
			map.put("corpoIdrico", dto.getCorpoIdrico());
			map.put("comuneDiPresa", dto.getComuneDiPresa());
			map.put("annualitaPagamento", dto.getAnnualitaPagamento());
			map.put("annoRichPagamento", dto.getAnnoRichPagamento());
			map.put("scadenzaPagamento", dto.getScadenzaPagamento());
			map.put("motivoSoll", dto.getMotivoSoll());
			map.put("canoneDovuto", dto.getCanoneDovuto());
			map.put("importoVersato", dto.getImportoVersato());
			map.put("importoMancante", dto.getImportoMancante());
			map.put("interessiMancanti", dto.getInteressiMancanti());
			map.put("speseNotifica", dto.getSpeseNotifica());
			map.put("interessiTeorici", dto.getInteressiTeorici());
			map.put("interessiVersati", dto.getInteressiVersati());
			map.put("dilazione", dto.getDilazione());
			map.put("codiceAvviso", dto.getCodiceAvviso());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_W, null, null), params, keyHolder);
			dto.setIdSollDatiAmministr(genId);

			return dto;
		} catch (Exception e) {
			LOGGER.error("[SollDatiAmministrDAOImpl::saveSollDatiAmministrWorking] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiAmministrDAOImpl::saveSollDatiAmministrWorking] END");
		}
	}

	@Override
	public Integer updateCodiceAvvisoWorkingByIdAccertamento(String codiceAvviso, Long idAccertamento)
			throws DAOException {
		LOGGER.debug("[SollDatiAmministrDAOImpl::updateCodiceAvvisoWorkingByIdAccertamento] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codiceAvviso", codiceAvviso);
			map.put("idAccertamento", idAccertamento);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(UPDATE_CODICE_AVVISO_W_BY_ID_ACCERT, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDatiAmministrDAOImpl::updateCodiceAvvisoWorkingByIdAccertamento] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiAmministrDAOImpl::updateCodiceAvvisoWorkingByIdAccertamento] END");
		}

		return res;
	}

	@Override
	public Integer deleteSollDatiAmministrWorkingByElabora(Long idElabora) throws DAOException {
		LOGGER.debug("[SollDatiAmministrDAOImpl::deleteSollDatiAmministrWorkingByElabora] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_W_BY_ELABORA, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDatiAmministrDAOImpl::deleteSollDatiAmministrWorkingByElabora] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiAmministrDAOImpl::deleteSollDatiAmministrWorkingByElabora] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteSollDatiAmministrByIdAccertamento(Long idAccertamento) throws DAOException {
		LOGGER.debug("[SollDatiAmministrDAOImpl::deleteSollDatiAmministrByIdAccertamento] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAccertamento", idAccertamento);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_BY_ID_ACCERTAMENTO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDatiAmministrDAOImpl::deleteSollDatiAmministrByIdAccertamento] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiAmministrDAOImpl::deleteSollDatiAmministrByIdAccertamento] END");
		}

		return res;
	}

	@Override
	public Integer copySollDatiAmministrFromWorking(Long idElabora, String attore) throws DAOException {
		LOGGER.debug("[SollDatiAmministrDAOImpl::copySollDatiAmministrFromWorking] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestAttoreIns", attore);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataIns", now);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_COPY_FROM_WORKING, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[SollDatiAmministrDAOImpl::copySollDatiAmministrFromWorking] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[SollDatiAmministrDAOImpl::copySollDatiAmministrFromWorking] END");
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
	public RowMapper<SollDatiAmministrDTO> getRowMapper() throws SQLException {
		return new SollDatiAmministrRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<SollDatiAmministrDTO> getExtendedRowMapper() throws SQLException {
		return new SollDatiAmministrRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class SollDatiAmministrRowMapper implements RowMapper<SollDatiAmministrDTO> {

		/**
		 * Instantiates a new AvvisoDatiTitolare row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SollDatiAmministrRowMapper() throws SQLException {
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
		public SollDatiAmministrDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SollDatiAmministrDTO bean = new SollDatiAmministrDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SollDatiAmministrDTO bean) throws SQLException {
			bean.setIdSollDatiAmministr(getLong(rs, "id_soll_dati_amministr"));
			bean.setIdAccertamento(getLong(rs, "id_accertamento"));
			bean.setCodiceUtente(rs.getString("codice_utente"));
			bean.setCodiceUtenza(rs.getString("codice_utenza"));
			bean.setTipoSollecito(rs.getString("tipo_sollecito"));
			bean.setTipoTitolo(rs.getString("tipo_titolo"));
			bean.setNumTitolo(rs.getString("num_titolo"));
			bean.setDataTitolo(rs.getDate("data_titolo"));
			bean.setCorpoIdrico(rs.getString("corpo_idrico"));
			bean.setComuneDiPresa(rs.getString("comune_di_presa"));
			bean.setAnnualitaPagamento(rs.getInt("annualita_pagamento"));
			bean.setAnnoRichPagamento(rs.getString("anno_rich_pagamento"));
			bean.setScadenzaPagamento(rs.getString("scadenza_pagamento"));
			bean.setMotivoSoll(rs.getString("motivo_soll"));
			bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setImportoMancante(rs.getBigDecimal("importo_mancante"));
			bean.setInteressiMancanti(rs.getBigDecimal("interessi_mancanti"));
			bean.setSpeseNotifica(rs.getBigDecimal("spese_notifica"));
			bean.setInteressiTeorici(rs.getBigDecimal("interessi_teorici"));
			bean.setInteressiVersati(rs.getBigDecimal("interessi_versati"));
			bean.setDilazione(rs.getString("dilazione"));
			bean.setCodiceAvviso(rs.getString("codice_avviso"));
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

		private Long getLong(ResultSet rs, String strColName) throws SQLException {
			long nValue = rs.getLong(strColName);
			return rs.wasNull() ? null : nValue;
		}
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}