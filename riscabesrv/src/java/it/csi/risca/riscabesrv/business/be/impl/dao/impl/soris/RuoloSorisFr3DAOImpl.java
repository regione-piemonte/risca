/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.soris;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.soris.RuoloSorisFr3DAO;
import it.csi.risca.riscabesrv.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr3DTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type RuoloSorisFr3 dao.
 *
 * @author CSI PIEMONTE
 */
public class RuoloSorisFr3DAOImpl  extends RiscaBeSrvGenericDAO<RuoloSorisFr3DTO> implements RuoloSorisFr3DAO {
	
	private static final String QUERY_INSERT = 
			" insert into risca_r_ruolo_soris_fr3 "+
			" (id_ruolo_soris_fr3, id_ruolo, id_file_soris,id_pagamento, tipo_record, utenza, progr_record,  " +
			" cod_ambito, cod_ente_creditore, anno_ruolo, numero_ruolo, p_tipo_ufficio,   " + 
			" p_codice_ufficio, p_anno_riferimento, p_tipo_modello, p_ident_prenot_ruolo,  " +
			" p_ident_atto,progr_articolo_ruolo, identif_cartella, progr_articolo_cartella, codice_entrata,   " +
			" tipo_entrata, codice_fiscale,data_evento, importo_carico_risco,importo_interessi, importo_aggio_ente, " +
			" importo_aggio_contrib, t_spese_proc_esec,t_spese_proc_esec_p_lista, codice_divisa, modalita_pagam, data_registr, "+
			" num_operaz_contabile, progr_inter_op_contab, note, gest_data_ins, gest_attore_ins, gest_attore_upd, gest_data_upd, gest_uid ) " +
			" VALUES ( " +
		    " :idRuoloSorisFr3, :idRuolo, :idFileSoris, :idPagamento, :tipoRecord, :utenza, :progrRecord, " +
			" :codAmbito, :codEnteCreditore, :annoRuolo, :numeroRuolo, :pTipoUfficio, "+
			" :pCodiceUfficio, :pAnnoRiferimento, :pTipoModello, :pIdentPrenotRuolo, "+
			" :pIdentAtto, :progrArticoloRuolo, :identifCartella, :progrArticoloCartella, :codiceEntrata, "+
			" :tipoEntrata, :codiceFiscale, :dataEvento, :importoCaricoRisco,  :importoInteressi, :importoAggioEnte, "+
			" :importoAggioContrib, :tSpeseProcEsec, :tSpeseProcEsecPLista, :codiceDivisa, :modalitaPagam, :dataRegistr, "+
		    " :numOperazContabile, :progrInterOpContab, :note , :gestDataIns, :gestAttoreIns, :gestAttoreUpd, :gestDataUpd, :gestUid) ";
	
		
	private static final String QUERY_LOAD = 
			"with totale_rate_sd as ( " + 
			"                        select fin.* from ( " + 
			"                        select det.cod_riscossione, det.anno_data_scadenza_pagamento, count(*) as quanti " + 
			"                        from ( " + 
			"                              select ris.id_riscossione, " + 
			"                                     ris.cod_riscossione, " + 
			"                                     to_char(rrs.data_scadenza_pagamento,'YYYY') as anno_data_scadenza_pagamento " + 
			"                                from risca_t_riscossione ris " + 
			"                          inner join risca_t_stato_debitorio sd on ris.id_riscossione = sd.id_riscossione " + 
			"                          inner join risca_t_accertamento acc on sd.id_stato_debitorio = acc.id_stato_debitorio " + 
			"                                                             and acc.id_tipo_accertamento = 3 " + 
			"                                                             and acc.flg_annullato = 0 " + 
			"                          inner join risca_r_rata_sd rrs on sd.id_stato_debitorio = rrs.id_stato_debitorio " + 
			"                                                        and rrs.id_rata_sd_padre is null  " + 
			"                            ) det " + 
			"                        group by det.cod_riscossione, det.anno_data_scadenza_pagamento " + 
			"                        ) fin " + 
			"                     ) " + 
			"select soris_tot.utenza,  " + 
			"       soris_tot.p_ident_prenot_ruolo,  " + 
			"       soris_tot.p_anno_riferimento,  " + 
			"       soris_tot.identif_cartella,  " + 
			"       soris_tot.tot_riscosso, " + 
			"       soris_tot.data_evento, " + 
			"       rata_pagamento.id_riscossione, " + 
			"       rata_pagamento.id_stato_debitorio, " + 
			"       rata_pagamento.id_rata_sd, " + 
			"       rata_pagamento.anno_data_scadenza_pagamento " + 
			"from (  " + 
			"      select distinct p_ident_prenot_ruolo,  " + 
			"                      utenza, " + 
			"                      p_anno_riferimento,  " + 
			"                      identif_cartella, " + 
			"                      codice_fiscale,  " + 
			"                      data_evento,  " + 
			"                      sum(importo_carico_risco) tot_riscosso  " + 
			"        from risca_r_ruolo_soris_fr3 " + 
			"        where importo_carico_risco <> 0 " + 
			"          and id_file_soris = :idFileSoris  " + 
			"          and to_char(data_registr,'YYYYMMDD')|| " + 
			"              anno_ruolo|| " + 
			"              numero_ruolo|| " + 
			"              p_tipo_ufficio|| " + 
			"              p_codice_ufficio|| " + 
			"              p_anno_riferimento|| " + 
			"              p_tipo_modello|| " + 
			"              p_ident_prenot_ruolo|| " + 
			"              p_ident_atto|| " + 
			"              '00'|| " + 
			"              progr_articolo_ruolo|| " + 
			"              to_char(data_evento,'YYYYMMDD')|| " + 
			"              num_operaz_contabile||  " + 
			"              progr_inter_op_contab	"+
			"      not in (SELECT chiave_inf_annullare FROM risca_w_soris_fr7) " + 
			"        group by p_ident_prenot_ruolo, utenza, p_anno_riferimento, identif_cartella, codice_fiscale, data_evento " + 
			"     ) soris_tot " + 
			"   left join ( " + 
			"              select * from (                        " + 
			"                             select ris.id_riscossione, " + 
			"                                    ris.cod_riscossione, " + 
			"                                    sd.id_stato_debitorio, " + 
			"                                    acc.id_accertamento, " + 
			"                                    rrs.id_rata_sd, " + 
			"                                    rrs.data_scadenza_pagamento, " + 
			"                                    to_char(rrs.data_scadenza_pagamento,'YYYY') as anno_data_scadenza_pagamento " + 
			"                               from risca_t_riscossione ris " + 
			"                         inner join risca_t_stato_debitorio sd on ris.id_riscossione = sd.id_riscossione " + 
			"                         inner join risca_t_accertamento acc on sd.id_stato_debitorio = acc.id_stato_debitorio " + 
			"                                                            and acc.id_tipo_accertamento = 3 " + 
			"                                                            and acc.flg_annullato = 0 " + 
			"                         inner join risca_r_rata_sd rrs on sd.id_stato_debitorio = rrs.id_stato_debitorio " + 
			"                                                       and rrs.id_rata_sd_padre is null " + 
			"                         inner join totale_rate_sd trs on ris.cod_riscossione = trs.cod_riscossione " + 
			"                                                       and to_char(rrs.data_scadenza_pagamento,'YYYY') = trs.anno_data_scadenza_pagamento " + 
			"                                                       and trs.quanti <2 " + 
			"                       union all                      " + 
			"                            select distinct ris.id_riscossione, " + 
			"                                   ris.cod_riscossione, " + 
			"                                   0 as id_stato_debitorio, " + 
			"                                   0 as id_accertamento, " + 
			"                                   0 as id_rata_sd, " + 
			"                                   to_date('1900-01-01','YYYY-MM-DD') as data_scadenza_pagamento, " + 
			"                                   trs.anno_data_scadenza_pagamento " + 
			"                              from risca_t_riscossione ris " + 
			"                        inner join risca_t_stato_debitorio sd on ris.id_riscossione = sd.id_riscossione " + 
			"                        inner join risca_t_accertamento acc on sd.id_stato_debitorio = acc.id_stato_debitorio " + 
			"                                                           and acc.id_tipo_accertamento = 3 " + 
			"                                                           and acc.flg_annullato = 0 " + 
			"                        inner join risca_r_rata_sd rrs on sd.id_stato_debitorio = rrs.id_stato_debitorio " + 
			"                                                      and rrs.id_rata_sd_padre is null " + 
			"                        inner join totale_rate_sd trs on ris.cod_riscossione = trs.cod_riscossione " + 
			"                                                      and to_char(rrs.data_scadenza_pagamento,'YYYY') = trs.anno_data_scadenza_pagamento " + 
			"                                                      and trs.quanti =2 " + 
			"                              ) uni                                 " + 
			"             ) rata_pagamento on soris_tot.utenza = rata_pagamento.cod_riscossione " + 
			"                             and trim(soris_tot.p_anno_riferimento) = anno_data_scadenza_pagamento" ;
			
			
	public static final String QUERY_UPDATE = "update risca_r_ruolo_soris_fr3 "
			+ " set id_pagamento = :idPagamento, gest_data_upd = :gestDataUpd , "
			+ "gest_attore_upd= :gestAttoreUpd "
			+ " where p_ident_prenot_ruolo  = :pIdentPrenotRuolo and p_anno_riferimento = :pAnnoRiferimento and id_file_soris = :idFileSoris and data_evento = :dataEvento ";
	

