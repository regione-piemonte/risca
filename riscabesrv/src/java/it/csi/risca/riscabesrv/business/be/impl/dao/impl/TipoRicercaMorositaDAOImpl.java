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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRicercaMorositaDAO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoRicercaMorositaDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class TipoRicercaMorositaDAOImpl extends RiscaBeSrvGenericDAO<TipoRicercaMorositaDTO> implements TipoRicercaMorositaDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	

	
	
	public static final String QUERY_SELECT_ALL_TIPO_RICERCA_MOROSITA = "select * from risca_d_tipo_ricerca_morosita  ";
	
	public static final String QUERY_LOAD_MOROSITA=" select distinct  \r\n" + 
			"			 sd.id_stato_debitorio, \r\n" + 
			"			 sd.id_stato_contribuzione, \r\n" + 
			"			 sd.nap, \r\n" + 
			"			 rts.id_soggetto, \r\n" + 
			"			 rata.canone_dovuto,  \r\n" + 
			"			 risc.cod_riscossione, \r\n" + 
			"			 risc.id_riscossione, \r\n" + 
			"			 rata.canone_dovuto importo_dovuto , \r\n" + 
			"			 TO_CHAR(rata.data_scadenza_pagamento, 'yyyy-mm-dd') data_scadenza, \r\n" + 
			"			 coalesce(rata.interessi_maturati,0) + coalesce(sd.imp_spese_notifica, 0) interessi_dovuti, \r\n" + 
			"			 SUM(det.importo_versato) somma_imp_versato, 		\r\n" + 
			"			 CASE  \r\n" + 
			"			   WHEN (coalesce(rata.canone_dovuto, 0) + coalesce(rimb1.imp_rimb, 0)) -        \r\n" + 
			"			       coalesce(SUM(det.importo_versato), 0) < 0 \r\n" + 
			"			      THEN 0  \r\n" + 
			"			  ELSE (coalesce(rata.canone_dovuto, 0) + coalesce(rimb1.imp_rimb, 0)) -                            \r\n" + 
			"			  coalesce(SUM(det.importo_versato), 0)  \r\n" + 
			"			 END importo_canone_mancante, 		\r\n" + 
			"			   CASE  \r\n" + 
			"			      WHEN (coalesce(rata.canone_dovuto, 0) + coalesce(rimb1.imp_rimb, 0)) -                                               \r\n" + 
			"			            coalesce(SUM(det.importo_versato), 0) > 0  \r\n" + 
			"			            THEN (coalesce(rata.interessi_maturati, 0) + coalesce(sd.imp_spese_notifica, 0)) \r\n" + 
			"			    ELSE \r\n" + 
			"			       CASE  \r\n" + 
			"			         WHEN (coalesce(rata.interessi_maturati, 0) + coalesce(sd.imp_spese_notifica, 0)) +  \r\n" + 
			"			              (coalesce(rata.canone_dovuto, 0) + coalesce(rimb1.imp_rimb, 0)) -    \r\n" + 
			"			               coalesce(SUM(det.importo_versato), 0) < 0 \r\n" + 
			"			              THEN 0  \r\n" + 
			"			          ELSE (coalesce(rata.interessi_maturati, 0) + coalesce(sd.imp_spese_notifica, 0)) +  \r\n" + 
			"			              (coalesce(rata.canone_dovuto, 0) + coalesce(rimb1.imp_rimb, 0)) -     \r\n" + 
			"			               coalesce(SUM(det.importo_versato), 0) \r\n" + 
			"			         END  \r\n" + 
			"			    END interessi_mancanti, \r\n" + 
			"			  	CASE \r\n" + 
			"			     WHEN (coalesce(rata.canone_dovuto, 0) + coalesce(rimb1.imp_rimb, 0)) -         \r\n" + 
			"			           coalesce(SUM(det.importo_versato), 0) >= 0  \r\n" + 
			"			         THEN 0 \r\n" + 
			"			  ELSE ABS((coalesce(rata.canone_dovuto, 0) + coalesce(rimb1.imp_rimb, 0)) -   \r\n" + 
			"			          coalesce(SUM(det.importo_versato), 0)) \r\n" + 
			"			  END interessi_versati, 						\r\n" + 
			"			  coalesce(rimb.imp_rimb, 0) somma_rimborsi, \r\n" + 
			"			  sd.flg_annullato as stato, \r\n" + 
			"			  TO_CHAR(MAX(pag.data_op_val), 'yyyy-mm-dd') dataPagamento, \r\n" + 
			"			  sd.id_attivita_stato_deb,\r\n" + 
			"			  CASE \r\n" + 
			"				 WHEN	(COALESCE(SUM(det.importo_versato),0) - (COALESCE(rata.canone_dovuto,0) +  COALESCE(sd.imp_spese_notifica,0)  \r\n" + 
			"				 \r\n" + 
			"				 + COALESCE(rata.interessi_maturati ,0)) -  COALESCE(rimb1.imp_rimb, 0) ) > 0 \r\n" + 
			"				 THEN \r\n" + 
			"				(COALESCE(SUM(det.importo_versato),0) - (COALESCE(rata.canone_dovuto,0) +  COALESCE(sd.imp_spese_notifica,0) \r\n" + 
			"				 \r\n" + 
			"				 + COALESCE(rata.interessi_maturati ,0) ) -  COALESCE(rimb1.imp_rimb, 0) )\r\n" + 
			"			   else 0 \r\n" + 
			"					END importo_eccedente, \r\n" + 
			"			 sd.imp_compensazione_canone,\r\n" + 
			"			 rdsr.cod_stato_riscossione,\r\n" + 
			"			 sd.flg_restituito_mittente, \r\n" + 
			"			 sd.flg_annullato \r\n" + 
			"			FROM \r\n" + 
			"			  risca_t_riscossione risc\r\n" + 
			"			  join risca_d_stato_riscossione rdsr on risc.id_stato_riscossione = rdsr.id_stato_riscossione \r\n" + 
			"			  join risca_d_tipo_riscossione dtr on risc.id_tipo_riscossione = dtr.id_tipo_riscossione \r\n" + 
			"			  join risca_t_stato_debitorio sd on risc.id_riscossione = sd.id_riscossione  \r\n" + 
			"			  join risca_t_soggetto rts on sd.id_soggetto = rts.id_soggetto \r\n" + 
			"			  join risca_r_rata_sd rata on sd.id_stato_debitorio = rata.id_stato_debitorio \r\n" + 
			"			  AND rata.id_rata_sd_padre IS NULL \r\n" + 
			"			  left join risca_r_dettaglio_pag det on rata.id_rata_sd = det.id_rata_sd \r\n" + 
			"			  left join risca_t_pagamento pag on det.id_pagamento = pag.id_pagamento \r\n" + 
			"			  left join risca_d_stato_contribuzione stato_contr on sd.id_stato_contribuzione = stato_contr.id_stato_contribuzione \r\n" + 
			"			  left join (SELECT rimb.id_stato_debitorio , SUM(rimb.imp_rimborso) imp_rimb \r\n" + 
			"			             FROM risca_r_rimborso rimb \r\n" + 
			"			         GROUP BY rimb.id_stato_debitorio \r\n" + 
			"			          ) rimb on sd.id_stato_debitorio = rimb.id_stato_debitorio \r\n" + 
			"			  left join (SELECT rimb.id_stato_debitorio , SUM(rimb.imp_rimborso) imp_rimb \r\n" + 
			"			         FROM risca_r_rimborso rimb \r\n" + 
			"			         where rimb.id_tipo_rimborso=1 \r\n" + 
			"			         GROUP BY rimb.id_stato_debitorio \r\n" + 
			"			      ) rimb1 on sd.id_stato_debitorio = rimb1.id_stato_debitorio  \r\n" + 
			"			 where rata.data_scadenza_pagamento  <= current_date \r\n" + 
			"			 and  rata.canone_dovuto <> 0 "
			+ " and dtr.id_ambito = :idAmbito "; 
	
	public static final String ORDER_BY_SD =" ORDER BY data_scadenza desc ";
	
	public static final String RICMOR01 = " AND sd.id_attivita_stato_deb  IS NULL "
			+ "  AND sd.id_stato_contribuzione = '2'"
			+ "  AND sd.flg_annullato = 0"
			+ "  and not exists"
			+ "  (SELECT * FROM risca_t_accertamento ac WHERE  ac.data_protocollo ="
			+ "         (SELECT MAX(ac1.data_protocollo)"
			+ "            FROM risca_t_accertamento ac1"
			+ "           WHERE ac1.id_stato_debitorio  = sd.id_stato_debitorio  "
			+ "             AND ac1.flg_annullato= 0"
			+ "             AND ac1.flg_restituito = 0 ))";
			
			
			
			
