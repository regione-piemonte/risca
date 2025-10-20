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
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.GenericException;
import it.csi.risca.riscabesrv.business.be.impl.dao.ProvvedimentiIstanzeDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.ProvvedimentoDTO;
import it.csi.risca.riscabesrv.dto.TipiProvvedimentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipiTitoloExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Provvedimenti Istanze DAO Impl.
 *
 * @author CSI PIEMONTE
 */

public class ProvvedimentiIstanzeDAOImpl extends RiscaBeSrvGenericDAO<ProvvedimentoDTO> implements ProvvedimentiIstanzeDAO{

	private final String className = this.getClass().getSimpleName();

	private static final String QUERY_PROVVEDIMENTI_ISTANZA = "SELECT rrp.*, rdtt.*, rdtp.*, rda.*, rda.id_ambito as idAmbito FROM RISCA_R_PROVVEDIMENTO rrp "
			+ "inner join risca_d_tipo_provvedimento rdtp  on rrp.id_tipo_provvedimento = rdtp.id_tipo_provvedimento "
			+ "inner join risca_d_ambito rda on rdtp.id_ambito  = rda.id_ambito "
           	+ "left  join risca_d_tipo_titolo rdtt on rrp.id_tipo_titolo = rdtt.id_tipo_titolo ";
	
	private static final String QUERY_PROVVEDIMENTI_ISTANZA_BY_ID_RISCOSSIONE = QUERY_PROVVEDIMENTI_ISTANZA 
			+ " WHERE rrp.id_riscossione = :idRiscossione";
	
	private static final String QUERY_PROVVEDIMENTI_ISTANZA_BY_ID_PROVVEDIMENTO = QUERY_PROVVEDIMENTI_ISTANZA 
			+ " WHERE rrp.id_provvedimento = :idProvvedimento";
	
	private static final String QUERY_INSERT_PROVVEDIMENTO = "INSERT INTO risca_r_provvedimento (id_provvedimento, id_riscossione, id_tipo_titolo, id_tipo_provvedimento, num_titolo, data_provvedimento, "
			+ "note, gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
			+ "VALUES(nextval('seq_risca_r_provvedimento'), :idRiscossione, :idTipoTitolo, :idTipoProvvedimento, :numTitolo, :dataProvvedimento, :note, "
			+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";
	private static final String QUERY_UPDATE_PROVVEDIMENTO  = "UPDATE risca_r_provvedimento "
			+ "SET id_riscossione= :idRiscossione,id_tipo_titolo =:idTipoTitolo, id_tipo_provvedimento = :idTipoProvvedimento, num_titolo = :numTitolo, data_provvedimento = :dataProvvedimento,  "
			+ "note =:note,  gest_data_upd= :gestDataUpd, gest_attore_upd= :gestAttoreUpd, gest_uid = :gestUid "
			+ "WHERE id_provvedimento = :idProvvedimento";

	private static final String QUERY_DELETE_PROVVEDIMENTO = "DELETE FROM risca_r_provvedimento rrp WHERE rrp.id_provvedimento = :idProvvedimentiIstanze ";
	
	private static final String QUERY_PROVVEDIMENTO_LEGATO_SD = QUERY_PROVVEDIMENTI_ISTANZA
			+ " join RISCA_T_STATO_DEBITORIO rtsd on rrp.id_provvedimento = rtsd.id_provvedimento"
			+ " WHERE rrp.id_provvedimento = :idProvvedimentiIstanze ";
	
	private static final String QUERY_DELETE_PROVVVEDIMENTO_BY_ID_RISCOSSIONE = "DELETE FROM risca_r_provvedimento rrp WHERE rrp.id_riscossione = :idRiscossione ";

