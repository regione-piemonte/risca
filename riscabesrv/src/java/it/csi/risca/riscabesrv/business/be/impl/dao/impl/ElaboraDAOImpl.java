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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.ElaboraDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.ElaboraDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.ParametroElaboraDTO;
import it.csi.risca.riscabesrv.dto.StatoElaborazioneDTO;
import it.csi.risca.riscabesrv.dto.TipoElaboraExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type Elabora dao.
 *
 * @author CSI PIEMONTE
 */
public class ElaboraDAOImpl extends RiscaBeSrvGenericDAO<ElaboraDTO> implements ElaboraDAO {

	private final String className = this.getClass().getSimpleName();

	
    @Autowired
    private MessaggiDAO messaggiDAO;
    
    private static final String ATTORE_SC = "BATCH_STATO_CONT-EpayToRiscaWS";
    private static final String ATTORE_BATCH_STATO_CONT = "BATCH_STATO_CONT";
    
	private static final String WHERE = "WHERE 1=1 ";
	
	private static final String QUERY_LOAD_ELABORA_BY_TIPO_AND_STATO = "SELECT * FROM risca_t_elabora rte "
			+ "INNER JOIN risca_d_ambito rda ON rte.id_ambito  = rda.id_ambito "
			+ "INNER JOIN risca_d_tipo_elabora rdte ON rte.id_tipo_elabora = rdte.id_tipo_elabora "
			+ "INNER JOIN risca_d_stato_elabora rdse ON rte.id_stato_elabora = rdse.id_stato_elabora " ;
	
	private static final String QUERY_COUNT_ELABORA_BY_TIPO_AND_STATO = "SELECT count(*) FROM risca_t_elabora rte "
			+ "INNER JOIN risca_d_ambito rda ON rte.id_ambito  = rda.id_ambito "
			+ "INNER JOIN risca_d_tipo_elabora rdte ON rte.id_tipo_elabora = rdte.id_tipo_elabora "
			+ "INNER JOIN risca_d_stato_elabora rdse ON rte.id_stato_elabora = rdse.id_stato_elabora " + WHERE;
	
	private static final String CONDITION_AMBITO = " AND rte.id_ambito = :idAmbito ";
	private static final String CONDITION_TIPO = " AND rdte.cod_tipo_elabora IN (:codTipoElabora) ";
	private static final String CONDITION_ID_TIPO = " AND rdte.id_tipo_elabora IN (:idTipoElabora) ";
	private static final String CONDITION_STATO = " AND rdse.cod_stato_elabora IN (:codStatoElabora) ";
	private static final String CONDITION_FUNZIONALITA = " AND rdte.id_funzionalita = (select id_funzionalita from risca_d_funzionalita rdf where cod_funzionalita = :codFunzionalita) ";
	private static final String CONDITION_DATE_AFTER = " AND data_richiesta >= to_date(:dataRichiestaInizio) ";
	private static final String CONDITION_DATE_BEFORE = " AND data_richiesta <= to_date(:dataRichiestaFine) ";
	private static final String	CONDITION_FLG_VISIBILE = " AND rdte.flg_visibile = :flgVisibile ";
	private static final String QUERY_JOIN_PARAMETRO_ELABORA = " INNER JOIN  risca_r_parametro_elabora rrpe ON rte.id_elabora = rrpe.id_elabora ";
	private static final String	CONDITION_PARAMETRO_ELABORA = " AND rrpe.raggruppamento ='R1' "
			                                                  + "AND rrpe.chiave ='ANNO' "
			                                                   + " AND rrpe.valore = :valore ";
	
	private static final String QUERY_INSERT_ELABORA = "INSERT INTO risca_t_elabora(id_elabora, id_ambito, "
			+ "id_tipo_elabora, id_stato_elabora, data_richiesta, gest_attore_ins, gest_data_ins, "
			+ "gest_attore_upd, gest_data_upd, gest_uid) "
			+ "SELECT :idElabora, :idAmbito, id_tipo_elabora, id_stato_elabora, :dataRichiesta, "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid "
			+ "FROM risca_d_stato_elabora se, risca_d_tipo_elabora te "
			+ "WHERE se.cod_stato_elabora = :codStatoElabora and te.cod_tipo_elabora = :codTipoElabora ";