//			" sd.id_stato_debitorio IN (( "
//			+ "  SELECT sd.id_stato_debitorio  "
//			+ "  FROM risca_t_stato_debitorio sd "
//			+ "  WHERE sd.id_attivita_stato_deb  IS NULL "
//			+ " )) "
//			+ " AND NOT EXISTS "
//			+ "("
//			+ "  SELECT * "
//			+ "  FROM risca_t_accertamento ac  WHERE ac.id_accertamento = "
//			+ "  (SELECT MAX(ac1.id_accertamento) "
//			+ "  FROM risca_t_accertamento ac1 "
//			+ "  WHERE ac1.id_stato_debitorio  = sd.id_stato_debitorio  AND ac1.flg_annullato= 0 "
//			+ "  AND ac1.flg_restituito  = 0)) ";

	
	
	public static final String RICMOR02 = " AND sd.id_attivita_stato_deb = 2"
			+ " AND sd.id_stato_contribuzione = '2'"
			+ " AND sd.flg_annullato = 0 "; 
	
	public static final String RICMOR03 = "	AND sd.id_stato_contribuzione = '2' "
			+ " AND sd.flg_annullato = 0 "
			+ " AND sd.id_attivita_stato_deb  IS NULL  "
			+ " AND sd.id_stato_debitorio IN (  "
			+ " WITH lst_max_acc (id_stato_debitorio) AS ( "
			+ "         SELECT id_stato_debitorio, MAX(ac1.data_protocollo) as data_max "
			+ "           FROM risca_t_accertamento ac1 "
			+ "          WHERE ac1.id_stato_debitorio = sd.id_stato_debitorio   "
			+ "          group by id_stato_debitorio) "
			+ "    select lst_max_acc.id_stato_debitorio from lst_max_acc, risca_t_accertamento ac2  "
			+ "     where ac2.id_tipo_accertamento in (1,4) and \r\n"
			+ "         ac2.id_stato_debitorio = lst_max_acc.id_stato_debitorio and "
			+ "         ac2.data_protocollo = lst_max_acc.data_max) ";
	
	public static final String RICMOR04 = " AND sd.id_attivita_stato_deb  IS NULL"
			+ " AND sd.id_stato_contribuzione = '2'"
			+ " AND sd.flg_annullato = 0"
			+ " AND sd.id_stato_debitorio IN ( "
			+ "  WITH lst_max_acc (id_stato_debitorio) AS ("
			+ "         SELECT id_stato_debitorio, MAX(ac1.data_protocollo) as data_max"
			+ "           FROM risca_t_accertamento ac1"
			+ "          WHERE ac1.id_stato_debitorio = sd.id_stato_debitorio  "
			+ "          group by id_stato_debitorio)"
			+ "    select lst_max_acc.id_stato_debitorio from lst_max_acc, risca_t_accertamento ac2 "
			+ "     where ac2.id_tipo_accertamento in (1,4) and "
			+ "         ac2.id_stato_debitorio = lst_max_acc.id_stato_debitorio and"
			+ "         ac2.data_protocollo = lst_max_acc.data_max"
			+ "         AND ac2.data_scadenza < current_date)";
	
	public static final String RICMOR05 = "	AND sd.id_attivita_stato_deb = 3 "
			+ " AND sd.id_stato_contribuzione = '2'"
			+ " AND sd.flg_annullato = 0 ";
	
	public static final String RICMOR06 = " AND sd.id_stato_contribuzione = '2' "
			+ " AND sd.flg_annullato = 0 "
			+ " AND sd.id_attivita_stato_deb  IS NULL  "
			+ " AND sd.id_stato_debitorio IN (  "
			+ " WITH lst_max_acc (id_stato_debitorio) AS ( "
			+ "         SELECT id_stato_debitorio, MAX(ac1.data_protocollo) as data_max "
			+ "           FROM risca_t_accertamento ac1"
			+ "          WHERE ac1.id_stato_debitorio = sd.id_stato_debitorio   "
			+ "          group by id_stato_debitorio) "
			+ "    select lst_max_acc.id_stato_debitorio from lst_max_acc, risca_t_accertamento ac2 "
			+ "     where ac2.id_tipo_accertamento in (2,5,6) and  "
			+ "         ac2.id_stato_debitorio = lst_max_acc.id_stato_debitorio and "
			+ "         ac2.data_protocollo = lst_max_acc.data_max and      ac2.data_scadenza >= current_date) ";
	
