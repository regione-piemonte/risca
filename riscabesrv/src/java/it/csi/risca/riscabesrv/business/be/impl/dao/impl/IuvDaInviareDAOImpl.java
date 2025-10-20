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

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDaInviareDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.IuvDaInviareDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type IuvDaInviareDAOImpl dao.
 *
 * @author CSI PIEMONTE
 */
public class IuvDaInviareDAOImpl extends RiscaBeSrvGenericDAO<IuvDaInviareDTO> implements IuvDaInviareDAO {

	public static final String QUERY_INSERT = "INSERT INTO risca_r_iuv_da_inviare "
			+ "(id_iuv_da_inviare, id_iuv, id_elabora, id_lotto, flg_da_inviare, canone_dovuto, "
			+ "imp_versato, data_scad_pag, motivazione, importo_new, interessi, interes_rit_pag, "
			+ "tot_spese_notif_per_nap, ind_tipo_aggiornamento, flg_sd_annullato, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(:idIuvDaInviare, :idIuv, :idElabora, :idLotto, :flgDaInviare, :canoneDovuto, "
			+ ":impVersato, :dataScadPag, :motivazione, :importoNew, :interessi, :interesRitPag, "
			+ ":totSpeseNotifPerNap, :indTipoAggiornamento, :flgSdAnnullato, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid);";

	private static final String QUERY_IUV_DA_INVIARE_BY_ELABORA = "select * from risca_r_iuv_da_inviare "
			+ "where id_elabora = :idElabora and flg_da_inviare = 1 order by id_iuv";

	private static final String QUERY_ANNULLA_IUV_PER_IMPORTO_VERSATO = "UPDATE risca_r_iuv_da_inviare as t"
			+ "   SET flg_da_inviare = 1," 
			+ "       ind_tipo_aggiornamento = 'A',"
			+ "       motivazione = 'Annullamento per importo versato. NAP: ' || t2.nap,"
			+ "       gest_data_upd = :gestDataUpd, " 
			+ "       gest_attore_upd = :gestAttoreUpd"
			+ "  FROM risca_r_iuv_da_inviare t1, risca_t_iuv t2 " 
			+ " WHERE t.id_iuv_da_inviare = t1.id_iuv_da_inviare "
			+ "   AND t.id_iuv = t2.id_iuv" + "   AND t1.flg_da_inviare=0"
			+ "   AND (t1.canone_dovuto + t1.interes_rit_pag + t1.tot_spese_notif_per_nap) <= t1.imp_versato "
			+ "   AND t1.canone_dovuto <> 0 " 
			+ "   AND t1.flg_sd_annullato = 0" 
			+ "   AND t1.id_elabora = :idElabora;";

	private static final String QUERY_ANNULLA_IUV_PER_POS_DEB_ANNULLATA = "UPDATE risca_r_iuv_da_inviare as t"
			+ "   SET flg_da_inviare = 1,"
			+ "       ind_tipo_aggiornamento = 'A',"
			+ "       motivazione = 'Annullamento per posizione debitoria annullata/Non accertata. NAP: ' || t2.nap,"
			+ "       gest_data_upd = :gestDataUpd, "
			+ "       gest_attore_upd = :gestAttoreUpd"
			+ "  FROM risca_r_iuv_da_inviare t1, risca_t_iuv t2 "
			+ " WHERE t.id_iuv_da_inviare = t1.id_iuv_da_inviare "
			+ "   AND t.id_iuv = t2.id_iuv"
			+ "   AND t1.flg_da_inviare = 0 "
			+ "   AND t1.flg_sd_annullato = 1 "
			+ "   AND t1.id_elabora = :idElabora;";

