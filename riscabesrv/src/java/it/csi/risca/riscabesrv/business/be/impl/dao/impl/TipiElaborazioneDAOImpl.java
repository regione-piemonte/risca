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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import it.csi.risca.riscabesrv.business.be.impl.dao.RiscaBeSrvGenericDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiElaborazioneDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;
import it.csi.risca.riscabesrv.dto.FunzionalitaDTO;
import it.csi.risca.riscabesrv.dto.TipoElaboraDTO;
import it.csi.risca.riscabesrv.dto.TipoElaboraExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi Elaborazione DAO Impl.
 *
 * @author CSI PIEMONTE
 */
public class TipiElaborazioneDAOImpl extends RiscaBeSrvGenericDAO<TipoElaboraDTO> implements TipiElaborazioneDAO {

	private final String CLASSNAME = this.getClass().getSimpleName();
	
	private static final String ID_AMBITO = "idAmbito";
	
    private static final String QUERY_TIPO_ELABORA = "	select rdte.*, rda.*,rdf.* from risca_d_tipo_elabora rdte  "
    		+ "	inner join risca_d_ambito rda on rdte.id_ambito = rda.id_ambito  "
    		+ "	inner join risca_d_funzionalita rdf on rdte.id_funzionalita = rdf.id_funzionalita ";
  
    private static final String QUERY_ORDER_BY_TIPO_ELABORA ="	order by rdte.ordina_tipo_elabora ";
    

    private static final String QUERY_DATA_INIZIO_VALIDITA =" rdte.data_inizio_validita <= :dataAttuale ";
    
    private static final String QUERY_DATA_FINE_VALIDITA =" (rdte.data_fine_validita >= :dataAttuale  OR rdte.data_fine_validita is null)";

    private static final String QUERY_TIPO_ELABORA_BY_ID_AMBITO_ID_FUNZIONALITA_FLG_VISIBILE = QUERY_TIPO_ELABORA
            + "	where rdte.id_ambito = :idAmbito  "
            + "	and rdf.id_funzionalita  = :idFunzionalita "
            + "	and rdte.flg_visibile = :flgVisible  " 
            + " and "+ QUERY_DATA_INIZIO_VALIDITA
            + " and "+ QUERY_DATA_FINE_VALIDITA
            + QUERY_ORDER_BY_TIPO_ELABORA;
    


 
	@Override
	public List<TipoElaboraExtendedDTO> loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile(Long idAmbito, Long idFunzionalita,
			Integer flgVisible) throws Exception {
        LOGGER.debug("[TipiElaborazioneDAOImpl::loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile] BEGIN");
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();  
        Map<String, Object> map = new HashMap<>();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        Date dataAttuale = null;
		try {
			dataAttuale = formatter.parse(formatter.format(now));
		} catch (ParseException e) {
			 LOGGER.error("[TipiElaborazioneDAOImpl::loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile] Errore nel paarse dei dati", e);
	           
		}
        map.put("dataAttuale", dataAttuale);
        map.put(ID_AMBITO, idAmbito);
        map.put("idFunzionalita", idFunzionalita);
        map.put("flgVisible", flgVisible);
        try {
        	return findListByQuery(CLASSNAME, methodName, QUERY_TIPO_ELABORA_BY_ID_AMBITO_ID_FUNZIONALITA_FLG_VISIBILE, map);
			
        } catch(EmptyResultDataAccessException e) {
		    LOGGER.debug("[TipiElaborazioneDAOImpl::loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile] No record found in database for Tipo Elabora ", e);
		  return Collections.emptyList();
		}  catch (DataAccessException e) {
            LOGGER.error("[TipiElaborazioneDAOImpl::loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile] Errore nell'accesso ai dati", e);
			throw new Exception(Constants.ERRORE_GENERICO);
        } finally {
            LOGGER.debug("[TipiElaborazioneDAOImpl::loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile] END");
        }

	}

	@Override
	public String getPrimaryKeySelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<TipoElaboraDTO> getRowMapper() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowMapper<TipoElaboraExtendedDTO> getExtendedRowMapper() throws SQLException {

		return new TipoElaboraExtendedRowMapper();
	}

    /**
     * The type Tipo Elabora row mapper.
     */
    public static class TipoElaboraExtendedRowMapper implements RowMapper<TipoElaboraExtendedDTO> {

        /**
         * Instantiates a new Tipo Elabora row mapper.
         *
         * @throws SQLException the sql exception
         */
        public TipoElaboraExtendedRowMapper() throws SQLException {
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
        public TipoElaboraExtendedDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	TipoElaboraExtendedDTO bean = new TipoElaboraExtendedDTO();
            populateBean(rs, bean);
            return bean;
        }

        private void populateBean(ResultSet rs, TipoElaboraExtendedDTO bean) throws SQLException {
        	AmbitoDTO ambito = new AmbitoDTO();
        	popolaAmbito(rs, ambito);
        	bean.setAmbito(ambito);
        	FunzionalitaDTO funzionalita = new FunzionalitaDTO();
        	popolaFunzionalita(rs, funzionalita);
        	bean.setFunzionalitaDTO(funzionalita); 
            bean.setIdTipoElabora(rs.getLong("id_tipo_elabora"));
            bean.setIdAmbito(rs.getLong("id_ambito"));
            bean.setIdFunzionalita(rs.getLong("id_funzionalita"));
            bean.setCodTipoElabora(rs.getString("cod_tipo_elabora"));
            bean.setDesTipoElabora(rs.getString("des_tipo_elabora"));
            bean.setOrdinaTipoElabora(rs.getLong("ordina_tipo_elabora"));
            bean.setFlgDefault(rs.getInt("flg_default"));
            bean.setFlgVisibile(rs.getInt("flg_visibile"));
            bean.setDataInizioValidita(rs.getDate("data_inizio_validita"));
            bean.setDataFineValidita(rs.getDate("data_fine_validita"));
        }

		private void popolaFunzionalita(ResultSet rs, FunzionalitaDTO funzionalita) throws SQLException {
			funzionalita.setIdFunzionalita(rs.getLong("id_funzionalita"));
			funzionalita.setCodFunzionalita(rs.getString("cod_funzionalita"));
			funzionalita.setDesFunzionalita(rs.getString("des_funzionalita"));
			
		}

		private void popolaAmbito(ResultSet rs, AmbitoDTO ambito) throws SQLException {
			ambito.setIdAmbito(rs.getLong("id_ambito"));
			ambito.setCodAmbito(rs.getString("cod_ambito"));
			ambito.setDesAmbito(rs.getString("des_ambito"));
			
		}
    }
	
	

}
