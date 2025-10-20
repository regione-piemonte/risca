/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl.dao.impl.tributi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoUsoDAO;
import it.csi.risca.riscabesrv.dto.DatoTecnicoDTO;
import it.csi.risca.riscabesrv.dto.RiduzioneAumentoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneUsoDTO;
import it.csi.risca.riscabesrv.dto.TipoUsoRegolaDTO;
import it.csi.risca.riscabesrv.dto.ambiente.DatoTecnicoJsonDTO;
import it.csi.risca.riscabesrv.dto.ambiente.RiscossioneDatoTecnicoDTO;
import it.csi.risca.riscabesrv.dto.tributi.DatiTecniciTributiDTO;
import it.csi.risca.riscabesrv.dto.tributi.TipoUsoDatoTecnicoTributiDTO;

/**
 * The type Riscossione dao.
 *
 * @author CSI PIEMONTE
 */
public class DatoTecnicoTributiDAOImpl extends RiscaBeSrvGenericDAO<DatoTecnicoDTO> {


    @Autowired
    private TipoUsoDAO tipoUsoDAO;
    @Autowired
    private MessaggiDAO messaggiDAO;
    
    private static final String QUERY_UPDATE_RISCOSSIONE = "UPDATE risca_t_riscossione "
            + "SET json_dt = to_jsonb(:jsonDt::json) " 
            + "WHERE id_riscossione = :idRiscossione";
    
  
    private static final String QUERY_FOR_AMBITO = "   SELECT rda.cod_ambito AS codice  " + 
    		"   FROM risca_d_ambito rda  " + 
    		"   INNER JOIN risca_d_tipo_riscossione rdtr ON rda.id_ambito = rdtr.id_ambito " + 
    		"   INNER JOIN risca_t_riscossione rtr on rdtr.id_tipo_riscossione = rtr.id_tipo_riscossione " + 
    		"   WHERE rtr.id_riscossione = :idRiscossione";
    
	private static final String QUERY_LOAD_RISCOSSIONE_USO = "SELECT distinct rrru.* FROM risca_r_riscossione_uso rrru "
			+ "WHERE rrru.id_riscossione = :idRiscossione ";
	
	private static final String QUERY_DELETE_RISCOSSIONE_USO  = "DELETE FROM risca_r_riscossione_uso rrru "
			+ "WHERE rrru.id_riscossione_uso = :idRiscossioneUso ";
	
    private static final String QUERY_INSERT_RISCOSSIONE_USO = "INSERT INTO risca_r_riscossione_uso (id_riscossione_uso, id_riscossione, id_tipo_uso, "
            + "gest_data_ins, gest_attore_ins, gest_data_upd, gest_attore_upd, gest_uid) "
            + "VALUES(nextval('seq_risca_r_riscossione_uso'), :idRiscossione, :idTipoUso, "
            + ":gestDataIns, :gestAttoreIns, :gestDataUpd, :gestAttoreUpd, :gestUid)";
        


