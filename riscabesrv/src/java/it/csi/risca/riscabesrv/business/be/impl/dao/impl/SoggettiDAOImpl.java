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
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.DatiInputErratiException;
import it.csi.risca.riscabesrv.business.be.exception.GenericException;
import it.csi.risca.riscabesrv.business.be.exception.SystemException;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.NazioneDTO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ComuniDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.FonteDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.GruppiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RecapitoPostelDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneRecapitoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.SoggettoGruppoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoDebitorioDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiNaturaGiuridicaDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiSoggettoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper.AccertamentoDaoRowMapper;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.mapper.SoggettoDaoRowMapper;
import it.csi.risca.riscabesrv.dto.AccertamentoDTO;
import it.csi.risca.riscabesrv.dto.DetailDTO;
import it.csi.risca.riscabesrv.dto.ErrorObjectDTO;
import it.csi.risca.riscabesrv.dto.FonteDTO;
import it.csi.risca.riscabesrv.dto.GruppiDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.RecapitiExtendedDTO;
import it.csi.risca.riscabesrv.dto.SoggettiDTO;
import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipiNaturaGiuridicaDTO;
import it.csi.risca.riscabesrv.dto.TipiSoggettoDTO;
import it.csi.risca.riscabesrv.dto.VerifyRiscossioneStatoDebDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type Soggetti dao impl.
 *
 * @author CSI PIEMONTE
 */
public class SoggettiDAOImpl extends RiscaBeSrvGenericDAO<SoggettiExtendedDTO> implements SoggettiDAO {
	
    private final String className = this.getClass().getSimpleName();
    
    @Autowired
    private RiscossioneDAO riscossioneDAO;
	
    @Autowired
    private MessaggiDAO messaggiDAO;
    
    @Autowired
    private BusinessLogic  businessLogic;
    
    @Autowired
    private TipiSoggettoDAO tipiSoggettoDAO;
    
    @Autowired
    private TipiNaturaGiuridicaDAO tipiNaturaGiuridicaDAO;
    
    @Autowired
    private ComuniDAO comuniDAO;
    
    @Autowired
    private FonteDAO fonteDAO;
    
    @Autowired
    private SoggettoGruppoDAO soggettoGruppoDAO;
    
    @Autowired
    private GruppiDAO gruppiDAO;
    
    @Autowired	
    private RiscossioneRecapitoDAO riscossioneRecapitoDAO;
    
    @Autowired	
    private RecapitoPostelDAO recapitoPostelDAO;
    
    @Autowired
    private StatoDebitorioDAO statoDebitorioDAO;
    
	private static final String QUERY_LOAD_SOGGETTO_BY_ID_AMB_AND_TIPO_SOGG_AND_CF = "SELECT rts.* FROM risca_t_soggetto rts "
			+ "INNER JOIN risca_d_ambito rda ON rts.id_ambito = rda.id_ambito "
			+ "WHERE rts.id_ambito = :idAmbito "
			+ "AND rts.id_tipo_soggetto = :idTipoSoggetto "
			+ "AND rts.cf_soggetto = :cfSoggetto "
			+ "AND rts.data_cancellazione is null";

	private static final String QUERY_LOAD_SOGGETTO_BY_ID = "SELECT rts.* FROM risca_t_soggetto rts WHERE rts.id_soggetto = :idSoggetto";
	
	private static final String QUERY_GET_CAMPI_SOGGETTO_BY_ID = "SELECT rts.cf_soggetto, rts.den_soggetto, rts.cognome,"
			+ " rts.nome,rts.id_tipo_soggetto, rdts.cod_tipo_soggetto FROM risca_t_soggetto rts "
			+ " inner join risca_d_tipo_soggetto rdts on rts.id_tipo_soggetto = rdts.id_tipo_soggetto"
			+ " WHERE rts.id_soggetto = :idSoggetto";
	
    private static final String QUERY_INSERT_SOGGETTO = "INSERT INTO risca_t_soggetto (id_soggetto, id_ambito, id_tipo_soggetto, cf_soggetto, id_tipo_natura_giuridica, id_fonte_origine, id_fonte, nome, " + 
            "cognome, den_soggetto, partita_iva_soggetto, data_nascita_soggetto, id_comune_nascita, id_stato_nascita, citta_estera_nascita, gest_attore_ins,  " + 
    		"gest_data_ins, gest_data_upd, gest_attore_upd, gest_uid, data_aggiornamento, data_cancellazione)  " + 
    		"VALUES(nextval('seq_risca_t_soggetto'), :idAmbito, :idTipoSoggetto, :cfSoggetto, :idTipoNaturaGiuridica, :idFonteOrigine, :idFonte, :nome, :cognome,  " + 
    		":denSoggetto, :partitaIvaSoggetto, :dataNascitaSoggetto, :idComuneNascita, :idStatoNascita, :cittaEsteraNascita, :gestAttoreIns, :gestDataIns,  " + 
    		":gestDataUpd, :gestAttoreUpd, :gestUid, :dataAggiornamento, :dataCancellazione);";

    private static final String QUERY_UPDATE_SOGGETTO = "UPDATE risca_t_soggetto SET id_ambito = :idAmbito, id_tipo_soggetto = :idTipoSoggetto, cf_soggetto = :cfSoggetto, id_tipo_natura_giuridica = :idTipoNaturaGiuridica, id_fonte_origine = :idFonteOrigine,  " + 
    		"id_fonte = :idFonte, nome = :nome, cognome = :cognome,  " + 
    		"den_soggetto = :denSoggetto, partita_iva_soggetto = :partitaIvaSoggetto, data_nascita_soggetto = :dataNascitaSoggetto, id_comune_nascita = :idComuneNascita,  " + 
    		"id_stato_nascita = :idStatoNascita, citta_estera_nascita = :cittaEsteraNascita,  " + 
    		"gest_data_upd = :gestDataUpd, gest_attore_upd = :gestAttoreUpd, gest_uid = :gestUid, data_aggiornamento = :dataAggiornamento, data_cancellazione = :dataCancellazione "
    		+ "WHERE id_soggetto = :idSoggetto;";
    
