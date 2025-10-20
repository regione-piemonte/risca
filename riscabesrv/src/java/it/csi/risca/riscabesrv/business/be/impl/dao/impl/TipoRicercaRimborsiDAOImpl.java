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

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRicercaRimborsiDAO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoRicercaRimborsoDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class TipoRicercaRimborsiDAOImpl extends RiscaBeSrvGenericDAO<TipoRicercaRimborsoDTO> implements TipoRicercaRimborsiDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	public static final String QUERY_SELECT_ALL_TIPO_RICERCA_RIMBORSI = "select * from risca_d_tipo_ricerca_rimborso  ";

	
	public static final String QUERY_LOAD_RICERCA_RIMBORSI =" WITH SOMMA AS  \r\n" + 
			"			   (select SUM(DECODE (T.id_tipo_rimborso, 1, T.imp_rimborso, T.imp_restituito)) SOMMA_RIMBORSI, \r\n" + 
			"			           T.id_stato_debitorio \r\n" + 
			"			      FROM risca_r_rimborso T, \r\n" + 
			"			           risca_t_stato_debitorio SD \r\n" + 
			"			     WHERE T.id_stato_debitorio = SD.id_stato_debitorio \r\n" + 
			"			  GROUP BY T.id_stato_debitorio \r\n" + 
			"			 ) \r\n" + 
			"			 select distinct sd.id_stato_debitorio,  \r\n" + 
			"			                 sd.nap, rts.id_soggetto,  \r\n" + 
			"			                 rata.canone_dovuto,  \r\n" + 
			"			                 risc.cod_riscossione,  \r\n" + 
			"			                 risc.id_riscossione,  \r\n" + 
			"			                 rata.canone_dovuto  \r\n" + 
			"			                 importo_dovuto ,  \r\n" + 
			"			                 TO_CHAR(rata.data_scadenza_pagamento, 'yyyy-mm-dd') data_scadenza,  \r\n" + 
			"			                 coalesce(rata.interessi_maturati,0) + coalesce(sd.imp_spese_notifica, 0) interessi_dovuti, \r\n" + 
			"			                 SUM(det.importo_versato) somma_imp_versato,  \r\n" + 
			"			                 -- ------------------------------------- importo_canone_mancante \r\n" + 
			"			                   CASE  \r\n" + 
			"			                   WHEN coalesce(rata.canone_dovuto, 0)  +    \r\n" + 
			"			                        coalesce(rimb1.imp_rimb, 0) -         \r\n" + 
			"			                        coalesce(SUM(det.importo_versato), 0) < 0   \r\n" + 
			"			                         THEN 0   \r\n" + 
			"			                    ELSE coalesce(rata.canone_dovuto, 0) +    \r\n" + 
			"			                         coalesce(rimb1.imp_rimb, 0) -    \r\n" + 
			"			                         coalesce(SUM(det.importo_versato), 0)  \r\n" + 
			"			                  END importo_canone_mancante,  					\r\n" + 
			"			                 -- ------------------------------------- interessi_mancanti \r\n" + 
			"			                  CASE   \r\n" + 
			"			                     WHEN (coalesce(rata.canone_dovuto, 0) ) +    \r\n" + 
			"			                          coalesce(rimb1.imp_rimb, 0) -      \r\n" + 
			"			                          coalesce(SUM(det.importo_versato), 0) > 0   \r\n" + 
			"			                          THEN (coalesce(rata.interessi_maturati, 0) + coalesce(sd.imp_spese_notifica, 0))   \r\n" + 
			"			                  ELSE   \r\n" + 
			"			                     CASE   \r\n" + 
			"			                      WHEN (coalesce(rata.interessi_maturati, 0) + coalesce(sd.imp_spese_notifica, 0)) +    \r\n" + 
			"			                           (coalesce(rata.canone_dovuto, 0) ) +    \r\n" + 
			"			                            coalesce(rimb1.imp_rimb, 0) -    \r\n" + 
			"			                            coalesce(SUM(det.importo_versato), 0) < 0   \r\n" + 
			"			                           THEN 0   \r\n" + 
			"			                      ELSE (coalesce(rata.interessi_maturati, 0) + coalesce(sd.imp_spese_notifica, 0)) +   \r\n" + 
			"			                            (coalesce(rata.canone_dovuto, 0) ) +   \r\n" + 
			"			                             coalesce(rimb1.imp_rimb, 0) -      \r\n" + 
			"			                             coalesce(SUM(det.importo_versato), 0)  \r\n" + 
			"			                       END   \r\n" + 
			"			                  END interessi_mancanti,   						\r\n" + 
			"			                 -- ------------------------------------ interessi_versati \r\n" + 
			"			                   CASE   \r\n" + 
			"			                   WHEN (coalesce(rata.canone_dovuto, 0) ) +    \r\n" + 
			"			                        coalesce(rimb1.imp_rimb, 0) -    \r\n" + 
			"			                        coalesce(SUM(det.importo_versato), 0) >= 0  \r\n" + 
			"			                      THEN 0   \r\n" + 
			"			                 ELSE ABS((coalesce(rata.canone_dovuto, 0) ) +   \r\n" + 
			"			                        coalesce(rimb1.imp_rimb, 0) -         \r\n" + 
			"			                       coalesce(SUM(det.importo_versato), 0))   \r\n" + 
			"			                  END interessi_versati,  \r\n" + 
			"			                 coalesce(rimb.imp_rimb, 0) rimborsi,  \r\n" + 
			"			                 sd.flg_annullato as stato,  \r\n" + 
			"			                 TO_CHAR(MAX(pag.data_op_val), 'yyyy-mm-dd') dataPagamento,  \r\n" + 
			"			                 sd.id_attivita_stato_deb, \r\n" + 
			"			                 -- ----------------------------------- importo_eccedente  \r\n" + 
			"			                 CASE  \r\n" + 
			"			                    WHEN (COALESCE(SUM(det.importo_versato),0) -(  COALESCE(rata.canone_dovuto,0) +  COALESCE(sd.imp_spese_notifica,0)   \r\n" + 
			"			                          + COALESCE(rata.interessi_maturati ,0) ) - COALESCE(rimb1.imp_rimb, 0)) > 0  \r\n" + 
			"			                    THEN  \r\n" + 
			"			                         (COALESCE(SUM(det.importo_versato),0) -(  COALESCE(rata.canone_dovuto,0) +  COALESCE(sd.imp_spese_notifica,0)  \r\n" + 
			"			                          + COALESCE(rata.interessi_maturati ,0) ) - COALESCE(rimb1.imp_rimb, 0) ) \r\n" + 
			"			                    ELSE 0  \r\n" + 
			"			                 END importo_eccedente, \r\n" + 
			"			                 -- -------------------------------------------------------- \r\n" + 
			"			                 rdsr.cod_stato_riscossione, \r\n" + 
			"			                 som.somma_rimborsi as imp_compensazione_canone   -- (il valore di 'somma_rimborsi' e mappato su 'imp_compensazione_canone') \r\n" + 
			"			     FROM risca_t_riscossione risc  \r\n" + 
			"			     join risca_d_stato_riscossione rdsr on risc.id_stato_riscossione = rdsr.id_stato_riscossione  \r\n" + 
			"			     join risca_d_tipo_riscossione dtr on risc.id_tipo_riscossione = dtr.id_tipo_riscossione  \r\n" + 
			"			     join risca_t_stato_debitorio sd on risc.id_riscossione = sd.id_riscossione   \r\n" + 
			"			     join risca_t_soggetto rts on sd.id_soggetto = rts.id_soggetto  \r\n" + 
			"			     join risca_r_rata_sd rata on sd.id_stato_debitorio = rata.id_stato_debitorio  \r\n" + 
			"			                              AND rata.id_rata_sd_padre IS NULL  \r\n" + 
			"			     left join risca_r_dettaglio_pag det on rata.id_rata_sd = det.id_rata_sd  \r\n" + 
			"			     left join risca_t_pagamento pag on det.id_pagamento = pag.id_pagamento  \r\n" + 
			"			     left join risca_d_stato_contribuzione stato_contr on sd.id_stato_contribuzione = stato_contr.id_stato_contribuzione  \r\n" + 
			"			     left join (SELECT rimb.id_stato_debitorio , \r\n" + 
			"			                       SUM(rimb.imp_rimborso) imp_rimb  \r\n" + 
			"			                  FROM risca_r_rimborso rimb \r\n" + 
			"			              GROUP BY rimb.id_stato_debitorio) rimb on sd.id_stato_debitorio = rimb.id_stato_debitorio   \r\n" + 
			"			     left join (SELECT rimb.id_stato_debitorio, \r\n" + 
			"			                       SUM(rimb.imp_rimborso) imp_rimb  \r\n" + 
			"			                  FROM risca_r_rimborso rimb \r\n" + 
			"			                 where rimb.id_tipo_rimborso = 1 \r\n" + 
			"			              GROUP BY rimb.id_stato_debitorio) rimb1 on sd.id_stato_debitorio = rimb1.id_stato_debitorio \r\n" + 
			"			     left join somma som on sd.id_stato_debitorio = som.id_stato_debitorio            " + 
			"			     left join risca_r_rimborso rim on sd.id_stato_debitorio = rim.id_stato_debitorio " + 
			"			    where rata.canone_dovuto <> 0  "
			+ "     and dtr.id_ambito = :idAmbito ";
	
	public static final String ORDER_BY_SD =" ORDER BY cod_riscossione asc ";
	
	public static final String ANNO ="  AND TO_CHAR(rata.data_scadenza_pagamento,'yyyy') = :anno ";
	
	public static final String GROUP_BY =" GROUP BY sd.id_stato_debitorio, \r\n"
			+ "         risc.id_riscossione, \r\n"
			+ "         risc.cod_riscossione, \r\n"
			+ "         rata.canone_dovuto, \r\n"
			+ "         rata.interessi_maturati, \r\n"
			+ "         sd.imp_spese_notifica,         \r\n"
			+ "         rata.data_scadenza_pagamento, \r\n"
			+ "         sd.flg_annullato, \r\n"
			+ "         rimb.imp_rimb, \r\n"
			+ "         rimb1.imp_rimb, \r\n"
			+ "         sd.id_attivita_stato_deb, \r\n"
			+ "         sd.imp_compensazione_canone, \r\n"
			+ "         rdsr.cod_stato_riscossione, \r\n"
			+ "         risc.id_riscossione, \r\n"
			+ "         rts.id_soggetto,\r\n"
			+ "         som.somma_rimborsi\r\n";
	
	
	public static final String RICRIM01 =" AND sd.id_STATO_CONTRIBUZIONE = 4 and sd.id_attivita_stato_deb is null ";
	public static final String RICRIM02 =" AND sd.id_attivita_stato_deb = 6 ";
	public static final String RICRIM03 =" AND rim.id_tipo_rimborso = 1 ";
	public static final String RICRIM04 =" AND sd.id_STATO_CONTRIBUZIONE = 4 and sd.id_attivita_stato_deb = 1 ";
	public static final String RICRIM05 =" AND (rim.id_tipo_rimborso = 2 or sd.id_attivita_stato_deb = 8) ";
	public static final String RICRIM06 =" AND rim.id_tipo_rimborso = 3 ";
	
	public static final String  QUERY_TOT_RICERCA_RIMBORSI =  QUERY_LOAD_RICERCA_RIMBORSI  ;
	@Override
	public List<TipoRicercaRimborsoDTO>  loadAllTipoRicercaRimborsi() throws DAOException {
		LOGGER.debug("[TipoRicercaRimborsiDAOImpl::loadAllTipoRicercaMorosita] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<TipoRicercaRimborsoDTO> res = new ArrayList<TipoRicercaRimborsoDTO>();
		try {

			res = findSimpleDTOListByQuery(CLASSNAME, methodName, QUERY_SELECT_ALL_TIPO_RICERCA_RIMBORSI, null);

		} catch (Exception e) {
			LOGGER.error("[TipoRicercaRimborsiDAOImpl::loadAllTipoRicercaMorosita] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[TipoRicercaRimborsiDAOImpl::loadAllTipoRicercaMorosita] END");
		}

		return res;
	}



	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public RowMapper<TipoRicercaRimborsoDTO> getRowMapper() throws SQLException {
		return new TipoRicercaRimborsiRowMapper();
	}



	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * The type TipoRicercaMorositaRowMapper.
	 */
	public static class TipoRicercaRimborsiRowMapper implements RowMapper<TipoRicercaRimborsoDTO> {
		
		/**
		 * Instantiates a new TipoRicercaMorositaRowMapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public TipoRicercaRimborsiRowMapper() throws SQLException {
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
		public TipoRicercaRimborsoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TipoRicercaRimborsoDTO bean = new TipoRicercaRimborsoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, TipoRicercaRimborsoDTO bean) throws SQLException {
			bean.setIdTipoRicercaRimborso(rs.getLong("id_tipo_ricerca_rimborso"));
			bean.setCodTipoRicercaRimborso(rs.getString("cod_tipo_ricerca_rimborso"));
			bean.setDesTipoRicercaRimborso(rs.getString("des_tipo_ricerca_rimborso"));

		}

	}
	@Override
	public List<StatoDebitorioExtendedDTO> ricercaRimborsi(String tipoRicercaRimborsi,Long idAmbito, Integer anno, Integer offset,
			Integer limit, String sort) throws Exception {
		LOGGER.debug("[TipoRicercaRimborsiDAOImpl::ricercaRimborsi] BEGIN");
		List<StatoDebitorioExtendedDTO> resultRicercaRimborsi = new ArrayList<StatoDebitorioExtendedDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = null;
	        String dynamicOrderByCondition="";
			if (StringUtils.isNotBlank(sort)) {
				   dynamicOrderByCondition = mapSortConCampiDB(sort);
				   if(dynamicOrderByCondition != null) {
					   dynamicOrderByCondition = dynamicOrderByCondition.concat(",").concat(ORDER_BY_SD.replace("ORDER BY", ""));
				   }
			}else {
				dynamicOrderByCondition = ORDER_BY_SD;
			}
	         String query =  QUERY_LOAD_RICERCA_RIMBORSI ;
	         if(tipoRicercaRimborsi != null) {
	        	 if (tipoRicercaRimborsi.equals(Constants.RICRIM01)) 
	        		 query = query  + RICRIM01;
	        	 else if (tipoRicercaRimborsi.equals(Constants.RICRIM02)) 
	         		 query = query  + RICRIM02;
	        	 else if (tipoRicercaRimborsi.equals(Constants.RICRIM03)) 
	         		 query = query  + RICRIM03;
	        	 else if (tipoRicercaRimborsi.equals(Constants.RICRIM04)) 
	         		 query = query  + RICRIM04;	
	        	 else if (tipoRicercaRimborsi.equals(Constants.RICRIM05)) 
	         		 query = query  + RICRIM05;		
	        	 else if (tipoRicercaRimborsi.equals(Constants.RICRIM06)) 
	         		 query = query  + RICRIM06;	
	         }
	         if(idAmbito != null) {
			    map.put("idAmbito", idAmbito);	 
		     }
	         if(anno != null) {
	        	 query =  query + ANNO;  
	             map.put("anno", anno.toString());
	         }
	         query = query + GROUP_BY;
	         params = getParameterValue(map);
	         resultRicercaRimborsi = template.query(getQuery(query + dynamicOrderByCondition, offset!= null ? offset.toString(): null ,  limit != null ? limit.toString(): null), params, getRowMapperStatoDebitorio());
			
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[TipoRicercaRimborsiDAOImpl::ricercaRimborsi] Data not found");
			return Collections.emptyList();
		} catch (SQLException e) {
			LOGGER.error("[TipoRicercaRimborsiDAOImpl::ricercaRimborsi] Errore nell'esecuzione della query ", e);
			throw new Exception(e);
		}  catch (Exception e) {
			LOGGER.error("[TipoRicercaRimborsiDAOImpl::ricercaRimborsi] Errore generale ", e);
			throw new Exception(e);
		} finally {
			LOGGER.debug("[TipoRicercaRimborsiDAOImpl::ricercaRimborsi] END");
		}
		return resultRicercaRimborsi;
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
			bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setCodUtenza(rs.getString("cod_riscossione"));
			bean.setImportoDovuto(rs.getBigDecimal("importo_dovuto"));
			bean.setAccDataScadenzaPag(rs.getString("data_scadenza"));
			bean.setNap(rs.getString("nap"));
			bean.setAccImportoVersato(rs.getBigDecimal("somma_imp_versato"));
			bean.setAccImportoDiCanoneMancante(rs.getBigDecimal("importo_canone_mancante"));
			bean.setAccInteressiMancanti(rs.getBigDecimal("interessi_mancanti"));
			bean.setAccInteressiVersati(rs.getBigDecimal("interessi_versati"));
			bean.setDataPagamento(rs.getString("datapagamento"));
			bean.setImpCompensazioneCanone(rs.getBigDecimal("imp_compensazione_canone"));
			bean.setIdAttivitaStatoDeb(rs.getLong("id_attivita_stato_deb"));
			bean.setStatoRiscossione(rs.getString("cod_stato_riscossione"));
			bean.setIdSoggetto(rs.getLong("id_soggetto")); 
			bean.setIntMaturatiSpeseNotifica(rs.getBigDecimal("interessi_dovuti")); 
			bean.setImportoEccedente(rs.getBigDecimal("importo_eccedente"));
		}
		

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

	@Override
	public Integer countRicercaRimborsi(String tipoRicercaRimborsi,Long idAmbito, Integer anno) throws Exception {
		LOGGER.debug("[TipoRicercaRimborsiDAOImpl::countRicercaRimborsi] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			MapSqlParameterSource params = null;
	         String query = QUERY_TOT_RICERCA_RIMBORSI;
	         String where=" where ";
	         String and =" and ";
	         if(tipoRicercaRimborsi != null) {
	        	 if (tipoRicercaRimborsi.equals(Constants.RICRIM01)) 
	        		 query = query  + RICRIM01;
	        	 else if (tipoRicercaRimborsi.equals(Constants.RICRIM02)) 
	         		 query = query  + RICRIM02;
	        	 else if (tipoRicercaRimborsi.equals(Constants.RICRIM03)) 
	         		 query = query  + RICRIM03;
	        	 else if (tipoRicercaRimborsi.equals(Constants.RICRIM04)) 
	         		 query = query  + RICRIM04;	
	        	 else if (tipoRicercaRimborsi.equals(Constants.RICRIM05)) 
	         		 query = query  + RICRIM05;		
	        	 else if (tipoRicercaRimborsi.equals(Constants.RICRIM06)) 
	         		 query = query  + RICRIM06;	
	         }
	         if(idAmbito != null) {
			    map.put("idAmbito", idAmbito);	 
		     }
	         if(anno != null) {
	        	 query = query  + ANNO;  
	             map.put("anno", anno.toString());
	         }
	         query = query + GROUP_BY ;
	         query = "SELECT COUNT(*) FROM ( "+ query + " ) as tot_rimborsi " ;
	         params = getParameterValue(map);
			return template.queryForObject(query , params, Integer.class);			
		} catch (DataAccessException e) {
			LOGGER.error("[TipoRicercaRimborsiDAOImpl::countRicercaRimborsi] ERROR", e);
			throw new Exception(e);
		}	
	}

}
