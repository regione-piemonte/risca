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
import java.util.Collections;
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
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoSdUtilizzatoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.RimborsoSdUtilizzatoDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type ParametroElabora dao.
 *
 * @author CSI PIEMONTE
 */
public class RimborsoSdUtilizzatoDAOImpl extends RiscaBeSrvGenericDAO<RimborsoSdUtilizzatoDTO> implements RimborsoSdUtilizzatoDAO {

	private final String className = this.getClass().getSimpleName();
	
	private static final String QUERY_INSERT = " INSERT INTO risca_w_rimborso_sd_utilizzato "
			+ "(id_stato_debitorio, id_elabora, id_rimborso, imp_utilizzato) "
			+ "VALUES(:idStatoDebitorio, :idElabora, :idRimborso, :impUtilizzato) ";
	
	private static final String QUERY_SELECT_RIMBORSO_SD_UTILIZZATO_BY_ID_SD = "select * from risca_r_rimborso_sd_utilizzato "
			+ " WHERE id_stato_debitorio = :idStatoDebitorio ";

	private static final String QUERY_INSERT_R_RIMBORSO ="INSERT INTO risca_r_rimborso_sd_utilizzato"
			+ " (id_stato_debitorio, id_rimborso, imp_utilizzato, gest_data_ins,"
			+ " gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid)"
			+ "VALUES(:idStatoDebitorio, :idRimborso, :impUtilizzato, :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid) ";

	private static final String QUERY_INSERT_RIMBORSO_SD_UTILIZZATO_FROM_WORKING = " insert into RISCA_R_RIMBORSO_SD_UTILIZZATO "
			+ " ( id_stato_debitorio, id_rimborso, imp_utilizzato, gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ " select id_stato_debitorio, id_rimborso, imp_utilizzato, :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid "
			+ " from RISCA_W_RIMBORSO_SD_UTILIZZATO "
			+ " where id_stato_debitorio = :idStatoDebitorio "
			+ " and id_elabora = :idElabora ";
	
	private static final String QUERY_DELETE_RIMBORSO_SD_UTILIZZATO_WORKING_BY_SD = " delete from RISCA_W_RIMBORSO_SD_UTILIZZATO where id_stato_debitorio = :idStatoDebitorio and id_elabora = :idElabora ";
	
	private static final String QUERY_SELECT_RIMBORSO_SD_UTILIZZATO_BY_ID_RIMBORSO = "select * from risca_r_rimborso_sd_utilizzato "
			+ " WHERE id_rimborso in (:idRimborsi) ";
	