    private static final String QUERY_DELETE_SOGGETTO = "DELETE FROM risca_t_soggetto rts WHERE rts.id_soggetto = :idSoggetto"; 
    
	private static final String QUERY_LOAD_SOGGETTO_BY_CAMPO_RICERCA = "SELECT distinct rts.* ,rrr.id_tipo_recapito, rrr.indirizzo, rrr.id_tipo_invio, rrr.email, rrr.pec ,"
			+ "CASE WHEN rts.den_soggetto is not null THEN (rts.den_soggetto|| ' ' ||  rts.cognome|| ' ' || rts.nome)  "
			+ "   WHEN rts.den_soggetto is  null THEN (rts.cognome|| ' ' ||  rts.nome) END "
			+ " FROM risca_t_soggetto rts "
			+ "INNER JOIN risca_d_ambito rda ON rts.id_ambito = rda.id_ambito "
			+ "LEFT JOIN risca_r_recapito rrr on rrr.id_soggetto =rts.id_soggetto "
			+ "WHERE rts.id_ambito = :idAmbito "
			+ "AND (rts.cf_soggetto ilike '%'||:campoRicerca||'%' "
			+ "OR rts.partita_iva_soggetto ilike '%'||:campoRicerca||'%' "
			+ "OR rts.den_soggetto ilike '%'||:campoRicerca||'%' "
			+ "OR rts.cognome ilike '%'||:campoRicerca||'%' "
			+ "OR rrr.pec ilike '%'||:campoRicerca||'%') "
			+ "AND rts.data_cancellazione is null "
			+ "AND rrr.data_cancellazione is null";
	
	private static final String QUERY_COUNT_SOGGETTO_BY_CAMPO_RICERCA = "SELECT COUNT( distinct t.id_soggetto) FROM ("+QUERY_LOAD_SOGGETTO_BY_CAMPO_RICERCA+") AS t";
	
	@Override
	public SoggettiExtendedDTO loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg(Long idAmbito, Long idTipoSoggetto,
	        String cfSoggetto) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		SoggettiExtendedDTO soggetto = null;
		try {
			soggetto = loadSoggettoData(idAmbito, idTipoSoggetto, cfSoggetto);
			if (soggetto != null) {
				try {
					loadAdditionalData(soggetto);
				} catch (DataAccessException | DAOException | SQLException e) {
					LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
					throw e;
				}
			}
		} catch (DataAccessException | SQLException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw e;
		}finally {
		    LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return soggetto;
	}