	private static final String QUERY_UPDATE_ELABORA = "UPDATE risca_t_elabora SET id_ambito = :idAmbito,  "
			+ "id_tipo_elabora = (SELECT id_tipo_elabora FROM risca_d_tipo_elabora WHERE cod_tipo_elabora = :codTipoElabora),  "
			+ "id_stato_elabora = (SELECT id_stato_elabora FROM risca_d_stato_elabora WHERE cod_stato_elabora = :codStatoElabora),  "
			+ "data_richiesta = :dataRichiesta, "
			+ "gest_attore_upd = :gestAttoreUpd, gest_data_upd = :gestDataUpd, gest_uid = :gestUid, nome_file_generato = :nomeFileGenerato  "
			+ "WHERE id_elabora = :idElabora";
	
	private static final String QUERY_LOAD_ELABORA_BY_ID = "SELECT * FROM risca_t_elabora rte  "
			+ "INNER JOIN risca_d_ambito rda ON rte.id_ambito  = rda.id_ambito  "
			+ "INNER JOIN risca_d_tipo_elabora rdte ON rte.id_tipo_elabora = rdte.id_tipo_elabora  "
			+ "INNER JOIN risca_d_stato_elabora rdse ON rte.id_stato_elabora = rdse.id_stato_elabora  "
			+ "WHERE id_elabora = :idElabora ";

	private static final String	ORDER_BY = " ORDER BY ";
	
	private static final String	DATA_RICHIESTA_DESC= "  data_richiesta DESC ";
	private static final String	ID_ELABORAZIONI_DESC = "  id_elabora DESC ";
	
	public static final String STATO_ELAB_TERMINATA ="TERMIN";
	public static final String STATO_ELAB_TERMINATA_KO="TERMIN_KO";
	
	public static final String QUERY_VERIFY_BOLL_ORDINARIA = QUERY_LOAD_ELABORA_BY_TIPO_AND_STATO + QUERY_JOIN_PARAMETRO_ELABORA + WHERE + CONDITION_PARAMETRO_ELABORA;
	
