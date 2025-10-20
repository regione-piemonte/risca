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
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.risca.riscabesrv.business.be.impl.dao.BilAccDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.BilAccDTO;

/**
 * The type BilAcc impl.
 *
 * @author CSI PIEMONTE
 */
public class BilAccDAOImpl extends RiscaBeSrvGenericDAO<BilAccDTO> implements BilAccDAO {

	private final String className = this.getClass().getSimpleName();

	private static final String QUERY_BY_COD_ACCERTA_BILANCIO = " select * from risca_t_bil_acc ba, risca_d_accerta_bilancio ab "
			+ " where ba.id_accerta_bilancio = ab.id_accerta_bilancio and id_ambito = :idAmbito "
			+ " and :annoPag >= extract(year from ba.data_inizio_validita) "
			+ " and :annoPag <= extract(year from ba.data_fine_validita) "
			+ " and cod_accerta_bilancio = :codAccertaBilancio ";
	private static final String QUERY_BY_ID_ACCERTA_BILANCIO = " select * from risca_t_bil_acc ba, risca_d_accerta_bilancio ab "
			+ " where ba.id_accerta_bilancio = ab.id_accerta_bilancio and id_ambito = :idAmbito "
			+ " and :annoPag >= extract(year from ba.data_inizio_validita) "
			+ " and :annoPag <= extract(year from ba.data_fine_validita) "
			+ " and ab.id_accerta_bilancio  = :idAccertaBilancio ";
	private static final String COND_ANNO = " and anno = :anno ";
	private static final String COND_ANNO_COMPETENZA = " and anno_competenza = :annoCompetenza ";
	
	private static final String QUERY_NUMERO_ACC_BILANCIO_BY_DATE = "select anno || cod_bil_acc numero_acc_bilancio "
			+ "	from risca_T_bil_acc where id_accerta_bilancio = :idAccertaBilancio and anno_competenza = EXTRACT(year from :dataScadenzaPag::date) and data_fine_validita > current_date "
			+ "	group by anno, cod_bil_acc ";
	
	private static final String QUERY_ACCERTA_BILANCIO_PIU_VECCHIO = " select * from risca_t_bil_acc ba, risca_d_accerta_bilancio ab "
			+ "  where ba.id_accerta_bilancio = ab.id_accerta_bilancio and id_ambito = :idAmbito  "
			+ "  and :annoPag >= extract(year from ba.data_inizio_validita) "
			+ "  and :annoPag <= extract(year from ba.data_fine_validita) "
			+ "  and cod_accerta_bilancio = :codAccertaBilancio  "
			+ "  order by anno_competenza asc "
			+ "  limit 1";

