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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.IuvDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.IuvDTO;
import it.csi.risca.riscabesrv.dto.IuvExtendedDTO;
import it.csi.risca.riscabesrv.dto.StatoIuvDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type IuvDAOImpl dao.
 *
 * @author CSI PIEMONTE
 */
public class IuvDAOImpl extends RiscaBeSrvGenericDAO<IuvDTO> implements IuvDAO {
	
	public static final String QUERY_IUV_BY_NAP = "SELECT rti.*, rdsi.* FROM risca_t_iuv rti "
			+ "INNER JOIN risca_d_stato_iuv rdsi ON rdsi.id_stato_iuv = rti.id_stato_iuv "
			+ "WHERE rti.nap = :nap";
	
	public static final String QUERY_IUV_BY_ID = "SELECT rti.*, rdsi.* FROM risca_t_iuv rti "
			+ "INNER JOIN risca_d_stato_iuv rdsi ON rdsi.id_stato_iuv = rti.id_stato_iuv "
			+ "WHERE rti.id_iuv = :idIuv";
	
	private static final String QUERY_INSERT = "INSERT INTO risca_t_iuv "
			+ "(id_iuv, nap, id_stato_iuv, iuv, codice_avviso, importo, codice_versamento, "
			+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(:idIuv, :nap, :idStatoIuv, :iuv, :codiceAvviso, :importo, :codiceVersamento, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid);";
	
	private static final String QUERY_UPDATE_STATO_IUV_ATTIVO_BY_NAP = " update RISCA_T_IUV "
			+ " set id_stato_iuv = 1 "
			+ " where id_stato_iuv = 6 "
			+ " and nap = :nap ";
	
	private static final String QUERY_UPDATE_STATO_IUV_BY_NAP = "update RISCA_T_IUV "
			+ "set id_stato_iuv = ( select id_stato_iuv from risca_d_stato_iuv where cod_stato_iuv = :codStatoIuv), "
			+ "gest_attore_upd = :gestAttoreUpd, "
			+ "gest_data_upd = :gestDataUpd "
			+ "where nap = :nap ";
	
	private static final String QUERY_UPDATE_IMPORTO_IUV_BY_NAP = " update RISCA_T_IUV "
			+ "set importo = :importo, "
			+ "gest_attore_upd = :gestAttoreUpd, "
			+ "gest_data_upd = :gestDataUpd "
			+ "where nap = :nap ";
	
	private static final String QUERY_SELECT_IUV_BY_CODICE_AVVISO = " select * from risca_t_iuv where codice_avviso = :codiceAvviso ";

	@Override
	public IuvDTO saveIuv(IuvDTO dto) throws Exception {
		LOGGER.debug("[IuvDAOImpl::saveIuv] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_t_iuv");

			map.put("idIuv", genId);
			map.put("nap", dto.getNap());
			map.put("idStatoIuv", dto.getStatoIuv().getIdStatoIuv());
			map.put("iuv", dto.getIuv());
			map.put("codiceAvviso", dto.getCodiceAvviso());
			map.put("importo", dto.getImporto());
			map.put("codiceVersamento", dto.getCodiceVersamento());
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
			LOGGER.error("[IuvDAOImpl::saveIuv] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e ;

		} finally {
			LOGGER.debug("[IuvDAOImpl::saveIuv] END");
		}

		return dto;
	}

