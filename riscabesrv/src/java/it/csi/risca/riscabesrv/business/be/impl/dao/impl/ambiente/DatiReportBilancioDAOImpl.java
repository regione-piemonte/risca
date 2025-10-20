/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.DatiReportBilancioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.OutputDatiDTO;

public class DatiReportBilancioDAOImpl extends RiscaBeSrvGenericDAO<OutputDatiDTO> implements DatiReportBilancioDAO {

	private static final String QUERY_LOAD_DETTAGLI_BIL_ACC = "select * from v_report_bilancio_dettagli_bil_acc where anno_data_op_val = :annoDataOpVal";
	private static final String QUERY_LOAD_DETTAGLI_ALTRI_PAG = "select * from v_report_bilancio_altri_pagamenti where anno_data_op_val = :annoDataOpVal";

	private static final String QUERY_LOAD_CANONI_MONETIZZAZIONI = "select cam.id_bil_acc, "
			+ "  cam.cod_accertamento || ' - ' || cam.anno as codice, " + "  tbb.des_bil_acc as accertamento, "
			+ "  cru.totale_ruolo as ruolo,  " + "  cte.totale_tesoriere as tesoriere, "
			+ "  cam.totale_importo_accerta_bilancio as totale " + "  from ( "
			+ "       select lis.id_bil_acc, lis.cod_accertamento, lis.anno, sum(importo_accerta_bilancio) as totale_importo_accerta_bilancio "
			+ "         from v_report_bilancio_dettagli_bil_acc lis "
			+ "        where lis.cod_accerta_bilancio not in ('INTERESSI','SPESA_NOTIFICA') "
			+ "          and lis.flg_eccedenza <> 1 " + "          and lis.anno_data_op_val = :annoDataOpVal "
			+ "     group by lis.id_bil_acc, lis.cod_accertamento, lis.anno " + "    ) cam "
			+ "    left join risca_t_bil_acc tbb on cam.id_bil_acc = tbb.id_bil_acc " + "    left join ( "
			+ "       select lis.id_bil_acc, lis.cod_accertamento, lis.anno, sum(importo_accerta_bilancio) as totale_ruolo "
			+ "         from v_report_bilancio_dettagli_bil_acc lis "
			+ "        where lis.cod_accerta_bilancio not in ('INTERESSI','SPESA_NOTIFICA') "
			+ "          and lis.flg_ruolo = 1 " + "          and lis.flg_eccedenza <> 1  "
			+ "          and lis.anno_data_op_val = :annoDataOpVal "
			+ "     group by lis.id_bil_acc, lis.cod_accertamento, lis.anno "
			+ "     ) cru on ( cam.id_bil_acc = cru.id_bil_acc  "
			+ "                and cam.cod_accertamento = cru.cod_accertamento  "
			+ "                and cam.anno = cru.anno ) " + "    left join ( "
			+ "       select lis.id_bil_acc, lis.cod_accertamento, lis.anno, sum(importo_accerta_bilancio) as totale_tesoriere "
			+ "         from v_report_bilancio_dettagli_bil_acc lis "
			+ "        where lis.cod_accerta_bilancio not in ('INTERESSI','SPESA_NOTIFICA') "
			+ "          and lis.flg_ruolo = 0 " + "          and lis.flg_eccedenza <> 1 "
			+ "          and lis.anno_data_op_val = :annoDataOpVal "
			+ "     group by lis.id_bil_acc, lis.cod_accertamento, lis.anno "
			+ "     ) cte on ( cam.id_bil_acc = cte.id_bil_acc  "
			+ "                and cam.cod_accertamento = cte.cod_accertamento  "
			+ "                and cam.anno = cte.anno ) " + " order by cam.anno, cam.id_bil_acc ";

