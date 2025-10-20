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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagExtendedDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagSearchResultDTO;
import it.csi.risca.riscabesrv.dto.PagamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.TipoModalitaPagDTO;
import it.csi.risca.riscabesrv.util.Constants;

public class DettaglioPagDAOImpl  extends RiscaBeSrvGenericDAO<DettaglioPagDTO> implements DettaglioPagDAO {

 
    private final String className = this.getClass().getSimpleName();
    
	public static final String QUERY_SELECT_DETTAGLIO_PAG_BY_ID ="SELECT"
			+ "	rrdp.*, rtp.* ,rdtmp.*, rda.*, rata.*"
			+ " FROM risca_r_dettaglio_pag rrdp "
			+ " inner join risca_t_pagamento rtp on rrdp.id_pagamento = rtp.id_pagamento"
			+ " inner join risca_r_rata_sd rata on rrdp.id_rata_sd = rata.id_rata_sd"
			+ " inner join risca_d_tipo_modalita_pag rdtmp on rdtmp.id_tipo_modalita_pag = rtp.id_tipo_modalita_pag"
			+ " inner  join risca_d_ambito rda on rtp.id_ambito = rda.id_ambito " 
			+ " WHERE rrdp.id_dettaglio_pag = :idDettaglioPag"
			+ "	AND rata.id_rata_sd_padre  IS null ";
	
	public static final String QUERY_SELECT_DETTAGLIO_PAG_BY_ID_RATA ="SELECT"
			+ "	rrdp.*, rtp.* ,rdtmp.*, rda.*, rata.* "
			+ " FROM risca_r_dettaglio_pag rrdp "
			+ " inner join risca_t_pagamento rtp on rrdp.id_pagamento = rtp.id_pagamento"
			+ " inner join risca_r_rata_sd rata on rrdp.id_rata_sd = rata.id_rata_sd"
			+ " inner join risca_d_tipo_modalita_pag rdtmp on rdtmp.id_tipo_modalita_pag = rtp.id_tipo_modalita_pag"
			+ " inner  join risca_d_ambito rda on rtp.id_ambito = rda.id_ambito " 
			+ " WHERE rrdp.id_rata_sd = :idRataSd";
	