	private SoggettiExtendedDTO loadSoggettoData(Long idAmbito, Long idTipoSoggetto, String cfSoggetto) throws DataAccessException, SQLException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Object> map = new HashMap<>();
	    map.put("idAmbito", idAmbito);
	    map.put("idTipoSoggetto", idTipoSoggetto);
	    map.put("cfSoggetto", cfSoggetto);
	    MapSqlParameterSource params = getParameterValue(map);
	    try {
			return template.queryForObject(QUERY_LOAD_SOGGETTO_BY_ID_AMB_AND_TIPO_SOGG_AND_CF, params, getRowMapper());
		} catch (EmptyResultDataAccessException e) {
		    LOGGER.debug(getClassFunctionDebugString(className, methodName," Nessun soggetto presente"));
			return null;
		}
	}

	private void loadAdditionalData(SoggettiExtendedDTO soggetto) throws DataAccessException, SQLException, DAOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    
		List<RecapitiExtendedDTO> listRecapiti = businessLogic.readRecapitiByIdSoggetto(soggetto.getIdSoggetto());
		soggetto.setRecapiti(listRecapiti);
		
		TipiSoggettoDTO tipiSoggetto = tipiSoggettoDAO.loadTipiSoggettoByIdOrCodTipoSoggetto(String.valueOf(soggetto.getIdTipoSoggetto()));
		soggetto.setTipoSoggetto(tipiSoggetto);
		if(soggetto.getIdTipoNaturaGiuridica() != null) {
			TipiNaturaGiuridicaDTO tipiNaturaGiuridica = tipiNaturaGiuridicaDAO.loadTipoNaturaGiuridicaByIdOrCod(String.valueOf(soggetto.getIdTipoNaturaGiuridica()));
			soggetto.setTipiNaturaGiuridica(tipiNaturaGiuridica);
		}
		ComuneExtendedDTO comuneNascita = null;
		if(soggetto.getIdComuneNascita() != null) {
			comuneNascita = comuniDAO.loadComuneById(Long.valueOf(soggetto.getIdComuneNascita()));
			soggetto.setComuneNascita(comuneNascita);
		}
		NazioneDTO nazioneNascita = null;
		if (comuneNascita != null) {
			nazioneNascita = comuneNascita.getProvincia() != null && comuneNascita.getProvincia().getRegione() != null
					? comuneNascita.getProvincia().getRegione().getNazione()
					: null;
		}
		soggetto.setNazioneNascita(nazioneNascita);
		if(soggetto.getIdFonte() != null) {
			FonteDTO fonteSoggetto = fonteDAO.loadFonteByCodFonte(String.valueOf(soggetto.getIdFonte()));
			soggetto.setFonte(fonteSoggetto);
		}

		
		List<GruppiDTO> gruppoSoggetto = new ArrayList<GruppiDTO>();
		List<Long> listIdSoggettoGruppo= null;
		try {
			listIdSoggettoGruppo = soggettoGruppoDAO.loadIdSoggettoGruppoByIdSoggetto(soggetto.getIdSoggetto());
			if (listIdSoggettoGruppo != null && !listIdSoggettoGruppo.isEmpty()) {
				gruppoSoggetto = gruppiDAO.loadGruppiSoggettoByIdGruppo(listIdSoggettoGruppo);
				soggetto.setGruppoSoggetto(gruppoSoggetto);
			}
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw new DAOException(Constants.ERRORE_GENERICO);
			
		}
	}


	@Override
	public SoggettiExtendedDTO loadSoggettoById(Long idSoggetto) throws SQLException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    SoggettiExtendedDTO soggetto = new SoggettiExtendedDTO();
        try {     	
            Map<String, Object> map = new HashMap<>();
	        map.put("idSoggetto", idSoggetto);
	        MapSqlParameterSource params = getParameterValue(map);
	        try {
				soggetto = template.queryForObject(QUERY_LOAD_SOGGETTO_BY_ID, params, getRowMapper());
			}catch (EmptyResultDataAccessException e) {
			    LOGGER.debug(getClassFunctionDebugString(className, methodName," Nessun soggetto presente"));
				return null;
			}  catch (Exception e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);

			}
	        if (soggetto != null) {
      	        try {
      				loadAdditionalData(soggetto);
      			} catch (DataAccessException | DAOException | SQLException e) {
      				LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
      	           throw e;
      			}
      	    }
	        return soggetto;

        } catch (SQLException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
           throw e;
        } catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
            throw e;
        } catch (DAOException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
            throw new SQLException(e);
		} finally {
		    LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
	}
    
	@Override
	public Long saveSoggetto(SoggettiExtendedDTO dto, String fruitore) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    try {
            validateSoggetto(dto);
            Map<String, Object> map = new HashMap<>();
            
            SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");

            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();
            map.put("idAmbito", dto.getIdAmbito());
            map.put("idTipoSoggetto", dto.getTipoSoggetto() !=null ? dto.getTipoSoggetto().getIdTipoSoggetto() : dto.getIdTipoSoggetto() );
            map.put("cfSoggetto", dto.getCfSoggetto()); 

	        MapSqlParameterSource params = getParameterValue(map);
	        
	        List<SoggettiExtendedDTO> list = template.query(QUERY_LOAD_SOGGETTO_BY_ID_AMB_AND_TIPO_SOGG_AND_CF, params, getRowMapper());
            if(!list.isEmpty()) {
				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E074);
				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
            }
            if(dto.getTipiNaturaGiuridica() != null)
            	map.put("idTipoNaturaGiuridica", dto.getTipiNaturaGiuridica().getIdTipoNaturaGiuridica()); 
            else
            	map.put("idTipoNaturaGiuridica", null);
            
            map.put("idFonte", dto.getIdFonte() != null ? dto.getIdFonte() :  1L);
            
            map.put("idFonteOrigine", dto.getIdFonteOrigine() != null ? dto.getIdFonteOrigine() : 1L);
      
            map.put("nome", dto.getNome());
            map.put("cognome", dto.getCognome());
            map.put("denSoggetto", dto.getDenSoggetto());          
            map.put("partitaIvaSoggetto", dto.getPartitaIvaSoggetto());

            try {
				String dataNascSogg = dto.getDataNascitaSoggetto();
				if(dataNascSogg != null) {
					if(!dataNascSogg.equals("")) {
					
						Date dataNascitaSoggetto = formatter.parse(dataNascSogg);
						map.put("dataNascitaSoggetto", dataNascitaSoggetto);
					}
					else {
						map.put("dataNascitaSoggetto", null);
					}
				} 
				else {
					map.put("dataNascitaSoggetto", null);
				}
				map.put("dataAggiornamento", now);
//				
				
				String dataCancel = dto.getDataCancellazione();
				if(dataCancel != null) {
					if(!dataCancel.equals("")) {
					
						Date dataCancellazione = formatter.parse(dataCancel);
						map.put("dataCancellazione", dataCancellazione);
					}
					else {
						map.put("dataCancellazione", null);
					}
				} 
				else {
					map.put("dataCancellazione", null);
				}
				

			} catch (ParseException e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
	            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				throw e;
			}
            
            if(dto.getComuneNascita() != null)
            	map.put("idComuneNascita", dto.getComuneNascita().getIdComune());
            else
            	map.put("idComuneNascita", null);
            if(dto.getNazioneNascita() != null)
            	map.put("idStatoNascita", dto.getNazioneNascita().getIdNazione());
            else
            	map.put("idStatoNascita",null);
            map.put("cittaEsteraNascita", dto.getCittaEsteraNascita());
            map.put("gestDataIns", now);
            map.put("gestAttoreIns", dto.getGestAttoreIns());
            map.put("gestDataUpd", now);
            map.put("gestAttoreUpd", dto.getGestAttoreUpd());
