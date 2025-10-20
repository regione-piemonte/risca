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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TassiDiInteresseDAO;
import it.csi.risca.riscabesrv.dto.TassiDiInteresseDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type TassiDiInteresse dao.
 *
 * @author CSI PIEMONTE
 */

public class TassiDiInteresseDAOImpl extends RiscaBeSrvGenericDAO<TassiDiInteresseDTO> implements TassiDiInteresseDAO {

	private final String className = this.getClass().getSimpleName();

	private static final String QUERY_TASSI_INTERESSE_GET = "SELECT id_ambito_interesse, id_ambito, tipo_interesse, "
			+ "data_inizio, data_fine, percentuale, giorni_legali " + "FROM risca.risca_r_ambito_interesse "
			+ "WHERE id_ambito = :idAmbito AND tipo_interesse = :tipoDiInteresse order by data_inizio desc";

	private static final String QUERY_TASSI_INTERESSE_POST = "INSERT INTO risca.risca_r_ambito_interesse "
			+ "(id_ambito_interesse, id_ambito, tipo_interesse, data_inizio, data_fine, percentuale, giorni_legali, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(:idAmbitoInteresse, :idAmbito, :tipoInteresse, :dataInizio, :dataFine, :percentuale, :giorniLegali, :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";

	private static final String QUERY_TASSI_INTERESSE_DELETE = "DELETE FROM risca.risca_r_ambito_interesse "
			+ "WHERE id_ambito_interesse= :idAmbitoInteresse";

	private static final String QUERY_TASSI_INTERESSE_UPDATE = "UPDATE risca_r_ambito_interesse\r\n"
			+ "SET id_ambito= :idAmbito, tipo_interesse= :tipoInteresse, data_inizio= :dataInizio, data_fine= :dataFine, "
			+ "percentuale= :percentuale, giorni_legali= :giorniLegali, "
			+ "gest_data_upd= :gestDataUpd, gest_attore_upd= :gestAttoreUpd, gest_uid= :gestUid\r\n"
			+ "WHERE id_ambito_interesse= :idAmbitoInteresse;\r\n";

	private static final String QUERY_TASSI_INTERESSE_GET_BY_ID = "SELECT id_ambito_interesse, id_ambito, tipo_interesse, data_inizio, data_fine, percentuale, giorni_legali, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid\r\n"
			+ "FROM risca_r_ambito_interesse\r\n" + "WHERE id_ambito_interesse= :idAmbitoInteresse";

	private static final String QUERY_TOTAL_COUNT_TASSI_INTERESSE = "SELECT COUNT(*) FROM risca.risca_r_ambito_interesse "
			+ "WHERE id_ambito = :idAmbito AND tipo_interesse = :tipoDiInteresse";

	private static final String QUERY_TASSO_DI_INTERESSE_DATA_INIZIO_RECENTE = "SELECT id_ambito_interesse, id_ambito, tipo_interesse, "
			+ "data_inizio, data_fine, percentuale, giorni_legali, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid "
			+ "FROM risca_r_ambito_interesse " + "WHERE id_ambito = :idAmbito AND tipo_interesse = :tipoDiInteresse "
			+ "ORDER BY data_inizio DESC " + "LIMIT 1";

	private static final String QUERY_UPDATE_DATA_FINE = "UPDATE risca_r_ambito_interesse SET data_fine = :dataFine WHERE id_ambito_interesse = :idAmbitoInteresse";

	@Override
	public List<TassiDiInteresseDTO> loadTassiDiInteresse(Long idAmbito, String tipoDiInteresse, Integer offset,
			Integer limit, String sort) {

		LOGGER.debug("[TassiDiInteresseDAOImpl::loadTassiDiInteresse] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			map.put("tipoDiInteresse", tipoDiInteresse);

			MapSqlParameterSource params = getParameterValue(map);

			return template.query(getQuery(QUERY_TASSI_INTERESSE_GET, offset != null ? offset.toString() : null,
					limit != null ? limit.toString() : null), params, getRowMapper());

		} catch (DataAccessException e) {
			LOGGER.error("[TassiDiInteresseDAOImpl::loadTassiDiInteresse] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[TassiDiInteresseDAOImpl::loadTassiDiInteresse] END");
		}
	}