	private static final String QUERY_ANNULLA_IUV_PER_CAMBIO_TITOLARE = "UPDATE risca_r_iuv_da_inviare as t "
			+ " SET flg_da_inviare = 1, "
			+ "    ind_tipo_aggiornamento = 'A', "
			+ "    motivazione = 'Annullamento per cambio titolare. NAP: ' || t2.nap, "
			+ "    gest_data_upd = :gestDataUpd,"
			+ "    gest_attore_upd = :gestAttoreUpd "
			+ " FROM risca_r_iuv_da_inviare t1, "
			+ "       ( select iuv.id_iuv, iuv.nap, iuv.id_stato_iuv, "
			+ "         sdc.id_stato_debitorio, sdc.id_riscossione, sdc.id_soggetto, sdc.id_soggetto_avviso "
			+ "         from risca_t_iuv iuv "
			+ "         join ( "
			+ "             select sd.id_stato_debitorio, sd.id_riscossione, sd.id_soggetto, sd.nap, adt.id_soggetto as id_soggetto_avviso "
			+ "             from risca_t_stato_debitorio sd "
			+ "             join risca_r_avviso_dati_titolare adt on sd.nap = adt.nap "
			+ "             and sd.id_soggetto <> adt.id_soggetto) sdc on iuv.nap = sdc.nap "
			+ "        ) t2 "
			+ " WHERE t.id_iuv_da_inviare = t1.id_iuv_da_inviare "
			+ " AND t.id_iuv = t2.id_iuv "
			+ " AND t1.flg_da_inviare = 0 "
			+ " AND t1.id_elabora = :idElabora ";

	private static final String QUERY_ANNULLA_IUV_PER_EMISSIONE_A_RUOLO = 		
			" UPDATE RISCA_R_IUV_DA_INVIARE as t "
			+ "   SET flg_da_inviare= 1, "
			+ "       ind_tipo_aggiornamento= 'A',  "
			+ "       motivazione ='Annullamento per emissione a ruolo. NAP: ' || t1.nap, "
			+ "       gest_data_upd = :gestDataUpd, "
			+ "       gest_attore_upd = :gestAttoreUpd "
			+ "  from (   select daa.* "
			+ "			 FROM ( "
			+ "			       select rii.*, t2.nap  "
			+ "			         from RISCA_R_IUV_DA_INVIARE rii "
			+ "               inner join risca_t_iuv t2 on rii.id_iuv = t2.id_iuv "
			+ "			      ) daa "
			+ " 			inner join ( "
			+ "   			                 select lna.nap, sum(valido) as validita from ( "
			+ "			                       select sd.id_stato_debitorio, sd.nap, acc.id_tipo_accertamento, "
			+ "			                              case  "
			+ "			                              	when id_tipo_accertamento = 3 then 0   "
			+ "			                              	else 1 "
			+ "			                              end as valido "
			+ "			                         from risca_t_stato_debitorio sd  "
			+ "			                   inner join (  " 
			+ "			                               select acc.* "
			+ "                                             from risca_t_accertamento acc "
			+ "                                       inner join ( select acm.id_stato_debitorio, max(acm.data_protocollo) as max_data_ptrotocollo "
			+ "                                                      from risca_t_accertamento acm "
			+ "                                                  group by acm.id_stato_debitorio    "                                   
			+ "                                                   ) lac on acc.id_stato_debitorio = lac.id_stato_debitorio  "
			+ "                                                        and acc.data_protocollo = lac.max_data_ptrotocollo "
			+ "			                              ) acc on sd.id_stato_debitorio = acc.id_stato_debitorio  "
			+ "			                    order by nap "
			+ "                               ) lna "
			+ "			                 group by lna.nap  "
			+ "		                 ) nva on daa.nap = nva.nap   "    
			+ "		                      and nva.validita=0  "
			+ "           where daa.flg_da_inviare=0 "
			+ "             AND daa.id_elabora =  :idElabora "
			+ "           order by daa.nap   "
			+ "	     ) t1		    "
			+ "	 WHERE t.id_iuv_da_inviare = t1.id_iuv_da_inviare ";

	private static final String QUERY_MODIFICA_IUV_PER_CANONE_DOVUTO = "UPDATE risca_r_iuv_da_inviare as t"
			+ "   SET flg_da_inviare = 1,"
			+ "       ind_tipo_aggiornamento = 'M',"
			+ "       motivazione = 'Modifica importo da euro '||t2.importo||' a euro '|| t1.canone_dovuto||'. NAP: '|| t2.nap ,"
			+ "       importo_new = t1.canone_dovuto, "
			+ "       gest_data_upd = :gestDataUpd, "
			+ "       gest_attore_upd = :gestAttoreUpd "
			+ "  FROM risca_r_iuv_da_inviare t1, risca_t_iuv t2 "
			+ " WHERE t.id_iuv_da_inviare = t1.id_iuv_da_inviare "
			+ "   AND t.id_iuv = t2.id_iuv "
			+ "   AND t1.flg_da_inviare= 0 "
			+ "   AND t1.imp_versato = 0 "
			+ "   AND t2.importo <> t1.canone_dovuto "
			+ "   AND t1.data_scad_pag >= CURRENT_DATE "
			+ "   AND t1.id_elabora = :idElabora";
	