	@Override
	public IuvDTO getIuvByNap(String nap) throws Exception {

		LOGGER.debug("[IuvDAOImpl::getIuvByNap] BEGIN");
        Map<String, Object> map = new HashMap<>();
        List<IuvDTO> iuv = new ArrayList<IuvDTO>();
        IuvDTO iuvDTO = new IuvDTO();
        try {
            map.put("nap", nap);
            MapSqlParameterSource params = getParameterValue(map);
            iuv =  template.query(QUERY_IUV_BY_NAP, params, getRowMapper());
            
		}catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[IuvDAOImpl::getIuvByNap] Data not found for nap: "+ nap);
			return null;
		}  catch (Exception e) {
            LOGGER.error("[IuvDAOImpl::getIuvByNap] Errore generale ", e);
            LOGGER.debug("[IuvDAOImpl::getIuvByNap] END");
            throw e;
		}
        LOGGER.debug("[IuvDAOImpl::getIuvByNap] END");
        if(iuv.size() > 0) {
        	iuvDTO = iuv.get(0);
        	return iuvDTO;
        }
        else 
        	return iuvDTO;
        
	}
	
	@Override
	public IuvDTO getIuvById(Long idIuv) throws Exception {

		LOGGER.debug("[IuvDAOImpl::getIuvById] BEGIN");
        Map<String, Object> map = new HashMap<>();
        List<IuvDTO> iuv = new ArrayList<IuvDTO>();
        IuvDTO iuvDTO = new IuvDTO();
        try {
            map.put("idIuv", idIuv);
            MapSqlParameterSource params = getParameterValue(map);
            iuv =  template.query(QUERY_IUV_BY_ID, params, getRowMapper());
            
		}catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[IuvDAOImpl::getIuvById] Data not found for id: "+ idIuv);
			return null;
		}  catch (Exception e) {
            LOGGER.error("[IuvDAOImpl::getIuvById] Errore generale ", e);
            LOGGER.debug("[IuvDAOImpl::getIuvById] END");
            throw e;
		}
        LOGGER.debug("[IuvDAOImpl::getIuvById] END");
        if(iuv.size() > 0) {
        	iuvDTO = iuv.get(0);
        	return iuvDTO;
        }
        else 
        	return iuvDTO;
        
	}
	
	@Override
	public IuvDTO getIuvByCodiceAvviso(String codiceAvviso) {
		LOGGER.debug("[IuvDAOImpl::getIuvByCodiceAvviso] BEGIN");
		Map<String, Object> map = new HashMap<>();
		List<IuvDTO> iuv = new ArrayList<IuvDTO>();
		IuvDTO iuvDTO = new IuvDTO();
		try {
			map.put("codiceAvviso", codiceAvviso);
			MapSqlParameterSource params = getParameterValue(map);
			iuv = template.query(QUERY_SELECT_IUV_BY_CODICE_AVVISO, params, getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[IuvDAOImpl::getIuvByCodiceAvviso] Data not found for codiceAvviso: " + codiceAvviso);
			return null;
		} catch (Exception e) {
			LOGGER.debug("[IuvDAOImpl::getIuvByCodiceAvviso]ERROR: " + e.getMessage());
			return null;
		}
		LOGGER.debug("[IuvDAOImpl::getIuvByCodiceAvviso] END");
		if (iuv.size() > 0) {
			iuvDTO = iuv.get(0);
			return iuvDTO;
		} else {
			return null;
		}
	}
	
	@Override
	public Integer updateStatoIuvInseritoByNap(String nap) {
		LOGGER.debug("[IuvDAOImpl::updateStatoIuvInseritoByNap] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("nap", nap);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_UPDATE_STATO_IUV_ATTIVO_BY_NAP, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[IuvDAOImpl::updateStatoIuvInseritoByNap] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e ;

		} finally {
			LOGGER.debug("[IuvDAOImpl::updateStatoIuvInseritoByNap] END");
		}
	}
	
	@Override
	public Integer updateStatoIuvByNap(String nap, String codStatoIuv, String attore) {
		LOGGER.debug("[IuvDAOImpl::updateStatoIuvByNap] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("codStatoIuv", codStatoIuv);
			map.put("nap", nap);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_UPDATE_STATO_IUV_BY_NAP, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[IuvDAOImpl::updateStatoIuvByNap] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e ;

		} finally {
			LOGGER.debug("[IuvDAOImpl::updateStatoIuvByNap] END");
		}
	}
	
	@Override
	public Integer updateImportoIuvByNap(String nap, BigDecimal importo, String attore) {
		LOGGER.debug("[IuvDAOImpl::updateImportoIuvByNap] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("importo", importo);
			map.put("nap", nap);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_UPDATE_IMPORTO_IUV_BY_NAP, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[IuvDAOImpl::updateImportoIuvByNap] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e ;

		} finally {
			LOGGER.debug("[IuvDAOImpl::updateImportoIuvByNap] END");
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
	public RowMapper<IuvDTO> getRowMapper() throws SQLException {
		return new IuvRowMapper();
	}


	@Override
	public RowMapper<IuvDTO> getExtendedRowMapper() throws SQLException {
		return new IuvRowMapper();
	}

	/**
	 * The type row mapper.
	 */
	public static class IuvRowMapper implements RowMapper<IuvDTO> {

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
		public IuvDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			IuvDTO bean = new IuvDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, IuvDTO bean) throws SQLException {
			bean.setIdIuv(rs.getLong("id_iuv"));
			bean.setNap(rs.getString("nap"));
	        StatoIuvDTO statoIuv = new StatoIuvDTO();
	        populateBeanStatoIuv(rs, statoIuv);
	        bean.setStatoIuv(statoIuv);
			bean.setIuv(rs.getString("iuv"));
			bean.setCodiceAvviso(rs.getString("codice_avviso"));
			bean.setImporto(rs.getBigDecimal("importo"));
			bean.setCodiceVersamento(rs.getString("codice_versamento"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}
	    private void populateBeanStatoIuv(ResultSet rs, StatoIuvDTO bean) throws SQLException {
	        bean.setIdStatoIuv(rs.getLong("id_stato_iuv"));
	        bean.setCodStatoIuv(rs.getString("cod_stato_iuv"));
	        bean.setDesStatoIuv(rs.getString("des_stato_iuv"));
	    }
    }
	
	
	@Override
	public List<IuvExtendedDTO> getSumCanoneSpeseAndTotVersatoByNap(String nap) throws  DAOException, SystemException {
		LOGGER.debug("[IuvDAOImpl::getSumCanoneSpeseAndTotVersatoByNap] BEGIN");
		
		List<IuvExtendedDTO> iuvExtendedList = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();

		sql.append(" select sd.nap, iu.id_iuv , iu.IUV, coalesce ( sum(r.canone_dovuto),0) as Dovuto_IUV, coalesce( sum(sd.imp_spese_notifica),0) as Spese_IUV, "); 
		sql.append(" coalesce (sum(dp.tot_versato),0) as Versato_IUV , (select max (r.data_scadenza_pagamento) ) data_scadenza_pagamento, ( select min(flg_annullato) ) as flg_annullato  ");
		sql.append(" from risca_t_iuv iu, risca_t_stato_debitorio sd, ");
		sql.append(" risca_r_rata_sd r  left outer join   ");
		sql.append("            (select id_rata_sd, SUM(a.importo_versato) as tot_versato ");
		sql.append("               from risca_r_dettaglio_pag a, risca_t_pagamento b ");
		sql.append("			      where a.id_pagamento = b.id_pagamento ");
		sql.append("               group by id_rata_sd) dp  on  dp.id_rata_sd = r.id_rata_sd   ");
		sql.append(" where sd.id_stato_debitorio = r.id_stato_debitorio ");
		sql.append("     and iu.nap = sd.nap ");
		sql.append("     and sd.nap = :nap ");
		sql.append("     and iu.id_stato_iuv = "+Constants.DB.STATO_IUV.ID_STATO_IUV.ATTIVO); 
		sql.append(" 	 and r.id_rata_sd_padre IS null ");
		sql.append("     and sd.flg_annullato = 0 ");
		sql.append(" group by sd.nap,iu.id_iuv ,iu.IUV, iu.id_stato_iuv  ");


		paramMap.addValue("nap", nap);

		LOGGER.debug("[IuvDAOImpl - getSumCanoneSpeseAndTotVersatoByNap] query =" + sql.toString());

		try
		{
			iuvExtendedList = template.query(sql.toString(), paramMap, new ExtendedIuvRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[IuvDAOImpl::getSumCanoneSpeseAndTotVersatoByNap]  NESSUN RISULTATO");
			throw new DAOException("Nessun dato in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[IuvDAOImpl::getSumCanoneSpeseAndTotVersatoByNap] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[IuvDAOImpl::getSumCanoneSpeseAndTotVersatoByNap] END");
		}
		return (List<IuvExtendedDTO>) iuvExtendedList;
	}
	
	
	public static class ExtendedIuvRowMapper implements RowMapper<IuvExtendedDTO> {

		public ExtendedIuvRowMapper() throws SQLException {}

		@Override
		public IuvExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			IuvExtendedDTO bean = new IuvExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, IuvExtendedDTO bean) throws SQLException {
			bean.setNap(rs.getString("nap"));
			bean.setIdIuv(rs.getLong("id_iuv"));
			bean.setIuv(rs.getString("iuv"));
			bean.setDovutoIuv(rs.getBigDecimal("dovuto_iuv"));
			bean.setSpeseIuv(rs.getBigDecimal("spese_iuv"));
			bean.setVersatoIuv(rs.getBigDecimal("versato_iuv"));
			bean.setDataScadPag(rs.getDate("data_scadenza_pagamento"));
			bean.setFlgAnnullato(rs.getInt("flg_annullato"));
			
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

}
