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
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.ArchiviazioneActaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.ArchiviazioneActaDTO;
import it.csi.risca.riscabesrv.util.BollUtils;

/**
 * The type ArchiviazioneActaDAO dao impl.
 *
 * @author CSI PIEMONTE
 */
public class ArchiviazioneActaDAOImpl extends RiscaBeSrvGenericDAO<ArchiviazioneActaDTO>
		implements ArchiviazioneActaDAO {

	private static final String QUERY_LOAD_BOLL_DOCUMENTI_DA_ARCHIVIARE_BY_SPED = " SELECT * "
			+ " FROM v_archiviazione_acta_ordinaria_speciale " + " where id_spedizione = :idSpedizione "
			+ " and (id_invio_acta is null  or cod_esito_invio_acta <> '000' or cod_esito_acquisizione_acta <> '000')";
	
	private static final String LIMIT_CONDITION = " limit :limitValue ";

	private static final String QUERY_LOAD_ACC_DOCUMENTI_DA_ARCHIVIARE_BY_SPED = " SELECT * "
			+ " FROM v_archiviazione_acta_bonari_solleciti " + " where id_spedizione = :idSpedizione "
			+ " and (id_invio_acta is null  or cod_esito_invio_acta <> '000' or cod_esito_acquisizione_acta <> '000')";

	private static final String QUERY_LOAD_BOLL_DOCUMENTI_NON_ARCHIVIATI_BY_SPED_ACTA = " SELECT * "
			+ " FROM v_archiviazione_acta_ordinaria_speciale " + " where id_spedizione_acta = :idSpedizioneActa "
			+ " and ( flg_archiviata_acta is null or flg_archiviata_acta = 0 ) ";

	private static final String QUERY_LOAD_ACC_DOCUMENTI_NON_ARCHIVIATI_BY_SPED_ACTA = " SELECT * "
			+ " FROM v_archiviazione_acta_bonari_solleciti " + " where id_spedizione_acta = :idSpedizioneActa "
			+ " and ( flg_archiviata_acta is null or flg_archiviata_acta = 0 ) ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchiviazioneActaDTO> loadDocumentiActaDaArchiviareBySped(Long idSpedizione, String codTipoSpedizione,
			Long limit) {
		LOGGER.debug("[ArchiviazioneActaDAOImpl::loadDocumentiActaDaArchiviareBySped] BEGIN");
		try {
			String query = codTipoSpedizione.equals(BollUtils.COD_TIPO_SPEDIZIONE_ACCERTA)
					? QUERY_LOAD_ACC_DOCUMENTI_DA_ARCHIVIARE_BY_SPED
					: QUERY_LOAD_BOLL_DOCUMENTI_DA_ARCHIVIARE_BY_SPED;

			Map<String, Object> map = new HashMap<>();
			map.put("idSpedizione", idSpedizione);
			if (limit != null) {
				map.put("limitValue", limit);
				query = query + LIMIT_CONDITION;
			}
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(query, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error(
					"[ArchiviazioneActaDAOImpl::loadDocumentiActaDaArchiviareBySped] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[ArchiviazioneActaDAOImpl::loadDocumentiActaDaArchiviareBySped] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[ArchiviazioneActaDAOImpl::loadDocumentiActaDaArchiviareBySped] END");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchiviazioneActaDTO> loadDocumentiActaNonArchiviatiBySpedActa(Long idSpedizioneActa,
			String codTipoSpedizione) {
		LOGGER.debug("[ArchiviazioneActaDAOImpl::loadDocumentiActaNonArchiviatiBySpedActa] BEGIN");
		try {
			String query = codTipoSpedizione.equals(BollUtils.COD_TIPO_SPEDIZIONE_ACCERTA)
					? QUERY_LOAD_ACC_DOCUMENTI_NON_ARCHIVIATI_BY_SPED_ACTA
					: QUERY_LOAD_BOLL_DOCUMENTI_NON_ARCHIVIATI_BY_SPED_ACTA;
			Map<String, Object> map = new HashMap<>();
			map.put("idSpedizioneActa", idSpedizioneActa);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(query, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error(
					"[ArchiviazioneActaDAOImpl::loadDocumentiActaNonArchiviatiBySpedActa] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(
					"[ArchiviazioneActaDAOImpl::loadDocumentiActaNonArchiviatiBySpedActa] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[ArchiviazioneActaDAOImpl::loadDocumentiActaNonArchiviatiBySpedActa] END");
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
	public RowMapper<ArchiviazioneActaDTO> getRowMapper() throws SQLException {
		return new SpedizioniArchiviazioneActaRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<ArchiviazioneActaDTO> getExtendedRowMapper() throws SQLException {
		return new SpedizioniArchiviazioneActaRowMapper();
	}

	/**
	 * The type InvioActa row mapper.
	 */
	public static class SpedizioniArchiviazioneActaRowMapper implements RowMapper<ArchiviazioneActaDTO> {

		/**
		 * Instantiates a new Elabora row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public SpedizioniArchiviazioneActaRowMapper() throws SQLException {
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
		public ArchiviazioneActaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ArchiviazioneActaDTO bean = new ArchiviazioneActaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, ArchiviazioneActaDTO bean) throws SQLException {
			if (rsHasColumn(rs, "id_ambito"))
				bean.setIdAmbito(rs.getLong("id_ambito"));
			if (rsHasColumn(rs, "cod_ambito"))
				bean.setCodAmbito(rs.getString("cod_ambito"));
			if (rsHasColumn(rs, "id_spedizione"))
				bean.setIdSpedizione(rs.getLong("id_spedizione"));
			if (rsHasColumn(rs, "id_tipo_spedizione"))
				bean.setIdTipoSpedizione(rs.getLong("id_tipo_spedizione"));
			if (rsHasColumn(rs, "id_elabora_spedizione"))
				bean.setIdElaboraSpedizione(rs.getLong("id_elabora_spedizione"));
			if (rsHasColumn(rs, "cod_tipo_elabora"))
				bean.setCodTipoElabora(rs.getString("cod_tipo_elabora"));
			if (rsHasColumn(rs, "data_protocollo"))
				bean.setDataProtocollo(rs.getDate("data_protocollo"));
			if (rsHasColumn(rs, "data_scadenza"))
				bean.setDataScadenza(rs.getDate("data_scadenza"));
			if (rsHasColumn(rs, "anno"))
				bean.setAnno(rs.getInt("anno"));
			if (rsHasColumn(rs, "cod_tipo_spedizione"))
				bean.setCodTipoSpedizione(rs.getString("cod_tipo_spedizione"));
			if (rsHasColumn(rs, "id_spedizione_acta"))
				bean.setIdSpedizioneActa(rs.getLong("id_spedizione_acta"));
			if (rsHasColumn(rs, "id_elabora_spedizione_acta"))
				bean.setIdElaboraSpedizioneActa(getResultSetLong(rs, "id_elabora_spedizione_acta"));
			if (rsHasColumn(rs, "nome_dirigente_protempore"))
				bean.setNomeDirigenteProtempore(rs.getString("nome_dirigente_protempore"));
			if (rsHasColumn(rs, "id_accertamento"))
				bean.setIdAccertamento(getResultSetLong(rs, "id_accertamento"));
			if (rsHasColumn(rs, "cod_tipo_accertamento"))
				bean.setCodTipoAccertamento(rs.getString("cod_tipo_accertamento"));
			if (rsHasColumn(rs, "nap"))
				bean.setNap(rs.getString("nap"));
			if (rsHasColumn(rs, "num_protocollo"))
				bean.setNumProtocollo(rs.getString("num_protocollo"));
			if (rsHasColumn(rs, "id_soggetto"))
				bean.setIdSoggetto(rs.getLong("id_soggetto"));
			if (rsHasColumn(rs, "cf_soggetto"))
				bean.setCfSoggetto(rs.getString("cf_soggetto"));
			if (rsHasColumn(rs, "nome"))
				bean.setNome(rs.getString("nome"));
			if (rsHasColumn(rs, "cognome"))
				bean.setCognome(rs.getString("cognome"));
			if (rsHasColumn(rs, "den_soggetto"))
				bean.setDenSoggetto(rs.getString("den_soggetto"));
			if (rsHasColumn(rs, "id_tipo_soggetto"))
				bean.setIdTipoSoggetto(rs.getLong("id_tipo_soggetto"));
			if (rsHasColumn(rs, "cod_tipo_soggetto"))
				bean.setCodTipoSoggetto(rs.getString("cod_tipo_soggetto"));
			if (rsHasColumn(rs, "codice_utenza"))
				bean.setCodiceUtenza(rs.getString("codice_utenza"));
			if (rsHasColumn(rs, "id_invio_acta"))
				bean.setIdInvioActa(getResultSetLong(rs, "id_invio_acta"));
			if (rsHasColumn(rs, "flg_multiclassificazione"))
				bean.setFlgMulticlassificazione(rs.getInt("flg_multiclassificazione"));
			if (rsHasColumn(rs, "flg_archiviata_acta"))
				bean.setFlgArchiviataActa(rs.getInt("flg_archiviata_acta"));
			if (rsHasColumn(rs, "data_invio"))
				bean.setDataInvio(rs.getDate("data_invio"));
			if (rsHasColumn(rs, "cod_esito_invio_acta"))
				bean.setCodEsitoInvioActa(rs.getString("cod_esito_invio_acta"));
			if (rsHasColumn(rs, "cod_esito_acquisizione_acta"))
				bean.setCodEsitoAcquisizioneActa(rs.getString("cod_esito_acquisizione_acta"));
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

		private Long getResultSetLong(ResultSet resultset, String colName) throws SQLException {
			long v = resultset.getLong(colName);
			if (resultset.wasNull()) {
				return null;
			}
			return Long.valueOf(v);
		}

	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}