	private static final String QUERY_MODIFICA_IUV_PER_IMPORTO_VERSATO = "UPDATE risca_r_iuv_da_inviare as t"
			+ "   SET flg_da_inviare = 1,"
			+ "       ind_tipo_aggiornamento = 'M',"
			+ "       motivazione = 'Integrazione a quanto già versato. NAP: '|| t2.nap, "
			+ "       importo_new = (t1.canone_dovuto - t1.imp_versato), "
			+ "       gest_data_upd = :gestDataUpd,"
			+ "       gest_attore_upd = :gestAttoreUpd"
			+ "  FROM risca_r_iuv_da_inviare t1, risca_t_iuv t2 "
			+ " WHERE t.id_iuv_da_inviare = t1.id_iuv_da_inviare "
			+ "   AND t.id_iuv = t2.id_iuv  "
			+ "   AND t1.flg_da_inviare = 0 "
			+ "   AND t1.imp_versato <> 0 "
			+ "   AND t2.importo <> (t1.canone_dovuto - t1.imp_versato) "
			+ "   AND t1.canone_dovuto > t1.imp_versato "
			+ "   AND t1.data_scad_pag >= CURRENT_DATE "
			+ "   AND t1.id_elabora = :idElabora";
	
	private static final String QUERY_MODIFICA_IUV_PER_ADDEBITO_INTERESSI = "UPDATE risca_r_iuv_da_inviare as t"
			+ "   SET flg_da_inviare = 1,"
			+ "       ind_tipo_aggiornamento = 'M',"
			+ "       motivazione = 'Modifica importo per addebito interessi. NAP: '|| t2.nap,"
			+ "       importo_new = (t1.canone_dovuto + t1.interes_rit_pag + t1.tot_spese_notif_per_nap) - t1.imp_versato + t1.interessi, "
			+ "       gest_data_upd = :gestDataUpd,"
			+ "       gest_attore_upd = :gestAttoreUpd"
			+ "  FROM risca_r_iuv_da_inviare t1, risca_t_iuv t2 "
			+ " WHERE t.id_iuv_da_inviare = t1.id_iuv_da_inviare "
			+ "   AND t.id_iuv = t2.id_iuv  "
			+ "   AND t1.flg_da_inviare = 0 "
			+ "   AND (t1.canone_dovuto + t1.interes_rit_pag + t1.tot_spese_notif_per_nap) > t1.imp_versato "
			+ "   AND t2.importo <> (t1.canone_dovuto + t1.interes_rit_pag + t1.tot_spese_notif_per_nap) - t1.imp_versato + t1.interessi "
			+ "   AND t1.data_scad_pag < CURRENT_DATE "
			+ "   AND t1.id_elabora = :idElabora";
	
	public static final String QUERY_COUNT_IUV_DA_INVIARE_BY_ID_IUV = "select  COUNT(*) from risca_r_iuv_da_inviare WHERE id_iuv = :idIuv and id_elabora = :idElabora ";

	private static final String QUERY_UPDATE_IUV_DA_INVIARE = "UPDATE risca_r_iuv_da_inviare "
			+ "   SET interessi = interessi + :interessi , " 
			+ "       interes_rit_pag = interes_rit_pag + :interesRitPag, "
			+ "       canone_dovuto =:canoneDovuto, imp_versato = :impVersato, tot_spese_notif_per_nap =:totSpeseNotifPerNap, flg_sd_annullato = :flgSdAnnullato, "
			+ "       gest_attore_upd = :gestAttoreUpd,  gest_data_upd = :gestDataUpd "
			+ "   where id_iuv = :idIuv and id_elabora = :idElabora";
	
