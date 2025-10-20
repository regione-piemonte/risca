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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.PagNonPropriExtendedDTO;
import it.csi.risca.riscabesrv.dto.PagamentoDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoImpNonPropriDTO;
import it.csi.risca.riscabesrv.dto.TipoModalitaPagDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class PagamentoDAOImpl extends RiscaBeSrvGenericDAO<PagamentoDTO> implements PagamentoDAO {

	public static final String QUERY_PAGAMENTO_BY_STATO_DEBITORIO = "select rtp.* from risca_t_pagamento rtp \n"
			+ "where rtp.data_op_val = (select max(rtp.data_op_val) from risca_t_pagamento rtp\n"
			+ "inner join risca_r_dettaglio_pag rrdp on rtp.id_pagamento = rrdp.id_pagamento\n"
			+ "inner join risca_r_rata_sd rrrsd on rrdp.id_rata_sd = rrrsd.id_rata_sd\n"
			+ "inner join risca_t_stato_debitorio rtsd on rrrsd.id_stato_debitorio = rtsd.id_stato_debitorio\n"
			+ "where rtsd.id_stato_debitorio = :idStatoDebitorio and rrrsd.id_rata_sd_padre is null ) ";

	public static final String QUERY_PAGAMENTO_BY_ID_RISCOSSIONE = "select rtp.*, rtsd.*, rrrsd.*, rdtmp.*, rda.* from risca_t_pagamento rtp "
			+ "inner join risca_r_dettaglio_pag rrdp on rtp.id_pagamento = rrdp.id_pagamento "
			+ "inner join risca_r_rata_sd rrrsd on rrdp.id_rata_sd = rrrsd.id_rata_sd "
			+ "inner join risca_t_stato_debitorio rtsd on rrrsd.id_stato_debitorio = rtsd.id_stato_debitorio "
			+ "inner join risca_d_tipo_modalita_pag rdtmp on rtp.id_tipo_modalita_pag = rdtmp.id_tipo_modalita_pag "
			+ "inner  join risca_d_ambito rda on pag.id_ambito = rda.id_ambito "
			+ "where rtsd.id_riscossione = :idRiscossione ";

	public static final String QUERY_DETTAGLIO_LIST_BY_PAGAMENTO = "select rrdp.* from risca_r_dettaglio_pag rrdp "
			+ "where rrdp.id_pagamento = :idPagamento ";

	public static final String QUERY_PAG_NON_PROPRI_LIST_BY_PAGAMENTO = "select rrpnp.*, rdtinp.* from risca_r_pag_non_propri rrpnp "
			+ "inner join risca_d_tipo_imp_non_propri rdtinp on rrpnp.id_tipo_imp_non_propri = rdtinp.id_tipo_imp_non_propri "
			+ "where rrpnp.id_pagamento = :idPagamento ";

	public static final String QUERY_INSERT_PAGAMENTO = "INSERT INTO risca_t_pagamento (id_pagamento, "
			+ "id_ambito, id_tipologia_pag, id_tipo_modalita_pag, "
			+ "id_file_poste, id_immagine, causale, data_op_val," + "importo_versato, data_download, quinto_campo, cro,"
			+ " note, numero_pagamento, soggetto_versamento, indirizzo_versamento,"
			+ " civico_versamento, frazione_versamento, comune_versamento,"
			+ " cap_versamento, prov_versamento, flg_rimborsato, imp_da_assegnare,"
			+ " codice_avviso,id_file_soris, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid)"
			+ "VALUES(:idPagamento, :idAmbito, :idTipologiaPag, :idTipoModalitaPag, :idFilePoste, :idImmagine, :causale, :dataOpVal,"
			+ " :importoVersato, :dataDownload, :quintoCampo, :cro,"
			+ " :note, :numeroPagamento, :soggettoVersamento, :indirizzoVersamento, :civicoVersamento, :frazioneVersamento,"
			+ " :comuneVersamento, :capVersamento, :provVersamento, :flgRimborsato,"
			+ " :impDaAssegnare, :codiceAvviso, :idFileSoris, :gestDataIns, :gestAttoreIns, "
			+ " :gestDataUpd, :gestAttoreUpd, :gestUid) ";

	public static final String QUERY_UPDATE_PAGAMENTO = "UPDATE risca_t_pagamento SET id_ambito = :idAmbito, id_tipologia_pag= :idTipologiaPag, "
			+ "id_tipo_modalita_pag= :idTipoModalitaPag, id_file_poste= :idFilePoste, "
			+ "id_immagine=:idImmagine, causale=:causale, data_op_val=:dataOpVal, "
			+ "importo_versato=:importoVersato, data_download=:dataDownload, quinto_campo=:quintoCampo,"
			+ " cro=:cro, note=:note, numero_pagamento=:numeroPagamento, soggetto_versamento=:soggettoVersamento,"
			+ " indirizzo_versamento=:indirizzoVersamento, civico_versamento=:civicoVersamento,"
			+ " frazione_versamento=:frazioneVersamento, comune_versamento=:comuneVersamento, cap_versamento=:capVersamento,"
			+ " prov_versamento=:provVersamento, flg_rimborsato=:flgRimborsato, imp_da_assegnare=:impDaAssegnare, codice_avviso=:codiceAvviso,"
			+ " gest_data_upd=:gestDataUpd,"
			+ " gest_attore_upd=:gestAttoreUpd, gest_uid=:gestUid WHERE id_pagamento= :idPagamento";

	public static final String QUERY_DELETE_PAGAMENTO = "DELETE from risca_t_pagamento WHERE id_pagamento = :idPagamento";

	public static final String QUERY_DELETE_DETTAGLIO_PAG = "DELETE from risca_r_dettaglio_pag WHERE id_pagamento = :idPagamento";

	public static final String QUERY_DELETE_PAG_NON_PROPRI = "DELETE from risca_r_pag_non_propri WHERE id_pagamento = :idPagamento";

	public static final String QUERY_PAGAMENTO_BY_ID_STATO_DEBITORIO = "select rtp.*, rtsd.*, rrrsd.*, rdtmp.* , rda.* from risca_t_pagamento rtp "
			+ "inner join risca_r_dettaglio_pag rrdp on rtp.id_pagamento = rrdp.id_pagamento "
			+ "inner join risca_r_rata_sd rrrsd on rrdp.id_rata_sd = rrrsd.id_rata_sd "
			+ "inner join risca_t_stato_debitorio rtsd on rrrsd.id_stato_debitorio = rtsd.id_stato_debitorio "
			+ "inner join risca_d_tipo_modalita_pag rdtmp on rtp.id_tipo_modalita_pag = rdtmp.id_tipo_modalita_pag "
			+ "inner  join risca_d_ambito rda on rtp.id_ambito = rda.id_ambito "
			+ "where rtsd.id_stato_debitorio = :idStatoDebitorio " + "and rrrsd.id_rata_sd_padre is null ";

	public static final String QUERY_PAGAMENTO_BY_ID_PAGAMENTO = "select rtp.*, rdtmp.*, rda.* from risca_t_pagamento rtp "
			+ "inner join risca_d_tipo_modalita_pag rdtmp on rtp.id_tipo_modalita_pag = rdtmp.id_tipo_modalita_pag "
			+ "inner  join risca_d_ambito rda on rtp.id_ambito = rda.id_ambito "
			+ "where rtp.id_pagamento = :idPagamento ";

	public static final String QUERY_PAGAMENTO_BY_CODICE_AVVISO = "select * from risca_t_pagamento "
			+ "where codice_avviso = :codiceAvviso ";

	public static final String QUERY_PAGAMENTO_BY_QUINTO_CAMPO = "select * from risca_t_pagamento "
			+ "where quinto_campo  = :quintoCampo  ";

	public static final String QUERY_PAGAMENTO_FOR_POSTE_ESTRCO = " select * from risca_t_pagamento "
			+ " where data_op_val = to_date(:dataValuta,'ddmmyy') " + " and importo_versato = :importoMovimento "
			+ " and causale = :descrizioneMovimento " + " and id_tipo_modalita_pag = :idTipoModalitaPag ";
	public static final String CONDITION_CRO_FOR_POSTE_ESTRCO = " and cro = :riferimentoBanca ";

	public static final String QUERY_PAGAMENTI_DA_VISIONARE = "SELECT pag.* ,rdtmp.* , rda.*"
			+ "    FROM risca_t_pagamento pag " + " left join (" + "           SELECT dpa.id_pagamento "
			+ "             FROM risca_r_dettaglio_pag dpa "
			+ "          ) as det on pag.id_pagamento = det.id_pagamento " + " left join ( "
			+ "          SELECT pnp.id_pagamento " + "            FROM risca_r_pag_non_propri pnp "
			+ "          ) as pno on pag.id_pagamento = pno.id_pagamento "
			+ "inner join risca_d_tipo_modalita_pag rdtmp on pag.id_tipo_modalita_pag = rdtmp.id_tipo_modalita_pag "
			+ "inner  join risca_d_ambito rda on pag.id_ambito = rda.id_ambito " + " where det.id_pagamento is null "
			+ "  and pno.id_pagamento is null " + "   order by id_pagamento ";

	public static final String QUERY_COUNT_PAGAMENTI_DA_VISIONARE = " select COUNT(*) from ( "
			+ QUERY_PAGAMENTI_DA_VISIONARE + " ) as TOT ";

	// conta quanti pagamenti sono stati effettuati ed estrae la data del piu'
	// recente
	public static final String QUERY_MAX_DATA_PAG = " select " + "case " + "	when conta_pagamenti = 0 then null "
			+ "	when conta_pagamenti = 1 then max_data_op_val " + "	else max_data_op_val || '*' " + "end "
			+ "as max_data_op_val " + "from " + "(  "
			+ "select COUNT(*) conta_pagamenti, TO_CHAR(MAX(x.data_op_val),'DD/MM/YYYY') max_data_op_val "
			+ "from RISCA_T_PAGAMENTO x, RISCA_R_DETTAGLIO_PAG a, RISCA_R_RATA_SD b "
			+ "where x.id_pagamento = a.id_pagamento " + "    and a.id_rata_sd = b.id_rata_sd "
			+ "    and b.id_stato_debitorio = :idStatoDebitorio " + ") as contapag ";

	public static final String QUERY_ID_PAGAMENTO_CON_DETTAGLIO = "select distinct rtp.id_pagamento, rtp.data_op_val, rtp.importo_versato from risca_t_pagamento rtp "
			+ " inner join risca_r_dettaglio_pag rrdp  on rtp.id_pagamento = rrdp.id_pagamento ";
	public static final String CONDITION_LISTA_ID = " where rtp.id_pagamento in (:listaIdPagamento) ";
	public static final String CONDITION_ANNO = " where extract (year from rtp.data_op_val) = :anno ";
	public static final String CONDITION_DATA_RIF = " where rtp.gest_data_upd >= to_date(:dataRif) ";
	
	//TODO aggiungere la condizione id_file_soris != :idFileSoris
	public static final String QUERY_COUNT_ESISTENZA_PAGAMENTO_PER_SORIS = " select id_pagamento from risca_t_pagamento where id_tipo_modalita_pag = 12 and  "
			+ " note LIKE 'Ingiunzione SORIS%'  and data_op_val = to_date(:dataEvento) and importo_versato = :sumImportoCaricoRisco and id_file_soris != :idFileSoris ";
	
	public static final String QUERY_COUNT_ESISTENZA_PAGAMENTO_DA_ANNULLARE_PER_SORIS = " select COUNT(*) from risca_t_pagamento p, risca_r_dettaglio_pag rrdp  where p.id_pagamento = rrdp.id_pagamento  and p.id_pagamento = :idPagamento and  "
			+ "  p.data_op_val = to_date(:dataEvento, 'yyyy-MM-dd') ";
	
	public static final String ORDER_BY_DATA_IMPORTO = " order by rtp.data_op_val, rtp.importo_versato desc ";
	
	public static final String QUERY_UPDATE_ANNULLAMENTO_PAGAMENTO_SORIS =
	 " UPDATE risca_t_pagamento set  id_file_soris = :idFileSoris, "
		+"	  note = (select 'Pagamento di '||rtp.importo_versato || ' annullato da SORIS con il flusso di ' ||( select to_char(data_file, 'YYYY-MM') as dataFile from risca_t_file_soris rtfs where rtfs.id_file_soris =:idFileSoris) "
		+"	  from risca_t_pagamento rtp where id_pagamento = :idPagamento)	, importo_versato = 0,	"
		+"				gest_data_upd=:gestDataUpd, gest_attore_upd=:gestAttoreUpd, gest_uid=:gestUid "
		+"				WHERE id_pagamento= :idPagamento ";

	@Override
	public PagamentoDTO getPagamentoWithMaxDataOpVal(Long idStatoDebitorio) throws SQLException {
		LOGGER.debug("[PagamentoDAOImpl::getPagamentoWithMaxDataOpVal] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);

			return template.queryForObject(getQuery(QUERY_PAGAMENTO_BY_STATO_DEBITORIO, null, null), params,
					getRowMapper());

		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[PagamentoDAOImpl::getPagamentoWithMaxDataOpVal] No record found in database for idStatoDebitorio "
							+ idStatoDebitorio,
					e);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentoWithMaxDataOpVal] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentoWithMaxDataOpVal] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::getPagamentoWithMaxDataOpVal] END");
		}
	}

	@Override
	public List<PagamentoExtendedDTO> getPagamentoByIdRiscossione(Long idRiscossione) throws SQLException {
		LOGGER.debug("[PagamentoDAOImpl::getPagamentoByIdRiscossione] BEGIN");

		List<PagamentoExtendedDTO> pagamentoList = new ArrayList<PagamentoExtendedDTO>();
		List<DettaglioPagDTO> dettaglioPagList = new ArrayList<DettaglioPagDTO>();
		List<PagNonPropriExtendedDTO> pagNonPropriList = new ArrayList<PagNonPropriExtendedDTO>();

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRiscossione", idRiscossione);
			MapSqlParameterSource params = getParameterValue(map);

			pagamentoList = template.query(getQuery(QUERY_PAGAMENTO_BY_ID_RISCOSSIONE, null, null), params,
					getExtendedRowMapper());

			if (pagamentoList != null && pagamentoList.size() > 0) {
				for (PagamentoExtendedDTO pagamento : pagamentoList) {

					map.put("idPagamento", pagamento.getIdPagamento());
					params = getParameterValue(map);

					dettaglioPagList = template.query(getQuery(QUERY_DETTAGLIO_LIST_BY_PAGAMENTO, null, null), params,
							getDettaglioPagRowMapper());

					if (dettaglioPagList != null && dettaglioPagList.size() > 0)
						pagamento.setDettaglioPag(dettaglioPagList);

					pagNonPropriList = template.query(getQuery(QUERY_PAG_NON_PROPRI_LIST_BY_PAGAMENTO, null, null),
							params, getPagNonPropriRowMapper());

					if (pagNonPropriList != null && pagNonPropriList.size() > 0)
						pagamento.setPagNonPropri(pagNonPropriList);
				}

			}

		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[PagamentoDAOImpl::getPagamentoByIdRiscossione] No record found in database for idRiscossione "
							+ idRiscossione,
					e);
			return Collections.emptyList();
		} catch (SQLException e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentoByIdRiscossione] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentoByIdRiscossione] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::getPagamentoByIdRiscossione] END");
		}
		return pagamentoList;
	}

	@Override
	public PagamentoExtendedDTO savePagamento(PagamentoExtendedDTO dto, String fruitore) throws SQLException {
		LOGGER.debug("[PagamentoDAOImpl::savePagamento] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_t_pagamento");
			map.put("idPagamento", genId);
			map.put("idAmbito", dto.getAmbito() != null ? dto.getAmbito().getIdAmbito() : dto.getIdAmbito());
			if (fruitore == null) {
				// id tipologia sara sempre manuale
				map.put("idTipologiaPag", Constants.ID_TIPOLOGIA_PAGAMENTO_MANUALE);
				dto.setIdTipologiaPag(Constants.ID_TIPOLOGIA_PAGAMENTO_MANUALE);
				// id tipo modalita pagamento sara sempre manuale
				map.put("idTipoModalitaPag", Constants.ID_TIPO_MODALITA_PAGAMENTO_MANUALE);
				dto.setIdTipoModalitaPag(Constants.ID_TIPO_MODALITA_PAGAMENTO_MANUALE);
//				sara sempre manuale per il FE
				map.put("flgRimborsato", Constants.FLAG_RIMBORSATO);
				dto.setFlgRimborsato(Constants.FLAG_RIMBORSATO);

			} else {
				map.put("idTipologiaPag", dto.getIdTipologiaPag());
				map.put("idTipoModalitaPag",
						dto.getTipoModalitaPag() != null ? dto.getTipoModalitaPag().getIdTipoModalitaPag()
								: dto.getIdTipoModalitaPag());
				map.put("flgRimborsato", dto.getFlgRimborsato());
			}
			map.put("idFilePoste", dto.getIdFilePoste());
			map.put("idImmagine", dto.getIdImmagine());
			map.put("causale", dto.getCausale());
			map.put("dataOpVal", dto.getDataOpVal());
			map.put("importoVersato", dto.getImportoVersato());
			map.put("dataDownload", dto.getDataDownload());
			map.put("quintoCampo", dto.getQuintoCampo());
			map.put("cro", dto.getCro());
			map.put("note", dto.getNote());
			map.put("numeroPagamento", dto.getNumeroPagamento());
			map.put("soggettoVersamento", dto.getSoggettoVersamento());
			map.put("indirizzoVersamento", dto.getIndirizzoVersamento());
			map.put("civicoVersamento", dto.getCivicoVersamento());
			map.put("frazioneVersamento", dto.getFrazioneVersamento());
			map.put("comuneVersamento", dto.getComuneVersamento());
			map.put("capVersamento", dto.getCapVersamento());
			map.put("provVersamento", dto.getProvVersamento());

			map.put("impDaAssegnare", dto.getImpDaAssegnare());
			map.put("codiceAvviso", dto.getCodiceAvviso());
			map.put("idFileSoris", dto.getIdFileSoris());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_INSERT_PAGAMENTO;

			template.update(getQuery(query, null, null), params, keyHolder);

			dto.setIdPagamento(genId);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			return dto;

		} catch (SQLException e) {
			LOGGER.error("[PagamentoDAOImpl::savePagamento] Errore nell'esecuzione della query", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::savePagamento] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::savePagamento] END");
		}
	}

	@Override
	public Long deleteByIdPagamento(Long idPagamento) {
		LOGGER.debug("[PagamentoDAOImpl::deleteByIdPagamento] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idPagamento", idPagamento);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String queryPagamento = QUERY_DELETE_PAGAMENTO;
			String queryDetPag = QUERY_DELETE_DETTAGLIO_PAG;
			String queryPagNonPropri = QUERY_DELETE_PAG_NON_PROPRI;

			template.update(getQuery(queryDetPag, null, null), params, keyHolder);

			template.update(getQuery(queryPagNonPropri, null, null), params, keyHolder);

			template.update(getQuery(queryPagamento, null, null), params, keyHolder);

			return idPagamento;

		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::deleteByIdPagamento] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::deleteByIdPagamento] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<PagamentoDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new PagamentoRowMapper();
	}

	@Override
	public RowMapper<PagamentoExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new PagamentoExtendedRowMapper();
	}

	public RowMapper<DettaglioPagDTO> getDettaglioPagRowMapper() throws SQLException {
		return new DettaglioPagRowMapper();
	}

	public RowMapper<DettaglioPagDTO> getDettaglioPagExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new DettaglioPagRowMapper();
	}

	public RowMapper<PagNonPropriExtendedDTO> getPagNonPropriRowMapper() throws SQLException {
		return new PagNonPropriRowMapper();
	}

	public RowMapper<PagNonPropriExtendedDTO> getPagNonPropriExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new PagNonPropriRowMapper();
	}

	public static class PagamentoRowMapper implements RowMapper<PagamentoDTO> {

		public PagamentoRowMapper() throws SQLException {
		}

		@Override
		public PagamentoDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			PagamentoDTO bean = new PagamentoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, PagamentoDTO bean) throws SQLException {
			bean.setIdPagamento(rs.getLong("id_pagamento"));
			bean.setIdAmbito(rs.getLong("id_ambito"));
			bean.setIdTipologiaPag(rs.getLong("id_tipologia_pag"));
			bean.setIdTipoModalitaPag(rs.getLong("id_tipo_modalita_pag"));
			if (rs.getLong("id_file_poste") > 0)
				bean.setIdFilePoste(rs.getLong("id_file_poste"));
			if (rs.getLong("id_immagine") > 0)
				bean.setIdImmagine(rs.getLong("id_immagine"));
			bean.setCausale(rs.getString("causale"));
			bean.setDataOpVal(rs.getDate("data_op_val"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setDataDownload(rs.getDate("data_download"));
			bean.setQuintoCampo(rs.getString("quinto_campo"));
			bean.setCro(rs.getString("cro"));
			bean.setNote(rs.getString("note"));
			bean.setNumeroPagamento(rs.getLong("numero_pagamento"));
			bean.setSoggettoVersamento(rs.getString("soggetto_versamento"));
			bean.setIndirizzoVersamento(rs.getString("indirizzo_versamento"));
			bean.setCivicoVersamento(rs.getString("civico_versamento"));
			bean.setFrazioneVersamento(rs.getString("frazione_versamento"));
			bean.setComuneVersamento(rs.getString("comune_versamento"));
			bean.setCapVersamento(rs.getString("cap_versamento"));
			bean.setProvVersamento(rs.getString("prov_versamento"));
			bean.setFlgRimborsato(rs.getInt("flg_rimborsato"));
			bean.setImpDaAssegnare(rs.getBigDecimal("imp_da_assegnare"));
			bean.setCodiceAvviso(rs.getString("codice_avviso"));
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

	}

	public static class PagamentoExtendedRowMapper implements RowMapper<PagamentoExtendedDTO> {

		public PagamentoExtendedRowMapper() throws SQLException {
		}

		@Override
		public PagamentoExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			PagamentoExtendedDTO bean = new PagamentoExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, PagamentoExtendedDTO bean) throws SQLException {
			bean.setIdPagamento(rs.getLong("id_pagamento"));
			AmbitoDTO ambito = new AmbitoDTO();
			populateBeanAmbito(rs, ambito);
			bean.setAmbito(ambito);
			bean.setIdTipologiaPag(rs.getLong("id_tipologia_pag"));
			bean.setIdTipoModalitaPag(rs.getLong("id_tipo_modalita_pag"));
			if (rs.getLong("id_file_poste") > 0)
				bean.setIdFilePoste(rs.getLong("id_file_poste"));
			if (rs.getLong("id_immagine") > 0)
				bean.setIdImmagine(rs.getLong("id_immagine"));
			bean.setCausale(rs.getString("causale"));
			bean.setDataOpVal(rs.getDate("data_op_val"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setDataDownload(rs.getDate("data_download"));
			bean.setQuintoCampo(rs.getString("quinto_campo"));
			bean.setCro(rs.getString("cro"));
			bean.setNote(rs.getString("note"));
			bean.setNumeroPagamento(rs.getLong("numero_pagamento"));
			bean.setSoggettoVersamento(rs.getString("soggetto_versamento"));
			bean.setIndirizzoVersamento(rs.getString("indirizzo_versamento"));
			bean.setCivicoVersamento(rs.getString("civico_versamento"));
			bean.setFrazioneVersamento(rs.getString("frazione_versamento"));
			bean.setComuneVersamento(rs.getString("comune_versamento"));
			bean.setCapVersamento(rs.getString("cap_versamento"));
			bean.setProvVersamento(rs.getString("prov_versamento"));
			bean.setFlgRimborsato(rs.getInt("flg_rimborsato"));
			bean.setImpDaAssegnare(rs.getBigDecimal("imp_da_assegnare"));
			bean.setCodiceAvviso(rs.getString("codice_avviso"));

			TipoModalitaPagDTO tipoModalitaPag = new TipoModalitaPagDTO();
			populateBeanTipoModalitaPag(rs, tipoModalitaPag);
			bean.setTipoModalitaPag(tipoModalitaPag);

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

		private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
			bean.setIdAmbito(rs.getLong("id_ambito"));
			bean.setCodAmbito(rs.getString("cod_ambito"));
			bean.setDesAmbito(rs.getString("des_ambito"));

		}

		private void populateBeanTipoModalitaPag(ResultSet rs, TipoModalitaPagDTO bean) throws SQLException {
			bean.setIdTipoModalitaPag(rs.getLong("id_tipo_modalita_pag"));
			bean.setCodModalitaPag(rs.getString("cod_tipo_modalita_pag"));
			bean.setDesModalitaPag(rs.getString("des_tipo_modalita_pag"));

		}

//		@Override
//		public StatoDebitorioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
//			StatoDebitorioDTO bean = new StatoDebitorioDTO();
//			populateBean(rs, bean);
//			return bean;
//		}

		private void populateBeanStatoDebitorio(ResultSet rs, StatoDebitorioExtendedDTO bean) throws SQLException {
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setTipoTitolo(rs.getString("des_tipo_titolo"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));

			bean.setIdRecapito(rs.getLong("id_recapito"));
			bean.setIdAttivitaStatoDeb(rs.getLong("id_attivita_stato_deb"));
			bean.setIdStatoContribuzione(rs.getLong("id_stato_contribuzione"));
			bean.setNotaRinnovo(rs.getString("nota_rinnovo"));

			bean.setIdTipoDilazione(rs.getLong("id_tipo_dilazione"));
			bean.setNap(rs.getString("nap"));
			bean.setNumTitolo(rs.getString("num_titolo"));
			bean.setDataProvvedimento(rs.getDate("data_provvedimento"));
			bean.setNumRichiestaProtocollo(rs.getString("num_richiesta_protocollo"));
			bean.setDataRichiestaProtocollo(rs.getDate("data_richiesta_protocollo"));
			bean.setDataUltimaModifica(rs.getDate("data_ultima_modifica"));
			bean.setDesUsi(rs.getString("des_usi"));
			bean.setNote(rs.getString("note"));
			bean.setImpRecuperoCanone(rs.getBigDecimal("imp_recupero_canone"));
			bean.setImpRecuperoInteressi(rs.getBigDecimal("imp_recupero_interessi"));
			bean.setImpSpeseNotifica(rs.getBigDecimal("imp_spese_notifica"));
			bean.setImpCompensazioneCanone(rs.getBigDecimal("imp_compensazione_canone"));
			bean.setDescPeriodoPagamento(rs.getString("desc_periodo_pagamento"));
			bean.setDescMotivoAnnullo(rs.getString("desc_motivo_annullo"));
			bean.setFlgAnnullato(rs.getInt("flg_annullato"));
			bean.setFlgRestituitoMittente(rs.getInt("flg_restituito_mittente"));
			bean.setFlgInvioSpeciale(rs.getInt("flg_invio_speciale"));
			bean.setFlgDilazione(rs.getInt("flg_dilazione"));
			bean.setFlgAddebitoAnnoSuccessivo(rs.getInt("flg_addebito_anno_successivo"));

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

		private void populateBeanRataSd(ResultSet rs, RataSdDTO bean) throws SQLException {
			bean.setIdRataSd(rs.getLong("id_rata_sd"));
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			if (rs.getLong("id_rata_sd_padre") > 0)
				bean.setIdRataSdPadre(rs.getLong("id_rata_sd_padre"));
			bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
			bean.setInteressiMaturati(rs.getBigDecimal("interessi_maturati"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if (rs.getDate("data_scadenza_pagamento") != null)
				bean.setDataScadenzaPagamento(formatter.format(rs.getDate("data_scadenza_pagamento")));

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

	}

	public static class DettaglioPagRowMapper implements RowMapper<DettaglioPagDTO> {

		public DettaglioPagRowMapper() throws SQLException {
		}

		@Override
		public DettaglioPagDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			DettaglioPagDTO bean = new DettaglioPagDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, DettaglioPagDTO bean) throws SQLException {
			bean.setIdPagamento(rs.getLong("id_pagamento"));
			bean.setIdRataSd(rs.getLong("id_rata_sd"));
			bean.setIdDettaglioPag(rs.getLong("id_dettaglio_pag"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setInteressiMaturati(rs.getBigDecimal("interessi_maturati"));
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

	}

	public static class PagNonPropriRowMapper implements RowMapper<PagNonPropriExtendedDTO> {

		public PagNonPropriRowMapper() throws SQLException {
		}

		@Override
		public PagNonPropriExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			PagNonPropriExtendedDTO bean = new PagNonPropriExtendedDTO();
			populateBeanPagNonPropri(rs, bean);
			return bean;
		}

		private void populateBeanPagNonPropri(ResultSet rs, PagNonPropriExtendedDTO bean) throws SQLException {

			bean.setIdPagamento(rs.getLong("id_pagamento"));
			bean.setIdPagNonPropri(rs.getLong("id_pag_non_propri"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));

			TipoImpNonPropriDTO tipoImpNonPropri = new TipoImpNonPropriDTO();
			populateBeanTipoImpNonPropri(rs, tipoImpNonPropri);
			bean.setTipoImpNonPropri(tipoImpNonPropri);

		}

		private void populateBeanTipoImpNonPropri(ResultSet rs, TipoImpNonPropriDTO bean) throws SQLException {

			bean.setIdTipoImpNonPropri(rs.getLong("id_tipo_imp_non_propri"));
			bean.setCodTipoImpNonPropri(rs.getString("cod_tipo_imp_non_propri"));
			bean.setDesTipoImpNonPropri(rs.getString("des_tipo_imp_non_propri"));
		}

	}

	@Override
	public PagamentoExtendedDTO updatePagamento(PagamentoExtendedDTO dto, String fruitore) {
		LOGGER.debug("[PagamentoDAOImpl::updatePagamento] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idPagamento", dto.getIdPagamento());
			map.put("idAmbito", dto.getAmbito() != null ? dto.getAmbito().getIdAmbito() : dto.getIdAmbito());
			if (fruitore == null) {
				// id tipologia sara sempre manuale
				map.put("idTipologiaPag", dto.getIdTipologiaPag() != null ? dto.getIdTipologiaPag()
						: Constants.ID_TIPOLOGIA_PAGAMENTO_MANUALE);
				// id tipo modalita pagamento sara sempre manuale
				map.put("idTipoModalitaPag",
						(dto.getTipoModalitaPag() != null && dto.getTipoModalitaPag().getIdTipoModalitaPag() > 0)
								? dto.getTipoModalitaPag().getIdTipoModalitaPag()
								:

								Constants.ID_TIPO_MODALITA_PAGAMENTO_MANUALE);
//				sara sempre manuale per il FE
				map.put("flgRimborsato",
						dto.getFlgRimborsato() != null ? dto.getFlgRimborsato() : Constants.FLAG_RIMBORSATO);
			} else {
				map.put("idTipologiaPag", dto.getIdTipologiaPag());
				map.put("idTipoModalitaPag",
						dto.getTipoModalitaPag() != null ? dto.getTipoModalitaPag().getIdTipoModalitaPag()
								: dto.getIdTipoModalitaPag());
				map.put("flgRimborsato", dto.getFlgRimborsato());
			}

			map.put("idFilePoste", dto.getIdFilePoste());
			map.put("idImmagine", dto.getIdImmagine());
			map.put("causale", dto.getCausale());
			map.put("dataOpVal", dto.getDataOpVal());
			map.put("importoVersato", dto.getImportoVersato());
			map.put("dataDownload", dto.getDataDownload());
			map.put("quintoCampo", dto.getQuintoCampo());
			map.put("cro", dto.getCro());
			map.put("note", dto.getNote());
			map.put("numeroPagamento", dto.getNumeroPagamento());
			map.put("soggettoVersamento", dto.getSoggettoVersamento());
			map.put("indirizzoVersamento", dto.getIndirizzoVersamento());
			map.put("civicoVersamento", dto.getCivicoVersamento());
			map.put("frazioneVersamento", dto.getFrazioneVersamento());
			map.put("comuneVersamento", dto.getComuneVersamento());
			map.put("capVersamento", dto.getCapVersamento());
			map.put("provVersamento", dto.getProvVersamento());
			map.put("impDaAssegnare", dto.getImpDaAssegnare());
			map.put("codiceAvviso", dto.getCodiceAvviso());
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_UPDATE_PAGAMENTO;

			template.update(getQuery(query, null, null), params, keyHolder);

			dto.setGestDataUpd(now);
			return dto;

		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::updatePagamento] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::updatePagamento] END");
		}
	}

	@Override
	public List<PagamentoExtendedDTO> getPagamentiByIdStatoDebitorio(Long idStatoDebitorio) throws SQLException {
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiByIdStatoDebitorio] BEGIN");

		List<PagamentoExtendedDTO> pagamentoList = new ArrayList<PagamentoExtendedDTO>();
		List<DettaglioPagDTO> dettaglioPagList = new ArrayList<DettaglioPagDTO>();
		List<PagNonPropriExtendedDTO> pagNonPropriList = new ArrayList<PagNonPropriExtendedDTO>();

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);

			pagamentoList = template.query(getQuery(QUERY_PAGAMENTO_BY_ID_STATO_DEBITORIO, null, null), params,
					getExtendedRowMapper());

			if (pagamentoList != null && pagamentoList.size() > 0) {
				for (PagamentoExtendedDTO pagamento : pagamentoList) {

					map.put("idPagamento", pagamento.getIdPagamento());
					params = getParameterValue(map);

					dettaglioPagList = template.query(getQuery(QUERY_DETTAGLIO_LIST_BY_PAGAMENTO, null, null), params,
							getDettaglioPagRowMapper());

					if (dettaglioPagList != null && dettaglioPagList.size() > 0)
						pagamento.setDettaglioPag(dettaglioPagList);

					pagNonPropriList = template.query(getQuery(QUERY_PAG_NON_PROPRI_LIST_BY_PAGAMENTO, null, null),
							params, getPagNonPropriRowMapper());

					if (pagNonPropriList != null && pagNonPropriList.size() > 0)
						pagamento.setPagNonPropri(pagNonPropriList);
				}

			}

		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[PagamentoDAOImpl::getPagamentiByIdStatoDebitorio] No record found in database for idStatoDebitorio "
							+ idStatoDebitorio,
					e);
			return Collections.emptyList();
		} catch (SQLException e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentiByIdStatoDebitorio] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentiByIdStatoDebitorio] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::getPagamentiByIdStatoDebitorio] END");
		}
		return pagamentoList;
	}

	@Override
	public PagamentoExtendedDTO getPagamentoByIdPagamento(Long idPagamento) throws SQLException {
		LOGGER.debug("[PagamentoDAOImpl::getPagamentoByIdPagamento] BEGIN");

		PagamentoExtendedDTO pagamento = new PagamentoExtendedDTO();
		List<DettaglioPagDTO> dettaglioPagList = new ArrayList<DettaglioPagDTO>();
		List<PagNonPropriExtendedDTO> pagNonPropriList = new ArrayList<PagNonPropriExtendedDTO>();

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idPagamento", idPagamento);
			MapSqlParameterSource params = getParameterValue(map);

			pagamento = template.queryForObject(getQuery(QUERY_PAGAMENTO_BY_ID_PAGAMENTO, null, null), params,
					getExtendedRowMapper());

			if (pagamento != null) {
				map.put("idPagamento", pagamento.getIdPagamento());
				params = getParameterValue(map);

				dettaglioPagList = template.query(getQuery(QUERY_DETTAGLIO_LIST_BY_PAGAMENTO, null, null), params,
						getDettaglioPagRowMapper());

				if (dettaglioPagList != null && dettaglioPagList.size() > 0)
					pagamento.setDettaglioPag(dettaglioPagList);

				pagNonPropriList = template.query(getQuery(QUERY_PAG_NON_PROPRI_LIST_BY_PAGAMENTO, null, null), params,
						getPagNonPropriRowMapper());

				if (pagNonPropriList != null && pagNonPropriList.size() > 0)
					pagamento.setPagNonPropri(pagNonPropriList);
			}

		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[PagamentoDAOImpl::getPagamentoByIdPagamento] No record found in database for idPagamento "
					+ idPagamento, e);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentoByIdPagamento] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentoByIdPagamento] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::getPagamentiByIdStatoDebitorio] END");
		}
		return pagamento;
	}

	@Override
	public List<PagamentoDTO> getPagamentiByCodiceAvviso(String codiceAvviso) {
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiByCodiceAvviso] BEGIN");
		List<PagamentoDTO> listPagamenti = new ArrayList<PagamentoDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codiceAvviso", codiceAvviso);
			MapSqlParameterSource params = getParameterValue(map);
			listPagamenti = template.query(getQuery(QUERY_PAGAMENTO_BY_CODICE_AVVISO, null, null), params,
					new PagamentoRowMapper());
		} catch (Exception e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentiByCodiceAvviso] Errore generale ", e);
			LOGGER.debug("[PagamentoDAOImpl::getPagamentiByCodiceAvviso] END");
			return listPagamenti;
		}
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiByCodiceAvviso] END");
		return listPagamenti;
	}

	@Override
	public List<PagamentoDTO> getPagamentiByQuintoCampo(String quintoCampo) {
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiByQuintoCampo] BEGIN");
		List<PagamentoDTO> listPagamenti = new ArrayList<PagamentoDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("quintoCampo", quintoCampo);
			MapSqlParameterSource params = getParameterValue(map);
			listPagamenti = template.query(getQuery(QUERY_PAGAMENTO_BY_QUINTO_CAMPO, null, null), params,
					new PagamentoRowMapper());
		} catch (Exception e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentiByQuintoCampo] Errore generale ", e);
			LOGGER.debug("[PagamentoDAOImpl::getPagamentiByQuintoCampo] END");
			return listPagamenti;
		}
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiByQuintoCampo] END");
		return listPagamenti;
	}

	@Override
	public List<PagamentoDTO> getPagamentiForPosteEstrco(String dataValuta, BigDecimal importoMovimento,
			String descrizioneMovimento, Long idTipoModalitaPag, String cro) {
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiForPosteEstrco] BEGIN");
		List<PagamentoDTO> listPagamenti = new ArrayList<PagamentoDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("dataValuta", dataValuta);
			map.put("importoMovimento", importoMovimento);
			map.put("descrizioneMovimento", descrizioneMovimento);
			map.put("idTipoModalitaPag", idTipoModalitaPag);
			if (cro != null) {
				map.put("cro", cro);
			}
			MapSqlParameterSource params = getParameterValue(map);
			listPagamenti = template.query(getQuery(QUERY_PAGAMENTO_FOR_POSTE_ESTRCO, null, null), params,
					new PagamentoRowMapper());
		} catch (Exception e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentiForPosteEstrco] Errore generale ", e);
			LOGGER.debug("[PagamentoDAOImpl::getPagamentiForPosteEstrco] END");
			return listPagamenti;
		}
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiForPosteEstrco] END");
		return listPagamenti;
	}

	@Override
	public List<PagamentoExtendedDTO> getPagamentiDaVisionare(Integer offset, Integer limit, String sort)
			throws Exception {
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiDaVisionare] BEGIN");
		List<PagamentoExtendedDTO> listPagamenti = new ArrayList<>();
		List<DettaglioPagDTO> dettaglioPagList = new ArrayList<DettaglioPagDTO>();
		List<PagNonPropriExtendedDTO> pagNonPropriList = new ArrayList<PagNonPropriExtendedDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			listPagamenti = template.query(getQuery(QUERY_PAGAMENTI_DA_VISIONARE,
					offset != null ? offset.toString() : null, limit != null ? limit.toString() : null),
					new PagamentoExtendedRowMapper());
			for (PagamentoExtendedDTO pagamentoExtendedDTO : listPagamenti) {

				if (pagamentoExtendedDTO != null) {

					MapSqlParameterSource params = getParameterValue(map);
					map.put("idPagamento", pagamentoExtendedDTO.getIdPagamento());
					params = getParameterValue(map);

					dettaglioPagList = template.query(getQuery(QUERY_DETTAGLIO_LIST_BY_PAGAMENTO, null, null), params,
							getDettaglioPagRowMapper());

					if (dettaglioPagList != null && dettaglioPagList.size() > 0)
						pagamentoExtendedDTO.setDettaglioPag(dettaglioPagList);

					pagNonPropriList = template.query(getQuery(QUERY_PAG_NON_PROPRI_LIST_BY_PAGAMENTO, null, null),
							params, getPagNonPropriRowMapper());

					if (pagNonPropriList != null && pagNonPropriList.size() > 0)
						pagamentoExtendedDTO.setPagNonPropri(pagNonPropriList);

				}

			}
		} catch (Exception e) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentiDaVisionare] Errore generale ", e);
			LOGGER.debug("[PagamentoDAOImpl::getPagamentiDaVisionare] END");
			throw e;
		}
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiDaVisionare] END");
		return listPagamenti;
	}

	@Override
	public Integer countPagamentiDaVisionare() throws Exception {
		LOGGER.debug("[PagamentoDAOImpl::countPagamentiDaVisionare] BEGIN");
		try {
			return template.queryForObject(QUERY_COUNT_PAGAMENTI_DA_VISIONARE, new HashMap<>(), Integer.class);

		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::countPagamentiDaVisionare] Errore nell'accesso ai dati ", e);
			throw new Exception(e);
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::countPagamentiDaVisionare] END");
		}
	}

	public String getDataPagamentoMax(Long idStatoDebitorio) {
		LOGGER.debug("[PagamentoDAOImpl::getDataPagamentoMax] BEGIN");
		String ret = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);

			ret = template.queryForObject(QUERY_MAX_DATA_PAG, params, new MaxDataRowMapper());

		} catch (Exception e) {
			LOGGER.error("[PagamentoDAOImpl::getDataPagamentoMax] Errore nell'accesso ai dati", e);
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::getDataPagamentoMax] END");
		}
		return ret;
	}

	public static class MaxDataRowMapper implements RowMapper<String> {
		public MaxDataRowMapper() {
		}

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("max_data_op_val");
		}
	}

	@Override
	public List<PagamentoExtendedDTO> getPagamentiPerRiscCoattivaEFallimByIdRataSdAndIdStatoDeb(Long idRataSd,
			Long idStatoDebitorio) throws DAOException, SystemException {
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiPerRiscCoattivaEFallimByIdRataSdAndIdStatoDeb] BEGIN");
		List<PagamentoExtendedDTO> pagamentoList = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();

		sql.append("select ");
		sql.append("	b.data_op_val, ");
		sql.append("	a.importo_versato , ");
		sql.append("	b.id_pagamento, ");
		sql.append("	b.id_tipo_modalita_pag ");
		sql.append("from ");
		sql.append("	risca_r_dettaglio_pag a, ");
		sql.append("	risca_t_pagamento b ");
		sql.append("where ");
		sql.append("	a.id_rata_sd = :ID_RATA_SD ");
		sql.append("	and  a.id_pagamento = b.id_pagamento ");
		sql.append("	and b.data_op_val < ( select  ");
		sql.append("   case when MIN(data_protocollo)  is null then (TO_DATE('" + Constants.DATA_PROTOCOLLO_NULL
				+ "', 'dd/mm/yyyy') ) ");
		sql.append("   else MIN(data_protocollo)end as data_protocollo  ");
		sql.append("   from risca_t_accertamento ");
		sql.append("    where id_stato_debitorio = :ID_STATO_DEBITORIO ");
		sql.append("    and  id_tipo_accertamento IN ( ");
		sql.append("                          "
				+ Constants.DB.TIPO_ACCERTAMENTO.ID_TIPO_ACCERTAMENTO.RISCOSSIONE_COATTIVA + ", ");
		sql.append(
				"                          " + Constants.DB.TIPO_ACCERTAMENTO.ID_TIPO_ACCERTAMENTO.FALLIMENTO + "  ");
		sql.append("                            ) ");
		sql.append("    and flg_annullato = " + Constants.FLAG_ANNULLATO_FALSE + ") ");
		sql.append("order by ");
		sql.append("	b.data_op_val, ");
		sql.append("	b.id_pagamento ");

		paramMap.addValue("ID_RATA_SD", idRataSd);
		paramMap.addValue("ID_STATO_DEBITORIO", idStatoDebitorio);

		LOGGER.debug("[PagamentoDAOImpl - getPagamentiPerRiscCoattivaEFallimByIdRataSdAndIdStatoDeb] query ="
				+ sql.toString());

		try {
			pagamentoList = template.query(sql.toString(), paramMap, new ExtendedForStatoContribuzioneRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			LOGGER.debug(
					"\"[PagamentoDAOImpl::getPagamentiPerRiscCoattivaEFallimByIdRataSdAndIdStatoDeb]  NESSUN RISULTATO");
			throw new DAOException("Nessun dato in base alla richiesta");
		} catch (Throwable ex) {
			LOGGER.error(
					"[PagamentoDAOImpl::getPagamentiPerRiscCoattivaEFallimByIdRataSdAndIdStatoDeb] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::getPagamentiPerRiscCoattivaEFallimByIdRataSdAndIdStatoDeb] END");
		}
		return (List<PagamentoExtendedDTO>) pagamentoList;
	}

	public static class ExtendedForStatoContribuzioneRowMapper implements RowMapper<PagamentoExtendedDTO> {

		public ExtendedForStatoContribuzioneRowMapper() throws SQLException {
		}

		@Override
		public PagamentoExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			PagamentoExtendedDTO bean = new PagamentoExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, PagamentoExtendedDTO bean) throws SQLException {
			bean.setDataOpVal(rs.getDate("data_op_val"));
			bean.setIdPagamento(rs.getLong("id_pagamento"));
			bean.setIdTipoModalitaPag(rs.getLong("id_tipo_modalita_pag"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));

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
	public List<PagamentoExtendedDTO> getPagamentiByIdStatoDebForDilazione(Long idStatoDebitorio)
			throws DAOException, SystemException {
		LOGGER.debug("[PagamentoDAOImpl::getPagamentiByIdStatoDebForDilazione] BEGIN");
		List<PagamentoExtendedDTO> pagamentoList = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();

		sql.append(
				"select dp.id_pagamento, coalesce (dp.importo_versato,0) as importo_versato, p.data_op_val, p.id_tipo_modalita_pag ");
		sql.append("from ");
		sql.append("	risca_r_rata_sd rsd, ");
		sql.append("	risca_r_dettaglio_pag  dp, ");
		sql.append("    risca_t_pagamento p ");
		sql.append("where ");
		sql.append("	rsd.id_stato_debitorio = :ID_STATO_DEBITORIO ");
		sql.append("	and  rsd.id_rata_sd_padre IS NULL  ");
		sql.append("	and rsd.id_rata_sd =  dp.id_rata_sd ");
		sql.append("   and dp.id_pagamento = p.id_pagamento ");
		sql.append("   order by p.data_op_val, p.id_pagamento  ");

		paramMap.addValue("ID_STATO_DEBITORIO", idStatoDebitorio);

		LOGGER.debug("[PagamentoDAOImpl - getPagamentiByIdStatoDebForDilazione] query =" + sql.toString());

		try {
			pagamentoList = template.query(sql.toString(), paramMap, new ExtendedForDilazioneRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			LOGGER.debug("\"[PagamentoDAOImpl::getPagamentiByIdStatoDebForDilazione]  NESSUN RISULTATO");
			throw new DAOException("Nessun dato in base alla richiesta");
		} catch (Throwable ex) {
			LOGGER.error("[PagamentoDAOImpl::getPagamentiByIdStatoDebForDilazione] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::getPagamentiByIdStatoDebForDilazione] END");
		}
		return (List<PagamentoExtendedDTO>) pagamentoList;
	}

	public static class ExtendedForDilazioneRowMapper implements RowMapper<PagamentoExtendedDTO> {

		public ExtendedForDilazioneRowMapper() throws SQLException {
		}

		@Override
		public PagamentoExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			PagamentoExtendedDTO bean = new PagamentoExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, PagamentoExtendedDTO bean) throws SQLException {
			bean.setIdPagamento(rs.getLong("id_pagamento"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setDataOpVal(rs.getDate("data_op_val"));
			bean.setIdTipoModalitaPag(rs.getLong("id_tipo_modalita_pag"));

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
	public List<Long> getListaIdPagamentiByAnnoDataRif(String anno, String dataRif, List<Long> listaIdPagamento) {
		LOGGER.debug("[PagamentoDAOImpl::getListaIdPagamentiByAnnoDataRif] BEGIN");
		List<Long> listPagamenti = new ArrayList<Long>();
		try {
			Map<String, Object> map = new HashMap<>();
			String query = QUERY_ID_PAGAMENTO_CON_DETTAGLIO;
			if (anno != null) {
				map.put("anno", Integer.valueOf(anno));
				query += CONDITION_ANNO;
			} else if (dataRif != null) {
				map.put("dataRif", dataRif);
				query += CONDITION_DATA_RIF;
			} else if (listaIdPagamento != null && listaIdPagamento.size() > 0) {
				map.put("listaIdPagamento", listaIdPagamento);
				query += CONDITION_LISTA_ID;
			}
			query += ORDER_BY_DATA_IMPORTO;
			MapSqlParameterSource params = getParameterValue(map);
			listPagamenti = template.query(getQuery(query, null, null), params, new IdPagamentoRowMapper());
		} catch (Exception e) {
			LOGGER.error("[PagamentoDAOImpl::getListaIdPagamentiByAnnoDataRif] Errore generale ", e);
			LOGGER.debug("[PagamentoDAOImpl::getListaIdPagamentiByAnnoDataRif] END");
			return listPagamenti;
		}
		LOGGER.debug("[PagamentoDAOImpl::getListaIdPagamentiByAnnoDataRif] END");
		return listPagamenti;
	}

	public static class IdPagamentoRowMapper implements RowMapper<Long> {
		public IdPagamentoRowMapper() throws SQLException {
		}

		@Override
		public Long mapRow(ResultSet rs, int arg1) throws SQLException {
			return rs.getLong("id_pagamento");
		}
	}	
	
	@Override
	public List<Long> countEsistenzaPagamentoPerSoris(String dataEvento, BigDecimal sumImportoCaricoRisco , Long idFileSoris ) throws Exception {
		LOGGER.debug("[PagamentoDAOImpl::countEsistenzaPagamentoPerSoris] BEGIN");
		LOGGER.debug("[PagamentoDAOImpl::countEsistenzaPagamentoPerSoris] dataEvento: "+dataEvento);
		LOGGER.debug("[PagamentoDAOImpl::countEsistenzaPagamentoPerSoris] sumImportoCaricoRisco: "+sumImportoCaricoRisco);
		LOGGER.debug("[PagamentoDAOImpl::countEsistenzaPagamentoPerSoris] idFileSoris: "+idFileSoris);
		List<Long> listPagamenti = new ArrayList<Long>();
		try {
			
			Map<String, Object> map = new HashMap<>();
			map.put("dataEvento", dataEvento);
			map.put("sumImportoCaricoRisco", sumImportoCaricoRisco);
			map.put("idFileSoris", idFileSoris);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(QUERY_COUNT_ESISTENZA_PAGAMENTO_PER_SORIS, params, new IdPagamentoRowMapper());

		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::countEsistenzaPagamentoPerSoris] Errore nell'accesso ai dati ", e);
			e.printStackTrace();
			return listPagamenti;
		} 
		finally {
			LOGGER.debug("[PagamentoDAOImpl::countEsistenzaPagamentoPerSoris] END");
		}
	}
	
	
	@Override
	public Integer countEsistenzaPagamentoDaAnnullarePerSoris(String dataEvento, Long idPagamento ) throws Exception {
		LOGGER.debug("[PagamentoDAOImpl::countEsistenzaPagamentoDaAnnullarePerSoris] BEGIN");
		LOGGER.debug("[PagamentoDAOImpl::countEsistenzaPagamentoDaAnnullarePerSoris] dataEvento:"+dataEvento);
		LOGGER.debug("[PagamentoDAOImpl::countEsistenzaPagamentoDaAnnullarePerSoris] idPagamento:"+idPagamento);
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("dataEvento", dataEvento);
			map.put("idPagamento", idPagamento);
			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(QUERY_COUNT_ESISTENZA_PAGAMENTO_DA_ANNULLARE_PER_SORIS, params, Integer.class);

		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::countEsistenzaPagamentoDaAnnullarePerSoris] Errore nell'accesso ai dati ", e);
			throw new Exception(e);
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::countEsistenzaPagamentoDaAnnullarePerSoris] END");
		}
	}
	
	@Override
	public PagamentoExtendedDTO updateAnnullamentoPagamentoSoris(PagamentoExtendedDTO dto, String fruitore, Long idFileSoris) {
		LOGGER.debug("[PagamentoDAOImpl::updateAnnullamentoPagamentoSoris] BEGIN");
		LOGGER.debug("[PagamentoDAOImpl::updateAnnullamentoPagamentoSoris] idPagamento: "+dto.getIdPagamento());
		LOGGER.debug("[PagamentoDAOImpl::updateAnnullamentoPagamentoSoris] idFileSoris: "+idFileSoris);
		
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idPagamento", dto.getIdPagamento());	
			map.put("idFileSoris", idFileSoris);	
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_UPDATE_ANNULLAMENTO_PAGAMENTO_SORIS;

			template.update(getQuery(query, null, null), params, keyHolder);

			dto.setGestDataUpd(now);
			return dto;

		} catch (DataAccessException e) {
			LOGGER.error("[PagamentoDAOImpl::updateAnnullamentoPagamentoSoris] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[PagamentoDAOImpl::updateAnnullamentoPagamentoSoris] END");
		}
	}

}