	@Override
	public RuoloSorisFr3DTO saveRuoloSorisFr3(RuoloSorisFr3DTO dto) {
		LOGGER.debug("[RuoloSorisFr3DAOImpl::saveRuoloSorisFr3] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			String gestUID = null;         
			Long genId = findNextSequenceValue("seq_risca_r_ruolo_soris_fr3");
			map.put("idRuoloSorisFr3", genId);
			map.put("idRuolo", dto.getIdRuolo());
			map.put("idFileSoris", dto.getIdFileSoris());
			map.put("idPagamento", dto.getIdPagamento());
			map.put("tipoRecord", dto.getTipoRecord());
			map.put("utenza", dto.getUtenza());
			map.put("progrRecord", dto.getProgrRecord());
			map.put("codAmbito", dto.getCodAmbito());
			map.put("codEnteCreditore", dto.getCodEnteCreditore());
			map.put("annoRuolo", dto.getAnnoRuolo());
			map.put("numeroRuolo", dto.getNumeroRuolo());
			map.put("pTipoUfficio", dto.getpTipoUfficio());
			
			map.put("pCodiceUfficio", dto.getpCodiceUfficio());
			map.put("pAnnoRiferimento", dto.getpAnnoRiferimento());
			
			map.put("pTipoModello", dto.getpTipoModello());
			map.put("pIdentPrenotRuolo", dto.getpIdentPrenotRuolo());
			map.put("pIdentAtto", dto.getpIdentAtto());
			map.put("progrArticoloRuolo", dto.getProgrArticoloRuolo());
			map.put("identifCartella", dto.getIdentifCartella());
			map.put("progrArticoloCartella", dto.getProgrArticoloCartella());
			map.put("codiceEntrata", dto.getCodiceEntrata());
			map.put("tipoEntrata", dto.getTipoEntrata());
			map.put("codiceFiscale", dto.getCodiceFiscale());
			
			map.put("dataEvento", dto.getDataEvento());
			map.put("importoCaricoRisco", dto.getImportoCaricoRisco());
			map.put("importoInteressi", dto.getImportoInteressi());
			map.put("importoAggioEnte", dto.getImportoAggioEnte());
			map.put("importoAggioContrib", dto.getImportoAggioContrib());
			map.put("tSpeseProcEsec", dto.gettSpeseProcEsec());
			map.put("tSpeseProcEsecPLista", dto.gettSpeseProcEsecPLista());
			map.put("codiceDivisa", dto.getCodiceDivisa());
			map.put("modalitaPagam", dto.getModalitaPagam());
			map.put("dataRegistr", dto.getDataRegistr());
			map.put("numOperazContabile", dto.getNumOperazContabile());
			map.put("progrInterOpContab", dto.getProgrInterOpContab());
			map.put("note", dto.getNote());
			
			
			gestUID = generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now);
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", gestUID);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			dto.setGestUid(gestUID);
			dto.setIdRuoloSorisFr3(genId);
			return dto;

		} catch (SQLException e) {
			LOGGER.error("[RuoloSorisFr3DAOImpl::saveRuoloSorisFr3] Errore nell'esecuzione della query", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RuoloSorisFr3DAOImpl::saveRuoloSorisFr3] Errore nell'accesso ai dati", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;
		} finally {
			LOGGER.debug("[RuoloSorisFr3DAOImpl::saveRuoloSorisFr3] END");
		}

	}

	
	@Override
	public List<RuoloSorisFr3DTO> loadEstrazioneDatiSorisPerInsertPagamenti(Long idElabora, Long idFileSoris) {
		LOGGER.debug("[RuoloSorisFr3DAOImpl::loadEstrazioneDatiSorisPerInsertPagamenti] BEGIN");
		LOGGER.debug("[RuoloSorisFr3DAOImpl::loadEstrazioneDatiSorisPerInsertPagamenti] idElabora: "+idElabora);
		LOGGER.debug("[RuoloSorisFr3DAOImpl::loadEstrazioneDatiSorisPerInsertPagamenti] idFileSoris: "+idFileSoris);
		List<RuoloSorisFr3DTO> listRuoloSorisFr3 = new ArrayList<RuoloSorisFr3DTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idFileSoris", idFileSoris);
			MapSqlParameterSource params = getParameterValue(map);
			listRuoloSorisFr3 = template.query(getQuery(QUERY_LOAD, null, null), params,
					new RuoloSorisFr3RowMapper());
		} catch (Exception e) {
			LOGGER.error("[RuoloSorisFr3DAOImpl::loadEstrazioneDatiSorisPerInsertPagamenti] Errore generale ", e);
			LOGGER.debug("[RuoloSorisFr3DAOImpl::loadEstrazioneDatiSorisPerInsertPagamenti] END");
			return listRuoloSorisFr3;
		}
		LOGGER.debug("[RuoloSorisFr3DAOImpl::loadEstrazioneDatiSorisPerInsertPagamenti] END");
		return listRuoloSorisFr3;
	}
	
	
	@Override
	public RuoloSorisFr3DTO updateRuoloSorisFr3(RuoloSorisFr3DTO dto) throws DAOException {
		LOGGER.debug("[RuoloSorisFr3DAOImpl::updateRuoloSorisFr3] BEGIN");
		LOGGER.debug("[RuoloSorisFr3DAOImpl::updateRuoloSorisFr3] idPagamento: "+dto.getIdPagamento());
		LOGGER.debug("[RuoloSorisFr3DAOImpl::updateRuoloSorisFr3] pIdentPrenotRuolo"+dto.getpIdentPrenotRuolo());
		LOGGER.debug("[RuoloSorisFr3DAOImpl::updateRuoloSorisFr3] pAnnoRiferimento"+dto.getpAnnoRiferimento());
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();	
			map.put("idPagamento", dto.getIdPagamento());
			map.put("pIdentPrenotRuolo", dto.getpIdentPrenotRuolo());
			map.put("pAnnoRiferimento", dto.getpAnnoRiferimento());
			map.put("idFileSoris", dto.getIdFileSoris());
			map.put("dataEvento", dto.getDataEvento());
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_UPDATE;
			template.update(getQuery(query, null, null), params);
			dto.setGestDataUpd(now);
			return dto;
		} catch (Exception e) {
			LOGGER.error("[RuoloSorisFr3DAOImpl::updateRuoloSorisFr3] ERROR : ",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RuoloSorisFr3DAOImpl::updateRuoloSorisFr3] END");
		}
	}
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RowMapper<RuoloSorisFr3DTO> getRowMapper() throws SQLException {
		return new RuoloSorisFr3RowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<RuoloSorisFr3DTO> getExtendedRowMapper() throws SQLException {
		return new RuoloSorisFr3RowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class RuoloSorisFr3RowMapper implements RowMapper<RuoloSorisFr3DTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RuoloSorisFr3RowMapper() throws SQLException {
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
		public RuoloSorisFr3DTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RuoloSorisFr3DTO bean = new RuoloSorisFr3DTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RuoloSorisFr3DTO bean) throws SQLException {		
			
			if(rsHasColumn(rs, "p_ident_prenot_ruolo"))bean.setpIdentPrenotRuolo(rs.getString("p_ident_prenot_ruolo"));
			if(rsHasColumn(rs, "p_anno_riferimento"))bean.setpAnnoRiferimento(rs.getString("p_anno_riferimento"));
			if(rsHasColumn(rs, "tot_riscosso"))bean.setTotRiscosso(rs.getBigDecimal("tot_riscosso"));
			if(rsHasColumn(rs, "id_stato_debitorio"))bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			if(rsHasColumn(rs, "id_rata_sd"))bean.setIdRataSd(rs.getLong("id_rata_sd"));		
			if(rsHasColumn(rs, "data_evento"))bean.setDataEvento(rs.getDate("data_evento"));
			if(rsHasColumn(rs, "utenza"))bean.setUtenza(rs.getString("utenza"));
			if(rsHasColumn(rs, "id_ruolo_soris_fr3"))bean.setIdRuoloSorisFr3(rs.getLong("id_ruolo_soris_fr3"));
			if(rsHasColumn(rs, "identif_cartella"))bean.setIdentifCartella(rs.getString("identif_cartella"));
			
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

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}


}