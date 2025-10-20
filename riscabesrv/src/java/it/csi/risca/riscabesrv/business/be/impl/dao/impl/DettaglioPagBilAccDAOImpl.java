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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagBilAccDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.DettaglioPagBilAccDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.SommaImpAssegnatoTipoUsoSdDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class DettaglioPagBilAccDAOImpl extends RiscaBeSrvGenericDAO<DettaglioPagBilAccDTO>
		implements DettaglioPagBilAccDAO {

	private final String className = this.getClass().getSimpleName();

	public static final String QUERY_INSERT = "INSERT INTO risca_r_dettaglio_pag_bil_acc "
			+ " (id_dettaglio_pag_bil_acc, id_dettaglio_pag, id_bil_acc, importo_accerta_bilancio, "
			+ " flg_eccedenza, flg_ruolo, flg_pubblico, "
			+ " gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ " VALUES(:idDettaglioPagBilAcc, :idDettaglioPag, :idBilAcc, :importoAccertaBilancio, "
			+ " :flgEccedenza, :flgRuolo, :flgPubblico, "
			+ " :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid) ";

	public static final String QUERY_DELETE_DETTAGLIO_PAG = " delete from risca_r_dettaglio_pag_bil_acc where id_dettaglio_pag = :idDettaglioPag ";
	public static final String QUERY_DELETE_DETTAGLIO_PAG_BY_ID_PAG = " delete from risca_r_dettaglio_pag_bil_acc dett_bil "
			+ "using risca_r_dettaglio_pag dett " + "where dett.id_dettaglio_pag = dett_bil.id_dettaglio_pag "
			+ "and dett.id_pagamento  = :idPagamento ";
	public static final String QUERY_DELETE_DETTAGLIO_PAG_BY_ID_RATA_SD = "delete from risca_r_dettaglio_pag_bil_acc where id_dettaglio_pag in "
			+ " ( SELECT dpb.id_dettaglio_pag FROM risca_r_dettaglio_pag_bil_acc dpb "
			+ " left join risca_r_dettaglio_pag rdp on dpb.id_dettaglio_pag = rdp.id_dettaglio_pag "
			+ " where rdp.id_rata_sd = :idRataSd ) ";

	public static final String QUERY_DELETE_DETTAGLIO_PAG_BY_LIST_ID_PAG = " delete from risca_r_dettaglio_pag_bil_acc dett_bil "
			+ "using risca_r_dettaglio_pag dett " + "where dett.id_dettaglio_pag = dett_bil.id_dettaglio_pag "
			+ "and dett.id_pagamento  in (:listIdPagamento) ";
	public static final String QUERY_DELETE_DETTAGLIO_PAG_BY_LIST_ID_RATA_SD = "delete from risca_r_dettaglio_pag_bil_acc where id_dettaglio_pag in "
			+ " ( SELECT dpb.id_dettaglio_pag FROM risca_r_dettaglio_pag_bil_acc dpb "
			+ " left join risca_r_dettaglio_pag rdp on dpb.id_dettaglio_pag = rdp.id_dettaglio_pag "
			+ " where rdp.id_rata_sd in (:listIdRataSd) ) ";

	public static final String QUERY_SELECT_DETTAGLIO_PAG_BY_ID_RATA_SD = " SELECT dpb.* "
			+ " FROM risca_r_dettaglio_pag_bil_acc dpb "
			+ " left join risca_r_dettaglio_pag rdp on dpb.id_dettaglio_pag = rdp.id_dettaglio_pag "
			+ " where rdp.id_rata_sd = :idRataSd ";

	public static final String QUERY_SELECT_SOMMA_IMP_ASSEGNATO_TIPO_USO = " SELECT bav.id_accerta_bilancio, bav.id_rata_sd, sum(importo_accerta_bilancio) as imp_assegnato "
			+ " from (SELECT dpb.*, rdp.id_rata_sd, tba.id_bil_acc, tba.id_accerta_bilancio "
			+ "           FROM risca_r_dettaglio_pag_bil_acc dpb "
			+ "      left join risca_r_dettaglio_pag rdp on dpb.id_dettaglio_pag = rdp.id_dettaglio_pag "
			+ "      left join risca_t_bil_acc tba on dpb.id_bil_acc = tba.id_bil_acc "
			+ "          where rdp.id_rata_sd = :idRataSd     "
			+ "            and tba.id_accerta_bilancio = :idAccertaBilancio) bav "
			+ " group by bav.id_accerta_bilancio, bav.id_rata_sd";

	@Override
	public DettaglioPagBilAccDTO saveDettaglioPagBilAcc(DettaglioPagBilAccDTO dto) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		try {
			Map<String, Object> map = new HashMap<>();
			Long genId = findNextSequenceValue("seq_risca_r_dettaglio_pag_bil_acc");
			map.put("idDettaglioPagBilAcc", genId);
			map.put("idDettaglioPag", dto.getIdDettaglioPag());
			map.put("idBilAcc", dto.getIdBilAcc());
			map.put("importoAccertaBilancio", dto.getImportoAccertaBilancio());
			map.put("flgEccedenza", dto.getFlgEccedenza());
			map.put("flgRuolo", dto.getFlgRuolo());
			map.put("flgPubblico", dto.getFlgPubblico());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_INSERT;

			template.update(getQuery(query, null, null), params, keyHolder);

			dto.setIdDettaglioPag(genId);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			return dto;

		} catch (SQLException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

	}

	@Override
	public Integer deleteDettaglioPagBilAccByIdDettaglioPag(Long idDettaglioPag) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idDettaglioPag", idDettaglioPag);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_DELETE_DETTAGLIO_PAG, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public Integer deleteDettaglioPagBilAccByIdPagamento(Long idPagamento) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idPagamento", idPagamento);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_DELETE_DETTAGLIO_PAG_BY_ID_PAG, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public Integer deleteDettaglioPagBilAccByListaIdPagamento(List<Long> listIdPagamento) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("listIdPagamento", listIdPagamento);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_DELETE_DETTAGLIO_PAG_BY_LIST_ID_PAG, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			return null;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public Integer deleteDettaglioPagBilAccByIdRataSd(Long idRataSd) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRataSd", idRataSd);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_DELETE_DETTAGLIO_PAG_BY_ID_RATA_SD, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public Integer deleteDettaglioPagBilAccByListaIdRataSd(List<Long> listIdRataSd) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("listIdRataSd", listIdRataSd);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_DELETE_DETTAGLIO_PAG_BY_LIST_ID_RATA_SD, null, null), params,
					keyHolder);

		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			return null;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public List<DettaglioPagBilAccDTO> getDettaglioPagBilAccByIdRataSd(Long idRataSd) throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<DettaglioPagBilAccDTO> listDettPagBilAcc = new ArrayList<DettaglioPagBilAccDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRataSd", idRataSd);
			MapSqlParameterSource params = getParameterValue(map);

			listDettPagBilAcc = template.query(getQuery(QUERY_SELECT_DETTAGLIO_PAG_BY_ID_RATA_SD, null, null), params,
					getRowMapper());

		} catch (SQLException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new DAOException(e);
		} catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new DAOException(e);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return listDettPagBilAcc;
	}

	@Override
	public SommaImpAssegnatoTipoUsoSdDTO getSommaImpAssegnatoTipoUso(Long idRataSd, Long idAccertaBilancio)
			throws DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		SommaImpAssegnatoTipoUsoSdDTO sommaImpAss = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRataSd", idRataSd);
			map.put("idAccertaBilancio", idAccertaBilancio);
			MapSqlParameterSource params = getParameterValue(map);

			sommaImpAss = template.queryForObject(getQuery(QUERY_SELECT_SOMMA_IMP_ASSEGNATO_TIPO_USO, null, null),
					params, new SommaImpAssegnatoTipoUsoRowMapper());

		} catch (EmptyResultDataAccessException e) {
			// non e' propriamente un errore, significa che per quella rata non sono gia'
			// state inserite righe di bilancio
			LOGGER.debug("[DettaglioPagBilAccDAOImpl::getSommaImpAssegnatoTipoUso] record non torvati per idRataSd: "
					+ idRataSd + " e idAccertaBilancio: " + idAccertaBilancio);
		} catch (SQLException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new DAOException(e);
		} catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new DAOException(e);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return sommaImpAss;
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<DettaglioPagBilAccDTO> getRowMapper() throws SQLException {
		return new DettaglioPagBilAccRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return new DettaglioPagBilAccRowMapper();
	}

	public static class SommaImpAssegnatoTipoUsoRowMapper implements RowMapper<SommaImpAssegnatoTipoUsoSdDTO> {

		public SommaImpAssegnatoTipoUsoRowMapper() throws SQLException {
		}

		@Override
		public SommaImpAssegnatoTipoUsoSdDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			SommaImpAssegnatoTipoUsoSdDTO bean = new SommaImpAssegnatoTipoUsoSdDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, SommaImpAssegnatoTipoUsoSdDTO bean) throws SQLException {
			bean.setIdRataSd(rs.getLong("id_rata_sd"));
			bean.setIdAccertaBilancio(rs.getLong("id_accerta_bilancio"));
			bean.setImpAssegnato(rs.getBigDecimal("imp_assegnato"));
		}
	}

	public static class DettaglioPagBilAccRowMapper implements RowMapper<DettaglioPagBilAccDTO> {

		public DettaglioPagBilAccRowMapper() throws SQLException {
		}

		@Override
		public DettaglioPagBilAccDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			DettaglioPagBilAccDTO bean = new DettaglioPagBilAccDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, DettaglioPagBilAccDTO bean) throws SQLException {
			bean.setIdDettaglioPagBilAcc(rs.getLong("id_dettaglio_pag_bil_acc"));
			bean.setIdDettaglioPag(rs.getLong("id_dettaglio_pag"));
			bean.setIdBilAcc(rs.getLong("id_bil_acc"));
			bean.setImportoAccertaBilancio(rs.getBigDecimal("importo_accerta_bilancio"));
			bean.setFlgEccedenza(rs.getInt("flg_eccedenza"));
			bean.setFlgRuolo(rs.getInt("flg_ruolo"));
			bean.setFlgPubblico(rs.getInt("flg_pubblico"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}
	}
}
