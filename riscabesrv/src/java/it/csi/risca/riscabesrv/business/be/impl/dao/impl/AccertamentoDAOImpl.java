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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AccertamentoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DettaglioPagDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RataSdDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper.AccertamentoDaoRowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper.RecapitoPostelDaoRowMapper;
import it.csi.risca.riscabesrv.dto.AccertamentoDTO;
import it.csi.risca.riscabesrv.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.DettaglioPagExtendedDTO;
import it.csi.risca.riscabesrv.dto.RataSdDTO;
import it.csi.risca.riscabesrv.dto.RimborsoExtendedDTO;
import it.csi.risca.riscabesrv.dto.TableColumnDTO;
import it.csi.risca.riscabesrv.dto.TipiAccertamentoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Accertamento dao.
 *
 * @author CSI PIEMONTE
 */
public class AccertamentoDAOImpl extends RiscaBeSrvGenericDAO<AccertamentoDTO> implements AccertamentoDAO {

	private final String className = this.getClass().getSimpleName();


	@Autowired
	private RataSdDAO rataSdDAO;
	
	
	@Autowired
	private RimborsoDAO rimborsoDAO;
	
	@Autowired
	private DettaglioPagDAO dettaglioPagDAO;
											
	public static final String QUERY_INSERT = "INSERT INTO risca_t_accertamento "
			+ "(id_accertamento, id_stato_debitorio, id_tipo_accertamento, id_file_450, num_protocollo, data_protocollo,data_scadenza, "
			+ "flg_restituito, flg_annullato, data_notifica, nota,gest_attore_ins,  gest_data_ins, gest_attore_upd, gest_data_upd, "
			+ " gest_uid, id_spedizione) "
			+ "VALUES(:idAccertamento,:idStatoDebitorio , :idTipoAccertamento, :idFile450, :numProtocollo, :dataProtocollo, :dataScadenza, "
			+ ":flgRestituito, :flgAnnullato, :dataNotifica, :nota,  "
			+ ":gestAttoreIns, :gestDataIns, :gestAttoreUpd, :gestDataUpd, :gestUid, :idSpedizione) ";
	
	public static final String QUERY_INSERT_W = "INSERT INTO risca_w_accertamento "
			+ "(id_accertamento, id_stato_debitorio, id_tipo_accertamento, id_file_450, id_elabora, num_protocollo, data_protocollo,data_scadenza, "
			+ "flg_restituito, flg_annullato, data_notifica, nota, id_spedizione) "
			+ "VALUES(:idAccertamento,:idStatoDebitorio , :idTipoAccertamento, :idFile450, :idElabora, :numProtocollo, :dataProtocollo, :dataScadenza, "
			+ ":flgRestituito, :flgAnnullato, :dataNotifica, :nota, :idSpedizione) ";
	
	public static final String QUERY_UPDATE = "update risca_t_accertamento "
			+ " set id_stato_debitorio = :idStatoDebitorio, id_tipo_accertamento = :idTipoAccertamento, id_file_450 =:idFile450, "
			+ "num_protocollo = :numProtocollo, data_protocollo =:dataProtocollo, "
			+ "data_scadenza = :dataScadenza, flg_restituito = :flgRestituito, flg_annullato = :flgAnnullato, "
			+ "data_notifica = :dataNotifica, nota = :nota, "
			+ " gest_data_upd = :gestDataUpd , "
			+ "gest_attore_upd= :gestAttoreUpd "
			+ " where id_accertamento  = :idAccertamento ";
	
	public static final String QUERY_DELETE = "DELETE FROM  risca_t_accertamento WHERE id_stato_debitorio = :idStatoDebitorio";
	
	public static final String QUERY_DELETE_BY_ID_ACCERTAMENTO = "DELETE FROM  risca_t_accertamento WHERE id_accertamento = :idAccertamento";
	
	public static final String QUERY_DELETE_WORKING_BY_ELABORA = "DELETE FROM risca_w_accertamento WHERE id_elabora = :idElabora ";

