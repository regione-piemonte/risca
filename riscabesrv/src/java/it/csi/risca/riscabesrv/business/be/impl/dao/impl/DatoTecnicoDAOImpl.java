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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.DatoTecnicoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.ambiente.DatoTecnicoAmbienteDAOImpl;
import it.csi.risca.riscabesrv.business.be.impl.dao.impl.tributi.DatoTecnicoTributiDAOImpl;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.DTGrandeIdroDTO;
import it.csi.risca.riscabesrv.dto.DatoTecnicoDTO;
import it.csi.risca.riscabesrv.dto.RiduzioneAumentoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneUsoDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatiTecniciAmbienteDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatoTecnicoJsonDTO;
import it.csi.risca.riscabesrv.dto.ambiente.RiscossioneDatoTecnicoDTO;
import it.csi.risca.riscabesrv.dto.ambiente.TipoUsoDatoTecnicoDTO;
import it.csi.risca.riscabesrv.dto.tributi.DatiTecniciTributiDTO;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type Riscossione dao.
 *
 * @author CSI PIEMONTE
 */
public class DatoTecnicoDAOImpl extends RiscaBeSrvGenericDAO<DatoTecnicoDTO> implements DatoTecnicoDAO {


	private final String className = this.getClass().getSimpleName();

	private static final String AMBIENTE = "AMBIENTE";
	private static final String OPERE_PUBBLICHE = "Opere pubbliche";
	private static final String ATTIVITA_ESTRATTIVE = "Attivita estrattive";
	private static final String TRIBUTI = "TRIBUTI";
	
	public static final String COD_TIPO_USO_GRANDE_IDROELETTRICO = "GRANDE_IDROELETTRICO";
    
    private static final String QUERY_UPDATE_RISCOSSIONE = "UPDATE risca_t_riscossione "
            + "SET json_dt = to_jsonb(:jsonDt::json) ,"
			+ " gest_data_upd = :gestDataUpd "
            + "WHERE id_riscossione = :idRiscossione";
    
    private static final String QUERY_INSERT_TIPO_USO_RIDAUM = "INSERT INTO risca_r_uso_ridaum (id_riscossione_uso, id_riduzione_aumento, "  
    		+ "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid, id_uso_ridaum ) " 
    	    + "VALUES(:idRiscossioneUso, :idRiduzioneAumento, "  
    		+ ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid, nextval('seq_risca_r_uso_ridaum'))";
    
    private static final String QUERY_INSERT_RISCOSSIONE_USO = "INSERT INTO risca_r_riscossione_uso (id_riscossione_uso, id_riscossione, id_tipo_uso, "
            + "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
            + "VALUES(nextval('seq_risca_r_riscossione_uso'), :idRiscossione, :idTipoUso, "
            + ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";
    
	private static final String QUERY_LOAD_RISCOSSIONE_USO = "SELECT distinct rrru.* FROM risca_r_riscossione_uso rrru "
			+ "WHERE rrru.id_riscossione = :idRiscossione ";
	
	private static final String QUERY_DELETE_TIPO_USO_RIDAUM = "DELETE FROM risca_r_uso_ridaum rrur "
			+ "WHERE rrur.id_riscossione_uso = :idRiscossioneUso ";
	
	private static final String QUERY_DELETE_RISCOSSIONE_USO  = "DELETE FROM risca_r_riscossione_uso rrru "
			+ "WHERE rrru.id_riscossione_uso = :idRiscossioneUso ";
	
    private static final String QUERY_FOR_AMBITO = "   SELECT rda.cod_ambito AS codice  " + 
    		"   FROM risca_d_ambito rda  " + 
    		"   INNER JOIN risca_d_tipo_riscossione rdtr ON rda.id_ambito = rdtr.id_ambito " + 
    		"   INNER JOIN risca_t_riscossione rtr on rdtr.id_tipo_riscossione = rtr.id_tipo_riscossione " + 
    		"   WHERE rtr.id_riscossione = :idRiscossione";
        
	private static final String DATA_RIFERIMENTO = "dataRiferimento";

//    private static final String QUERY_RISCOSSIONE_USO = "SELECT rrru.id_tipo_uso "
//    		+ "FROM risca_r_riscossione_uso rrru "
//            + "WHERE id_riscossione = :idRiscossione";
    
    private static final String QUERY_TIPO_USO_REGOLA= "SELECT rrtur.* "
    		+ "FROM risca_r_tipo_uso_regola rrtur " 
    		+ "WHERE id_tipo_uso in (:idTipoUso) "
    		+ "AND data_inizio <= :dataRiferimento "
    		+ "AND (data_fine is null OR data_fine > current_date)";
    
    private static final String QUERY_TIPO_USO_REGOLA_SINGOLO= "SELECT rrtur.* "
    		+ "FROM risca_r_tipo_uso_regola rrtur " 
    		+ "WHERE id_tipo_uso = :idTipoUso "
    		+ "AND data_inizio <= :dataRiferimento "
    		+ "AND (data_fine is null OR data_fine > current_date)";
    
