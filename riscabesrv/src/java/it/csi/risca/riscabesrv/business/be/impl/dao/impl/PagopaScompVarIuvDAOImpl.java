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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaScompVarIuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.PagopaScompVarIuvDTO;
import it.csi.risca.riscabesrv.dto.ScomposizioneRichiestaIuvDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type PagopaScompVarIuv dao.
 *
 * @author CSI PIEMONTE
 */
public class PagopaScompVarIuvDAOImpl extends RiscaBeSrvGenericDAO<PagopaScompVarIuvDTO>
		implements PagopaScompVarIuvDAO {

	private static final String QUERY_INSERT_WORKING = "insert into risca_w_pagopa_scomp_var_iuv "
			+ " (id_pagopa_scomp_var_iuv, nap, id_bil_acc, importo_per_nap, cod_bil_acc, "
			+ "anno,dati_spec_risc, importo_per_acc_orig, importo_per_acc, id_tipo_bil_acc, "
			+ "sum_imp_per_acc_orig, perc_tot_imp, note_backoffice) "
			+ "values (:idPagopaScompVarIuv, :nap, :idBilAcc, :importoPerNap, :codBilAcc, "
			+ ":anno, :datiSpecRisc, :importoPerAccOrig, :importoPerAcc, :idTipoBilAcc, "
			+ ":sumImpPerAccOrig, :percTotImp, :noteBackoffice)";

	private static final String QUERY_INSERT = "INSERT INTO risca_r_pagopa_scomp_var_iuv "
			+ " (id_pagopa_scomp_var_iuv, id_lotto, nap, id_bil_acc, importo_per_nap, cod_bil_acc, "
			+ " anno, dati_spec_risc, importo_per_acc, id_tipo_bil_acc, note_backoffice, "
			+ " gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ " VALUES(:idPagopaScompVarIuv, :idLotto, :nap, :idBilAcc, :importoPerNap, :codBilAcc, "
			+ " :anno, :datiSpecRisc, :importoPerAcc, :idTipoBilAcc, :noteBackoffice, "
			+ " :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid);";

	private static final String QUERY_LOAD_WORKING_BY_NAP = "select * from risca_w_pagopa_scomp_var_iuv "
			+ " where nap = :nap";

	private static final String QUERY_LOAD_WORKING_BY_IUV = "select * "
			+ "from risca_w_pagopa_scomp_var_iuv rwpsvi inner join risca_t_iuv rti on rti.nap = rwpsvi.nap "
			+ "inner join risca_r_iuv_da_inviare rridi on rridi.id_iuv = rti.id_iuv  where rridi.id_iuv  = :idIuv  and id_elabora = :idElabora ";

	private static final String QUERY_SCOMPOSIZIONE_CANONE = "select sco.nap, sco.id_bil_acc,sco.importo_per_nap,sco.cod_bil_acc, sco.anno,  "
			+ "sco.dati_spec_risc, sco.importo_per_acc_orig, sco.id_accerta_bilancio, sco.sum_imp_per_acc_orig,  "
			+ "sco.progr_imp_per_acc_orig, sco.count_imp_per_acc_orig, " + "CASE "
			+ "WHEN sco.sum_imp_per_acc_orig = 0 then 0 "
			+ "else ((sco.importo_per_acc_orig / sco.sum_imp_per_acc_orig) * 100) "
			+ "end as perc_tot_imp                              " + "from ( "
			+ "	select s2.nap,ba.id_bil_acc, idi.importo_new - (idi.interes_rit_pag + idi.tot_spese_notif_per_nap + idi.interessi)  "
			+ "		as importo_per_nap,ba.cod_bil_acc,ba.dati_spec_risc,ba.anno, s2.importo_per_acc_orig, s2.id_accerta_bilancio, "
			+ "        SUM(s2.importo_per_acc_orig) OVER (PARTITION BY s2.NAP) as sum_imp_per_acc_orig, "
			+ "		ROW_NUMBER() OVER (PARTITION BY s2.NAP ORDER BY s2.importo_per_acc_orig) as progr_imp_per_acc_orig, "
			+ "        COUNT(s2.importo_per_acc_orig) OVER (PARTITION BY s2.NAP) as count_imp_per_acc_orig "
			+ "    from ( " + "       select s1.nap, " + "          CASE "
			+ "          WHEN length(s1.nap) = 11 then '20' || substr(s1.nap, 5, 2) "
			+ "          else '20' || substr(s1.nap, 1, 2) " + "          end as anno, "
			+ "          s1.id_accerta_bilancio, SUM(s1.canone_uso) as importo_per_acc_orig "
			+ "       from (                          "
			+ "          select sd.nap, ausd.id_tipo_uso,SUM(ausd.canone_uso) as canone_uso,  "
			+ "              u.id_accerta_bilancio, u.des_tipo_uso " + "          from "
			+ "             risca_T_STATO_DEBITORIO sd, " + "             risca_R_ANNUALITA_SD ann, "
			+ "             risca_R_ANNUALITA_USO_SD ausd, " + "             risca_D_TIPO_USO u  "
			+ "          where sd.id_stato_debitorio = ann.id_stato_debitorio "
			+ "            and sd.flg_annullato = 0 " + "            and ann.id_annualita_sd = ausd.id_annualita_sd "
			+ "            and ausd.id_tipo_uso = u.id_tipo_uso " + "            and u.id_tipo_uso_padre IS NULL "
			+ "         	group by sd.nap, ausd.id_tipo_uso, u.id_accerta_bilancio, u.des_tipo_uso  "
			+ "         ) s1 " + "         group by s1.nap, s1.id_accerta_bilancio " + "    ) s2 "
			+ "	inner join RISCA_T_IUV  ti  on s2.nap = ti.nap     "
			+ "	inner join RISCA_R_IUV_DA_INVIARE  idi  on ti.id_iuv = idi.id_iuv  "
			+ "	inner join risca_t_bil_acc ba on s2.id_accerta_bilancio =  ba.id_accerta_bilancio "
			+ "        and s2.anno = CAST (ba.anno_competenza AS text) "
			+ "        and TRUNC(current_date) BETWEEN ba.data_inizio_validita and ba.data_fine_validita "
			+ "        and idi.flg_da_inviare = 1 " 
			+ "		   and idi.id_elabora = :idElabora "
			+ "        and idi.ind_tipo_aggiornamento = 'M' " + ") sco "
			+ "where importo_per_nap > 0" + "order by sco.nap, sco.importo_per_acc_orig;";
	private static final String QUERY_SCOMPOSIZIONE_INTERESSI = "select ti.nap,ba.id_bil_acc as id_bil_acc,ba.cod_bil_acc,ba.anno,ba.dati_spec_risc, "
			+ "(idi.interessi + idi.interes_rit_pag) as interessi_tot, "
			+ "ba.id_accerta_bilancio,idi.canone_dovuto,idi.imp_versato	"
			+ "from RISCA_R_IUV_DA_INVIARE idi,RISCA_T_BIL_ACC ba,RISCA_T_IUV ti " 
			+ "where idi.id_iuv = ti.id_iuv  "
			+ "and (idi.interessi + idi.interes_rit_pag) > 0 " 
			+ "and ba.anno = EXTRACT(YEAR FROM current_date) "
			+ "and ba.id_accerta_bilancio = 10 " 
			+ "and idi.flg_da_inviare = 1 "
			+ "and idi.id_elabora = :idElabora "
			+ "and idi.ind_tipo_aggiornamento = 'M'";
	private static final String QUERY_SCOMPOSIZIONE_SPESE_NOTIFICA = "select ti.nap,ba.id_bil_acc as id_bil_acc,ba.cod_bil_acc,ba.anno,ba.dati_spec_risc, "
			+ "(idi.interessi + idi.interes_rit_pag) as interessi_tot, "
			+ "idi.tot_spese_notif_per_nap as tot_spese_notif_per_nap, "
			+ "ba.id_accerta_bilancio,idi.canone_dovuto,idi.imp_versato "
			+ "from RISCA_R_IUV_DA_INVIARE idi, RISCA_T_BIL_ACC ba, RISCA_T_IUV ti "
			+ "where idi.id_iuv = ti.id_iuv  " 
			+ "and (idi.interessi + idi.interes_rit_pag) > 0 "
			+ "and ba.anno = EXTRACT(YEAR FROM current_date) " 
			+ "and ba.id_accerta_bilancio = 20 "
			+ "and idi.flg_da_inviare = 1 "
			+ "and idi.id_elabora = :idElabora "
			+ "and idi.ind_tipo_aggiornamento = 'M'";
	
	private static final String QUERY_DELETE_WORKING_BY_NAP = " delete from risca_w_pagopa_scomp_var_iuv where nap in (:listaNap) ";

	private static final String QUERY_DELETE_WORKING = " delete from risca_w_pagopa_scomp_var_iuv ";


	@Override
	public PagopaScompVarIuvDTO savePagopaScompVarIuv(PagopaScompVarIuvDTO dto) {
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::savePagopaScompVarIuv] BEGIN");
		try {
			savePagopaScompVarIuv(dto, false);
		} catch (Exception e) {
			LOGGER.error("[PagopaScompVarIuvDAOImpl::savePagopaScompVarIuv] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[PagopaScompVarIuvDAOImpl::savePagopaScompVarIuv] END");
		}

		return dto;
	}

	@Override
	public PagopaScompVarIuvDTO savePagopaScompVarIuvWorking(PagopaScompVarIuvDTO dto) {
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::savePagopaScompVarIuvWorking] BEGIN");
		try {
			savePagopaScompVarIuv(dto, true);
		} catch (Exception e) {
			LOGGER.error("[PagopaScompVarIuvDAOImpl::savePagopaScompVarIuvWorking] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[PagopaScompVarIuvDAOImpl::savePagopaScompVarIuvWorking] END");
		}

		return dto;
	}

	@Override
	public List<PagopaScompVarIuvDTO> loadScompVarIuvWorkingByNap(String nap) {
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScompVarIuvWorkingByNap] BEGIN");
		List<PagopaScompVarIuvDTO> list = new ArrayList<PagopaScompVarIuvDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			MapSqlParameterSource params = getParameterValue(map);
			list = template.query(getQuery(QUERY_LOAD_WORKING_BY_NAP, null, null), params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("[PagopaScompVarIuvDAOImpl::loadScompVarIuvWorkingByNap] Errore nell'esecuzione della query",
					e);
			return list;
		} finally {
			LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScompVarIuvWorkingByNap] END");
		}
		return list;
	}

	@Override
	public List<PagopaScompVarIuvDTO> loadScompVarIuvWorkingByIdIuv(Long idIuv, Long idElabora) {
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScompVarIuvWorkingByIdIuv] BEGIN");
		List<PagopaScompVarIuvDTO> list = new ArrayList<PagopaScompVarIuvDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idIuv", idIuv);
			map.put("idElabora", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			list = template.query(getQuery(QUERY_LOAD_WORKING_BY_IUV, null, null), params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("[PagopaScompVarIuvDAOImpl::loadScompVarIuvWorkingByIdIuv] Errore nell'esecuzione della query",
					e);
			return list;
		} finally {
			LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScompVarIuvWorkingByIdIuv] END");
		}
		return list;
	}

	private void savePagopaScompVarIuv(PagopaScompVarIuvDTO dto, boolean working) throws SQLException {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		Long genId = findNextSequenceValue("seq_risca_r_pagopa_scomp_var_iuv");

		map.put("idPagopaScompVarIuv", genId);
		map.put("nap", dto.getNap());
		map.put("idBilAcc", dto.getIdBilAcc());
		map.put("importoPerNap", dto.getImportoPerNap());
		map.put("codBilAcc", dto.getCodBilAcc());
		map.put("anno", dto.getAnno());
		map.put("datiSpecRisc", dto.getDatiSpecRisc());
		map.put("importoPerAcc", dto.getImportoPerAcc());
		map.put("idTipoBilAcc", dto.getIdTipoBilAcc());
		if (working) {
			map.put("importoPerAccOrig", dto.getImportoPerAccOrig());
			map.put("sumImpPerAccOrig", dto.getSumImpPerAccOrig());
			map.put("percTotImp", dto.getPercTotImp());
		}
		map.put("noteBackoffice", dto.getNoteBackoffice());
		if (!working) {
			map.put("idLotto", dto.getIdLotto());
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
			query = QUERY_INSERT_WORKING;
		}
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setIdPagopaScompVarIuv(genId);
	}

	@Override
	public List<ScomposizioneRichiestaIuvDTO> loadScomposizioneCanone(Long idElabora) {
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScomposizioneCanone] BEGIN");
		List<ScomposizioneRichiestaIuvDTO> listScomp = new ArrayList<ScomposizioneRichiestaIuvDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			listScomp = template.query(getQuery(QUERY_SCOMPOSIZIONE_CANONE, null, null), params,
					new ScomposizioneRowMapper());
		} catch (Exception e) {
			LOGGER.error("[PagopaScompVarIuvDAOImpl::loadScomposizioneCanone] Errore generale ", e);
			LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScomposizioneCanone] END");
			return listScomp;
		}
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScomposizioneCanone] END");
		return listScomp;
	}

	@Override
	public List<ScomposizioneRichiestaIuvDTO> loadScomposizioneInteressi(Long idElabora) {
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScomposizioneInteressi] BEGIN");
		List<ScomposizioneRichiestaIuvDTO> listScomp = new ArrayList<ScomposizioneRichiestaIuvDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			listScomp = template.query(getQuery(QUERY_SCOMPOSIZIONE_INTERESSI, null, null), params,
					new ScomposizioneRowMapper());
		} catch (Exception e) {
			LOGGER.error("[PagopaScompVarIuvDAOImpl::loadScomposizioneInteressi] Errore generale ", e);
			LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScomposizioneInteressi] END");
			return listScomp;
		}
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScomposizioneInteressi] END");
		return listScomp;
	}

	@Override
	public List<ScomposizioneRichiestaIuvDTO> loadScomposizioneSpeseNotifica(Long idElabora) {
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScomposizioneSpeseNotifica] BEGIN");
		List<ScomposizioneRichiestaIuvDTO> listScomp = new ArrayList<ScomposizioneRichiestaIuvDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			listScomp = template.query(getQuery(QUERY_SCOMPOSIZIONE_SPESE_NOTIFICA, null, null), params,
					new ScomposizioneRowMapper());
		} catch (Exception e) {
			LOGGER.error("[PagopaScompVarIuvDAOImpl::loadScomposizioneSpeseNotifica] Errore generale ", e);
			LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScomposizioneSpeseNotifica] END");
			return listScomp;
		}
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::loadScomposizioneSpeseNotifica] END");
		return listScomp;
	}
	
	@Override
	public Integer deletePagopaScompVarIuvWorkingWhereNap(List<String> listaNap) throws DAOException {
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::deletePagopaScompVarIuvWorkingWhereNap] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("listaNap", listaNap);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_WORKING_BY_NAP, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[PagopaScompRichIuvDAOImpl::deletePagopaScompVarIuvWorkingWhereNap] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[PagopaScompVarIuvDAOImpl::deletePagopaScompVarIuvWorkingWhereNap] END");
		}

		return res;
	}
	
	@Override
	public Integer deletePagopaScompVarIuvWorking() throws DAOException {
		LOGGER.debug("[PagopaScompVarIuvDAOImpl::deletePagopaScompVarIuvWorking] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_WORKING, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[PagopaScompRichIuvDAOImpl::deletePagopaScompVarIuvWorking] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[PagopaScompVarIuvDAOImpl::deletePagopaScompVarIuvWorking] END");
		}

		return res;
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
	public RowMapper<PagopaScompVarIuvDTO> getRowMapper() throws SQLException {
		return new PagopaScompVarIuvRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<PagopaScompVarIuvDTO> getExtendedRowMapper() throws SQLException {
		return new PagopaScompVarIuvRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class PagopaScompVarIuvRowMapper implements RowMapper<PagopaScompVarIuvDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public PagopaScompVarIuvRowMapper() throws SQLException {
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
		public PagopaScompVarIuvDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			PagopaScompVarIuvDTO bean = new PagopaScompVarIuvDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, PagopaScompVarIuvDTO bean) throws SQLException {

			bean.setIdPagopaScompVarIuv(rs.getLong("id_pagopa_scomp_var_iuv"));

			if (rsHasColumn(rs, "gest_attore_ins"))
				bean.setIdLotto(rs.getLong("id_lotto"));

			bean.setNap(rs.getString("nap"));
			bean.setIdBilAcc(rs.getLong("id_bil_acc"));
			bean.setImportoPerNap(rs.getBigDecimal("importo_per_nap"));
			bean.setCodBilAcc(rs.getString("cod_bil_acc"));
			bean.setAnno(rs.getString("anno"));
			bean.setDatiSpecRisc(rs.getString("dati_spec_risc"));
			bean.setImportoPerAcc(rs.getBigDecimal("importo_per_acc"));
			bean.setIdTipoBilAcc(rs.getLong("id_tipo_bil_acc"));

			if (rsHasColumn(rs, "importo_per_acc_orig"))
				bean.setImportoPerAccOrig(rs.getBigDecimal("importo_per_acc_orig"));
			if (rsHasColumn(rs, "sum_imp_per_acc_orig"))
				bean.setSumImpPerAccOrig(rs.getBigDecimal("sum_imp_per_acc_orig"));
			if (rsHasColumn(rs, "perc_tot_imp"))
				bean.setPercTotImp(rs.getBigDecimal("perc_tot_imp"));

			bean.setNoteBackoffice(rs.getString("note_backoffice"));

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

	public static class ScomposizioneRowMapper implements RowMapper<ScomposizioneRichiestaIuvDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public ScomposizioneRowMapper() throws SQLException {
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
		public ScomposizioneRichiestaIuvDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ScomposizioneRichiestaIuvDTO bean = new ScomposizioneRichiestaIuvDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, ScomposizioneRichiestaIuvDTO bean) throws SQLException {

			bean.setNap(rs.getString("nap"));
			bean.setIdBilAcc(rs.getLong("id_bil_acc"));
			if (rsHasColumn(rs, "importo_per_nap"))
				bean.setImportoPerNap(rs.getBigDecimal("importo_per_nap"));
			if (rsHasColumn(rs, "cod_bil_acc"))
				bean.setCodBilAcc(rs.getString("cod_bil_acc"));
			if (rsHasColumn(rs, "anno"))
				bean.setAnno(rs.getString("anno"));
			if (rsHasColumn(rs, "anno"))
				bean.setDatiSpecRisc(rs.getString("dati_spec_risc"));
			if (rsHasColumn(rs, "importo_per_acc_orig"))
				bean.setImportoPerAccOrig(rs.getBigDecimal("importo_per_acc_orig"));
			if (rsHasColumn(rs, "id_accerta_bilancio"))
				bean.setIdAccertaBilancio(rs.getLong("id_accerta_bilancio"));
			if (rsHasColumn(rs, "sum_imp_per_acc_orig"))
				bean.setSumImpPerAccOrig(rs.getBigDecimal("sum_imp_per_acc_orig"));
			if (rsHasColumn(rs, "progr_imp_per_acc_orig"))
				bean.setProgrImpPerAccOrig(rs.getInt("progr_imp_per_acc_orig"));
			if (rsHasColumn(rs, "count_imp_per_acc_orig"))
				bean.setCountImpPerAccOrig(rs.getInt("count_imp_per_acc_orig"));
			if (rsHasColumn(rs, "perc_tot_imp"))
				bean.setPercTotImp(rs.getBigDecimal("perc_tot_imp"));
			if (rsHasColumn(rs, "interessi_tot"))
				bean.setInteressiTot(rs.getBigDecimal("interessi_tot"));
			if (rsHasColumn(rs, "tot_spese_notif_per_nap"))
				bean.setTotSpeseNotifPerNap(rs.getBigDecimal("tot_spese_notif_per_nap"));
			if (rsHasColumn(rs, "canone_dovuto"))
				bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
			if (rsHasColumn(rs, "imp_versato"))
				bean.setImpVersato(rs.getBigDecimal("imp_versato"));
			if (rsHasColumn(rs, "id_accerta_bilancio"))
				bean.setIdTipoBilAcc(rs.getLong("id_accerta_bilancio"));
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