	@Override
	public TassiDiInteresseDTO saveTassiDiInteresse(TassiDiInteresseDTO nuovoTassoDiInteresse, Long idAmbito)
			throws DAOException {
		// save del nuovo tasso di interesse
		LOGGER.debug("[TassiDiInteresseDAOImpl::saveTassiDiInteresse] BEGIN");
		try {
			Map<String, Object> tassoDiInteresseMappa = new HashMap<>();
			Calendar calendario = Calendar.getInstance();
			Date dateNow = calendario.getTime();

			Long genId = findNextSequenceValue("seq_risca_r_ambito_interesse");
			tassoDiInteresseMappa.put("idAmbitoInteresse", genId);
			tassoDiInteresseMappa.put("idAmbito", nuovoTassoDiInteresse.getIdAmbito());
			tassoDiInteresseMappa.put("tipoInteresse", nuovoTassoDiInteresse.getFlgTipoInteresse());
			tassoDiInteresseMappa.put("dataInizio", nuovoTassoDiInteresse.getDataInizio());
			tassoDiInteresseMappa.put("dataFine", getDataFine("2100-12-31"));
			tassoDiInteresseMappa.put("percentuale", nuovoTassoDiInteresse.getPercentuale());
			tassoDiInteresseMappa.put("giorniLegali", nuovoTassoDiInteresse.getGiorniLegali());
			tassoDiInteresseMappa.put("gestAttoreIns", nuovoTassoDiInteresse.getGestAttoreIns());
			tassoDiInteresseMappa.put("gestDataIns", dateNow);
			tassoDiInteresseMappa.put("gestAttoreUpd", nuovoTassoDiInteresse.getGestAttoreUpd());
			tassoDiInteresseMappa.put("gestDataUpd", dateNow);
			tassoDiInteresseMappa.put("gestUid", generateGestUID(
					nuovoTassoDiInteresse.getGestAttoreIns() + nuovoTassoDiInteresse.getGestAttoreUpd() + dateNow));

			MapSqlParameterSource params = getParameterValue(tassoDiInteresseMappa);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_TASSI_INTERESSE_POST;
			template.update(getQuery(query, null, null), params, keyHolder);

			nuovoTassoDiInteresse.setIdAmbitoInteresse(genId);
			nuovoTassoDiInteresse.setGestDataIns(dateNow);
			nuovoTassoDiInteresse.setGestDataUpd(dateNow);

			LOGGER.debug("[TassiDiInteresseDAOImpl::saveTassiDiInteresse] END");

			return nuovoTassoDiInteresse;
		} catch (Exception e) {
			LOGGER.error("[TassiDiInteresseDAOImpl::saveTassiDiInteresse] ERROR : ", e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[TassiDiInteresseDAOImpl::saveTassiDiInteresse] END");
		}
	}

	@Override
	public Long deleteTassiDiInteresse(Long idAmbitoInteresse, Long idAmbito, String tipoInteresse)
			throws DAOException {
		LOGGER.debug("[TassiDiInteresseDAOImpl::deleteTassiDiInteresse] BEGIN >>>>>>>>>>>>>>>" + idAmbitoInteresse);
		
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbitoInteresse", idAmbitoInteresse);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String queryTassiInteresse = QUERY_TASSI_INTERESSE_DELETE;

			template.update(getQuery(queryTassiInteresse, null, null), params, keyHolder);
			map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			map.put("tipoDiInteresse", tipoInteresse);
			params = getParameterValue(map);
			keyHolder = new GeneratedKeyHolder();
			TassiDiInteresseDTO tassoRecente = template.queryForObject(QUERY_TASSO_DI_INTERESSE_DATA_INIZIO_RECENTE,
					params, getRowMapper());

			//aggiorno la data fine del tasso di interesse eliminato
			map = new HashMap<>();
			map.put("idAmbitoInteresse", tassoRecente.getIdAmbitoInteresse());
			map.put("dataFine", getDataFine("2100-12-31"));
			params = getParameterValue(map);
			keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_UPDATE_DATA_FINE, null, null), params, keyHolder);

			LOGGER.debug("[TassiDiInteresseDAOImpl::deleteTassiDiInteresse] END");