//    private static final String QUERY_RISCOSSIONE_JSON_DT = "SELECT rtr.json_dt "
//    		+ "FROM risca_t_riscossione rtr "
//            + "WHERE id_riscossione = :idRiscossione";
 
    private static final String QUERY_ALGORITMO = "SELECT rda.script_algoritmo "
    		+ "FROM risca_d_algoritmo rda "
            + "WHERE id_algoritmo = :idAlgoritmo";
    
    private static final String QUERY_FOR_ID_TIPO_USO = "SELECT rdtu.id_tipo_uso "
    		+ "FROM risca_d_tipo_uso rdtu "
            + "WHERE cod_tipo_uso = :codTipoUso";
    
    private static final String QUERY_FOR_COD_TIPO_USO = "SELECT rdtu.cod_tipo_uso "
    		+ "FROM risca_d_tipo_uso rdtu "
            + "WHERE id_tipo_uso = :idTipoUso";

    private static final String QUERY_RIDUZIONE_AUMENTO = "SELECT distinct rdra.* "
            + "FROM risca_d_riduzione_aumento rdra ";
    
    private static final String QUERY_RIDUZIONE_AUMENTO_BY_COD_RIDUZIONE_AUMENTO = QUERY_RIDUZIONE_AUMENTO
            + "WHERE cod_riduzione_aumento = :codRiduzioneAumento ";
    
    private static final String QUERY_DATA_INIZIO_VALIDITA =" rdra.data_inizio_validita <= :dataAttuale ";
    
    private static final String QUERY_DATA_FINE_VALIDITA =" rdra.data_fine_validita >= :dataAttuale";

    private static final String ORDER_BY_ORDINA_RIDUZIONE_AUMENTO =" ORDER BY rdra.ordina_riduzione_aumento ASC ";

	
    @Autowired
    private MessaggiDAO messaggiDAO;
    @Autowired
    private TipoUsoDAO tipoUsoDAO;
    
	@Autowired
	private AmbitiDAO ambitiDAO;
    
	@Autowired
	private DatoTecnicoAmbienteDAOImpl datoTecnicoAmbienteDaoImpl;
	
	@Autowired
	private DatoTecnicoTributiDAOImpl datoTecnicoTributiDaoImpl;
	
	@Autowired
	private RiscossioneDAO riscossioneDAO;
    @Autowired
    private BusinessLogic businessLogic;

    
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public RiscossioneDatoTecnicoDTO saveDatoTecnico(RiscossioneDatoTecnicoDTO dto, String codFisc, Long idAmbito) throws Exception{
        LOGGER.debug("[DatoTecnicoDAOImpl::saveDatoTecnico] BEGIN");
        
    	String ambito = "";
    	RiscossioneDatoTecnicoDTO riscosDatoTecino = new RiscossioneDatoTecnicoDTO();
    	Utils utils = new Utils();
        if(utils.isLocalMod()){
        	ambito = AMBIENTE;
		    businessLogic.validatorDTO(null, null, dto.getRiscossione().getDatiTecnici());
		    businessLogic.validatorDTO(null, DatiTecniciAmbienteDTO.class, dto.getRiscossione().getDatiTecnici());
        	riscosDatoTecino = datoTecnicoAmbienteDaoImpl.saveDatoTecnico(dto, codFisc);
    	}else {
    			//TO DO QUERY SU TABELLA AMBITO PER VERIFICA AMBITO
    			LOGGER.debug("[DatoTecnicoDAOImpl::saveDatoTecnico] verifica ambito");
    			AmbitoDTO ambitoDTO = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
    			ambito = ambitoDTO.getCodAmbito();
    			LOGGER.debug("[DatoTecnicoDAOImpl::saveDatoTecnico] ambito trovato: " + ambito);
    			switch (ambito) {
    			  case AMBIENTE:
    				LOGGER.debug("[DatoTecnicoDAOImpl::saveDatoTecnico] ambito: AMBIENTE"); 
    				// DP SE FRUITORE BATCH_PORTING cod fisc MIGRAZIONE NON VERIFICO IL JSON
    			    if(codFisc != null  && !codFisc.equals(Constants.MIGRAZIONE))  {
		    		    businessLogic.validatorDTO(null, null, dto.getRiscossione().getDatiTecnici());
		    			businessLogic.validatorDTO(null, DatiTecniciAmbienteDTO.class, dto.getRiscossione().getDatiTecnici());
    			    }
    			    riscosDatoTecino = datoTecnicoAmbienteDaoImpl.saveDatoTecnico(dto, codFisc);
    			    break;
    			  case OPERE_PUBBLICHE:
    				//TO DO
    			    break;
    			  case ATTIVITA_ESTRATTIVE:
    				//TO DO
    			    break;
    			  case TRIBUTI:
    				  LOGGER.debug("[DatoTecnicoDAOImpl::saveDatoTecnico] ambito: TRIBUTI");
      			      businessLogic.validatorDTO(null, null, dto.getRiscossione().getDatiTecnici());
      			      businessLogic.validatorDTO(null, DatiTecniciTributiDTO.class, dto.getRiscossione().getDatiTecnici());
    				  riscosDatoTecino = datoTecnicoTributiDaoImpl.saveDatoTecnico(dto, codFisc);
    				  break;
    			}
    		}	   
    		LOGGER.debug("[DatoTecnicoDAOImpl::saveDatoTecnico] END");
            return riscosDatoTecino;


	}
	
	private BigDecimal calcolaCanone(String algoritmo, String parametri, String datiTecnici){
		LOGGER.debug("[DatoTecnicoDAOImpl::calcolaCanone] BEGIN calcolo canone singolo");
    	ScriptEngineManager factory = new ScriptEngineManager();
    	ScriptEngine engine = factory.getEngineByName("groovy");

    	try {
			engine.eval(algoritmo);
		} catch (ScriptException e) {
			  LOGGER.error("[DatoTecnicoDAOImpl::calcolaCanone] Errore nel caricamento dell'algoritmo", e);
		}

    	Invocable inv = (Invocable) engine;
    	try {
			Object result = inv.invokeFunction("calcolaCanone", parametri, datiTecnici);
			BigDecimal ret = BigDecimal.ZERO;
	         if(result instanceof BigDecimal) {
	               ret = (BigDecimal) result;
	            } else if(result instanceof Integer) {
	                ret = new BigDecimal((Integer) result);
	            } else if(result instanceof Long) {
	                ret = new BigDecimal((Long) result);
	            } 
	        LOGGER.debug("[DatoTecnicoDAOImpl::calcolaCanone] END");
			return ret;
		} catch (NoSuchMethodException e) {
			  LOGGER.error("[DatoTecnicoDAOImpl::calcolaCanone] Errore esecuzione algoritmo ", e);
		} catch (ScriptException e) {
			  LOGGER.error("[DatoTecnicoDAOImpl::calcolaCanone] Errore Script", e);
		}
    	LOGGER.debug("[DatoTecnicoDAOImpl::calcolaCanone] END calcolo canone singolo");
    	return BigDecimal.ZERO;
    }


	public RowMapper getRiscossioneUsoRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return new RiscossioneUsoRowMapper();
	}


	@Override
	public String getPrimaryKeySelect() {
		return null;
	}

	
	@Override
	public RowMapper<DatoTecnicoDTO> getRowMapper() throws SQLException {
		return new DatoTecnicoRowMapper();
	}
	

	public RowMapper<DatoTecnicoJsonDTO> getRowMapperJsonDT() throws SQLException {
		return new DatoTecnicoJsonRowMapper();
	}

	@Override
	public RowMapper<?> getExtendedRowMapper() throws SQLException {
		return null;
	}
	
    /**
     * The type Tipo riscossione row mapper.
     */
	 public static class RiscossioneUsoRowMapper implements RowMapper<RiscossioneUsoDTO> {

	        /**
	         * Instantiates a new Tipo adempimento row mapper.
	         *
	         * @throws SQLException the sql exception
	         */
	        public RiscossioneUsoRowMapper() throws SQLException {
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
	        public RiscossioneUsoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	RiscossioneUsoDTO bean = new RiscossioneUsoDTO();
	            populateBean(rs, bean);
	            return bean;
	        }

	        private void populateBean(ResultSet rs, RiscossioneUsoDTO bean) throws SQLException {
	            bean.setIdRiscossioneUso(rs.getLong("id_riscossione_uso"));
	            bean.setIdRiscossione(rs.getLong("id_riscossione"));
	            bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
	            bean.setGestDataIns(rs.getDate("gest_data_ins"));
				bean.setGestAttoreIns(rs.getString("gest_attore_ins"));
				bean.setGestDataUpd(rs.getDate("gest_data_upd"));
				bean.setGestAttoreUpd(rs.getString("gest_attore_upd"));
				bean.setGestUid(rs.getString("gest_uid"));

	        }
	       
	 }
    /**
     * The type Tipo riscossione row mapper.
     */
    public static class DatoTecnicoJsonRowMapper implements RowMapper<DatoTecnicoJsonDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public DatoTecnicoJsonRowMapper() throws SQLException {
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
        public DatoTecnicoJsonDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	DatoTecnicoJsonDTO bean = new DatoTecnicoJsonDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, DatoTecnicoJsonDTO bean) throws SQLException {
            bean.setIdRiscossione(rs.getLong("id_riscossione"));
            bean.setDataModifica(rs.getString("data_modifica"));
            bean.setDataInserimento(rs.getString("data_inserimento"));
            bean.setDatiTecnici(rs.getString("dati_tecnici"));
            bean.setGestUID(rs.getString("gest_uid"));
        }
    }
    
    public static class DatoTecnicoRowMapper implements RowMapper<DatoTecnicoDTO> {

        /**
         * Instantiates a new Tipo adempimento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public DatoTecnicoRowMapper() throws SQLException {
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
        public DatoTecnicoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            DatoTecnicoDTO bean = new DatoTecnicoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, DatoTecnicoDTO bean) throws SQLException {
            bean.setIdDatoTecnico(rs.getLong("id_riscossione"));
            bean.setCodDatoTecnico(rs.getString("id_template"));
            bean.setDesDatoTecnico(rs.getString("id_stato_riscossione"));
            bean.setIdUnitaMisura(rs.getLong("id_soggetto"));
            bean.setIdTipoDatoTecnico(rs.getLong("id_tipo_riscossione"));
            bean.setFlgCalcolato(rs.getString("id_gruppo_soggetto"));

        }
    }


	public RowMapper<TipoUsoRegolaDTO> getTipoUsoRegolaRowMapper() throws SQLException {
		return new TipoUsoRegolaRowMapper();
	}


	public RowMapper<?> getExtendedTipoUsoRegolaRowMapper() throws SQLException {
		return null;
	}

    public static class TipoUsoRegolaRowMapper implements RowMapper<TipoUsoRegolaDTO> {

        /**
         * Instantiates a new tipo uso regola row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoUsoRegolaRowMapper() throws SQLException {
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
        public TipoUsoRegolaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	TipoUsoRegolaDTO bean = new TipoUsoRegolaDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipoUsoRegolaDTO bean) throws SQLException {
            bean.setIdTipoUsoRegola(rs.getLong("id_tipo_uso_regola"));
            bean.setIdTipoUso(rs.getLong("id_tipo_uso"));
            bean.setDataInizio(rs.getString("data_inizio"));
            bean.setDataFine(rs.getString("data_fine"));
            bean.setJsonRegola(rs.getString("json_regola"));
            bean.setIdAlgoritmo(rs.getLong("id_algoritmo"));

        }
    }

	public RowMapper<RiduzioneAumentoDTO> getRowMapperRiduzioneAumento() throws SQLException {
		 return new RiduzioneAumentoRowMapper();
	}
    /**
     * The type Riduzione Aumento row mapper.
     */
    public static class RiduzioneAumentoRowMapper implements RowMapper<RiduzioneAumentoDTO> {

        /**
         * Instantiates a new Riduzione Aumento row mapper.
         *
         * @throws SQLException the sql exception
         */
        public RiduzioneAumentoRowMapper() throws SQLException {
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
        public RiduzioneAumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            RiduzioneAumentoDTO bean = new RiduzioneAumentoDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, RiduzioneAumentoDTO bean) throws SQLException {
            bean.setIdRiduzioneAumento(rs.getLong("id_riduzione_aumento"));
            bean.setSiglaRiduzioneAumento(rs.getString("sigla_riduzione_aumento"));
            bean.setDesRiduzioneAumento(rs.getString("des_riduzione_aumento"));
            bean.setPercRiduzioneAumento(rs.getLong("perc_riduzione_aumento"));
            bean.setFlgRiduzioneAumento(rs.getString("flg_riduzione_aumento"));
            bean.setFlgManuale(rs.getString("flg_manuale"));
            bean.setFlgDaApplicare(rs.getString("flg_da_applicare"));
            bean.setOrdinaRiduzioneAumento(rs.getLong("ordina_riduzione_aumento"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
            
        }
    }


	@Override
	public RiscossioneDatoTecnicoDTO updateDTGrandeIdro(Integer idRiscossione, DTGrandeIdroDTO dTGrandeIdroDTO,
	        HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws BusinessException, Exception {
	    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

	    // Validazione dei dati in ingresso
	    if (idRiscossione == null || dTGrandeIdroDTO == null) {
	        throw new BusinessException("ID riscossione ed i dati tecnici del grande idroelettrico devono essere specificati.");
	    }
	    RiscossioneDatoTecnicoDTO riscossioneDatoTecnicoDTO = new RiscossioneDatoTecnicoDTO();

	    try {
	        String jsonDt = riscossioneDAO.loadDatoTecnico(Long.valueOf(idRiscossione));
	        DatiTecniciAmbienteDTO datiTecniciAmbiente = Utils.creaDatiTecniciFromJsonDt(jsonDt, "dati_tecnici");
	        TipoUsoDatoTecnicoDTO tipoUsoGI = datiTecniciAmbiente.getUsi().get(COD_TIPO_USO_GRANDE_IDROELETTRICO);
	        if (tipoUsoGI != null) {
	            updateTipoUsoField(tipoUsoGI,dTGrandeIdroDTO);
	            JSONObject newJsonDt = datiTecniciAmbiente.toJsonObj();
		        updateJsonDt(idRiscossione, newJsonDt);
		        riscossioneDatoTecnicoDTO = createRiscossioneDatoTecnicoDTO(idRiscossione, newJsonDt);
	        }

	    } catch (JsonProcessingException e) {
	        LOGGER.error("Errore durante il parsing JSON.", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	        throw new BusinessException("Errore durante il parsing JSON.");
	    } catch (Exception e) {
	        LOGGER.error(getClassFunctionErrorInfo(className, methodName, e));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	        throw new Exception("Errore durante l'aggiornamento dei dati tecnici.", e);
	    } finally {
	        LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	    }

	    return riscossioneDatoTecnicoDTO;
	}

	private void updateTipoUsoField(TipoUsoDatoTecnicoDTO tipoUso, DTGrandeIdroDTO dTGrandeIdroDTO) {
		tipoUso.setCoeffEnergGrat(dTGrandeIdroDTO.getCoeffEnergGrat());
		tipoUso.setExtraProfitti(dTGrandeIdroDTO.getExtraProfitti());
		tipoUso.setPnmPerEnergGrat(dTGrandeIdroDTO.getPnmPerEnergGrat());
		tipoUso.setPercQuotaVar(dTGrandeIdroDTO.getPercQuotaVar());

	}

	private void updateJsonDt(Integer idRiscossione, JSONObject jsonDT) {
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        JSONObject jsonRisc = new JSONObject();
        JSONObject json =  new JSONObject();
		json.put("gest_UID", "");
		json.put("id_riscossione", idRiscossione);
		json.put("data_modifica", formatter.format(now));
		json.put("data_inserimento",formatter.format(now));
		json.put("dati_tecnici", jsonDT);
        jsonRisc.put("riscossione", json);
        
        String newJsonDt =   jsonRisc.toString();
	    Map<String, Object> params = new HashMap<>();
	    params.put("idRiscossione", idRiscossione);
	    params.put("jsonDt", newJsonDt);
	    params.put("gestDataUpd", now);
	    MapSqlParameterSource parameters = getParameterValue(params);
	    template.update(getQuery(QUERY_UPDATE_RISCOSSIONE, null, null), parameters);
	}

	private RiscossioneDatoTecnicoDTO createRiscossioneDatoTecnicoDTO(Integer idRiscossione, JSONObject newJsonDt) {
	    RiscossioneDatoTecnicoDTO riscossioneDatoTecnicoDTO = new RiscossioneDatoTecnicoDTO();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        DatoTecnicoJsonDTO datoTecnicoJsonDTO = new DatoTecnicoJsonDTO();
        datoTecnicoJsonDTO.setDataInserimento(formatter.format(now));
        datoTecnicoJsonDTO.setDataModifica(formatter.format(now));
        datoTecnicoJsonDTO.setDatiTecnici(newJsonDt.toString());
        datoTecnicoJsonDTO.setIdRiscossione(Long.valueOf(idRiscossione));
        datoTecnicoJsonDTO.setGestUID(null);
        riscossioneDatoTecnicoDTO.setRiscossione(datoTecnicoJsonDTO);
	    return riscossioneDatoTecnicoDTO;
	}


}
