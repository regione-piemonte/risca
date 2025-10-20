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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoSdUtilizzatoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRimborsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente.RimborsoAmbienteDAOImpl;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.tributi.RimborsoTributiDAOImpl;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.RimborsoDTO;
import it.csi.risca.riscabesrv.dto.RimborsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RimborsoSdUtilizzatoDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoRimborsoDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type StatoDebitorio dao.
 *
 * @author CSI PIEMONTE
 */
public class RimborsoDAOImpl extends RiscaBeSrvGenericDAO<RimborsoDTO> implements RimborsoDAO {

	public static final String QUERY_INSERT_UPD_DA_COMPENSARE = " insert into RISCA_W_RIMBORSO_UPD "
			+ "(id_elabora, id_rimborso, id_stato_debitorio, id_tipo_rimborso, id_soggetto, id_gruppo_soggetto, imp_rimborso,   "
			+ "causale, num_determina, data_determina, imp_restituito) "
			+ "select   "
			+ ":idElabora, rim.id_rimborso, rim.id_stato_debitorio, rim.id_tipo_rimborso, rim.id_soggetto, rim.id_gruppo_soggetto, rim.imp_rimborso,  "
			+ "rim.causale, rim.num_determina, rim.data_determina, rim.imp_restituito "
			+ "from RISCA_R_RIMBORSO rim "
			+ "inner join ( "
			+ "	select sta.id_stato_debitorio, risc_ambito.id_riscossione, risc_ambito.id_ambito "
			+ "	from RISCA_T_STATO_DEBITORIO sta "
			+ "	inner join ( "
			+ "		select ris.id_riscossione, tri.id_tipo_riscossione, tri.id_ambito  "
			+ "		from risca_t_riscossione ris "
			+ "		inner join risca_d_tipo_riscossione tri on ris.id_tipo_riscossione = tri.id_tipo_riscossione  "
			+ "		where tri.id_ambito = :idAmbito "
			+ "	) risc_ambito on sta.id_riscossione = risc_ambito.id_riscossione "
			+ "	where sta.id_stato_debitorio IN (  "
			+ "		select id_stato_debitorio from RISCA_R_RIMBORSO where id_tipo_rimborso = 2 ) "
			+ ") sd_ambito on rim.id_stato_debitorio = sd_ambito.id_stato_debitorio "
			+ "where id_tipo_rimborso = 2 ";
	
	public static final String QUERY_LOAD_RIMBORSI_UPD_DA_COMPENSARE = "select a.*, b.id_riscossione "
			+ " from RISCA_W_RIMBORSO_UPD a, RISCA_W_STATO_DEBITORIO_UPD b, risca_d_tipo_rimborso c "
			+ " where a.id_stato_debitorio = b.id_stato_debitorio "
			+ " and a.id_tipo_rimborso = c.id_tipo_rimborso "
			+ " and c.cod_tipo_rimborso  = 'DA_COMPENSARE' "
			+ " and a.id_soggetto = :idSoggetto ";
	public static final String WHERE_ID_GRUPPO_SOGGETTO = " and a.id_gruppo_soggetto = :idGruppoSoggetto ";
	public static final String WHERE_ID_RISCOSSIONE = " and b.id_riscossione = :idRiscossione ";
	public static final String ORDER_BY = " order by (COALESCE(a.imp_rimborso, 0) - COALESCE(a.imp_restituito, 0)) DESC ";
	public static final String ORDER_BY_SD = " order by a.id_stato_debitorio ";
	
	public static final String QUERY_UPDATE_UPD_RIMBORSO_COMPENSATO = "update RISCA_W_RIMBORSO_UPD "
			+ " set id_tipo_rimborso = (select id_tipo_rimborso from risca_d_tipo_rimborso where cod_tipo_rimborso  =  'COMPENSATO'), "
			+ " imp_restituito = :impRimborso "
			+ " where id_rimborso = :idRimborso "
			+ " and id_elabora = :idElabora ";
	
	public static final String QUERY_UPDATE_UPD_RIMBORSO_COMPENSAZIONE = "update RISCA_W_RIMBORSO_UPD "
			+ " set imp_restituito = COALESCE(imp_restituito, 0) + :compensazione "
			+ " where id_rimborso = :idRimborso "
			+ " and id_elabora = :idElabora ";
	
