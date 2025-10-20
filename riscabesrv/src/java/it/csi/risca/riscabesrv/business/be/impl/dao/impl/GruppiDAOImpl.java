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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.impl.dao.FonteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.GruppiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.IndirizziSpedizioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettoGruppoDAO;
import it.csi.risca.riscabesrv.dto.FonteDTO;
import it.csi.risca.riscabesrv.dto.GruppiDTO;
import it.csi.risca.riscabesrv.dto.GruppiExtendedDTO;
import it.csi.risca.riscabesrv.dto.GruppiSoggettoDTO;
import it.csi.risca.riscabesrv.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type Gruppi dao impl.
 *
 * @author CSI PIEMONTE
 */
public class GruppiDAOImpl extends RiscaBeSrvGenericDAO<GruppiDTO> implements GruppiDAO { 
	
    private final String className = this.getClass().getSimpleName();
    @Autowired
    private MessaggiDAO messaggiDAO;
    @Autowired
    private SoggettiDAO soggettiDAO;
    
    @Autowired
    private SoggettoGruppoDAO soggettoGruppoDAO;
    
    @Autowired
    private IndirizziSpedizioneDAO indirizziSpedizioneDAO;
    
    @Autowired
    private FonteDAO fonteDAO;

    
	private static final String QUERY_LOAD_GRUPPI = "SELECT * FROM risca_t_gruppo_soggetto WHERE data_cancellazione is null";

	private static final String QUERY_LOAD_SOGGETTI_GRUPPO = 
			  "SELECT * "
			+ "	FROM risca_r_soggetto_gruppo "
			+ "	WHERE id_gruppo_soggetto = :id_gruppo_soggetto";
	
	private static final String QUERY_RETRIEVE_ID_REFERENTE_FROM_SOGGETTI_GRUPPO = 
		  "SELECT id_soggetto "
		  + "	FROM risca_r_soggetto_gruppo rrsg  "
		  + "	WHERE rrsg.id_gruppo_soggetto = :IdGruppoSoggetto AND rrsg.flg_capo_gruppo=1";
	
	private static final String QUERY_LOAD_GRUPPI_BY_ID = 
			  "SELECT * "
			+ "FROM risca_t_gruppo_soggetto "
			+ "WHERE (:isIdGruppoSoggetto=0 OR id_gruppo_soggetto = :id_gruppo_soggetto)"
			+ "		AND (:isCodGruppoSoggetto=0 OR cod_gruppo_soggetto = :cod_gruppo_soggetto)"
			+ "		AND (:isDesGruppoSoggetto=0 OR des_gruppo_soggetto = :des_gruppo_soggetto)"
			+ "		AND data_cancellazione IS NULL";
	
    private static final String QUERY_INSERT_GRUPPO_SOGGETTO = "INSERT INTO risca_t_gruppo_soggetto (id_gruppo_soggetto, cod_gruppo_soggetto, cod_gruppo_fonte, des_gruppo_soggetto, id_fonte, id_fonte_origine,  " + 
            "gest_attore_ins,  " + 
    		"gest_data_ins, gest_data_upd, gest_attore_upd, gest_uid, data_aggiornamento, data_cancellazione)  " + 
    		"VALUES(nextval('seq_risca_t_gruppo_soggetto'), :cod_gruppo_soggetto, :cod_gruppo_fonte, :des_gruppo_soggetto, :id_fonte, :id_fonte_origine,  " + 
    		":gest_attore_ins, :gest_data_ins,  " + 
    		":gest_data_upd, :gest_attore_upd, :gest_uid, :data_aggiornamento, :data_cancellazione);";
    
    private static final String QUERY_UPDATE_GRUPPO_SOGGETTO = 
    		  "	UPDATE risca_t_gruppo_soggetto SET "
  			+ "		des_gruppo_soggetto = :des_gruppo_soggetto, id_fonte = :id_fonte, " 
  			+ " 	gest_data_upd = :gest_data_upd, gest_attore_upd = :gest_attore_upd, data_aggiornamento = :data_aggiornamento"
  			+ " WHERE id_gruppo_soggetto = :id_gruppo_soggetto;";
  	        
    private static final String QUERY_INSERT_SOGGETTO_GRUPPO = "INSERT INTO risca_r_soggetto_gruppo ("
    		+ "	id_soggetto, id_gruppo_soggetto,  flg_capo_gruppo, gest_attore_ins, gest_data_ins, gest_data_upd, gest_attore_upd, gest_uid"
    		+ ") VALUES ("
    		+ "	:id_soggetto, :id_gruppo_soggetto, :flg_capo_gruppo, :gest_attore_ins, :gest_data_ins, :gest_data_upd, :gest_attore_upd, :gest_uid)";
    
    private static final String QUERY_UPDATE_SOGGETTO_GRUPPO = "UPDATE risca_r_soggetto_gruppo SET "
    		+ "		flg_capo_gruppo=:flg_capo_gruppo, gest_data_upd=:gest_data_upd, gest_attore_upd=:gest_attore_upd, gest_uid=:gest_uid"
    		+ " WHERE id_soggetto=:id_soggetto AND id_gruppo_soggetto=:id_gruppo_soggetto"
;

	
    
    //private static final String QUERY_DELETE_GRUPPO_SOGGETTO = "UPDATE risca_t_gruppo_soggetto SET data_cancellazione=NOW() WHERE id_gruppo_soggetto = :id_gruppo_soggetto"; 
    
    private static final String QUERY_DELETE_SOGGETTO_GRUPPO = "DELETE FROM risca_r_soggetto_gruppo WHERE id_gruppo_soggetto = :id_gruppo_soggetto AND id_soggetto = :id_soggetto";

    private static final String QUERY_DELETE_GRUPPO_SOGGETTO = "DELETE FROM risca_t_gruppo_soggetto WHERE id_gruppo_soggetto = :id_gruppo_soggetto";
    
    private static final String QUERY_DELETE_ALL_SOGGETTO_GRUPPO = "DELETE FROM risca_r_soggetto_gruppo WHERE id_gruppo_soggetto = :id_gruppo_soggetto";

    private static final String QUERY_DELETE_INDIRIZZI_GRUPPO = "DELETE FROM risca_r_recapito_postel rrrp WHERE id_gruppo_soggetto = :id_gruppo_soggetto";
    
    private static final String QUERY_UPDATE_SOGGETTO_RISCOSSIONE = "UPDATE risca_t_riscossione SET id_soggetto=:idSoggettoNew WHERE id_soggetto = :idSoggettoOld";

	private static final String QUERY_RISCOSSIONE_BY_GRUPPO = "SELECT COUNT(1) as howManyRecords FROM risca_t_riscossione"
			+ " WHERE id_gruppo_soggetto = :id_gruppo_soggetto";
    