	public static final String QUERY_SELECT_ALL_ACCERTAMENTI = "select  rta.*, rdta.* from RISCA_T_ACCERTAMENTO rta " + 
			"left join risca_d_tipo_accertamento rdta on rta.id_tipo_accertamento = rdta.id_tipo_accertamento ";
//			+" left join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
//			+" left join risca_r_dettaglio_pag rrdp on rrrs.id_rata_sd = rrdp.id_rata_sd "
//			+" left join risca_t_pagamento rtp on rrdp.id_pagamento = rtp.id_pagamento "
//			+ "	group by rtsd.id_stato_debitorio,	rtsd.id_riscossione,	rtsd.id_provvedimento,	rtsd.id_soggetto,"
//			+ "	rtsd.id_gruppo_soggetto,	rtsd.id_recapito,	rtsd.id_attivita_stato_deb,	rtsd.id_stato_contribuzione,"
//			+ "	rtsd.id_tipo_dilazione,	rtsd.num_titolo,	rtsd.data_provvedimento,	rtsd.num_richiesta_protocollo,"
//			+ "	rtsd.data_richiesta_protocollo,	rtsd.data_ultima_modifica,	rtsd.des_usi	,rtsd.note,	rtsd.imp_recupero_canone,"
//			+ "	rtsd.imp_recupero_interessi,	rtsd.imp_spese_notifica,	rtsd.imp_compensazione_canone,"
//			+ "	rtsd.desc_periodo_pagamento,	rtsd.desc_motivo_annullo,	rtsd.flg_annullato,"
//			+ "	rtsd.flg_restituito_mittente	,rtsd.flg_invio_speciale	,rtsd.flg_dilazione	,rtsd.flg_addebito_anno_successivo	"
//			+ "	,rtsd.gest_data_ins,"
//			+ "	rtsd.gest_attore_ins	,rtsd.gest_data_upd	,rtsd.gest_attore_upd,	rtsd.gest_uid,	rtsd.nap	,rtsd.nota_rinnovo";
	
	
	public static final String QUERY_SELECT_ACCERTAMENTI_BY_ID_STATO_DEBITORIO = "select  rta.*, rdta.* from RISCA_T_ACCERTAMENTO rta " + 
			"left join risca_d_tipo_accertamento rdta on rta.id_tipo_accertamento = rdta.id_tipo_accertamento " + 
			"where rta.id_stato_debitorio = :idStatoDebitorio";
//			+ " left join risca_r_rata_sd rrrs on rtsd.id_stato_debitorio = rrrs.id_stato_debitorio "
//			+ " left join risca_r_dettaglio_pag rrdp on rrrs.id_rata_sd = rrdp.id_rata_sd "
//			+ " left join risca_t_pagamento rtp on rrdp.id_pagamento = rtp.id_pagamento "
//			+ " where rta.id_stato_debitorio = :idStatoDebitorio"
//			+ "	group by rtsd.id_stato_debitorio,	rtsd.id_riscossione,	rtsd.id_provvedimento,	rtsd.id_soggetto,"
//			+ "	rtsd.id_gruppo_soggetto,	rtsd.id_recapito,	rtsd.id_attivita_stato_deb,	rtsd.id_stato_contribuzione,"
//			+ "	rtsd.id_tipo_dilazione,	rtsd.num_titolo,	rtsd.data_provvedimento,	rtsd.num_richiesta_protocollo,"
//			+ "	rtsd.data_richiesta_protocollo,	rtsd.data_ultima_modifica,	rtsd.des_usi	,rtsd.note,	rtsd.imp_recupero_canone,"
//			+ "	rtsd.imp_recupero_interessi,	rtsd.imp_spese_notifica,	rtsd.imp_compensazione_canone,"
//			+ "	rtsd.desc_periodo_pagamento,	rtsd.desc_motivo_annullo,	rtsd.flg_annullato,"
//			+ "	rtsd.flg_restituito_mittente	,rtsd.flg_invio_speciale	,rtsd.flg_dilazione	,rtsd.flg_addebito_anno_successivo	"
//			+ "	,rtsd.gest_data_ins,"
//			+ "	rtsd.gest_attore_ins	,rtsd.gest_data_upd	,rtsd.gest_attore_upd,	rtsd.gest_uid,	rtsd.nap	,rtsd.nota_rinnovo";