	public static final String QUERY_LOAD_ELABORA_BY_CF= "SELECT * FROM risca_t_elabora rte  "
			+ "INNER JOIN risca_d_ambito rda ON rte.id_ambito  = rda.id_ambito  "
			+ "INNER JOIN risca_d_tipo_elabora rdte ON rte.id_tipo_elabora = rdte.id_tipo_elabora  "
			+ "inner join risca_d_funzionalita rdf on rdte.id_funzionalita = rdf.id_funzionalita "
			+ "INNER JOIN risca_d_stato_elabora rdse ON rte.id_stato_elabora = rdse.id_stato_elabora  "
			+ "where rdf.cod_funzionalita  = 'REP' "
			+ "and rte.gest_attore_ins = :codiceFiscale "
			+ "and rte.nome_file_generato ilike  '%'||:codiceFiscale||'%' ";
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElaboraDTO> loadElabora(String idAmbito, List<String> codTipoElabora, List<String> codStatoElabora,
			String dataRichiestaInizio, String dataRichiestaFine, String codFunzionalita, Integer offset, Integer limit,
			String sort, Integer flgVisibile) {
		LOGGER.debug("[ElaboraDAOImpl::loadElabora] BEGIN");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String query = QUERY_LOAD_ELABORA_BY_TIPO_AND_STATO +WHERE;
			Map<String, Object> map = new HashMap<>();
			if (idAmbito != null) {
				map.put("idAmbito", Long.parseLong(idAmbito));
				query += CONDITION_AMBITO;
			}
			if (codTipoElabora != null && codTipoElabora.size() > 0) {
				map.put("codTipoElabora", codTipoElabora);
				query += CONDITION_TIPO;
			}
			if (codStatoElabora != null && codStatoElabora.size() > 0) {
				map.put("codStatoElabora", codStatoElabora);
				query += CONDITION_STATO;
			}
			if (dataRichiestaInizio != null) {
				Date dataRichIn;
				try {
					dataRichIn = formatter.parse(dataRichiestaInizio);
					map.put("dataRichiestaInizio", dataRichIn);
					query += CONDITION_DATE_AFTER;
				} catch (ParseException e) {
					LOGGER.error("ElaboraDAOImpl::loadElabora] Errore nell'parse data ", e);
					return null;
				}
			}else {
				 Calendar cal = Calendar.getInstance();
			        // Sottrai 6 mesi alla data corrente
			        cal.add(Calendar.MONTH, -6);
			        Date sixMonthsAgoDate = cal.getTime(); 
					map.put("dataRichiestaInizio", sixMonthsAgoDate);
					query += CONDITION_DATE_AFTER;
					
			}
			if (dataRichiestaFine != null) {
				Date dataRichFin;
				try {
					dataRichFin = formatter.parse(dataRichiestaFine);
					map.put("dataRichiestaFine", dataRichFin);
					query += CONDITION_DATE_BEFORE;
				} catch (ParseException e) {
					LOGGER.error("ElaboraDAOImpl::loadElabora] Errore nell'parse data ", e);
					return null;
				}
			}
			if (codFunzionalita != null && !codFunzionalita.isEmpty()) {
				map.put("codFunzionalita", codFunzionalita);
				query += CONDITION_FUNZIONALITA;
			}
			if (flgVisibile != null) {
				map.put("flgVisibile", flgVisibile);
				query += CONDITION_FLG_VISIBILE;
			}
	        
	       
			if (StringUtils.isNotBlank(sort)) {
				query += mapSortConCampiDB(sort).concat(",").concat(ID_ELABORAZIONI_DESC);
			}else {
				query += ORDER_BY.concat(DATA_RICHIESTA_DESC).concat(",").concat(ID_ELABORAZIONI_DESC) ;
			}
			
			
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(
					getQuery(query, offset != null ? offset.toString() : null, limit != null ? limit.toString() : null),
					params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[ElaboraDAOImpl::loadElabora] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[ElaboraDAOImpl::loadElabora] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[ElaboraDAOImpl::loadElabora] END");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElaboraDTO loadElaboraById(Long idElabora, Boolean donwload) {
		LOGGER.debug("[ElaboraDAOImpl::loadElabora] BEGIN");
		try {
			String query = QUERY_LOAD_ELABORA_BY_ID;
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);

			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(getQuery(query, null, null), params, getRowMapper());
		} catch (SQLException e) {
			LOGGER.error("[ElaboraDAOImpl::loadElabora] Errore nell'esecuzione della query", e);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error("[ElaboraDAOImpl::loadElabora] Errore nell'accesso ai dati", e);
			return null;
		} finally {
			LOGGER.debug("[ElaboraDAOImpl::loadElabora] END");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ElaboraDTO> verifyElabora(String idAmbito, List<Integer> idTipoElabora, List<String> codStatoElabora) {
		LOGGER.debug("[ElaboraDAOImpl::verifyElabora] BEGIN");
		try {
			String query  = QUERY_LOAD_ELABORA_BY_TIPO_AND_STATO + WHERE;
			Map<String, Object> map = new HashMap<>();
			if (idAmbito != null) { 
				map.put("idAmbito", Long.parseLong(idAmbito));
				query += CONDITION_AMBITO;
			}
			if (idTipoElabora != null && idTipoElabora.size() > 0) {
				map.put("idTipoElabora", idTipoElabora);
				query += CONDITION_ID_TIPO;
			}
			if (codStatoElabora != null && codStatoElabora.size() > 0) {
				map.put("codStatoElabora", codStatoElabora);
				query += CONDITION_STATO;
			}
			MapSqlParameterSource params = getParameterValue(map);
			List<ElaboraDTO> result = template.query(getQuery(query, null, null), params, getRowMapper());

			return result;
		} catch (SQLException e) {
			LOGGER.error("[ElaboraDAOImpl::verifyElabora] Errore nell'esecuzione della query", e);
			return null; // false;
		} catch (DataAccessException e) {
			LOGGER.error("[ElaboraDAOImpl::verifyElabora] Errore nell'accesso ai dati", e);
			return null; // false;
		} finally {
			LOGGER.debug("[ElaboraDAOImpl::verifyElabora] END");
		}
	}

	@Override
	public ElaboraDTO saveElabora(ElaboraDTO dto) throws Exception {
		LOGGER.debug("[ElaboraDAOImpl::saveElabora] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();

			Long genId = findNextSequenceValue("seq_risca_t_elabora");

			map.put("idElabora", genId);
			map.put("idAmbito", dto.getAmbito().getIdAmbito());
			map.put("codTipoElabora", dto.getTipoElabora().getCodTipoElabora());
			map.put("codStatoElabora", dto.getStatoElabora().getCodStatoElabora());
			map.put("dataRichiesta", now);
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_INSERT_ELABORA, null, null), params, keyHolder);
			dto.setIdElabora(genId);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			dto.setDataRichiesta(now);
		} catch (Exception e) {
			LOGGER.error("[ElaboraDAOImpl::saveElabora] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		     throw e;

		} finally {
			LOGGER.debug("[ElaboraDAOImpl::saveElabora] END");
		}

		return dto;
	}

	@Override
	public ElaboraDTO updateElabora(ElaboraDTO dto) throws Exception{
		LOGGER.debug("[ElaboraDAOImpl::updateElabora] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();

			map.put("idAmbito", dto.getAmbito().getIdAmbito());
			map.put("codTipoElabora", dto.getTipoElabora().getCodTipoElabora());
			map.put("codStatoElabora", dto.getStatoElabora().getCodStatoElabora());
			map.put("dataRichiesta", now);
			
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid",  generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			map.put("nomeFileGenerato", dto.getNomeFileGenerato());
			map.put("idElabora", dto.getIdElabora());

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(getQuery(QUERY_UPDATE_ELABORA, null, null), params, keyHolder);
			dto.setGestDataUpd(now);
			dto.setDataRichiesta(now);
		} catch (Exception e) {
			LOGGER.error("[ElaboraDAOImpl::updateElabora] ERROR : " +e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		     throw e;
		} finally {
			LOGGER.debug("[ElaboraDAOImpl::updateElabora] END");
		}

		return dto;
	}

	private String mapSortConCampiDB(String sort) {
		if (sort.contains("idAmbito")) {
			return getQuerySortingSegment(sort.replace("idAmbito", "id_ambito"));
		}
		if (sort.contains("codTipoElabora")) {
			return getQuerySortingSegment(sort.replace("codTipoElabora", "cod_tipo_elabora"));
		}
		if (sort.contains("codStatoElabora")) {
			return getQuerySortingSegment(sort.replace("codStatoElabora", "cod_stato_elabora"));
		}
		if (sort.contains("dataRichiesta")) {
			return getQuerySortingSegment(sort.replace("dataRichiesta", "data_richiesta"));
		}
		return "";
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
	public RowMapper<ElaboraDTO> getRowMapper() throws SQLException {
		return new ElaboraRowMapper();
	}

	/**
	 * Returns a RowMapper for a new bean instance
	 *
	 * @return RowMapper<T>
	 */
	@Override
	public RowMapper<ElaboraDTO> getExtendedRowMapper() throws SQLException {
		return new ElaboraRowMapper();
	}

	/**
	 * The type Configurazione row mapper.
	 */
	public static class ElaboraRowMapper implements RowMapper<ElaboraDTO> {

		/**
		 * Instantiates a new Elabora row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public ElaboraRowMapper() throws SQLException {
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
		public ElaboraDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ElaboraDTO bean = new ElaboraDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, ElaboraDTO bean) throws SQLException {
			bean.setIdElabora(rs.getLong("id_elabora"));
			bean.setDataRichiesta(rs.getDate("data_richiesta"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
			bean.setNomeFileGenerato(rs.getString("nome_file_generato"));

			populateAmbito(rs, bean);
			populateTipoElabora(rs, bean);
			populateStatoElabora(rs, bean);
		}

		private void populateTipoElabora(ResultSet rs, ElaboraDTO bean) throws SQLException {
			TipoElaboraExtendedDTO tipoElab = new TipoElaboraExtendedDTO();
			tipoElab.setIdTipoElabora(rs.getLong("id_tipo_elabora"));
			tipoElab.setIdAmbito(rs.getLong("id_ambito"));
			tipoElab.setIdFunzionalita(rs.getLong("id_funzionalita"));
			tipoElab.setCodTipoElabora(rs.getString("cod_tipo_elabora"));
			tipoElab.setDesTipoElabora(rs.getString("des_tipo_elabora"));
			tipoElab.setOrdinaTipoElabora(rs.getLong("ordina_tipo_elabora"));
			tipoElab.setFlgDefault(rs.getInt("flg_default"));
			tipoElab.setFlgVisibile(rs.getInt("flg_visibile"));
			tipoElab.setDataInizioValidita(rs.getDate("data_inizio_validita"));
			tipoElab.setDataFineValidita(rs.getDate("data_fine_validita"));
			bean.setTipoElabora(tipoElab);
		}

		private void populateStatoElabora(ResultSet rs, ElaboraDTO bean) throws SQLException {
			StatoElaborazioneDTO statoElab = new StatoElaborazioneDTO();
			statoElab.setIdStatoElabora(rs.getLong("id_stato_elabora"));
			statoElab.setCodStatoElabora(rs.getString("cod_stato_elabora"));
			statoElab.setDesStatoElabora(rs.getString("des_stato_elabora"));
			bean.setStatoElabora(statoElab);
		}

		private void populateAmbito(ResultSet rs, ElaboraDTO bean) throws SQLException {
			AmbitoDTO ambito = new AmbitoDTO();
			ambito.setIdAmbito(rs.getLong("id_ambito"));
			ambito.setCodAmbito(rs.getString("cod_ambito"));
			ambito.setDesAmbito(rs.getString("des_ambito"));
			bean.setAmbito(ambito);
		}

	}

	@Override
	public Integer countAllElabora(String idAmbito, List<String> codTipoElabora, List<String> codStatoElabora,
			String dataRichiestaInizio, String dataRichiestaFine, String codFunzionalita, Integer flgVisibile) {
		LOGGER.debug("[ElaboraDAOImpl::countAllElabora] BEGIN");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String query = QUERY_COUNT_ELABORA_BY_TIPO_AND_STATO;
			Map<String, Object> map = new HashMap<>();
			if (idAmbito != null) {
				map.put("idAmbito", Long.parseLong(idAmbito));
				query += CONDITION_AMBITO;
			}
			if (codTipoElabora != null && codTipoElabora.size() > 0) {
				map.put("codTipoElabora", codTipoElabora);
				query += CONDITION_TIPO;
			}
			if (codStatoElabora != null && codStatoElabora.size() > 0) {
				map.put("codStatoElabora", codStatoElabora);
				query += CONDITION_STATO;
			}
			if (dataRichiestaInizio != null) {
				Date dataRichIn;
				try {
					dataRichIn = formatter.parse(dataRichiestaInizio);
					map.put("dataRichiestaInizio", dataRichIn);
					query += CONDITION_DATE_AFTER;
				} catch (ParseException e) {
					LOGGER.error("ElaboraDAOImpl::loadElabora] Errore nell'parse data ", e);
					return null;
				}
			}
			if (dataRichiestaFine != null) {
				Date dataRichFin;
				try {
					dataRichFin = formatter.parse(dataRichiestaFine);
					map.put("dataRichiestaFine", dataRichFin);
					query += CONDITION_DATE_BEFORE;
				} catch (ParseException e) {
					LOGGER.error("ElaboraDAOImpl::loadElabora] Errore nell'parse data ", e);
					return null;
				}
			}
			if (codFunzionalita != null && !codFunzionalita.isEmpty()) {
				map.put("codFunzionalita", codFunzionalita);
				query += CONDITION_FUNZIONALITA;
			}
			if (flgVisibile != null) {
				map.put("flgVisibile", flgVisibile);
				query += CONDITION_FLG_VISIBILE;
			}
			MapSqlParameterSource params = getParameterValue(map);
			return template.queryForObject(query, params, Integer.class);
		} catch (DataAccessException e) {
			LOGGER.error("[ElaboraDAOImpl::loadElabora] Errore nell'accesso ai dati", e);
			 throw e;
		} finally {
			LOGGER.debug("[ElaboraDAOImpl::loadElabora] END");
		}
	}


	/**
	 * Verifica se l'elaborazione e' di tipo "Bollettazione ordinaria" e gestisce le eccezioni se necessario.
	 *
	 * @param elabora L'oggetto ElaboraDTO da verificare.
	 * @throws BusinessException Lanciata in caso di eccezione business.
	 * @throws Exception 
	 */
	@Override
	public void verifyBollOrdinaria(ElaboraDTO elabora) throws BusinessException, Exception {
	    LOGGER.debug("[ElaboraDAOImpl::verifyBollOrdinaria] BEGIN");

	    // Verifica se l'elaborazione e' di tipo "Bollettazione ordinaria"
	    if (isBollOrdinaria(elabora)) {
	        // Costruisce le condizioni per la query SQL
	        Map<String, Object> conditions = buildQueryConditions(elabora);

	        // Costruisce la query SQL con le condizioni
	        String queryWithConditions = addConditionsToQuery(QUERY_VERIFY_BOLL_ORDINARIA, conditions);

	        // Ottiene i parametri per la query SQL
	        MapSqlParameterSource params = getParameterValue(conditions);

	        // Esegue la query SQL
	        List<ElaboraDTO> resultBO = template.query(getQuery(queryWithConditions, null, null), params, getRowMapper());

	        // Se il risultato non e' vuoto, gestisce l'eccezione BusinessException
	        if (!resultBO.isEmpty()) {
	            throwBusinessException(Constants.E101);
	        }
	    }

	    LOGGER.debug("[ElaboraDAOImpl::verifyBollOrdinaria] END");
	}

	/**
	 * Verifica se l'elaborazione e' di tipo "Bollettazione ordinaria".
	 *
	 * @param elabora L'oggetto ElaboraDTO da verificare.
	 * @return true se l'elaborazione e' di tipo "Bollettazione ordinaria", altrimenti false.
	 */
	private boolean isBollOrdinaria(ElaboraDTO elabora) {
	    return elabora.getTipoElabora() != null &&
	            elabora.getTipoElabora().getIdTipoElabora() != null &&
	            elabora.getTipoElabora().getIdTipoElabora().equals(1L);
	}

	/**
	 * Costruisce una mappa con chiave e valore dall'oggetto Elabora.
	 *
	 * @param elabora L'oggetto ElaboraDTO da utilizzare per costruire le condizioni.
	 * @return Un Map di condizioni.
	 */
	private Map<String, Object> buildQueryConditions(ElaboraDTO elabora) {
	    Map<String, Object> conditions = new HashMap<>();
	    conditions.put("idAmbito", elabora.getAmbito().getIdAmbito());
	    conditions.put("idTipoElabora", 1L); // id_tipo_elabora 1 = Bollettazione ordinaria
	    conditions.put("codStatoElabora", Arrays.asList(STATO_ELAB_TERMINATA));

	    // Se ci sono parametri "ANNO", aggiunge la condizione "valore" alla mappa
	    if (Utils.isNotEmpty(elabora.getParametri())) {
	        String anno = elabora.getParametri().stream()
	                .filter(f -> "ANNO".equals(f.getChiave()))
	                .map(ParametroElaboraDTO::getValore)
	                .findFirst()
	                .orElse(null);
	        conditions.put("valore", anno);
	    }

	    return conditions;
	}

	/**
	 * Gestisce l'eccezione BusinessException con il codice specificato.
	 *
	 * @param codMessaggio Il codice del messaggio di eccezione.
	 * @throws BusinessException Lanciata con il codice del messaggio specificato.
	 * @throws SystemException 
	 * @throws SQLException 
	 */
	private void throwBusinessException(String codMessaggio) throws BusinessException, Exception {
	    MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(codMessaggio);
	    throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
	}


	/**
	 * Aggiunge condizioni dinamiche a una query SQL esistente basate su un mappa di condizioni e restituisce la query aggiornata.
	 *
	 * @param query La query SQL esistente a cui aggiungere le condizioni.
	 * @param conditions Una mappa contenente le condizioni da aggiungere alla query (nome del parametro -> valore).
	 * @return La query SQL aggiornata con le condizioni.
	 */
	private String addConditionsToQuery(String query, Map<String, Object> conditions) {
		Map<String, String> mapCondition=	createBollOrdinariaClauses();
	    for (Map.Entry<String, Object> entry : conditions.entrySet()) {
	        String key = entry.getKey();
	        Object value = entry.getValue();
	        if (value != null && mapCondition.containsKey(key) ) {
	            query += mapCondition.get(key);
	        }
	    }
	    return query;
	}
	/**
	 * Crea e restituisce una mappa contenente le clausole per il bollettazione ordinaria.
	 *
	 * @return Una mappa che associa chiavi di tipo String a valori di tipo String.
	 * Le chiavi rappresentano gli attributi delle clausole, e i valori
	 * sono i corrispondenti valori delle clausole.
	 */
	private Map<String, String> createBollOrdinariaClauses() {
	    Map<String, String> bollOrdinariaoClauses = new HashMap<>();
	    bollOrdinariaoClauses.put("idAmbito", CONDITION_AMBITO);
	    bollOrdinariaoClauses.put("idTipoElabora", CONDITION_ID_TIPO);
	    bollOrdinariaoClauses.put("codStatoElabora", CONDITION_STATO);
	    return bollOrdinariaoClauses;
	}
	
	@Override
	public long countElaboraNotturno() throws DAOException, SystemException {
		LOGGER.debug("[ElaboraDAOImpl::countElaboraNotturno] START");

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		sql.append(" select count(*) from risca_t_elabora rte  WHERE rte.gest_attore_upd in( '"+ATTORE_BATCH_STATO_CONT+"', '"+ATTORE_SC+"' ) ");	
		sql.append(" and rte.id_stato_elabora =  "+Constants.DB.STATO_ELABORA.ID_STATO_ELABORA.STACONT_OK ); 
		sql.append(" and DATE(rte.gest_data_upd) = DATE(CURRENT_DATE) ");

		long conteggio = 0;

		try {
			
			conteggio = template.queryForObject(sql.toString(), paramMap, Long.class);

		} catch (RuntimeException ex) {
			LOGGER.error("[ElaboraDAOImpl::countElaboraNotturno] esecuzione query", ex);
			throw new DAOException("Query failed");
		} finally {
			LOGGER.debug("[ElaboraDAOImpl::countElaboraNotturno] END");
		}
		return conteggio;
	}
	
	

	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ElaboraDTO> loadElaboraByCF(String codiceFiscale) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("codiceFiscale", codiceFiscale);
			MapSqlParameterSource params = getParameterValue(map);
			return template.query(QUERY_LOAD_ELABORA_BY_CF, params,  getRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		} catch (DataAccessException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}
	
	

}