	public static final String QUERY_UPDATE_RIMBORSO_FROM_RIMBORSO_UPD = " update RISCA_R_RIMBORSO a "
			+ " set (id_tipo_rimborso, imp_restituito, gest_attore_upd, gest_data_upd) = "
			+ " ( "
			+ "     select id_tipo_rimborso, imp_restituito, :gestAttoreUpd, to_timestamp(:gestDataUpd, 'YYYY-MM-DD HH24:MI:SS.MS') "
			+ "     from RISCA_W_RIMBORSO_UPD b "
			+ "     where a.id_rimborso = b.id_rimborso "
			+ "     and id_elabora = :idElabora1 "
			+ " ) "
			+ " where a.id_rimborso IN (select id_rimborso from RISCA_W_RIMBORSO_UPD where id_elabora = :idElabora2 ) ";
	
	public static final String  QUERY_LOAD_RIMBORSI_BY_ID_STATO_DEBITORIO="Select r.* "
			+ " from risca_r_rimborso r "
			+ " where r.id_stato_debitorio = :idStatoDebitorio";

	
	public static final String QUERY_UPDATE_RIMBORSO=" UPDATE risca_r_rimborso SET id_stato_debitorio= :idStatoDebitorio, "
			+ "id_tipo_rimborso= :idTipoRimborso, id_soggetto= :idSoggetto, imp_rimborso= :impRimborso, causale= :causale, "
			+ "num_determina= :numDetermina, data_determina= :dataDetermina,"
			+ " imp_restituito= :impRestituito, gest_data_upd= :gestDataUp, gest_attore_upd= :gestAttoreUpd, gest_uid= :gestUid "
			+ "WHERE id_rimborso= :idRimborso";
	

	public static final String QUERY_DELETE = "DELETE FROM  risca_r_rimborso rrr WHERE rrr.id_stato_debitorio = :idStatoDebitorio";

	private static final String QUERY_DELETE_RIMBORSO_UPD_WORKING_BY_ELAB = " delete from risca_w_rimborso_upd where id_elabora = :idElabora ";

	

	
	@Autowired
	private TipoRimborsoDAO tipoRimborsoDAO;
	
    @Autowired
    private SoggettiDAO soggettoDAO;
    
    @Autowired
    private RimborsoSdUtilizzatoDAO rimborsoSdUtilizzatoDAO;
    
	@Autowired
	private RimborsoAmbienteDAOImpl rimborsoAmbienteDAOImpl;
	
	@Autowired
	private RimborsoTributiDAOImpl rimborsoTributiDaoImpl;
	
	@Autowired
	private AmbitiDAO ambitiDAO;
	