	@Override
	public List<ProvvedimentoDTO> getProvvedimentiIstanze() throws SQLException {
		LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::getProvvedimentiIstanze] BEGIN");
		List<ProvvedimentoDTO> listProvvedimentoDTO = new ArrayList<ProvvedimentoDTO>();
		try {
			return  template.query(QUERY_PROVVEDIMENTI_ISTANZA,  getRowMapper()); 
		} catch (SQLException e) {
			LOGGER.error("[ProvvedimentiIstanzeDAOImpl::getProvvedimentiIstanze] Errore nell'esecuzione della query", e);
	    	throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[ProvvedimentiIstanzeDAOImpl::getProvvedimentiIstanze] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::getProvvedimentiIstanze] END");
		}
	}
	
	
	@Override
	public List<ProvvedimentoDTO>  getProvvedimentiIstanzeByidRiscossione(Long idRiscossione) throws SQLException {
		
		LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::getProvvedimentiIstanzeByidRiscossione] BEGIN");
		List<ProvvedimentoDTO> listProvvedimentoDTO = new ArrayList<ProvvedimentoDTO>();
        Map<String, Object> map = new HashMap<>();
        map.put("idRiscossione", idRiscossione);
        MapSqlParameterSource params = getParameterValue(map);
		try {
			return  template.query(QUERY_PROVVEDIMENTI_ISTANZA_BY_ID_RISCOSSIONE, params, getRowMapper()); 
		} catch (SQLException e) {
			LOGGER.error("[ProvvedimentiIstanzeDAOImpl::getProvvedimentiIstanzeByidRiscossione] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[ProvvedimentiIstanzeDAOImpl::getProvvedimentiIstanzeByidRiscossione] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::getProvvedimentiIstanzeByidRiscossione] END");
		}
	}
	
	@Override
	public ProvvedimentoDTO getProvvedimentoIstanzaByIdProvvedimenti(Long idProvvedimentiIstanze) throws SQLException {
		LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::getProvvedimentoIstanzaByIdProvvedimenti] BEGIN");
		ProvvedimentoDTO provvedimentoDTO = new ProvvedimentoDTO();
        Map<String, Object> map = new HashMap<>();
        map.put("idProvvedimento", idProvvedimentiIstanze);
        MapSqlParameterSource params = getParameterValue(map);
		try {
			return  template.queryForObject(QUERY_PROVVEDIMENTI_ISTANZA_BY_ID_PROVVEDIMENTO, params, getRowMapper()); 
		} catch (SQLException e) {
			LOGGER.error("[ProvvedimentiIstanzeDAOImpl::getProvvedimentoIstanzaByIdProvvedimenti] Errore nell'esecuzione della query", e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error("[ProvvedimentiIstanzeDAOImpl::getProvvedimentoIstanzaByIdProvvedimenti] Errore nell'accesso ai dati", e);
			throw e;
		} finally {
			LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::getProvvedimentoIstanzaByIdProvvedimenti] END");
		}
	}

	
	@Override
	public Long saveProvvedimentiIstanze(ProvvedimentoDTO provvedimentoDTO) throws Exception {
		LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::saveProvvedimentiIstanze] BEGIN");
        Map<String, Object> map = new HashMap<>();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			
			map.put("idRiscossione", provvedimentoDTO.getIdRiscossione());
			map.put("idTipoTitolo", provvedimentoDTO.getTipoTitoloExtendedDTO()!= null ? provvedimentoDTO.getTipoTitoloExtendedDTO().getIdTipoTitolo() : null);
			map.put("idTipoProvvedimento", provvedimentoDTO.getTipiProvvedimentoExtendedDTO().getIdTipoProvvedimento());
			map.put("numTitolo", provvedimentoDTO.getNumTitolo());

			String dataProvvedimento = provvedimentoDTO.getDataProvvedimento();
			if (dataProvvedimento != null) {
				if (!dataProvvedimento.equals("")) {
					Date dataProvv;
					try {
						dataProvv = formatter.parse(dataProvvedimento);
					} catch (ParseException e) {
						LOGGER.error("[ProvvedimentiIstanzeDAOImpl::saveProvvedimentiIstanze] Errore nell'parse data ", e);
						return null;
					}
					map.put("dataProvvedimento", dataProvv);
				} else {
					map.put("dataProvvedimento", null);
				}
			} else {
				map.put("dataProvvedimento", null);
			}

			map.put("note", provvedimentoDTO.getNote());
			map.put("gestDataIns", now);
			map.put("gestAttoreIns", provvedimentoDTO.getGestAttoreIns());
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", provvedimentoDTO.getGestAttoreUpd());
			map.put("gestUid", generateGestUID(provvedimentoDTO.getGestAttoreIns() + provvedimentoDTO.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			template.update(getQuery(QUERY_INSERT_PROVVEDIMENTO, null, null), params, keyHolder,
					new String[] { "id_provvedimento" });
			Number key = keyHolder.getKey();
			return key.longValue();
		}  catch (DataAccessException e) {
			LOGGER.error("[ProvvedimentiIstanzeDAOImpl::saveProvvedimentiIstanze] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::saveProvvedimentiIstanze] END");
		}
	}
	
	@Override
	public Long updateProvvedimentiIstanze(ProvvedimentoDTO provvedimentoDTO) throws Exception {
		LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::updateProvvedimentiIstanze] BEGIN");
        Map<String, Object> map = new HashMap<>();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			map.put("idProvvedimento", provvedimentoDTO.getIdProvvedimento());
			map.put("idRiscossione", provvedimentoDTO.getIdRiscossione());
			map.put("idTipoTitolo", provvedimentoDTO.getTipoTitoloExtendedDTO()!= null ? provvedimentoDTO.getTipoTitoloExtendedDTO().getIdTipoTitolo() : null);
			map.put("idTipoProvvedimento", provvedimentoDTO.getTipiProvvedimentoExtendedDTO().getIdTipoProvvedimento());
			map.put("numTitolo", provvedimentoDTO.getNumTitolo());

			String dataProvvedimento = provvedimentoDTO.getDataProvvedimento();
			if (dataProvvedimento != null) {
				if (!dataProvvedimento.equals("")) {
					Date dataProvv;
					try {
						dataProvv = formatter.parse(dataProvvedimento);
					} catch (ParseException e) {
						LOGGER.error("[ProvvedimentiIstanzeDAOImpl::updateProvvedimentiIstanze] Errore nell'parse data ", e);
						return null;
					}
					map.put("dataProvvedimento", dataProvv);
				} else {
					map.put("dataProvvedimento", null);
				}
			} else {
				map.put("dataProvvedimento", null);
			}

			map.put("note", provvedimentoDTO.getNote());
			map.put("gestDataUpd", now);
			map.put("gestAttoreUpd", provvedimentoDTO.getGestAttoreUpd());
			map.put("gestUid", generateGestUID(provvedimentoDTO.getGestAttoreIns() + provvedimentoDTO.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			

			int num = template.update(getQuery(QUERY_UPDATE_PROVVEDIMENTO, null, null), params);
			
			return (long) num;
		}  catch (DataAccessException e) {
			LOGGER.error("[ProvvedimentiIstanzeDAOImpl::updateProvvedimentiIstanze] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::updateProvvedimentiIstanze] END");
		}
	}

	@Override
	public ProvvedimentoDTO deleteProvvedimentiIstanze(Long idProvvedimentiIstanze) throws GenericException, SQLException {
		LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::deleteProvvedimentiIstanze] BEGIN");

	try {
		ProvvedimentoDTO provvedimentoDTO = getProvvedimentoIstanzaByIdProvvedimenti (idProvvedimentiIstanze) ;
		Map<String, Object> map = new HashMap<>();
		map.put("idProvvedimentiIstanze", idProvvedimentiIstanze);
		MapSqlParameterSource params = getParameterValue(map);
//		List<ProvvedimentoDTO> listProvvedimento = template.query(QUERY_PROVVEDIMENTO_LEGATO_SD, params, getRowMapper()); 
//		if(!listProvvedimento.isEmpty()) {
//			ErrorObjectDTO error = new ErrorObjectDTO("400", "E081", "Attenzione: Il provvedimento e' legato ad uno stato debitorio. Impossibile procedere con l'eliminazione", null, null,null);
//			LOGGER.info("[ProvvedimentiIstanzeDAOImpl::deleteProvvedimentiIstanze] Il provvedimento e' legato ad uno stato debitorio");
//			throw new GenericException(error);
//		}
		template.update(getQuery(QUERY_DELETE_PROVVEDIMENTO, null, null), params);
		LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::deleteProvvedimentiIstanze] END");
		return provvedimentoDTO;
	} catch (DataAccessException e) {
    	LOGGER.error("[ProvvedimentiIstanzeDAOImpl::deleteProvvedimentiIstanze] Errore nell'accesso ai dati", e);
    	throw e;
    } catch (SQLException e) {
    	LOGGER.error("[ProvvedimentiIstanzeDAOImpl::deleteProvvedimentiIstanze] Errore nella query ", e);
    	throw e;
	} finally {
        LOGGER.debug("[ProvvedimentiIstanzeDAOImpl::deleteProvvedimentiIstanze] END");
    }
	}

	
	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<ProvvedimentoDTO> getRowMapper() throws SQLException {
		return new ProvvedimentoRowMapper();
	}

	/**
	 * The type Provvedimento Row Mapper.
	 */
	public class ProvvedimentoRowMapper implements RowMapper<ProvvedimentoDTO> {

		@Override
		public ProvvedimentoDTO mapRow(ResultSet rs, int arg1) throws SQLException {
			ProvvedimentoDTO bean = new ProvvedimentoDTO();
            populateBeanExtended(rs, bean);
            return bean;

		}

		private void populateBeanExtended(ResultSet rs, ProvvedimentoDTO bean) throws SQLException {
	
			//bean.setIdTipoProvvedimento(rs.getLong("id_tipo_provvedimento"));
			//bean.setIdTipoTitolo(rs.getLong("id_tipo_titolo"));
			bean.setIdProvvedimento(rs.getLong("id_provvedimento"));
			bean.setIdRiscossione(rs.getLong("id_riscossione"));
			bean.setNumTitolo(rs.getString("num_titolo"));
			bean.setDataProvvedimento(rs.getString("data_provvedimento"));
			bean.setNote(rs.getString("note"));
			
			bean.setGestDataIns(rs.getDate("gest_data_ins"));
			bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			bean.setGestUid(rs.getString("gest_uid"));
            TipiTitoloExtendedDTO tipoTitolo = new TipiTitoloExtendedDTO();
            populateBeanTipoTitolo(rs, tipoTitolo);
            bean.setTipoTitoloExtendedDTO(tipoTitolo);
            
            TipiProvvedimentoExtendedDTO tipoProvvedimento = new TipiProvvedimentoExtendedDTO();
            populateBeanTipoProvvedimento(rs, tipoProvvedimento);
            bean.setTipiProvvedimentoExtendedDTO(tipoProvvedimento);
		}
		
        private void populateBeanTipoTitolo(ResultSet rs, TipiTitoloExtendedDTO bean) throws SQLException {
        	if(rs.getLong("id_tipo_titolo") > 0L) {
	        	bean.setIdTipoTitolo(rs.getLong("id_tipo_titolo"));
	//            bean.setIdAmbito(rs.getLong("id_ambito"));
	            AmbitoDTO ambito = new AmbitoDTO();
	            populateBeanAmbito(rs, ambito);
	            bean.setAmbito(ambito);
	            bean.setCodTipoTitolo(rs.getString("cod_tipo_titolo"));
	            bean.setDesTipoTitolo(rs.getString("des_tipo_titolo"));
        	}
            
        }
        
        private void populateBeanTipoProvvedimento(ResultSet rs, TipiProvvedimentoExtendedDTO bean) throws SQLException {
        	bean.setIdTipoProvvedimento(rs.getLong("id_tipo_provvedimento"));
//            bean.setIdAmbito(rs.getLong("id_ambito"));
            AmbitoDTO ambito = new AmbitoDTO();
            populateBeanAmbito(rs, ambito);
            bean.setAmbito(ambito);
            bean.setCodTipoProvvedimento(rs.getString("cod_tipo_provvedimento"));
            bean.setDesTipoProvvedimento(rs.getString("des_tipo_provvedimento"));
            bean.setFlgIstanza(rs.getString("flg_istanza"));
        }
        
        private void populateBeanAmbito(ResultSet rs, AmbitoDTO bean) throws SQLException {
    
                bean.setIdAmbito(rs.getLong("idAmbito"));
                bean.setCodAmbito(rs.getString("cod_ambito"));
                bean.setDesAmbito(rs.getString("des_ambito"));

        }

	}
	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}


	@Override
	@Transactional
	public void deleteProvvedimentiIstanzeByIdRiscossione(Long idRiscossione) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	try {
		Map<String, Object> map = new HashMap<>();
		map.put("idRiscossione", idRiscossione);
		MapSqlParameterSource params = getParameterValue(map);
		template.update(getQuery(QUERY_DELETE_PROVVVEDIMENTO_BY_ID_RISCOSSIONE, null, null), params);
	} catch (DataAccessException e) {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		LOGGER.debug(getClassFunctionErrorInfo(className, methodName, e));
		throw e;
	} finally {
		LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	}
		
	}


	



}