	private static final String QUERY_LOAD_ECCEDENZE = " select cam.id_bil_acc, cam.cod_accertamento || ' - ' || cam.anno as codice, "
			+ "              tbb.des_bil_acc as accertamento, cru.totale_ruolo as ruolo,   "
			+ "              cte.totale_tesoriere as tesoriere, cam.totale_importo_accerta_bilancio as totale "
			+ "         from ( "
			+ "               select lis.id_bil_acc, lis.cod_accertamento, lis.anno, sum(importo_accerta_bilancio) as totale_importo_accerta_bilancio "
			+ "                 from v_report_bilancio_dettagli_bil_acc lis "
			+ "                where lis.cod_accerta_bilancio not in ('INTERESSI','SPESA_NOTIFICA') "
			+ "                  and lis.flg_eccedenza = 1  "
			+ "                  and lis.anno_data_op_val = :annoDataOpVal "
			+ "             group by lis.id_bil_acc, lis.cod_accertamento, lis.anno) cam "
			+ "    left join risca_t_bil_acc tbb on cam.id_bil_acc = tbb.id_bil_acc "
			+ "    left join ( "
			+ "               select lis.id_bil_acc, lis.cod_accertamento, lis.anno, sum(importo_accerta_bilancio) as totale_ruolo "
			+ "                 from v_report_bilancio_dettagli_bil_acc lis "
			+ "                where lis.cod_accerta_bilancio not in ('INTERESSI','SPESA_NOTIFICA') "
			+ "                  and lis.flg_eccedenza = 1  "
			+ "                  and lis.flg_ruolo = 1  "
			+ "                  and lis.anno_data_op_val = :annoDataOpVal "
			+ "             group by lis.id_bil_acc, lis.cod_accertamento, lis.anno  "
			+ "             ) cru on ( cam.id_bil_acc = cru.id_bil_acc  "
			+ "                        and cam.cod_accertamento = cru.cod_accertamento  "
			+ "                        and cam.anno = cru.anno  ) "
			+ "    left join ( "
			+ "               select lis.id_bil_acc, lis.cod_accertamento, lis.anno, sum(importo_accerta_bilancio) as totale_tesoriere "
			+ "                 from v_report_bilancio_dettagli_bil_acc lis "
			+ "                where lis.cod_accerta_bilancio not in ('INTERESSI','SPESA_NOTIFICA') "
			+ "                  and lis.flg_eccedenza = 1  "
			+ "                  and lis.flg_ruolo = 0  "
			+ "                  and lis.anno_data_op_val = :annoDataOpVal "
			+ "             group by lis.id_bil_acc, lis.cod_accertamento,lis.anno    "
			+ "             ) cte on ( cam.id_bil_acc = cte.id_bil_acc  "
			+ "                        and cam.cod_accertamento = cte.cod_accertamento  "
			+ "                        and cam.anno = cte.anno ) "
			+ " order by cam.anno, cam.id_bil_acc ";

	private static final String QUERY_LOAD_NIDA = " select cam.id_bil_acc, " + "              cam.cod_accertamento, "
			+ "              cam.cod_accertamento || ' - ' || cam.anno_data_op_val as codice, "
			+ "              '(Non identificati e da assegnare ' || cam.anno_data_op_val || ')'as accertamento, "
			+ "              null as ruolo,  " + "              cte.totale_tesoriere as tesoriere, "
			+ "              cam.totale_importo_accerta_bilancio as totale, " + "              cam.anno_data_op_val "
			+ "         from ( "
			+ "               select lis.id_bil_acc, lis.cod_accertamento, lis.anno_data_op_val, sum(importo_accerta_bilancio) as totale_importo_accerta_bilancio "
			+ "                 from v_report_bilancio_altri_pagamenti lis "
			+ "                where tipologia_da_visionare in ('SENZA_DETTAGLI_E_SENZA_NON_PROPRI','CON_IMPORTO_DA_ASSEGNARE') "
			+ "                   or (tipologia_da_visionare = 'CON_PAGAMENTO_NON_PROPRIO' "
			+ "                       and cod_tipo_imp_non_propri = 'NI') "
			+ "             group by lis.id_bil_acc, lis.cod_accertamento, lis.anno_data_op_val "
			+ "             ) cam " + "    left join ( "
			+ "               select lis.id_bil_acc, lis.cod_accertamento, lis.anno_data_op_val, sum(importo_accerta_bilancio) as totale_tesoriere "
			+ "                 from v_report_bilancio_altri_pagamenti lis "
			+ "                where tipologia_da_visionare in ('SENZA_DETTAGLI_E_SENZA_NON_PROPRI','CON_IMPORTO_DA_ASSEGNARE') "
			+ "                   or (tipologia_da_visionare = 'CON_PAGAMENTO_NON_PROPRIO' "
			+ "                       and cod_tipo_imp_non_propri = 'NI') "
			+ "             group by lis.id_bil_acc, lis.cod_accertamento, lis.anno_data_op_val "
			+ "             ) cte on ( cam.anno_data_op_val = cte.anno_data_op_val ) "
			+ " where cam.anno_data_op_val in (:annoDataOpVal) " + " order by cam.anno_data_op_val, cam.id_bil_acc ";

