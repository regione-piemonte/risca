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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiTitolareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AvvisoDatiTitolareDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type AvvisoPagamento dao.
 *
 * @author CSI PIEMONTE
 */
public class AvvisoDatiTitolareDAOImpl extends RiscaBeSrvGenericDAO<AvvisoDatiTitolareDTO>
		implements AvvisoDatiTitolareDAO {

	public static final String QUERY_INSERT = "INSERT INTO risca_r_avviso_dati_titolare "
			+ "(nap, id_soggetto, id_gruppo_soggetto, nome_titolare_ind_post, indirizzo_ind_post, presso_ind_post, comune_ind_post, "
			+ "cap_ind_post, prov_ind_post, importo_da_versare, scadenza_pagamento, n_utenze, annualita_pagamento, "
			+ "stati_pagamenti, dilazione, quinto_campo, codice_fiscale_eti_calc, codice_fiscale_calc, n_avviso_calc, "
			+ "frase_stato_pag_calc, codice_utenza_calc, scadenza_pagamento2, vuoto3, modalita_invio, pec_email, "
			+ "numero_protocollo_sped, data_protocollo_sped, gest_attore_ins, gest_data_ins, gest_attore_upd, "
			+ "gest_data_upd, gest_uid, id_titolare) "
			+ "VALUES(:nap, :idSoggetto, :idGruppoSoggetto, :nomeTitolareIndPost, :indirizzoIndPost, :pressoIndPost, :comuneIndPost, "
			+ ":capIndPost, :provIndPost, :importoDaVersare, :scadenzaPagamento, :nUtenze, :annualitaPagamento, "
			+ ":statiPagamenti, :dilazione, :quintoCampo, :codiceFiscaleEtiCalc, :codiceFiscaleCalc, :nAvvisoCalc, "
			+ ":fraseStatoPagCalc, :codiceUtenzaCalc, :scadenzaPagamento2, :vuoto3, :modalitaInvio, :pecEmail, "
			+ ":numeroProtocolloSped, :dataProtocolloSped, :gestAttoreIns, :gestDataIns, :gestAttoreUpd, "
			+ ":gestDataUpd, :gestUid, :idTitolare)";

	public static final String QUERY_INSERT_W = "INSERT INTO risca_w_avviso_dati_titolare "
			+ "(nap, id_soggetto, id_gruppo_soggetto, nome_titolare_ind_post, indirizzo_ind_post, presso_ind_post, comune_ind_post, "
			+ "cap_ind_post, prov_ind_post, importo_da_versare, scadenza_pagamento, n_utenze, annualita_pagamento, "
			+ "stati_pagamenti, dilazione, quinto_campo, codice_fiscale_eti_calc, codice_fiscale_calc, n_avviso_calc, "
			+ "frase_stato_pag_calc, codice_utenza_calc, scadenza_pagamento2, vuoto3, modalita_invio, pec_email, "
			+ "numero_protocollo_sped, data_protocollo_sped, id_titolare) "
			+ "VALUES(:nap, :idSoggetto, :idGruppoSoggetto, :nomeTitolareIndPost, :indirizzoIndPost, :pressoIndPost, :comuneIndPost, "
			+ ":capIndPost, :provIndPost, :importoDaVersare, :scadenzaPagamento, :nUtenze, :annualitaPagamento, "
			+ ":statiPagamenti, :dilazione, :quintoCampo, :codiceFiscaleEtiCalc, :codiceFiscaleCalc, :nAvvisoCalc, "
			+ ":fraseStatoPagCalc, :codiceUtenzaCalc, :scadenzaPagamento2, :vuoto3, :modalitaInvio, :pecEmail, "
			+ ":numeroProtocolloSped, :dataProtocolloSped, :idTitolare)";

	private static final String QUERY_UDPDATE_W_IMPORTO_DA_VERSARE = "update risca_w_avviso_dati_titolare "
			+ "set importo_da_versare = COALESCE(importo_da_versare, 0) + :canoneAnnuo, "
			+ "annualita_pagamento = :annualitaPagamento " + "where nap = :nap";

	private static final String QUERY_UDPDATE_W_DILAZIONE = "update risca_w_avviso_dati_titolare a "
			+ "set dilazione = :dilazione, " + "scadenza_pagamento2 = :scadenzaPagamento2, "
			+ "n_utenze = (select count(*) from RISCA_W_AVVISO_DATI_AMMIN b where b.nap = a.nap) " + "where nap = :nap";

	private static final String QUERY_UDPDATE_W_COMPENSAZIONE = "update risca_w_avviso_dati_titolare "
			+ "set importo_da_versare = COALESCE(importo_da_versare, 0) - :compensazione " + "where nap = :nap ";

	private static final String QUERY_LOAD_W_BY_SPEDIZIONE = "select * " + "from risca_w_avviso_dati_titolare a "
			+ "where a.nap IN ( " + "	  select b.nap " + "	  from risca_w_avviso_pagamento b "
			+ "	  where id_spedizione = :idSpedizione "
			+ "	  and EXISTS (select 'XXX' from risca_w_stato_debitorio c where b.nap = c.nap) " + ") order by a.nap";

	private static final String QUERY_DELETE_TITOLARI_SENZA_RISCOSSIONI = "delete from RISCA_W_AVVISO_DATI_TITOLARE b "
			+ " where NOT EXISTS (select 1 " + "	from RISCA_W_AVVISO_DATI_AMMIN a " + "	where b.nap = a.nap)";

	private static final String QUERY_UPDATE_NUMERO_UTENZE = "update RISCA_W_AVVISO_DATI_TITOLARE as a "
			+ " set n_utenze = (select count(*) from RISCA_W_AVVISO_DATI_AMMIN b where b.nap = a.nap)";

	private static final String QUERY_INSERT_AVVISO_DATI_TITOLARE_FROM_WORKING_BY_NAP = " INSERT INTO risca_r_avviso_dati_titolare "
			+ " (nap, id_soggetto, id_gruppo_soggetto, nome_titolare_ind_post, indirizzo_ind_post, presso_ind_post, comune_ind_post, cap_ind_post, prov_ind_post, importo_da_versare,  "
			+ " scadenza_pagamento, n_utenze, annualita_pagamento, stati_pagamenti, dilazione, quinto_campo, codice_fiscale_eti_calc, codice_fiscale_calc,  "
			+ " n_avviso_calc, frase_stato_pag_calc, codice_utenza_calc, scadenza_pagamento2, vuoto3, modalita_invio, pec_email, numero_protocollo_sped, data_protocollo_sped,  "
			+ " gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid, id_titolare) "
			+ " select nap, id_soggetto, id_gruppo_soggetto, nome_titolare_ind_post, indirizzo_ind_post, presso_ind_post, comune_ind_post, cap_ind_post, prov_ind_post, importo_da_versare,  "
			+ " scadenza_pagamento, n_utenze, annualita_pagamento, stati_pagamenti, dilazione, quinto_campo, codice_fiscale_eti_calc, codice_fiscale_calc,  "
			+ " n_avviso_calc, frase_stato_pag_calc, codice_utenza_calc, scadenza_pagamento2, vuoto3, modalita_invio, pec_email, numero_protocollo_sped, data_protocollo_sped,  "
			+ " :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, null, id_titolare "
			+ " from risca_w_avviso_dati_titolare " + " where nap = :nap";

	private static final String QUERY_DELETE_AVVISO_DATI_TITOLARE_WORKING_BY_NAP = " delete from risca_w_avviso_dati_titolare where nap = :nap ";

	private static final String QUERY_LOAD_AVVISO_DATI_TITOLARE_BY_NAP = " select * from risca_r_avviso_dati_titolare where nap = :nap ";

	@Override
	public AvvisoDatiTitolareDTO saveAvvisoDatiTitolare(AvvisoDatiTitolareDTO dto) throws DAOException {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::saveAvvisoDatiTitolare] BEGIN");
		try {
			dto = saveAvvisoDatiTitolare(dto, false);
		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::saveAvvisoDatiTitolare] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::saveAvvisoDatiTitolare] END");
		}

		return dto;
	}

	@Override
	public AvvisoDatiTitolareDTO saveAvvisoDatiTitolareWorking(AvvisoDatiTitolareDTO dto) throws DAOException {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::saveAvvisoDatiTitolareWorking] BEGIN");
		try {
			dto = saveAvvisoDatiTitolare(dto, true);
		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::saveAvvisoDatiTitolareWorking] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::saveAvvisoDatiTitolareWorking] END");
		}

		return dto;
	}

	private AvvisoDatiTitolareDTO saveAvvisoDatiTitolare(AvvisoDatiTitolareDTO dto, boolean working) {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();

		map.put("nap", dto.getNap());
		map.put("idSoggetto", dto.getIdSoggetto());
		map.put("idGruppoSoggetto", dto.getIdGruppoSoggetto());
		map.put("nomeTitolareIndPost", dto.getNomeTitolareIndPost());
		map.put("indirizzoIndPost", dto.getIndirizzoIndPost());
		map.put("pressoIndPost", dto.getPressoIndPost());
		map.put("comuneIndPost", dto.getComuneIndPost());
		map.put("capIndPost", dto.getCapIndPost());
		map.put("provIndPost", dto.getProvIndPost());
		map.put("importoDaVersare", dto.getImportoDaVersare());
		map.put("scadenzaPagamento", dto.getScadenzaPagamento());
		map.put("nUtenze", dto.getnUtenze());
		map.put("annualitaPagamento", dto.getAnnualitaPagamento());
		map.put("statiPagamenti", dto.getStatiPagamenti());
		map.put("dilazione", dto.getDilazione());
		map.put("quintoCampo", dto.getQuintoCampo());
		map.put("codiceFiscaleEtiCalc", dto.getCodiceFiscaleEtiCalc());
		map.put("codiceFiscaleCalc", dto.getCodiceFiscaleCalc());
		map.put("nAvvisoCalc", dto.getnAvvisoCalc());
		map.put("fraseStatoPagCalc", dto.getFraseStatoPagCalc());
		map.put("codiceUtenzaCalc", dto.getCodiceUtenzaCalc());
		map.put("scadenzaPagamento2", dto.getScadenzaPagamento2());
		map.put("vuoto3", dto.getVuoto3());
		map.put("modalitaInvio", dto.getModalitaInvio());
		map.put("pecEmail", dto.getPecEmail());
		map.put("numeroProtocolloSped", dto.getNumeroProtocolloSped());
		map.put("dataProtocolloSped", dto.getDataProtocolloSped());
		map.put("idTitolare", dto.getIdTitolare());
		if (!working) {
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
		}

		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT;
		if (working) {
			query = QUERY_INSERT_W;
		}
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setGestDataIns(now);
		dto.setGestDataUpd(now);
		return dto;
	}

	@Override
	public Integer updateWorkingDatiTitolareImportoDaVersare(BigDecimal canoneAnnuo, int annualitaPagamento, String nap)
			throws DAOException {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::updateWDatiTitolareImportoDaVersare] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("canoneAnnuo", canoneAnnuo);
			map.put("annualitaPagamento", annualitaPagamento);
			map.put("nap", nap);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UDPDATE_W_IMPORTO_DA_VERSARE, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::updateWDatiTitolareImportoDaVersare] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::updateWDatiTitolareImportoDaVersare] END");
		}

		return res;
	}

	@Override
	public Integer updateWorkingDatiTitolareDilazioneScandenza(String dilazione, String scadenzaPagamento2, String nap)
			throws DAOException {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::updateWDatiTitolareDilazioneScandenza] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("dilazione", dilazione);
			map.put("scadenzaPagamento2", scadenzaPagamento2);
			map.put("nap", nap);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UDPDATE_W_DILAZIONE, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::updateWDatiTitolareDilazioneScandenza] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::updateWDatiTitolareDilazioneScandenza] END");
		}

		return res;
	}

	@Override
	public Integer updateWorkingDatiTitolareCompensazione(BigDecimal compensazione, String nap) throws DAOException {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::updateWorkingDatiTitolareCompensazione] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("compensazione", compensazione);
			map.put("nap", nap);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UDPDATE_W_COMPENSAZIONE, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::updateWorkingDatiTitolareCompensazione] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::updateWorkingDatiTitolareCompensazione] END");
		}

		return res;
	}

	@Override
	public List<AvvisoDatiTitolareDTO> loadAvvisoDatiTitolareWorkingBySpedizione(Long idSpedizione) {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::loadAvvisoDatiTitolareWorkingBySpedizione] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idSpedizione", idSpedizione);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_LOAD_W_BY_SPEDIZIONE, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error(
					"[AvvisoDatiTitolareDAOImpl::loadAvvisoDatiTitolareWorkingBySpedizione] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(
					"[AvvisoDatiTitolareDAOImpl::loadAvvisoDatiTitolareWorkingBySpedizione] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::loadAvvisoDatiTitolareWorkingBySpedizione] END");
		}
	}

	@Override
	public Integer deleteWorkingDatiTitolareSenzaRiscDaBollettare() throws DAOException {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::deleteWorkingDatiTitolareSenzaRiscDaBollettare] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_TITOLARI_SENZA_RISCOSSIONI, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::deleteWorkingDatiTitolareSenzaRiscDaBollettare] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::deleteWorkingDatiTitolareSenzaRiscDaBollettare] END");
		}
		return res;
	}

	@Override
	public Integer updateWorkingDatiTitolareNumUtenze() throws DAOException {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::updateWorkingDatiTitolareNumUtenze] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_NUMERO_UTENZE, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::deleteWorkingDatiTitolareSenzaRiscDaBollettare] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::updateWorkingDatiTitolareNumUtenze] END");
		}
		return res;
	}

	@Override
	public Integer copyAvvisoDatiTitolareFromWorkingByNap(String nap, String attore) throws DAOException {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::copyAvvisoDatiTitolareFromWorkingByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("nap", nap);
			map.put("gestAttoreIns", attore);
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_INSERT_AVVISO_DATI_TITOLARE_FROM_WORKING_BY_NAP, null, null), params,
					keyHolder);

		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::copyAvvisoDatiTitolareFromWorkingByNap] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::copyAvvisoDatiTitolareFromWorkingByNap] END");
		}

		return res;
	}

	@Override
	public Integer deleteAvvisoDatiTitolarenWorkingByNap(String nap) throws DAOException {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::deleteAvvisoDatiTitolarenWorkingByNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_AVVISO_DATI_TITOLARE_WORKING_BY_NAP, null, null), params,
					keyHolder);

		} catch (Exception e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::deleteAvvisoDatiTitolarenWorkingByNap] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::deleteAvvisoDatiTitolarenWorkingByNap] END");
		}

		return res;
	}

	@Override
	public AvvisoDatiTitolareDTO loadAvvisoDatiTitolareByNap(String nap) {
		LOGGER.debug("[AvvisoDatiTitolareDAOImpl::loadAvvisoDatiTitolareByNap] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(getQuery(QUERY_LOAD_AVVISO_DATI_TITOLARE_BY_NAP, null, null), params,
					getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::loadAvvisoDatiTitolareByNap]Data not found for nap: "+ nap);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::loadAvvisoDatiTitolareByNap] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[AvvisoDatiTitolareDAOImpl::loadAvvisoDatiTitolareByNap] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AvvisoDatiTitolareDAOImpl::loadAvvisoDatiTitolareByNap] END");
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
	public RowMapper<AvvisoDatiTitolareDTO> getRowMapper() throws SQLException {
		return new AvvisoDatiTitolareRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<AvvisoDatiTitolareDTO> getExtendedRowMapper() throws SQLException {
		return new AvvisoDatiTitolareRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class AvvisoDatiTitolareRowMapper implements RowMapper<AvvisoDatiTitolareDTO> {

		/**
		 * Instantiates a new AvvisoDatiTitolare row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public AvvisoDatiTitolareRowMapper() throws SQLException {
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
		public AvvisoDatiTitolareDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AvvisoDatiTitolareDTO bean = new AvvisoDatiTitolareDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AvvisoDatiTitolareDTO bean) throws SQLException {
			bean.setNap(rs.getString("nap"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			bean.setIdGruppoSoggetto(getLong(rs, "id_gruppo_soggetto"));
			bean.setNomeTitolareIndPost(rs.getString("nome_titolare_ind_post"));
			bean.setIndirizzoIndPost(rs.getString("indirizzo_ind_post"));
			bean.setPressoIndPost(rs.getString("presso_ind_post"));
			bean.setComuneIndPost(rs.getString("comune_ind_post"));
			bean.setCapIndPost(rs.getString("cap_ind_post"));
			bean.setProvIndPost(rs.getString("prov_ind_post"));
			bean.setImportoDaVersare(rs.getBigDecimal("importo_da_versare"));
			bean.setScadenzaPagamento(rs.getString("scadenza_pagamento"));
			bean.setnUtenze(rs.getInt("n_utenze"));
			bean.setAnnualitaPagamento(rs.getInt("annualita_pagamento"));
			bean.setStatiPagamenti(rs.getString("stati_pagamenti"));
			bean.setDilazione(rs.getString("dilazione"));
			bean.setQuintoCampo(rs.getString("quinto_campo"));
			bean.setCodiceFiscaleEtiCalc(rs.getString("codice_fiscale_eti_calc"));
			bean.setCodiceFiscaleCalc(rs.getString("codice_fiscale_calc"));
			bean.setnAvvisoCalc(rs.getString("n_avviso_calc"));
			bean.setFraseStatoPagCalc(rs.getString("frase_stato_pag_calc"));
			bean.setCodiceUtenzaCalc(rs.getString("codice_utenza_calc"));
			bean.setScadenzaPagamento2(rs.getString("scadenza_pagamento2"));
			bean.setVuoto3(rs.getString("vuoto3"));
			bean.setModalitaInvio(rs.getString("modalita_invio"));
			bean.setPecEmail(rs.getString("pec_email"));
			bean.setNumeroProtocolloSped(rs.getString("numero_protocollo_sped"));
			bean.setDataProtocolloSped(rs.getDate("data_protocollo_sped"));
			bean.setIdTitolare(rs.getString("id_titolare"));

			if (rsHasColumn(rs, "gest_attore_ins"))
				bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if (rsHasColumn(rs, "gest_data_ins"))
				bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if (rsHasColumn(rs, "gest_attore_upd"))
				bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if (rsHasColumn(rs, "gest_data_upd"))
				bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if (rsHasColumn(rs, "gest_uid"))
				bean.setGestUid(rs.getString("gest_uid"));
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