//	public static final String RICMOR07 = " AND sd.id_attivita_stato_deb  IS NULL "
//			+ " AND sd.id_stato_contribuzione = '2' "
//			+ " AND sd.flg_annullato = 0 "
//			+ " AND sd.id_stato_debitorio IN (  "
//			+ "  WITH lst_max_acc (id_stato_debitorio) AS ( "
//			+ "         SELECT id_stato_debitorio, MAX(ac1.data_protocollo) as data_max "
//			+ "           FROM risca_t_accertamento ac1 "
//			+ "          WHERE ac1.id_stato_debitorio = sd.id_stato_debitorio   "
//			+ "          group by id_stato_debitorio) "
//			+ "    select lst_max_acc.id_stato_debitorio from lst_max_acc, risca_t_accertamento ac2  "
//			+ "     where ac2.id_tipo_accertamento in (2,5,6) and  "
//			+ "         ac2.id_stato_debitorio = lst_max_acc.id_stato_debitorio and "
//			+ "         ac2.data_protocollo = lst_max_acc.data_max "
//			+ "         AND ac2.data_scadenza < current_date) ";

	public static final String RICMOR07 = " AND sd.id_attivita_stato_deb  IS NULL "
			+ "  AND sd.id_stato_contribuzione = '2' "
			+ "  AND sd.flg_annullato = 0 "
			+ "  AND sd.id_stato_debitorio IN ( "
			+ "    WITH lst_max_acc (id_stato_debitorio) AS ( "
			+ "                                 SELECT id_stato_debitorio, MAX(ac1.data_protocollo) as data_max "
			+ "                                   FROM risca_t_accertamento ac1 "
			+ "                               group by id_stato_debitorio) "
			+ "      select lst_max_acc.id_stato_debitorio "
			+ "         from lst_max_acc "
			+ "        join ( "
			+ "          select acc.id_stato_debitorio, acc.data_protocollo "
			+ "            from risca_t_accertamento acc "
			+ "           where acc.id_tipo_accertamento in (2,5,6) "
			+ "            and acc.data_scadenza < current_date "
			+ "            and acc.flg_annullato = 0 "
			+ "            and acc.flg_restituito = 0 "
			+ "            ) ac2 on lst_max_acc.id_stato_debitorio = ac2.id_stato_debitorio "
			+ "                      and lst_max_acc.data_max = ac2.data_protocollo      "
			+ "   ) ";

	
	public static final String RICMOR08 = "	 AND sd.id_attivita_stato_deb = 4  "
			+ "AND sd.id_stato_contribuzione = '2' "
			+ "AND sd.flg_annullato = 0 "
			+ "";
	public static final String RICMOR09 = " AND sd.id_stato_contribuzione = '2' "
			+ "AND sd.flg_annullato = 0 "
			+ "AND sd.id_attivita_stato_deb  IS NULL  "
			+ "AND sd.id_stato_debitorio IN (  "
			+ "WITH lst_max_acc (id_stato_debitorio) AS ( "
			+ "        SELECT id_stato_debitorio, MAX(ac1.data_protocollo) as data_max "
			+ "           FROM risca_t_accertamento ac1 "
			+ "          WHERE ac1.id_stato_debitorio = sd.id_stato_debitorio "
			+ "          group by id_stato_debitorio) "
			+ "    select lst_max_acc.id_stato_debitorio from lst_max_acc, risca_t_accertamento ac2  "
			+ "     where ac2.id_tipo_accertamento = 3 and "
			+ "         ac2.id_stato_debitorio = lst_max_acc.id_stato_debitorio and "
			+ "        ac2.data_protocollo = lst_max_acc.data_max) ";
	public static final String RICMOR10 = " AND sd.id_attivita_stato_deb = 1 "
			+ " AND sd.id_stato_contribuzione = '2' "
			+ " AND sd.flg_annullato = 0 " ;

	public static final String RICMOR11 = " AND sd.id_stato_contribuzione = '2' "
			+ "AND sd.flg_annullato = 0 "
			+ "AND sd.id_attivita_stato_deb  IS NULL  "
			+ "AND sd.id_stato_debitorio IN (  "
			+ "WITH lst_max_acc (id_stato_debitorio) AS ( "
			+ "        SELECT id_stato_debitorio, MAX(ac1.data_protocollo) as data_max "
			+ "           FROM risca_t_accertamento ac1 "
			+ "          WHERE ac1.id_stato_debitorio = sd.id_stato_debitorio "
			+ "          group by id_stato_debitorio) "
			+ "    select lst_max_acc.id_stato_debitorio from lst_max_acc, risca_t_accertamento ac2  "
			+ "     where ac2.id_tipo_accertamento = 3   "
			+ "        and ac2.id_stato_debitorio = lst_max_acc.id_stato_debitorio "
			+ "        and ac2.data_protocollo = lst_max_acc.data_max "
			+ "        and ac2.flg_annullato = 1) ";
	
	public static final String RICMOR12  = " AND sd.id_attivita_stato_deb = 5 "
			+ " AND sd.id_stato_contribuzione = '2' "
			+ " AND sd.flg_annullato = 0 ";
	
	public static final String RICMOR13  = " AND sd.id_attivita_stato_deb = 7 "
			+ " AND sd.id_stato_contribuzione = '2' "
			+ " AND sd.flg_annullato = 0 ";
	
	public static final String RICMOR14 = " AND sd.id_stato_contribuzione = '2' "
			+ "AND sd.flg_annullato = 0 "
			+ "AND sd.id_attivita_stato_deb  IS NULL "
			+ "AND sd.id_stato_debitorio IN (  "
			+ "WITH lst_max_acc (id_stato_debitorio) AS ( "
			+ "        SELECT id_stato_debitorio, MAX(ac1.data_protocollo) as data_max "
			+ "           FROM risca_t_accertamento ac1 "
			+ "          WHERE ac1.id_stato_debitorio = sd.id_stato_debitorio "
			+ "          group by id_stato_debitorio) "
			+ "    select lst_max_acc.id_stato_debitorio from lst_max_acc, risca_t_accertamento ac2  "
			+ "     where ac2.id_tipo_accertamento = 6   "
			+ "        and ac2.id_stato_debitorio = lst_max_acc.id_stato_debitorio "
			+ "        and ac2.data_protocollo = lst_max_acc.data_max "
			+ "        and ac2.data_scadenza >= current_date) ";
	
	public static final String RICMOR15  = " AND sd.id_attivita_stato_deb = 9 "
			+ "AND sd.id_stato_contribuzione = '2' "
			+ "AND sd.flg_annullato = 0 ";
	
	public static final String RICMOR16  = " AND sd.id_attivita_stato_deb = 10 "
			+ "AND sd.id_stato_contribuzione = '2' "
			+ "AND sd.flg_annullato = 0 ";
	
	public static final String ANNO ="  SUBSTRING(TO_CHAR(rata.data_scadenza_pagamento, 'yyyy-mm-dd'), 1, 4) = CAST(:anno AS TEXT) ";
	
	public static final String FLG_RESTITUITO =" sd.id_stato_debitorio IN ( SELECT distinct ac1.id_stato_debitorio "
					+ "  FROM risca_t_accertamento ac1 "
					+ " where ac1.flg_restituito = :flgRest ) ";  
										
	public static final String FLG_ANNULATO =" sd.id_stato_debitorio IN ( SELECT distinct ac1.id_stato_debitorio "
			+ "  FROM risca_t_accertamento ac1 "
			+ " where ac1.flg_annullato = :flgAnn ) ";   

	public static final String GROUP_BY =" GROUP BY  "
			+ "    sd.id_stato_debitorio, "
			+ "    risc.id_riscossione, "
			+ "    risc.cod_riscossione,   "
			+ "    rata.canone_dovuto,        "
			+ "    rata.interessi_maturati,      "
			+ "    sd.imp_spese_notifica,         "
			+ "    rata.data_scadenza_pagamento,  "
			+ "    sd.flg_annullato,             "
			+ "    rimb.imp_rimb, "
			+ "    rimb1.imp_rimb, "
			+ "    sd.id_attivita_stato_deb, "
			+ "    sd.imp_compensazione_canone,"
			+ " rdsr.cod_stato_riscossione, "
			+ " risc.id_riscossione ,"
			+ "rts.id_soggetto";
	
	
	public static final String  QUERY_FILTER_LIM_MAGGIORE=" HAVING "
			+ "    (coalesce(rata.canone_dovuto, 0)"
			+ "     + coalesce((coalesce(rata.interessi_maturati, 0) + coalesce(sd.imp_spese_notifica, 0)), 0) "
			+ "     - coalesce(SUM(det.importo_versato), 0)"
			+ "     + coalesce(sd.imp_compensazione_canone, 0)) > CAST(:lim AS numeric) ";
	
	public static final String  QUERY_FILTER_LIM_MINORE=" HAVING "
			+ "    (coalesce(rata.canone_dovuto, 0)"
			+ "     + coalesce((coalesce(rata.interessi_maturati, 0) + coalesce(sd.imp_spese_notifica, 0)), 0) "
			+ "     - coalesce(SUM(det.importo_versato), 0)"
			+ "     + coalesce(sd.imp_compensazione_canone, 0)) <= CAST(:lim AS numeric) ";
	

	
	@Override
	public List<TipoRicercaMorositaDTO>  loadAllTipoRicercaMorosita() throws DAOException {
		LOGGER.debug("[TipoRicercaMorositaDAOImpl::loadAllTipoRicercaMorosita] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<TipoRicercaMorositaDTO> res = new ArrayList<>();
		try {

			res = findSimpleDTOListByQuery(CLASSNAME, methodName, QUERY_SELECT_ALL_TIPO_RICERCA_MOROSITA, null);

		} catch (Exception e) {
			LOGGER.error("[TipoRicercaMorositaDAOImpl::loadAllTipoRicercaMorosita] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[TipoRicercaMorositaDAOImpl::loadAllTipoRicercaMorosita] END");
		}

		return res;
	}



	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public RowMapper<TipoRicercaMorositaDTO> getRowMapper() throws SQLException {
		return new TipoRicercaMorositaRowMapper();
	}



	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * The type TipoRicercaMorositaRowMapper.
	 */
	public static class TipoRicercaMorositaRowMapper implements RowMapper<TipoRicercaMorositaDTO> {
		
		/**
		 * Instantiates a new TipoRicercaMorositaRowMapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public TipoRicercaMorositaRowMapper() throws SQLException {
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
		public TipoRicercaMorositaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TipoRicercaMorositaDTO bean = new TipoRicercaMorositaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, TipoRicercaMorositaDTO bean) throws SQLException {
			bean.setIdTipoRicercaMorosita(rs.getLong("id_tipo_ricerca_morosita"));
			bean.setCodTipoRicercaMorosita(rs.getString("cod_tipo_ricerca_morosita"));
			bean.setDesTipoRicercaMorosita(rs.getString("des_tipo_ricerca_morosita"));

		}

	}
	@Override
	public List<StatoDebitorioExtendedDTO> ricercaMorosita(String tipoRicercaMorosita, Long idAmbito, Integer anno, Integer flgRest,
	        Integer flgAnn, String lim, Integer offset, Integer limit, String sort) throws Exception {
	    LOGGER.debug("[TipoRicercaMorositaDAOImpl::ricercaMorosita] BEGIN");
	    List<StatoDebitorioExtendedDTO> resultRicercaMorosita = new ArrayList<>();
	    try {
	        StringBuilder queryBuilder = new StringBuilder()
	                .append(QUERY_LOAD_MOROSITA);
	        Map<String, Object> queryParams = new HashMap<>();
	        if (tipoRicercaMorosita != null) {
	            Map<String, String> tipoRicercaClauses = createTipoRicercaClauses();
	            if (tipoRicercaClauses.containsKey(tipoRicercaMorosita)) {
	                queryBuilder.append(tipoRicercaClauses.get(tipoRicercaMorosita));
	            }
	        }

	        if (idAmbito != null) {
	            queryParams.put("idAmbito", idAmbito);
	        }
		   List<String> tipiAbilitatiAiFlg = new ArrayList<>();
		   tipiAbilitatiAiFlg.add(Constants.RICMOR03);
		   tipiAbilitatiAiFlg.add(Constants.RICMOR04);
		   tipiAbilitatiAiFlg.add(Constants.RICMOR06);
		   tipiAbilitatiAiFlg.add(Constants.RICMOR07);
		   tipiAbilitatiAiFlg.add(Constants.RICMOR09);
		   tipiAbilitatiAiFlg.add(Constants.RICMOR14);
	        if (flgRest.equals(1)) {
	        	if( tipiAbilitatiAiFlg.contains(tipoRicercaMorosita)) {
		            queryParams.put("flgRest", flgRest);
		            queryBuilder.append(" AND ");
		            queryBuilder.append(FLG_RESTITUITO);
	        	}
	        }

	        if (flgAnn.equals(1)) {
	        	if(tipiAbilitatiAiFlg.contains(tipoRicercaMorosita)) {
		            queryParams.put("flgAnn", flgAnn);
		            queryBuilder.append(" AND ");
		            queryBuilder.append(FLG_ANNULATO);
	        	}
	        }

	        if (anno != null) {
	            queryParams.put("anno", anno.toString());
	                queryBuilder.append(" AND ");
	            queryBuilder.append(ANNO);
	        }
            queryBuilder.append(GROUP_BY);
            
	        if (lim != null) {
	            String queryFilter = lim.length() >= 1 && Character.toString(lim.charAt(0)).equals("+") ? QUERY_FILTER_LIM_MAGGIORE : QUERY_FILTER_LIM_MINORE;
	            queryParams.put("lim", lim.substring(1));
	            queryBuilder.append(queryFilter);
	        }
            
	        if (StringUtils.isNotBlank(sort)) {
	            String dynamicOrderByCondition = mapSortConCampiDB(sort);
	            if (dynamicOrderByCondition != null) {
	                dynamicOrderByCondition = dynamicOrderByCondition.concat(",").concat(ORDER_BY_SD.replace("ORDER BY", ""));
	            }
	            queryBuilder.append(dynamicOrderByCondition);
	        } else {
	            queryBuilder.append(ORDER_BY_SD);
	        }
	        SqlParameterSource params = new MapSqlParameterSource(queryParams);
	        String finalQuery = queryBuilder.toString();
	        resultRicercaMorosita = template.query(getQuery(finalQuery, offset != null ? offset.toString() : null, limit != null ? limit.toString() : null), params, getRowMapperStatoDebitorio());

	    } catch (EmptyResultDataAccessException e) {
	        LOGGER.debug("[TipoRicercaMorositaDAOImpl::ricercaMorosita] Data not found");
	        return Collections.emptyList();
	    } catch (SQLException e) {
	        LOGGER.error("[TipoRicercaMorositaDAOImpl::ricercaMorosita] Errore nell'esecuzione della query ", e);
	        throw new Exception(e);
	    } catch (Exception e) {
	        LOGGER.error("[TipoRicercaMorositaDAOImpl::ricercaMorosita] Errore generale ", e);
	        throw new Exception(e);
	    } finally {
	        LOGGER.debug("[TipoRicercaMorositaDAOImpl::ricercaMorosita] END");
	    }
	    return resultRicercaMorosita;
	}

	private Map<String, String> createTipoRicercaClauses() {
	    Map<String, String> tipoRicercaClauses = new HashMap<>();
	    tipoRicercaClauses.put(Constants.RICMOR01, RICMOR01);
	    tipoRicercaClauses.put(Constants.RICMOR02, RICMOR02);
	    tipoRicercaClauses.put(Constants.RICMOR03, RICMOR03);
	    tipoRicercaClauses.put(Constants.RICMOR04, RICMOR04);
	    tipoRicercaClauses.put(Constants.RICMOR05, RICMOR05);
	    tipoRicercaClauses.put(Constants.RICMOR06, RICMOR06);
	    tipoRicercaClauses.put(Constants.RICMOR07, RICMOR07);
	    tipoRicercaClauses.put(Constants.RICMOR08, RICMOR08);
	    tipoRicercaClauses.put(Constants.RICMOR09, RICMOR09);
	    tipoRicercaClauses.put(Constants.RICMOR10, RICMOR10);
	    tipoRicercaClauses.put(Constants.RICMOR11, RICMOR11);
	    tipoRicercaClauses.put(Constants.RICMOR12, RICMOR12);
	    tipoRicercaClauses.put(Constants.RICMOR13, RICMOR13);
	    tipoRicercaClauses.put(Constants.RICMOR14, RICMOR14);
	    tipoRicercaClauses.put(Constants.RICMOR15, RICMOR15);
	    tipoRicercaClauses.put(Constants.RICMOR16, RICMOR16);
	    return tipoRicercaClauses;
	}




	private String mapSortConCampiDB(String sort) {
        String nomeCampo="";
		if(sort.contains("codiceUtenza")) {
			if(sort.substring(1).equals("codiceUtenza") && sort.charAt(0) == '-')
			    nomeCampo = sort.substring(0, 1).concat(" risc.cod_riscossione ");
			else
				nomeCampo = " risc.cod_riscossione ";
		}
		if(sort.contains("dataScadenza")) {
			if(sort.substring(1).equals("dataScadenza") && sort.charAt(0) == '-')
			    nomeCampo = sort.substring(0, 1).concat(" rata.data_scadenza_pagamento ");
			else
				nomeCampo = " rata.data_scadenza_pagamento ";
		}
		return  getQuerySortingSegment(nomeCampo);

	}




	public RowMapper<StatoDebitorioExtendedDTO> getRowMapperStatoDebitorio() throws SQLException {
		return new StatoDebitorioRowMapper();
	}

	/**
	 * The type StatoDebitorioRowMapper.
	 */
	public static class StatoDebitorioRowMapper implements RowMapper<StatoDebitorioExtendedDTO> {
		
		/**
		 * Instantiates a new StatoDebitorioRowMapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public StatoDebitorioRowMapper() throws SQLException {
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
		public StatoDebitorioExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			StatoDebitorioExtendedDTO bean = new StatoDebitorioExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, StatoDebitorioExtendedDTO bean) throws SQLException {
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setIdStatoContribuzione(rs.getLong("id_stato_contribuzione"));
			bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setCodUtenza(rs.getString("cod_riscossione"));
			bean.setNap(rs.getString("nap"));
			bean.setImportoDovuto(rs.getBigDecimal("importo_dovuto"));
			bean.setAccDataScadenzaPag(rs.getString("data_scadenza"));
			bean.setAccImportoVersato(rs.getBigDecimal("somma_imp_versato"));
			bean.setAccImportoDiCanoneMancante(rs.getBigDecimal("importo_canone_mancante"));
			bean.setAccInteressiMancanti(rs.getBigDecimal("interessi_mancanti"));
			bean.setAccInteressiVersati(rs.getBigDecimal("interessi_versati"));
			bean.setSommaRimborsi(rs.getBigDecimal("somma_rimborsi"));
			bean.setDataPagamento(rs.getString("datapagamento"));
			bean.setImpCompensazioneCanone(rs.getBigDecimal("imp_compensazione_canone"));
			bean.setIdAttivitaStatoDeb(rs.getLong("id_attivita_stato_deb"));
			bean.setStatoRiscossione(rs.getString("cod_stato_riscossione"));
			bean.setImportoEccedente(rs.getBigDecimal("importo_eccedente"));
			bean.setFlgRestituitoMittente(rs.getInt("flg_restituito_mittente"));
			bean.setFlgAnnullato(rs.getInt("flg_annullato"));
			bean.setIdSoggetto(rs.getLong("id_soggetto")); 
			bean.setIntMaturatiSpeseNotifica(rs.getBigDecimal("interessi_dovuti")); 
			

		}
		

	}
	@Override
	public Integer countRicercaMorosita(String tipoRicercaMorosita, Long idAmbito, Integer anno, Integer flgRest, Integer flgAnn, String lim) {
	    LOGGER.debug("[TipoRicercaMorositaDAOImpl::countRicercaMorosita] BEGIN");
	    try {
	        StringBuilder queryBuilder = new StringBuilder(QUERY_LOAD_MOROSITA);
	        Map<String, Object> queryParams = new HashMap<>();

	        if (tipoRicercaMorosita != null) {
	            Map<String, String> tipoRicercaClauses = createTipoRicercaClauses();
	            if (tipoRicercaClauses.containsKey(tipoRicercaMorosita)) {
	                queryBuilder.append(tipoRicercaClauses.get(tipoRicercaMorosita));
	            }
	        }
	        if (idAmbito != null) {
	            queryParams.put("idAmbito", idAmbito);
	        }

	        // Verifica dei tipi di ricerca abilitati ai flag per coerenza con la query di ricerca
	        // [VF] Task 36
			List<String> tipiAbilitatiAiFlg = new ArrayList<>();
			tipiAbilitatiAiFlg.add(Constants.RICMOR03);
			tipiAbilitatiAiFlg.add(Constants.RICMOR04);
			tipiAbilitatiAiFlg.add(Constants.RICMOR06);
			tipiAbilitatiAiFlg.add(Constants.RICMOR07);
			tipiAbilitatiAiFlg.add(Constants.RICMOR09);
			tipiAbilitatiAiFlg.add(Constants.RICMOR14);
			if (flgRest.equals(1)) {
				if (tipiAbilitatiAiFlg.contains(tipoRicercaMorosita)) {
					queryParams.put("flgRest", flgRest);
					queryBuilder.append(" AND ");
					queryBuilder.append(FLG_RESTITUITO);
				}
			}

			if (flgAnn.equals(1)) {
				if (tipiAbilitatiAiFlg.contains(tipoRicercaMorosita)) {
					queryParams.put("flgAnn", flgAnn);
					queryBuilder.append(" AND ");
					queryBuilder.append(FLG_ANNULATO);
				}
			}

	        if (anno != null) {
	            queryParams.put("anno", anno.toString());
	                queryBuilder.append(" AND ");
	            queryBuilder.append(ANNO);
	        }
	        queryBuilder.append(GROUP_BY);
	        if (lim != null) {
	            String queryFilter = lim.length() >= 1 && Character.toString(lim.charAt(0)).equals("+") ? QUERY_FILTER_LIM_MAGGIORE : QUERY_FILTER_LIM_MINORE;
	            queryParams.put("lim", lim.substring(1));
	            queryBuilder.append(queryFilter);
	        }
	        
	        String finalQuery = "SELECT COUNT(*) FROM (" + queryBuilder.toString() + ") as tot_morosita";
	        SqlParameterSource params = new MapSqlParameterSource(queryParams);

	        return template.queryForObject(finalQuery, params, Integer.class);

	    } catch (DataAccessException e) {
	        LOGGER.error("[TipoRicercaMorositaDAOImpl::countRicercaMorosita] ERROR", e);
	    }
	    return null;
	}
	


}