	private static final String QUERY_MODIFICA_IUV_INIZIO_ANNO = " update RISCA_R_IUV_DA_INVIARE as t "
			+ "   set flg_da_inviare = 1, "
			+ "       ind_tipo_aggiornamento = 'M', "
			+ "       importo_new = (t.canone_dovuto + t.interes_rit_pag + t.tot_spese_notif_per_nap) - t.imp_versato + t.interessi,  "
			+ "       motivazione ='Modifica IUV inizio anno. NAP: '||t1.nap, "
			+ "       gest_data_upd = :gestDataUpd , "
			+ "	      gest_attore_upd = :gestAttoreUpd "
			+ "       FROM ( "
			+ "          select idi.id_iuv_da_inviare, idi.id_iuv, idi.id_elabora, idi.canone_dovuto, idi.interes_rit_pag, "
			+ "          		 idi.tot_spese_notif_per_nap, idi.imp_versato, idi.interessi,	 "
			+ "                 idi.flg_da_inviare, idi.ind_tipo_aggiornamento, idi.importo_new, idi.motivazione,  "
			+ "                 iuv.nap, iuv.importo  "
			+ "            from risca_r_iuv_da_inviare idi "
			+ "           left join risca_t_iuv iuv on idi.id_iuv = iuv.id_iuv "
			+ "           inner join (  "
			+ "               select avp.nap, avp.id_spedizione, avp.imp_totale_dovuto, uls.tipo "
			+ "                 from risca_t_avviso_pagamento avp  "
			+ "               inner join ( "
			+ "                          select max(id_spedizione) id_spedizione, 'Bollettazione Ordinaria' as tipo  "
			+ "                            from risca_t_spedizione  "
			+ "                           where id_tipo_spedizione = 1  "
			+ "                        union all "
			+ "                           select max(id_spedizione) , 'Bollettazione Speciale' as tipo "
			+ "                             from risca_t_spedizione  "
			+ "                            where id_tipo_spedizione = 2  "
			+ "                          ) uls on avp.id_spedizione = uls.id_spedizione "
			+ "                      ) nav on iuv.nap = nav.nap "
			+ "           where idi.flg_da_inviare = 0    "
			+ "             and idi.id_elabora = :idElabora "
			+ "       ) t1 "
			+ " WHERE t.id_iuv_da_inviare = t1.id_iuv_da_inviare ";
	

	private static final String QUERY_ANNULLA_INTERESSI_BY_ID_ELABORA = "UPDATE risca_r_iuv_da_inviare "
			+ "   SET interessi = 0 , " 
			+"        gest_data_upd = :gestDataUpd, " 
			+ "	      gest_attore_upd = :gestAttoreUpd "
			+ "   where DATE(data_scad_pag) > DATE(current_date) "
			+ "   and imp_versato = 0 "
			+ "   and interessi > 0  "
			+ "   and id_elabora =  :idElabora ";
	
	
	private static final String QUERY_DELETE_IUV_DA_INVIARE  = "DELETE from risca_r_iuv_da_inviare rridi " + 
			"			where rridi.id_iuv_da_inviare in (  select rridi2.id_iuv_da_inviare " + 
			"            from risca_r_iuv_da_inviare rridi2 " + 
			"      inner join risca_t_elabora ela on rridi2.id_elabora = ela.id_elabora " + 
			"           where DATE(rridi2.gest_data_ins) < DATE(current_date-7) " + 
			"             and ela.id_ambito = :idAmbito ) ";
	