//            String uid = generateGestUID(dto.getCfAccredito() + dto.getDesEmailAccredito() + now);
            map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
             params = getParameterValue(map);
            KeyHolder keyHolder = new GeneratedKeyHolder();

            template.update(getQuery(QUERY_INSERT_SOGGETTO, null, null), params, keyHolder, new String[]{"id_soggetto"});
            Number key = keyHolder.getKey();

	        map.put("idSoggetto", key.longValue());

            for(int i=0; i<dto.getRecapiti().size(); i++) {
            	dto.getRecapiti().get(i).setIdSoggetto(key.longValue());
	            if(dto.getRecapiti().get(i).getComuneRecapito() != null) {
	            	Long idComune= dto.getRecapiti().get(i).getComuneRecapito().getIdComune();
	            	//DP sposto il controllo sul fruitore == null per il controllo sul comune valido	            	
	            		params = getParameterValue(map);	    
	            	if(fruitore == null) {	
	            		ComuneExtendedDTO comuneValido = comuniDAO.loadComuneById(idComune);
	            		if(comuneValido.getDataFineValidita() != null) {
	        				LOGGER.error(getClassFunctionErrorInfo(className, methodName,  "Comune non valido"));
	        				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E085);
	        				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
	            		}
	            	}
	            }	
            }
        	businessLogic.insertUpdateAllRecapiti(dto.getRecapiti(), null, dto, null);
            return key.longValue();

        } 
        catch (BusinessException be) {
			throw be;
		}
        catch (DatiInputErratiException dire) {
        	if(dire.getError() != null) {
    			LOGGER.error(getClassFunctionErrorInfo(className, methodName, dire.getError().getCode()), dire);
        		dire.getError().setDetail(new DetailDTO(dto));
        	}
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        	throw new GenericException(dire.getError());
        }
            
        catch (GenericException ge) {
        	if(ge.getErroObjectDTO() != null)
    			LOGGER.error(getClassFunctionErrorInfo(className, methodName, ge.getErroObjectDTO().getCode()), ge);
        	else
    			LOGGER.error(getClassFunctionErrorInfo(className, methodName, ge), ge);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        	throw ge;
        }
        catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        	throw e;
        } finally {
		    LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
	}
	
	@Override
	public Long updateSoggetto(SoggettiExtendedDTO dto,String fruitore, Long indModManuale) throws GenericException, Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        validateSoggetto(dto);
        
        try {
        	 Map<String, Object> map = new HashMap<>();
             
             SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
             
             Calendar cal = Calendar.getInstance();
             Date now = cal.getTime();
             
             map.put("idSoggetto", dto.getIdSoggetto());
             map.put("idAmbito", dto.getIdAmbito());
             map.put("idTipoSoggetto", dto.getTipoSoggetto().getIdTipoSoggetto());
             map.put("cfSoggetto", dto.getCfSoggetto()); 
             if(dto.getTipiNaturaGiuridica() != null)
             	map.put("idTipoNaturaGiuridica", dto.getTipiNaturaGiuridica().getIdTipoNaturaGiuridica()); 
             else
             	map.put("idTipoNaturaGiuridica", null);
             map.put("idFonteOrigine", dto.getIdFonteOrigine());
             map.put("idFonte", 1L);
	      
             map.put("nome", dto.getNome());
             map.put("cognome", dto.getCognome());
             map.put("denSoggetto", dto.getDenSoggetto());          
             map.put("partitaIvaSoggetto", dto.getPartitaIvaSoggetto());
        
             try {
 				String dataNascSogg = dto.getDataNascitaSoggetto();
 				if(dataNascSogg != null) {
 					if(!dataNascSogg.equals("")) {
 					
 						Date dataNascitaSoggetto = formatter.parse(dataNascSogg);
 						map.put("dataNascitaSoggetto", dataNascitaSoggetto);
 					}
 					else {
 						map.put("dataNascitaSoggetto", null);
 					}
 				} 
 				else {
 					map.put("dataNascitaSoggetto", null);
 				}
 				
 				map.put("dataAggiornamento", now);
 				dto.setDataAggiornamento(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now));
 				
 				String dataCancel = dto.getDataCancellazione();
 				if(dataCancel != null) {
 					if(!dataCancel.equals("")) {
 					
 						Date dataCancellazione = formatter.parse(dataCancel);
 						map.put("dataCancellazione", dataCancellazione);
 					}
 					else {
 						map.put("dataCancellazione", null);
 					}
 				} 
 				else {
 					map.put("dataCancellazione", null);
 				}
 				

 			} catch (ParseException e) {
    			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);

 			}
             if(dto.getComuneNascita() != null )
            	 map.put("idComuneNascita", dto.getComuneNascita().getIdComune());
             else
            	 map.put("idComuneNascita",null);
             if(dto.getNazioneNascita() != null)
            	 map.put("idStatoNascita", dto.getNazioneNascita().getIdNazione());
             else
            	 map.put("idStatoNascita", null);
             map.put("cittaEsteraNascita", dto.getCittaEsteraNascita());
             map.put("gestDataUpd", now);
             map.put("gestAttoreUpd", dto.getGestAttoreUpd());