	public static final String QUERY_COPY_ACCERTAMENTI_FROM_WORKING = " INSERT INTO RISCA_T_ACCERTAMENTO( "
			+ " id_accertamento, id_stato_debitorio, id_tipo_accertamento, "
			+ " num_protocollo, data_protocollo, data_scadenza,flg_restituito, "
			+ " flg_annullato, data_notifica, id_file_450, id_spedizione, "
			+ " gest_data_ins, gest_attore_ins, gest_data_upd , gest_attore_upd , gest_uid) "
			+ " SELECT "
			+ " id_accertamento, id_stato_debitorio, id_tipo_accertamento, "
			+ " num_protocollo, data_protocollo, data_scadenza, flg_restituito, "
			+ " flg_annullato, data_notifica, id_file_450, id_spedizione, "
			+ " :gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid "
			+ " FROM RISCA_W_ACCERTAMENTO b "
			+ " WHERE b.id_elabora = :idElabora ";
	
	public static final String QUERY_SELECT_ACCERTAMENTI_BY_ID_ACCERTAMENTO = "select  rta.*, rdta.* from RISCA_T_ACCERTAMENTO rta " + 
			" left join risca_d_tipo_accertamento rdta on rta.id_tipo_accertamento = rdta.id_tipo_accertamento " + 
			" where rta.id_accertamento  = :idAccertamento ";
	
	@Override
	public AccertamentoExtendedDTO saveAccertamenti(AccertamentoExtendedDTO dto) throws DAOException {
		LOGGER.debug("[AccertamentoDAOImpl::saveAccertamenti] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();	
			Long genId = findNextSequenceValue("seq_risca_t_accertamento");
			map.put("idAccertamento", genId);
			map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
	        map.put("idTipoAccertamento", dto.getTipoAccertamento().getIdTipoAccertamento());
	        map.put("idFile450", dto.getIdFile450());
	        map.put("numProtocollo", dto.getNumProtocollo());
	        map.put("dataProtocollo", dto.getDataProtocollo());
	        map.put("dataScadenza", dto.getDataScadenza());
	        map.put("flgAnnullato", dto.getFlgAnnullato());
	        map.put("flgRestituito", dto.getFlgRestituito());
	        map.put("dataNotifica", dto.getDataNotifica());
	        map.put("nota", dto.getNota());
	        map.put("idSpedizione", dto.getIdSpedizione());
			map.put("gestAttoreIns", dto.getGestAttoreIns());
			map.put("gestDataIns", now);
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_INSERT;
			template.update(getQuery(query, null, null), params);
			dto.setIdAccertamento(genId);
			dto.setGestDataIns(now);
			dto.setGestDataUpd(now);
			return dto;
		} catch (Exception e) {
			LOGGER.error("[AccertamentoDAOImpl::saveAccertamenti] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AccertamentoDAOImpl::saveAccertamenti] END");
		}
	}
	@Override
	public AccertamentoExtendedDTO updateAccertamenti(AccertamentoExtendedDTO dto) throws DAOException {
		LOGGER.debug("[AccertamentoDAOImpl::updateAccertamenti] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();	
			map.put("idAccertamento", dto.getIdAccertamento());
			map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
	        map.put("idTipoAccertamento", dto.getTipoAccertamento().getIdTipoAccertamento());
	        map.put("idFile450", dto.getIdFile450());
	        map.put("numProtocollo", dto.getNumProtocollo());
	        map.put("dataProtocollo", dto.getDataProtocollo());
	        map.put("dataScadenza", dto.getDataScadenza());
	        map.put("flgAnnullato", dto.getFlgAnnullato());
	        map.put("flgRestituito", dto.getFlgRestituito());
	        map.put("dataNotifica", dto.getDataNotifica());
	        map.put("nota", dto.getNota());
			map.put("gestAttoreUpd", dto.getGestAttoreUpd());
			map.put("gestDataUpd", now);
			map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_UPDATE;
			template.update(getQuery(query, null, null), params);
			dto.setGestDataUpd(now);
			return dto;
		} catch (Exception e) {
			LOGGER.error("[AccertamentoDAOImpl::updateAccertamenti] ERROR : ",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AccertamentoDAOImpl::updateAccertamenti] END");
		}
	}
	