	private static final String AMBIENTE = "AMBIENTE";
	private static final String OPERE_PUBBLICHE = "Opere pubbliche";
	private static final String ATTIVITA_ESTRATTIVE = "Attivita estrattive";
	private static final String TRIBUTI = "TRIBUTI";
	
    
	@Override
	public Integer saveRimborsiUpdDaCompensare(Long idElabora, Long idAmbito) throws DAOException {
		LOGGER.debug("[RimborsoDAOImpl::saveRimborsiUpdDaCompensare] BEGIN");
		int result = 0;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("idAmbito", idAmbito);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_INSERT_UPD_DA_COMPENSARE;

			result = template.update(getQuery(query, null, null), params, keyHolder);
		} catch (Exception e) {
			LOGGER.error("[RimborsoDAOImpl::saveRimborsiUpdDaCompensare] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RimborsoDAOImpl::saveRimborsiUpdDaCompensare] END");
		}
		return result;
	}
	
	@Override
	public List<RimborsoExtendedDTO> loadRimborsiDaCompensareUpd(Long idRiscossione, Long idSoggetto, Long idGruppoSoggetto, boolean orderBySD) {
		LOGGER.debug("[RimborsoDAOImpl::loadRimborsiDaCompensareUpd] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idSoggetto", idSoggetto);
			String query = QUERY_LOAD_RIMBORSI_UPD_DA_COMPENSARE;
			if(idGruppoSoggetto != null) {
				map.put("idGruppoSoggetto", idGruppoSoggetto);
				query += WHERE_ID_GRUPPO_SOGGETTO;
			}
			if(idRiscossione != null) {
				map.put("idRiscossione", idRiscossione);
				query += WHERE_ID_RISCOSSIONE;
			}
			if(orderBySD) {
				query += ORDER_BY_SD;
			} else {
				query += ORDER_BY;
			}
			
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(getQuery(query, null, null),
					params, getExtendedRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[RimborsoDAOImpl::loadRimborsiDaCompensareUpd] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RimborsoDAOImpl::loadRimborsiDaCompensareUpd] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[RimborsoDAOImpl::loadRimborsiDaCompensareUpd] END");
		}
	}
	
	@Override
	public Integer updateRimborsoUpdCompensato(Long idRimborso, BigDecimal impRimborso, Long idElabora) throws DAOException {
		LOGGER.debug("[RimborsoDAOImpl::updateRimborsoUpdCompensato] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRimborso", idRimborso);
			map.put("impRimborso", impRimborso);
			map.put("idElabora", idElabora);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_UPD_RIMBORSO_COMPENSATO, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[RimborsoDAOImpl::updateRimborsoUpdCompensato] ERROR : "  ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RimborsoDAOImpl::updateRimborsoUpdCompensato] END");
		}

		return res;
	}
	
	@Override
	public Integer updateRimborsoUpdCompensazione(Long idRimborso, BigDecimal compensazione, Long idElabora) throws DAOException {
		LOGGER.debug("[RimborsoDAOImpl::updateRimborsoUpdCompensazione] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRimborso", idRimborso);
			map.put("compensazione", compensazione);
			map.put("idElabora", idElabora);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_UPD_RIMBORSO_COMPENSAZIONE, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[RimborsoDAOImpl::updateRimborsoUpdCompensazione] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RimborsoDAOImpl::updateRimborsoUpdCompensazione] END");
		}

		return res;
	}
	
	@Override
	public Integer updateRimborsoFromRimborsoUpd(String attore, Long idElabora) throws DAOException {
		LOGGER.debug("[RimborsoDAOImpl::updateRimborsoFromRimborsoUpd] BEGIN");
		Integer res = null;
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("gestAttoreUpd", attore);
			map.put("gestDataUpd", now);
			map.put("idElabora1", idElabora);
			map.put("idElabora2", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_UPDATE_RIMBORSO_FROM_RIMBORSO_UPD, null, null), params, keyHolder);
			
		} catch (Exception e) {
			LOGGER.error("[RimborsoDAOImpl::updateRimborsoFromRimborsoUpd] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RimborsoDAOImpl::updateRimborsoFromRimborsoUpd] END");
		}

		return res;
	}
	
	@Override
	public Integer deleteRimborsoUpdWorkingByIdElabora(Long idElabora) throws DAOException {
		LOGGER.debug("[RimborsoDAOImpl::deleteRimborsoUpdWorkingByIdElabora] BEGIN");
		Integer res = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			res = template.update(getQuery(QUERY_DELETE_RIMBORSO_UPD_WORKING_BY_ELAB, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error(
					"[RimborsoDAOImpl::deleteRimborsoUpdWorkingByIdElabora] ERROR : " ,e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RimborsoDAOImpl::deleteRimborsoUpdWorkingByIdElabora] END");
		}

		return res;
	}
	
	@Override
	public List<RimborsoExtendedDTO> loadRimborsiByIdStatoDebitorio(Long idStatoDebitorio){
		LOGGER.debug("[RimborsoDAOImpl::loadRimborsiByIdStatoDebitorio] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			String query = QUERY_LOAD_RIMBORSI_BY_ID_STATO_DEBITORIO;
			
			MapSqlParameterSource params = getParameterValue(map);
			List<RimborsoDTO> list = template.query(getQuery(query, null, null),
					params, getRowMapper());
			List<RimborsoExtendedDTO> listExtended = new ArrayList<>();
			for (RimborsoDTO rimborsoDTO : list) {
				RimborsoExtendedDTO rimborsoExtendedDTO = new RimborsoExtendedDTO();
				rimborsoExtendedDTO.setIdRimborso(rimborsoDTO.getIdRimborso());
				rimborsoExtendedDTO.setIdStatoDebitorio(rimborsoDTO.getIdStatoDebitorio());

				rimborsoExtendedDTO.setIdTipoRimborso(rimborsoDTO.getIdTipoRimborso());
				rimborsoExtendedDTO.setIdSoggetto(rimborsoDTO.getIdSoggetto());

				rimborsoExtendedDTO.setImpRimborso(rimborsoDTO.getImpRimborso());
				rimborsoExtendedDTO.setCausale(rimborsoDTO.getCausale());
				rimborsoExtendedDTO.setNumDetermina(rimborsoDTO.getNumDetermina());
				rimborsoExtendedDTO.setDataDetermina(rimborsoDTO.getDataDetermina());
				rimborsoExtendedDTO.setImpRestituito(rimborsoDTO.getImpRestituito());
				
				rimborsoExtendedDTO.setGestAttoreIns(rimborsoDTO.getGestAttoreIns());
				rimborsoExtendedDTO.setGestDataIns(rimborsoDTO.getGestDataIns());
				rimborsoExtendedDTO.setIdGruppoSoggetto(rimborsoDTO.getIdGruppoSoggetto());
				rimborsoExtendedDTO.setGestUid(rimborsoDTO.getGestUid());
				listExtended.add(rimborsoExtendedDTO);
			}
	
			for (RimborsoExtendedDTO rimborsoExtendedDTO : listExtended) {
				TipoRimborsoDTO tipoRimborso = null;
				 SoggettiExtendedDTO soggetto = null;
				 RimborsoSdUtilizzatoDTO rimborsoSdUtilizzatoDTO = null;
				try {
					tipoRimborso = tipoRimborsoDAO.loadTipiRimborsoById(rimborsoExtendedDTO.getIdTipoRimborso(), null, null, null);
					
			         soggetto = soggettoDAO.loadSoggettoById(rimborsoExtendedDTO.getIdSoggetto());
			         rimborsoSdUtilizzatoDTO = rimborsoSdUtilizzatoDAO.getRimborsoSdUtilizzatoByIdStatoDebitorio(idStatoDebitorio);
			        
				} catch (DAOException e) {
					LOGGER.error("[RimborsoDAOImpl::loadRimborsiByIdStatoDebitorio]DAOException: ",e);
				}
				catch (Exception e) {
					LOGGER.error("[RimborsoDAOImpl::loadRimborsiByIdStatoDebitorio]DAOException: ",e);
				}
				rimborsoExtendedDTO.setTipoRimborso(tipoRimborso);
				rimborsoExtendedDTO.setSoggettoRimborso(soggetto);
				rimborsoExtendedDTO.setRimborsoSdUtilizzato(rimborsoSdUtilizzatoDTO);	
				
			}
			return listExtended;
		}catch (EmptyResultDataAccessException e) {
			LOGGER.debug("[RimborsoDAOImpl::loadRimborsiByIdStatoDebitorio]Data not found for idStatoDebitorio: "+ idStatoDebitorio);
			return null;
		} catch (SQLException e) {
			LOGGER.error("[RimborsoDAOImpl::loadRimborsiByIdStatoDebitorio] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[RimborsoDAOImpl::loadRimborsiByIdStatoDebitorio] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[RimborsoDAOImpl::loadRimborsiByIdStatoDebitorio] END");
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
	public RowMapper<RimborsoDTO> getRowMapper() throws SQLException {
		boolean working = getTableName().equalsIgnoreCase("seq_risca_r_rimborso") ? false : true;
		return new RimborsoRowMapper(working);
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<RimborsoExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new RimborsoExtendedRowMapper();
	}

	/**
	 * The type Rimborso row mapper.
	 */
	public static class RimborsoRowMapper implements RowMapper<RimborsoDTO> {

		private boolean working;

		/**
		 * Instantiates a new Rimborso row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RimborsoRowMapper(boolean working) throws SQLException {
			this.working = working;
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
		public RimborsoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RimborsoDTO bean = new RimborsoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RimborsoDTO bean) throws SQLException {
			bean.setIdRimborso(rs.getLong("id_rimborso"));
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setIdTipoRimborso(rs.getLong("id_tipo_rimborso"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			bean.setImpRimborso(rs.getBigDecimal("imp_rimborso"));
			bean.setCausale(rs.getString("causale"));
			if(rs.getLong("id_gruppo_soggetto") > 0 )
		    	bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
			bean.setNumDetermina(rs.getString("num_determina"));
			bean.setDataDetermina(rs.getDate("data_determina"));
			bean.setImpRestituito(rs.getBigDecimal("imp_restituito"));
			
			if (!working) {
				bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
				bean.setGestDataIns(rs.getDate("gest_data_ins"));
				bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
				bean.setGestDataUpd(rs.getDate("gest_data_upd"));
				bean.setGestUid(rs.getString("gest_uid"));
			}
		}
	}
	
	/**
	 * The type Rimborso row mapper.
	 */
	public static class RimborsoExtendedRowMapper implements RowMapper<RimborsoExtendedDTO> {

		/**
		 * Instantiates a new Rimborso row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public RimborsoExtendedRowMapper() throws SQLException {
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
		public RimborsoExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RimborsoExtendedDTO bean = new RimborsoExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, RimborsoExtendedDTO bean) throws SQLException {
			bean.setIdRimborso(rs.getLong("id_rimborso"));
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			bean.setIdTipoRimborso(rs.getLong("id_tipo_rimborso"));
			bean.setIdSoggetto(rs.getLong("id_soggetto"));
			if(rs.getLong("id_gruppo_soggetto") >0)
			   bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
			bean.setImpRimborso(rs.getBigDecimal("imp_rimborso"));
			bean.setCausale(rs.getString("causale"));
			bean.setNumDetermina(rs.getString("num_determina"));
			bean.setDataDetermina(rs.getDate("data_determina"));
			bean.setImpRestituito(rs.getBigDecimal("imp_restituito"));
	
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
			
			//Campo aggiuntivo
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
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
	public RimborsoExtendedDTO saveRimborso(RimborsoExtendedDTO dto, Long idAmbito) throws Exception {
		LOGGER.debug("[RimborsoDAOImpl::saveRimborso] BEGIN");
		String ambito = "";
		Utils utils = new Utils();
	    if(utils.isLocalMod()){
	    	ambito = AMBIENTE;
	    	dto = rimborsoAmbienteDAOImpl.saveRimborso(dto);
		}else {
				LOGGER.debug("[RimborsoDAOImpl::saveRimborso] verifica ambito");
				AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
				ambito = ambitoDTO.getCodAmbito();
				LOGGER.debug("[RimborsoDAOImpl::saveRimborso] ambito: " + ambito);
				switch (ambito) {
				  case AMBIENTE:
					LOGGER.debug("[RimborsoDAOImpl::saveRimborso] ambito: AMBIENTE");
					dto = rimborsoAmbienteDAOImpl.saveRimborso(dto);
				    break;
				  case OPERE_PUBBLICHE:
					//TO DO
				    break;
				  case ATTIVITA_ESTRATTIVE:
					//TO DO
				    break;
				  case TRIBUTI:
					  LOGGER.debug("[RimborsoDAOImpl::saveRimborso] ambito: TRIBUTI");
					  dto = rimborsoTributiDaoImpl.saveRimborso(dto);
					  break;
				}
			}	   
			LOGGER.debug("[RimborsoDAOImpl::saveRimborso] END");
	        return dto;
	}

	@Override
	public RimborsoExtendedDTO updateRimborso(RimborsoExtendedDTO dto) throws Exception {
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idRimborso", dto.getIdRimborso());
			map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
			map.put("idTipoRimborso", dto.getTipoRimborso().getIdTipoRimborso());
			map.put("idSoggetto", dto.getSoggettoRimborso().getIdSoggetto());
			map.put("impRimborso", dto.getImpRimborso());
			map.put("causale", dto.getCausale());
			map.put("numDetermina", dto.getNumDetermina());
			
			map.put("dataDetermina", dto.getDataDetermina());
			map.put("impRestituito", dto.getImpRestituito());
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUp", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			dto.setGestDataUpd(now);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_UPDATE_RIMBORSO;
			
			template.update(getQuery(query, null, null), params, keyHolder);
		} catch (DataAccessException e) {
			LOGGER.error("[RimborsoAmbienteDAOImpl::updateRimborso] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new Exception(Constants.ERRORE_GENERICO);
		}


		return dto;
	}

	@Override
	public void deleteRimborso(Long idStatoDebitorio) throws DAOException {
		LOGGER.debug("[RimborsoDAOImpl::deleteRimborso] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_DELETE;
			template.update(getQuery(query, null, null), params);
		} catch (Exception e) {
			LOGGER.error("[RimborsoDAOImpl::deleteRimborso] ERROR : ",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
				throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[RimborsoDAOImpl::deleteRimborso] END");
		}
		
	}

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

}