	private static final String QUERY_LOAD_GRUPPI_BY_ID_AMBITO_AND_CAMPO_RICERCA="SELECT  distinct rtgs.* ,CASE WHEN rts.den_soggetto is not null THEN rts.den_soggetto|| ' ' ||  rts.cognome|| ' ' || rts.nome "
			+ "  WHEN rts.den_soggetto is  null THEN rts.cognome|| ' ' ||  rts.nome "
			+ " END as nome_cognome,"
			+ "rts.cf_soggetto, rrsg.flg_capo_gruppo " + 
			"FROM risca_t_soggetto rts " + 
			"inner join risca_r_soggetto_gruppo rrsg on rrsg.id_soggetto= rts.id_soggetto " + 
			"inner join risca_t_gruppo_soggetto rtgs on rtgs.id_gruppo_soggetto = rrsg.id_gruppo_soggetto " + 
		    "INNER JOIN risca_d_ambito rda ON rts.id_ambito = rda.id_ambito " +
			"WHERE upper(rtgs.des_gruppo_soggetto) ilike upper('%'||:campoRicerca||'%') " + 
			"and rts.data_cancellazione is null " + 
			"and rtgs.data_cancellazione is null " + 
			"and rts.id_ambito = :idAmbito ";
			
	private static final String QUERY_COUNT_GRUPPI_BY_ID_AMBITO_AND_CAMPO_RICERCA="SELECT COUNT( distinct rtgs.*) "+
			"FROM risca_t_soggetto rts " + 
			"inner join risca_r_soggetto_gruppo rrsg on rrsg.id_soggetto= rts.id_soggetto " + 
			"inner join risca_t_gruppo_soggetto rtgs on rtgs.id_gruppo_soggetto = rrsg.id_gruppo_soggetto " + 
		    "INNER JOIN risca_d_ambito rda ON rts.id_ambito = rda.id_ambito " +
			"WHERE upper(rtgs.des_gruppo_soggetto) ilike upper('%'||:campoRicerca||'%') " + 
			"and rts.data_cancellazione is null " + 
			"and rtgs.data_cancellazione is null " + 
			"and rts.id_ambito = :idAmbito ";
			
//			"select distinct  rtgs.* FROM risca_t_gruppo_soggetto rtgs "
//			+ " inner join risca_r_soggetto_gruppo rrsg on rtgs.id_gruppo_soggetto = rrsg.id_gruppo_soggetto "
//			+ " left join risca_t_soggetto rts on rrsg.id_soggetto  = rts.id_soggetto "
//			+ " left join risca_d_ambito rda on rts.id_ambito = rda.id_ambito "
//			+ " where rda.id_ambito =:idAmbito "
//			+ " and  rtgs.des_gruppo_soggetto ilike '%'||:campoRicerca||'%' ";
	
	private static final String QUERY_LOAD_GRUPPI_BY_ID_AMBITO_AND_CAMPO_RICERCA_FULL =" Union   " + 
			"SELECT  distinct rtgs.* ,CASE WHEN rts.den_soggetto is not null THEN rts.den_soggetto|| ' ' ||  rts.cognome|| ' ' || rts.nome "
			+ "  WHEN rts.den_soggetto is  null THEN rts.cognome|| ' ' ||  rts.nome "
			+ " END as nome_cognome,"
			+ "rts.cf_soggetto, rrsg.flg_capo_gruppo  " + 
			"FROM risca_t_soggetto rts " + 
			"left join RISCA_R_RECAPITO rrr on rrr.id_soggetto =rts.id_soggetto " + 
			"inner join risca_r_soggetto_gruppo rrsg on rrsg.id_soggetto= rts.id_soggetto " + 
			"inner join risca_t_gruppo_soggetto rtgs on rtgs.id_gruppo_soggetto = rrsg.id_gruppo_soggetto " + 
		    "INNER JOIN risca_d_ambito rda ON rts.id_ambito = rda.id_ambito " +
			"WHERE ( cf_soggetto ilike '%'||:campoRicerca||'%' " + 
			"or upper(rts.cognome) ilike upper('%'||:campoRicerca||'%') " + 
			"or upper(rts.partita_iva_soggetto) ilike upper('%'||:campoRicerca||'%') " + 
			"or upper(rts.den_soggetto) like upper('%'||:campoRicerca||'%') " + 
			"or upper(rrr.pec) ilike upper('%'||:campoRicerca||'%') )" + 
			"and rts.data_cancellazione is null " + 
			"and rtgs.data_cancellazione is null " + 
			"and rrr.data_cancellazione is null " + 
			"and rts.id_ambito = :idAmbito ";
	
	private static final String QUERY_COUNT_GRUPPI_BY_ID_AMBITO_AND_CAMPO_RICERCA_FULL ="SELECT  COUNT(*)  "
			+ "FROM ("
			+"SELECT  distinct rtgs.* " +
			"FROM risca_t_soggetto rts " + 
			"inner join risca_r_soggetto_gruppo rrsg on rrsg.id_soggetto= rts.id_soggetto " + 
			"inner join risca_t_gruppo_soggetto rtgs on rtgs.id_gruppo_soggetto = rrsg.id_gruppo_soggetto " + 
		    "INNER JOIN risca_d_ambito rda ON rts.id_ambito = rda.id_ambito " +
			"WHERE upper(rtgs.des_gruppo_soggetto) ilike upper('%'||:campoRicerca||'%') " + 
			"and rts.data_cancellazione is null " + 
			"and rtgs.data_cancellazione is null " + 
			"and rts.id_ambito = :idAmbito "
			+ " Union   " + 
			"SELECT  distinct rtgs.* "+
			"FROM risca_t_soggetto rts " + 
			"left join RISCA_R_RECAPITO rrr on rrr.id_soggetto =rts.id_soggetto " + 
			"inner join risca_r_soggetto_gruppo rrsg on rrsg.id_soggetto= rts.id_soggetto " + 
			"inner join risca_t_gruppo_soggetto rtgs on rtgs.id_gruppo_soggetto = rrsg.id_gruppo_soggetto " + 
		    "INNER JOIN risca_d_ambito rda ON rts.id_ambito = rda.id_ambito " +
			"WHERE ( cf_soggetto ilike '%'||:campoRicerca||'%' " + 
			"or upper(rts.cognome) ilike upper('%'||:campoRicerca||'%') " + 
			"or upper(rts.partita_iva_soggetto) ilike upper('%'||:campoRicerca||'%') " + 
			"or upper(rts.den_soggetto) like upper('%'||:campoRicerca||'%') " + 
			"or upper(rrr.pec) ilike upper('%'||:campoRicerca||'%') )" + 
			"and rts.data_cancellazione is null " + 
			"and rtgs.data_cancellazione is null " + 
			"and rrr.data_cancellazione is null " + 
			"and rts.id_ambito = :idAmbito ) as c";
	
    private static final String QUERY_UPDATE_RECAPITO_POSTEL_IND_VALID_POSTEL = "UPDATE risca_r_recapito_postel SET ind_valido_postel = :indValidoPostel " +
   		 "WHERE id_recapito_postel = :idRecapitoPostel ";
    
    private static final String QUERY_AND_ID_GRUPPO_SOGGETTO=" AND id_gruppo_soggetto = :idGruppoSoggetto ";
    
    private static final String QUERY_UPDATE_STATO_DEBITORIO = "UPDATE risca_t_stato_debitorio SET id_soggetto=:idSoggettoNew WHERE id_gruppo_soggetto = :idGruppoSoggetto"
    		+ " AND id_attivita_stato_deb IS NULL";