	@Override
	public void deleteAccertamentoByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException {
		LOGGER.debug("[AccertamentoDAOImpl::deleteAccertamentoByIdStatoDebitorio] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idStatoDebitorio", idStatoDebitorio);
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_DELETE;
			template.update(getQuery(query, null, null), params);
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::deleteAccertamentoByIdStatoDebitorio] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AccertamentoDAOImpl::deleteAccertamentoByIdStatoDebitorio] END");
		}
	}
	
	@Override
	public void deleteAccertamentoById(Long idAccertamento) throws DAOException {
		LOGGER.debug("[AccertamentoDAOImpl::deleteAccertamentoById] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAccertamento", idAccertamento);
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_DELETE_BY_ID_ACCERTAMENTO;
			template.update(getQuery(query, null, null), params);
		} catch (Exception e) {
			LOGGER.error("[StatoDebitorioDAOImpl::deleteAccertamentoById] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AccertamentoDAOImpl::deleteAccertamentoById] END");
		}
	}

	
	@Override
	public List<AccertamentoExtendedDTO> loadAllAccertamentiOrByIdStatoDeb(Long idStatoDebitorio,Integer offset,Integer limit,String sort) throws DAOException {
		LOGGER.debug("[AccertamentoDAOImpl::loadAllAccertamentiOrByIdStatoDeb] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<AccertamentoExtendedDTO> res = new ArrayList<AccertamentoExtendedDTO>();
        MapSqlParameterSource paramST = null;
		try {
			String dynamicOrderByCondition="";
			if (StringUtils.isNotBlank(sort)) {
			   dynamicOrderByCondition = mapSortConCampiDB(sort);

			}		
			if(idStatoDebitorio == null)
				res =  template.query(getQuery(QUERY_SELECT_ALL_ACCERTAMENTI + dynamicOrderByCondition,  offset!= null ? offset.toString(): null ,  limit != null ? limit.toString(): null), paramST, getExtendedRowMapper());
			
			
			else if(idStatoDebitorio != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("idStatoDebitorio", idStatoDebitorio);
				paramST = getParameterValue(map);
				res =  template.query(getQuery(QUERY_SELECT_ACCERTAMENTI_BY_ID_STATO_DEBITORIO + dynamicOrderByCondition,  offset!= null ? offset.toString(): null ,  limit != null ? limit.toString(): null), paramST, getExtendedRowMapper());
				
			}			
			if(!res.isEmpty()) {
				
				BigDecimal importoDovuto = BigDecimal.ZERO;
				String dataScadenzaPag;
				BigDecimal interessiDovuti = BigDecimal.ZERO;
				BigDecimal importoVersato= BigDecimal.ZERO;
				BigDecimal importoDiCanoneMancante= BigDecimal.ZERO;
				BigDecimal interessiMancanti= BigDecimal.ZERO;
				BigDecimal interessiVersati= BigDecimal.ZERO;
				BigDecimal importoRimborsato = BigDecimal.ZERO;
				
				for (AccertamentoExtendedDTO accertamentoDTO : res) {

					Long idStatoDeb = accertamentoDTO.getIdStatoDebitorio();
					
					List<RimborsoExtendedDTO> listRimborsi = rimborsoDAO.loadRimborsiByIdStatoDebitorio(idStatoDeb);		
					
					List<RataSdDTO> listRate = rataSdDAO.loadListRataSdByStatoDebitorio(idStatoDeb);
					

					BigDecimal statoDebImpSpesNotSD = template.query(StatoDebitorioDAOImpl.QUERY_SELECT_IMPORTO_SPESE_NOTIFICA_BY_ID_STATO_DEBITORIO, paramST, new ResultSetExtractor<BigDecimal>(){
						    @Override
						    public BigDecimal extractData(ResultSet rs) throws SQLException,DataAccessException {
						    	BigDecimal statoDebImpSpesNotSD = null;
						        while(rs.next()){
						        	statoDebImpSpesNotSD = rs.getBigDecimal("imp_spese_notifica") != null ?  rs.getBigDecimal("imp_spese_notifica") : BigDecimal.ZERO;
						        }
						        return statoDebImpSpesNotSD;
						    }
						});
					
					if(listRimborsi.size() > 0 && listRate.size() > 0) {
						
						for(RimborsoExtendedDTO rimborso : listRimborsi) {
							importoRimborsato = importoRimborsato.add(rimborso.getImpRimborso());
						}
						accertamentoDTO.setImportoRimborsato(importoRimborsato);
						
						for(RataSdDTO rata : listRate ) {
							if(rata.getIdRataSdPadre() != null) {
								BigDecimal canoneDovuto = rata.getCanoneDovuto();
								BigDecimal rataInterMat = BigDecimal.ZERO;
								BigDecimal statoDebImpSpesNot = BigDecimal.ZERO;
								
								if(rata.getInteressiMaturati() != null)
									rataInterMat = rata.getInteressiMaturati();
								

								if(statoDebImpSpesNotSD != null)
									statoDebImpSpesNot= statoDebImpSpesNotSD;
								
								if(canoneDovuto != null)
									importoDovuto = canoneDovuto;
								else
									canoneDovuto = BigDecimal.ZERO;
								
								dataScadenzaPag = rata.getDataScadenzaPagamento();

								interessiDovuti = rataInterMat.add(statoDebImpSpesNot);
								
								accertamentoDTO.setImportoDovuto(importoDovuto);
								accertamentoDTO.setDataScadenzaPag(dataScadenzaPag);
								accertamentoDTO.setInteressiDovuti(interessiDovuti);

								List<DettaglioPagExtendedDTO> dettagliPag = dettaglioPagDAO.getDettaglioPagByIdRate(rata.getIdRataSd());
								
								for(DettaglioPagExtendedDTO dettaglioPag : dettagliPag) {
									if(dettaglioPag.getImportoVersato() != null)
										importoVersato = importoVersato.add(dettaglioPag.getImportoVersato());
								}
								accertamentoDTO.setImportoVersato(importoVersato);
														
								
								BigDecimal risultato = (canoneDovuto.add(importoRimborsato)).subtract(importoVersato);
								
							    int compareResult = risultato.compareTo(BigDecimal.ZERO); 

							    if (compareResult > 0) {
							    	importoDiCanoneMancante = risultato;
							    	accertamentoDTO.setImportoDiCanoneMancante(importoDiCanoneMancante);
							    	
							    	accertamentoDTO.setInteressiVersati(BigDecimal.ZERO);

									interessiMancanti = rataInterMat.add(statoDebImpSpesNot);

							    	accertamentoDTO.setInteressiMancanti(interessiMancanti);
							    	
							    
						  	} else if (compareResult < 0) {
							    	importoRimborsato = risultato;
							    	accertamentoDTO.setImportoDiCanoneMancante(BigDecimal.ZERO);
							    	accertamentoDTO.setInteressiVersati(importoRimborsato);   
							    	
							    	BigDecimal interessi = (rataInterMat.add(statoDebImpSpesNot)).subtract((canoneDovuto.add(importoRimborsato)).subtract(importoVersato));
							    	
							    	int compareInteressi = interessi.compareTo(BigDecimal.ZERO);
							    	
							    	 if (compareInteressi > 0) {
							    		interessiMancanti = interessi;
							    		accertamentoDTO.setInteressiMancanti(interessiMancanti);
							    	} 
							    	else 
							    		accertamentoDTO.setInteressiMancanti(BigDecimal.ZERO);
							    }
				
								
								break;
							}
						}
						

					}
					
				}
			}
			
		} catch (Exception e) {
			LOGGER.error("[AccertamentoDAOImpl::loadAllAccertamentiOrByIdStatoDeb] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AccertamentoDAOImpl::loadAllAccertamentiOrByIdStatoDeb] END");
		}

		return res;
	}
	
	@Override
	public AccertamentoDTO saveAccertamentoWorking(AccertamentoDTO dto) throws DAOException {
		LOGGER.debug("[AccertamentoDAOImpl::saveAccertamentoWorking] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			
			Long genId = findNextSequenceValue("seq_risca_t_accertamento");
			map.put("idAccertamento", genId);
			map.put("idStatoDebitorio", dto.getIdStatoDebitorio());
	        map.put("idTipoAccertamento", dto.getTipoAccertamento().getIdTipoAccertamento());
	        map.put("idFile450", dto.getIdFile450());
	        map.put("idElabora", dto.getIdElabora());
	        map.put("numProtocollo", dto.getNumProtocollo());
	        map.put("dataProtocollo", dto.getDataProtocollo());
	        map.put("dataScadenza", dto.getDataScadenza());
	        map.put("flgAnnullato", dto.getFlgAnnullato());
	        map.put("flgRestituito", dto.getFlgRestituito());
	        map.put("dataNotifica", dto.getDataNotifica());
	        map.put("nota", dto.getNota());
	        map.put("idSpedizione", dto.getIdSpedizione());
			
			MapSqlParameterSource params = getParameterValue(map);
			String query = QUERY_INSERT_W;
			template.update(getQuery(query, null, null), params);
			dto.setIdAccertamento(genId);
			
			return dto;
		} catch (Exception e) {
			LOGGER.error("[AccertamentoDAOImpl::saveAccertamentoWorking] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AccertamentoDAOImpl::saveAccertamentoWorking] END");
		}
	}
	
	@Override
	public void deleteAccertamentoWorkingByIdElabora(Long idElabora) throws DAOException {
		LOGGER.debug("[AccertamentoDAOImpl::deleteAccertamentoByIdElabora] BEGIN");
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			MapSqlParameterSource params = getParameterValue(map);
			template.update(getQuery(QUERY_DELETE_WORKING_BY_ELABORA, null, null), params);
		} catch (Exception e) {
			LOGGER.error("[AccertamentoDAOImpl::deleteAccertamentoByIdElabora] ERROR : " ,e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AccertamentoDAOImpl::deleteAccertamentoByIdElabora] END");
		}
	}
	
	
	@Override
	public AccertamentoExtendedDTO loadAccertamentoByIdAccertamento(Long idAccertamento) throws DAOException {
		LOGGER.debug("[AccertamentoDAOImpl::loadAccertamentoByIdAccertamento] BEGIN");
		Map<String, Object> map = new HashMap<>();
		AccertamentoExtendedDTO accertamentoDTO = null;
		try {
			map.put("idAccertamento", idAccertamento);
			MapSqlParameterSource params = getParameterValue(map);
			accertamentoDTO = template.queryForObject(QUERY_SELECT_ACCERTAMENTI_BY_ID_ACCERTAMENTO, params, getExtendedRowMapper());
		} catch (Exception e) {
			LOGGER.error("[AccertamentoDAOImpl::loadAccertamentoByIdAccertamento] ERROR : " ,e);
			return null;

		}finally {
			LOGGER.debug("[AccertamentoDAOImpl::loadAccertamentoByIdAccertamento] END");
		}
		return accertamentoDTO;
	}
	
	
	private String mapSortConCampiDB(String sort) {
        String nomeCampo="";
		if(sort.contains("codiceUtenza")) {
			return getQuerySortingSegment(sort.replace("codiceUtenza", "rtr.cod_riscossione"));
		}
//		if(sort.contains("dataPagamento")) {
//			return getQuerySortingSegment(sort.replace("dataPagamento", "rtp.data_op_val"));
//		}
		return nomeCampo;
	}
	
	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RowMapper<AccertamentoDTO> getRowMapper() throws SQLException {
		return new AccertamentoRowMapper();
	}
	@Override
	public RowMapper<AccertamentoExtendedDTO> getExtendedRowMapper() throws SQLException {
		return new AccertamentoExtendedRowMapper();
	}
	
	/**
	 * The type Accertamento row mapper.
	 */
	public static class AccertamentoRowMapper implements RowMapper<AccertamentoDTO> {
		
		/**
		 * Instantiates a new Accertamento row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public AccertamentoRowMapper() throws SQLException {
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
		public AccertamentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccertamentoDTO bean = new AccertamentoDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AccertamentoDTO bean) throws SQLException {
			bean.setIdAccertamento(rs.getLong("id_accertamento"));
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			TipiAccertamentoDTO tipoAccertamento = new TipiAccertamentoDTO();
			populateBeanTipoAccertamento(rs,tipoAccertamento);
			bean.setTipoAccertamento(tipoAccertamento);
			
			bean.setIdFile450(rs.getLong("id_file_450"));
			bean.setNumProtocollo(rs.getString("num_protocollo"));
			bean.setDataProtocollo(rs.getDate("data_protocollo"));
			bean.setDataScadenza(rs.getDate("data_scadenza"));
			bean.setDataNotifica(rs.getDate("data_notifica"));
			bean.setNota(rs.getString("nota"));
			bean.setFlgRestituito(rs.getInt("flg_restituito"));
			bean.setFlgAnnullato(rs.getInt("flg_annullato"));

			if(rsHasColumn(rs, "id_elabora")) bean.setIdElabora(rs.getLong("id_elabora"));
			
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));

			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
			
		}
		
		private void populateBeanTipoAccertamento(ResultSet rs, TipiAccertamentoDTO bean) throws SQLException {

			bean.setIdTipoAccertamento(rs.getLong("id_tipo_accertamento"));
			bean.setCodTipoAccertamento(rs.getString("cod_tipo_accertamento"));
			bean.setDesTipoAccertamento(rs.getString("des_tipo_accertamento"));	
			bean.setFlgAutomatico(rs.getInt("flg_automatico"));
			bean.setFlgManuale(rs.getInt("flg_manuale"));
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
	/**
	 * The type Accertamento row mapper.
	 */
	public static class AccertamentoExtendedRowMapper implements RowMapper<AccertamentoExtendedDTO> {
		
		/**
		 * Instantiates a new Accertamento row mapper.
		 *
		 * @throws SQLException the sql exception
		 */
		public AccertamentoExtendedRowMapper() throws SQLException {
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
		public AccertamentoExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AccertamentoExtendedDTO bean = new AccertamentoExtendedDTO();
			populateBean(rs, bean);
			return bean;
		}

		private void populateBean(ResultSet rs, AccertamentoExtendedDTO bean) throws SQLException {
			bean.setIdAccertamento(rs.getLong("id_accertamento"));
			bean.setIdStatoDebitorio(rs.getLong("id_stato_debitorio"));
			TipiAccertamentoDTO tipoAccertamento = new TipiAccertamentoDTO();
			populateBeanTipoAccertamento(rs,tipoAccertamento);
			bean.setTipoAccertamento(tipoAccertamento);
			
			if(rs.getLong("id_file_450") > 0)
			   bean.setIdFile450(rs.getLong("id_file_450"));
			bean.setNumProtocollo(rs.getString("num_protocollo"));
			bean.setDataProtocollo(rs.getDate("data_protocollo"));
			bean.setDataScadenza(rs.getDate("data_scadenza"));
			bean.setDataNotifica(rs.getDate("data_notifica"));
			bean.setNota(rs.getString("nota"));
			bean.setFlgRestituito(rs.getInt("flg_restituito"));
			bean.setFlgAnnullato(rs.getInt("flg_annullato"));

			
			if(rsHasColumn(rs, "gest_attore_ins")) bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
			if(rsHasColumn(rs, "gest_data_ins")) bean.setGestDataIns(rs.getDate("gest_data_ins"));
			if(rsHasColumn(rs, "gest_attore_upd")) bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
			if(rsHasColumn(rs, "gest_data_upd")) bean.setGestDataUpd(rs.getDate("gest_data_upd"));
			if(rsHasColumn(rs, "gest_uid")) bean.setGestUid(rs.getString("gest_uid"));
			
		}
		
		private void populateBeanTipoAccertamento(ResultSet rs, TipiAccertamentoDTO bean) throws SQLException {

			bean.setIdTipoAccertamento(rs.getLong("id_tipo_accertamento"));
			bean.setCodTipoAccertamento(rs.getString("cod_tipo_accertamento"));
			bean.setDesTipoAccertamento(rs.getString("des_tipo_accertamento"));	
			bean.setFlgAutomatico(rs.getInt("flg_automatico"));
			bean.setFlgManuale(rs.getInt("flg_manuale"));
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
	public Integer copyAccertamentoFromWorking(Long idElabora, String attore) throws DAOException {
		LOGGER.debug("[AccertamentoDAOImpl::copyAccertamentoFromWorking] BEGIN");
		try {
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			Map<String, Object> map = new HashMap<>();
			map.put("idElabora", idElabora);
			map.put("gestAttoreIns", attore);
			map.put("gestAttoreUpd", attore);
			map.put("gestDataIns", now);
			map.put("gestDataUpd", now);
			map.put("gestUid", null);
			MapSqlParameterSource params = getParameterValue(map);
			KeyHolder keyHolder = new GeneratedKeyHolder();

			return template.update(getQuery(QUERY_COPY_ACCERTAMENTI_FROM_WORKING, null, null), params, keyHolder);

		} catch (Exception e) {
			LOGGER.error("[AccertamentoDAOImpl::copyAccertamentoFromWorking] ERROR : ",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new DAOException(Constants.ERRORE_GENERICO);
		} finally {
			LOGGER.debug("[AccertamentoDAOImpl::copyAccertamentoFromWorking] END");
		}
	}
	
	@Override
	public String findMinDataProtocolloByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException, SystemException
	{
		LOGGER.debug("[AccertamentoDAOImpl::findMinDataProtocolloByIdStatoDebitorio] BEGIN");
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();		
		
		sql.append(" select CASE when MIN(data_protocollo)  is null then '"+Constants.DATA_SCONOSCIUTA+"' else MIN(data_protocollo) end as data_protocollo  ");
		sql.append(" from risca_t_accertamento ");
		sql.append(" where id_stato_debitorio = :id_stato_debitorio ");
		sql.append(" and id_tipo_accertamento IN ( "+Constants.DB.TIPO_ACCERTAMENTO.ID_TIPO_ACCERTAMENTO.RISCOSSIONE_COATTIVA+
				","+Constants.DB.TIPO_ACCERTAMENTO.ID_TIPO_ACCERTAMENTO.FALLIMENTO);
		sql.append(" )and flg_annullato = "+Constants.FLAG_ANNULLATO_FALSE);

		paramMap.addValue("id_stato_debitorio", idStatoDebitorio);


		LOGGER.debug("[AccertamentoDAOImpl - findMinDataProtocolloByIdStatoDebitorio] query =" + sql.toString());

		String dataProtocollo = null;
		try
		{
			dataProtocollo = template.queryForObject(sql.toString(), paramMap, String.class);
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[AccertamentoDAOImpl::findMinDataProtocolloByIdStatoDebitorio]  NESSUN RISULTATO");
			throw new DAOException("Nessuna data protocollo trovata in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[AccertamentoDAOImpl::findMinDataProtocolloByIdStatoDebitorio] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[AccertamentoDAOImpl::findMinDataProtocolloByIdStatoDebitorio] END");
		}
		return dataProtocollo;
	}
	
	
	@Override
	public AccertamentoDTO findMaxDataAndNumProtocolloByIdStatoDebitorio(Long idStatoDebitorio) throws DAOException, SystemException
	{
		LOGGER.debug("[AccertamentoDAOImpl::findMaxDataAndNumProtocolloByIdStatoDebitorio] BEGIN");
		LOGGER.debug("[AccertamentoDAOImpl::findMaxDataAndNumProtocolloByIdStatoDebitorio] idStatoDebitorio: "+idStatoDebitorio);
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();				

		sql.append(" select acc.data_protocollo, ");
		sql.append(" replace(  case when split_part(acc.num_protocollo, '(Rif ', 2) != '' then split_part(acc.num_protocollo, '(', 1) || split_part( split_part( split_part(acc.num_protocollo, '/', 1), '(Rif ', 2), ')', 1) else split_part(acc.num_protocollo, '(', 1) || split_part( split_part( split_part(acc.num_protocollo, '/', 1), '(Rif ', 2), ')', 1) end, ' ', '') num_protocollo , ");
		sql.append(" 	 acc.data_notifica, ");
		sql.append(" acc.id_accertamento ");
		sql.append("    from	risca_t_accertamento acc ");
		sql.append("   where acc.data_protocollo in ( ");
		sql.append("                              select  MAX(data_protocollo) data_protocollo ");
		sql.append("                                from	risca_t_accertamento rta ");
		sql.append("                               where rta.id_stato_debitorio = :id_stato_debitorio ");
		sql.append("                              and (flg_restituito is null or flg_restituito = 0) ");
		sql.append("                                 and (flg_annullato is null or flg_annullato = 0) ");
		sql.append("                                 and id_tipo_accertamento in (2, 5, 6) ");
		sql.append("                               ) ");
		sql.append("     and acc.id_stato_debitorio = :id_stato_debitorio ");
		sql.append("  order by acc.data_notifica, acc.gest_data_upd desc ");

		
		paramMap.addValue("id_stato_debitorio", idStatoDebitorio);

		LOGGER.debug("[AccertamentoDAOImpl - findMaxDataAndNumProtocolloByIdStatoDebitorio] query =" + sql.toString());

		AccertamentoDTO accertamentoDto = null;
		try
		{
			accertamentoDto = template.queryForObject(sql.toString(), paramMap, new AccertamentoDaoRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[AccertamentoDAOImpl::findMaxDataAndNumProtocolloByIdStatoDebitorio]  NESSUN RISULTATO");
			throw new DAOException("Nessun accertamento trovato in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[AccertamentoDAOImpl::findMaxDataAndNumProtocolloByIdStatoDebitorio] esecuzione query", ex);
			throw new SystemException("Errore di sistema: Esiste piu' di un accertamento con la stessa data protocollo. ", ex);
		}
		finally
		{
			LOGGER.debug("[AccertamentoDAOImpl::findMaxDataAndNumProtocolloByIdStatoDebitorio] END");
		}
		return accertamentoDto;
	}
	
	
	@Override
	public String getTableNameAString() {
		return getTableName();
	}

}