			return idAmbitoInteresse;

		} catch (Exception e) {
			LOGGER.debug("[TassiDiInteresseDAOImpl::deleteTassiDiInteresse] ERROR");
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	@Transactional
	public TassiDiInteresseDTO updateTassiDiInteresse(TassiDiInteresseDTO tassoDiIntesseUpdate) throws Exception {
		// Verifico che le date del tasso che ho modificato siano ancora valide e non
		// siano state modificate
		try {
			LOGGER.debug("[TassiDiInteresseDAOImpl::updateTassiDiInteresse] BEGIN");
			Map<String, Object> mapTassoDiInteresse = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date dateNow = cal.getTime();

			mapTassoDiInteresse.put("idAmbitoInteresse", tassoDiIntesseUpdate.getIdAmbitoInteresse());
			mapTassoDiInteresse.put("idAmbito", tassoDiIntesseUpdate.getIdAmbito());
			mapTassoDiInteresse.put("tipoInteresse", tassoDiIntesseUpdate.getFlgTipoInteresse());
			mapTassoDiInteresse.put("dataInizio", tassoDiIntesseUpdate.getDataInizio());
			mapTassoDiInteresse.put("dataFine", tassoDiIntesseUpdate.getDataFine());
			mapTassoDiInteresse.put("percentuale", tassoDiIntesseUpdate.getPercentuale());
			mapTassoDiInteresse.put("giorniLegali", tassoDiIntesseUpdate.getGiorniLegali());
			mapTassoDiInteresse.put("gestAttoreUpd", tassoDiIntesseUpdate.getGestAttoreUpd());
			mapTassoDiInteresse.put("gestDataUpd", dateNow);
			mapTassoDiInteresse.put("gestUid", generateGestUID(
					tassoDiIntesseUpdate.getGestAttoreIns() + tassoDiIntesseUpdate.getGestAttoreUpd() + dateNow));

			MapSqlParameterSource params = getParameterValue(mapTassoDiInteresse);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_TASSI_INTERESSE_UPDATE, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[TassiDiInteresseDAOImpl::updateTassiDiInteresse] ERROR : " + e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		}
		return tassoDiIntesseUpdate;
	}

	@Override
	public TassiDiInteresseDTO loadTassoDiInteressePiuRecente(Long idAmbito, String tipoDiInteresse) {
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("tipoDiInteresse", tipoDiInteresse);
		paramsMap.put("idAmbito", idAmbito);
		MapSqlParameterSource params = getParameterValue(paramsMap);
		return template.queryForObject(QUERY_TASSO_DI_INTERESSE_DATA_INIZIO_RECENTE, params, getRowMapper());
	}

	/***
	 * METODI DI COMODO
	 * 
	 * @throws ParseException
	 */

	@Override
	public TassiDiInteresseDTO loadTassoDiInteresseByIdAmbitoInteresse(Long idAmbitoInteresse) {
		LOGGER.debug("[TassiDiInteresseDAOImpl::loadTassoDiInteresseByIdAmbitoInteresse] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbitoInteresse", idAmbitoInteresse);

			MapSqlParameterSource params = getParameterValue(map);
			TassiDiInteresseDTO tassoDiInteresse = template.queryForObject(QUERY_TASSI_INTERESSE_GET_BY_ID, params,
					getRowMapper());
			return tassoDiInteresse;
		} catch (DataAccessException e) {
			LOGGER.error(
					"[TassiDiInteresseDAOImpl::loadTassoDiInteresseByIdAmbitoInteresse] Errore nell'accesso ai dati",
					e);
			return null;
		}
	}

	@Override
	public RowMapper<TassiDiInteresseDTO> getRowMapper() {
		return new RowMapper<TassiDiInteresseDTO>() {
			@Override
			public TassiDiInteresseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				TassiDiInteresseDTO tassiDiInteresse = new TassiDiInteresseDTO();
				tassiDiInteresse.setIdAmbitoInteresse(rs.getLong("id_ambito_interesse"));
				tassiDiInteresse.setIdAmbito(rs.getLong("id_ambito"));
				tassiDiInteresse.setFlgTipoInteresse(rs.getString("tipo_interesse"));
				tassiDiInteresse.setDataInizio(rs.getDate("data_inizio"));
				tassiDiInteresse.setDataFine(rs.getDate("data_fine"));
				tassiDiInteresse.setPercentuale(rs.getBigDecimal("percentuale"));
				tassiDiInteresse.setGiorniLegali(rs.getInt("giorni_legali"));
				return tassiDiInteresse;
			}
		};
	}

	private Date getDataFine(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public String getTableNameAString() {
		return "risca.risca_r_ambito_interesse";
	}

	@Override
	public String getPrimaryKeySelect() {
		return "SELECT * FROM risca.risca_r_ambito_interesse WHERE id_ambito_interesse = :idAmbitoInteresse";
	}

	@Override
	public Integer countAllTassiDiInteresse(Long idAmbito, String tipoDiInteresse) {
		Map<String, Object> map = new HashMap<>();
		map.put("idAmbito", idAmbito);
		map.put("tipoDiInteresse", tipoDiInteresse);
		MapSqlParameterSource params = getParameterValue(map);

		return template.queryForObject(QUERY_TOTAL_COUNT_TASSI_INTERESSE, params, Integer.class);
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		// Implementazione del RowMapper esteso
		return null;
	}
}