	public static final String QUERY_INSERT="INSERT INTO risca_r_dettaglio_pag "
			+ "(id_dettaglio_pag, id_rata_sd, importo_versato, interessi_maturati, id_pagamento,"
			+ "  gest_attore_ins, gest_data_ins,gest_attore_upd, gest_data_upd, "
			+ " gest_uid) "
			+ "VALUES(:idDettaglioPag, :idRataSd, :impVersato, :interessiMaturati, :idPagamento,"
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid) ";
	
	public static final String QUERY_SELECT_DETTAGLIO_PAG_BY_ID_RISCOSSIONE_ID_SD ="select "
			+ "	dp.*, rtp.* ,rdtmp.* , rda.* ,rsd.* "
			+ "	from  risca_t_stato_debitorio sd, "
			+ "	risca_r_rata_sd rsd, "
			+ "	risca_r_dettaglio_pag dp, "
			+ "	risca_t_pagamento rtp, "
			+ " risca_d_tipo_modalita_pag rdtmp,"			
			+ " risca_d_ambito rda "
			+ "	where rsd.id_stato_debitorio = sd.id_stato_debitorio "
			+ "	and dp.id_pagamento = rtp.id_pagamento "
			+ "	and sd.id_riscossione = :idRiscossione "
			+ "	and dp.id_rata_sd = rsd.id_rata_sd "
			+ "	and sd.id_stato_debitorio = :idStatoDebitorio "
			+ "	and rtp.id_ambito = rda.id_ambito "
			+ " and rtp.id_tipo_modalita_pag = rdtmp.id_tipo_modalita_pag "
			+ " order by rtp.data_op_val desc ";	

	public static final String QUERY_SELECT_DETTAGLIO_PAG_BY_ID_PAGAMENTO=" select "
			+ "dett.id_dettaglio_pag , prat.cod_riscossione , to_char(rata.data_scadenza_pagamento ,'YYYY') anno,"
			+ "	rata.canone_dovuto, dett.importo_versato , rata.data_scadenza_pagamento "
			+ "	FROM risca_r_dettaglio_pag dett,"
			+ "	risca_r_rata_sd  rata, risca_t_stato_debitorio sd,"
			+ "	risca_t_riscossione prat "
			+ "	WHERE dett.id_pagamento = :idPagamento "
			+ "	AND dett.id_rata_sd =rata.id_rata_sd "
			+ "	AND rata.id_rata_sd_padre  IS null "
			+ "	AND rata.id_stato_debitorio =sd.id_stato_debitorio "
			+ "	AND sd.id_riscossione =prat.id_riscossione ";   
	
	public static final String QUERY_UPDATE_IMP_VERSATO =" UPDATE risca_r_dettaglio_pag set"
			+ "	importo_versato = :importVersato, "
			+ "	gest_data_upd = :gestDataUpd ,"
			+ "	gest_attore_upd = :gestAttoreUpd "
			+ " WHERE id_dettaglio_pag = :idDettaglioPag ";
	
	public static final String QUERY_DELETE_DETTAGLIO_PAG = "DELETE from risca_r_dettaglio_pag WHERE id_dettaglio_pag = :idDettaglioPag";

	public static final String QUERY_SELECT_DETTAGLIO_PAG_BY_LIST_ID_PAGAMENTO = "select dett.* from risca_r_dettaglio_pag dett "
			+ " where dett.id_pagamento  in (:listIdPagamento) ";
	
	
	public static final String QUERY_UPDATE_DETTAGLIO_PAG_ANNULLAMENTO_SORIS = " UPDATE risca_r_dettaglio_pag set"
			+ "	importo_versato = 0, "
			+ "	gest_data_upd = :gestDataUpd ,"
			+ "	gest_attore_upd = :gestAttoreUpd "
			+ " WHERE id_pagamento = :idPagamento ";
	
	
	public static final String QUERY_SELECT_DETTAGLIO_PAG_BY_LIST_ID_RATA = " SELECT rdp.* FROM risca_r_dettaglio_pag rdp "
			+ " inner join risca_r_dettaglio_pag_bil_acc dpb on dpb.id_dettaglio_pag = rdp.id_dettaglio_pag "
			+ " where rdp.id_rata_sd in (:listIdRata) ";
	
	@Override
	public List<DettaglioPagExtendedDTO> getDettaglioPagByIdRate(long idRataSd) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRataSd", idRataSd);
			MapSqlParameterSource params = getParameterValue(map);
	
			return template.query(getQuery(QUERY_SELECT_DETTAGLIO_PAG_BY_ID_RATA, null, null), params,
					getExtendedRowMapper());
	
		} catch(EmptyResultDataAccessException e) {
			LOGGER.debug(getClassFunctionDebugString(className, methodName,"No record found in database for idRataSd "+ idRataSd ));
		    return Collections.emptyList();
		} catch (SQLException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
			throw new Exception(e);
		} catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
			throw new Exception(e);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));	
		}
	}
	
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public RowMapper<DettaglioPagDTO> getRowMapper() throws SQLException {
		return new DettaglioPagRowMapper();
	}
	
	@Override
	public RowMapper<DettaglioPagExtendedDTO> getExtendedRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new DettaglioPagExtendedRowMapper();
	}
	

	public RowMapper<DettaglioPagSearchResultDTO> DettaglioPagSearchResultRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new DettaglioPagSearchResultExtendedRowMapper();
	}
	public static class DettaglioPagSearchResultExtendedRowMapper implements RowMapper<DettaglioPagSearchResultDTO> {

		public DettaglioPagSearchResultExtendedRowMapper() throws SQLException {}

		@Override
		public DettaglioPagSearchResultDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			DettaglioPagSearchResultDTO bean = new DettaglioPagSearchResultDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, DettaglioPagSearchResultDTO bean) throws SQLException {
			
			bean.setIdDettaglioPag(rs.getLong("id_dettaglio_pag"));
			bean.setCodRiscossione(rs.getString("cod_riscossione"));
			bean.setAnno(rs.getInt("anno"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setCanonDovuto(rs.getBigDecimal("canone_dovuto"));		
			bean.setDataScadenzaPagamento(rs.getString("data_scadenza_pagamento"));	
			
		}
			
	}
	public static class DettaglioPagRowMapper implements RowMapper<DettaglioPagDTO> {

		public DettaglioPagRowMapper() throws SQLException {}

		@Override
		public DettaglioPagDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			DettaglioPagDTO bean = new DettaglioPagDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, DettaglioPagDTO bean) throws SQLException {
			bean.setIdPagamento(rs.getLong("id_pagamento"));
			bean.setIdRataSd(rs.getLong("id_rata_sd"));
			bean.setIdDettaglioPag(rs.getLong("id_dettaglio_pag"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setInteressiMaturati(rs.getBigDecimal("interessi_maturati"));		
			
		}
			
	}
	
	public static class DettaglioPagExtendedRowMapper implements RowMapper<DettaglioPagExtendedDTO> {

		public DettaglioPagExtendedRowMapper() throws SQLException {}

		@Override
		public DettaglioPagExtendedDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			DettaglioPagExtendedDTO bean = new DettaglioPagExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, DettaglioPagExtendedDTO bean) throws SQLException {
			bean.setIdPagamento(rs.getLong("id_pagamento"));
			bean.setIdRataSd(rs.getLong("id_rata_sd"));
			bean.setIdDettaglioPag(rs.getLong("id_dettaglio_pag"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setInteressiMaturati(rs.getBigDecimal("interessi_maturati"));
			PagamentoExtendedDTO pagamentoDTO = new PagamentoExtendedDTO();
			populateBeanPagamento(rs, pagamentoDTO);
			bean.setPagamento(pagamentoDTO);
			RataSdDTO rata = new RataSdDTO();
			populateBeanRata(rs, rata);
			bean.setRataSd(rata);
			
		}

	

	private void populateBeanRata(ResultSet rs, RataSdDTO bean) throws SQLException {
		bean.setIdRataSd(rs.getLong("id_rata_sd"));
		bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
		if(rs.getLong("id_rata_sd_padre") > 0)
	     	bean.setIdRataSdPadre(rs.getLong("id_rata_sd_padre"));
		bean.setCanoneDovuto(rs.getBigDecimal("canone_dovuto"));
		bean.setInteressiMaturati(rs.getBigDecimal("interessi_maturati"));
        SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
        if(rs.getDate("data_scadenza_pagamento") != null)
		   bean.setDataScadenzaPagamento(formatter.format(rs.getDate("data_scadenza_pagamento")));
	
		if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
		if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
		if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
		if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
		if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
		
	}
		private void populateBeanPagamento(ResultSet rs, PagamentoExtendedDTO bean) throws SQLException {
			TipoModalitaPagDTO tipoModalitaPag = new TipoModalitaPagDTO();
		    populateBeanTipoModalitaPag(rs,tipoModalitaPag);
		    bean.setTipoModalitaPag(tipoModalitaPag);
			bean.setIdPagamento(rs.getLong("id_pagamento"));
	        bean.setIdAmbito(rs.getLong("id_ambito"));
			bean.setIdTipologiaPag(rs.getLong("id_tipologia_pag"));
			bean.setIdTipoModalitaPag(rs.getLong("id_tipo_modalita_pag"));
		    if(rs.getLong("id_file_poste") > 0)
			  bean.setIdFilePoste(rs.getLong("id_file_poste"));
		    if(rs.getLong("id_immagine") > 0)
		    	bean.setIdImmagine(rs.getLong("id_immagine"));
			bean.setCausale(rs.getString("causale"));
			bean.setDataOpVal(rs.getDate("data_op_val"));
			bean.setImportoVersato(rs.getBigDecimal("importo_versato"));
			bean.setDataDownload(rs.getDate("data_download"));
			bean.setQuintoCampo(rs.getString("quinto_campo"));
			bean.setCro(rs.getString("cro"));
			bean.setNote(rs.getString("note"));
			bean.setNumeroPagamento(rs.getLong("numero_pagamento"));
			bean.setSoggettoVersamento(rs.getString("soggetto_versamento"));
			bean.setIndirizzoVersamento(rs.getString("indirizzo_versamento"));
			bean.setCivicoVersamento(rs.getString("civico_versamento"));
			bean.setFrazioneVersamento(rs.getString("frazione_versamento"));
			bean.setComuneVersamento(rs.getString("comune_versamento"));
			bean.setCapVersamento(rs.getString("cap_versamento"));
			bean.setProvVersamento(rs.getString("prov_versamento"));
			bean.setFlgRimborsato(rs.getInt("flg_rimborsato"));
			bean.setImpDaAssegnare(rs.getBigDecimal("imp_da_assegnare"));
			bean.setCodiceAvviso(rs.getString("codice_avviso"));
		    AmbitoDTO ambito = new AmbitoDTO();
	        populateBeanAmbito(rs, ambito);
	        bean.setAmbito(ambito);
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

		private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
         bean.setIdAmbito(rs.getLong("id_ambito"));
         bean.setCodAmbito(rs.getString("cod_ambito"));
         bean.setDesAmbito(rs.getString("des_ambito"));
			
		}
		private void populateBeanTipoModalitaPag(ResultSet rs, TipoModalitaPagDTO bean) throws SQLException {
     		bean.setIdTipoModalitaPag(rs.getLong("id_tipo_modalita_pag"));
			bean.setCodModalitaPag(rs.getString("cod_tipo_modalita_pag"));
			bean.setDesModalitaPag(rs.getString("des_tipo_modalita_pag"));
			
		}
		
	}

	@Override
	public DettaglioPagDTO saveDettaglioPag(DettaglioPagDTO dto) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRataSd", dto.getIdRataSd());
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Long genId = findNextSequenceValue("seq_risca_r_dettaglio_pag");
			map.put("idDettaglioPag", genId);
			map.put("idRataSd", dto.getIdRataSd());
			map.put("impVersato", dto.getImportoVersato());
			map.put("interessiMaturati", dto.getInteressiMaturati());
			map.put("idPagamento", dto.getIdPagamento());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid",  generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String query = QUERY_INSERT;
	
			template.update(getQuery(query, null, null), params, keyHolder);

			dto.setIdDettaglioPag(genId);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			return dto;
				
		} catch (SQLException e) {
			LOGGER.debug(getClassFunctionErrorInfo(className, methodName,e));
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
			}

	}

	@Override
	public List<DettaglioPagExtendedDTO> getDettaglioPagByIdRiscossioneAndIdSD(Long idRiscossione,
			Long idStatoDebitorio) throws DAOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idRiscossione", idRiscossione);
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);
	
			return template.query(getQuery(QUERY_SELECT_DETTAGLIO_PAG_BY_ID_RISCOSSIONE_ID_SD, null, null), params,
					getExtendedRowMapper());
	
		} catch(EmptyResultDataAccessException e) {
			LOGGER.debug(getClassFunctionDebugString(className, methodName,"No record found in database for idRiscossione "+ idRiscossione ));
		    return Collections.emptyList();
		}  catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
				throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));	
			}
	}

	@Override
	public List<DettaglioPagSearchResultDTO> getDettaglioPagByIdPagamento(Long idPagamento) throws DAOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<DettaglioPagSearchResultDTO> list = new ArrayList<>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idPagamento", idPagamento);
			MapSqlParameterSource params = getParameterValue(map);
	
			 list =  template.query(getQuery(QUERY_SELECT_DETTAGLIO_PAG_BY_ID_PAGAMENTO, null, null), params,
					DettaglioPagSearchResultRowMapper());
			for (DettaglioPagSearchResultDTO dettaglioPagSearchResultDTO : list) {
				Long idDettaglioPag = dettaglioPagSearchResultDTO.getIdDettaglioPag();
				DettaglioPagExtendedDTO dettaglioPag = getDettaglioPagById(idDettaglioPag);
				dettaglioPagSearchResultDTO.setDettaglioPag(dettaglioPag);
			}
			
	
		} catch(EmptyResultDataAccessException e) {
			LOGGER.debug(getClassFunctionDebugString(className, methodName,"No record found in database for idPagamento "+ idPagamento ));
		    return Collections.emptyList();
		}  catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
			}
		return list;
	}

	@Override
	public Long deleteDettaglioPagById(Long idDettaglioPag) throws DAOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idDettaglioPag", idDettaglioPag);

			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String queryDetPag = QUERY_DELETE_DETTAGLIO_PAG;

			template.update(getQuery(queryDetPag, null, null), params, keyHolder);

			return idDettaglioPag;
				
		}  catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		}  finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));	
			}
	}

	@Override
	public DettaglioPagExtendedDTO getDettaglioPagById(long idDettaglioPag) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idDettaglioPag", idDettaglioPag);
			MapSqlParameterSource params = getParameterValue(map);
			String queryDetPag = QUERY_SELECT_DETTAGLIO_PAG_BY_ID;

			return template.queryForObject(getQuery(queryDetPag, null, null), params,
					getExtendedRowMapper());
				
		}  catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
			throw new DAOException(Constants.ERRORE_GENERICO);
		}  finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}
	
	@Override
	public void updateDettaglioPagByListIdStatoDebSetInteressiMaturati(List<Long> listIdStatoDebitorio, BigDecimal interessiMaturati, String fruitore) throws DAOException
	{
		if (listIdStatoDebitorio == null || listIdStatoDebitorio.size() == 0) return;

		try
		{
			StringBuilder queryUpdate = new StringBuilder();
			queryUpdate.append(" update risca_r_dettaglio_pag  ");
			queryUpdate.append(" SET ");
			queryUpdate.append(" interessi_maturati = :interessiMaturati , gest_data_upd = CURRENT_TIMESTAMP , gest_attore_upd = :gestAttoreUpd ");
			queryUpdate.append("  where id_rata_sd  in( ");
			queryUpdate.append(" select id_rata_sd from risca_r_rata_sd,risca_t_stato_debitorio sd  ");
			queryUpdate.append(" where sd.id_stato_debitorio = risca_r_rata_sd.id_stato_debitorio  ");		
			queryUpdate.append(" and risca_r_rata_sd.id_stato_debitorio in(:listIdStatoDebitorio))");

			LOGGER.debug("[DettaglioPagDAOImpl::updateDettaglioPagByListIdStatoDebSetInteressiMaturati] queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	
	
			paramMap.put("listIdStatoDebitorio", listIdStatoDebitorio);
			paramMap.put("interessiMaturati", interessiMaturati);
			paramMap.put("gestAttoreUpd",fruitore);
	
			template.update(queryUpdate.toString(), paramMap);  

			LOGGER.debug("[DettaglioPagDAOImpl::updateDettaglioPagByListIdStatoDebSetInteressiMaturati]  Inserimento effettuato. Stato = SUCCESS ");
		}
		catch (DataIntegrityViolationException ex)
		{
			LOGGER.debug("[DettaglioPagDAOImpl::updateDettaglioPagByListIdStatoDebSetInteressiMaturati]  Integrity Keys Violation");
			throw new DAOException(ex.getMessage());
		}
		catch (Exception e) {
			LOGGER.error("[DettaglioPagDAOImpl::updateDettaglioPagByListIdStatoDebSetInteressiMaturati] ERROR : " +e);
			throw new DAOException(e.getMessage());
		}  finally {
			LOGGER.debug("[DettaglioPagDAOImpl::updateDettaglioPagByListIdStatoDebSetInteressiMaturati] END");
		} 
	}
	
	@Override
	public void updateDettaglioPagSetInteressiMaturatiByIdPagamentoAndIdRataStatoDeb(BigDecimal interessiMaturati, Long idPagamento, Long idRataStatoDeb, String fruitore) throws DAOException
	{
		try
		{
		      
			StringBuilder queryUpdate = new StringBuilder();
			queryUpdate.append(" update risca_r_dettaglio_pag  ");
			queryUpdate.append(" SET ");
			queryUpdate.append(" interessi_maturati = :interessiMaturati, gest_data_upd = CURRENT_TIMESTAMP , gest_attore_upd = :gestAttoreUpd ");
			queryUpdate.append("  where id_pagamento = :idPagamento and id_rata_sd = :idRataSD ");

			LOGGER.debug("[DettaglioPagDAOImpl::updateDettaglioPagSetInteressiMaturatiByIdPagamentoAndIdRataStatoDeb] queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	
	
			paramMap.put("interessiMaturati", interessiMaturati);
			paramMap.put("idPagamento", idPagamento);
			paramMap.put("idRataSD", idRataStatoDeb);
			paramMap.put("gestAttoreUpd", fruitore);
	
			template.update(queryUpdate.toString(), paramMap);  

			LOGGER.debug("[DettaglioPagDAOImpl::updateDettaglioPagSetInteressiMaturatiByIdPagamentoAndIdRataStatoDeb]  Inserimento effettuato. Stato = SUCCESS ");
		}
		catch (DataIntegrityViolationException ex)
		{
			LOGGER.debug("[DettaglioPagDAOImpl::updateDettaglioPagSetInteressiMaturatiByIdPagamentoAndIdRataStatoDeb]  Integrity Keys Violation");
			throw new DAOException(ex.getMessage());
		}
		catch (Exception e) {
			LOGGER.error("[DettaglioPagDAOImpl::updateDettaglioPagSetInteressiMaturatiByIdPagamentoAndIdRataStatoDeb] ERROR : " +e);
			throw new DAOException(e.getMessage());
		}  finally {
			LOGGER.debug("[DettaglioPagDAOImpl::updateDettaglioPagSetInteressiMaturatiByIdPagamentoAndIdRataStatoDeb] END");
		} 
	}

	@Override
	public DettaglioPagExtendedDTO updateDettaglioPag(DettaglioPagExtendedDTO dto) {
		   String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			try {
				Map<String, Object> map = new HashMap<>();
				Calendar cal = Calendar.getInstance();
				Date now = cal.getTime();
				map.put("idDettaglioPag", dto.getIdDettaglioPag());
				map.put("importVersato", dto.getImportoVersato());
				map.put("gestAttoreUpd", dto.getGestAttoreUpd());
				map.put("gestDataUpd", now);
				MapSqlParameterSource params = getParameterValue(map);
				KeyHolder keyHolder = new GeneratedKeyHolder();
				String query = QUERY_UPDATE_IMP_VERSATO;
				template.update(getQuery(query, null, null), params, keyHolder);
				dto.setGestDataUpd(now);
				return dto;
					
			} catch (DataAccessException e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw e;
			} finally {
				LOGGER.debug(getClassFunctionEndInfo(className, methodName));
			}
		
	}
	
	@Override
	public List<DettaglioPagDTO> getDettagliPagByListaIdPag(List<Long> listIdPagamento) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<DettaglioPagDTO> list = new ArrayList<>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("listIdPagamento", listIdPagamento);
			MapSqlParameterSource params = getParameterValue(map);
			list = template.query(getQuery(QUERY_SELECT_DETTAGLIO_PAG_BY_LIST_ID_PAGAMENTO, null, null), params,
					getRowMapper());

		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			return null;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return list;
	}
	
	@Override
	public List<DettaglioPagDTO> getDettagliPagByListaIdRata(List<Long> listIdRata) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<DettaglioPagDTO> list = new ArrayList<>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("listIdRata", listIdRata);
			MapSqlParameterSource params = getParameterValue(map);
			list = template.query(getQuery(QUERY_SELECT_DETTAGLIO_PAG_BY_LIST_ID_RATA, null, null), params,
					getRowMapper());

		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			return null;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return list;
	}
	
	@Override
	public DettaglioPagDTO updateDettaglioPagAnnullamentoSoris(DettaglioPagDTO dto) {
		   String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName)+ " idPagamento: "+dto.getIdPagamento());
			LOGGER.debug(getClassFunctionBeginInfo(className, methodName)+ " GestAttoreUpd: "+dto.getGestAttoreUpd());
			try {
				Map<String, Object> map = new HashMap<>();
				Calendar cal = Calendar.getInstance();
				Date now = cal.getTime();
				map.put("idPagamento", dto.getIdPagamento());
				map.put("gestAttoreUpd", dto.getGestAttoreUpd());
				map.put("gestDataUpd", now);
				MapSqlParameterSource params = getParameterValue(map);
				KeyHolder keyHolder = new GeneratedKeyHolder();
				String query = QUERY_UPDATE_DETTAGLIO_PAG_ANNULLAMENTO_SORIS;
				template.update(getQuery(query, null, null), params, keyHolder);
				dto.setGestDataUpd(now);
				return dto;
					
			} catch (DataAccessException e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw e;
			} finally {
				LOGGER.debug(getClassFunctionEndInfo(className, methodName));
			}
		
	}
	
}
