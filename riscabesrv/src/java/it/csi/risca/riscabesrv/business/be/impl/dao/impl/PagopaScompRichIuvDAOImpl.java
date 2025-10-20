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
import it.csi.risca.riscabesrv.business.be.impl.dao.PagopaScompRichIuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.PagopaScompRichIuvDTO;
import it.csi.risca.riscabesrv.dto.ScomposizioneRichiestaIuvDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type PagopaScompRichIuv dao.
 *
 * @author CSI PIEMONTE
 */
public class PagopaScompRichIuvDAOImpl extends RiscaBeSrvGenericDAO<PagopaScompRichIuvDTO>
		implements PagopaScompRichIuvDAO {

	private static final String QUERY_INSERT_WORKING = "insert into risca_w_pagopa_scomp_rich_iuv "
			+ " (id_pagopa_scomp_rich_iuv, nap, id_bil_acc, importo_per_nap, cod_bil_acc, "
			+ "anno,dati_spec_risc, importo_per_acc_orig, importo_per_acc, id_tipo_bil_acc, "
			+ "sum_imp_per_acc_orig, perc_tot_imp, note_backoffice) "
			+ "values (:idPagopaScompRichIuv, :nap, :idBilAcc, :importoPerNap, :codBilAcc, "
			+ ":anno, :datiSpecRisc, :importoPerAccOrig, :importoPerAcc, :idTipoBilAcc, "
			+ ":sumImpPerAccOrig, :percTotImp, :noteBackoffice)";
	
	private static final String QUERY_INSERT = "INSERT INTO risca_r_pagopa_scomp_rich_iuv "
			+ " (id_pagopa_scomp_rich_iuv, id_lotto, nap, id_bil_acc, importo_per_nap, cod_bil_acc, "
			+ " anno, dati_spec_risc, importo_per_acc, id_tipo_bil_acc, note_backoffice, "
			+ " gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ " VALUES(:idPagopaScompRichIuv, :idLotto, :nap, :idBilAcc, :importoPerNap, :codBilAcc, "
			+ " :anno, :datiSpecRisc, :importoPerAcc, :idTipoBilAcc, :noteBackoffice, "
			+ " :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid);";

	private static final String QUERY_LOAD_WORKING_BY_NAP = "select * from risca_w_pagopa_scomp_rich_iuv "
			+ " where nap = :nap";
	
	private static final String QUERY_DELETE_WORKING_BY_NAP = " delete from risca_w_pagopa_scomp_rich_iuv where nap in (:listaNap) ";
	
	private static final String QUERY_SCOMPOSIZIONE_RICHIESTE_IUV = " select sco.nap,  "
			+ "       sco.id_bil_acc, "
			+ "       sco.importo_per_nap, "
			+ "       sco.cod_bil_acc,  "
			+ "       sco.anno,  "
			+ "       sco.dati_spec_risc,  "
			+ "       sco.importo_per_acc_orig,  "
			+ "       sco.id_accerta_bilancio,  "
			+ "       sco.sum_imp_per_acc_orig,  "
			+ "       sco.progr_imp_per_acc_orig,  "
			+ "       sco.count_imp_per_acc_orig, "
			+ "       case "
			+ "            WHEN sco.sum_imp_per_acc_orig = 0 then 0 "
			+ "            else ((sco.importo_per_acc_orig / sco.sum_imp_per_acc_orig) * 100) "
			+ "       end as perc_tot_imp   "
			+ "  from ( "
			+ "        select s2.nap, "
			+ "               ba.id_bil_acc, "
			+ "               ap.imp_totale_dovuto as importo_per_nap, "
			+ "               ba.cod_bil_acc, "
			+ "               ba.dati_spec_risc, "
			+ "               ba.anno,  "
			+ "               s2.importo_per_acc_orig, "
			+ "               s2.id_accerta_bilancio, "
			+ "               SUM(s2.importo_per_acc_orig) OVER (PARTITION BY s2.NAP) as sum_imp_per_acc_orig, "
			+ "               ROW_NUMBER() OVER (PARTITION BY s2.NAP ORDER BY s2.importo_per_acc_orig) as progr_imp_per_acc_orig, "
			+ "               COUNT(s2.importo_per_acc_orig) OVER (PARTITION BY s2.NAP) as count_imp_per_acc_orig "
			+ "         from (  "
			+ "               select s1.nap, "
			+ "                      CASE "
			+ "                          WHEN length(s1.nap) = 11 then '20' || substr(s1.nap, 5, 2) "
			+ "                          else '20' || substr(s1.nap, 1, 2) "
			+ "                      end as anno, "
			+ "                      s1.id_accerta_bilancio, "
			+ "                      SUM(s1.canone_uso) as importo_per_acc_orig "
			+ "                 from (  "
			+ "                       select sd.nap, "
			+ "                              ausd.id_annualita_sd, "
			+ "                              ann.anno, "
			+ "                              trunc(ausd.canone_uso) as canone_uso, "
			+ "                              u.id_accerta_bilancio "
			+ "                         from  "
			+ "                              ( "
			+ "                                  select nap, id_stato_debitorio, flg_annullato from risca_w_stato_debitorio "
			+ "                                  UNION "
			+ "                                  select nap, id_stato_debitorio, flg_annullato from risca_w_stato_debitorio_upd "
			+ "                              ) sd, "
			+ "                              ( "
			+ "                                 select id_annualita_sd,id_stato_debitorio,canone_annuo,anno from risca_w_annualita_sd "
			+ "                                 union "
			+ "                                 select a.id_annualita_sd, a.id_stato_debitorio, a.canone_annuo, a.anno  "
			+ "                                   from risca_r_annualita_sd a, risca_t_stato_debitorio s "
			+ "                                  where s.flg_invio_speciale = :flgInvioSpeciale "
			+ "                                    and a.id_stato_debitorio = s.id_stato_debitorio   "
			+ "                               ) ann, "
			+ "                              ( "
			+ "                               select id_tipo_uso,id_annualita_sd,canone_uso from risca_w_annualita_uso_sd                      "
			+ "                               union         "
			+ "                               select id_tipo_uso,id_annualita_sd,canone_uso from risca_r_annualita_uso_sd x                     "
			+ "                                where EXISTS "
			+ "                                     ( select id_annualita_sd "
			+ "                                         from risca_r_annualita_sd a, risca_t_stato_debitorio s "
			+ "                                        where a.id_annualita_sd = x.id_annualita_sd "
			+ "                                          and s.flg_invio_speciale = :flgInvioSpeciale "
			+ "                                          and a.id_stato_debitorio = s.id_stato_debitorio "
			+ "                                     ) "
			+ "                              ) ausd, "
			+ "                              risca_d_tipo_uso u "
			+ "                        where sd.id_stato_debitorio = ann.id_stato_debitorio  "
			+ "                          and sd.flg_annullato = 0 "
			+ "                          and ann.id_annualita_sd = ausd.id_annualita_sd "
			+ "                          and ausd.id_tipo_uso = u.id_tipo_uso "
			+ "                          and u.id_tipo_uso_padre IS NULL   "
			+ "                       ) s1 "
			+ "                group by s1.nap, s1.id_accerta_bilancio "
			+ "                ) s2 "
			+ "        inner join risca_w_avviso_pagamento ap on s2.nap = ap.nap  "
			+ "        inner join risca_t_bil_acc ba on s2.id_accerta_bilancio = ba.id_accerta_bilancio "
			+ "                                    and s2.anno = CAST (ba.anno_competenza AS text) "
			+ "                                        and TRUNC(current_date) BETWEEN ba.data_inizio_validita and ba.data_fine_validita "
			+ "           ) sco "
			+ "  order by sco.nap, sco.importo_per_acc_orig; ";
	
	@Override
	public PagopaScompRichIuvDTO savePagopaScompRichIuv(PagopaScompRichIuvDTO dto) {
		LOGGER.debug("[PagopaScompRichIuvDAOImpl::savePagopaScompRichIuv] BEGIN");
		try {
			savePagopaScompRichIuv(dto, false);
		} catch (Exception e) {
			LOGGER.error("[PagopaScompRichIuvDAOImpl::savePagopaScompRichIuv] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[PagopaScompRichIuvDAOImpl::savePagopaScompRichIuv] END");
		}

		return dto;
	}

	@Override
	public PagopaScompRichIuvDTO savePagopaScompRichIuvWorking(PagopaScompRichIuvDTO dto) {
		LOGGER.debug("[PagopaScompRichIuvDAOImpl::savePagopaScompRichIuvWorking] BEGIN");
		try {
			savePagopaScompRichIuv(dto, true);
		} catch (Exception e) {
			LOGGER.error("[PagopaScompRichIuvDAOImpl::savePagopaScompRichIuvWorking] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[PagopaScompRichIuvDAOImpl::savePagopaScompRichIuvWorking] END");
		}

		return dto;
	}
	
	@Override
	public List<PagopaScompRichIuvDTO> loadScompRichIuvWorkingByNap(String nap) {
		LOGGER.debug("[PagopaScompRichIuvDAOImpl::loadScompRichIuvWorkingByNap] BEGIN");
		List<PagopaScompRichIuvDTO> list = new ArrayList<PagopaScompRichIuvDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			MapSqlParameterSource params = getParameterValue(map);
			list = template.query(getQuery(QUERY_LOAD_WORKING_BY_NAP, null, null), params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("[PagopaScompRichIuvDAOImpl::loadScompRichIuvWorkingByNap] Errore nell'esecuzione della query", e);
			return list;
		} finally {
			LOGGER.debug("[PagopaScompRichIuvDAOImpl::loadScompRichIuvWorkingByNap] END");
		}
		return list;
	}
	
	@Override
	public List<ScomposizioneRichiestaIuvDTO> loadScomposizioneRichiesteIuv(String codTipoElabora) {
		LOGGER.debug("[PagopaScompRichIuvDAOImpl::loadScomposizioneRichiesteIuv] BEGIN");
        List<ScomposizioneRichiestaIuvDTO> listScomp = new ArrayList<ScomposizioneRichiestaIuvDTO>();
        try {
        	Map<String, Object> map = new HashMap<>();
			map.put("flgInvioSpeciale", codTipoElabora.equalsIgnoreCase("BG") ? 2 : 1);
			MapSqlParameterSource params = getParameterValue(map);
			listScomp = template.query(getQuery(QUERY_SCOMPOSIZIONE_RICHIESTE_IUV, null, null),
					params, new ScomposizioneRowMapper());
		} catch (Exception e) {
            LOGGER.error("[PagopaScompRichIuvDAOImpl::loadScomposizioneRichiesteIuv] Errore generale ", e);
            LOGGER.debug("[PagopaScompRichIuvDAOImpl::loadScomposizioneRichiesteIuv] END");
            return listScomp;
		}
        LOGGER.debug("[PagopaScompRichIuvDAOImpl::loadScomposizioneRichiesteIuv] END");
        return listScomp;
	}
	
	@Override
	public Integer deletePagopaScompRichIuvWorkingWhereNap(List<String> listaNap) throws DAOException {
		LOGGER.debug("[PagopaScompRichIuvDAOImpl::deletePagopaScompRichIuvWorking] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("listaNap", listaNap);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_WORKING_BY_NAP, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[PagopaScompRichIuvDAOImpl::deletePagopaScompRichIuvWorking] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[PagopaScompRichIuvDAOImpl::deletePagopaScompRichIuvWorking] END");
		}

		return res;
	}
	
	private void savePagopaScompRichIuv(PagopaScompRichIuvDTO dto, boolean working) throws SQLException {
		Map<String, Object> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		Long genId = findNextSequenceValue("seq_risca_r_pagopa_scomp_rich_iuv");

		map.put("idPagopaScompRichIuv", genId);
		map.put("nap", dto.getNap());
		map.put("idBilAcc", dto.getIdBilAcc());
		map.put("importoPerNap", dto.getImportoPerNap());
		map.put("codBilAcc", dto.getCodBilAcc());
		map.put("anno", dto.getAnno());
		map.put("datiSpecRisc", dto.getDatiSpecRisc());
		map.put("importoPerAcc", dto.getImportoPerAcc());
		map.put("idTipoBilAcc", dto.getIdTipoBilAcc());
		if(working) {
			map.put("importoPerAccOrig", dto.getImportoPerAccOrig());
			map.put("sumImpPerAccOrig", dto.getSumImpPerAccOrig());
			map.put("percTotImp", dto.getPercTotImp());
		}
		map.put("noteBackoffice", dto.getNoteBackoffice());
		if(!working) {
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
		if(working) {
			query = QUERY_INSERT_WORKING;
		}
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setIdPagopaScompRichIuv(genId);
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
	public RowMapper<PagopaScompRichIuvDTO> getRowMapper() throws SQLException {
		return new PagopaScompRichIuvRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<PagopaScompRichIuvDTO> getExtendedRowMapper() throws SQLException {
		return new PagopaScompRichIuvRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class PagopaScompRichIuvRowMapper implements RowMapper<PagopaScompRichIuvDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public PagopaScompRichIuvRowMapper() throws SQLException {
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
		public PagopaScompRichIuvDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			PagopaScompRichIuvDTO bean = new PagopaScompRichIuvDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, PagopaScompRichIuvDTO bean) throws SQLException {

			bean.setIdPagopaScompRichIuv(rs.getLong("id_pagopa_scomp_rich_iuv"));
			
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setIdLotto(rs.getLong("id_lotto"));
			
			bean.setNap(rs.getString("nap"));
			bean.setIdBilAcc(rs.getLong("id_bil_acc"));
			bean.setImportoPerNap(rs.getBigDecimal("importo_per_nap"));
			bean.setCodBilAcc(rs.getString("cod_bil_acc"));
			bean.setAnno(rs.getString("anno"));
			bean.setDatiSpecRisc(rs.getString("dati_spec_risc"));
			bean.setImportoPerAcc(rs.getBigDecimal("importo_per_acc"));
			bean.setIdTipoBilAcc(rs.getLong("id_tipo_bil_acc"));
			
			if(rsHasColumn(rs, "importo_per_acc_orig")) bean.setImportoPerAccOrig(rs.getBigDecimal("importo_per_acc_orig"));
			if(rsHasColumn(rs, "sum_imp_per_acc_orig"))  bean.setSumImpPerAccOrig(rs.getBigDecimal("sum_imp_per_acc_orig"));
			if(rsHasColumn(rs, "perc_tot_imp"))  bean.setPercTotImp(rs.getBigDecimal("perc_tot_imp"));
			
			bean.setNoteBackoffice(rs.getString("note_backoffice"));
			
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));

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
			bean.setImportoPerNap(rs.getBigDecimal("importo_per_nap"));
			bean.setCodBilAcc(rs.getString("cod_bil_acc"));
			bean.setAnno(rs.getString("anno"));
			bean.setDatiSpecRisc(rs.getString("dati_spec_risc"));
			bean.setImportoPerAccOrig(rs.getBigDecimal("importo_per_acc_orig"));
			bean.setIdAccertaBilancio(rs.getLong("id_accerta_bilancio"));
			bean.setSumImpPerAccOrig(rs.getBigDecimal("sum_imp_per_acc_orig"));
			bean.setProgrImpPerAccOrig(rs.getInt("progr_imp_per_acc_orig"));
			bean.setCountImpPerAccOrig(rs.getInt("count_imp_per_acc_orig"));
			bean.setPercTotImp(rs.getBigDecimal("perc_tot_imp"));
			
		}
			
	}


	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}