	private static final String QUERY_LOAD_INTERESSI = " select cam.id_bil_acc, "
			+ "  cam.cod_accertamento || ' - ' || cam.anno as codice, " + "  'Interessi ' || " + "    case  "
			+ "      when cam.flg_pubblico = 0 then 'diverso da PA' " + "      when cam.flg_pubblico = 1 then 'PA' "
			+ "      else '*** errore ***' " + "    end as accertamento, " + "  cru.totale_ruolo as ruolo, "
			+ "  cte.totale_tesoriere as tesoriere, " + "  cam.totale_importo_accerta_bilancio as totale " + " from ( "
			+ "       select lis.id_bil_acc, lis.cod_accertamento, lis.anno, lis.flg_pubblico, sum(importo_accerta_bilancio) as totale_importo_accerta_bilancio "
			+ "         from v_report_bilancio_dettagli_bil_acc lis "
			+ "        where lis.cod_accerta_bilancio in ('INTERESSI') "
			+ "          and lis.anno_data_op_val = :annoDataOpVal "
			+ "     group by lis.id_bil_acc, lis.cod_accertamento, lis.anno, lis.flg_pubblico " + "     ) cam "
			+ "    left join risca_t_bil_acc tbb on cam.id_bil_acc = tbb.id_bil_acc " + "    left join ( "
			+ "           select lis.id_bil_acc, lis.cod_accertamento, lis.anno, lis.flg_pubblico, sum(importo_accerta_bilancio) as totale_ruolo "
			+ "             from v_report_bilancio_dettagli_bil_acc lis "
			+ "            where lis.cod_accerta_bilancio in ('INTERESSI') "
			+ "              and lis.flg_ruolo = 1     " + "              and lis.anno_data_op_val = :annoDataOpVal "
			+ "         group by lis.id_bil_acc, lis.cod_accertamento, lis.anno, lis.flg_pubblico "
			+ "         ) cru on ( cam.id_bil_acc = cru.id_bil_acc  "
			+ "                    and cam.cod_accertamento = cru.cod_accertamento  "
			+ "                    and cam.anno = cru.anno  "
			+ "                    and cam.flg_pubblico = cru.flg_pubblico ) " + "    left join ( "
			+ "           select lis.id_bil_acc, lis.cod_accertamento, lis.anno, lis.flg_pubblico, sum(importo_accerta_bilancio) as totale_tesoriere "
			+ "             from v_report_bilancio_dettagli_bil_acc lis "
			+ "            where lis.cod_accerta_bilancio in ('INTERESSI') " + "              and lis.flg_ruolo = 0  "
			+ "              and lis.anno_data_op_val = :annoDataOpVal "
			+ "         group by lis.id_bil_acc, lis.cod_accertamento,lis.anno, lis.flg_pubblico "
			+ "         ) cte on ( cam.id_bil_acc = cte.id_bil_acc  "
			+ "                    and cam.cod_accertamento = cte.cod_accertamento  "
			+ "                    and cam.anno = cte.anno  "
			+ "                    and cam.flg_pubblico = cte.flg_pubblico ) " + " order by cam.anno, cam.id_bil_acc ";

	private static final String QUERY_LOAD_SPESE_NOTIFICA = " select cam.id_bil_acc, cam.cod_accertamento || ' - ' || cam.anno as codice, "
			+ "              tbb.des_bil_acc as accertamento, cru.totale_ruolo as ruolo,  "
			+ "              cte.totale_tesoriere as tesoriere, cam.totale_importo_accerta_bilancio as totale "
			+ "         from ( "
			+ "               select lis.id_bil_acc, lis.cod_accertamento, lis.anno, sum(importo_accerta_bilancio) as totale_importo_accerta_bilancio "
			+ "                 from v_report_bilancio_dettagli_bil_acc lis "
			+ "                where lis.cod_accerta_bilancio in ('SPESA_NOTIFICA') "
			+ "                  and lis.anno_data_op_val = :annoDataOpVal "
			+ "             group by lis.id_bil_acc, lis.cod_accertamento, lis.anno) cam "
			+ "    left join risca_t_bil_acc tbb on cam.id_bil_acc = tbb.id_bil_acc "
			+ "    left join ( "
			+ "               select lis.id_bil_acc, lis.cod_accertamento, lis.anno, sum(importo_accerta_bilancio) as totale_ruolo "
			+ "                 from v_report_bilancio_dettagli_bil_acc lis "
			+ "                where lis.cod_accerta_bilancio in ('SPESA_NOTIFICA') "
			+ "                  and lis.flg_ruolo = 1 "
			+ "                  and lis.anno_data_op_val = :annoDataOpVal "
			+ "             group by lis.id_bil_acc, lis.cod_accertamento, lis.anno "
			+ "             ) cru on ( cam.id_bil_acc = cru.id_bil_acc  "
			+ "                        and cam.cod_accertamento = cru.cod_accertamento  "
			+ "                        and cam.anno = cru.anno  ) "
			+ "    left join ( "
			+ "               select lis.id_bil_acc, lis.cod_accertamento, lis.anno, sum(importo_accerta_bilancio) as totale_tesoriere "
			+ "                 from v_report_bilancio_dettagli_bil_acc lis "
			+ "                where lis.cod_accerta_bilancio in ('SPESA_NOTIFICA') "
			+ "                  and lis.flg_ruolo = 0  "
			+ "                  and lis.anno_data_op_val = :annoDataOpVal "
			+ "             group by lis.id_bil_acc, lis.cod_accertamento,lis.anno "
			+ "             ) cte on ( cam.id_bil_acc = cte.id_bil_acc  "
			+ "                        and cam.cod_accertamento = cte.cod_accertamento  "
			+ "                        and cam.anno = cte.anno ) "
			+ " order by cam.anno, cam.id_bil_acc ";

