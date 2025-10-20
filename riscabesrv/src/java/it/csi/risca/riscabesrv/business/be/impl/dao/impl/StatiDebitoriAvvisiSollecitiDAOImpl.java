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

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatiDebitoriAvvisiSollecitiDAO;
import it.csi.risca.riscabesrv.dto.AvvisoSollecitoCheckSDResultDTO;
import it.csi.risca.riscabesrv.dto.AvvisoSollecitoCheckSoggettoResultDTO;
import it.csi.risca.riscabesrv.dto.StatiDebitoriAvvisiSollecitiDTO;

public class StatiDebitoriAvvisiSollecitiDAOImpl extends RiscaBeSrvGenericDAO<StatiDebitoriAvvisiSollecitiDTO>
		implements StatiDebitoriAvvisiSollecitiDAO {

	public static final String QUERY_SELECT_V_AVVISI_BONARI_SOLLECITI = " select * from v_stati_debitori_avvisi_e_solleciti vsdaes "
			+ "inner join risca_d_attivita_stato_deb rdasd on vsdaes.id_attivita_stato_deb = rdasd.id_attivita_stato_deb "
			+ "where cod_ambito = :codAmbito " + "and rdasd.cod_attivita_stato_deb = :codAttivitaStatoDeb "
			+ "ORDER BY id_titolare, id_gruppo_soggetto, cod_riscossione, id_stato_debitorio, id_pagamento ";

	public static final String QUERY_CONDIZIONE_1_PAGOPA = " select :idSoggetto as soggetto_titolare, "
			+ "       CASE  WHEN sd_nap_valorizzato.quanti = 0 then 'KO' ELSE 'OK' "
			+ "       END as sd_nap_valorizzato, CASE "
			+ "          WHEN sd_nap_nullo.quanti > 0 then 'KO' ELSE 'OK' "
			+ "       END as sd_nap_nullo, CASE "
			+ "          WHEN codici_avviso_attivi.quanti = 0 then 'KO' ELSE 'OK' "
			+ "       END as codici_avviso_attivi, CASE  "
			+ "          WHEN codici_avviso_non_attivi.quanti > 0 then 'KO' ELSE 'OK'  "
			+ "       END as codici_avviso_non_attivi " + "from ( "
			+ "     select 'SD con NAP valorizzato' as tipo_conteggio, count(*) as quanti "
			+ "            from risca_t_riscossione ris "
			+ "      inner join risca_t_stato_debitorio sta on ris.id_riscossione = sta.id_riscossione "
			+ "           where ris.id_soggetto = :idSoggetto and sta.nap is not null "
			+ "               and sta.id_attivita_stato_deb = :idAttivitaStatoDeb ) sd_nap_valorizzato, "
			+ "     ( " + "       select 'SD con NAP NULL' as tipo_conteggio, count(*) as quanti "
			+ "            from risca_t_riscossione ris "
			+ "      inner join risca_t_stato_debitorio sta on ris.id_riscossione = sta.id_riscossione "
			+ "           where ris.id_soggetto = :idSoggetto and sta.nap is null "
			+ "             and sta.id_attivita_stato_deb = :idAttivitaStatoDeb ) sd_nap_nullo, ( "
			+ "      select 'codici avviso ATTIVI' as tipo_conteggio, count(*) as quanti "
			+ "             from risca_t_iuv iuv inner join ( "
			+ "                      select sta.nap from risca_t_riscossione ris "
			+ "                  inner join risca_t_stato_debitorio sta on ris.id_riscossione = sta.id_riscossione "
			+ "                       where ris.id_soggetto = :idSoggetto) lna "
			+ "                          on iuv.nap = lna.nap and iuv.id_stato_iuv = 1 "
			+ "     ) codici_avviso_attivi, ( "
			+ "     select 'codici avviso NON attivi o non presenti' as tipo_conteggio, count(*) as quanti "
			+ "	         from ( select * from risca_t_riscossione ris "
			+ "			inner join risca_t_stato_debitorio sta on ris.id_riscossione = sta.id_riscossione "
			+ "			left join risca_t_iuv iuv on sta.nap = iuv.nap "
			+ "			where ris.id_soggetto = :idSoggetto "
			+ "			and sta.id_attivita_stato_deb = :idAttivitaStatoDeb and iuv.id_stato_iuv <> 1 "
			+ "                   ) lis where lis.codice_avviso is not null "
			+ "     ) codici_avviso_non_attivi ";
	
	public static final String QUERY_CONDIZIONE_2_PAGOPA = "select s.id_soggetto as id_soggetto, "
			+ "sd.id_stato_debitorio, sd.nap as sd_nap, sd.id_soggetto as sd_id_soggetto_titolare, "
			+ "r.id_soggetto  as prat_id_soggetto_titolare, s.cf_soggetto as sogg_cf  "
			+ "from RISCA_T_STATO_DEBITORIO sd, RISCA_T_SOGGETTO s "
			+ "left join risca_t_riscossione r on r.id_soggetto = s.id_soggetto "
			+ "where sd.id_attivita_stato_deb = :idAttivitaStatoDeb "
			+ "and sd.id_riscossione = r.id_riscossione "
			+ "and r.id_soggetto = :idSoggetto "
			+ "order by s.id_soggetto, sd.nap";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StatiDebitoriAvvisiSollecitiDTO> loadStatiDebitoriAvvisiSolleciti(String codAmbito,
			String codAttivitaStatoDeb) {
		LOGGER.debug("[StatiDebitoriAvvisiSollecitiDAOImpl::loadStatiDebitoriAvvisiSolleciti] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codAmbito", codAmbito);
			map.put("codAttivitaStatoDeb", codAttivitaStatoDeb);
			MapSqlParameterSource params = getParameterValue(map);

			// TODO Query valida per ambito AMBIENTE --> al momento non si conosce la logica
			// per altri ambiti
			return template.query(getQuery(QUERY_SELECT_V_AVVISI_BONARI_SOLLECITI, null, null), params,
					getExtendedRowMapper());
		} catch (SQLException e) {
			LOGGER.error(
					"[StatiDebitoriAvvisiSollecitiDAOImpl::loadStatiDebitoriAvvisiSolleciti] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(
					"[StatiDebitoriAvvisiSollecitiDAOImpl::loadStatiDebitoriAvvisiSolleciti] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[StatiDebitoriAvvisiSollecitiDAOImpl::loadStatiDebitoriAvvisiSolleciti] END");
		}
	}

	@Override
	public AvvisoSollecitoCheckSDResultDTO checkCondizioneSdPerPagopa(Long idSoggetto, Long idAttivitaStatoDeb) {
		LOGGER.debug("[StatiDebitoriAvvisiSollecitiDAOImpl::checkCondizioneSdPerPagopa] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idSoggetto", idSoggetto);
			map.put("idAttivitaStatoDeb", idAttivitaStatoDeb);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(getQuery(QUERY_CONDIZIONE_1_PAGOPA, null, null), params,
					new AvvisoSollecitoCheckSDResultRowMapper());
		} catch (SQLException e) {
			LOGGER.error(
					"[StatiDebitoriAvvisiSollecitiDAOImpl::checkCondizioneSdPerPagopa] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(
					"[StatiDebitoriAvvisiSollecitiDAOImpl::checkCondizioneSdPerPagopa] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[StatiDebitoriAvvisiSollecitiDAOImpl::checkCondizioneSdPerPagopa] END");
		}
	}
	
	@Override
	public List<AvvisoSollecitoCheckSoggettoResultDTO> loadSoggettoSdPraticaPerCheckCondizione2(Long idSoggetto, Long idAttivitaStatoDeb) {
		LOGGER.debug("[StatiDebitoriAvvisiSollecitiDAOImpl::loadSoggettoSdPraticaPerCheckCondizione2] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idSoggetto", idSoggetto);
			map.put("idAttivitaStatoDeb", idAttivitaStatoDeb);
			MapSqlParameterSource params = getParameterValue(map);

			return template.query(getQuery(QUERY_CONDIZIONE_2_PAGOPA, null, null), params,
					new AvvisoSollecitoCheckSoggettoRowMapper());
		} catch (SQLException e) {
			LOGGER.error(
					"[StatiDebitoriAvvisiSollecitiDAOImpl::loadSoggettoSdPraticaPerCheckCondizione2] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(
					"[StatiDebitoriAvvisiSollecitiDAOImpl::loadSoggettoSdPraticaPerCheckCondizione2] Errore nell'accesso ai dati",
					e);
			return null;
		} finally {
			LOGGER.debug("[StatiDebitoriAvvisiSollecitiDAOImpl::loadStatiDebitoriAvvisiSolleciti] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<StatiDebitoriAvvisiSollecitiDTO> getRowMapper() throws SQLException {

		return new StatiDebitoriAvvisiSollecitiRowMapper();
	}

	@Override
	public RowMapper<StatiDebitoriAvvisiSollecitiDTO> getExtendedRowMapper() throws SQLException {
		return new StatiDebitoriAvvisiSollecitiRowMapper();
	}

	public static class StatiDebitoriAvvisiSollecitiRowMapper implements RowMapper<StatiDebitoriAvvisiSollecitiDTO> {

		public StatiDebitoriAvvisiSollecitiRowMapper() throws SQLException {
			// Instantiate class
		}

		@Override
		public StatiDebitoriAvvisiSollecitiDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			StatiDebitoriAvvisiSollecitiDTO bean = new StatiDebitoriAvvisiSollecitiDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, StatiDebitoriAvvisiSollecitiDTO bean) throws SQLException {
			bean.setIdStatoDebitorio(getResultSetLong(rs, "id_stato_debitorio"));
			bean.setDescPeriodoPagamento(rs.getString("desc_periodo_pagamento"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setIdAttivitaStatoDeb(getResultSetLong(rs, "id_attivita_stato_deb"));
			bean.setIdSoggettoSd(getResultSetLong(rs, "id_soggetto_sd"));
			bean.setIdGruppoSoggettoSd(getResultSetLong(rs, "id_gruppo_soggetto_sd"));
			bean.setIdTitolare(rs.getString("id_titolare"));
			bean.setNumTitolo(rs.getString("num_titolo"));
			bean.setDataProvvedimento(rs.getDate("data_provvedimento"));
			bean.setFlgDilazione(rs.getInt("flg_dilazione"));
			bean.setNap(rs.getString("nap"));
			bean.setCodAmbito(rs.getString("cod_ambito"));
			bean.setDesAmbito(rs.getString("des_ambito"));
			bean.setDesTipoTitolo(rs.getString("des_tipo_titolo"));
			bean.setIdRiscossione(getResultSetLong(rs, "id_riscossione"));
			bean.setIdTipoRiscossione(getResultSetLong(rs, "id_tipo_riscossione"));
			bean.setIdStatoRiscossione(getResultSetLong(rs, "id_stato_riscossione"));
			bean.setIdSoggetto(getResultSetLong(rs, "id_soggetto"));
			bean.setIdGruppoSoggetto(getResultSetLong(rs, "id_gruppo_soggetto"));
			bean.setIdTipoAutorizza(getResultSetLong(rs, "id_tipo_autorizza"));
			bean.setCodRiscossione(rs.getString("cod_riscossione"));
			bean.setCodRiscossioneProv(rs.getString("cod_riscossione_prov"));
			bean.setCodRiscossioneProg(rs.getString("cod_riscossione_prog"));
			bean.setIdSiglaRiscossione(getResultSetLong(rs, "id_sigla_riscossione"));
			bean.setCodRiscossioneLetteraProv(rs.getString("cod_riscossione_lettera_prov"));
			bean.setNumPratica(rs.getString("num_pratica"));
			bean.setFlgPrenotata(rs.getInt("flg_prenotata"));
			bean.setMotivoPrenotazione(rs.getString("motivo_prenotazione"));
			bean.setNoteRiscossione(rs.getString("note_riscossione"));
			bean.setDataIniConcessione(rs.getDate("data_ini_concessione"));
			bean.setDataScadConcessione(rs.getDate("data_scad_concessione"));
			bean.setDataIniSospCanone(rs.getDate("data_ini_sosp_canone"));
			bean.setDataFineSospCanone(rs.getDate("data_fine_sosp_canone"));
			bean.setJsonDt(rs.getString("json_dt"));
			bean.setIdComponenteDt(getResultSetLong(rs, "id_componente_dt"));
			bean.setCfSoggetto(rs.getString("cf_soggetto"));
			bean.setIdTipoSoggetto(getResultSetLong(rs, "id_tipo_soggetto"));
			bean.setIdRiscossioneP(getResultSetLong(rs, "id_riscossione_p"));
			bean.setIdRecapitoP(getResultSetLong(rs, "id_recapito_p"));
			bean.setCodTipoRecapitoP(rs.getString("cod_tipo_recapito_p"));
			bean.setDesTipoRecapitoP(rs.getString("des_tipo_recapito_p"));
			bean.setCodTipoInvioP(rs.getString("cod_tipo_invio_p"));
			bean.setDesTipoInvioP(rs.getString("des_tipo_invio_p"));
			bean.setCodTipoSedeP(rs.getString("cod_tipo_sede_p"));
			bean.setDesTipoSedeP(rs.getString("des_tipo_sede_p"));
			bean.setIdRiscossioneA(getResultSetLong(rs, "id_riscossione_a"));
			bean.setIdRecapitoA(getResultSetLong(rs, "id_recapito_a"));
			bean.setCodTipoRecapitoA(rs.getString("cod_tipo_recapito_a"));
			bean.setDesTipoRecapitoA(rs.getString("des_tipo_recapito_a"));
			bean.setCodTipoInvioA(rs.getString("cod_tipo_invio_a"));
			bean.setDesTipoInvioA(rs.getString("des_tipo_invio_a"));
			bean.setCodTipoSedeA(rs.getString("cod_tipo_sede_a"));
			bean.setDesTipoSedeA(rs.getString("des_tipo_sede_a"));
			bean.setSommaImportoVersato(rs.getBigDecimal("somma_importo_versato"));
			bean.setIdRataSd(getResultSetLong(rs, "id_rata_sd"));
			bean.setDataScadenzaPagamento(rs.getDate("data_scadenza_pagamento"));
			bean.setAnnualitaDiPagamento(rs.getString("annualita_di_pagamento"));
			bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
			bean.setRataInteressiMaturati(rs.getBigDecimal("rata_interessi_maturati"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setImportoRimborsato(rs.getBigDecimal("importo_rimborsato"));
			bean.setDettPagImpVers(rs.getBigDecimal("dett_pag_imp_vers"));
			bean.setInteressiMaturati(rs.getBigDecimal("interessi_maturati"));
			bean.setRataPag(getResultSetLong(rs, "rata_pag"));
			bean.setIdPagamento(getResultSetLong(rs, "id_pagamento"));
			bean.setDataVersamentoDett(rs.getDate("data_versamento_dett"));
			bean.setGiorniRitardo(rs.getInt("giorni_ritardo"));
		}

		private Long getResultSetLong(ResultSet resultset, String colName) throws SQLException {
			long v = resultset.getLong(colName);
			if (resultset.wasNull()) {
				return null;
			}
			return Long.valueOf(v);
		}

	}

	public static class AvvisoSollecitoCheckSDResultRowMapper implements RowMapper<AvvisoSollecitoCheckSDResultDTO> {

		public AvvisoSollecitoCheckSDResultRowMapper() throws SQLException {
			// Instantiate class
		}

		@Override
		public AvvisoSollecitoCheckSDResultDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			AvvisoSollecitoCheckSDResultDTO bean = new AvvisoSollecitoCheckSDResultDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AvvisoSollecitoCheckSDResultDTO bean) throws SQLException {
			bean.setSoggettoTitolare(getResultSetLong(rs, "soggetto_titolare"));
			bean.setSdNapValorizzato(rs.getString("sd_nap_valorizzato"));
			bean.setSdNapNullo(rs.getString("sd_nap_nullo"));
			bean.setCodiciAvvisoAttivi(rs.getString("codici_avviso_attivi"));
			bean.setCodiciAvvisoNonAttivi(rs.getString("codici_avviso_non_attivi"));
		}

		private Long getResultSetLong(ResultSet resultset, String colName) throws SQLException {
			long v = resultset.getLong(colName);
			if (resultset.wasNull()) {
				return null;
			}
			return Long.valueOf(v);
		}

	}
	
	public static class AvvisoSollecitoCheckSoggettoRowMapper implements RowMapper<AvvisoSollecitoCheckSoggettoResultDTO> {

		public AvvisoSollecitoCheckSoggettoRowMapper() throws SQLException {
			// Instantiate class
		}

		@Override
		public AvvisoSollecitoCheckSoggettoResultDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			AvvisoSollecitoCheckSoggettoResultDTO bean = new AvvisoSollecitoCheckSoggettoResultDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AvvisoSollecitoCheckSoggettoResultDTO bean) throws SQLException {
			bean.setIdSoggetto(getResultSetLong(rs, "id_soggetto"));
			bean.setIdStatoDebitorio(getResultSetLong(rs,"id_stato_debitorio"));
			bean.setSdNap(rs.getString("sd_nap"));
			bean.setSdIdSoggettoTitolare(getResultSetLong(rs,"sd_id_soggetto_titolare"));
			bean.setPratIdSoggettoTitolare(getResultSetLong(rs,"prat_id_soggetto_titolare"));
			bean.setSoggCf(rs.getString("sogg_cf"));
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
