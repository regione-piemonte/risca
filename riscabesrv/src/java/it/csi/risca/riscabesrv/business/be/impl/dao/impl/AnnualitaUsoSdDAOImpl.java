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
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AnnualitaUsoSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.UsoRidaumSdDAO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdDTO;
import it.csi.risca.riscabesrv.dto.AnnualitaUsoSdExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.UsoRidaumSdExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class AnnualitaUsoSdDAOImpl extends RiscaBeSrvGenericDAO<AnnualitaUsoSdDTO> implements AnnualitaUsoSdDAO {

	@Autowired
	private UsoRidaumSdDAO usoRidaumSdDAO;

	@Autowired
	private TipoUsoDAO tipoUsoDAO;

	public static final String QUERY_SELECT_ANNUALITA_USO_SD_PRINC = " SELECT g.anno, " + "            ausd.*, "
			+ "            u.id_unita_misura, " + "            m.sigla_unita_misura, "
			+ "            m.des_unita_misura, " + "            u.des_tipo_uso, "
			+ "            a.id_tipo_uso as uso_t_anno, " + "            a.json_regola,   	 "
			+ "            a.data_fine_calcolata " + " FROM " + "	RISCA_R_ANNUALITA_USO_SD ausd, "
			+ "	RISCA_R_ANNUALITA_SD g,           " + "	RISCA_D_TIPO_USO u, " + "	RISCA_D_UNITA_MISURA m, "
			+ "	(select  " + "	   CASE WHEN reg.data_fine IS NULL  " + "		THEN reg.data_inizio  "
			+ "		ELSE reg.data_fine  " + "		END AS data_fine_calcolata, reg.* "
			+ "	  from risca_r_tipo_uso_regola reg) as a " + "	WHERE " + "	   g.id_annualita_sd = :idAnnualitaSd "
			+ "	   and g.id_annualita_sd = ausd.id_annualita_sd " + "	   and ausd.id_tipo_uso = u.id_tipo_uso	 "
			+ "	   and u.id_tipo_uso_padre is null " + "	   and u.id_unita_misura = m.id_unita_misura	 "
			+ "	   and a.id_tipo_uso = u.id_tipo_uso "
			+ "	   and g.anno between EXTRACT(year from a.data_inizio) and EXTRACT(year from a.data_fine_calcolata) "
			+ "	ORDER BY g.anno";

	public static final String QUERY_SELECT_ANNUALITA_USO_SD_ID = "SELECT a.*, u.* FROM RISCA_R_ANNUALITA_USO_SD a "
			+ " inner join RISCA_D_TIPO_USO u on a.id_tipo_uso = u.id_tipo_uso"
			+ " WHERE a.id_annualita_sd = :idAnnualitaSd";

	public static final String QUERY_INSERT = "INSERT INTO risca_r_annualita_uso_sd "
			+ "(id_annualita_uso_sd, id_annualita_sd, id_tipo_uso, canone_uso, canone_unitario, "
			+ " gest_attore_ins, gest_data_ins, gest_attore_upd,  gest_data_upd, " + " gest_uid) "
			+ "VALUES(:idAnnualitaUsoSd, :idAnnualitaSd, :idTipoUso, :canoneUso, :canoneUnitario, "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid) ";

	public static final String QUERY_INSERT_WORKING = "INSERT INTO risca_w_annualita_uso_sd "
			+ "(id_annualita_uso_sd, id_annualita_sd, id_tipo_uso, canone_uso, canone_unitario) "
			+ "VALUES(:idAnnualitaUsoSd, :idAnnualitaSd, :idTipoUso, :canoneUso, :canoneUnitario) ";

	public static final String QUERY_UPDATE = "UPDATE risca_r_annualita_uso_sd "
			+ "SET id_annualita_sd= :idAnnualitaSd, id_tipo_uso = :idTipoUso , canone_uso = :canoneUso, canone_unitario = :canoneUnitario,"
			+ " gest_data_upd = :gestDataUpd , gest_attore_upd = :gestAttoreUpd "
			+ " where id_annualita_uso_sd  = :idAnnualitaUsoSd ";
	
	private static final String QUERY_DELETE_ANNUALITA_US_SD_BY_ID = "DELETE FROM risca_r_annualita_uso_sd WHERE id_annualita_uso_sd = :idAnnualitaUsoSd";

	private static final String QUERY_SELECT_WORKING_BY_ID_ANNUALITA_SD = " SELECT * FROM risca_w_annualita_uso_sd WHERE id_annualita_sd = :idAnnualitaSd";
	private static final String QUERY_DELETE_WORKING_BY_ID_ANNUALITA_SD = " DELETE FROM risca_w_annualita_uso_sd WHERE id_annualita_sd = :idAnnualitaSd";
	
	private static final String QUERY_INSERT_ANNUALITA_USO_SD_FROM_WORKING =" insert into RISCA_R_ANNUALITA_USO_SD (id_annualita_uso_sd, id_annualita_sd, id_tipo_uso, canone_uso, canone_unitario,"
			+ " gest_attore_ins, gest_data_ins, gest_attore_upd, gest_data_upd, gest_uid) "
			+ " select id_annualita_uso_sd, id_annualita_sd, id_tipo_uso, canone_uso, canone_unitario,"
			+ " :gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid "
			+ " from RISCA_W_ANNUALITA_USO_SD where id_annualita_sd = :idAnnualitaSd ";
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AnnualitaUsoSdExtendedDTO> loadAnnualitaUsiPrincipaliSd(Long idAnnualitaSd) {
		LOGGER.debug("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsiPrincipaliSd] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAnnualitaSd", idAnnualitaSd);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(QUERY_SELECT_ANNUALITA_USO_SD_PRINC, null, null), params,
					getExtendedRowMapper());
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsiPrincipaliSd]Data not found for idAnnualitaSd: "
					+ idAnnualitaSd);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsiPrincipaliSd] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsiPrincipaliSd] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsiPrincipaliSd] END");
		}
	}

	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<AnnualitaUsoSdDTO> getRowMapper() throws SQLException {

		return new AnnualitaUsoSdRowMapper();
	}

	@Override
	public RowMapper<AnnualitaUsoSdExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new AnnualitaUsoSdExtendedRowMapper();
	}

	public static class AnnualitaUsoSdRowMapper implements RowMapper<AnnualitaUsoSdDTO> {

		public AnnualitaUsoSdRowMapper() throws SQLException {
			// Instantiate class
		}

		@Override
		public AnnualitaUsoSdDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			AnnualitaUsoSdDTO bean = new AnnualitaUsoSdDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AnnualitaUsoSdDTO bean) throws SQLException {
			bean.setIdAnnualitaUsoSd(rs.getLong("id_annualita_uso_sd"));
			bean.setIdAnnualitaSd(rs.getLong("id_annualita_sd"));
			bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
			bean.setCanoneUso(rs.getBigDecimal("canone_uso") != null ? rs.getBigDecimal("canone_uso") : BigDecimal.ZERO );
			bean.setCanoneUnitario(rs.getBigDecimal("canone_unitario") != null ? rs.getBigDecimal("canone_unitario") : BigDecimal.ZERO );
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
			TipoUsoExtendedDTO tipoUsoDTO = new TipoUsoExtendedDTO();
			populateBeanTipoUso(rs, tipoUsoDTO);
			bean.setTipoUso(tipoUsoDTO);

		}

		private void populateBeanTipoUso(ResultSet rs, TipoUsoExtendedDTO bean) throws SQLException {
			if(rsHasColumn(rs, "id_tipo_uso")) bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
			if(rsHasColumn(rs, "id_ambito")) bean.setIdAmbito(rs.getLong("id_ambito"));
			if(rsHasColumn(rs, "id_unita_misura")) bean.setIdUnitaMisura(rs.getLong("id_unita_misura"));
			if(rsHasColumn(rs, "id_accerta_bilancio")) bean.setIdAccertaBilancio(rs.getLong("id_accerta_bilancio"));
			if(rsHasColumn(rs, "cod_tipo_uso")) bean.setCodTipouso(rs.getString("cod_tipo_uso"));
			if(rsHasColumn(rs, "des_tipo_uso")) bean.setDesTipouso(rs.getString("des_tipo_uso"));

			if(rsHasColumn(rs, "id_tipo_uso_padre")) bean.setIdTipoUsoPadre(rs.getString("id_tipo_uso_padre"));

			if(rsHasColumn(rs, "flg_uso_principale")) bean.setFlgUsoPrincipale(rs.getString("flg_uso_principale"));
			if(rsHasColumn(rs, "ordina_tipo_uso")) bean.setOrdinaTipoUso(rs.getLong("ordina_tipo_uso"));

			if(rsHasColumn(rs, "data_inizio_validita")) bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
			if(rsHasColumn(rs, "data_fine_validita")) bean.setDataFineValidita(rs.getDate("data_fine_validita"));

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

	public static class AnnualitaUsoSdExtendedRowMapper implements RowMapper<AnnualitaUsoSdExtendedDTO> {

		public AnnualitaUsoSdExtendedRowMapper() throws SQLException {
			// Instantiate class
		}

		@Override
		public AnnualitaUsoSdExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			AnnualitaUsoSdExtendedDTO bean = new AnnualitaUsoSdExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AnnualitaUsoSdExtendedDTO bean) throws SQLException {
			bean.setIdAnnualitaUsoSd(rs.getLong("id_annualita_uso_sd"));
			bean.setIdAnnualitaSd(rs.getLong("id_annualita_sd"));
			bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
			bean.setCanoneUso(rs.getBigDecimal("canone_uso") != null ? rs.getBigDecimal("canone_uso") : BigDecimal.ZERO );
			bean.setCanoneUnitario(rs.getBigDecimal("canone_unitario") != null ? rs.getBigDecimal("canone_unitario") : BigDecimal.ZERO );
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));

			// extended fields
			bean.setIdUnitaMisura(rs.getLong("id_unita_misura"));
			bean.setSiglaUnitaMisura(rs.getString("sigla_unita_misura"));
			bean.setDesUnitaMisura(rs.getString("des_unita_misura"));
			bean.setDesTipoUso(rs.getString("des_tipo_uso"));
			bean.setUsoTAnno(rs.getLong("uso_t_anno"));
			bean.setJsonRegola(rs.getString("json_regola"));
			bean.setAnno(rs.getString("anno"));
			bean.setDataFineCalcolata(rs.getString("data_fine_calcolata"));

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
	public List<AnnualitaUsoSdDTO> loadAnnualitaUsiByIdAnnualitaSd(Long idAnnualitaSd) {
		LOGGER.debug("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsiPrincipaliSd] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAnnualitaSd", idAnnualitaSd);
			MapSqlParameterSource params = getParameterValue(map);
			List<AnnualitaUsoSdDTO> listAnnualitaUsoSdDTO = template
					.query(getQuery(QUERY_SELECT_ANNUALITA_USO_SD_ID, null, null), params, getRowMapper());
			if (!listAnnualitaUsoSdDTO.isEmpty()) {
				for (AnnualitaUsoSdDTO annualitaUsoSdDTO : listAnnualitaUsoSdDTO) {
					TipoUsoExtendedDTO tipoUso = tipoUsoDAO
							.loadTipoUsoByIdTipoUsoOrCodTipoUso(annualitaUsoSdDTO.getIdTipoUso().toString());
					annualitaUsoSdDTO.setTipoUso(tipoUso);
				}
			}
			return listAnnualitaUsoSdDTO;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsiPrincipaliSd]Data not found for idAnnualitaSd: "
					+ idAnnualitaSd);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsiPrincipaliSd] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsiPrincipaliSd] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsiPrincipaliSd] END");
		}
	}

	@Override
	public AnnualitaUsoSdDTO saveAnnualitaUsoSdDTO(AnnualitaUsoSdDTO dto) throws Exception {
		LOGGER.debug("[AnnualitaUsoSdDAOImpl::saveAnnualitaUsoSdDTO] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_r_annualita_uso_sd");
			map.put("idAnnualitaUsoSd", genId);
			map.put("idAnnualitaSd", dto.getIdAnnualitaSd());
			map.put("idTipoUso", dto.getTipoUso().getIdTipoUso());
			map.put("canoneUso", dto.getCanoneUso() != null ? dto.getCanoneUso().setScale(2, RoundingMode.HALF_UP) : null);
			map.put("canoneUnitario", dto.getCanoneUnitario());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_INSERT;
			template.update(getQuery(query, null, null), params, keyHolder);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			dto.setIdAnnualitaUsoSd(genId);
			if (dto.getUsoRidaumSd() != null)
				if (!dto.getUsoRidaumSd().isEmpty()) {
					for (UsoRidaumSdExtendedDTO usoRidaumSdExtendedDTO : dto.getUsoRidaumSd()) {
						usoRidaumSdExtendedDTO.setIdAnnualitaUsoRidaum(genId);
						usoRidaumSdDAO.saveUsoRidaumSD(usoRidaumSdExtendedDTO);
					}
				}
		} catch (Exception e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::saveAnnualitaUsoSdDTO] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}

		LOGGER.debug("[AnnualitaUsoSdDAOImpl::saveAnnualitaUsoSdDTO] END");
		return dto;
	}

	@Override
	public AnnualitaUsoSdDTO saveAnnualitaUsoSdWorking(AnnualitaUsoSdDTO dto) throws DataAccessException, SQLException {
		LOGGER.debug("[AnnualitaUsoSdDAOImpl::saveAnnualitaUsoSdWorking] BEGIN");
		Map<String, Object> map = new HashMap<>();

		Long genId = findNextSequenceValue("seq_risca_r_annualita_uso_sd");
		map.put("idAnnualitaUsoSd", genId);
		map.put("idAnnualitaSd", dto.getIdAnnualitaSd());
		map.put("idTipoUso", dto.getTipoUso().getIdTipoUso());
		map.put("canoneUso", dto.getCanoneUso());
		map.put("canoneUnitario", dto.getCanoneUnitario());

		MapSqlParameterSource params = getParameterValue(map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String query = QUERY_INSERT_WORKING;
		template.update(getQuery(query, null, null), params, keyHolder);
		dto.setIdAnnualitaUsoSd(genId);

		LOGGER.debug("[AnnualitaUsoSdDAOImpl::saveAnnualitaUsoSdWorking] END");
		return dto;
	}

	@Override
	public AnnualitaUsoSdDTO updateAnnualitaUsoSdDTO(AnnualitaUsoSdDTO dto) throws Exception {
		LOGGER.debug("[AnnualitaUsoSdDAOImpl::updateAnnualitaUsoSdDTO] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idAnnualitaUsoSd", dto.getIdAnnualitaUsoSd());
			map.put("idAnnualitaSd", dto.getIdAnnualitaSd());
			map.put("idTipoUso", dto.getTipoUso().getIdTipoUso());
			map.put("canoneUso", dto.getCanoneUso());
			map.put("canoneUnitario", dto.getCanoneUnitario());
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			dto.setGestDataUpd(now);

			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_UPDATE;

			template.update(getQuery(query, null, null), params);
			if (dto.getUsoRidaumSd() != null)
				if (!dto.getUsoRidaumSd().isEmpty()) {
					for (UsoRidaumSdExtendedDTO usoRidaumSdExtendedDTO : dto.getUsoRidaumSd()) {
						usoRidaumSdDAO.updateUsoRidaumSD(usoRidaumSdExtendedDTO);
					}
				}
		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::updateAnnualitaUsoSdDTO] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (SQLException e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::updateAnnualitaUsoSdDTO] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		} catch (Exception e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::updateAnnualitaUsoSdDTO] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}

		LOGGER.debug("[AnnualitaUsoSdDAOImpl::updateAnnualitaUsoSdDTO] END");
		return dto;
	}

	@Override
	public AnnualitaUsoSdDTO deleteAnnualitaUsoSdDTO(AnnualitaUsoSdDTO annualitaUsoSdDTO)
			throws Exception {
		LOGGER.debug("[AnnualitaUsoSdDAOImpl::deleteAnnualitaUsoSdDTO] BEGIN");
		try {
			if (annualitaUsoSdDTO.getUsoRidaumSd() != null)
				if (!annualitaUsoSdDTO.getUsoRidaumSd().isEmpty()) {
					for (UsoRidaumSdExtendedDTO usoRidaumSdExtendedDTO : annualitaUsoSdDTO.getUsoRidaumSd()) {
						usoRidaumSdDAO.deleteUsoRidaumSD(usoRidaumSdExtendedDTO);
					}
				}
			Map<String, Object> map = new HashMap<>();
			map.put("idAnnualitaUsoSd", annualitaUsoSdDTO.getIdAnnualitaUsoSd());
			MapSqlParameterSource params = getParameterValue(map);
			template.update(getQuery(QUERY_DELETE_ANNUALITA_US_SD_BY_ID, null, null), params);

		} catch (DataAccessException e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::deleteAnnualitaUsoSdDTO] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AnnualitaUsoSdDAOImpl::deleteAnnualitaUsoSdDTO] END");
		}
		return annualitaUsoSdDTO;
	}

	@Override
	public List<AnnualitaUsoSdDTO> loadAnnualitaUsoSdWorkingByIdAnnualitaSd(Long idAnnualitaSd) throws DAOException {
		LOGGER.debug("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsoSdWorkingByIdAnnualitaSd] BEGIN");
		List<AnnualitaUsoSdDTO> listAnnualitaUsoSd = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAnnualitaSd", idAnnualitaSd);
			MapSqlParameterSource params = getParameterValue(map);

			listAnnualitaUsoSd = template.query(getQuery(QUERY_SELECT_WORKING_BY_ID_ANNUALITA_SD, null, null), params,
					getRowMapper());
		} catch (Exception e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsoSdWorkingByIdAnnualitaSd] ERROR : " + e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AnnualitaUsoSdDAOImpl::loadAnnualitaUsoSdWorkingByIdAnnualitaSd] END");
		}
		return listAnnualitaUsoSd;
	}

	@Override
	public Integer deleteAnnualitaUsoSdWorkingByIdAnnualitaSd(Long idAnnualitaSd) throws DAOException {
		LOGGER.debug("[AnnualitaUsoSdDAOImpl::deleteAnnualitaUsoSdWorkingByIdAnnualitaSd] BEGIN");
		Integer ret = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAnnualitaSd", idAnnualitaSd);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			ret = template.update(getQuery(QUERY_DELETE_WORKING_BY_ID_ANNUALITA_SD, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[AnnualitaUsoSdDAOImpl::deleteAnnualitaUsoSdWorkingByIdAnnualitaSd] ERROR : " + e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AnnualitaUsoSdDAOImpl::deleteAnnualitaUsoSdWorkingByIdAnnualitaSd] END");
		}
		return ret;
	}
	
	@Override
	public List<AnnualitaUsoSdDTO> copyAnnualitaUsoSdFromWorkingByAnnualitaSd(Long idAnnualitaSd, String attore) throws DAOException {
		LOGGER.debug("[AnnualitaUsoSdDAOImpl::copyAnnualitaUsoSdFromWorkingByAnnualitaSd] BEGIN");
		List<AnnualitaUsoSdDTO> listAnnualitaUsoSd = null;
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idAnnualitaSd", idAnnualitaSd);
			map.put("gestAttoreIns", attore);
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_ANNUALITA_USO_SD_FROM_WORKING, null, null), params, keyHolder);
			
			map = new HashMap<>();
			map.put("idAnnualitaSd", idAnnualitaSd);
			params = getParameterValue(map);
			listAnnualitaUsoSd = template
					.query(getQuery(QUERY_SELECT_ANNUALITA_USO_SD_ID, null, null), params, getRowMapper());

		} catch (Exception e) {
			LOGGER.error(
					"[AnnualitaUsoSdDAOImpl::copyAnnualitaUsoSdFromWorkingByAnnualitaSd] ERROR : " +e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AnnualitaUsoSdDAOImpl::copyAnnualitaUsoSdFromWorkingByAnnualitaSd] END");
		}

		return listAnnualitaUsoSd;
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}
}
