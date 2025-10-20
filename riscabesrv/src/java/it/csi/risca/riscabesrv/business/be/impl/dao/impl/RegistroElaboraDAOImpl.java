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

import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class RegistroElaboraDAOImpl extends RiscaBeSrvGenericDAO<RegistroElaboraDTO> implements RegistroElaboraDAO {

	private static final String QUERY_INSERT_REGISTRO_ELABORA = "INSERT INTO risca_r_registro_elabora(id_registro_elabora,  "
			+ "id_elabora, id_passo_elabora, flg_esito_elabora, nota_elabora, gest_attore_ins,  "
			+ "gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid, id_fase_elabora)  "
			+ "SELECT :idRegistroElabora, :idElabora, id_passo_elabora, :flgEsitoElabora,  "
			+ ":notaElabora, :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid, id_fase_elabora  "
			+ "FROM risca_d_passo_elabora, risca_d_fase_elabora " + "WHERE cod_passo_elabora = :codPassoElabora "
			+ "and cod_fase_elabora= :codFaseElabora ";

	public static final String FLAG_ESITO_ELABORA = "rrre.flg_esito_elabora = :esito";

	public static final String ESITO_ELABORA = "rrre.id_fase_elabora = (select id_fase_elabora from risca_d_fase_elabora where cod_fase_elabora = :codFaseElabora) ";

	public static final String QUERY_SELECT_REGISTRO_ELABORA_BY_ELABORA_AND_AMBITO = "SELECT rte.data_richiesta, rdte.des_tipo_elabora, rdse.des_stato_elabora, rrre.*, rdpe.cod_passo_elabora, rdpe.des_passo_elabora "
			+ "FROM risca_t_elabora rte, risca_r_registro_elabora rrre, "
			+ "risca_d_tipo_elabora rdte, risca_d_stato_elabora rdse, risca_d_passo_elabora rdpe "
			+ "where rte.id_elabora  = rrre.id_elabora " + "and rte.id_tipo_elabora = rdte.id_tipo_elabora "
			+ "and rte.id_stato_elabora = rdse.id_stato_elabora " + "and rrre.id_passo_elabora = rdpe.id_passo_elabora "
			+ "and rte.id_elabora = :idElabora " + "and rte.id_ambito = :idAmbito ";
	
	public static final String ORDER_BY = " order by rrre.id_registro_elabora ";

	@Override
	public RegistroElaboraDTO saveRegistroElabora(RegistroElaboraDTO dto) throws Exception {
		LOGGER.debug("[RegistroElaboraDAOImpl::saveRegistroElabora] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long idGen = findNextSequenceValue("seq_risca_r_registro_elabora");
			map.put("idRegistroElabora", idGen);
			map.put("idElabora", dto.getIdElabora());
			map.put("codPassoElabora", dto.getCodPassoElabora());
			map.put("flgEsitoElabora", dto.getFlgEsitoElabora());
			map.put("notaElabora", dto.getNotaElabora());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			map.put("codFaseElabora", dto.getCodFaseElabora());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_REGISTRO_ELABORA, null, null), params, keyHolder);
			
			dto.setIdRegistroElabora(idGen);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
		} catch (Exception e) {
			LOGGER.error("[RegistroElaboraDAOImpl::saveRegistroElabora] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);

		} finally {
			LOGGER.debug("[RegistroElaboraDAOImpl::saveRegistroElabora] END");
		}

		return dto;
	}

	/**
	 * {@inheritDoc}
	 * @throws Exception 
	 */
	@Override
	public List<RegistroElaboraExtendedDTO> loadRegistroElaboraByElaboraAndAmbito(String idElabora, String idAmbito,
			Integer esito, String codFaseElabora) throws Exception {
		LOGGER.debug("[RegistroElaboraDAOImpl::loadRegistroElaboraByElaboraAndAmbito] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", Long.parseLong(idElabora));
			map.put("idAmbito", Long.parseLong(idAmbito));
			map.put("esito", esito);
			if (codFaseElabora != null) {
				map.put("codFaseElabora", codFaseElabora);
			}
			MapSqlParameterSource params = getParameterValue(map);

			String query = QUERY_SELECT_REGISTRO_ELABORA_BY_ELABORA_AND_AMBITO;
			if (esito != null) {
				query = query + " and " + FLAG_ESITO_ELABORA;
			}
			if (codFaseElabora != null) {
				query = query + " and " + ESITO_ELABORA;
			}
			query += ORDER_BY;

			return template.query(getQuery(query, null, null), params, getExtendedRowMapper());

		} catch (SQLException e) {
			LOGGER.error(
					"[RegistroElaboraDAOImpl::loadRegistroElaboraByElaboraAndAmbito] Errore nell'esecuzione della query",
					e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (DataAccessException e) {
			LOGGER.error("[RegistroElaboraDAOImpl::loadRegistroElaboraByElaboraAndAmbito] Errore nell'accesso ai dati",
					e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RegistroElaboraDAOImpl::loadRegistroElaboraByElaboraAndAmbito] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<RegistroElaboraDTO> getRowMapper() throws SQLException {

		return new RegistroElaboraRowMapper();
	}

	@Override
	public RowMapper<RegistroElaboraExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new RegistroElaboraExtendedRowMapper();
	}

	public static class RegistroElaboraRowMapper implements RowMapper<RegistroElaboraDTO> {

		public RegistroElaboraRowMapper() throws SQLException {
			// Instantiate class
		}

		@Override
		public RegistroElaboraDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			RegistroElaboraDTO bean = new RegistroElaboraDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RegistroElaboraDTO bean) throws SQLException {
			bean.setIdRegistroElabora(rs.getLong("id_registro_elabora"));
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setCodPassoElabora(rs.getString("cod_passo_elabora"));
			bean.setFlgEsitoElabora(rs.getInt("flg_esito_elabora"));
			bean.setNotaElabora(rs.getString("nota_elabora"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
		}

	}

	public static class RegistroElaboraExtendedRowMapper implements RowMapper<RegistroElaboraExtendedDTO> {

		public RegistroElaboraExtendedRowMapper() throws SQLException {
			// Instantiate class
		}

		@Override
		public RegistroElaboraExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			RegistroElaboraExtendedDTO bean = new RegistroElaboraExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RegistroElaboraExtendedDTO bean) throws SQLException {
			bean.setIdRegistroElabora(rs.getLong("id_registro_elabora"));
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setCodPassoElabora(rs.getString("cod_passo_elabora"));
			bean.setFlgEsitoElabora(rs.getInt("flg_esito_elabora"));
			bean.setNotaElabora(rs.getString("nota_elabora"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			// extended fields
			bean.setDataRichiesta(rs.getDate("data_richiesta"));
			bean.setDesTipoElabora(rs.getString("des_tipo_elabora"));
			bean.setDesStatoElabora(rs.getString("des_stato_elabora"));
			bean.setDesPassoElabora(rs.getString("des_passo_elabora"));
		}

	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}
