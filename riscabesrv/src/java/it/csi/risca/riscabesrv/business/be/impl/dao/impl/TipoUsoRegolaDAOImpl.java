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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoRegolaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.RiduzioneAumentoDAOImpl.TipoUsoExtendedRowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.UnitaMisuraDAOImpl.UnitaMisuraRowMapper;
import it.csi.risca.riscabesrv.dto.JsonRangeDTO;
import it.csi.risca.riscabesrv.dto.JsonRegolaDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaExtendedDTO;
import it.csi.risca.riscabesrv.dto.UnitaMisuraDTO;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type Tipo uso dao. 
 * 
 * @author CSI PIEMONTE
 */ 
public class TipoUsoRegolaDAOImpl extends RiscaBeSrvGenericDAO<TipoUsoRegolaDTO> implements TipoUsoRegolaDAO {

	private final String className = this.getClass().getSimpleName();

	private static final String QUERY_TIPO_USO_REGOLA_BY_COD_TIPOUSO = "select a.* "
			+ "from RISCA_R_TIPO_USO_REGOLA a, RISCA_D_TIPO_USO b " + "where a.id_tipo_uso = b. id_tipo_uso "
			+ "     and b.cod_tipo_uso = :codTipoUso " + "     and a.data_inizio <= :dataRiferimento"
			+ "     and (a.data_fine is null OR a.data_fine > :dataRiferimento)";

	private static final String QUERY_LOAD_ANNI_FROM_DATA_INIZIO = "select distinct(date_part('year',data_inizio)) as ANNO "
			+ "from risca_r_tipo_uso_regola rrtur "
			+ "join risca_d_tipo_uso rdtu on rrtur.id_tipo_uso = rdtu.id_tipo_uso "
			+ "where rdtu.id_ambito = :idAmbito " + "order by ANNO desc";

	private static final String QUERY_TIPO_USO_REGOLA_BY_AMBITO_ANNO = "SELECT *  "
			+ "FROM risca_r_tipo_uso_regola rrtur "
			+ "JOIN risca_d_tipo_uso rdtu ON rrtur.id_tipo_uso = rdtu.id_tipo_uso  "
			+ "join risca_d_ambito rda ON rdtu.id_ambito  = rda.id_ambito "
			+ "join risca_d_unita_misura  rdum ON rdtu.id_unita_misura  = rdum.id_unita_misura "
			+ "WHERE rdtu.id_ambito =  :idAmbito " + "AND EXTRACT(YEAR FROM rrtur.data_inizio) = :anno";

	private static final String QUERY_INSERT_TIPO_USO_REGOLA = "INSERT INTO risca_r_tipo_uso_regola "
			+ "(id_tipo_uso_regola, id_tipo_uso, data_inizio, data_fine, json_regola,"
			+ " id_algoritmo, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(:idTipoUsoRegola, :idTipoUso, :dataInizio, :dataFine, to_jsonb(:jsonRegola::json), :idAlgoritmo,:gestDataIns,"
			+ " :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";

	private static final String QUERY_UPDATE_TIPO_USO_REGOLA = "UPDATE risca_r_tipo_uso_regola SET 	"
			+ " id_tipo_uso=:idTipoUso," + "	data_inizio=:dataInizio," + "	data_fine=:dataFine,"
			+ "	json_regola= to_jsonb(:jsonRegola::json)," + "	id_algoritmo=:idAlgoritmo,"
			+ "	gest_data_upd=:gestDataUpd,	gest_attore_upd=:gestAttoreUpd"
			+ "	WHERE id_tipo_uso_regola= :idTipoUsoRegola ";

	private static final String QUERY_UPDATE_DATA_FINE_TIPO_USO_REGOLA = "UPDATE risca_r_tipo_uso_regola SET 	"
			+ "	data_fine = :dataFine, gest_data_upd=:gestDataUpd,	gest_attore_upd=:gestAttoreUpd WHERE id_tipo_uso_regola in ( :listIdTipoUsoRegola) ";

	private static final String QUERY_TIPO_USO_REGOLA_BY_AMBITO_AND_DT_FINE = "SELECT *  "
			+ "FROM risca_r_tipo_uso_regola rrtur "
			+ "JOIN risca_d_tipo_uso rdtu ON rrtur.id_tipo_uso = rdtu.id_tipo_uso  "
			+ "join risca_d_ambito rda ON rdtu.id_ambito  = rda.id_ambito "
			+ "join risca_d_unita_misura  rdum ON rdtu.id_unita_misura  = rdum.id_unita_misura "
			+ "WHERE rdtu.id_ambito =  :idAmbito " + "AND  rrtur.data_fine is null ";