	private static final String QUERY_LOAD_PAGAMENTI_NON_DI_COMPETENZA = " select cam.id_bil_acc, "
			+ "      cam.cod_accertamento, " + "      cam.cod_accertamento || ' - ' || cam.anno_data_op_val as codice, "
			+ "      'Non di competenza ' || cam.anno_data_op_val as accertamento, " + "      null as ruolo, "
			+ "      null as tesoriere, " + "      cam.totale_importo_accerta_bilancio as totale, "
			+ "      cam.anno_data_op_val " + " from ( "
			+ "       select lis.id_bil_acc, lis.cod_accertamento, lis.anno_data_op_val, sum(importo_accerta_bilancio) as totale_importo_accerta_bilancio "
			+ "         from v_report_bilancio_altri_pagamenti lis "
			+ "        where (tipologia_da_visionare = 'CON_PAGAMENTO_NON_PROPRIO' "
			+ "               and cod_tipo_imp_non_propri = 'NC') "
			+ "     group by lis.id_bil_acc, lis.cod_accertamento, lis.anno_data_op_val " + "     ) cam "
			+ " where cam.anno_data_op_val in (:annoDataOpVal) " + " order by cam.anno_data_op_val, cam.id_bil_acc ";

	private static final String QUERY_LOAD_PAGAMENTI_DA_RIMBORSARE = " select cam.id_bil_acc, "
			+ "  cam.cod_accertamento, " + "  cam.cod_accertamento || ' - ' || cam.anno_data_op_val as codice, "
			+ "  'Da rimborsare ' || cam.anno_data_op_val as accertamento, " + "  null as ruolo,  "
			+ "  null as tesoriere,  " + "  cam.totale_importo_accerta_bilancio as totale, " + "  cam.anno_data_op_val "
			+ "from ( "
			+ "   select lis.id_bil_acc, lis.cod_accertamento, lis.anno_data_op_val, sum(importo_accerta_bilancio) as totale_importo_accerta_bilancio "
			+ "     from  v_report_bilancio_altri_pagamenti lis "
			+ "    where (tipologia_da_visionare = 'CON_PAGAMENTO_NON_PROPRIO' "
			+ "      and cod_tipo_imp_non_propri = 'DR')  "
			+ "	group by lis.id_bil_acc, lis.cod_accertamento, lis.anno_data_op_val " + ") cam "
			+ "where cam.anno_data_op_val in (:annoDataOpVal) " + "order by cam.anno_data_op_val, cam.id_bil_acc ";

	private static final String QUERY_LOAD_COMPENSAZIONI = " select 0 as id_bil_acc, 'compensazioni ' || anno as codice, null as ruolo, null as tesoriere,   "
			+ "  		cam.totale_importo_compensazione_canone as totale  " + "  from (  "
			+ "        select dettaglio.anno, sum(totale_importo_compensazione_canone) as totale_importo_compensazione_canone  "
			+ "        from (  "
			+ "              select lis.id_stato_debitorio, lis.anno, lis.flg_eccedenza, sum(tot_importo_compensato) as totale_importo_compensazione_canone  "
			+ "                from (SELECT distinct x.id_stato_debitorio,  "
			+ "                                      x.anno_data_op_val as anno,  "
			+ "                                      x.flg_eccedenza, "
			+ "                                     coalesce(x.tot_importo_compensato,0) as tot_importo_compensato  "
			+ "                        FROM v_report_bilancio_dettagli_bil_acc x) lis                 "
			+ "               where lis.anno = :annoDataOpVal  " + "               and lis.flg_eccedenza = 1 "
			+ "            group by lis.id_stato_debitorio, lis.anno, lis.flg_eccedenza " + "            ) dettaglio  "
			+ "      group by dettaglio.anno) cam " + "  order by cam.anno ";