	public RiscossioneDatoTecnicoDTO saveDatoTecnico(RiscossioneDatoTecnicoDTO dto, String codFisc) throws Exception{
        LOGGER.debug("[DatoTecnicoTributiDAOImpl::saveDatoTecnico] BEGIN");
        JSONObject jsonRisc = new JSONObject();
        try {
            Map<String, Object> map = new HashMap<>();
   		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();
            
            map.put("idRiscossione", dto.getRiscossione().getIdRiscossione());
            String datiTecnici = dto.getRiscossione().getDatiTecnici();
    		
    		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
    		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    		DatiTecniciTributiDTO datiTecniciTributi = mapper.readValue(datiTecnici, DatiTecniciTributiDTO.class);

            Map<String, TipoUsoDatoTecnicoTributiDTO> mapDT = datiTecniciTributi.getUsi();
            //DatiGeneraliDTO dg = datiTecniciTributi.getDatiGenerali();
            Set<String> keys = mapDT.keySet();

            JSONObject jsonDT = datiTecniciTributi.toJsonObj();
            JSONObject json =  new JSONObject();
    		json.put("gest_UID", dto.getRiscossione().getGestUID());
    		json.put("id_riscossione", dto.getRiscossione().getIdRiscossione());
    		json.put("data_modifica", dto.getRiscossione().getDataModifica());
    		json.put("data_inserimento", dto.getRiscossione().getDataInserimento());
    		json.put("dati_tecnici", jsonDT);
            jsonRisc.put("riscossione", json);
            
            String jsonString =   jsonRisc.toString();

            map.put("jsonDt", jsonString);

            MapSqlParameterSource params = getParameterValue(map);

            template.update(getQuery(QUERY_UPDATE_RISCOSSIONE, null, null), params);
            
            
          	List<RiscossioneUsoDTO>   listRiscossioneUso = template.query(QUERY_LOAD_RISCOSSIONE_USO, params, getRiscossioneUsoRowMapper());
      			for (RiscossioneUsoDTO riscossioneUso : listRiscossioneUso) {
                	if(riscossioneUso.getIdRiscossioneUso() != null) {
                        map.put("idRiscossioneUso", riscossioneUso.getIdRiscossioneUso());
                        params = getParameterValue(map);
      					template.update(QUERY_DELETE_RISCOSSIONE_USO, params);
      				}
      			}  
                KeyHolder keyHolderProvv = new GeneratedKeyHolder();
                Number key = keyHolderProvv.getKey();
                for(String k : keys) {
      
    	            map.put("idRiscossione", dto.getRiscossione().getIdRiscossione());
    	            map.put("idTipoUso", mapDT.get(k).getIdTipoUsoLegge());
    	            map.put("gestDataIns", now);
    	            map.put("gestAttoreIns", codFisc);
    	            map.put("gestDataUpd", now);
    	            map.put("gestAttoreUpd", codFisc);
    	            map.put("gestUid", generateGestUID(codFisc + codFisc + now));
    	            MapSqlParameterSource paramsRiscUso = getParameterValue(map);
    	            template.update(getQuery(QUERY_INSERT_RISCOSSIONE_USO, null, null), paramsRiscUso, keyHolderProvv, new String[]{"id_riscossione_uso"});
//    	            if(mapDT.get(k).getTipoUsoSpecifico().length >0) {
//    	            	for (String tipoUsoSpecifico : mapDT.get(k).getTipoUsoSpecifico()) {
//    	            		TipoUsoExtendedDTO tipoUsoExtendedDTO = tipoUsoDAO.loadTipoUsoByIdTipoUsoOrCodTipoUso(tipoUsoSpecifico);
//    	            	    KeyHolder keyHolderProvvUsoSpecifico = new GeneratedKeyHolder();
//    	            		map.put("idRiscossione", dto.getRiscossione().getIdRiscossione());
//    			            map.put("idTipoUso", tipoUsoExtendedDTO.getIdTipoUso());
//    			            map.put("gestDataIns", now);
//    			            map.put("gestAttoreIns", codFisc);
//    			            map.put("gestDataUpd", now);
//    			            map.put("gestAttoreUpd", codFisc);
//    			            map.put("gestUid", generateGestUID(codFisc + codFisc + now));
//    			            MapSqlParameterSource paramsRiscSpeci = getParameterValue(map);
//    			            template.update(getQuery(QUERY_INSERT_RISCOSSIONE_USO, null, null), paramsRiscSpeci, keyHolderProvvUsoSpecifico, new String[]{"id_riscossione_uso"});
//    				            
//    					}
//    	            }
    	            key = keyHolderProvv.getKey();

                }
            
            
        
            String st = datiTecniciTributi.toJsonString();

            dto.getRiscossione().setDatiTecnici(st);
            return dto; //key.longValue();
        }catch (BusinessException e) {
        	LOGGER.error("[DatoTecnicoTributiDAOImpl::saveDatoTecnico] ERROR : " +e);
        	throw e;
        } 
        catch (Exception e) {
        	LOGGER.error("[DatoTecnicoTributiDAOImpl::saveDatoTecnico] ERROR : " +e);
        	if(e.getMessage().contains("Duplicate key")) {
        		messaggiDAO.loadMessaggiByCodMessaggio("E042");
        		throw new BusinessException(400, "E042", "uso di legge duplicato");
        	}

			return null;

        } finally {
            LOGGER.debug("[DatoTecnicoTributiDAOImpl::saveDatoTecnico] END");
        }

	}
	


	public RowMapper<RiscossioneUsoDTO> getRiscossioneUsoRowMapper() throws SQLException {
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
}