//             String uid = generateGestUID(dto.getCfAccredito() + dto.getDesEmailAccredito() + now);
             map.put("gestUid", generateGestUID(dto.getGestAttoreIns() + dto.getGestAttoreUpd() + now));
             MapSqlParameterSource params = getParameterValue(map);
             // recupero soggetto prima di fare update per il controllo dell indirizzi di spedizione 
			 SoggettiExtendedDTO  soggettiExtendedOld  = loadSoggettoById(dto.getIdSoggetto());
			template.update(getQuery(QUERY_UPDATE_SOGGETTO, null, null), params);
			for (int i = 0; i < dto.getRecapiti().size(); i++) {
				//per il nuovo recapito
				
				if(dto.getRecapiti().get(i).getIdSoggetto() == null)
					dto.getRecapiti().get(i).setIdSoggetto(dto.getIdSoggetto());
				
				if (dto.getRecapiti().get(i).getComuneRecapito() != null) {
					Long idComune = dto.getRecapiti().get(i).getComuneRecapito().getIdComune();
					// DP sposto il controllo sul fruitore == null per il controllo sul comune
					// valido
					params = getParameterValue(map);
					if (fruitore == null) {
	            		ComuneExtendedDTO comuneValido = comuniDAO.loadComuneById(idComune);
						if (comuneValido.getDataFineValidita() != null) {	
							LOGGER.error(getClassFunctionErrorInfo(className, methodName,  "Comune non valido"));
	        				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E085);
	        				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));
						}
					}
				}
			}
			businessLogic.insertUpdateAllRecapiti(dto.getRecapiti(), soggettiExtendedOld, dto, indModManuale);
			return dto.getIdSoggetto();
            
        } catch (BusinessException be) {
			throw be;
		} catch (GenericException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
        	throw e;
		}catch (DatiInputErratiException dire) {
        	if(dire.getError() != null) {
    			LOGGER.error(getClassFunctionErrorInfo(className, methodName,dire.getError().getCode()), dire);
        		dire.getError().setDetail(new DetailDTO(dto));
        	}
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        	throw new GenericException(dire.getError());
        }catch (DataAccessException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e), e);
            return null;
        }
        finally {
		    LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
	}
	
	private void validateSoggetto(SoggettiExtendedDTO soggetto) throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        Long idStatoNascita = -1L;
        Long idTipoSoggetto = -1L, idTipoNaturaGiuridica = -1L;
        idTipoSoggetto = soggetto.getIdTipoSoggetto() != null ? soggetto.getIdTipoSoggetto() : soggetto.getTipoSoggetto() != null ? soggetto.getTipoSoggetto().getIdTipoSoggetto() : -1L;

        if(soggetto.getIdTipoNaturaGiuridica() != null)
        	idTipoNaturaGiuridica = soggetto.getIdTipoNaturaGiuridica();
        else if(soggetto.getTipiNaturaGiuridica() != null)
        	idTipoNaturaGiuridica = soggetto.getTipiNaturaGiuridica().getIdTipoNaturaGiuridica();
        else
        	idTipoNaturaGiuridica = -1L;

        
        idStatoNascita = soggetto.getIdStatoNascita() !=null ? soggetto.getIdStatoNascita() : soggetto.getNazioneNascita() != null ? soggetto.getNazioneNascita().getIdNazione() : -1L;
        
        boolean isPF = idTipoSoggetto == 1/*PF*/;
        boolean isNotPB = !isPF && idTipoNaturaGiuridica != 7 /*N7 - impresa privata*/;
        boolean isItalia = idStatoNascita == 1/*IT*/;
        
        HashMap<String, String> invalidFieldsMap = getInvalidMandatoryFields("",
        	"cf_soggetto", soggetto.getCfSoggetto(),
        	"id_tipo_soggetto", soggetto.getTipoSoggetto() == null? false : (idTipoSoggetto != -1),
            "id_tipo_natura_giuridica ", isPF? true : (idTipoNaturaGiuridica != -1),
	        "id_stato_nascita", isNotPB ? true : (idStatoNascita != -1),
            "nome", isNotPB ? true : soggetto.getNome(),
            "cognome", isNotPB ? true : soggetto.getCognome(),
            "den_soggetto", isPF ? true : soggetto.getDenSoggetto(),
	        "partita_iva_soggetto", isPF ? true : soggetto.getPartitaIvaSoggetto(),
	        "id_comune_nascita", soggetto.getComuneNascita() == null || isNotPB || !isItalia ? true : soggetto.getComuneNascita().getIdComune(),
	        "data_nascita_soggetto", isNotPB ? true : soggetto.getDataNascitaSoggetto(),
	        "citta_estera_nascita", isNotPB || isItalia? true : soggetto.getCittaEsteraNascita()
        );

        // fixme: verify "4.5	Algoritmo 4 – Inserisci/modifica/Elimina RECAPITO" for update column!
		for(int j=0; j<soggetto.getRecapiti().size(); j++) {
			RecapitiExtendedDTO recapito = soggetto.getRecapiti().get(j);

	        boolean isItaliaRecap = (recapito.getIdNazioneRecapito() != null && recapito.getIdNazioneRecapito() == 1/*IT*/) 
	        		|| (recapito.getNazioneRecapito() != null && recapito.getNazioneRecapito().getIdNazione() == 1/*IT*/);
	        
	        HashMap<String, String> fieldsMap =  new HashMap<String, String>();
	        //Deve avere id Fonte 1 (RISCA) per fare il controllo sul tipo di invio
	        if(recapito.getIdFonte()!= null && recapito.getIdFonte() == 1) {
				Long tipoInvio = recapito.getTipoInvio() == null ? recapito.getIdTipoInvio() : recapito.getTipoInvio().getIdTipoInvio();
				fieldsMap = getInvalidMandatoryFields("recapiti."+j+".",
					"id_tipo_invio", tipoInvio != null,
					"id_tipo_sede", recapito.getTipoSede() == null ? true : recapito.getTipoSede().getIdTipoSede(),
					"id_nazione_recapito", recapito.getNazioneRecapito() == null ? true : recapito.getNazioneRecapito().getIdNazione(),
					"id_comune_recapito", recapito.getComuneRecapito() == null ? true : recapito.getComuneRecapito().getIdComune(),
					"citta_estera_recapito", isItaliaRecap ? true : recapito.getCittaEsteraRecapito(),
					"indirizzo", recapito.getIndirizzo(),
					//"num_civico", recapito.getNumCivico(),
					"cap_recapito", recapito.getCapRecapito(),
					"email", tipoInvio != 2/*E - eMail*/? true : recapito.getEmail(),
					"pec", tipoInvio != 1/*P - Pec*/? true : recapito.getPec()
				);
	        }

			
			if(!fieldsMap.isEmpty())
				invalidFieldsMap.putAll(fieldsMap);
		}
		
		if(!invalidFieldsMap.isEmpty()) {
			ErrorObjectDTO error = new ErrorObjectDTO("400","E001",
					"errore validazione dati di ingresso",
					null, null,invalidFieldsMap);
			
			for (Map.Entry<String, String> field: invalidFieldsMap.entrySet()) {
	        	LOGGER.error("[SoggettiDAOImpl::validateSoggetto] Campo = " + field.getKey() + " errore = " + field.getValue());
			}
			LOGGER.debug("[SoggettiDAOImpl::validateSoggetto] END");
			throw new GenericException(error);
		}

		
			
        // validate recapiti
    	long howManyRecapitiPrincipale = CollectionUtils.emptyIfNull(soggetto.getRecapiti()).stream()
				.filter(x -> "1".equals(""+x.getIdTipoRecapito()) || "P".equals(""+x.getTipoRecapito().getCodTipoRecapito())).count();
		if(howManyRecapitiPrincipale != 1) {
			ErrorObjectDTO error = new ErrorObjectDTO("400", howManyRecapitiPrincipale == 0?"E029" : "E023",
					"errore associazione obbligatoria di un recapito principale al soggetto",
					null, null,null);
			LOGGER.error("[SoggettiDAOImpl::validateSoggetto] ERROR: errore associazione obbligatoria di un recapito principale al soggetto");
			LOGGER.debug("[SoggettiDAOImpl::validateSoggetto] END");
			throw new GenericException(error);
		}
		LOGGER.debug("[SoggettiDAOImpl::validateSoggetto] END");
	}
	
	@Override
	public SoggettiExtendedDTO deleteSoggetto(Long idSoggetto, Long idRecapito) throws Exception {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        SoggettiExtendedDTO soggetto = new SoggettiExtendedDTO();
        soggetto = loadSoggettoById(idSoggetto);
        try {
			Map<String, Object> map = new HashMap<>();
			map.put("idSoggetto", idSoggetto);
			map.put("idRecapito", idRecapito);
			MapSqlParameterSource params = getParameterValue(map);
			VerifyRiscossioneStatoDebDTO verify;
			try {
				verify = riscossioneDAO.verifyRiscossioniSTDebitori(1L, idRecapito, "A", "D", null);
			} catch (Exception e) {
				throw e;
			}
			if(verify.getNumRiscossioni() > 0 || verify.getNumStatDeb() > 0) {
				
				if(idRecapito != null) {
					recapitoPostelDAO.deleteRecapitoAlternativoPostelByIdRecapito(idRecapito);
					riscossioneRecapitoDAO.deleteRiscossioneRecapitoByIdRecapito(idRecapito);	
					//count su stato debitorio per idRecapito 
					Integer countStatoDebitorio = statoDebitorioDAO.countStatoDebitorioByIdRecapito(idRecapito);
					
					if(countStatoDebitorio > 0 ) {
						MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio("E118");
        				throw new BusinessException(400, messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()));										
					}
					
					businessLogic.deleteRecapitoAlternativoByIdRecapito(idRecapito);								
					
					return soggetto;
				}

			} else if(idRecapito != null){
				recapitoPostelDAO.deleteRecapitoAlternativoPostelByIdRecapito(idRecapito);
				businessLogic.deleteRecapitoAlternativoByIdRecapito(idRecapito);
				
				return soggetto;
			} else {
				recapitoPostelDAO.deleteRecapitoAlternativoPostelByIdSoggetto(idSoggetto);
				soggettoGruppoDAO.deleteSoggettoGruppoByIdSoggetto(idSoggetto);
				businessLogic.deleteRecapitiByIdSoggetto(idSoggetto);
				template.update(getQuery(QUERY_DELETE_SOGGETTO, null, null), params);
				return soggetto;
			}
        } catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e), e);
            return soggetto;
        } finally {
    	    LOGGER.debug(getClassFunctionEndInfo(className, methodName)); 
    	}
		return soggetto;
	}

	@Override
	public List<SoggettiExtendedDTO> loadSoggettiByCampoRicerca(Long idAmbito, String campoRicerca,Integer offset, Integer limit,String sort) throws SQLException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    List<SoggettiExtendedDTO> listSoggetto = new ArrayList<SoggettiExtendedDTO>();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("idAmbito", idAmbito);
			map.put("campoRicerca", campoRicerca);
			MapSqlParameterSource params = getParameterValue(map);
			String dynamicOrderByCondition = "";
			if (StringUtils.isNotBlank(sort)) {
				dynamicOrderByCondition = mapSortConCampiDB(sort);
			}
			List<SoggettiExtendedDTO> listSoggettoAll = template.query(
					getQuery(QUERY_LOAD_SOGGETTO_BY_CAMPO_RICERCA + dynamicOrderByCondition, null, null), params,
					getRowMapper());

			listSoggetto = listSoggettoAll.stream().filter(Utils.distinctByKey(b -> b.getIdSoggetto()))
					.collect(Collectors.toList());
			if (offset != null && limit != null) {
				offset = (offset - 1) * 10;
				listSoggetto = listSoggetto.stream().skip(offset).limit(limit).collect(Collectors.toList());
			}
			if (listSoggetto != null && !listSoggetto.isEmpty()) {
				for (int i = 0; i < listSoggetto.size(); i++) {

					if (listSoggetto.get(i) != null) {
						try {
							loadAdditionalData(listSoggetto.get(i));
						} catch (DataAccessException | DAOException | SQLException e) {
							LOGGER.error(getClassFunctionErrorInfo(className, methodName,e), e);

							throw e;
						}
					}
				}
			}

			return listSoggetto;

		} catch (SQLException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e), e);
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e), e);
			throw e;
		} catch (DAOException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName,e), e);
			throw new SQLException(e);
		} finally {
    	    LOGGER.debug(getClassFunctionEndInfo(className, methodName));        
		}
	}
	
	private String mapSortConCampiDB(String sort) {
		String nomeCampo = "";
		if (sort.contains("tipoSoggetto")) {
			if (sort.substring(1).equals("tipoSoggetto") && sort.charAt(0) == '-')
				nomeCampo = sort.substring(0, 1).concat("rts.id_tipo_soggetto");
			else
				nomeCampo = "rts.id_tipo_soggetto";
			return getQuerySortingSegment(nomeCampo);
		}
		if (sort.contains("soggetto")) {
			if (sort.substring(1).equals("soggetto") && sort.charAt(0) == '-')
				nomeCampo = sort.substring(0, 1).concat(
						"CASE WHEN rts.den_soggetto is not null THEN  rts.den_soggetto|| ' ' ||  rts.cognome|| ' ' || rts.nome "
								+ " WHEN rts.den_soggetto is  null THEN rts.cognome|| ' ' ||  rts.nome " + " END ");
			else
				nomeCampo = "CASE WHEN rts.den_soggetto is not null THEN rts.den_soggetto|| ' ' ||  rts.cognome|| ' ' || rts.nome "
						+ " WHEN rts.den_soggetto is  null THEN rts.cognome|| ' ' ||  rts.nome " + " END ";
			return getQuerySortingSegment(nomeCampo);
		}
		if (sort.contains("codiceFiscale")) {
			if (sort.substring(1).equals("codiceFiscale") && sort.charAt(0) == '-')
				nomeCampo = sort.substring(0, 1).concat("rts.cf_soggetto");
			else
				nomeCampo = "rts.cf_soggetto";
			return getQuerySortingSegment(nomeCampo);
		}
		if (sort.contains("partitaIva")) {
			if (sort.substring(1).equals("partitaIva") && sort.charAt(0) == '-')
				nomeCampo = sort.substring(0, 1).concat("rts.partita_iva_soggetto");
			else
				nomeCampo = "rts.partita_iva_soggetto";
			return getQuerySortingSegment(nomeCampo);
		}
		if (sort.contains("indirizzo")) {
			if (sort.substring(1).equals("indirizzo") && sort.charAt(0) == '-') {
				nomeCampo = sort.substring(0, 1).concat("rrr.indirizzo");
				String idRecapito = getQuerySortingSegment(sort.substring(0, 1).concat("id_tipo_recapito"));
				return idRecapito.concat(getQuerySortingSegment(nomeCampo).replace(" ORDER BY ", ","));
			} else {
				String idRecapito = getQuerySortingSegment("id_tipo_recapito");
				return idRecapito.concat(getQuerySortingSegment("rrr.indirizzo").replace(" ORDER BY ", ","));
			}

		}
		if (sort.contains("tipoInvio")) {

			if (sort.substring(1).equals("tipoInvio") && sort.charAt(0) == '-') {
		//      ordinare in modo crescente id tipo invio
				String idRecapito = getQuerySortingSegment("id_tipo_recapito");
				return idRecapito.concat(getQuerySortingSegment("id_tipo_invio").replace(" ORDER BY ", ","));
			} else {
				String idRecapito = getQuerySortingSegment("id_tipo_recapito");
				return idRecapito.concat(getQuerySortingSegment("-id_tipo_invio").replace(" ORDER BY ", ","));
	
			}
		}
		if (sort.contains("email")) {
			if (sort.substring(1).equals("email") && sort.charAt(0) == '-') {
				nomeCampo = sort.substring(0, 1).concat("email");
				String idRecapito = getQuerySortingSegment(sort.substring(0, 1).concat("id_tipo_recapito"));
				return idRecapito.concat(getQuerySortingSegment(nomeCampo).replace(" ORDER BY ", ","));
			} else {
				String idRecapito = getQuerySortingSegment("id_tipo_recapito");
				return idRecapito.concat(getQuerySortingSegment("email").replace(" ORDER BY ", ","));

			}
		}
		if (sort.contains("pec")) {
			if (sort.substring(1).equals("pec") && sort.charAt(0) == '-') {
				nomeCampo = sort.substring(0, 1).concat("pec");
				String idRecapito = getQuerySortingSegment(sort.substring(0, 1).concat("id_tipo_recapito"));
				return idRecapito.concat(getQuerySortingSegment(nomeCampo).replace(" ORDER BY ", ","));
			} else {
				String idRecapito = getQuerySortingSegment("id_tipo_recapito");
				return idRecapito.concat(getQuerySortingSegment("pec").replace(" ORDER BY ", ","));
			}
		}
		return nomeCampo;

	}

	
	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	@Override
	public RowMapper<SoggettiExtendedDTO> getRowMapper() throws SQLException {
		return new SoggettiRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}
	
    /**
     * The type soggetti row mapper.
     */
    public static class SoggettiRowMapper implements RowMapper<SoggettiExtendedDTO> {

        /**
         * Instantiates a new  soggetti row mapper.
         *
         * @throws SQLException the sql exception
         */
        public SoggettiRowMapper() throws SQLException {
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
        public SoggettiExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	SoggettiExtendedDTO bean = new SoggettiExtendedDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, SoggettiDTO bean) throws SQLException {
            bean.setIdSoggetto(rs.getLong("id_soggetto"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setIdTipoSoggetto(rs.getLong("id_tipo_soggetto"));
            bean.setCfSoggetto(rs.getString("cf_soggetto"));
            bean.setIdTipoNaturaGiuridica(rs.getLong("id_tipo_natura_giuridica") > 0 ? rs.getLong("id_tipo_natura_giuridica") : null);
            bean.setIdFonteOrigine(rs.getLong("id_fonte_origine") > 0 ? rs.getLong("id_fonte_origine") : null);
            bean.setIdFonte(rs.getLong("id_fonte") > 0 ? rs.getLong("id_fonte") : null);
            bean.setNome(rs.getString("nome"));
            bean.setCognome(rs.getString("cognome"));
            bean.setDenSoggetto(rs.getString("den_soggetto"));
            bean.setPartitaIvaSoggetto(rs.getString("partita_iva_soggetto"));
            bean.setDataNascitaSoggetto(rs.getString("data_nascita_soggetto"));
            bean.setIdComuneNascita(rs.getInt("id_comune_nascita") > 0 ? rs.getInt("id_comune_nascita") : null);
            bean.setIdStatoNascita(rs.getInt("id_stato_nascita") > 0 ? rs.getInt("id_stato_nascita") : null);
            bean.setCittaEsteraNascita(rs.getString("citta_estera_nascita"));
            bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
            bean.setGestDataIns(rs.getDate("gest_data_ins"));
            bean.setGestDataUpd(rs.getDate("gest_data_upd"));
            bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
            bean.setGestUid(rs.getString("gest_uid"));
            bean.setDataAggiornamento(rs.getString("data_aggiornamento"));
            bean.setDataCancellazione(rs.getString("data_cancellazione"));
        }
        
          
        
    }

	@Override
	public Integer countSoggettiByCampoRicerca(Long idAmbito, String campoRicerca) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    Map<String, Object> map = new HashMap<>();
	        map.put("idAmbito", idAmbito);
	        map.put("campoRicerca", campoRicerca);
	        MapSqlParameterSource params = getParameterValue(map);
			try {
				return template.queryForObject(QUERY_COUNT_SOGGETTO_BY_CAMPO_RICERCA, params, Integer.class);
			} catch (DataAccessException e) {
				LOGGER.error(getClassFunctionErrorInfo(className, methodName,e), e);
			}
			return null;	

	}

	@Override
	public SoggettiExtendedDTO getSoggettoById(Long idSoggetto) throws SQLException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    LOGGER.debug("[SoggettiDAOImpl::getSoggettoById] idSoggetto: "+idSoggetto);		
	    Map<String, Object> map = new HashMap<>();
	    map.put("idSoggetto", idSoggetto);
	    MapSqlParameterSource params = getParameterValue(map);
        SoggettiExtendedDTO soggetto = template.query(QUERY_GET_CAMPI_SOGGETTO_BY_ID, params, new ResultSetExtractor<SoggettiExtendedDTO>() {

		    public SoggettiExtendedDTO extractData(ResultSet rs) throws SQLException, DataAccessException {
		    	SoggettiExtendedDTO soggetto = new SoggettiExtendedDTO();
		        if (rs.next()) {
		        	soggetto.setCognome(rs.getString("cognome"));
		        	soggetto.setNome(rs.getString("nome"));
		        	soggetto.setDenSoggetto(rs.getString("den_soggetto"));
		        	soggetto.setIdTipoSoggetto(rs.getLong("id_tipo_soggetto"));
		        	soggetto.setCfSoggetto(rs.getString("cf_soggetto"));
		        	TipiSoggettoDTO tipiSoggetto = new TipiSoggettoDTO();
		        	tipiSoggetto.setCodTipoSoggetto(rs.getString("cod_tipo_soggetto"));
		        	soggetto.setTipoSoggetto(tipiSoggetto);
		        }
		        return soggetto;
		    }
		 });
	    LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		return soggetto;
	}
	
	@Override
	public long countSoggettoByCfOrPI(String cf) throws DAOException, SystemException {
		LOGGER.debug("[SoggettiDAOImpl::countSoggettoByCfOrPI] START");		
		LOGGER.debug("[SoggettiDAOImpl::countSoggettoByCfOrPI] cf: "+cf);		
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		sql.append("select count(*)  from risca_t_soggetto where cf_soggetto = :CF_PI or partita_iva_soggetto = :CF_PI ");		

		long conteggio = 0;

		paramMap.addValue("CF_PI", cf);
		
		try {
			
			conteggio = template.queryForObject(sql.toString(), paramMap, Long.class);

		} catch (RuntimeException ex) {
			LOGGER.error("[SoggettiDAOImpl::countSoggettoByCfOrPI] esecuzione query", ex);
			throw new DAOException("Query failed soggetto non trovato");
		} finally {
			LOGGER.debug("[SoggettiDAOImpl::countSoggettoByCfOrPI] END");
		}
		return conteggio;
	}
	
	@Override
	public SoggettiExtendedDTO loadIdTipoSoggettoCfPartIvaByIdSoggetto(Long idSoggetto) throws DataAccessException, SQLException, DAOException, SystemException {
		LOGGER.debug("[SoggettiDAOImpl::loadIdTipoSoggettoCfPartIvaByIdSoggetto] BEGIN");
		LOGGER.debug("[SoggettiDAOImpl::loadIdTipoSoggettoCfPartIvaByIdSoggetto] idSoggetto: "+idSoggetto);
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();		
		
		sql.append("select id_tipo_soggetto, decode(id_tipo_soggetto, 1, cf_soggetto , partita_iva_soggetto) cf_soggetto, ");
		sql.append(" decode(id_tipo_soggetto, 1, '00000000000'  , partita_iva_soggetto ) partita_iva_soggetto , cognome, nome,  to_char(data_nascita_soggetto, 'YYYYMMdd') data_nascita_soggetto,  ");
		sql.append(" (select rdc.cod_belfiore_comune from risca_d_comune rdc where rdc.id_comune =rts.id_comune_nascita)cod_belfiore_comune   ,  ");
		sql.append("  (select rdp.sigla_provincia  from risca_d_provincia rdp where rdp.id_provincia = (select rdc.id_provincia from risca_d_comune rdc where rdc.id_comune =rts.id_comune_nascita))sigla_provincia, den_soggetto   ");
		sql.append(" from risca_t_soggetto rts where rts.id_soggetto = :id_soggetto ");

		paramMap.addValue("id_soggetto", idSoggetto);
		LOGGER.debug("[SoggettiDAOImpl - loadIdTipoSoggettoCfPartIvaByIdSoggetto] query =" + sql.toString());

		SoggettiExtendedDTO soggettoDto = null;
		try
		{
			soggettoDto = template.queryForObject(sql.toString(), paramMap, new SoggettoDaoRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOGGER.debug("\"[SoggettiDAOImpl::loadIdTipoSoggettoCfPartIvaByIdSoggetto]  NESSUN RISULTATO");
			throw new DAOException("Nessun soggetto trovato in base alla richiesta");
		}
		catch (Throwable ex)
		{
			LOGGER.error("[SoggettiDAOImpl::loadIdTipoSoggettoCfPartIvaByIdSoggetto] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOGGER.debug("[SoggettiDAOImpl::loadIdTipoSoggettoCfPartIvaByIdSoggetto] END");
		}
		return soggettoDto;
	}
	
	@Override
	public String getTableNameAString() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