	private static final String QUERY_LOAD_RIMBORSI = "  select 0 as id_bil_acc,'rimborsi ' || anno as codice, null as ruolo, null as tesoriere,  "
			+ " 		cam.totale_importo_rimborso_canone as totale  " + "  from (  "
			+ "        select dettaglio.anno, sum(totale_importo_rimborso_canone) as totale_importo_rimborso_canone  "
			+ "        from (  "
			+ "              select lis.id_stato_debitorio, lis.anno, lis.flg_eccedenza, sum(tot_importo_rimborso) as totale_importo_rimborso_canone  "
			+ "                from (SELECT distinct x.id_stato_debitorio,  "
			+ "                                      x.anno_data_op_val as anno,   "
			+ "                                      x.flg_eccedenza, "
			+ "                                     coalesce(x.tot_importo_rimborso,0) as tot_importo_rimborso  "
			+ "                        FROM v_report_bilancio_dettagli_bil_acc x) lis                 "
			+ "               where lis.anno = :annoDataOpVal  " + "               and lis.flg_eccedenza = 1 "
			+ "            group by lis.id_stato_debitorio, lis.anno, lis.flg_eccedenza " + "            ) dettaglio  "
			+ "      group by dettaglio.anno) cam " + "  order by cam.anno ";

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<OutputDatiDTO> getRowMapper() throws SQLException {
		return new OutputDatiRowMapper();
	}

