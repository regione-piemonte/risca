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
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioniBollDAO;
import it.csi.risca.riscabesrv.dto.RiscossioneBollDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class RiscossioniBollDAOImpl extends RiscaBeSrvGenericDAO<RiscossioneBollDTO> implements RiscossioniBollDAO {

	public static final String QUERY_SELECT_V_RISCOSSIONI_BS = "SELECT * FROM v_riscossioni_bollettazione_speciale vrbs, risca_d_stato_riscossione rdsr "
			+ "WHERE vrbs.id_stato_riscossione = rdsr.id_stato_riscossione " 
			+ "AND cod_ambito = :codAmbito " 
			+ "AND vrbs.flg_invio_speciale = :flgInvioSpeciale "
			+ "AND ( "
			+ "	((data_scad_concessione > to_date( :dtScadPagamento ) or data_scad_concessione is null ) "
			+ "		AND (rdsr.cod_stato_riscossione  =  'ATTIVA' "
			+ "		OR (rdsr.cod_stato_riscossione  =  'SOSPESA' AND to_date( :dtScadPagamento ) NOT BETWEEN vrbs.data_ini_sosp_canone AND vrbs .data_fine_sosp_canone))"
			+ "	) " + "	OR  " + "	( "
			+ "	(data_scad_concessione < to_date( :dtScadPagamento ) or data_scad_concessione is null ) "
			+ "	AND rdsr.cod_stato_riscossione not in ('RINUNCIATA', 'REVOCATA', 'ANNULLATA', 'RIGETTATA',  'SOSPESA')"
			+ "	AND EXISTS ( " + "		select rrp.id_tipo_provvedimento "
			+ "     from risca_r_provvedimento rrp, risca_d_tipo_provvedimento tp "
			+ "     where vrbs.id_riscossione = rrp.id_riscossione "
			+ "     and rrp.id_tipo_provvedimento = tp.id_tipo_provvedimento "
			+ "     and tp.cod_tipo_provvedimento IN ('IST_RINNOVO', 'IST_SANATORIA', 'AUT_PROVVISORIA')) " + "	) " + ") "
			+ " ORDER BY id_titolare, id_gruppo_soggetto, sum_imp_rimborso_riscossione desc, id_riscossione ";

	
	public static final String QUERY_SELECT_V_RISCOSSIONI_BO = "SELECT * FROM v_riscossioni_bollettazione_ordinaria vrbo, risca_d_stato_riscossione rdsr  "
			+ " WHERE vrbo.id_stato_riscossione = rdsr.id_stato_riscossione  "
			+ " AND cod_ambito = :codAmbito  "
			+ " AND (  "
			+ "	((coalesce(data_scad_concessione, to_date(:dtScadPagamento)) >= to_date('01/01/'|| :anno , 'DD/MM/YYYY'))  "
			+ "		AND (rdsr.cod_stato_riscossione  =  'ATTIVA' OR (rdsr.cod_stato_riscossione  =  'SOSPESA'  "
			+ "			AND to_date( :dtScadPagamento ) NOT BETWEEN vrbo.data_ini_sosp_canone AND vrbo .data_fine_sosp_canone)) "
			+ "	)  "
			+ "	OR  "
			+ "	(  "
			+ "		(coalesce(data_scad_concessione, to_date('01/01/'|| :anno , 'DD/MM/YYYY'))  < to_date('01/01/'|| :anno , 'DD/MM/YYYY'))  "
			+ "		AND (rdsr.cod_stato_riscossione not in ('RINUNCIATA', 'REVOCATA', 'ANNULLATA', 'RIGETTATA', 'SOSPESA') "
			+ "		OR (rdsr.cod_stato_riscossione = ('SCADUTA') AND to_char(data_scad_concessione,  'YYYY') = :anno)) "
			+ "	) "
			+ "	AND EXISTS ( select rrp.id_tipo_provvedimento  "
			+ "		     from risca_r_provvedimento rrp, risca_d_tipo_provvedimento tp  "
			+ "		     where vrbo.id_riscossione = rrp.id_riscossione  "
			+ "		     and rrp.id_tipo_provvedimento = tp.id_tipo_provvedimento  "
			+ "		     and tp.cod_tipo_provvedimento IN ('IST_RINNOVO', 'IST_SANATORIA', 'AUT_PROVVISORIA')) "
			+ " ) "
			// + " and vrbo.cod_riscossione in('TO11544', 'AL00856', 'AL12051') "
			+ " ORDER BY id_titolare, id_gruppo_soggetto, sum_imp_rimborso_riscossione desc, id_riscossione";
	
	/**
	 * {@inheritDoc}
	 * @throws Exception 
	 */
	@Override
	public List<RiscossioneBollDTO> loadRiscossioniBollettazioneSpeciale(String codAmbito, String dtScadPagamento, String tipoElabora) throws Exception {
		LOGGER.debug("[RiscossioniBollDAOImpl::loadRiscossioniBollettazioneSpeciale] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codAmbito", codAmbito);
			map.put("dtScadPagamento", dtScadPagamento);
			if (tipoElabora != null && tipoElabora.equalsIgnoreCase("BG")) {
				// Bollettazione Grande Idroelettrico
				map.put("flgInvioSpeciale", 2);
			} else {
				// Bollettazione Speciale
				map.put("flgInvioSpeciale", 1);
			}
			MapSqlParameterSource params = getParameterValue(map);

			// TODO Query valida per ambito AMBIENTE --> al momento non si conosce la logica
			// per altri ambiti
			return template.query(getQuery(QUERY_SELECT_V_RISCOSSIONI_BS, null, null), params, getExtendedRowMapper());
		} catch (SQLException e) {
			LOGGER.error(
					"[RiscossioniBollDAOImpl::loadRiscossioniBollettazioneSpeciale] Errore nell'esecuzione della query",
					e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioniBollDAOImpl::loadRiscossioniBollettazioneSpeciale] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RiscossioniBollDAOImpl::loadRiscossioniBollettazioneSpeciale] END");
		}
	}

	@Override
	public List<RiscossioneBollDTO> loadRiscossioniBollettazioneOrdinaria(String codAmbito, String dtScadPagamento,
			String anno) throws Exception {
		LOGGER.debug("[RiscossioniBollDAOImpl::loadRiscossioniBollettazioneOrdinaria] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codAmbito", codAmbito);
			map.put("dtScadPagamento", dtScadPagamento);
			map.put("anno", anno);
			MapSqlParameterSource params = getParameterValue(map);

			// TODO Query valida per ambito AMBIENTE --> al momento non si conosce la logica
			// per altri ambiti
			return template.query(getQuery(QUERY_SELECT_V_RISCOSSIONI_BO, null, null), params, getExtendedRowMapper());
		} catch (SQLException e) {
			LOGGER.error(
					"[RiscossioniBollDAOImpl::loadRiscossioniBollettazioneOrdinaria] Errore nell'esecuzione della query",
					e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (DataAccessException e) {
			LOGGER.error("[RiscossioniBollDAOImpl::loadRiscossioniBollettazioneOrdinaria] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RiscossioniBollDAOImpl::loadRiscossioniBollettazioneSpeciale] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<RiscossioneBollDTO> getRowMapper() throws SQLException {

		return new RiscossioneBollRowMapper();
	}

	@Override
	public RowMapper<RiscossioneBollDTO> getExtendedRowMapper() throws SQLException {
		return new RiscossioneBollRowMapper();
	}

	public static class RiscossioneBollRowMapper implements RowMapper<RiscossioneBollDTO> {

		public RiscossioneBollRowMapper() throws SQLException {
			// Instantiate class
		}

		@Override
		public RiscossioneBollDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			RiscossioneBollDTO bean = new RiscossioneBollDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RiscossioneBollDTO bean) throws SQLException {
			bean.setDatiRiscossione(rs.getString("dati_riscossione"));
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
			bean.setDatiTipoRiscossione(rs.getString("dati_tipo_riscossione"));
			bean.setCodTipoRiscossione(rs.getString("cod_tipo_riscossione"));
			bean.setDesTipoRiscossione(rs.getString("des_tipo_riscossione"));
			bean.setDatiStatoRiscossione(rs.getString("dati_stato_riscossione"));
			bean.setCodStatoRiscossione(rs.getString("cod_stato_riscossione"));
			bean.setDesStatoRiscossione(rs.getString("des_stato_riscossione"));
			bean.setDatiTipoAutorizza(rs.getString("dati_tipo_autorizza"));
			bean.setCodTipoAutorizza(rs.getString("cod_tipo_autorizza"));
			bean.setDesTipoAutorizza(rs.getString("des_tipo_autorizza"));
			bean.setDatiAmbito(rs.getString("dati_ambito"));
			bean.setCodAmbito(rs.getString("cod_ambito"));
			bean.setDesAmbito(rs.getString("des_ambito"));
			bean.setDatiIndirizzoPrincipale(rs.getString("dati_indirizzo_principale"));
			bean.setIdRiscossioneP(getResultSetLong(rs, "id_riscossione_p"));
			bean.setIdRecapitoP(getResultSetLong(rs, "id_recapito_p"));
			bean.setCodTipoRecapitoP(rs.getString("cod_tipo_recapito_p"));
			bean.setDesTipoRecapitoP(rs.getString("des_tipo_recapito_p"));
			bean.setCodTipoInvioP(rs.getString("cod_tipo_invio_p"));
			bean.setDesTipoInvioP(rs.getString("des_tipo_invio_p"));
			bean.setDatiIndirizzoAlternativo(rs.getString("dati_indirizzo_alternativo"));
			bean.setIdRiscossioneA(getResultSetLong(rs, "id_riscossione_a"));
			bean.setIdRecapitoA(getResultSetLong(rs, "id_recapito_a"));
			bean.setCodTipoRecapitoA(rs.getString("cod_tipo_recapito_a"));
			bean.setDesTipoRecapitoA(rs.getString("des_tipo_recapito_a"));
			bean.setCodTipoInvioA(rs.getString("cod_tipo_invio_a"));
			bean.setDesTipoInvioA(rs.getString("des_tipo_invio_a"));
			bean.setDatiRimborso(rs.getString("dati_rimborso"));
			bean.setSumImpRimborsoRiscossione(rs.getBigDecimal("sum_imp_rimborso_riscossione"));
			if(rsHasColumn(rs, "dati_stato_debitorio")) bean.setDatiStatoDebitorio(rs.getString("dati_stato_debitorio"));
			if(rsHasColumn(rs, "id_stato_debitorio")) bean.setIdStatoDebitorio(getResultSetLong(rs, "id_stato_debitorio"));
			if(rsHasColumn(rs, "desc_periodo_pagamento")) bean.setDescPeriodoPagamento(rs.getString("desc_periodo_pagamento"));
			bean.setStatoContribuzione(rs.getString("stato_contribuzione"));
			bean.setIdStatoContribuzione(getResultSetLong(rs, "id_stato_contribuzione"));
			bean.setIdTitolare(rs.getString("id_titolare"));
		}
		
		private boolean rsHasColumn(ResultSet rs, String column){
		    try{
		        rs.findColumn(column);
		        return true;
		    } catch (SQLException sqlex){
		        //Column not present in resultset
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