    private static final String QUERY_DELETE_RECAPITO_POSTEL_BY_ID_GRUPPO_ID_RECAPITO= "DELETE FROM risca_r_recapito_postel WHERE id_gruppo_soggetto = :idGruppoSoggetto AND id_recapito = :idRecapito";

	private static final String QUERY_LOAD_GRUPPI_SOGGETTO = "SELECT rtgs.* FROM risca_t_gruppo_soggetto rtgs "
			+ "WHERE rtgs.id_gruppo_soggetto in (:idGruppoSoggetto) ";
    
	@Override
	public List<GruppiDTO> loadGruppiSoggetto() throws Exception {
        LOGGER.debug("[GruppiDAOImpl::loadGruppiSoggetto] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        try {
        	List<GruppiDTO> gruppi = findListByQuery(className, methodName, QUERY_LOAD_GRUPPI, null, null, null, Boolean.TRUE);
        	if(gruppi != null) {
	            for(GruppiDTO gruppo : CollectionUtils.emptyIfNull(gruppi)) {
	            	List<GruppiSoggettoDTO>  listGruppiSoggetto = soggettoGruppoDAO.loadSoggettoGruppoByIdGruppoSoggetto(gruppo.getIdGruppoSoggetto());
	            	gruppo.setComponentiGruppo(listGruppiSoggetto);
	            }
	    		
	    		return gruppi;
        	}

        } catch (DataAccessException e) {
            LOGGER.error("[GruppiDAOImpl::loadGruppiSoggetto] Errore nell'accesso ai dati", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("[GruppiDAOImpl::loadGruppiSoggetto] Errore nell'accesso ai dati", e);
            throw e;
		} finally {
            LOGGER.debug("[GruppiDAOImpl::loadGruppiSoggetto] END");
        }

        return new ArrayList<GruppiDTO>();
	}
	


	@Override
	public Long findIdSoggettoByIdGruppoSoggetto(Long IdGruppoSoggetto) {
		Map<String, Object> parameters = Collections.singletonMap("IdGruppoSoggetto", IdGruppoSoggetto);
		RowMapper<Long> rowMapper = (rs, rowNum) -> Long.valueOf(rs.getLong(1));

		Long idSoggetto = template.queryForObject(QUERY_RETRIEVE_ID_REFERENTE_FROM_SOGGETTI_GRUPPO, 
				parameters,
				rowMapper);
		
		return idSoggetto;
	}
	
	@Override
	public GruppiDTO loadGruppiById(String codOrIdGruppo, final String desGruppo) throws SQLException {
        LOGGER.debug("[GruppiDAOImpl::loadGruppiById] BEGIN");
        if(!StringUtils.isEmpty(codOrIdGruppo) || !StringUtils.isEmpty(desGruppo)) {
	        try { 
	        	Long isIdValid = null;
	        	try {
	        		isIdValid = Long.parseLong(codOrIdGruppo);
	        		codOrIdGruppo = null;
	        	} catch (NumberFormatException skip) { }
	        	
	        	final Long idGruppo = isIdValid;
	        	final String codGruppo = codOrIdGruppo; 
	        	Map<String, Object> parameters = new HashMap<>();

	        	parameters.put("isIdGruppoSoggetto", idGruppo != null ? 1L : 0L);
	        	parameters.put("id_gruppo_soggetto", idGruppo);
	        	parameters.put("isCodGruppoSoggetto", codGruppo != null ? 1L : 0L);
	        	parameters.put("cod_gruppo_soggetto", codGruppo);
	        	parameters.put("isDesGruppoSoggetto", desGruppo != null ? 1L : 0L);
	        	parameters.put("des_gruppo_soggetto", desGruppo);

	        	GruppiDTO gruppo =template.queryForObject(QUERY_LOAD_GRUPPI_BY_ID, 
	            								getParameterValue(parameters), 
	            								getRowMapper());
	        	if(gruppo != null) {
		             gruppo.getIdGruppoSoggetto();
	            	List<GruppiSoggettoDTO>  listGruppiSoggetto = soggettoGruppoDAO.loadSoggettoGruppoByIdGruppoSoggetto(gruppo.getIdGruppoSoggetto());
	            	gruppo.setComponentiGruppo(listGruppiSoggetto);
		    		return gruppo;
	        	}
	        	
	        } catch (SQLException e) {
	            LOGGER.error("[GruppiDAOImpl::loadGruppiById] Errore nell'esecuzione della query", e);
	            throw e;
	        } catch (DataAccessException e) {
	            LOGGER.error("[GruppiDAOImpl::loadGruppiById] Errore nell'accesso ai dati", e);
	            throw e;
	        } catch (Exception e) {
	            LOGGER.error("[GruppiDAOImpl::loadGruppiById] Errore nell'accesso ai dati", e);
	            throw new SQLException(e);
			}
        }

        LOGGER.debug("[GruppiDAOImpl::loadGruppiById] END");
        throw new BusinessException("E001");
	}
    
	@Override
	public Long saveGruppi(GruppiDTO dto) throws Exception {
        LOGGER.debug("[GruppiDAOImpl::saveGruppi] BEGIN");
        
        try {
        	return storeGruppo(dto);
        }
	    catch(BusinessException be) {
            LOGGER.error("[GruppiDAOImpl::saveGruppi] ERROR : " +be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    	throw be;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            LOGGER.error("[GruppiDAOImpl::saveGruppi] ERROR : " +e);
            throw e;
        } finally {
            LOGGER.debug("[GruppiDAOImpl::saveGruppi] END");
        }
	}

	@Override
	public Long updateGruppi(GruppiDTO dto) throws Exception {
        LOGGER.debug("[GruppiDAOImpl::updateGruppi] BEGIN");
        try {
        	return storeGruppo(dto);
        }
        catch(BusinessException be) {
            LOGGER.error("[GruppiDAOImpl::updateGruppi] ERROR : " +be);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        	throw be;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            LOGGER.error("[GruppiDAOImpl::updateGruppi] ERROR : " +e);
            throw e;
        } finally {
            LOGGER.debug("[GruppiDAOImpl::updateGruppi] END");
        }
	}
	
	private Long storeGruppo(GruppiDTO dto) throws Exception {
		LOGGER.debug("[GruppiDAOImpl::storeGruppo] BEGIN");
        final Long id_gruppo_soggetto = dto.getIdGruppoSoggetto();

        validateMandatoryFields(
        		"DES_GRUPPO_SOGGETTO", dto.getDesGruppoSoggetto()
        );
        
    	if(CollectionUtils.emptyIfNull(dto.getComponentiGruppo()).size() == 0)
    	{    	messaggiDAO.loadMessaggiByCodMessaggio("E024");
    		throw new BusinessException(400, "E024", "necessaria presenza di almeno un componente associato al gruppo");
    	}

    	long howManyCapoGruppo = dto.getComponentiGruppo().stream().filter(x -> x.getFlgCapoGruppo() == 1).count();
    	if(howManyCapoGruppo != 1)
    		throw new BusinessException(400, howManyCapoGruppo == 0?"E035" : "E036", "numero di referenti associati al gruppo non corretto");
    	
        Map<String, Object> map = new HashMap<>();
        Date now = Calendar.getInstance().getTime();
        
        Long generatedID = findNextSequenceValue("seq_risca_cod_gruppo_soggetto");
        
        FonteDTO fonte = null;
        if(dto.getCodGruppoFonte() != null) {
        	Map<String, Object> parameters = new HashMap<>();
        	parameters.put("codFonte", dto.getCodGruppoFonte());

    		fonte = fonteDAO.loadFonteByCodFonte(dto.getCodGruppoFonte());
        }
        
        map.put("id_fonte", fonte == null? 1 : fonte.getIdFonte()); 
		map.put("des_gruppo_soggetto", dto.getDesGruppoSoggetto());
        map.put("data_aggiornamento", now);
        GruppiDTO gruppo = null;
    	if(id_gruppo_soggetto != null) {
            map.put("id_gruppo_soggetto", id_gruppo_soggetto);
            gruppo =  loadGruppiById(id_gruppo_soggetto.toString(), null);
    	}
    	else {
    		map.put("data_cancellazione", null);
            map.put("cod_gruppo_soggetto", "GRI" + generatedID);
            map.put("cod_gruppo_fonte", dto.getCodGruppoFonte());
            map.put("id_fonte_origine", fonte == null? 1 : fonte.getIdFonte());
            map.put("gest_data_ins", now);
            map.put("gest_attore_ins", dto.getGestAttoreIns());
            map.put("gest_uid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
    	}
		
//      String uid = generateGestUID(dto.getCfAccredito() + dto.getDesEmailAccredito() + now);
        map.put("gest_data_upd", now);
        map.put("gest_attore_upd", dto.getGestAttoreUpd());
		
        MapSqlParameterSource params = getParameterValue(map);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
    	
    	
        template.update(id_gruppo_soggetto != null? QUERY_UPDATE_GRUPPO_SOGGETTO : QUERY_INSERT_GRUPPO_SOGGETTO, params, keyHolder, new String[]{"id_gruppo_soggetto"});
        Number key = keyHolder.getKey();

        map.put("id_gruppo_soggetto", key.longValue());

        // retrieve all risca_r_soggetto_gruppo linked to this id_gruppo_soggetto
        List<Map<String, Object>> gruppiSoggettoList = null;
    	if(id_gruppo_soggetto != null) {
        	Map<String, Object> parameters = new HashMap<>();
        	parameters.put("id_gruppo_soggetto", id_gruppo_soggetto);
    		gruppiSoggettoList = template.queryForList(QUERY_LOAD_SOGGETTI_GRUPPO, parameters);
    	}

    	long idGruppoSoggettoReferenteNew = 0l, idGruppoSoggettoReferenteOld = 0l;
        for(GruppiSoggettoDTO gruppoSoggettoDTO : dto.getComponentiGruppo()) {
            Map<String, Object> mapSoggetto = new HashMap<>();
            
            boolean prevSoggettoFound = false;
            if(gruppiSoggettoList != null) {
                // get previous risca_r_soggetto_gruppo by id_soggetto, if any
                for(int soggettoIndex = 0; soggettoIndex<gruppiSoggettoList.size(); soggettoIndex++) {
                	if( ((Number)((Map)gruppiSoggettoList.get(soggettoIndex)).get("id_soggetto")).longValue() == gruppoSoggettoDTO.getIdSoggetto().longValue()) {
                		prevSoggettoFound = (mapSoggetto = gruppiSoggettoList.remove(soggettoIndex)) != null;
                		
                		Number capo = (Number)mapSoggetto.get("flg_capo_gruppo");
                        if(capo != null && capo.intValue() == 1)
                        	idGruppoSoggettoReferenteOld = ((Number)mapSoggetto.get("id_soggetto")).intValue();

                		break;
                	}
                }
            }
        		
            mapSoggetto.put("id_soggetto", gruppoSoggettoDTO.getIdSoggetto());
            mapSoggetto.put("id_gruppo_soggetto", key);
            mapSoggetto.put("flg_capo_gruppo", gruppoSoggettoDTO.getFlgCapoGruppo());
            
            if(gruppoSoggettoDTO.getFlgCapoGruppo() == 1)
            	idGruppoSoggettoReferenteNew = gruppoSoggettoDTO.getIdSoggetto().longValue();

            mapSoggetto.put("gest_data_ins", now);
            mapSoggetto.put("gest_attore_ins", dto.getGestAttoreIns());
            mapSoggetto.put("gest_data_upd", now);
            mapSoggetto.put("gest_attore_upd", dto.getGestAttoreUpd());
            //  String uid = generateGestUID(dto.getCfAccredito() + dto.getDesEmailAccredito() + now);
            mapSoggetto.put("gest_uid", generateGestUID( dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
	
            template.update(prevSoggettoFound ? QUERY_UPDATE_SOGGETTO_GRUPPO : QUERY_INSERT_SOGGETTO_GRUPPO, mapSoggetto);
        }

        // deletes all remaining "gruppiSoggettoList"
        for(Map<String, Object> gruppoSoggetto : CollectionUtils.emptyIfNull(gruppiSoggettoList)) {
        	Map<String, Object> parameters = new HashMap<>();
        	parameters.put("id_soggetto", gruppoSoggetto.get("id_soggetto"));
        	parameters.put("id_gruppo_soggetto", gruppoSoggetto.get("id_gruppo_soggetto"));

            template.update(QUERY_DELETE_SOGGETTO_GRUPPO,parameters);
        }

        // set "referente"
        if(idGruppoSoggettoReferenteNew != idGruppoSoggettoReferenteOld && idGruppoSoggettoReferenteOld != 0) {
        	final Long oldId = idGruppoSoggettoReferenteOld, newId = idGruppoSoggettoReferenteNew;
        	Map<String, Object> parameters = new HashMap<>();
        	parameters.put("idSoggettoNew", newId);
        	parameters.put("idSoggettoOld", oldId);

            template.update(QUERY_UPDATE_SOGGETTO_RISCOSSIONE,parameters);
        }
        
        
//        parte indirizzi di spedizione
		List<GruppiSoggettoDTO> listGruppiSoggettoDTO = dto.getComponentiGruppo().stream().filter(g -> g.getFlgCapoGruppo() == 1).collect(Collectors.toList());
        if(id_gruppo_soggetto == null) {
    		//insert indirizzi spedizione
            for(GruppiSoggettoDTO gruppoSoggettoDTO : listGruppiSoggettoDTO) {
	   			SoggettiExtendedDTO  soggettiExtendedDTO  = soggettiDAO.loadSoggettoById(gruppoSoggettoDTO.getIdSoggetto());
	            for(RecapitiExtendedDTO recapito : soggettiExtendedDTO.getRecapiti()) {
	    				IndirizzoSpedizioneDTO indirizzoSpedizioneDTO = new IndirizzoSpedizioneDTO();
						indirizzoSpedizioneDTO.setIdRecapito(recapito.getIdRecapito());
						indirizzoSpedizioneDTO.setIdGruppoSoggetto(keyHolder.getKey().longValue());
						indirizzoSpedizioneDTO.setDestinatarioPostel(dto.getDesGruppoSoggetto());
						if (recapito.getPresso() != null) {
							if (recapito.getPresso().trim().equals(dto.getDesGruppoSoggetto().trim()) || recapito.getPresso().trim().equals("N.A. N.A.".trim())) {
								indirizzoSpedizioneDTO.setPressoPostel(null);
							}else {
								indirizzoSpedizioneDTO.setPressoPostel("C/O " + recapito.getPresso());
							}
						} else {
							if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals("PF")) 
								indirizzoSpedizioneDTO.setPressoPostel("C/O " + soggettiExtendedDTO.getCognome() + " " + soggettiExtendedDTO.getNome());
							
							if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals("PB")
									|| soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals("PG")) 
								indirizzoSpedizioneDTO.setPressoPostel("C/O " + soggettiExtendedDTO.getDenSoggetto());
							
						}
						indirizzoSpedizioneDTO.setIndirizzoPostel(recapito.getIndirizzo() + ", "
								+ recapito.getNumCivico());
						if (recapito.getNazioneRecapito().getDenomNazione().equals("ITALIA")) {
							//DP aggiunto controllo null comune recapito
							if(recapito.getComuneRecapito()!=null) {
							  indirizzoSpedizioneDTO.setCittaPostel(recapito.getComuneRecapito().getDenomComune());
							}
						} else {
							indirizzoSpedizioneDTO.setCittaPostel(recapito.getCittaEsteraRecapito());
						}
						if (recapito.getNazioneRecapito().getDenomNazione().equals("ITALIA")) {
							//DP aggiunto controllo null comune recapito
							if(recapito.getComuneRecapito()!=null) {
							indirizzoSpedizioneDTO.setProvinciaPostel(
									recapito.getComuneRecapito().getProvincia().getSiglaProvincia());
							}
						}
						indirizzoSpedizioneDTO.setCapPostel(recapito.getCapRecapito());
						indirizzoSpedizioneDTO.setFrazionePostel(recapito.getDesLocalita());
//						if (!recapito.getNazioneRecapito().getDenomNazione().equals("ITALIA")) {
//							indirizzoSpedizioneDTO.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
//						}else {
							indirizzoSpedizioneDTO.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
//						}
						indirizzoSpedizioneDTO.setGestAttoreIns(dto.getGestAttoreIns());
						indirizzoSpedizioneDTO.setGestAttoreUpd(dto.getGestAttoreUpd());
						indirizzoSpedizioneDTO = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizioneDTO);
						if (indirizzoSpedizioneDTO.getIndValidoPostel().equals(0l)) {
							indirizzoSpedizioneDTO = truncateFieldsErrati(indirizzoSpedizioneDTO);
							indirizzoSpedizioneDTO = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizioneDTO);
							Long idRecapitoPostel = indirizzoSpedizioneDTO.getIdRecapitoPostel();
				        	Map<String, Object> parameters = new HashMap<>();
				        	parameters.put("indValidoPostel",  0l);
				        	parameters.put("idRecapitoPostel", idRecapitoPostel);
					        template.update(QUERY_UPDATE_RECAPITO_POSTEL_IND_VALID_POSTEL,parameters);
						}
	            	
	            }
	            	
            }
    	}else {
    		if(idGruppoSoggettoReferenteNew != idGruppoSoggettoReferenteOld && idGruppoSoggettoReferenteOld != 0) {
	        	// nuova associazione di un NUOVO soggetto referente
	    		final Long idSoggettoReferenteNew = idGruppoSoggettoReferenteNew;
	    		final Long idSoggettoReferenteOld = idGruppoSoggettoReferenteOld;
	        	Map<String, Object> parameters = new HashMap<>();
	        	parameters.put("idSoggettoNew",  idSoggettoReferenteNew);
	        	parameters.put("idGruppoSoggetto", id_gruppo_soggetto);
		        template.update(QUERY_UPDATE_STATO_DEBITORIO,parameters);
		        // si cancellano i vecchi indirizzi di spedizione 
	   			SoggettiExtendedDTO  soggettiExtendedDTOOld  = soggettiDAO.loadSoggettoById(idSoggettoReferenteOld);
	   			for (RecapitiExtendedDTO recapito : soggettiExtendedDTOOld.getRecapiti()) {
		        	Map<String, Object> parametersD= new HashMap<>();
		        	parametersD.put("idGruppoSoggetto",  id_gruppo_soggetto);
		        	parametersD.put("idRecapito", recapito.getIdRecapito());
		   			template.update(QUERY_DELETE_RECAPITO_POSTEL_BY_ID_GRUPPO_ID_RECAPITO,parametersD);
				}
	   			// si aggiunge nuovo indirizzo di spedizione per il nuovo soggetto referente
	            for(GruppiSoggettoDTO gruppoSoggettoDTO : listGruppiSoggettoDTO) {
		   			SoggettiExtendedDTO  soggettiExtendedDTO  = soggettiDAO.loadSoggettoById(gruppoSoggettoDTO.getIdSoggetto());
		            for(RecapitiExtendedDTO recapito : soggettiExtendedDTO.getRecapiti()) {
		    				IndirizzoSpedizioneDTO indirizzoSpedizioneDTO = new IndirizzoSpedizioneDTO();
							indirizzoSpedizioneDTO.setIdRecapito(recapito.getIdRecapito());
							indirizzoSpedizioneDTO.setIdGruppoSoggetto(keyHolder.getKey().longValue());
							indirizzoSpedizioneDTO.setDestinatarioPostel(dto.getDesGruppoSoggetto());

							if (recapito.getPresso() != null) {
								if (recapito.getPresso().trim().equals(dto.getDesGruppoSoggetto().trim()) || recapito.getPresso().trim().equals("N.A. N.A.".trim())) {
									indirizzoSpedizioneDTO.setPressoPostel(null);
								}else {
									indirizzoSpedizioneDTO.setPressoPostel("C/O " + recapito.getPresso());
								}
							} else {
								if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals("PF")) 
									indirizzoSpedizioneDTO.setPressoPostel("C/O " + soggettiExtendedDTO.getCognome() + " " + soggettiExtendedDTO.getNome());
								
								if (soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals("PB")
										|| soggettiExtendedDTO.getTipoSoggetto().getCodTipoSoggetto().equals("PG")) 
									indirizzoSpedizioneDTO.setPressoPostel("C/O " + soggettiExtendedDTO.getDenSoggetto());
								
							}
							
							indirizzoSpedizioneDTO.setIndirizzoPostel(recapito.getIndirizzo() + ", "
									+ recapito.getNumCivico());
							if (recapito.getNazioneRecapito().getDenomNazione().equals("ITALIA")) {
								//DP aggiunto controllo null comune recapito
								if(recapito.getComuneRecapito()!=null) {
								indirizzoSpedizioneDTO.setCittaPostel(recapito.getComuneRecapito().getDenomComune());
								}
							} else {
								indirizzoSpedizioneDTO.setCittaPostel(recapito.getCittaEsteraRecapito());
							}
							if (recapito.getNazioneRecapito().getDenomNazione().equals("ITALIA")) {
								//DP aggiunto controllo null comune recapito
								if(recapito.getComuneRecapito()!=null) {
								indirizzoSpedizioneDTO.setProvinciaPostel(
										recapito.getComuneRecapito().getProvincia().getSiglaProvincia());
								}
							}
							indirizzoSpedizioneDTO.setCapPostel(recapito.getCapRecapito());
							indirizzoSpedizioneDTO.setFrazionePostel(recapito.getDesLocalita());
//							if (!recapito.getNazioneRecapito().getDenomNazione().equals("ITALIA")) {
//								indirizzoSpedizioneDTO.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
//							}else {
								indirizzoSpedizioneDTO.setNazionePostel(recapito.getNazioneRecapito().getDenomNazione());
//							}
							indirizzoSpedizioneDTO.setGestAttoreIns(dto.getGestAttoreIns());
							indirizzoSpedizioneDTO.setGestAttoreUpd(dto.getGestAttoreUpd());
							indirizzoSpedizioneDTO = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizioneDTO);
							if (indirizzoSpedizioneDTO.getIndValidoPostel().equals(0l)) {
								indirizzoSpedizioneDTO = truncateFieldsErrati(indirizzoSpedizioneDTO);
								indirizzoSpedizioneDTO = indirizziSpedizioneDAO.saveIndirizziSpedizione(indirizzoSpedizioneDTO);
								Long idRecapitoPostel = indirizzoSpedizioneDTO.getIdRecapitoPostel();
					        	Map<String, Object> parametersI= new HashMap<>();
					        	parametersI.put("indValidoPostel",  0l);
					        	parametersI.put("idRecapitoPostel", idRecapitoPostel);
					        	parametersI.put("idGruppoSoggetto",  gruppoSoggettoDTO.getIdGruppoSoggetto());
						        template.update(QUERY_UPDATE_RECAPITO_POSTEL_IND_VALID_POSTEL  + QUERY_AND_ID_GRUPPO_SOGGETTO,parametersI);
							}
		            }
	            }


	        }else {
	    		//UPDATE indirizzi spedizione PER UN VECCHIO REFERENTE 
    			if(!gruppo.getDesGruppoSoggetto().equals(dto.getDesGruppoSoggetto())) {
		            for(GruppiSoggettoDTO gruppoSoggettoDTO : listGruppiSoggettoDTO) {
			   			SoggettiExtendedDTO  soggettiExtendedDTO  = soggettiDAO.loadSoggettoById(gruppoSoggettoDTO.getIdSoggetto());
			            for(RecapitiExtendedDTO recapito : soggettiExtendedDTO.getRecapiti()) {
			            	List<IndirizzoSpedizioneDTO> ListIndirizzoSpedizioneDTO =  recapito.getIndirizziSpedizione().stream()
													            			.filter(i -> i.getIdGruppoSoggetto() != null ? i.getIdGruppoSoggetto().equals(gruppoSoggettoDTO.getIdGruppoSoggetto()) : false )
													            			.collect(Collectors.toList());
			            	if(ListIndirizzoSpedizioneDTO != null) {
			            		for (IndirizzoSpedizioneDTO indirizzoSpedizioneDTO : ListIndirizzoSpedizioneDTO) {
	
				            			indirizzoSpedizioneDTO.setDestinatarioPostel(dto.getDesGruppoSoggetto());
				            			try {
										indirizzoSpedizioneDTO = indirizziSpedizioneDAO.updateIndirizziSpedizione(indirizzoSpedizioneDTO, 0L);
										
					        	        }catch(GenericExceptionList gel) {
												indirizzoSpedizioneDTO = truncateFieldsErrati(indirizzoSpedizioneDTO);
												indirizzoSpedizioneDTO = indirizziSpedizioneDAO.updateIndirizziSpedizione(indirizzoSpedizioneDTO, 0L);
												Long idRecapitoPostel = indirizzoSpedizioneDTO.getIdRecapitoPostel();
									        	Map<String, Object> parametersI= new HashMap<>();
									        	parametersI.put("indValidoPostel",  0l);
									        	parametersI.put("idRecapitoPostel", idRecapitoPostel);
									        	parametersI.put("idGruppoSoggetto",  gruppoSoggettoDTO.getIdGruppoSoggetto());
										        template.update(QUERY_UPDATE_RECAPITO_POSTEL_IND_VALID_POSTEL + QUERY_AND_ID_GRUPPO_SOGGETTO,parametersI);
										}
			            			}
								}
								
			            	}
	
			            }
		         }
	        }
    	}
        LOGGER.debug("[GruppiDAOImpl::storeGruppo] END");
        return key.longValue();		
	}
	

	private IndirizzoSpedizioneDTO truncateFieldsErrati(IndirizzoSpedizioneDTO indirizzoSpedizioneDTO) {
	
		if (indirizzoSpedizioneDTO.getDestinatarioPostel() != null) {
			String destinatario = IndirizziSpedizioneDAOImpl.transformString(indirizzoSpedizioneDTO.getDestinatarioPostel());
			indirizzoSpedizioneDTO.setDestinatarioPostel(usingSubstringMethod(destinatario, 100));
		}

		if (indirizzoSpedizioneDTO.getPressoPostel() != null) {
			String presso = IndirizziSpedizioneDAOImpl.transformString(indirizzoSpedizioneDTO.getPressoPostel());
			indirizzoSpedizioneDTO.setPressoPostel(usingSubstringMethod(presso, 100));

		}
		if (indirizzoSpedizioneDTO.getIndirizzoPostel() != null) {
			String indirizzo = IndirizziSpedizioneDAOImpl.transformString(indirizzoSpedizioneDTO.getIndirizzoPostel());
			indirizzoSpedizioneDTO.setIndirizzoPostel(usingSubstringMethod(indirizzo, 100));
		}
		if (indirizzoSpedizioneDTO.getCittaPostel() != null) {
			String citta = IndirizziSpedizioneDAOImpl.transformString(indirizzoSpedizioneDTO.getCittaPostel());
			indirizzoSpedizioneDTO.setCittaPostel(usingSubstringMethod(citta, 90));
		}

		if (indirizzoSpedizioneDTO.getNazionePostel() != null && indirizzoSpedizioneDTO.getNazionePostel().equals("ITALIA")) {
			if (indirizzoSpedizioneDTO.getProvinciaPostel() != null) {
				String provincia = IndirizziSpedizioneDAOImpl.transformString(indirizzoSpedizioneDTO.getProvinciaPostel());
				indirizzoSpedizioneDTO.setProvinciaPostel(usingSubstringMethod(provincia,  3));
			}
		}
		if (indirizzoSpedizioneDTO.getNazionePostel() != null && indirizzoSpedizioneDTO.getNazionePostel().equals("ITALIA")) {
			if (indirizzoSpedizioneDTO.getCapPostel() != null) {
				String cap = IndirizziSpedizioneDAOImpl.transformString(indirizzoSpedizioneDTO.getCapPostel());
				indirizzoSpedizioneDTO.setCapPostel(usingSubstringMethod(cap, 5));
			}

			if (indirizzoSpedizioneDTO.getCapPostel().length() <= 10) {
				String cap = IndirizziSpedizioneDAOImpl.transformString(indirizzoSpedizioneDTO.getCapPostel());
				indirizzoSpedizioneDTO.setCapPostel(usingSubstringMethod(cap, 5));
			}
		}

		if (indirizzoSpedizioneDTO.getFrazionePostel() != null) {
			String frazione = IndirizziSpedizioneDAOImpl.transformString(indirizzoSpedizioneDTO.getFrazionePostel());
			indirizzoSpedizioneDTO.setFrazionePostel(usingSubstringMethod(frazione, 100));
		}

		if (indirizzoSpedizioneDTO.getNazionePostel() != null) {
			String nazione = IndirizziSpedizioneDAOImpl.transformString(indirizzoSpedizioneDTO.getNazionePostel());
			indirizzoSpedizioneDTO.setNazionePostel(usingSubstringMethod(nazione, 60));
		}
		return indirizzoSpedizioneDTO;
	}

	static String usingSubstringMethod(String text, int length) {
	    if (text.length() <= length) {
	        return text;
	    } else {
	        return text.substring(0, length);
	    }
	}
	@Override
	public Long deleteGruppi(Long id_gruppo_soggetto, Boolean confermato) {
        LOGGER.debug("[GruppiDAOImpl::deleteGruppi] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<RiscossioneDTO> list = new ArrayList<RiscossioneDTO>();

        try {
        	Map<String, Object> map = new HashMap<>();
        	map.put("id_gruppo_soggetto", id_gruppo_soggetto);
        	MapSqlParameterSource params = getParameterValue(map);

	        if(!confermato
	        		&& template.queryForObject(QUERY_RISCOSSIONE_BY_GRUPPO, params, Integer.class) > 0) {
	        	return id_gruppo_soggetto;
	        }
	        else {	
	        	template.update(getQuery(QUERY_DELETE_INDIRIZZI_GRUPPO, null, null), params);
	        	template.update(getQuery(QUERY_DELETE_ALL_SOGGETTO_GRUPPO, null, null), params);
	        	return (long)template.update(getQuery(QUERY_DELETE_GRUPPO_SOGGETTO, null, null), params);
	        }

        } catch (DataAccessException e) {
        	LOGGER.error("[GruppiDAOImpl::deleteGruppi] Errore nell'accesso ai dati", e);
            throw e;
        } finally {
            LOGGER.debug("[GruppiDAOImpl::deleteGruppi] END");
        }
	}


	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<GruppiDTO> getRowMapper() throws SQLException {
		return new GruppiRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return new GruppiExtendedRowMapper(); // does it make any sense to have an extended rowMapper that is the same a not extended?
	}
	
    /**
     * The type soggetti gruppo row mapper.
     */
    public static class GruppiRowMapper implements RowMapper<GruppiDTO> {

        /**
         * Instantiates a new Tipo soggetti row mapper.
         *
         * @throws SQLException the sql exception
         */
        public GruppiRowMapper() throws SQLException {
            // Instantiate class
        }

        /**
         * Implementations must implement this method to map each row of data
         * in the ResultSet. This method should not call {@code next()} on
         * the ResultSet; it is only supposed to map values of the current row.
         *
         * @param rs     the ResultSet to map (pre-initialized for the current row)
         * @param rowNum the number of the current row
         * @return the result object for the current row (may be {@code null})
         * @throws SQLException if a SQLException is encountered getting
         *                      column values (that is, there's no need to catch SQLException)
         */
        @Override
        public GruppiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GruppiDTO bean = new GruppiDTO();
            populateBean(rs, bean);
            return bean;
        }

        protected void populateBean(ResultSet rs, GruppiDTO bean) throws SQLException {
            bean.setIdGruppoSoggetto(rs.getLong("id_gruppo_soggetto"));
            bean.setCodGruppoSoggetto(rs.getString("cod_gruppo_soggetto"));
            bean.setCodGruppoFonte(rs.getString("cod_gruppo_fonte"));
            bean.setDesGruppoSoggetto(rs.getString("des_gruppo_soggetto"));
            bean.setIdFonte(rs.getLong("id_fonte"));
            bean.setIdFonteOrigine(rs.getLong("id_fonte_origine"));
            bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
            bean.setGestDataIns(rs.getDate("gest_data_ins"));
            bean.setGestDataUpd(rs.getDate("gest_data_upd"));
            bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
            bean.setGestUid(rs.getString("gest_uid"));
            bean.setDataAggiornamento(rs.getString("data_aggiornamento"));
            bean.setDataCancellazione(rs.getString("data_cancellazione"));
        }
        
          
        
    }

    /**
     * The type soggetti gruppo row mapper.
     */
    public static class GruppiExtendedRowMapper extends GruppiRowMapper {

        public GruppiExtendedRowMapper() throws SQLException {
        }

        @Override
        public GruppiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	GruppiExtendedDTO bean = new GruppiExtendedDTO();
            populateBean(rs, bean);
            return bean;
        }

        protected void populateBean(ResultSet rs, GruppiExtendedDTO bean) throws SQLException {
        	super.populateBean(rs, bean);
        	
            bean.setFlgCapoGruppo(rs.getLong("flg_capo_gruppo"));
            bean.setIdSoggetto(rs.getLong("id_soggetto"));
        }
        
          
        
    }

	@Override
	public List<GruppiDTO> loadGruppiByIdAmbitoAndCampoRicerca(Long idAmbito, String campoRicerca, String flgTipoRicerca,Integer offset, Integer limit,String sort) {
		  LOGGER.debug("[GruppiDAOImpl::loadGruppiByIdAmbitoAndCampoRicerca] BEGIN");
	        try {
	            Map<String, Object> map = new HashMap<>();
		        map.put("idAmbito", idAmbito);
		        map.put("campoRicerca", campoRicerca);

		        MapSqlParameterSource params = getParameterValue(map);
		        String dynamicOrderByCondition="";
		        if (StringUtils.isNotBlank(sort)) {
		            dynamicOrderByCondition = mapSortConCampiDB(sort);
//		     		if (sort.contains("soggettoReferente")) {
//		     			sort = "numeroComponenti";
//		     		} 
		        }


		        if(flgTipoRicerca.equals("G")) {
		        	List<GruppiDTO> gruppi = template.query(getQuery(QUERY_LOAD_GRUPPI_BY_ID_AMBITO_AND_CAMPO_RICERCA+ dynamicOrderByCondition, null,null), params, getRowMapper());
		        	//group by id gruppo soggetto
		        	gruppi = gruppi.stream().filter(Utils.distinctByKey(b -> b.getIdGruppoSoggetto())).collect(Collectors.toList());
		        	if(gruppi != null) {
			            for(GruppiDTO gruppo : CollectionUtils.emptyIfNull(gruppi)) {
			            	List<GruppiSoggettoDTO>  listGruppiSoggetto = soggettoGruppoDAO.loadSoggettoGruppoByIdGruppoSoggetto(gruppo.getIdGruppoSoggetto());
			            	gruppo.setComponentiGruppo(listGruppiSoggetto);
			            }
				        //parte per sort by numero componenti Gruppo
				        if (StringUtils.isNotBlank(sort)) 
			     		if (sort.contains("numeroComponenti")) {
			     			if (sort.substring(1).equals("numeroComponenti") && sort.charAt(0) == '-')
			     				gruppi = gruppi.stream().sorted(Comparator.comparing(GruppiDTO::getNumeroComponentiGruppo).reversed()).collect(Collectors.toList());
			     			else
			     				gruppi = gruppi.stream().sorted(Comparator.comparing(GruppiDTO::getNumeroComponentiGruppo)).collect(Collectors.toList());
				     		
			     		}
				        if(offset != null && limit != null) {
				        	offset = (offset - 1) * 10;
				        	gruppi = gruppi.stream().skip(offset).limit(limit).collect(Collectors.toList());
				        }
				    		
				    	return gruppi;
		        }
		        	

	        	} else {
	        		List<GruppiDTO> gruppi = template.query(getQuery( QUERY_LOAD_GRUPPI_BY_ID_AMBITO_AND_CAMPO_RICERCA+QUERY_LOAD_GRUPPI_BY_ID_AMBITO_AND_CAMPO_RICERCA_FULL+ dynamicOrderByCondition,null, null), params, getRowMapper());


		        	//group by id gruppo soggetto
		        	gruppi = gruppi.stream().filter(Utils.distinctByKey(b -> b.getIdGruppoSoggetto())).collect(Collectors.toList());
	        		
	        		
	        		if(gruppi != null) {
			            for(GruppiDTO gruppo : CollectionUtils.emptyIfNull(gruppi)) {
			            	List<GruppiSoggettoDTO>  listGruppiSoggetto = soggettoGruppoDAO.loadSoggettoGruppoByIdGruppoSoggetto(gruppo.getIdGruppoSoggetto());
			            	gruppo.setComponentiGruppo(listGruppiSoggetto);
			            }
		        		//parte per sort by numero componenti Gruppo
			            if (StringUtils.isNotBlank(sort)) 
			     		if (sort.contains("numeroComponenti")) {
			     			if (sort.substring(1).equals("numeroComponenti") && sort.charAt(0) == '-')
			     				gruppi = gruppi.stream().sorted(Comparator.comparing(GruppiDTO::getNumeroComponentiGruppo).reversed()).collect(Collectors.toList());
			     			else
			     				gruppi = gruppi.stream().sorted(Comparator.comparing(GruppiDTO::getNumeroComponentiGruppo)).collect(Collectors.toList());
				     		
			     		}
				        if(offset != null && limit != null) {
				        	offset = (offset - 1) * 10;
				        	gruppi = gruppi.stream().skip(offset).limit(limit).collect(Collectors.toList());
				        }
				    	return gruppi;
		        }

	        	}
	        } catch (DataAccessException e) {
	            LOGGER.error("[GruppiDAOImpl::loadGruppiSoggetto] Errore nell'accesso ai dati", e);
	        } catch (SQLException e) {
	            LOGGER.error("[GruppiDAOImpl::loadGruppiSoggetto] Errore nella query ", e);
			} catch (Exception e) {
	            LOGGER.error("[GruppiDAOImpl::loadGruppiSoggetto] Errore nella query ", e);
			} finally {
	            LOGGER.debug("[GruppiDAOImpl::loadGruppiSoggetto] END");
	        }

	        return new ArrayList<GruppiDTO>();
	}
	private String mapSortConCampiDB(String sort) {
        String nomeCampo="";
     		if (sort.contains("nomeGruppo")) {
     			if(sort.substring(1).equals("nomeGruppo") && sort.charAt(0) == '-')
     			 nomeCampo = sort.substring(0, 1).concat("des_gruppo_soggetto");
     			else
     			 nomeCampo = "des_gruppo_soggetto";
     			
     			return getQuerySortingSegment(nomeCampo);
     		}
     		if (sort.contains("soggettoReferente")) {
     			if (sort.substring(1).equals("soggettoReferente") && sort.charAt(0) == '-') {
	     			 nomeCampo = sort.substring(0, 1).concat("nome_cognome");
	      			return getQuerySortingSegment("-flg_capo_gruppo").concat(getQuerySortingSegment(nomeCampo).replace(" ORDER BY ",","));
     			}else {
        			 nomeCampo= "nome_cognome";
          			return getQuerySortingSegment("-flg_capo_gruppo").concat(getQuerySortingSegment(nomeCampo).replace(" ORDER BY ",","));
     			}
     		}
     		if (sort.contains("codiceFiscaleReferente")) {
     			if (sort.substring(1).equals("codiceFiscaleReferente") && sort.charAt(0) == '-')
     			    nomeCampo = sort.substring(0, 1).concat("cf_soggetto");
     			else
     				nomeCampo = "cf_soggetto";
     			
     			return getQuerySortingSegment(nomeCampo);
     		}   

     		return nomeCampo;

	}

	@Override
	public Integer countGruppiByIdAmbitoAndCampoRicerca(Long idAmbito, String campoRicerca, String flgTipoRicerca) {
		  LOGGER.debug("[GruppiDAOImpl::countGruppiByIdAmbitoAndCampoRicerca] BEGIN");
	        try {
	            Map<String, Object> map = new HashMap<>();
		        map.put("idAmbito", idAmbito);
		        map.put("campoRicerca", campoRicerca);
		        MapSqlParameterSource params = getParameterValue(map);
		        
		        if(flgTipoRicerca.equals("G")) {
					return template.queryForObject(QUERY_COUNT_GRUPPI_BY_ID_AMBITO_AND_CAMPO_RICERCA, params, Integer.class);
	        	} else {
	        		return template.queryForObject(QUERY_COUNT_GRUPPI_BY_ID_AMBITO_AND_CAMPO_RICERCA_FULL, params, Integer.class);
		        }


	        	
	        } catch (DataAccessException e) {
	            LOGGER.error("[GruppiDAOImpl::countGruppiByIdAmbitoAndCampoRicerca] Errore nell'accesso ai dati", e);
	        } finally {
	            LOGGER.debug("[GruppiDAOImpl::countGruppiByIdAmbitoAndCampoRicerca] END");
	        }
			return null;
	}

	@Override
	public List<GruppiDTO> loadGruppiSoggettoByIdGruppo(List<Long> idGruppo) throws Exception {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        List<GruppiDTO> res = new ArrayList<>();
		    Map<String, Object> map = new HashMap<>();
		    map.put("idGruppoSoggetto", idGruppo);
			try {
				res = findSimpleDTOListByQuery(className, methodName, QUERY_LOAD_GRUPPI_SOGGETTO, map);
		        if(res != null && !res.isEmpty()) {
		            for (GruppiDTO gruppiDTO : res) {
		            	List<GruppiSoggettoDTO>  listGruppiSoggetto = soggettoGruppoDAO.loadSoggettoGruppoByIdGruppoSoggetto(gruppiDTO.getIdGruppoSoggetto());
		            	gruppiDTO.setComponentiGruppo(listGruppiSoggetto);
		            }
		         }
		        
			} catch (Exception e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
				throw  e;
			} finally {
		        LOGGER.debug(getClassFunctionEndInfo(className, methodName));
			}
			return res;
	}

}