	@Override
	public BilAccDTO loadBilAccByCodAccertaBilancio(Long idAmbito, Integer anno, Integer annoCompetenza,
			String codAccertaBilancio, Integer annoPag) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Map<String, Object> map = new HashMap<>();
		BilAccDTO bilAccDTO = null;
		try {
			String query = QUERY_BY_COD_ACCERTA_BILANCIO;

			map.put("idAmbito", idAmbito);
			map.put("codAccertaBilancio", codAccertaBilancio);
			map.put("annoPag", annoPag);
			if (anno != null) {
				map.put("anno", anno);
				query += COND_ANNO;
			}
			if (annoCompetenza != null) {
				map.put("annoCompetenza", annoCompetenza);
				query += COND_ANNO_COMPETENZA;
			}

			MapSqlParameterSource params = getParameterValue(map);
			bilAccDTO = template.queryForObject(query, params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("Exception for input: anno=" + anno + " annoCompetenza=" + annoCompetenza + " codAccertaBilancio="
					+ codAccertaBilancio + " annoPag=" + annoPag);
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e));
			return null;

		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return bilAccDTO;
	}

	@Override
	public BilAccDTO loadBilAccByIdAccertaBilancio(Long idAmbito, Integer anno, Integer annoCompetenza,
			Long idAccertaBilancio, Integer annoPag) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Map<String, Object> map = new HashMap<>();
		BilAccDTO bilAccDTO = null;
		try {
			String query = QUERY_BY_ID_ACCERTA_BILANCIO;

			map.put("idAmbito", idAmbito);
			map.put("idAccertaBilancio", idAccertaBilancio);
			map.put("annoPag", annoPag);
			if (anno != null) {
				map.put("anno", anno);
				query += COND_ANNO;
			}
			if (annoCompetenza != null) {
				map.put("annoCompetenza", annoCompetenza);
				query += COND_ANNO_COMPETENZA;
			}

			MapSqlParameterSource params = getParameterValue(map);
			bilAccDTO = template.queryForObject(query, params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error("Exception for input: anno=" + anno + " annoCompetenza=" + annoCompetenza + " idAccertaBilancio="
					+ idAccertaBilancio + " annoPag=" + annoPag);
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e));
			return null;

		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return bilAccDTO;
	}
	
	@Override
	public BilAccDTO getNumeroAccBilancioByDataScadenzaPag(String dataScadenzaPag, int idAccertaBilancio) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName)); 
		LOGGER.debug("getNumeroAccBilancioByDataScadenzaPag dataScadenzaPag: "+dataScadenzaPag); 
		LOGGER.debug("getNumeroAccBilancioByDataScadenzaPag idAccertaBilancio: "+idAccertaBilancio); 
		BilAccDTO bilAccDTO = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("dataScadenzaPag", dataScadenzaPag);
			map.put("idAccertaBilancio", idAccertaBilancio);
            MapSqlParameterSource params = getParameterValue(map);
            bilAccDTO=  template.queryForObject(QUERY_NUMERO_ACC_BILANCIO_BY_DATE, params, getExtendedRowMapper());
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			return null;
				
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		}finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return bilAccDTO;
	}
	
	
	@Override
	public BilAccDTO loadBilAccValidoPiuVecchio(Long idAmbito, String codAccertaBilancio, Integer annoPag) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Map<String, Object> map = new HashMap<>();
		BilAccDTO bilAccDTO = null;
		try {
			String query = QUERY_ACCERTA_BILANCIO_PIU_VECCHIO;

			map.put("idAmbito", idAmbito);
			map.put("codAccertaBilancio", codAccertaBilancio);
			map.put("annoPag", annoPag);

			MapSqlParameterSource params = getParameterValue(map);
			bilAccDTO = template.queryForObject(query, params, getRowMapper());
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e));
			return null;

		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return bilAccDTO;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<BilAccDTO> getRowMapper() throws SQLException {
		return new BilAccRowMapper();
	}

	/**
	 * The type Ambiti row mapper.
	 */
	public static class BilAccRowMapper implements RowMapper<BilAccDTO> {

		/**
		 * Instantiates a new Ambiti row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public BilAccRowMapper() throws SQLException {
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
		public BilAccDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			BilAccDTO bean = new BilAccDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, BilAccDTO bean) throws SQLException {
			
			if(rsHasColumn(rs, "numero_acc_bilancio")) bean.setNumero_acc_bilancio(rs.getString("numero_acc_bilancio"));
			if(rsHasColumn(rs, "id_ambito")) bean.setIdAmbito(rs.getLong("id_ambito"));
			if(rsHasColumn(rs, "id_bil_acc")) bean.setIdBilAcc(rs.getLong("id_bil_acc"));
			if(rsHasColumn(rs, "id_accerta_bilancio")) bean.setIdAccertaBilancio(rs.getLong("id_accerta_bilancio"));
			if(rsHasColumn(rs, "cod_bil_acc")) bean.setCodBilAcc(rs.getString("cod_bil_acc"));
			if(rsHasColumn(rs, "des_bil_acc")) bean.setDesBilAcc(rs.getString("des_bil_acc"));
			if(rsHasColumn(rs, "anno")) bean.setAnno(rs.getInt("anno"));
			if(rsHasColumn(rs, "anno_competenza")) bean.setAnnoCompetenza(rs.getInt("anno_competenza"));
			if(rsHasColumn(rs, "note_backoffice")) bean.setNoteBackoffice(rs.getString("note_backoffice"));
			if(rsHasColumn(rs, "dati_spec_risc")) bean.setDatiSpecRisc(rs.getString("dati_spec_risc"));
			if(rsHasColumn(rs, "data_inizio_validita")) bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
			if(rsHasColumn(rs, "data_fine_validita")) bean.setDataFineValidita(rs.getDate("data_fine_validita"));
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
			
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
	public RowMapper<BilAccDTO> getExtendedRowMapper() throws SQLException {
		return new BilAccRowMapper();
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