	@Override
	public RimborsoSdUtilizzatoDTO saveRimborsoSdUtilizzatoWorking(RimborsoSdUtilizzatoDTO dto, Long idElabora) throws DAOException {
		LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::saveRimborsoSdUtilizzatoWorking] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
			map.put("idRimborso", dto.getIdRimborso());
			map.put("impUtilizzato", dto.getImpUtilizzato());
			map.put("idElabora", idElabora);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[RimborsoSdUtilizzatoDAOImpl::saveRimborsoSdUtilizzatoWorking] ERROR : "	 ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		
				throw new DAOException(Constants.ERRORE_GENERICO);

		} finally {
			LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::saveRimborsoSdUtilizzatoWorking] END");
		}

		return dto;
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
	public RowMapper<RimborsoSdUtilizzatoDTO> getRowMapper() throws SQLException {
		return new RimborsoSdUtilizzatoRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<RimborsoSdUtilizzatoDTO> getExtendedRowMapper() throws SQLException {
		return new RimborsoSdUtilizzatoRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class RimborsoSdUtilizzatoRowMapper implements RowMapper<RimborsoSdUtilizzatoDTO> {

		/**
		 * Instantiates a new Elabora row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RimborsoSdUtilizzatoRowMapper() throws SQLException {
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
		public RimborsoSdUtilizzatoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RimborsoSdUtilizzatoDTO bean = new RimborsoSdUtilizzatoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RimborsoSdUtilizzatoDTO bean) throws SQLException {
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setIdRimborso(rs.getLong("id_rimborso"));
			bean.setImpUtilizzato(rs.getBigDecimal("imp_utilizzato"));
		}
	}

	@Override
	public RimborsoSdUtilizzatoDTO getRimborsoSdUtilizzatoByIdStatoDebitorio(Long id) throws DAOException {
		LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::getRimborsoSdUtilizzatoByIdStatoDebitorio] BEGIN");
        RimborsoSdUtilizzatoDTO res = new RimborsoSdUtilizzatoDTO();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", id);
			MapSqlParameterSource params = getParameterValue(map);

			res = template.queryForObject(getQuery(QUERY_SELECT_RIMBORSO_SD_UTILIZZATO_BY_ID_SD, null, null),
					params, getRowMapper());;

		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::getRimborsoSdUtilizzatoByIdStatoDebitorio]Data not found for idStatoDebitorio: "+  id);
			return null;
		} catch (Exception e) {
			LOGGER.error("[RimborsoSdUtilizzatoDAOImpl::getRimborsoSdUtilizzatoByIdStatoDebitorio] ERROR : ",e);

			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::getRimborsoSdUtilizzatoByIdStatoDebitorio] END");
		}

		return res;
	}

	@Override
	public RimborsoSdUtilizzatoDTO saveRimborsoSdUtilizzato(RimborsoSdUtilizzatoDTO dto) throws DAOException {
		LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::saveRimborsoSdUtilizzato] BEGIN");
		try {
			
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
			map.put("idRimborso", dto.getIdRimborso());
			map.put("impUtilizzato", dto.getImpUtilizzato());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_R_RIMBORSO, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[RimborsoSdUtilizzatoDAOImpl::saveRimborsoSdUtilizzato] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return null;

		} finally {
			LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::saveRimborsoSdUtilizzato] END");
		}

		return dto;
	}
	
	@Override
	public Integer insertRimborsoSdUtilizzatoFromWorking(Long idStatoDebitorio, String attore, Long idElabora)
			throws DAOException {
		LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::insertRimborsoSdUtilizzatoFromWorkin] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("gestAttoreIns", attore);
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);
			map.put("idElabora", idElabora);
			
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_INSERT_RIMBORSO_SD_UTILIZZATO_FROM_WORKING;
			return template.update(getQuery(query, null, null), params);

		} catch (Exception e) {
			LOGGER.error("[RimborsoSdUtilizzatoDAOImpl::insertRimborsoSdUtilizzatoFromWorkin] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::insertRimborsoSdUtilizzatoFromWorkin] END");
		}
	}
	
	@Override
	public Integer deleteRimborsoSdUtilizzatoWorkingByIdStatoDebitorio(Long idStatoDebitorio, Long idElabora) throws DAOException {
		LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::deleteRimborsoSdUtilizzatoWorkingByIdStatoDebitorio] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			map.put("idElabora", idElabora);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_RIMBORSO_SD_UTILIZZATO_WORKING_BY_SD, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[RimborsoSdUtilizzatoDAOImpl::deleteRimborsoSdUtilizzatoWorkingByIdStatoDebitorio] ERROR : ",e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RimborsoSdUtilizzatoDAOImpl::deleteRimborsoSdUtilizzatoWorkingByIdStatoDebitorio] END");
		}

		return res;
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<RimborsoSdUtilizzatoDTO> getRimborsoSdUtilizzatoByIdRimborsi(List<Long> idRimborsi) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<RimborsoSdUtilizzatoDTO> res = new ArrayList<>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRimborsi", idRimborsi);
			MapSqlParameterSource params = getParameterValue(map);

			res = template.query(getQuery(QUERY_SELECT_RIMBORSO_SD_UTILIZZATO_BY_ID_RIMBORSO, null, null),
					params, getRowMapper());;

		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug(getClassFunctionDebugString(className, methodName," Data not found for idRimborso: "+  Utils.formatArraytoStringWithSeparator(",", idRimborsi.toArray(new String[0]))));
			return Collections.emptyList();
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

		return res;
	}

}