	private static final String QUERY_LOAD_TIPO_USO_REGOLA_BY_ID = "select * "
			+ "from risca_r_tipo_uso_regola rrtur "
			+ "join risca_d_tipo_uso rdtu on rrtur.id_tipo_uso = rdtu.id_tipo_uso "
			+ "join risca_d_ambito rda ON rdtu.id_ambito  = rda.id_ambito "
			+ "join risca_d_unita_misura  rdum ON rdtu.id_unita_misura  = rdum.id_unita_misura "
			+ "where rrtur.id_tipo_uso_regola = :idTipoUsoRegola ";
	
	private static final String QUERY_COUNT_TIPO_USO_REGOLA_BY_AMBITO_MAX_ANNO ="SELECT count(*)"
			+ "	FROM risca_r_tipo_uso_regola rrtur"
			+ "	JOIN risca_d_tipo_uso rdtu ON rrtur.id_tipo_uso = rdtu.id_tipo_uso"
			+ "	JOIN risca_d_ambito rda ON rdtu.id_ambito = rda.id_ambito"
			+ "	WHERE rdtu.id_ambito = :idAmbito"
			+ "	AND EXTRACT(YEAR FROM rrtur.data_inizio) >= :anno"
			+ "	AND EXTRACT(YEAR FROM rrtur.data_inizio) = ("
			+ "	    SELECT MAX(EXTRACT(YEAR FROM data_inizio))"
			+ "	    FROM risca_r_tipo_uso_regola"
			+ "	)";
	
	
	public TipoUsoRegolaDTO loadTipoUsoRegolaByCodTipoUso(String dataRiferimento, String codTipoUso) {
		LOGGER.debug("[DatoTecnicoDAOImpl::loadTipoUsoRegolaByCodTipoUso] BEGIN");
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dataRif = null;
			try {
				dataRif = formatter.parse(dataRiferimento);
			} catch (ParseException e) {
				LOGGER.error("[TipoUsoRegolaDAOImpl::loadTipoUsoRegolaByCodTipoUso] Errore nel parse data riferimento",
						e);
				return null;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("dataRiferimento", dataRif);
			map.put("codTipoUso", codTipoUso);
			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(QUERY_TIPO_USO_REGOLA_BY_COD_TIPOUSO, params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[TipoUsoRegolaDAOImpl::loadTipoUsoRegolaByCodTipoUso] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[TipoUsoRegolaDAOImpl::loadTipoUsoRegolaByCodTipoUso] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[TipoUsoRegolaDAOImpl::loadTipoUsoRegolaByCodTipoUso] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<TipoUsoRegolaDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new TipoUsoRegolaRowMapper();
	}

	@Override
	public RowMapper<TipoUsoRegolaExtendedDTO> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new TipoUsoRegolaExtendedRowMapper();
	}

	public static class TipoUsoRegolaExtendedRowMapper implements RowMapper<TipoUsoRegolaExtendedDTO> {

		/**
		 * Instantiates a new tipo uso regola Extended row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public TipoUsoRegolaExtendedRowMapper() throws SQLException {
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
		public TipoUsoRegolaExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TipoUsoRegolaExtendedDTO bean = new TipoUsoRegolaExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, TipoUsoRegolaExtendedDTO bean) throws SQLException {
			bean.setIdTipoUsoRegola(rs.getLong("id_tipo_uso_regola"));
			bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
			bean.setDataInizio(rs.getString("data_inizio"));
			bean.setDataFine(rs.getString("data_fine"));
			bean.setJsonRegola(rs.getString("json_regola"));
			bean.setIdAlgoritmo(rs.getLong("id_algoritmo"));
			TipoUsoExtendedDTO tipoUso = populateBeanTipoUso(rs);
			UnitaMisuraDTO unitaMisura = populateBeanUnitaMisura(rs);
			tipoUso.setUnitaMisura(unitaMisura);
			bean.setTipoUso(tipoUso);
		}

		private UnitaMisuraDTO populateBeanUnitaMisura(ResultSet rs) throws SQLException {
			UnitaMisuraRowMapper unitaMisuraRowMapper = new UnitaMisuraRowMapper();
			return unitaMisuraRowMapper.mapRow(rs, 0);
		}

		private TipoUsoExtendedDTO populateBeanTipoUso(ResultSet rs) throws SQLException {
			TipoUsoExtendedRowMapper tipoUsoRowMapper = new TipoUsoExtendedRowMapper();
			return tipoUsoRowMapper.mapRow(rs, 0);
		}
	}

	public static class TipoUsoRegolaRowMapper implements RowMapper<TipoUsoRegolaDTO> {

		/**
		 * Instantiates a new tipo uso regola row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public TipoUsoRegolaRowMapper() throws SQLException {
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
		public TipoUsoRegolaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TipoUsoRegolaDTO bean = new TipoUsoRegolaDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, TipoUsoRegolaDTO bean) throws SQLException {
			bean.setIdTipoUsoRegola(rs.getLong("id_tipo_uso_regola"));
			bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
			bean.setDataInizio(rs.getString("data_inizio"));
			bean.setDataFine(rs.getString("data_fine"));
			bean.setJsonRegola(rs.getString("json_regola"));
			bean.setIdAlgoritmo(rs.getLong("id_algoritmo"));
		}
	}

	@Override
	public List<Integer> loadAllAnniFromDTInizio(Integer idAmbito) throws Exception, BusinessException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(QUERY_LOAD_ANNI_FROM_DATA_INIZIO, params, new IntegerListRowMapper());
		} catch (DataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	public class IntegerListRowMapper implements RowMapper<Integer> {
		@Override
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getInt("ANNO");
		}
	}

	@Override
	public List<TipoUsoRegolaExtendedDTO> loadAllUsoRegolaByIdAmbitoAndAnno(Integer idAmbito, Integer anno,
			Integer offset, Integer limit, String sort) throws Exception, BusinessException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			map.put("anno", anno);
			MapSqlParameterSource params = getParameterValue(map);
			String dynamicOrderByCondition = mapSortConCampiDB(sort);
			return template.query(
					getQuery(QUERY_TIPO_USO_REGOLA_BY_AMBITO_ANNO + dynamicOrderByCondition,
							offset != null ? offset.toString() : null, limit != null ? limit.toString() : null),
					params, getExtendedRowMapper());
		} catch (DataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	private String mapSortConCampiDB(String sort) {
		return " Order By rdtu.des_tipo_uso ASC ";
	}

	@Override
	public Integer countAllUsoRegolaByIdAmbitoAndAnno(Integer idAmbito, Integer anno) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			map.put("anno", anno);
			MapSqlParameterSource params = getParameterValue(map);
			String query = "SELECT COUNT(*) FROM ( " + QUERY_TIPO_USO_REGOLA_BY_AMBITO_ANNO + " ) as tot_uso_regola ";
			return template.queryForObject(query, params, Integer.class);

		} catch (DataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public TipoUsoRegolaExtendedDTO updateTipoUsoRegola(TipoUsoRegolaExtendedDTO tipoUsoRegola) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		Map<String, Object> map = new HashMap<>();
		try {

			map.put("idTipoUsoRegola", tipoUsoRegola.getIdTipoUsoRegola());
			map.put("idTipoUso", tipoUsoRegola.getTipoUso() != null ? tipoUsoRegola.getTipoUso().getIdTipoUso()
					: tipoUsoRegola.getIdTipoUso());
			if (tipoUsoRegola.getDataInizio() != null && !tipoUsoRegola.getDataInizio().equals("")) {
				Date dataInizio = formatter.parse(tipoUsoRegola.getDataInizio());
				map.put("dataInizio", dataInizio);
			} else {
				map.put("dataInizio", null);
			}
			if (tipoUsoRegola.getDataFine() != null && !tipoUsoRegola.getDataFine().equals("")) { 
				Date dataFine = formatter.parse(tipoUsoRegola.getDataFine());
				map.put("dataFine", dataFine);
			} else {
				map.put("dataFine", null);
			}
			if (tipoUsoRegola.getJsonRegolaDTO() != null) {
				JsonRegolaDTO jsonRegolaDto = tipoUsoRegola.getJsonRegolaDTO();
				List<JsonRangeDTO> jsonRange = new ArrayList<>();
				if (Utils.isNotEmpty(jsonRegolaDto.getJsonRanges())) {
//					Iterator<JsonRangeDTO> iterator = jsonRegolaDto.getJsonRanges().iterator();
//					while (iterator.hasNext()) {
//					    JsonRangeDTO jsonRangeDTO = iterator.next();
//					    if (jsonRangeDTO.getSogliaMax() != null && jsonRangeDTO.getSogliaMax().compareTo(BigDecimal.ZERO) == 0
//					            && jsonRangeDTO.getSogliaMin() != null && jsonRangeDTO.getSogliaMin().compareTo(BigDecimal.ZERO) == 0) {
//					        jsonRegolaDto.setMinimoPrincipale(jsonRangeDTO.getCanoneMinimoRange());
//					        // Usa iterator.remove() per rimuovere l'elemento dalla lista
//					        iterator.remove();
//					    }
//					}

					
					jsonRange = jsonRegolaDto.getJsonRanges().stream()
							.sorted(Comparator.comparing(JsonRangeDTO::getSogliaMin)) // crescente
							.collect(Collectors.toList());
					jsonRegolaDto.setJsonRanges(jsonRange);
				}
			    // Rimuovi "ranges" se la lista e vuota
			    if (jsonRange.isEmpty()) {
			        jsonRegolaDto.setJsonRanges(null);
			    }
				ObjectMapper mapper = new ObjectMapper();
				mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
				ObjectNode jsonNode = mapper.convertValue(jsonRegolaDto, ObjectNode.class);

				String jsonRegola = jsonNode.toString();
				tipoUsoRegola.setJsonRegola(jsonRegola);
				map.put("jsonRegola", jsonRegola);

			} else {
				map.put("jsonRegola", null);
			}
			map.put("idAlgoritmo", tipoUsoRegola.getIdAlgoritmo());
			map.put("gestAttoreUpd", tipoUsoRegola.getGestAttoreUpd());
			map.put("gestDataUpd", now);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_UPDATE_TIPO_USO_REGOLA, null, null), params, keyHolder);
		} catch (DataAccessException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return tipoUsoRegola;
	}

	@Override
	public List<TipoUsoRegolaExtendedDTO> loadAllUsoRegolaByIdAmbitoAndDataFineIsNull(Integer idAmbito)
			throws Exception, BusinessException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(QUERY_TIPO_USO_REGOLA_BY_AMBITO_AND_DT_FINE, params, getExtendedRowMapper());
		} catch (DataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public void updateDataFineTipoUsoRegola(List<Long> listIdTipoUsoRegola, Integer anno, String cf) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Map<String, Object> map = new HashMap<>();
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();

			map.put("listIdTipoUsoRegola", listIdTipoUsoRegola);
			map.put("dataFine", Utils.convertiStringInData((anno-1)+"-12-31",Utils.DEFAULT_DATE_FORMAT));
			map.put("gestAttoreUpd", cf );
			map.put("gestDataUpd", now);

			MapSqlParameterSource params = getParameterValue(map);

			template.update(QUERY_UPDATE_DATA_FINE_TIPO_USO_REGOLA, params);
		} catch (DataAccessException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public TipoUsoRegolaExtendedDTO saveTipoUsoRegola(TipoUsoRegolaExtendedDTO tipoUsoRegola) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		Map<String, Object> map = new HashMap<>();
		try {
			Long genId = findNextSequenceValue("seq_risca_r_tipo_uso_regola");
			map.put("idTipoUsoRegola", genId);
			map.put("idTipoUso", tipoUsoRegola.getIdTipoUso());
			if (tipoUsoRegola.getDataInizio() != null) {
				Date dataInizio = formatter.parse(tipoUsoRegola.getDataInizio());
				map.put("dataInizio", dataInizio);
			} else {
				map.put("dataInizio", null);
			}
			map.put("dataFine", tipoUsoRegola.getDataFine());

			map.put("jsonRegola", tipoUsoRegola.getJsonRegola());

			map.put("idAlgoritmo", tipoUsoRegola.getIdAlgoritmo());
			map.put("gestAttoreIns", tipoUsoRegola.getGestAttoreUpd());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", tipoUsoRegola.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid",  generateGestUID(tipoUsoRegola.getGestAttoreIns() + tipoUsoRegola.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			template.update(getQuery(QUERY_INSERT_TIPO_USO_REGOLA, null, null), params, keyHolder);
			tipoUsoRegola.setIdTipoUsoRegola(genId);
		} catch (DataAccessException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return tipoUsoRegola;
	}

	@Override
	public TipoUsoRegolaExtendedDTO loadTipoUsoRegola(Long idTipoUsoRegola) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idTipoUsoRegola", idTipoUsoRegola);
			MapSqlParameterSource params = getParameterValue(map);

				return template.queryForObject(QUERY_LOAD_TIPO_USO_REGOLA_BY_ID, params,  getExtendedRowMapper());
		}catch (EmptyResultDataAccessException e) {
			LOGGER.info(getClassFunctionDebugString(className, methodName, e.getMessage()));
			 throw new Exception(e) ;
			
		} catch (DataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			 throw new Exception(e) ;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		} 
		
	}

	@Override
	public Boolean checkAnnoExistence(Integer idAmbito, Integer anno) throws Exception, BusinessException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			map.put("anno", anno);
			MapSqlParameterSource params = getParameterValue(map);
			int tot = template.queryForObject(QUERY_COUNT_TIPO_USO_REGOLA_BY_AMBITO_MAX_ANNO, params,
					Integer.class);
			if (tot > 0)
				return true;
			else
				return false;
		} catch (DataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		} 
	}

}