	@Override
	public IuvDaInviareDTO saveIuvDaInviare(IuvDaInviareDTO dto) throws DAOException {
		LOGGER.debug("[IuvDaInviareDAOImpl::saveIuvDaInviare] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_r_iuv_da_inviare");

			map.put("idIuvDaInviare", genId);
			map.put("idIuv", dto.getIdIuv());
			map.put("idElabora", dto.getIdElabora());
			map.put("idLotto", dto.getIdLotto());
			map.put("flgDaInviare", dto.getFlgDaInviare());
			map.put("canoneDovuto", dto.getCanoneDovuto());
			map.put("impVersato", dto.getImpVersato());
			map.put("dataScadPag", dto.getDataScadPag());
			map.put("motivazione", dto.getMotivazione());
			map.put("importoNew", dto.getImportoNew());
			map.put("interessi", dto.getInteressi());
			map.put("interesRitPag", dto.getInteresRitPag());
			map.put("totSpeseNotifPerNap", dto.getTotSpeseNotifPerNap());
			map.put("indTipoAggiornamento", dto.getIndTipoAggiornamento());
			map.put("flgSdAnnullato", dto.getFlgSdAnnullato());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
			dto.setIdIuv(genId);
		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::saveIuvDaInviare] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(e.getMessage());
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::saveIuv] END");
		}

		return dto;
	}

	@Override
	public List<IuvDaInviareDTO> getIuvDaInviare(Long idElabora) {
		LOGGER.debug("[IuvDaInviareDAOImpl::getIuvDaInviare] BEGIN");
		Map<String, Object> map = new HashMap<>();
		List<IuvDaInviareDTO> iuvDaInviare = new ArrayList<IuvDaInviareDTO>();

		map.put("idElabora", idElabora);
		MapSqlParameterSource params = getParameterValue(map);
		try {
			iuvDaInviare = template.query(QUERY_IUV_DA_INVIARE_BY_ELABORA, params, getRowMapper());
		} catch (DataAccessException | SQLException e) {
			LOGGER.debug("[IuvDaInviareDAOImpl::getIuvDaInviare] ERROR " + e.getMessage());
			return null;
		}

		LOGGER.debug("[IuvDaInviareDAOImpl::getIuvDaInviare] END");
		return iuvDaInviare;
	}

	@Override
	public Integer annullaIuvPerImportoVersato(Long idElabora, String attore) {
		LOGGER.debug("[IuvDaInviareDAOImpl::annullaIuvPerImportoVersato] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", attore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_ANNULLA_IUV_PER_IMPORTO_VERSATO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::annullaIuvPerImportoVersato] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::annullaIuvPerImportoVersato] END");
		}
	}

	@Override
	public Integer annullaIuvPerPosizioneDebitoriaAnnullata(Long idElabora, String attore) {
		LOGGER.debug("[IuvDaInviareDAOImpl::annullaIuvPerPosizioneDebitoriaAnnullata] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", attore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_ANNULLA_IUV_PER_POS_DEB_ANNULLATA, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::annullaIuvPerPosizioneDebitoriaAnnullata] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::annullaIuvPerPosizioneDebitoriaAnnullata] END");
		}
	}

	@Override
	public Integer annullaIuvPerCambioTitolare(Long idElabora, String attore) {
		LOGGER.debug("[IuvDaInviareDAOImpl::annullaIuvPerCambioTitolare] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", attore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_ANNULLA_IUV_PER_CAMBIO_TITOLARE, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::annullaIuvPerCambioTitolare] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::annullaIuvPerCambioTitolare] END");
		}
	}

	@Override
	public Integer annullaIuvPerEmissioneARuolo(Long idElabora, String attore) {
		LOGGER.debug("[IuvDaInviareDAOImpl::annullaIuvPerEmissioneARuolo] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", attore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_ANNULLA_IUV_PER_EMISSIONE_A_RUOLO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::annullaIuvPerEmissioneARuolo] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::annullaIuvPerEmissioneARuolo] END");
		}
	}

	@Override
	public Integer modificaImportoIuvPerCanoneDovuto(Long idElabora, String attore) {
		LOGGER.debug("[IuvDaInviareDAOImpl::modificaImportoIuvPerCanoneDovuto] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", attore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_MODIFICA_IUV_PER_CANONE_DOVUTO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::modificaImportoIuvPerCanoneDovuto] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::modificaImportoIuvPerCanoneDovuto] END");
		}
	}

	@Override
	public Integer modificaImportoIuvPerImportoVersato(Long idElabora, String attore) {
		LOGGER.debug("[IuvDaInviareDAOImpl::modificaImportoIuvPerImportoVersato] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", attore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_MODIFICA_IUV_PER_IMPORTO_VERSATO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::modificaImportoIuvPerImportoVersato] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::modificaImportoIuvPerImportoVersato] END");
		}
	}

	@Override
	public Integer modificaImportoIuvPerAddebitoInteressi(Long idElabora, String attore) {
		LOGGER.debug("[IuvDaInviareDAOImpl::modificaImportoIuvPerAddebitoInteressi] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", attore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_MODIFICA_IUV_PER_ADDEBITO_INTERESSI, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::modificaImportoIuvPerAddebitoInteressi] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::modificaImportoIuvPerAddebitoInteressi] END");
		}
	}
	
	
	@Override
	public Integer aggiornaIuvDiInizioAnno(Long idElabora, String attore) {
		LOGGER.debug("[IuvDaInviareDAOImpl::aggiornaIuvDiInizioAnno] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", attore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_MODIFICA_IUV_INIZIO_ANNO, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::aggiornaIuvDiInizioAnno] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::aggiornaIuvDiInizioAnno] END");
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
	public RowMapper<IuvDaInviareDTO> getRowMapper() throws SQLException {
		return new IuvRowMapper();
	}

	@Override
	public RowMapper<IuvDaInviareDTO> getExtendedRowMapper() throws SQLException {
		return new IuvRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class IuvRowMapper implements RowMapper<IuvDaInviareDTO> {

		/**
		 * Instantiates a new row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public IuvRowMapper() throws SQLException {
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
		public IuvDaInviareDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			IuvDaInviareDTO bean = new IuvDaInviareDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, IuvDaInviareDTO bean) throws SQLException {
			bean.setIdIuvDaInviare(rs.getLong("id_iuv_da_inviare"));
			bean.setIdIuv(rs.getLong("id_iuv"));
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setIdLotto(rs.getLong("id_lotto"));
			bean.setFlgDaInviare(rs.getInt("flg_da_inviare"));
			bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
			bean.setImpVersato(rs.getBigDecimal("imp_versato"));
			bean.setDataScadPag(rs.getDate("data_scad_pag"));
			bean.setMotivazione(rs.getString("motivazione"));
			bean.setImportoNew(rs.getBigDecimal("importo_new"));
			bean.setInteressi(rs.getBigDecimal("interessi"));
			bean.setInteresRitPag(rs.getBigDecimal("interes_rit_pag"));
			bean.setTotSpeseNotifPerNap(rs.getBigDecimal("tot_spese_notif_per_nap"));
			bean.setIndTipoAggiornamento(rs.getString("ind_tipo_aggiornamento"));
			bean.setFlgSdAnnullato(rs.getInt("flg_sd_annullato"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}

	}
	
	@Override
	public Integer countIuvDaInviareByIdIuv(Long idIuv, Long idElabora) throws DAOException {
		LOGGER.debug("[IuvDaInviareDAOImpl::countIuvDaInviareByIdIuv] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idIuv", idIuv);
			map.put("idElabora", idElabora);
        	MapSqlParameterSource params = getParameterValue(map);
			return 	template.queryForObject( QUERY_COUNT_IUV_DA_INVIARE_BY_ID_IUV , params, Integer.class);
						
		}catch (DataAccessException e) {
				LOGGER.error("[IuvDaInviareDAOImpl::countIuvDaInviareByIdIuv] ERROR : " +e);
				throw new DAOException(e);
			}
	}
	
	@Override
	public Integer updateIuvDaInviare(IuvDaInviareDTO iuvDaInviare) throws DAOException{
		LOGGER.debug("[IuvDaInviareDAOImpl::updateIuvDaInviare] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("interessi", iuvDaInviare.getInteressi());
			map.put("interesRitPag", iuvDaInviare.getInteresRitPag());
			map.put("canoneDovuto", iuvDaInviare.getCanoneDovuto());
			map.put("impVersato", iuvDaInviare.getImpVersato());
			map.put("totSpeseNotifPerNap", iuvDaInviare.getTotSpeseNotifPerNap());
			map.put("flgSdAnnullato", iuvDaInviare.getFlgSdAnnullato());
			map.put("idIuv", iuvDaInviare.getIdIuv());
			map.put("idElabora", iuvDaInviare.getIdElabora());
			map.put("gestAttoreUpd", iuvDaInviare.getGestAttoreUpd());
			map.put("gestDataUpd", now);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_UPDATE_IUV_DA_INVIARE, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::updateIuvDaInviare] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::modificaImportoIuvPerAddebitoInteressi] END");
		}
	}
	
	@Override
	public Integer annullaInteressiByIdElabora(Long idElabora, String attore) {
		LOGGER.debug("[IuvDaInviareDAOImpl::annullaInteressiByIdElabora] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", attore);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_ANNULLA_INTERESSI_BY_ID_ELABORA, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[IuvDaInviareDAOImpl::annullaInteressiByIdElabora] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::annullaInteressiByIdElabora] END");
		}
	}
	
	@Override
	public Integer deleteIuvDaInviare(Long idAmbito) throws DAOException {
		LOGGER.debug("[IuvDaInviareDAOImpl::deleteIuvDaInviare] BEGIN");
		LOGGER.debug("[IuvDaInviareDAOImpl::deleteIuvDaInviare] idAmbito: "+idAmbito);
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_IUV_DA_INVIARE, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[IuvDaInviareDAOImpl::deleteIuvDaInviare] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[IuvDaInviareDAOImpl::deleteIuvDaInviare] END");
		}

		return res;
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}