	@Override
	public List<OutputDatiDTO> getDettagliBilAcc(Integer annoDataOpVal) throws DAOException {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("annoDataOpVal", annoDataOpVal);
			MapSqlParameterSource params = getParameterValue(map);

			OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
			List<OutputDatiDTO> outputDatiDtoList = template.query(QUERY_LOAD_DETTAGLI_BIL_ACC, params,
					(rs, rowNum) -> {
						OutputDatiDTO outputDatiDto = new OutputDatiDTO();
						rowMapper.populateDettagliBilAccData(rs, outputDatiDto);
						return outputDatiDto;
					});
			return outputDatiDtoList;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getDettagliBilAcc] Data not found for annoDataOpVal: "
					+ annoDataOpVal);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getDettagliBilAcc] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getDettagliBilAcc] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getDettagliBilAcc] END");
		}
	}

	@Override
	public List<OutputDatiDTO> getAltriPagamenti(Integer annoDataOpVal) throws DAOException {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("annoDataOpVal", annoDataOpVal);
			MapSqlParameterSource params = getParameterValue(map);

			OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
			List<OutputDatiDTO> outputDatiDtoList = template.query(QUERY_LOAD_DETTAGLI_ALTRI_PAG, params,
					(rs, rowNum) -> {
						OutputDatiDTO outputDatiDto = new OutputDatiDTO();
						rowMapper.populateAltriPagamentiData(rs, outputDatiDto);
						return outputDatiDto;
					});
			return outputDatiDtoList;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getAltriPagamenti] Data not found for annoDataOpVal: "
					+ annoDataOpVal);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getAltriPagamenti] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getAltriPagamenti] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getAltriPagamenti] END");
		}
	}

	@Override
	public List<OutputDatiDTO> getCanoniEMonetizzazioni(Integer annoDataOpVal) throws DAOException {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("annoDataOpVal", annoDataOpVal);
			MapSqlParameterSource params = getParameterValue(map);

			OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
			List<OutputDatiDTO> outputDatiDtoList = template.query(QUERY_LOAD_CANONI_MONETIZZAZIONI, params,
					(rs, rowNum) -> {
						OutputDatiDTO outputDatiDto = new OutputDatiDTO();
						rowMapper.populateAccertamentiBilancioData(rs, outputDatiDto);
						return outputDatiDto;
					});
			return outputDatiDtoList;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getCanoniEMonetizzazioni] Data not found for annoDataOpVal: "
					+ annoDataOpVal);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getCanoniEMonetizzazioni] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getCanoniEMonetizzazioni] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getCanoniEMonetizzazioni] END");
		}
	}

	@Override
	public List<OutputDatiDTO> getEccedenze(Integer annoDataOpVal) throws DAOException {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("annoDataOpVal", annoDataOpVal);
			MapSqlParameterSource params = getParameterValue(map);

			OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
			List<OutputDatiDTO> outputDatiDtoList = template.query(QUERY_LOAD_ECCEDENZE, params, (rs, rowNum) -> {
				OutputDatiDTO outputDatiDto = new OutputDatiDTO();
				rowMapper.populateAccertamentiBilancioData(rs, outputDatiDto);
				return outputDatiDto;
			});
			return outputDatiDtoList;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[DatiReportBilancioDAOImpl::getEccedenze] Data not found for annoDataOpVal: " + annoDataOpVal);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getEccedenze] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getEccedenze] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getEccedenze] END");
		}
	}

	@Override
	public OutputDatiDTO getCompensazioni(Integer annoDataOpVal) throws DAOException {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("annoDataOpVal", annoDataOpVal);
			MapSqlParameterSource params = getParameterValue(map);

			OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
			OutputDatiDTO outputDatiDto = template.queryForObject(QUERY_LOAD_COMPENSAZIONI, params, (rs, rowNum) -> {
				OutputDatiDTO odDto = new OutputDatiDTO();
				rowMapper.populateCompensazioniRimborsiData(rs, odDto);
				return odDto;
			});
			return outputDatiDto;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(
					"[DatiReportBilancioDAOImpl::getCompensazioni] Data not found for annoDataOpVal: " + annoDataOpVal);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getCompensazioni] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getCompensazioni] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getCompensazioni] END");
		}
	}

	@Override
	public OutputDatiDTO getRimborsi(Integer annoDataOpVal) throws DAOException {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("annoDataOpVal", annoDataOpVal);
			MapSqlParameterSource params = getParameterValue(map);

			OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
			OutputDatiDTO outputDatiDto = template.queryForObject(QUERY_LOAD_RIMBORSI, params, (rs, rowNum) -> {
				OutputDatiDTO odDto = new OutputDatiDTO();
				rowMapper.populateCompensazioniRimborsiData(rs, odDto);
				return odDto;
			});
			return outputDatiDto;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getRimborsi] Data not found for annoDataOpVal: " + annoDataOpVal);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getRimborsi] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getRimborsi] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getRimborsi] END");
		}
	}

	@Override
	public List<OutputDatiDTO> getNonIdentificatiDaAssegnare(Integer annoDataOpVal) throws DAOException {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("annoDataOpVal", annoDataOpVal);
			MapSqlParameterSource params = getParameterValue(map);

			OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
			List<OutputDatiDTO> outputDatiDtoList = template.query(QUERY_LOAD_NIDA, params, (rs, rowNum) -> {
				OutputDatiDTO outputDatiDto = new OutputDatiDTO();
				rowMapper.populateAccertamentiBilancioData(rs, outputDatiDto);
				return outputDatiDto;
			});
			return outputDatiDtoList;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getNonIdentificatiDaAssegnare] Data not found for annoDataOpVal: "
					+ annoDataOpVal);
			return null;
		} catch (SQLException e) {
			LOGGER.error(
					"[DatiReportBilancioDAOImpl::getNonIdentificatiDaAssegnare] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getNonIdentificatiDaAssegnare] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getNonIdentificatiDaAssegnare] END");
		}
	}

	@Override
	public List<OutputDatiDTO> getInteressi(Integer annoDataOpVal) throws DAOException {
		return loadData(QUERY_LOAD_INTERESSI, annoDataOpVal);
	}

	@Override
	public List<OutputDatiDTO> getSpeseNotifica(Integer annoDataOpVal) throws DAOException {
		return loadData(QUERY_LOAD_SPESE_NOTIFICA, annoDataOpVal);
	}

	private List<OutputDatiDTO> loadData(String query, Integer annoDataOpVal) throws DAOException {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("annoDataOpVal", annoDataOpVal);
			MapSqlParameterSource params = getParameterValue(map);

			OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
			List<OutputDatiDTO> outputDatiDtoList = template.query(query, params, (rs, rowNum) -> {
				OutputDatiDTO outputDatiDto = new OutputDatiDTO();
				rowMapper.populateAccertamentiBilancioData(rs, outputDatiDto);
				return outputDatiDto;
			});
			return outputDatiDtoList;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[DatiReportBilancioDAOImpl::loadData] Data not found for annoDataOpVal: " + annoDataOpVal);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::loadData] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::loadData] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[DatiReportBilancioDAOImpl::loadData] END");
		}
	}

	@Override
	public List<OutputDatiDTO> getPagamentiNonDiCompetenza(Integer annoDataOpVal) throws DAOException {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("annoDataOpVal", annoDataOpVal);
			MapSqlParameterSource params = getParameterValue(map);

			OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
			List<OutputDatiDTO> outputDatiDtoList = template.query(QUERY_LOAD_PAGAMENTI_NON_DI_COMPETENZA, params,
					(rs, rowNum) -> {
						OutputDatiDTO outputDatiDto = new OutputDatiDTO();
						rowMapper.populatePagamentiNonDiCompetenza(rs, outputDatiDto);
						return outputDatiDto;
					});
			return outputDatiDtoList;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getPagamentiNonDiCompetenza] Data not found for annoDataOpVal: "
					+ annoDataOpVal);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getPagamentiNonDiCompetenza] Errore nell'esecuzione della query",
					e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getPagamentiNonDiCompetenza] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getPagamentiNonDiCompetenza] END");
		}
	}

	@Override
	public List<OutputDatiDTO> getPagamentiDaRimborsare(Integer annoDataOpVal) throws DAOException {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("annoDataOpVal", annoDataOpVal);
			MapSqlParameterSource params = getParameterValue(map);

			OutputDatiRowMapper rowMapper = new OutputDatiRowMapper();
			List<OutputDatiDTO> outputDatiDtoList = template.query(QUERY_LOAD_PAGAMENTI_DA_RIMBORSARE, params,
					(rs, rowNum) -> {
						OutputDatiDTO outputDatiDto = new OutputDatiDTO();
						rowMapper.populatePagamentiDaRimborsare(rs, outputDatiDto);
						return outputDatiDto;
					});
			return outputDatiDtoList;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getPagamentiDaRimborsare] Data not found for annoDataOpVal: "
					+ annoDataOpVal);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getPagamentiDaRimborsare] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[DatiReportBilancioDAOImpl::getPagamentiDaRimborsare] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[DatiReportBilancioDAOImpl::getPagamentiDaRimborsare] END");
		}
	}

	/**
	 * The type OutputDatiRowMapper row mapper.
	 */
	public static class OutputDatiRowMapper implements RowMapper<OutputDatiDTO> {

		/**
		 * Instantiates a new OutputDatiRowMapper row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public OutputDatiRowMapper() throws SQLException {
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
		public OutputDatiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			OutputDatiDTO bean = new OutputDatiDTO();
			return bean;
		}

		private void populateDettagliBilAccData(ResultSet rs, OutputDatiDTO outputDatiDto) throws SQLException {
			outputDatiDto.setValoreColonna1(rs.getString("nap"));
			outputDatiDto.setValoreColonna2(rs.getString("cod_riscossione"));
			outputDatiDto.setValoreColonna3(formatBigDecimal(rs.getBigDecimal("canone_dovuto")));
			outputDatiDto.setValoreColonna4(formatDate(rs.getDate("data_scadenza_pagamento")));
			outputDatiDto.setValoreColonna5(formatBigDecimal(rs.getBigDecimal("interessi_maturati")));
			outputDatiDto.setValoreColonna6(formatBigDecimal(rs.getBigDecimal("imp_spese_notifica")));
			outputDatiDto.setValoreColonna7(formatDate(rs.getDate("data_op_val")));
			outputDatiDto.setValoreColonna8(formatBigDecimal(rs.getBigDecimal("importo_versato")));
			outputDatiDto.setValoreColonna9(formatBigDecimal(rs.getBigDecimal("importo_accerta_bilancio")));
			outputDatiDto.setValoreColonna10(rs.getInt("flg_eccedenza") == 1 ? "SI" : "");
			outputDatiDto.setValoreColonna11(rs.getString("des_accerta_bilancio"));
			outputDatiDto.setValoreColonna12(rs.getString("cod_accertamento") + " - " + rs.getString("anno"));
			outputDatiDto.setValoreColonna13(rs.getString("accertamento"));
			outputDatiDto.setValoreColonna14(rs.getString("anno_competenza"));
			outputDatiDto.setValoreColonna15(rs.getString("des_tipo_modalita_pag"));
			outputDatiDto.setValoreColonna16(rs.getString("note"));
			outputDatiDto.setValoreColonna17(rs.getInt("flg_pubblico") == 1 ? "SI" : "");
			outputDatiDto.setValoreColonna18(formatBigDecimal(rs.getBigDecimal("tot_importo_compensato")));
			outputDatiDto.setValoreColonna19(formatBigDecimal(rs.getBigDecimal("tot_importo_rimborso")));
			outputDatiDto.setValoreColonna20(rs.getInt("flg_ruolo") == 1 ? "SI" : "");
			outputDatiDto.setValoreColonna21("" + rs.getLong("id_pagamento"));
			outputDatiDto.setValoreColonna22("" + rs.getLong("id_dettaglio_pag"));
			outputDatiDto.setValoreColonna23("" + rs.getLong("id_dettaglio_pag_bil_acc"));
		}

		private void populateAltriPagamentiData(ResultSet rs, OutputDatiDTO outputDatiDto) throws SQLException {
//			outputDatiDto.setValoreColonna1("");
//			outputDatiDto.setValoreColonna2("");
//			outputDatiDto.setValoreColonna3("");
//			outputDatiDto.setValoreColonna4("");
//			outputDatiDto.setValoreColonna5("");
//			outputDatiDto.setValoreColonna6("");
			outputDatiDto.setValoreColonna7(formatDate(rs.getDate("data_op_val")));
			outputDatiDto.setValoreColonna8(formatBigDecimal(rs.getBigDecimal("importo_versato")));
			outputDatiDto.setValoreColonna9(formatBigDecimal(rs.getBigDecimal("importo_accerta_bilancio")));
//			outputDatiDto.setValoreColonna10("");

			if (rs.getString("cod_accertamento") != null && !(rs.getString("cod_tipo_imp_non_propri") != null
					&& rs.getString("cod_tipo_imp_non_propri").equals("NC"))) {
				outputDatiDto.setValoreColonna11(rs.getString("des_accerta_bilancio"));
				outputDatiDto.setValoreColonna12(
						rs.getString("cod_accertamento") + " - " + rs.getString("anno_data_op_val"));
			}

			outputDatiDto.setValoreColonna13(rs.getString("desc_accertamento"));
			outputDatiDto.setValoreColonna14("");
			outputDatiDto.setValoreColonna15(rs.getString("des_tipo_modalita_pag"));
			outputDatiDto.setValoreColonna16("VERSANTE: " + checkNull(rs.getString("soggetto_versamento")) 
										+ " - CAUSALE: " + checkNull(rs.getString("causale")) 
										+ " - NOTE: " + checkNull(rs.getString("note")));
//			outputDatiDto.setValoreColonna17("");
//			outputDatiDto.setValoreColonna18("");
//			outputDatiDto.setValoreColonna19("");
//			outputDatiDto.setValoreColonna20("");
			outputDatiDto.setValoreColonna21("" + rs.getLong("id_pagamento"));
			// outputDatiDto.setValoreColonna22("" + rs.getLong("id_dettaglio_pag"));
			// outputDatiDto.setValoreColonna23("" + rs.getLong("id_dettaglio_pag_bil_acc"));
		}

		private void populateAccertamentiBilancioData(ResultSet rs, OutputDatiDTO outputDatiDto) throws SQLException {
			outputDatiDto.setValoreColonna1(rs.getString("codice"));
			outputDatiDto.setValoreColonna2(rs.getString("accertamento"));
			outputDatiDto.setValoreColonna3(formatBigDecimal(rs.getBigDecimal("ruolo")));
			outputDatiDto.setValoreColonna4(formatBigDecimal(rs.getBigDecimal("tesoriere")));
			outputDatiDto.setValoreColonna5(formatBigDecimal(rs.getBigDecimal("totale")));
			// valoreColonna6 non valorizzato
		}

		private void populateCompensazioniRimborsiData(ResultSet rs, OutputDatiDTO outputDatiDto) throws SQLException {
			outputDatiDto.setValoreColonna1("");
			outputDatiDto.setValoreColonna2("");
			outputDatiDto.setValoreColonna3("");
			outputDatiDto.setValoreColonna4("");
			outputDatiDto.setValoreColonna5(formatBigDecimal(rs.getBigDecimal("totale")));
			// valoreColonna6 non valorizzato
		}

		private void populatePagamentiNonDiCompetenza(ResultSet rs, OutputDatiDTO outputDatiDto) throws SQLException {
			outputDatiDto.setValoreColonna2("NON DI COMPETENZA");
			outputDatiDto.setValoreColonna6(formatBigDecimal(rs.getBigDecimal("totale")));
		}

		private void populatePagamentiDaRimborsare(ResultSet rs, OutputDatiDTO outputDatiDto) throws SQLException {
			outputDatiDto.setValoreColonna2("DA RIMBORSARE");
			outputDatiDto.setValoreColonna6(formatBigDecimal(rs.getBigDecimal("totale")));
		}

		public String formatBigDecimal(BigDecimal value) {
			if (value == null)
				value = BigDecimal.ZERO;
			Locale currentLocale = Locale.getDefault();
			DecimalFormatSymbols symbols = new DecimalFormatSymbols(currentLocale);
			symbols.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("#.##", symbols);
			df.setMaximumFractionDigits(2);
			df.setMinimumFractionDigits(2);
			df.setGroupingUsed(false);
			return df.format(value);
		}

		public String formatDate(Date date) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			try {
				return df.format(date);
			} catch (Exception e) {
				return "";
			}
		}

		public String checkNull(String s) {
			if (s == null) {
				return "";
			}
			return s;
		}

	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}
